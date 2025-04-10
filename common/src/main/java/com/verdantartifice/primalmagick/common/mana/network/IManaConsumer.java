package com.verdantartifice.primalmagick.common.mana.network;

import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.ManaSparklePacket;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Interface identifying a network node which can receive mana from other network nodes and, optionally, connect
 * directly to devices to sink mana out of the network.
 *
 * @author Daedalus4096
 */
public interface IManaConsumer extends IManaNetworkNode {
    /**
     * Gets whether this consumer is a valid terminus for a network transmission packet.
     *
     * @return true if this consumer can sink mana from the network, or false if it's merely a relay
     */
    default boolean isTerminus() {
        return true;
    }

    /**
     * Gets whether this consumer can sink the given source of mana out of the network.
     *
     * @param source the source of mana to be queried
     * @return true if this consumer can sink the given source of mana out of the network, false otherwise
     */
    boolean canConsume(@NotNull Source source);

    /**
     * Receives the given source and amount of mana from the network and inserts it to storage.
     *
     * @param source source of mana to be inserted
     * @param maxReceive maximum amount of centimana to be inserted
     * @param simulate if {@code true}, the insertion will only be simulated
     * @return amount of centimana that was (or would have been, if simulated) inserted into storage
     */
    int receiveMana(@NotNull Source source, int maxReceive, boolean simulate);

    /**
     * Requests the given source and amount of mana from the network to be consumed. The network supplies mana from all
     * nodes which can support the request along the best known and active route for each.
     *
     * @param level the level in which this consumer resides
     * @param source the source of mana to be transferred
     * @param maxTransferCentimana the maximum amount of centimana to be transferred
     */
    default void doSiphon(@NotNull Level level, @NotNull Source source, final int maxTransferCentimana) {
        int remainingTransfer = maxTransferCentimana;
        RouteTable routeTable = this.getRouteTable();
        Set<Route.Hop> particleHops = new HashSet<>();

        // Get the best route for each origin linked to this terminus that can carry the requested source
        List<Route> routes = routeTable.getLinkedOrigins(this).stream()
                .map(supplier -> routeTable.getRoute(level, Optional.of(source), supplier, this, this))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(Route::getMaxThroughput).reversed().thenComparing(Route::hashCode))
                .toList();
        Iterator<Route> routeIterator = routes.iterator();

        // Transfer mana directly from the origin to the terminus
        while (remainingTransfer > 0 && routeIterator.hasNext()) {
            Route route = routeIterator.next();
            int toExtract = Math.min(remainingTransfer, route.getMaxThroughput());
            int actualExtracted = route.getHead().extractMana(source, toExtract, false);
            int actualReceived = route.getTail().receiveMana(source, actualExtracted, false);
            remainingTransfer -= actualReceived;
            particleHops.addAll(route.getHops());
        }

        // Show particles for each hop in the route
        if (level instanceof ServerLevel serverLevel) {
            particleHops.forEach(hop -> PacketHandler.sendToAllAround(
                    new ManaSparklePacket(hop.supplier().getBlockPos().getCenter(), hop.consumer().getBlockPos().getCenter(), 20, source.getColor()),
                    serverLevel,
                    hop.supplier().getBlockPos(),
                    32D));
        }
    }

    /**
     * Scans this consumer's surroundings for other mana network nodes and connects this consumer to the network.
     *
     * @param level the world in which this consumer resides
     */
    default void loadManaNetwork(@NotNull Level level) {
        int range = this.getNetworkRange();
        int rangeSqr = range * range;

        // Search for mana suppliers which are in range of this node
        List<IManaSupplier> suppliers = BlockPos.betweenClosedStream(new AABB(this.getBlockPos()).inflate(range))
                .filter(pos -> pos.distSqr(this.getBlockPos()) <= rangeSqr)
                .map(pos -> level.getBlockEntity(pos) instanceof IManaSupplier supplier ? supplier : null)
                .filter(Objects::nonNull)
                .toList();

        // Create direct routes to this consumer for origin suppliers
        suppliers.stream().filter(IManaSupplier::isOrigin)
                .map(supplier -> new Route(supplier, this))
                .filter(Route::isValid)
                .forEach(this.getRouteTable()::addRoute);

        // For suppliers that are actually relays, append this consumer to each of the routes that end in that supplier
        suppliers.stream().map(supplier -> supplier instanceof IManaRelay relay ? relay : null)
                .filter(Objects::nonNull)
                .flatMap(relay -> relay.getRouteTable().getRoutesForTail(relay).stream())
                .map(route -> route.pushTail(this))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Route::isValid)
                .forEach(this.getRouteTable()::addRoute);

        // Update connected nodes on the newly created routes
        this.getRouteTable().propagateRoutes(Set.of(this));
    }
}

package com.verdantartifice.primalmagick.common.mana.network;

import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Interface identifying a relay in a mana network, a device which can both transmit and receive mana but cannot
 * directly connect to devices to store it.
 *
 * @author Daedalus4096
 */
public interface IManaRelay extends IManaSupplier, IManaConsumer {
    @Override
    default boolean isTerminus() {
        return false;
    }

    @Override
    default boolean isOrigin() {
        return false;
    }

    @Override
    default int extractMana(@NotNull Source source, int maxExtract, boolean simulate) {
        // Relays don't contain mana, they only pass it along
        return 0;
    }

    @Override
    default int receiveMana(@NotNull Source source, int maxReceive, boolean simulate) {
        // Relays don't contain mana, they only pass it along
        return 0;
    }

    @Override
    default boolean canSupply(@NotNull Source source) {
        return this.canRelay(source);
    }

    @Override
    default boolean canConsume(@NotNull Source source) {
        return this.canRelay(source);
    }

    /**
     * Gets whether this relay can transmit the given source of mana through the network.
     *
     * @param source the source of mana to be queried
     * @return true if this relay can transmit the given source of mana through the network, false otherwise
     */
    boolean canRelay(Source source);

    @Override
    default void loadManaNetwork(@NotNull Level level) {
        level.getProfiler().push("loadManaNetwork");
        level.getProfiler().push("defaultManaRelay");

        int range = this.getNetworkRange();
        int rangeSqr = range * range;

        // Search for mana network nodes in range of this one
        level.getProfiler().push("findNodes");
        List<IManaNetworkNode> nodes = BlockPos.betweenClosedStream(new AABB(this.getBlockPos()).inflate(range))
                .filter(pos -> !this.getBlockPos().equals(pos) && pos.distSqr(this.getBlockPos()) <= rangeSqr)
                .map(pos -> level.getBlockEntity(pos) instanceof IManaNetworkNode node ? node : null)
                .filter(Objects::nonNull)
                .toList();

        // Create direct routes to this relay for origin suppliers
        level.getProfiler().popPush("createDirectSupplierRoutes");
        nodes.stream().map(node -> node instanceof IManaSupplier supplier ? supplier : null)
                .filter(supplier -> supplier != null && supplier.isOrigin())
                .map(supplier -> new Route(supplier, this))
                .filter(Route::isValid)
                .forEach(this.getRouteTable()::addRoute);

        // Create direct routes to this relay for terminus consumers
        level.getProfiler().popPush("createDirectConsumerRoutes");
        nodes.stream().map(node -> node instanceof IManaConsumer consumer ? consumer : null)
                .filter(consumer -> consumer != null && consumer.isTerminus())
                .map(consumer -> new Route(this, consumer))
                .filter(Route::isValid)
                .forEach(this.getRouteTable()::addRoute);

        // For nodes that are actually relays, create bidirectional routes connecting this and that
        level.getProfiler().popPush("createRelayRoutes");
        List<IManaRelay> relays = nodes.stream().map(node -> node instanceof IManaRelay relay ? relay : null)
                .filter(Objects::nonNull)
                .toList();
        relays.stream().flatMap(relay -> relay.getRouteTable().getRoutesForHead(relay).stream())
                .map(route -> route.pushHead(this))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Route::isValid)
                .forEach(this.getRouteTable()::addRoute);
        relays.stream().flatMap(relay -> relay.getRouteTable().getRoutesForTail(relay).stream())
                .map(route -> route.pushTail(this))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Route::isValid)
                .forEach(this.getRouteTable()::addRoute);

        // Connect existing routes that can use this relay as a bridge
        level.getProfiler().popPush("bridgeRoutes");
        Set<Route> heads = this.getRouteTable().getRoutesForTail(this);
        Set<Route> tails = this.getRouteTable().getRoutesForHead(this);
        heads.forEach(head -> tails.stream().map(head::connect)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Route::isValid)
                .forEach(this.getRouteTable()::addRoute));

        // Update connected nodes on the newly created routes
        level.getProfiler().popPush("propagateRoutes");
        this.getRouteTable().propagateRoutes(Set.of(this));
        level.getProfiler().pop();

        level.getProfiler().pop();
        level.getProfiler().pop();
    }
}

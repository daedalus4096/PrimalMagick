package com.verdantartifice.primalmagick.common.mana.network;

import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.platform.Services;
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

    boolean canRelay(Source source);

    @Override
    default void loadManaNetwork(@NotNull Level level) {
        int range = this.getNetworkRange();
        int rangeSqr = range * range;

        // Search for mana network nodes in range of this one
        List<IManaNetworkNode> nodes = BlockPos.betweenClosedStream(new AABB(this.getBlockPos()).inflate(range))
                .filter(pos -> !this.getBlockPos().equals(pos) && pos.distSqr(this.getBlockPos()) <= rangeSqr)
                .map(pos -> level.getBlockEntity(pos) instanceof IManaNetworkNode node ? node : null)
                .filter(Objects::nonNull)
                .toList();

        // Create direct routes to this relay for origin suppliers
        nodes.stream().map(node -> node instanceof IManaSupplier supplier ? supplier : null)
                .filter(supplier -> supplier != null && supplier.isOrigin())
                .map(supplier -> new Route(supplier, this))
                .filter(Route::isValid)
                .forEach(this.getRouteTable()::addRoute);

        // Create direct routes to this relay for terminus consumers
        nodes.stream().map(node -> node instanceof IManaConsumer consumer ? consumer : null)
                .filter(consumer -> consumer != null && consumer.isTerminus())
                .map(consumer -> new Route(this, consumer))
                .filter(Route::isValid)
                .forEach(this.getRouteTable()::addRoute);

        // For nodes that are actually relays, create bidirectional routes connecting this and that
        List<IManaRelay> relays = nodes.stream().map(node -> node instanceof IManaRelay relay ? relay : null)
                .filter(Objects::nonNull)
                .toList();
        relays.stream().flatMap(relay -> relay.getRouteTable().getRoutesForOrigin(relay).stream())
                .map(route -> route.pushOrigin(this))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Route::isValid)
                .forEach(this.getRouteTable()::addRoute);
        relays.stream().flatMap(relay -> relay.getRouteTable().getRoutesForTerminus(relay).stream())
                .map(route -> route.pushTerminus(this))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Route::isValid)
                .forEach(this.getRouteTable()::addRoute);

        // Connect existing routes that can use this relay as a bridge
        Set<Route> heads = this.getRouteTable().getRoutesForTerminus(this);
        Set<Route> tails = this.getRouteTable().getRoutesForOrigin(this);
        heads.forEach(head -> tails.stream().map(head::connect)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Route::isValid)
                .forEach(this.getRouteTable()::addRoute));

        // Update connected nodes on the newly created routes
        this.getRouteTable().propagateRoutes(Set.of(this));
    }
}

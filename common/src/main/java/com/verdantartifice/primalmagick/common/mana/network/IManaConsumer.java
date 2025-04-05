package com.verdantartifice.primalmagick.common.mana.network;

import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
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
    default boolean isTerminus() {
        return true;
    }

    boolean canConsume(Source source);

    default void onPlaced(Level level) {
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
                .flatMap(relay -> relay.getRouteTable().getRoutesForTerminus(relay).stream())
                .map(route -> route.pushTerminus(this))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Route::isValid)
                .forEach(this.getRouteTable()::addRoute);

        // Update connected nodes on the newly created routes
        this.getRouteTable().propagateRoutes(Set.of(this));
    }
}

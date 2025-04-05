package com.verdantartifice.primalmagick.common.mana.network;

import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.Objects;
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
        // Search for mana suppliers which can provide to this node
        int range = this.getNetworkRange();
        int rangeSqr = range * range;
        BlockPos.betweenClosedStream(new AABB(this.getBlockPos()).inflate(range))
                .filter(pos -> pos.distSqr(this.getBlockPos()) <= rangeSqr)
                .map(pos -> level.getBlockEntity(pos) instanceof IManaSupplier supplier ? supplier : null)
                .filter(Objects::nonNull)
                .map(supplier -> new Route(supplier, this))
                .filter(Route::isValid)
                .forEach(this.getRouteTable()::addRoute);

        // Update connected nodes on the newly created routes
        Set<IManaNetworkNode> processed = new HashSet<>(Set.of(this));
        this.getRouteTable().propagateRoutes(processed);
    }
}

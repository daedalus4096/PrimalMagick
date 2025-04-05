package com.verdantartifice.primalmagick.common.mana.network;

import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Interface identifying a network node which can supply mana to other network nodes and, optionally, connect directly
 * to devices to extract mana into the network.
 *
 * @author Daedalus4096
 */
public interface IManaSupplier extends IManaNetworkNode {
    default boolean isOrigin() {
        return true;
    }

    boolean canSupply(Source source);

    default void onPlaced(Level level) {
        // Search for mana consumers to which this supplier can provide
        int range = this.getNetworkRange();
        int rangeSqr = range * range;
        BlockPos.betweenClosedStream(new AABB(this.getBlockPos()).inflate(range))
                .filter(pos -> pos.distSqr(this.getBlockPos()) <= rangeSqr)
                .map(pos -> level.getBlockEntity(pos) instanceof IManaConsumer consumer ? consumer : null)
                .filter(Objects::nonNull)
                .map(consumer -> new Route(this, consumer))
                .filter(Route::isValid)
                .forEach(this.getRouteTable()::addRoute);

        // Update connected nodes on the newly created routes
        Set<IManaNetworkNode> processed = new HashSet<>(Set.of(this));
        this.getRouteTable().propagateRoutes(processed);
    }
}

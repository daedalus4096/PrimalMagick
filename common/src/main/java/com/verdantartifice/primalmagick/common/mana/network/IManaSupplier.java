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
 * Interface identifying a network node which can supply mana to other network nodes and, optionally, connect directly
 * to devices to extract mana into the network.
 *
 * @author Daedalus4096
 */
public interface IManaSupplier extends IManaNetworkNode {
    default boolean isOrigin() {
        return true;
    }

    boolean canSupply(@NotNull Source source);
    int extractMana(@NotNull Source source, int maxExtract, boolean simulate);

    default void loadManaNetwork(@NotNull Level level) {
        int range = this.getNetworkRange();
        int rangeSqr = range * range;

        // Confirm that area is loaded before scanning
        if (Services.LEVEL.isAreaLoaded(level, this.getBlockPos(), this.getNetworkRange())) {
            // Search for mana consumers which are in range of this node
            List<IManaConsumer> consumers = BlockPos.betweenClosedStream(new AABB(this.getBlockPos()).inflate(range))
                    .filter(pos -> pos.distSqr(this.getBlockPos()) <= rangeSqr)
                    .map(pos -> level.getBlockEntity(pos) instanceof IManaConsumer consumer ? consumer : null)
                    .filter(Objects::nonNull)
                    .toList();

            // Create direct routes from this supplier for terminus consumers
            consumers.stream().filter(IManaConsumer::isTerminus)
                    .map(consumer -> new Route(this, consumer))
                    .filter(Route::isValid)
                    .forEach(this.getRouteTable()::addRoute);

            // For consumers that are actually relays, prepend this supplier to each of the routes that start in that consumer
            consumers.stream().map(consumer -> consumer instanceof IManaRelay relay ? relay : null)
                    .filter(Objects::nonNull)
                    .flatMap(relay -> relay.getRouteTable().getRoutesForOrigin(relay).stream())
                    .map(route -> route.pushOrigin(this))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(Route::isValid)
                    .forEach(this.getRouteTable()::addRoute);

            // Update connected nodes on the newly created routes
            this.getRouteTable().propagateRoutes(Set.of(this));
        }
    }
}

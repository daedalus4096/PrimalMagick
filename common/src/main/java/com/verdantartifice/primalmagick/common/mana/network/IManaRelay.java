package com.verdantartifice.primalmagick.common.mana.network;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

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
        if (!Services.CONFIG.enableManaNetworking()) {
            LogUtils.getLogger().warn("Mana networking not enabled; skipping default relay bootstrap");
            return;
        }

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
        level.getProfiler().popPush("createDirectSupplierEdges");
        nodes.stream().map(node -> node instanceof IManaSupplier supplier ? supplier : null)
                .filter(Objects::nonNull)
                .forEach(supplier -> this.getRouteTable().add(supplier, this));

        // Create direct routes to this relay for terminus consumers
        level.getProfiler().popPush("createDirectConsumerEdges");
        nodes.stream().map(node -> node instanceof IManaConsumer consumer ? consumer : null)
                .filter(Objects::nonNull)
                .forEach(consumer -> this.getRouteTable().add(this, consumer));
        level.getProfiler().pop();

        level.getProfiler().pop();
        level.getProfiler().pop();
    }
}

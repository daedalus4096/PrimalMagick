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
 * Interface identifying a network node which can supply mana to other network nodes and, optionally, connect directly
 * to devices to extract mana into the network.
 *
 * @author Daedalus4096
 */
public interface IManaSupplier extends IManaNetworkNode {
    /**
     * Gets whether this supplier is a valid origin for a network transmission packet.
     *
     * @return true if this supplier can provide mana to the network, or false if it's merely a relay
     */
    default boolean isOrigin() {
        return true;
    }

    /**
     * Gets whether this supplier can provide the given source of mana to the network.
     *
     * @param source the source of mana to be queried
     * @return true if this supplier can provide the given source of mana to the network, false otherwise
     */
    boolean canSupply(@NotNull Source source);

    /**
     * Extracts the given source and amount of mana from storage and supplies it to the network for transmission.
     *
     * @param source source of mana to be extracted
     * @param maxExtract maximum amount of centimana to be extracted
     * @param simulate if {@code true}, the extraction will only be simulated
     * @return amount of centimana that was (or would have been, if simulated) supplied from storage
     */
    int extractMana(@NotNull Source source, int maxExtract, boolean simulate);

    /**
     * Scans this supplier's surroundings for other mana network nodes and connects this supplier to the network.
     *
     * @param level the world in which this supplier resides
     */
    default void loadManaNetwork(@NotNull Level level) {
        if (!Services.CONFIG.enableManaNetworking()) {
            LogUtils.getLogger().warn("Mana networking not enabled; skipping default supplier bootstrap");
            return;
        }

        level.getProfiler().push("loadManaNetwork");
        level.getProfiler().push("defaultManaSupplier");

        int range = this.getNetworkRange();
        int rangeSqr = range * range;

        // Search for mana consumers which are in range of this node
        level.getProfiler().push("findNodes");
        List<IManaConsumer> consumers = BlockPos.betweenClosedStream(new AABB(this.getBlockPos()).inflate(range))
                .filter(pos -> pos.distSqr(this.getBlockPos()) <= rangeSqr)
                .map(pos -> level.getBlockEntity(pos) instanceof IManaConsumer consumer ? consumer : null)
                .filter(Objects::nonNull)
                .toList();

        // Create direct routes from this supplier for terminus consumers
        level.getProfiler().popPush("createDirectConsumerEdges");
        consumers.forEach(consumer -> this.getRouteTable().add(this, consumer));
        level.getProfiler().pop();

        level.getProfiler().pop();
        level.getProfiler().pop();
    }
}

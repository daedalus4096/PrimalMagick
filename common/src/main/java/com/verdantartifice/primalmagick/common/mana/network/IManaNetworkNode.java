package com.verdantartifice.primalmagick.common.mana.network;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

/**
 * Base interface of a node in a wireless mana distribution network.
 *
 * @author Daedalus4096
 */
public interface IManaNetworkNode {
    default long getNodeId() {
        return this.getBlockPos().asLong();
    }

    BlockPos getBlockPos();
    int getNetworkRange();
    int getManaThroughput();
    @NotNull RouteTable getRouteTable();
}

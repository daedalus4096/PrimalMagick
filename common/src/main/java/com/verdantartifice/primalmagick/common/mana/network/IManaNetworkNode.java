package com.verdantartifice.primalmagick.common.mana.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Base interface of a node in a wireless mana distribution network.
 *
 * @author Daedalus4096
 */
public interface IManaNetworkNode {
    /**
     * Gets the network ID of this node, guaranteed to be unique for the level in which it resides.
     *
     * @return this node's network ID
     */
    default long getNodeId() {
        return this.getBlockPos().asLong();
    }

    /**
     * Gets the position of this node in the world.
     *
     * @return the position of this node
     */
    BlockPos getBlockPos();

    /**
     * Gets the range, in blocks, over which this node can connect to other nodes.
     *
     * @return this node's network range
     */
    int getNetworkRange();

    /**
     * Gets the amount of centimana that this node can supply or consume in a given operation.
     *
     * @return this node's throughput in centimana
     */
    int getManaThroughput();

    /**
     * Gets this node's route table, containing all the network connections of which it's aware.
     *
     * @return this node's route table
     */
    @NotNull RouteTable getRouteTable();

    void loadManaNetwork(@NotNull Level level);
}

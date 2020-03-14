package com.verdantartifice.primalmagic.common.rituals;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

/**
 * Interface indicating whether a block can send or receive salt power for rituals.
 * 
 * @author Daedalus4096
 */
public interface ISaltPowered {
    /**
     * Determine if this block can make a salt connection on the side provided.
     * Useful to control which sides are inputs and outputs for salt trails.
     *
     * @param state The current state
     * @param world The current world
     * @param pos Block position in world
     * @param side The side that is trying to make the connection, CAN BE NULL
     * @return True to make the connection
     */
    public default boolean canConnectSalt(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nullable Direction side) {
        return this.canProvideSaltPower(state) && side != null;
    }
    
    public default boolean canProvideSaltPower(@Nonnull BlockState state) {
        return false;
    }
    
    /**
     * Called to determine whether to allow the a block to handle its own indirect salt power rather than using the default rules.
     * 
     * @param world The world
     * @param pos Block position in world
     * @param side The INPUT side of the block to be powered - ie the opposite of this block's output side
     * @return Whether Block#isProvidingWeakPower should be called when determining indirect power
     */
    public default boolean shouldCheckWeakSaltPower(@Nonnull BlockState state, @Nonnull IWorldReader world, @Nonnull BlockPos pos, @Nonnull Direction side) {
        return state.isNormalCube(world, pos);
    }
    
    public default int getWeakSaltPower(@Nonnull BlockState blockState, @Nonnull IBlockReader blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
        return 0;
    }
    
    public default int getStrongSaltPower(@Nonnull BlockState blockState, @Nonnull IBlockReader blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
        return 0;
    }
}

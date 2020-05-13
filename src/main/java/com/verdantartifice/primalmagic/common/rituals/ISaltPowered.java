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
    
    public default boolean isBlockSaltPowered(@Nonnull IWorldReader world, @Nonnull BlockPos pos) {
        for (Direction dir : Direction.values()) {
            if (this.getSaltPower(world, pos.offset(dir), dir) > 0) {
                return true;
            }
        }
        return false;
    }
    
    public default int getSaltPower(@Nonnull IWorldReader world, @Nonnull BlockPos pos, @Nonnull Direction facing) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof ISaltPowered) {
            ISaltPowered saltBlock = ((ISaltPowered)state.getBlock());
            if (saltBlock.shouldCheckWeakSaltPower(state, world, pos, facing)) {
                int power = 0;
                for (Direction dir : Direction.values()) {
                    BlockPos offsetPos = pos.offset(dir);
                    BlockState offsetState = world.getBlockState(offsetPos);
                    if (offsetState.getBlock() instanceof ISaltPowered) {
                        power = Math.max(power, ((ISaltPowered)offsetState.getBlock()).getStrongSaltPower(offsetState, world, offsetPos, dir));
                        if (power >= 15) {
                            return power;
                        }
                    }
                }
                return power;
            } else {
                return saltBlock.getWeakSaltPower(state, world, pos, facing);
            }
        } else {
            return 0;
        }
    }
}

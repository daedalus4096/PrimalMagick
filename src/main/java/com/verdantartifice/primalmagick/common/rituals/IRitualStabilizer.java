package com.verdantartifice.primalmagick.common.rituals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Interface indicating whether a block can affect the stability of a magickal ritual.
 * 
 * @author Daedalus4096
 */
public interface IRitualStabilizer {
    /**
     * Determine whether this block is currently exacting a symmetry penalty on ritual stability.
     * 
     * @param world the world containing this block
     * @param pos the position of this block
     * @param otherPos the position of the mirroring block
     * @return true if this block is exacting a symmetry penalty on the ritual, false otherwise
     */
    public default boolean hasSymmetryPenalty(Level world, BlockPos pos, BlockPos otherPos) {
        return (world.getBlockState(pos).getBlock() != world.getBlockState(otherPos).getBlock());
    }
    
    /**
     * Get the absolute value of the bonus to ritual stability granted by this block, if not currently
     * exacting a symmetry penalty.
     * 
     * @param world the world containing this block
     * @param pos the position of this block
     * @return the absolute value of the bonus to ritual stability
     */
    public float getStabilityBonus(Level world, BlockPos pos);
    
    /**
     * Get the absolute value of the penalty to ritual stability inflicted by this block, if currently
     * exacting a symmetry penalty.
     * 
     * @param world the world containing this block
     * @param pos the position of this block
     * @return the absolute value of the penalty to ritual stability
     */
    public float getSymmetryPenalty(Level world, BlockPos pos);
}

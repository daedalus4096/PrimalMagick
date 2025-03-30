package com.verdantartifice.primalmagick.common.rituals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Interface indicating whether a block can affect the stability of a magickal ritual.
 * 
 * @author Daedalus4096
 */
public interface IRitualStabilizer extends ISaltPowered {
    /**
     * Determine whether this block is currently exacting a symmetry penalty on ritual stability.
     * 
     * @param world the world containing this block
     * @param pos the position of this block
     * @param otherPos the position of the mirroring block
     * @return true if this block is exacting a symmetry penalty on the ritual, false otherwise
     */
    default boolean hasSymmetryPenalty(Level world, BlockPos pos, BlockPos otherPos) {
        if (!(world.getBlockState(pos).getBlock() instanceof IRitualStabilizer block1)) {
            // If the main block isn't a stabilizer, then it shouldn't interfere
            return false;
        } else if (world.getBlockState(otherPos).getBlock() instanceof IRitualStabilizer block2) {
            // If both blocks are stabilizers, then they have to be the same and both powered, or else there's a penalty
            return block1 != block2 || !block1.isBlockSaltPowered(world, pos) || !block2.isBlockSaltPowered(world, otherPos);
        } else {
            // If the main block is a stabilizer and the other block isn't, then it's an automatic penalty
            return true;
        }
    }
    
    /**
     * Get the absolute value of the bonus to ritual stability granted by this block, if not currently
     * exacting a symmetry penalty.
     * 
     * @param world the world containing this block
     * @param pos the position of this block
     * @return the absolute value of the bonus to ritual stability
     */
    float getStabilityBonus(Level world, BlockPos pos);
    
    /**
     * Get the absolute value of the penalty to ritual stability inflicted by this block, if currently
     * exacting a symmetry penalty.
     * 
     * @param world the world containing this block
     * @param pos the position of this block
     * @return the absolute value of the penalty to ritual stability
     */
    float getSymmetryPenalty(Level world, BlockPos pos);
}

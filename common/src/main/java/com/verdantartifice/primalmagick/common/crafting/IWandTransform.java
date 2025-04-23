package com.verdantartifice.primalmagick.common.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Base interface for a wand transformation that turns one thing into another.
 * 
 * @author Daedalus4096
 */
public interface IWandTransform {
    /**
     * Determine whether this transform may be executed.
     * 
     * @param world the world in which the transform is taking place
     * @param player the player attempting to perform the transformation
     * @param pos the position of the thing to be transformed
     * @return true if the player may proceed with the transform, false otherwise
     */
    boolean isValid(Level world, Player player, BlockPos pos);

    /**
     * Determine whether this transformation is being on run something similar, but not exactly the same as, the
     * intended target and should show a warning to the player.
     *
     * @param world the world in which the transform is taking place
     * @param player the player attempting to perform the transformation
     * @param pos the position of the thing to be transformed
     * @return true if the player should be shown a similarity warning for the transform, false otherwise
     */
    boolean isSimilar(Level world, Player player, BlockPos pos);

    /**
     * Show the pre-defined similarity warning to the given player.
     *
     * @param world the world in which the transform is taking place
     * @param player the player attempting to perform the transformation
     * @param pos the position of the thing to be transformed
     */
    void showSimilarityWarning(Level world, Player player, BlockPos pos);
    
    /**
     * Execute the defined transformation.
     * 
     * @param world the world in which the transform is taking place
     * @param player the player attempting to perform the transformation
     * @param pos the position of the thing to be transformed
     */
    void execute(Level world, Player player, BlockPos pos);
}

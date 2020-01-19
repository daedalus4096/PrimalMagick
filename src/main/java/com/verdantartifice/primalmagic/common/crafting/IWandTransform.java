package com.verdantartifice.primalmagic.common.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public boolean isValid(World world, PlayerEntity player, BlockPos pos);
    
    /**
     * Execute the defined transformation.
     * 
     * @param world the world in which the transform is taking place
     * @param player the player attempting to perform the transformation
     * @param pos the position of the thing to be transformed
     */
    public void execute(World world, PlayerEntity player, BlockPos pos);
}

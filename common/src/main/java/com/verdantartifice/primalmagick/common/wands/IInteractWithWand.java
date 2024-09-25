package com.verdantartifice.primalmagick.common.wands;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Control interface for a block or tile entity that can interact with wands when right-clicked wtih them.
 * 
 * @author Daedalus4096
 */
public interface IInteractWithWand {
    /**
     * Handle the initial right-click of a wand on this block/tile.
     * 
     * @param wandStack the wand stack used
     * @param level the level containing the block/tile
     * @param player the player who used the wand
     * @param pos the world position of the block/tile
     * @param direction the side of the block/tile that was right-clicked
     * @return whether the interaction succeeded, failed, or should defer to subsequent code
     */
    public InteractionResult onWandRightClick(ItemStack wandStack, Level level, Player player, BlockPos pos, Direction direction);
    
    /**
     * Handle a tick's worth of behavior when the initial interaction is continued by holding the right-click.
     * 
     * @param wandStack the wand stack used
     * @param level the level containing the block/tile
     * @param player the player who used the wand, if any
     * @param targetPos the position of the player/block that used the wand
     * @param count the number of ticks which the wand has been used for continuously
     */
    public void onWandUseTick(ItemStack wandStack, Level level, @Nullable Player player, Vec3 targetPos, int count);
}

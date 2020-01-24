package com.verdantartifice.primalmagic.common.wands;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
     * @param world the world containing the block/tile
     * @param player the player who used the wand
     * @param pos the world position of the block/tile
     * @param direction the side of the block/tile that was right-clicked
     * @return whether the interaction succeeded, failed, or should defer to subsequent code
     */
    public ActionResultType onWandRightClick(ItemStack wandStack, World world, PlayerEntity player, BlockPos pos, Direction direction);
    
    /**
     * Handle a tick's worth of behavior when the initial interaction is continued by holding the right-click.
     * 
     * @param wandStack the wand stack used
     * @param player the player who used the wand
     * @param count the number of ticks which the wand has been used for continuously
     */
    public void onWandUseTick(ItemStack wandStack, PlayerEntity player, int count);
}

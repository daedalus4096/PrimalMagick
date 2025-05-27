package com.verdantartifice.primalmagick.common.wands;

import com.verdantartifice.primalmagick.common.crafting.IWandTransform;
import com.verdantartifice.primalmagick.common.crafting.WandTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base interface for a wand.  Wands store mana for use in crafting and, optionally, casting spells.
 * They are replenished by drawing from mana fonts or being charged in a wand charger.  The wand's mana
 * is stored internally as centimana (hundredths of mana points).
 * 
 * @author Daedalus4096
 */
public interface IWand extends ISpellContainer, IManaContainer {
    /**
     * Get the amount of centimana to siphon from a mana font when channeling it.
     * 
     * @param stack the wand stack to be queried
     * @return the amount of centimana to siphon from mana fonts
     */
    int getSiphonAmount(@Nullable ItemStack stack);

    /**
     * Clear any stored position data for the last interacted-with tile.
     * 
     * @param wandStack the wand stack to be modified
     */
    void clearPositionInUse(@NotNull ItemStack wandStack);

    /**
     * Store the given position data into the given wand stack.
     * 
     * @param wandStack the wand stack to be modified
     * @param pos the position data is to be stored
     */
    void setPositionInUse(@NotNull ItemStack wandStack, @NotNull BlockPos pos);

    /**
     * Get the position currently being interacted with by the given wand stack.
     * 
     * @param wandStack the wand stack to be queried
     * @return the position currently being interacted with, or null if none was found
     */
    @Nullable
    BlockPos getPositionInUse(@NotNull ItemStack wandStack);

    /**
     * Determine if the given wand stack has a glamour applied (i.e. if its appearance differs from that 
     * of its construction).
     * 
     * @param stack the wand stack to be queried
     * @return true if the wand has a glamour applied, false otherwise
     */
    boolean isGlamoured(@Nullable ItemStack stack);

    default InteractionResult onWandUseFirst(ItemStack stack, UseOnContext context) {
        // Only process on server side
        Level world = context.getLevel();
        if (world.isClientSide) {
            return InteractionResult.PASS;
        }

        // Bypass wand functionality if the player is sneaking
        if (context.getPlayer().isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        context.getPlayer().startUsingItem(context.getHand());

        // If the mouseover target is a wand-sensitive block, trigger that initial interaction
        BlockState bs = context.getLevel().getBlockState(context.getClickedPos());
        if (bs.getBlock() instanceof IInteractWithWand wandable) {
            return wandable.onWandRightClick(context.getItemInHand(), context.getLevel(), context.getPlayer(), context.getClickedPos(), context.getClickedFace());
        }

        // If the mouseover target is a wand-sensitive tile entity, trigger that initial interaction
        BlockEntity tile = context.getLevel().getBlockEntity(context.getClickedPos());
        if (tile != null && tile instanceof IInteractWithWand wandable) {
            return wandable.onWandRightClick(context.getItemInHand(), context.getLevel(), context.getPlayer(), context.getClickedPos(), context.getClickedFace());
        }

        // Otherwise, see if the mouseover target is a valid target for wand transformation
        for (IWandTransform transform : WandTransforms.getAll()) {
            if (transform.isValid(context.getLevel(), context.getPlayer(), context.getClickedPos())) {
                if (!context.getPlayer().mayUseItemAt(context.getClickedPos(), context.getClickedFace(), context.getItemInHand())) {
                    return InteractionResult.FAIL;
                } else {
                    // If so, save its position for future channeling
                    this.setPositionInUse(stack, context.getClickedPos());
                    return InteractionResult.SUCCESS;
                }
            } else if (transform.isSimilar(context.getLevel(), context.getPlayer(), context.getClickedPos())) {
                // If the target is not valid for the transform, but is similar to one, show the player a warning message
                transform.showSimilarityWarning(context.getLevel(), context.getPlayer(), context.getClickedPos());
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;
    }
}

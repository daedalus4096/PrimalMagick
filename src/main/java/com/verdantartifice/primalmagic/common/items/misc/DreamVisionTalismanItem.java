package com.verdantartifice.primalmagic.common.items.misc;

import java.util.List;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Item definition for a dream vision talisman.  These items store experience picked up by the player,
 * converting them to observations when the player sleeps.
 * 
 * @author Daedalus4096
 */
public class DreamVisionTalismanItem extends Item {
    protected static final int CAPACITY = 64;
    
    public DreamVisionTalismanItem() {
        super(new Item.Properties().tab(PrimalMagic.ITEM_GROUP).durability(63));
    }
    
    /**
     * Gets the amount of stored experience in the given talisman item stack.
     * 
     * @param stack the talisman to query
     * @return the amount of experience stored in the given talisman
     */
    public int getStoredExp(ItemStack stack) {
        return stack.getOrCreateTag().getInt("StoredExp");
    }
    
    /**
     * Adds the given amount of experience to the given talisman, up to its capacity.  Returns
     * any leftover experience that couldn't fit.
     * 
     * @param stack the talisman to update
     * @param toAdd the amount of experience to add
     * @return the amount of experience that couldn't fit in the talisman
     */
    public int addStoredExp(ItemStack stack, int toAdd) {
        int current = this.getStoredExp(stack);
        int max = this.getExpCapacity(stack);
        if (current + toAdd <= max) {
            stack.getOrCreateTag().putInt("StoredExp", current + toAdd);
            return 0;
        } else {
            int leftover = (current + toAdd) - max;
            stack.getOrCreateTag().putInt("StoredExp", max);
            return leftover;
        }
    }
    
    /**
     * Gets the maximum amount of experience that can be stored in the given talisman.
     * 
     * @param stack the talisman to query
     * @return the maximum amount of experience that can be stored in the given talisman
     */
    public int getExpCapacity(ItemStack stack) {
        return CAPACITY;
    }
    
    /**
     * Determines whether the talisman is currently attempting to store experience.
     * 
     * @param stack the talisman to query
     * @return whether the given talisman is currently active
     */
    public boolean isActive(ItemStack stack) {
        return !stack.getOrCreateTag().getBoolean("Disabled");
    }
    
    /**
     * Sets whether the talisman is currently attempting to store experience.
     * 
     * @param stack the talisman to update
     * @param active whether the talisman should capture experience
     */
    public void setActive(ItemStack stack, boolean active) {
        stack.getOrCreateTag().putBoolean("Disabled", !active);
    }

    /**
     * Determines whether the given talisman has stored all the experience that it can and is
     * ready to be drained by sleeping.
     * 
     * @param stack the talisman to query
     * @return whether the talisman is ready to be drained of experience by sleeping
     */
    public boolean isReadyToDrain(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof DreamVisionTalismanItem talisman) {
            if (talisman.getStoredExp(stack) >= talisman.getExpCapacity(stack)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("tooltip.primalmagic.dream_vision_talisman.exp", this.getStoredExp(stack), this.getExpCapacity(stack)));
        if (this.isActive(stack)) {
            tooltip.add(new TranslatableComponent("tooltip.primalmagic.dream_vision_talisman.active").withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(new TranslatableComponent("tooltip.primalmagic.dream_vision_talisman.inactive").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.isReadyToDrain(stack) || super.isFoil(stack);
    }
}

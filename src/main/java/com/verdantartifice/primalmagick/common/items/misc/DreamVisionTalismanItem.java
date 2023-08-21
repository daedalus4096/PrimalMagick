package com.verdantartifice.primalmagick.common.items.misc;

import java.util.List;

import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchManager;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
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
        super(new Item.Properties().durability(64));
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
            this.setStoredExp(stack, current + toAdd);
            return 0;
        } else {
            int leftover = (current + toAdd) - max;
            this.setStoredExp(stack, max);
            return leftover;
        }
    }
    
    protected void setStoredExp(ItemStack stack, int toSet) {
        stack.getOrCreateTag().putInt("StoredExp", toSet);
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
    
    /**
     * Drains the talisman of its experience to grant the player an observation.  Doing so decreases
     * the talisman's durability.
     * 
     * @param stack the talisman to drain
     * @param player the player to receive any observations
     * @return whether the talisman was successfully drained
     */
    public boolean doDrain(ItemStack stack, Player player) {
        Level level = player.level();
        if (!level.isClientSide && this.isReadyToDrain(stack)) {
            if (ResearchManager.addKnowledge(player, KnowledgeType.OBSERVATION, KnowledgeType.OBSERVATION.getProgression())) {
                this.setStoredExp(stack, 0);
                stack.hurtAndBreak(1, player, p -> {
                    p.displayClientMessage(Component.translatable("event.primalmagick.dream_vision_talisman.break").withStyle(ChatFormatting.RED), false);
                });
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(Component.translatable("tooltip.primalmagick.dream_vision_talisman.exp", this.getStoredExp(stack), this.getExpCapacity(stack)));
        if (this.isActive(stack)) {
            tooltip.add(Component.translatable("tooltip.primalmagick.active").withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.translatable("tooltip.primalmagick.inactive").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.isReadyToDrain(stack) || super.isFoil(stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean active = this.isActive(stack);
        if (level.isClientSide) {
            if (active) {
                player.displayClientMessage(Component.translatable("event.primalmagick.dream_vision_talisman.set_inactive"), false);
            } else {
                player.displayClientMessage(Component.translatable("event.primalmagick.dream_vision_talisman.set_active"), false);
            }
        }
        this.setActive(stack, !active);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}

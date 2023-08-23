package com.verdantartifice.primalmagick.common.items.misc;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Item definition for a hallowed orb.  A hallowed orb unlocks the Hallowed source when scanned on an
 * analysis table or with an arcanometer.
 * 
 * @author Daedalus4096
 */
public class HallowedOrbItem extends Item {
    public HallowedOrbItem() {
        super(new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable("item.primalmagick.hallowed_orb.tooltip.1").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
        tooltip.add(Component.translatable("item.primalmagick.hallowed_orb.tooltip.2").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
    }
}

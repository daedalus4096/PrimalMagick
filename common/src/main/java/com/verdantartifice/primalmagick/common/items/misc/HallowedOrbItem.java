package com.verdantartifice.primalmagick.common.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.List;
import java.util.function.Consumer;

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
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flagIn) {
        tooltip.accept(Component.translatable("item.primalmagick.hallowed_orb.tooltip.1").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
        tooltip.accept(Component.translatable("item.primalmagick.hallowed_orb.tooltip.2").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
    }
}

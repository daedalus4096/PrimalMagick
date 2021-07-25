package com.verdantartifice.primalmagic.common.items.misc;

import java.util.List;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Item definition for a hallowed orb.  A hallowed orb unlocks the Hallowed source when scanned on an
 * analysis table or with an arcanometer.
 * 
 * @author Daedalus4096
 */
public class HallowedOrbItem extends Item {
    public HallowedOrbItem() {
        super(new Item.Properties().tab(PrimalMagic.ITEM_GROUP).rarity(Rarity.RARE));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("tooltip.primalmagic.hallowed_orb.1").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
        tooltip.add(new TranslatableComponent("tooltip.primalmagic.hallowed_orb.2").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
    }
}

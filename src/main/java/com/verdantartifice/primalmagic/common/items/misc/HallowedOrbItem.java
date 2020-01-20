package com.verdantartifice.primalmagic.common.items.misc;

import java.util.List;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
        super(new Item.Properties().group(PrimalMagic.ITEM_GROUP).rarity(Rarity.RARE));
        this.setRegistryName(PrimalMagic.MODID, "hallowed_orb");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.primalmagic.hallowed_orb.1").applyTextStyles(TextFormatting.WHITE, TextFormatting.ITALIC));
        tooltip.add(new TranslationTextComponent("tooltip.primalmagic.hallowed_orb.2").applyTextStyles(TextFormatting.WHITE, TextFormatting.ITALIC));
    }
}

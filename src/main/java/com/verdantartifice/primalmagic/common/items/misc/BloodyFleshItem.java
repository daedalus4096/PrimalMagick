package com.verdantartifice.primalmagic.common.items.misc;

import java.util.List;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
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
 * Item definition for bloody flesh.  Bloody flesh drops from human-like mobs and unlocks the Blood source when eaten.
 * 
 * @author Daedalus4096
 */
public class BloodyFleshItem extends Item {
    public BloodyFleshItem() {
        super(new Item.Properties().group(PrimalMagic.ITEM_GROUP).rarity(Rarity.UNCOMMON).food(new Food.Builder().hunger(3).saturation(0.3F).meat().setAlwaysEdible().build()));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote && (entityLiving instanceof PlayerEntity)) {
            PlayerEntity player = (PlayerEntity)entityLiving;
            IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
            if (knowledge != null && knowledge.isResearchKnown(SimpleResearchKey.parse("FIRST_STEPS")) && !knowledge.isResearchKnown(Source.BLOOD.getDiscoverKey())) {
                // Only unlock the Blood source if the player has started mod progression and hasn't already unlocked it
                ResearchManager.completeResearch(player, Source.BLOOD.getDiscoverKey());
                ResearchManager.completeResearch(player, SimpleResearchKey.parse("t_discover_forbidden"));
                player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.discover_source.blood").mergeStyle(TextFormatting.GREEN), false);
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.primalmagic.bloody_flesh.1").mergeStyle(TextFormatting.DARK_RED, TextFormatting.ITALIC));
        tooltip.add(new TranslationTextComponent("tooltip.primalmagic.bloody_flesh.2").mergeStyle(TextFormatting.DARK_RED, TextFormatting.ITALIC));
    }
}

package com.verdantartifice.primalmagic.common.items.food;

import java.util.List;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Item definition for bloody flesh.  Bloody flesh drops from human-like mobs and unlocks the Blood source when eaten.
 * 
 * @author Daedalus4096
 */
public class BloodyFleshItem extends Item {
    public BloodyFleshItem() {
        super(new Item.Properties().tab(PrimalMagic.ITEM_GROUP).rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().nutrition(3).saturationMod(0.3F).meat().alwaysEat().build()));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClientSide && (entityLiving instanceof Player)) {
            Player player = (Player)entityLiving;
            IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
            if (knowledge != null && knowledge.isResearchKnown(SimpleResearchKey.FIRST_STEPS) && !knowledge.isResearchKnown(Source.BLOOD.getDiscoverKey())) {
                // Only unlock the Blood source if the player has started mod progression and hasn't already unlocked it
                ResearchManager.completeResearch(player, Source.BLOOD.getDiscoverKey());
                ResearchManager.completeResearch(player, SimpleResearchKey.parse("t_discover_forbidden"));
                player.displayClientMessage(new TranslatableComponent("event.primalmagic.discover_source.blood").withStyle(ChatFormatting.GREEN), false);
            }
        }
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("tooltip.primalmagic.bloody_flesh.1").withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));
        tooltip.add(new TranslatableComponent("tooltip.primalmagic.bloody_flesh.2").withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));
    }
}

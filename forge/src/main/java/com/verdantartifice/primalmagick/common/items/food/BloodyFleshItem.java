package com.verdantartifice.primalmagick.common.items.food;

import java.util.List;

import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.sources.Sources;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
        super(new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.BLOODY_FLESH));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClientSide && entityLiving instanceof Player player) {
            if (ResearchManager.isResearchStarted(player, ResearchEntries.FIRST_STEPS) && !ResearchManager.isResearchStarted(player, ResearchEntries.DISCOVER_BLOOD)) {
                // Only unlock the Blood source if the player has started mod progression and hasn't already unlocked it
                ResearchManager.completeResearch(player, ResearchEntries.DISCOVER_BLOOD);
                player.displayClientMessage(Component.translatable("event.primalmagick.discover_source.blood").withStyle(ChatFormatting.GREEN), false);
            }
        }
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.primalmagick.bloody_flesh.tooltip.1").withStyle(Sources.BLOOD.getChatColor(), ChatFormatting.ITALIC));
        tooltip.add(Component.translatable("item.primalmagick.bloody_flesh.tooltip.2").withStyle(Sources.BLOOD.getChatColor(), ChatFormatting.ITALIC));
    }
}

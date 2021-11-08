package com.verdantartifice.primalmagick.common.items.misc;

import java.util.List;

import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Definition of an item that unlocks a primal source when used.
 * 
 * @author Daedalus4096
 */
public class ForbiddenSourceGainItem extends Item {
    protected final Source source;
    
    public ForbiddenSourceGainItem(Source source, Item.Properties properties) {
        super(properties);
        this.source = source;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            if (SimpleResearchKey.FIRST_STEPS.isKnownByStrict(player)) {
                if (!this.source.getDiscoverKey().isKnownByStrict(player)) {
                    ResearchManager.completeResearch(player, this.source.getDiscoverKey());
                    ResearchManager.completeResearch(player, SimpleResearchKey.parse("t_discover_forbidden"));
                    player.displayClientMessage(new TranslatableComponent("event.primalmagick.discover_source." + this.source.getTag() + ".alternate").withStyle(ChatFormatting.GREEN), false);
                    if (!player.getAbilities().instabuild) {
                        player.getItemInHand(hand).shrink(1);
                    }
                } else {
                    player.displayClientMessage(new TranslatableComponent("event.primalmagick.knowledge_item.already_known").withStyle(ChatFormatting.RED), true);
                }
            } else {
                // Players who haven't started mod progression get no benefit
                player.displayClientMessage(new TranslatableComponent("event.primalmagick.knowledge_item.failure").withStyle(ChatFormatting.RED), true);
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(new TranslatableComponent("tooltip.primalmagick.forbidden_source_item").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}

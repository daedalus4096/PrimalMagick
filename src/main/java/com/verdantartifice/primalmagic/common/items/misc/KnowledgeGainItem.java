package com.verdantartifice.primalmagic.common.items.misc;

import java.util.List;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge.KnowledgeType;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.PlayClientSoundPacket;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Definition of an item that grants observation or theory progress when used by a player.
 * 
 * @author Daedalus4096
 */
public class KnowledgeGainItem extends Item {
    protected final KnowledgeType knowledgeType;
    protected final int knowledgePoints;
    
    public KnowledgeGainItem(KnowledgeType type, int points, Item.Properties properties) {
        super(properties);
        this.knowledgeType = type;
        this.knowledgePoints = points;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            if (SimpleResearchKey.FIRST_STEPS.isKnownByStrict(player)) {
                ResearchManager.addKnowledge(player, this.knowledgeType, this.knowledgePoints);
                player.displayClientMessage(new TranslatableComponent("event.primalmagic.knowledge_item.success").withStyle(ChatFormatting.GREEN), true);
                if (player instanceof ServerPlayer serverPlayer) {
                    PacketHandler.sendToPlayer(new PlayClientSoundPacket(SoundsPM.WRITING.get(), 1.0F, 1.0F + (float)player.getRandom().nextGaussian() * 0.05F), serverPlayer);
                }
                if (!player.getAbilities().instabuild) {
                    player.getItemInHand(hand).shrink(1);
                }
            } else {
                // Players who haven't started mod progression get no benefit
                player.displayClientMessage(new TranslatableComponent("event.primalmagic.knowledge_item.failure").withStyle(ChatFormatting.RED), true);
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        String translationKey = "tooltip." + PrimalMagic.MODID + "." + this.getRegistryName().getPath();
        tooltip.add(new TranslatableComponent(translationKey).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}

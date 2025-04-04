package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.PlayClientSoundPacket;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

/**
 * Definition of an item that unlocks arcane research when used.
 * 
 * @author Daedalus4096
 */
public class ResearchGainItem extends Item {
    protected final Supplier<ResearchEntryKey> keySupplier;
    
    public ResearchGainItem(Supplier<ResearchEntryKey> keySupplier, Item.Properties properties) {
        super(properties);
        this.keySupplier = keySupplier;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            if (ResearchManager.isResearchComplete(player, ResearchEntries.FIRST_STEPS)) {
                if (!this.keySupplier.get().isKnownBy(player)) {
                    ResearchManager.completeResearch(player, this.keySupplier.get());
                    player.displayClientMessage(Component.translatable("event.primalmagick.research.gain").withStyle(ChatFormatting.GREEN), false);
                    if (player instanceof ServerPlayer serverPlayer) {
                        PacketHandler.sendToPlayer(new PlayClientSoundPacket(SoundsPM.WRITING.get(), 1.0F, 1.0F + (float)player.getRandom().nextGaussian() * 0.05F), serverPlayer);
                    }
                    if (!player.getAbilities().instabuild) {
                        player.getItemInHand(hand).shrink(1);
                    }
                } else {
                    player.displayClientMessage(Component.translatable("event.primalmagick.knowledge_item.already_known").withStyle(ChatFormatting.RED), true);
                }
            } else {
                // Players who haven't started mod progression get no benefit
                player.displayClientMessage(Component.translatable("event.primalmagick.knowledge_item.failure").withStyle(ChatFormatting.RED), true);
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.primalmagick.research_item").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}

package com.verdantartifice.primalmagick.common.items.misc;

import java.util.List;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.PlayClientSoundPacket;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;

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
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Definition of an item that unlocks a forbidden primal source when used.
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
                    // FIXME Refactor this to either be blood-specific or fully generic, stop splitting the difference
                    ResearchManager.completeResearch(player, this.source.getDiscoverKey());
                    player.displayClientMessage(Component.translatable("event.primalmagick.discover_source." + this.source.getTag() + ".alternate").withStyle(ChatFormatting.GREEN), false);
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
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        String translationKey = "item." + PrimalMagick.MODID + "." + ForgeRegistries.ITEMS.getKey(this).getPath() + ".tooltip";
        tooltip.add(Component.translatable(translationKey).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}

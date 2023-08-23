package com.verdantartifice.primalmagick.common.items.misc;

import java.util.List;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.PlayClientSoundPacket;
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
 * Definition of an item that grants attunement points when used by a player.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractAttunementGainItem extends Item {
    protected final Source source;
    protected final AttunementType attunementType;
    protected final int amount;
    
    public AbstractAttunementGainItem(Source source, AttunementType type, int amount, Item.Properties properties) {
        super(properties);
        this.source = source;
        this.attunementType = type;
        this.amount = amount;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            if (SimpleResearchKey.FIRST_STEPS.isKnownByStrict(player)) {
                AttunementManager.incrementAttunement(player, this.source, this.attunementType, this.amount);
                player.displayClientMessage(Component.translatable("event.primalmagick.attunement_item.success").withStyle(ChatFormatting.GREEN), true);
                if (player instanceof ServerPlayer serverPlayer) {
                    PacketHandler.sendToPlayer(new PlayClientSoundPacket(SoundsPM.SHIMMER.get(), 1.0F, 1.0F + (float)player.getRandom().nextGaussian() * 0.05F), serverPlayer);
                }
                if (!player.getAbilities().instabuild) {
                    player.getItemInHand(hand).shrink(1);
                }
            } else {
                // Players who haven't started mod progression get no benefit
                player.displayClientMessage(Component.translatable("event.primalmagick.attunement_item.failure").withStyle(ChatFormatting.RED), true);
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        String translationKey = "item." + PrimalMagick.MODID + "." + ForgeRegistries.ITEMS.getKey(this).getPath() + ".tooltip";
        tooltip.add(Component.translatable(translationKey).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
    
    public int getColor(int tintIndex) {
        return tintIndex == 0 ? 0xFFFFFF : this.source.getColor();
    }
}

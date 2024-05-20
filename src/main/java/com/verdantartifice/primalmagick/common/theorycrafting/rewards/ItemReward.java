package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Theorycrafting reward that grants a specific item stack.
 * 
 * @author Daedalus4096
 */
public class ItemReward extends AbstractReward<ItemReward> {
    public static final Codec<ItemReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("stack").forGetter(r -> r.stack)
        ).apply(instance, ItemReward::new));
    
    private final ItemStack stack;
    
    public ItemReward(@Nonnull ItemStack stack) {
        this.stack = stack.copy();
    }

    @Override
    protected RewardType<ItemReward> getType() {
        return RewardTypesPM.ITEM.get();
    }

    @Override
    public void grant(ServerPlayer player) {
        if (!player.addItem(this.stack)) {
            ItemEntity entity = player.drop(this.stack, false);
            if (entity != null) {
                entity.setNoPickUpDelay();
                entity.setTarget(player.getUUID());
            }
        } else {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }
    }

    @Override
    public Component getDescription() {
        MutableComponent itemName = Component.empty().append(this.stack.getHoverName()).withStyle(this.stack.getRarity().getStyleModifier());
        if (this.stack.hasCustomHoverName()) {
            itemName.withStyle(ChatFormatting.ITALIC);
        }
        return Component.translatable("label.primalmagick.research_table.reward", this.stack.getCount(), itemName);
    }

    @Nonnull
    public static ItemReward fromNetworkInner(FriendlyByteBuf buf) {
        return new ItemReward(buf.readItem());
    }

    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeItemStack(this.stack, false);
    }
}

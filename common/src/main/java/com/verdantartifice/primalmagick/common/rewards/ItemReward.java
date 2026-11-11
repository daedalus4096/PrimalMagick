package com.verdantartifice.primalmagick.common.rewards;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Reward that grants a specific item stack.
 * 
 * @author Daedalus4096
 */
public class ItemReward extends AbstractReward<ItemReward> {
    public static final MapCodec<ItemReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStackTemplate.CODEC.fieldOf("stack").forGetter(r -> r.stack)
        ).apply(instance, ItemReward::new));
    
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemReward> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC, reward -> reward.stack,
            ItemReward::new);
    
    private final ItemStackTemplate stack;
    
    public ItemReward(@Nonnull ItemStackTemplate stack) {
        this.stack = stack;
    }
    
    public ItemReward(ItemLike item, int count) {
        this(new ItemStackTemplate(Preconditions.checkNotNull(item).asItem(), count));
    }
    
    public ItemReward(ItemLike item) {
        this(item, 1);
    }

    @Override
    protected RewardType<ItemReward> getType() {
        return RewardTypesPM.ITEM.get();
    }

    @Override
    public void grant(ServerPlayer player) {
        ItemStack toGrant = this.stack.create();
        if (!player.addItem(toGrant)) {
            ItemEntity entity = player.drop(toGrant, false);
            if (entity != null) {
                entity.setNoPickUpDelay();
                entity.setTarget(player.getUUID());
            }
        } else {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }
    }

    @Override
    public Component getDescription(Player player) {
        ItemStack descStack = this.stack.create();
        MutableComponent itemName = Component.empty().append(descStack.getHoverName()).withStyle(descStack.getRarity().color());
        if (descStack.has(DataComponents.CUSTOM_NAME)) {
            itemName.withStyle(ChatFormatting.ITALIC);
        }
        return Component.translatable("label.primalmagick.research_table.reward", descStack.getCount(), itemName);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ItemReward that)) return false;
        return Objects.equals(stack, that.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(stack);
    }
}

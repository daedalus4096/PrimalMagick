package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagick.common.util.ItemUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
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
public class ItemReward extends AbstractReward {
    public static final String TYPE = "item";
    public static final IRewardSerializer<ItemReward> SERIALIZER = new Serializer();
    
    private ItemStack stack;
    
    public ItemReward() {
        this.stack = ItemStack.EMPTY;
    }
    
    protected ItemReward(@Nonnull ItemStack stack) {
        this.stack = stack.copy();
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

    @Override
    public String getRewardType() {
        return TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IRewardSerializer<ItemReward> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.put("Stack", this.stack.save(new CompoundTag()));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.stack = ItemStack.of(nbt.getCompound("Stack"));
    }

    public static class Serializer implements IRewardSerializer<ItemReward> {
        @Override
        public ItemReward read(ResourceLocation templateId, JsonObject json) {
            ItemStack stack = ItemUtils.parseItemStack(json.getAsJsonPrimitive("stack").getAsString());
            if (stack == null || stack.isEmpty()) {
                throw new JsonSyntaxException("Invalid item stack for reward in project " + templateId.toString());
            }
            return new ItemReward(stack);
        }

        @Override
        public ItemReward fromNetwork(FriendlyByteBuf buf) {
            return new ItemReward(buf.readItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ItemReward reward) {
            buf.writeItemStack(reward.stack, false);
        }
    }
}

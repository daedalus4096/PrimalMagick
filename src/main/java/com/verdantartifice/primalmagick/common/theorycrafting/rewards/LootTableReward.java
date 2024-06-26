package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

/**
 * Theorycrafting reward that grants random items from a loot table.
 * 
 * @author Daedalus4096
 */
public class LootTableReward extends AbstractReward<LootTableReward> {
    public static final Codec<LootTableReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("lootTable").forGetter(r -> r.lootTable),
            ExtraCodecs.POSITIVE_INT.fieldOf("pullCount").forGetter(r -> r.pullCount),
            Codec.STRING.fieldOf("descTranslationKey").forGetter(r -> r.descTranslationKey)
        ).apply(instance, LootTableReward::new));

    private final ResourceLocation lootTable;
    private final int pullCount;
    private final String descTranslationKey;
    
    protected LootTableReward(ResourceLocation lootTable, int pullCount, String descTranslationKey) {
        this.lootTable = lootTable;
        this.pullCount = pullCount;
        this.descTranslationKey = descTranslationKey;
    }

    @Override
    protected RewardType<LootTableReward> getType() {
        return RewardTypesPM.LOOT_TABLE.get();
    }

    @Override
    public void grant(ServerPlayer player) {
        LootParams params = new LootParams.Builder(player.serverLevel())
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.ORIGIN, player.position())
                .withLuck(player.getLuck())
                .create(LootContextParamSets.ADVANCEMENT_REWARD);
        boolean playSound = false;
        
        for (int index = 0; index < this.pullCount; index++) {
            for (ItemStack stack : player.getServer().getLootData().getLootTable(this.lootTable).getRandomItems(params)) {
                if (!player.addItem(stack)) {
                    ItemEntity entity = player.drop(stack, false);
                    if (entity != null) {
                        entity.setNoPickUpDelay();
                        entity.setTarget(player.getUUID());
                    }
                } else {
                    playSound = true;
                }
            }
        }
        if (playSound) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }
    }

    @Override
    public Component getDescription() {
        return Component.translatable("label.primalmagick.research_table.reward", this.pullCount, Component.translatable(this.descTranslationKey));
    }

    @Nonnull
    static LootTableReward fromNetworkInner(FriendlyByteBuf buf) {
        return new LootTableReward(buf.readResourceLocation(), buf.readVarInt(), buf.readUtf());
    }

    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.lootTable);
        buf.writeVarInt(this.pullCount);
        buf.writeUtf(this.descTranslationKey);
    }
    
    public static Builder builder(ResourceLocation lootTable) {
        return new Builder(lootTable);
    }
    
    public static class Builder {
        protected final ResourceLocation lootTable;
        protected int pullCount = 1;
        protected String descTranslationKey = null;
        
        protected Builder(ResourceLocation lootTable) {
            this.lootTable = Preconditions.checkNotNull(lootTable);
        }
        
        public Builder pulls(int count) {
            this.pullCount = count;
            return this;
        }
        
        public Builder description(String translationKey) {
            this.descTranslationKey = translationKey;
            return this;
        }
        
        private void validate() {
            if (this.pullCount <= 0) {
                throw new IllegalStateException("Reward pull count must be positive");
            } else if (this.descTranslationKey == null || this.descTranslationKey.isEmpty()) {
                throw new IllegalStateException("Reward description translation key must not be empty");
            }
        }
        
        public LootTableReward build() {
            this.validate();
            return new LootTableReward(this.lootTable, this.pullCount, this.descTranslationKey);
        }
    }
}

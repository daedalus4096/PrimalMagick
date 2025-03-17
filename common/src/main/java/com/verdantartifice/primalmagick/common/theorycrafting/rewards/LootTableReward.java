package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Objects;

/**
 * Theorycrafting reward that grants random items from a loot table.
 * 
 * @author Daedalus4096
 */
public class LootTableReward extends AbstractReward<LootTableReward> {
    public static final MapCodec<LootTableReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("lootTable").forGetter(r -> r.lootTable),
            ExtraCodecs.POSITIVE_INT.fieldOf("pullCount").forGetter(r -> r.pullCount),
            Codec.STRING.fieldOf("descTranslationKey").forGetter(r -> r.descTranslationKey)
        ).apply(instance, LootTableReward::new));
    
    public static final StreamCodec<ByteBuf, LootTableReward> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.LOOT_TABLE),
            reward -> reward.lootTable,
            ByteBufCodecs.VAR_INT,
            reward -> reward.pullCount,
            ByteBufCodecs.STRING_UTF8,
            reward -> reward.descTranslationKey,
            LootTableReward::new);

    private final ResourceKey<LootTable> lootTable;
    private final int pullCount;
    private final String descTranslationKey;
    
    protected LootTableReward(ResourceKey<LootTable> lootTable, int pullCount, String descTranslationKey) {
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
            for (ItemStack stack : player.getServer().reloadableRegistries().getLootTable(this.lootTable).getRandomItems(params)) {
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LootTableReward that)) return false;
        return pullCount == that.pullCount && Objects.equals(lootTable, that.lootTable) && Objects.equals(descTranslationKey, that.descTranslationKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lootTable, pullCount, descTranslationKey);
    }

    public static Builder builder(ResourceKey<LootTable> lootTable) {
        return new Builder(lootTable);
    }
    
    public static class Builder {
        protected final ResourceKey<LootTable> lootTable;
        protected int pullCount = 1;
        protected String descTranslationKey = null;
        
        protected Builder(ResourceKey<LootTable> lootTable) {
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

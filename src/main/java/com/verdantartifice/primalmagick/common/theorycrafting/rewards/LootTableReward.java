package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import com.google.gson.JsonObject;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
public class LootTableReward extends AbstractReward {
    public static final String TYPE = "loot_table";
    public static final IRewardSerializer<LootTableReward> SERIALIZER = new Serializer();

    private ResourceLocation lootTable;
    private int pullCount;
    private String descTranslationKey;
    
    public LootTableReward() {
        this.lootTable = null;
        this.pullCount = 0;
        this.descTranslationKey = "";
    }
    
    protected LootTableReward(ResourceLocation lootTable, int pullCount, String descTranslationKey) {
        this.lootTable = lootTable;
        this.pullCount = pullCount;
        this.descTranslationKey = descTranslationKey;
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

    @Override
    public String getRewardType() {
        return TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IRewardSerializer<LootTableReward> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putString("LootTable", this.lootTable.toString());
        tag.putInt("PullCount", this.pullCount);
        tag.putString("DescTranslationKey", this.descTranslationKey);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.lootTable = new ResourceLocation(nbt.getString("LootTable"));
        this.pullCount = nbt.getInt("PullCount");
        this.descTranslationKey = nbt.getString("DescTranslationKey");
    }

    public static class Serializer implements IRewardSerializer<LootTableReward> {
        @Override
        public LootTableReward read(ResourceLocation templateId, JsonObject json) {
            ResourceLocation lootTable = new ResourceLocation(json.getAsJsonPrimitive("table").getAsString());
            int pullCount = json.getAsJsonPrimitive("pulls").getAsInt();
            String descTranslationKey = json.getAsJsonPrimitive("desc").getAsString();
            return new LootTableReward(lootTable, pullCount, descTranslationKey);
        }

        @Override
        public LootTableReward fromNetwork(FriendlyByteBuf buf) {
            return new LootTableReward(buf.readResourceLocation(), buf.readVarInt(), buf.readUtf());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, LootTableReward reward) {
            buf.writeResourceLocation(reward.lootTable);
            buf.writeVarInt(reward.pullCount);
            buf.writeUtf(reward.descTranslationKey);
        }
    }
}

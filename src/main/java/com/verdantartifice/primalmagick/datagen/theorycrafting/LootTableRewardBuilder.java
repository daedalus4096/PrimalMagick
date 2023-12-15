package com.verdantartifice.primalmagick.datagen.theorycrafting;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.LootTableReward;

import net.minecraft.resources.ResourceLocation;

public class LootTableRewardBuilder {
    protected final ResourceLocation lootTable;
    protected final String descTranslationKey;
    protected int pullCount = 1;

    protected LootTableRewardBuilder(ResourceLocation lootTable, String descTranslationKey) {
        this.lootTable = lootTable;
        this.descTranslationKey = descTranslationKey;
    }
    
    public static LootTableRewardBuilder table(ResourceLocation lootTable, String descTranslationKey) {
        return new LootTableRewardBuilder(lootTable, descTranslationKey);
    }
    
    public LootTableRewardBuilder pulls(int pullCount) {
        this.pullCount = pullCount;
        return this;
    }
    
    private void validate() {
        if (this.lootTable == null) {
            throw new IllegalStateException("No loot table for loot table project reward");
        }
        if (Strings.isNullOrEmpty(this.descTranslationKey)) {
            throw new IllegalStateException("No description key for loot table project reward");
        }
        if (this.pullCount <= 0) {
            throw new IllegalStateException("Invalid pull count for loot table project reward");
        }
    }
    
    public IFinishedProjectReward build() {
        this.validate();
        return new LootTableRewardBuilder.Result(this.lootTable, this.pullCount, this.descTranslationKey);
    }
    
    public static class Result implements IFinishedProjectReward {
        protected final ResourceLocation lootTable;
        protected final int pullCount;
        protected final String descTranslationKey;
        
        public Result(ResourceLocation lootTable, int pullCount, String descTranslationKey) {
            this.lootTable = lootTable;
            this.pullCount = pullCount;
            this.descTranslationKey = descTranslationKey;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("type", LootTableReward.TYPE);
            json.addProperty("table", this.lootTable.toString());
            json.addProperty("pulls", this.pullCount);
            json.addProperty("desc", this.descTranslationKey);
        }
    }
}

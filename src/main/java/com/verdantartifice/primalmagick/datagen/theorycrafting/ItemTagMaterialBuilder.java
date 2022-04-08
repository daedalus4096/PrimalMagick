package com.verdantartifice.primalmagick.datagen.theorycrafting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.theorycrafting.ItemTagProjectMaterial;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ItemTagMaterialBuilder {
    protected final ResourceLocation tagName;
    protected final int quantity;
    protected final boolean consumed;
    protected double weight = 1D;
    protected double bonusReward = 0.0D;
    protected CompoundResearchKey requiredResearch;

    protected ItemTagMaterialBuilder(@Nonnull TagKey<Item> tag, int quantity, boolean consumed) {
        this.tagName = tag.location();
        this.quantity = quantity;
        this.consumed = consumed;
    }
    
    public static ItemTagMaterialBuilder tag(@Nonnull TagKey<Item> tag, boolean consumed) {
        return tag(tag, 1, consumed);
    }
    
    public static ItemTagMaterialBuilder tag(@Nonnull TagKey<Item> tag, int quantity, boolean consumed) {
        return new ItemTagMaterialBuilder(tag, quantity, consumed);
    }
    
    public ItemTagMaterialBuilder weight(double weight) {
        this.weight = weight;
        return this;
    }
    
    public ItemTagMaterialBuilder requiredResearch(@Nullable CompoundResearchKey key) {
        this.requiredResearch = key;
        return this;
    }
    
    public ItemTagMaterialBuilder requiredResearch(@Nullable SimpleResearchKey key) {
        return requiredResearch(CompoundResearchKey.from(key));
    }
    
    public ItemTagMaterialBuilder requiredResearch(@Nullable String keyStr) {
        return requiredResearch(CompoundResearchKey.parse(keyStr));
    }
    
    public ItemTagMaterialBuilder bonusReward(double bonus) {
        this.bonusReward = bonus;
        return this;
    }
    
    private void validate() {
        if (this.tagName == null) {
            throw new IllegalStateException("No tag name for item tag project material");
        }
        if (this.quantity <= 0) {
            throw new IllegalStateException("Invalid quantity for item tag project material");
        }
        if (this.weight <= 0D) {
            throw new IllegalStateException("Invalid weight for item tag project material");
        }
        if (this.bonusReward < 0.0D) {
            throw new IllegalStateException("Invalid bonus reward for item tag project material");
        }
    }
    
    public IFinishedProjectMaterial build() {
        this.validate();
        return new ItemTagMaterialBuilder.Result(this.tagName, this.quantity, this.consumed, this.weight, this.bonusReward, this.requiredResearch);
    }
    
    public static class Result implements IFinishedProjectMaterial {
        protected final ResourceLocation tagName;
        protected final int quantity;
        protected final boolean consumed;
        protected final double weight;
        protected final double bonusReward;
        protected final CompoundResearchKey requiredResearch;
        
        public Result(@Nonnull ResourceLocation tagName, int quantity, boolean consumed, double weight, double bonusReward, @Nullable CompoundResearchKey requiredResearch) {
            this.tagName = tagName;
            this.quantity = quantity;
            this.consumed = consumed;
            this.weight = weight;
            this.bonusReward = bonusReward;
            this.requiredResearch = requiredResearch == null ? null : requiredResearch.copy();
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("type", ItemTagProjectMaterial.TYPE);
            json.addProperty("name", this.tagName.toString());
            json.addProperty("quantity", this.quantity);
            json.addProperty("consumed", this.consumed);
            json.addProperty("weight", this.weight);
            if (this.bonusReward > 0.0D) {
                json.addProperty("bonus_reward", this.bonusReward);
            }
            if (this.requiredResearch != null) {
                json.addProperty("required_research", this.requiredResearch.toString());
            }
        }
    }
}

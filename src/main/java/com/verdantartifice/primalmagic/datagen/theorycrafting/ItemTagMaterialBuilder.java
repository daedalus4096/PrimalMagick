package com.verdantartifice.primalmagic.datagen.theorycrafting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemTagProjectMaterial;

import net.minecraft.resources.ResourceLocation;

public class ItemTagMaterialBuilder {
    protected final ResourceLocation tagName;
    protected final int quantity;
    protected final boolean consumed;
    protected double weight = 1D;
    protected CompoundResearchKey requiredResearch;

    protected ItemTagMaterialBuilder(@Nonnull ResourceLocation tagName, int quantity, boolean consumed) {
        this.tagName = tagName;
        this.quantity = quantity;
        this.consumed = consumed;
    }
    
    public static ItemTagMaterialBuilder tag(@Nonnull String namespace, @Nonnull String path, boolean consumed) {
        return tag(namespace, path, 1, consumed);
    }
    
    public static ItemTagMaterialBuilder tag(@Nonnull ResourceLocation tagName, boolean consumed) {
        return tag(tagName, 1, consumed);
    }
    
    public static ItemTagMaterialBuilder tag(@Nonnull String namespace, @Nonnull String path, int quantity, boolean consumed) {
        return tag(new ResourceLocation(namespace, path), quantity, consumed);
    }
    
    public static ItemTagMaterialBuilder tag(@Nonnull ResourceLocation tagName, int quantity, boolean consumed) {
        return new ItemTagMaterialBuilder(tagName, quantity, consumed);
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
    }
    
    public IFinishedProjectMaterial build() {
        this.validate();
        return new ItemTagMaterialBuilder.Result(this.tagName, this.quantity, this.consumed, this.weight, this.requiredResearch);
    }
    
    public static class Result implements IFinishedProjectMaterial {
        protected final ResourceLocation tagName;
        protected final int quantity;
        protected final boolean consumed;
        protected final double weight;
        protected final CompoundResearchKey requiredResearch;
        
        public Result(@Nonnull ResourceLocation tagName, int quantity, boolean consumed, double weight, @Nullable CompoundResearchKey requiredResearch) {
            this.tagName = tagName;
            this.quantity = quantity;
            this.consumed = consumed;
            this.weight = weight;
            this.requiredResearch = requiredResearch == null ? null : requiredResearch.copy();
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("type", ItemTagProjectMaterial.TYPE);
            json.addProperty("name", this.tagName.toString());
            json.addProperty("quantity", this.quantity);
            json.addProperty("consumed", this.consumed);
            json.addProperty("weight", this.weight);
            if (this.requiredResearch != null) {
                json.addProperty("required_research", this.requiredResearch.toString());
            }
        }
    }
}

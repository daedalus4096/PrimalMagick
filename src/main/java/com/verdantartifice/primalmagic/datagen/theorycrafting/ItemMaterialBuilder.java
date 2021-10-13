package com.verdantartifice.primalmagic.datagen.theorycrafting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagic.common.util.ItemUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ItemMaterialBuilder {
    protected final ItemStack stack;
    protected final boolean consumed;
    protected boolean matchNBT = false;
    protected double weight = 1D;
    protected int afterCrafting = 0;
    protected CompoundResearchKey requiredResearch;
    
    protected ItemMaterialBuilder(@Nonnull ItemStack stack, boolean consumed) {
        this.stack = stack.copy();
        this.consumed = consumed;
    }
    
    public static ItemMaterialBuilder item(@Nonnull ItemLike item, boolean consumed) {
        return item(item, 1, null, consumed);
    }
    
    public static ItemMaterialBuilder item(@Nonnull ItemLike item, int count, boolean consumed) {
        return item(item, count, null, consumed);
    }
    
    public static ItemMaterialBuilder item(@Nonnull ItemLike item, int count, @Nullable CompoundTag nbt, boolean consumed) {
        ItemStack stack = new ItemStack(item, count);
        if (nbt != null) {
            stack.setTag(nbt);
        }
        return item(stack, consumed);
    }
    
    public static ItemMaterialBuilder item(@Nonnull ItemStack stack, boolean consumed) {
        return new ItemMaterialBuilder(stack, consumed);
    }
    
    public ItemMaterialBuilder weight(double weight) {
        this.weight = weight;
        return this;
    }
    
    public ItemMaterialBuilder matchNbt() {
        this.matchNBT = true;
        return this;
    }
    
    public ItemMaterialBuilder requiredResearch(@Nullable CompoundResearchKey key) {
        this.requiredResearch = key.copy();
        return this;
    }
    
    public ItemMaterialBuilder requiredResearch(@Nullable SimpleResearchKey key) {
        return requiredResearch(CompoundResearchKey.from(key));
    }
    
    public ItemMaterialBuilder requiredResearch(@Nullable String keyStr) {
        return requiredResearch(CompoundResearchKey.parse(keyStr)); 
    }
    
    public ItemMaterialBuilder afterCrafting(int after) {
        this.afterCrafting = after;
        return this;
    }
    
    private void validate() {
        if (this.stack == null || this.stack == ItemStack.EMPTY) {
            throw new IllegalStateException("No item stack for item project material");
        }
        if (this.weight <= 0D) {
            throw new IllegalStateException("Invalid weight for item project material");
        }
        if (this.afterCrafting < 0) {
            throw new IllegalStateException("Invalid minimum craft count value for item project material");
        }
    }
    
    public IFinishedProjectMaterial build() {
        this.validate();
        return new ItemMaterialBuilder.Result(this.stack, this.consumed, this.matchNBT, this.weight, this.afterCrafting, this.requiredResearch);
    }
    
    public static class Result implements IFinishedProjectMaterial {
        protected final ItemStack stack;
        protected final boolean consumed;
        protected final boolean matchNBT;
        protected final double weight;
        protected final int afterCrafting;
        protected final CompoundResearchKey requiredResearch;
        
        public Result(@Nonnull ItemStack stack, boolean consumed, boolean matchNBT, double weight, int afterCrafting, @Nullable CompoundResearchKey requiredResearch) {
            this.stack = stack.copy();
            this.consumed = consumed;
            this.matchNBT = matchNBT;
            this.weight = weight;
            this.afterCrafting = afterCrafting;
            this.requiredResearch = requiredResearch == null ? null : requiredResearch.copy();
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("type", ItemProjectMaterial.TYPE);
            json.addProperty("stack", ItemUtils.serializeItemStack(this.stack));
            json.addProperty("consumed", this.consumed);
            json.addProperty("match_nbt", this.matchNBT);
            json.addProperty("weight", this.weight);
            if (this.afterCrafting > 0) {
                json.addProperty("after_crafting", this.afterCrafting);
            }
            if (this.requiredResearch != null) {
                json.addProperty("required_research", this.requiredResearch.toString());
            }
        }
    }
}

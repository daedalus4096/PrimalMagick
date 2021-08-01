package com.verdantartifice.primalmagic.common.affinities;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

public abstract class AbstractAffinity implements IAffinity {
    protected ResourceLocation targetId;
    protected SourceList totalCache;

    protected AbstractAffinity(ResourceLocation target) {
        this.targetId = target;
    }
    
    @Override
    public ResourceLocation getTarget() {
        return this.targetId;
    }

    @Override
    public SourceList getTotal(@Nonnull RecipeManager recipeManager) {
        if (this.totalCache == null) {
            this.totalCache = this.calculateTotal(recipeManager);
        }
        return this.totalCache.copy();
    }
    
    protected abstract SourceList calculateTotal(@Nonnull RecipeManager recipeManager);
}

package com.verdantartifice.primalmagick.common.affinities;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.RegistryAccess;
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
    public SourceList getTotal(@Nullable RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        if (this.totalCache == null) {
            this.totalCache = this.calculateTotal(recipeManager, registryAccess, history);
        }
        return this.totalCache == null ? null : this.totalCache.copy();
    }
    
    protected abstract SourceList calculateTotal(@Nullable RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history);
}

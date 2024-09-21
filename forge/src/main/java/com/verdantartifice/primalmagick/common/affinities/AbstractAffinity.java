package com.verdantartifice.primalmagick.common.affinities;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

public abstract class AbstractAffinity implements IAffinity {
    protected ResourceLocation targetId;
    protected CompletableFuture<SourceList> totalCache;

    protected AbstractAffinity(ResourceLocation target) {
        this.targetId = target;
    }
    
    @Override
    public ResourceLocation getTarget() {
        return this.targetId;
    }

    @Override
    public CompletableFuture<SourceList> getTotalAsync(@Nullable RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        if (this.totalCache == null) {
            this.totalCache = this.calculateTotalAsync(recipeManager, registryAccess, history);
        }
        return this.totalCache;
    }
    
    protected abstract CompletableFuture<SourceList> calculateTotalAsync(@Nullable RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history);
}

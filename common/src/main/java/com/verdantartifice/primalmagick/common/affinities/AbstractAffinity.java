package com.verdantartifice.primalmagick.common.affinities;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractAffinity<T extends AbstractAffinity<T>> implements IAffinity {
    public static Codec<AbstractAffinity<?>> dispatchCodec() {
        return Services.AFFINITY_TYPES_REGISTRY.codec().dispatch("type", AbstractAffinity::getType, AffinityType::codec);
    }

    public static StreamCodec<RegistryFriendlyByteBuf, AbstractAffinity<?>> dispatchStreamCodec() {
        return Services.AFFINITY_TYPES_REGISTRY.registryFriendlyStreamCodec().dispatch(AbstractAffinity::getType, AffinityType::streamCodec);
    }

    protected ResourceLocation targetId;
    protected CompletableFuture<SourceList> totalCache;

    protected AbstractAffinity(@NotNull ResourceLocation target) {
        this.targetId = target;
    }
    
    @Override
    public @NotNull ResourceLocation getTarget() {
        return this.targetId;
    }

    public abstract @NotNull AffinityType<T> getType();

    @Override
    public @NotNull CompletableFuture<SourceList> getTotalAsync(@Nullable RecipeManager recipeManager, @NotNull RegistryAccess registryAccess, @NotNull List<ResourceLocation> history) {
        if (this.totalCache == null) {
            this.totalCache = this.calculateTotalAsync(recipeManager, registryAccess, history);
        }
        return this.totalCache;
    }
    
    protected abstract CompletableFuture<SourceList> calculateTotalAsync(@Nullable RecipeManager recipeManager, @NotNull RegistryAccess registryAccess, @NotNull List<ResourceLocation> history);
}

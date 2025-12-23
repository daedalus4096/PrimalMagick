package com.verdantartifice.primalmagick.common.affinities;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.JsonUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EntityTypeAffinity extends AbstractAffinity<EntityTypeAffinity> {
    public static final MapCodec<EntityTypeAffinity> CODEC = RecordCodecBuilder.<EntityTypeAffinity>mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("target").forGetter(EntityTypeAffinity::getTarget),
            SourceList.CODEC.fieldOf("values").forGetter(eta -> eta.values)
        ).apply(instance, EntityTypeAffinity::new)).validate(eta -> Services.ENTITY_TYPES_REGISTRY.containsKey(eta.targetId) ?
            DataResult.success(eta) :
            DataResult.error(() -> "Unknown target entity type " + eta.targetId
        ));

    public static final StreamCodec<RegistryFriendlyByteBuf, EntityTypeAffinity> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, EntityTypeAffinity::getTarget,
            SourceList.STREAM_CODEC, eta -> eta.values,
            EntityTypeAffinity::new);
    
    protected final SourceList values;
    
    protected EntityTypeAffinity(@NotNull ResourceLocation target, @NotNull SourceList values) {
        super(target);
        this.values = values;
    }

    @Override
    protected AffinityType<EntityTypeAffinity> getType() {
        return AffinityTypesPM.ENTITY_TYPE.get();
    }

    @Override
    protected CompletableFuture<SourceList> calculateTotalAsync(@Nullable RecipeManager recipeManager, @NotNull RegistryAccess registryAccess, @NotNull List<ResourceLocation> history) {
        return CompletableFuture.completedFuture(this.values);
    }
}

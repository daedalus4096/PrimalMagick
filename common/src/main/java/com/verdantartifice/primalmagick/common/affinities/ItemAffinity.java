package com.verdantartifice.primalmagick.common.affinities;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ItemAffinity extends AbstractAffinity<ItemAffinity> {
    public static final MapCodec<ItemAffinity> CODEC = RecordCodecBuilder.<ItemAffinity>mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("target").forGetter(ItemAffinity::getTarget),
            Codec.mapEither(FixedValues.CODEC, DerivedValues.CODEC).forGetter(ia -> ia.valuesEither),
            ResourceLocation.CODEC.optionalFieldOf("recipe").forGetter(ItemAffinity::getSourceRecipe)
        ).apply(instance, ItemAffinity::new)).validate(ia -> ia.valuesEither.map(
            fv -> DataResult.success(ia),
            dv -> ia.targetId.equals(dv.base) ? DataResult.error(() -> "Affinity base must not match target " + ia.targetId) : DataResult.success(ia)
        ));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemAffinity> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, ItemAffinity::getTarget,
            ByteBufCodecs.either(FixedValues.STREAM_CODEC, DerivedValues.STREAM_CODEC), ia -> ia.valuesEither,
            ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), ItemAffinity::getSourceRecipe,
            ItemAffinity::new);

    protected final Either<FixedValues, DerivedValues> valuesEither;
    protected final Optional<ResourceLocation> sourceRecipe;
    
    protected ItemAffinity(@NotNull ResourceLocation target, @NotNull Either<FixedValues, DerivedValues> valuesEither, @NotNull Optional<ResourceLocation> recipe) {
        super(target);
        this.valuesEither = valuesEither;
        this.sourceRecipe = recipe;
    }
    
    public static ItemAffinity fixed(@NotNull ResourceLocation target, @NotNull SourceList setValues, @NotNull Optional<ResourceLocation> recipe) {
        return new ItemAffinity(target, Either.left(new FixedValues(setValues)), recipe);
    }

    @Override
    protected AffinityType<ItemAffinity> getType() {
        return AffinityTypesPM.ITEM.get();
    }

    public Optional<ResourceLocation> getSourceRecipe() {
        return this.sourceRecipe;
    }
    
    @Override
    protected CompletableFuture<SourceList> calculateTotalAsync(@Nullable RecipeManager recipeManager, @NotNull RegistryAccess registryAccess, @NotNull List<ResourceLocation> history) {
        return this.valuesEither.map(
            fv -> CompletableFuture.completedFuture(fv.set),
            dv -> recipeManager == null ?
                    CompletableFuture.completedFuture(SourceList.EMPTY) :
                    AffinityManager.getInstance().getOrGenerateItemAffinityAsync(dv.base, recipeManager, registryAccess, history)
                            .thenCompose(baseEntry -> baseEntry == null ?
                                    CompletableFuture.completedFuture(SourceList.EMPTY) :
                                    baseEntry.getTotalAsync(recipeManager, registryAccess, history))
                            .thenApply(baseSources -> baseSources.add(dv.add).remove(dv.remove))
        );
    }

    public record FixedValues(SourceList set) {
        public static final MapCodec<FixedValues> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                SourceList.CODEC.fieldOf("set").forGetter(FixedValues::set)
            ).apply(instance, FixedValues::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, FixedValues> STREAM_CODEC = StreamCodec.composite(
                SourceList.STREAM_CODEC, FixedValues::set,
                FixedValues::new);
    }

    public record DerivedValues(ResourceLocation base, SourceList add, SourceList remove) {
        public static final MapCodec<DerivedValues> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("base").validate(
                        rl -> Services.ITEMS_REGISTRY.containsKey(rl) ?
                                DataResult.success(rl) :
                                DataResult.error(() -> "Item " + rl.toString() + " not defined")
                    ).forGetter(DerivedValues::base),
                SourceList.CODEC.optionalFieldOf("add", SourceList.EMPTY).forGetter(DerivedValues::add),
                SourceList.CODEC.optionalFieldOf("remove", SourceList.EMPTY).forGetter(DerivedValues::remove)
            ).apply(instance, DerivedValues::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, DerivedValues> STREAM_CODEC = StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, DerivedValues::base,
                SourceList.STREAM_CODEC, DerivedValues::add,
                SourceList.STREAM_CODEC, DerivedValues::remove,
                DerivedValues::new);
    }
}

package com.verdantartifice.primalmagick.common.affinities;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EnchantmentBonusAffinity extends AbstractAffinity<EnchantmentBonusAffinity> {
    // FIXME Validate that target enchantment exists
    public static final MapCodec<EnchantmentBonusAffinity> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("target").forGetter(EnchantmentBonusAffinity::getTarget),
            SourceList.CODEC.fieldOf("multiplier").forGetter(eba -> eba.multiplierValues)
        ).apply(instance, EnchantmentBonusAffinity::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, EnchantmentBonusAffinity> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, EnchantmentBonusAffinity::getTarget,
            SourceList.STREAM_CODEC, eba -> eba.multiplierValues,
            EnchantmentBonusAffinity::new);
    
    protected final SourceList multiplierValues;
    
    protected EnchantmentBonusAffinity(@NotNull ResourceLocation target, @NotNull SourceList multiplierValues) {
        super(target);
        this.multiplierValues = multiplierValues;
    }

    @Override
    protected AffinityType<EnchantmentBonusAffinity> getType() {
        return AffinityTypesPM.ENCHANTMENT_BONUS.get();
    }

    @Override
    protected CompletableFuture<SourceList> calculateTotalAsync(@Nullable RecipeManager recipeManager, @NotNull RegistryAccess registryAccess, @NotNull List<ResourceLocation> history) {
        return CompletableFuture.completedFuture(this.multiplierValues);
    }
}

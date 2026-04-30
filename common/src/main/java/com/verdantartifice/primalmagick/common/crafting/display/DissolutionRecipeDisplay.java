package com.verdantartifice.primalmagick.common.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;

public record DissolutionRecipeDisplay(SlotDisplay ingredient, SlotDisplay result, SourceList manaCosts, SlotDisplay craftingStation) implements RecipeDisplay {
    public static final MapCodec<DissolutionRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SlotDisplay.CODEC.fieldOf("ingredient").forGetter(DissolutionRecipeDisplay::ingredient),
            SlotDisplay.CODEC.fieldOf("result").forGetter(DissolutionRecipeDisplay::result),
            SourceList.CODEC.fieldOf("manaCosts").forGetter(DissolutionRecipeDisplay::manaCosts),
            SlotDisplay.CODEC.fieldOf("craftingStation").forGetter(DissolutionRecipeDisplay::craftingStation)
    ).apply(instance, DissolutionRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DissolutionRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC, DissolutionRecipeDisplay::ingredient,
            SlotDisplay.STREAM_CODEC, DissolutionRecipeDisplay::result,
            SourceList.STREAM_CODEC, DissolutionRecipeDisplay::manaCosts,
            SlotDisplay.STREAM_CODEC, DissolutionRecipeDisplay::craftingStation,
            DissolutionRecipeDisplay::new
    );

    public static final RecipeDisplay.Type<DissolutionRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);

    @Override
    @NotNull
    public Type<? extends RecipeDisplay> type() {
        return TYPE;
    }
}

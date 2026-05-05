package com.verdantartifice.primalmagick.common.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record RunecarvingRecipeDisplay(SlotDisplay baseIngredient, SlotDisplay etchingIngredient, SlotDisplay result,
                                       Optional<AbstractRequirement<?>> requirement, SlotDisplay craftingStation) implements RecipeDisplay {
    public static final MapCodec<RunecarvingRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SlotDisplay.CODEC.fieldOf("baseIngredient").forGetter(RunecarvingRecipeDisplay::baseIngredient),
            SlotDisplay.CODEC.fieldOf("etchingIngredient").forGetter(RunecarvingRecipeDisplay::etchingIngredient),
            SlotDisplay.CODEC.fieldOf("result").forGetter(RunecarvingRecipeDisplay::result),
            AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(RunecarvingRecipeDisplay::requirement),
            SlotDisplay.CODEC.fieldOf("craftingStation").forGetter(RunecarvingRecipeDisplay::craftingStation)
    ).apply(instance, RunecarvingRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RunecarvingRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC, RunecarvingRecipeDisplay::baseIngredient,
            SlotDisplay.STREAM_CODEC, RunecarvingRecipeDisplay::etchingIngredient,
            SlotDisplay.STREAM_CODEC, RunecarvingRecipeDisplay::result,
            ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()), RunecarvingRecipeDisplay::requirement,
            SlotDisplay.STREAM_CODEC, RunecarvingRecipeDisplay::craftingStation,
            RunecarvingRecipeDisplay::new
    );

    public static final RecipeDisplay.Type<RunecarvingRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);

    @Override
    @NotNull
    public Type<? extends RecipeDisplay> type() {
        return TYPE;
    }
}

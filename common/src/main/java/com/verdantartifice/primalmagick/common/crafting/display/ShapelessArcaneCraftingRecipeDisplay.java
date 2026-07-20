package com.verdantartifice.primalmagick.common.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public record ShapelessArcaneCraftingRecipeDisplay(List<SlotDisplay> ingredients, SlotDisplay result, SourceList manaCosts,
                                                   Optional<AbstractRequirement<?>> requirement, ExpertiseRecipeDisplay expertise,
                                                   SlotDisplay craftingStation) implements RecipeDisplay, IManaCostRecipeDisplay {
    public static final MapCodec<ShapelessArcaneCraftingRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(ShapelessArcaneCraftingRecipeDisplay::ingredients),
            SlotDisplay.CODEC.fieldOf("result").forGetter(ShapelessArcaneCraftingRecipeDisplay::result),
            SourceList.CODEC.fieldOf("manaCosts").forGetter(ShapelessArcaneCraftingRecipeDisplay::manaCosts),
            AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(ShapelessArcaneCraftingRecipeDisplay::requirement),
            ExpertiseRecipeDisplay.CODEC.fieldOf("expertise").forGetter(ShapelessArcaneCraftingRecipeDisplay::expertise),
            SlotDisplay.CODEC.fieldOf("craftingStation").forGetter(ShapelessArcaneCraftingRecipeDisplay::craftingStation)
    ).apply(instance, ShapelessArcaneCraftingRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessArcaneCraftingRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), ShapelessArcaneCraftingRecipeDisplay::ingredients,
            SlotDisplay.STREAM_CODEC, ShapelessArcaneCraftingRecipeDisplay::result,
            SourceList.STREAM_CODEC, ShapelessArcaneCraftingRecipeDisplay::manaCosts,
            ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()), ShapelessArcaneCraftingRecipeDisplay::requirement,
            ExpertiseRecipeDisplay.STREAM_CODEC, ShapelessArcaneCraftingRecipeDisplay::expertise,
            SlotDisplay.STREAM_CODEC, ShapelessArcaneCraftingRecipeDisplay::craftingStation,
            ShapelessArcaneCraftingRecipeDisplay::new
    );

    public static final RecipeDisplay.Type<ShapelessArcaneCraftingRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);

    @Override
    @NotNull
    public Type<? extends RecipeDisplay> type() {
        return TYPE;
    }
}

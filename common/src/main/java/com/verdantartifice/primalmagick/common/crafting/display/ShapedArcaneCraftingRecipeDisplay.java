package com.verdantartifice.primalmagick.common.crafting.display;

import com.mojang.serialization.Codec;
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

public record ShapedArcaneCraftingRecipeDisplay(int width, int height, List<SlotDisplay> ingredients, SlotDisplay result,
                                                SourceList manaCosts, Optional<AbstractRequirement<?>> requirement,
                                                ExpertiseRecipeDisplay expertise, SlotDisplay craftingStation) implements RecipeDisplay {
    public static final MapCodec<ShapedArcaneCraftingRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("width").forGetter(ShapedArcaneCraftingRecipeDisplay::width),
            Codec.INT.fieldOf("height").forGetter(ShapedArcaneCraftingRecipeDisplay::height),
            SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(ShapedArcaneCraftingRecipeDisplay::ingredients),
            SlotDisplay.CODEC.fieldOf("result").forGetter(ShapedArcaneCraftingRecipeDisplay::result),
            SourceList.CODEC.fieldOf("manaCosts").forGetter(ShapedArcaneCraftingRecipeDisplay::manaCosts),
            AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(ShapedArcaneCraftingRecipeDisplay::requirement),
            ExpertiseRecipeDisplay.CODEC.fieldOf("expertise").forGetter(ShapedArcaneCraftingRecipeDisplay::expertise),
            SlotDisplay.CODEC.fieldOf("craftingStation").forGetter(ShapedArcaneCraftingRecipeDisplay::craftingStation)
    ).apply(instance, ShapedArcaneCraftingRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShapedArcaneCraftingRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ShapedArcaneCraftingRecipeDisplay::width,
            ByteBufCodecs.VAR_INT, ShapedArcaneCraftingRecipeDisplay::height,
            SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), ShapedArcaneCraftingRecipeDisplay::ingredients,
            SlotDisplay.STREAM_CODEC, ShapedArcaneCraftingRecipeDisplay::result,
            SourceList.STREAM_CODEC, ShapedArcaneCraftingRecipeDisplay::manaCosts,
            ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()), ShapedArcaneCraftingRecipeDisplay::requirement,
            ExpertiseRecipeDisplay.STREAM_CODEC, ShapedArcaneCraftingRecipeDisplay::expertise,
            SlotDisplay.STREAM_CODEC, ShapedArcaneCraftingRecipeDisplay::craftingStation,
            ShapedArcaneCraftingRecipeDisplay::new
    );

    public static final RecipeDisplay.Type<ShapedArcaneCraftingRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);

    @Override
    @NotNull
    public Type<? extends RecipeDisplay> type() {
        return TYPE;
    }
}

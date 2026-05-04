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

public record ConcoctingRecipeDisplay(List<SlotDisplay> ingredients, SlotDisplay result, SourceList manaCosts,
                                      Optional<AbstractRequirement<?>> requirement, SlotDisplay craftingStation) implements RecipeDisplay {
    public static final MapCodec<ConcoctingRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(ConcoctingRecipeDisplay::ingredients),
            SlotDisplay.CODEC.fieldOf("result").forGetter(ConcoctingRecipeDisplay::result),
            SourceList.CODEC.fieldOf("manaCosts").forGetter(ConcoctingRecipeDisplay::manaCosts),
            AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(ConcoctingRecipeDisplay::requirement),
            SlotDisplay.CODEC.fieldOf("craftingStation").forGetter(ConcoctingRecipeDisplay::craftingStation)
    ).apply(instance, ConcoctingRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ConcoctingRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), ConcoctingRecipeDisplay::ingredients,
            SlotDisplay.STREAM_CODEC, ConcoctingRecipeDisplay::result,
            SourceList.STREAM_CODEC, ConcoctingRecipeDisplay::manaCosts,
            ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()), ConcoctingRecipeDisplay::requirement,
            SlotDisplay.STREAM_CODEC, ConcoctingRecipeDisplay::craftingStation,
            ConcoctingRecipeDisplay::new
    );

    public static final RecipeDisplay.Type<ConcoctingRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);

    @Override
    @NotNull
    public Type<? extends RecipeDisplay> type() {
        return TYPE;
    }
}

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

public record RitualRecipeDisplay(List<SlotDisplay> ingredients, List<SlotDisplay> props, SlotDisplay result, SourceList manaCosts,
                                  Optional<AbstractRequirement<?>> requirement, SlotDisplay craftingStation) implements RecipeDisplay {
    public static final MapCodec<RitualRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(RitualRecipeDisplay::ingredients),
            SlotDisplay.CODEC.listOf().fieldOf("props").forGetter(RitualRecipeDisplay::props),
            SlotDisplay.CODEC.fieldOf("result").forGetter(RitualRecipeDisplay::result),
            SourceList.CODEC.fieldOf("manaCosts").forGetter(RitualRecipeDisplay::manaCosts),
            AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(RitualRecipeDisplay::requirement),
            SlotDisplay.CODEC.fieldOf("craftingStation").forGetter(RitualRecipeDisplay::craftingStation)
    ).apply(instance, RitualRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RitualRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), RitualRecipeDisplay::ingredients,
            SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), RitualRecipeDisplay::props,
            SlotDisplay.STREAM_CODEC, RitualRecipeDisplay::result,
            SourceList.STREAM_CODEC, RitualRecipeDisplay::manaCosts,
            ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()), RitualRecipeDisplay::requirement,
            SlotDisplay.STREAM_CODEC, RitualRecipeDisplay::craftingStation,
            RitualRecipeDisplay::new
    );

    public static final RecipeDisplay.Type<RitualRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);

    @Override
    @NotNull
    public Type<? extends RecipeDisplay> type() {
        return TYPE;
    }
}

package com.verdantartifice.primalmagick.common.crafting.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.crafting.IHasExpertise;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Optional;

public record ExpertiseRecipeDisplay(int baseValue, int bonusValue, Optional<Identifier> groupOpt) {
    public static final Codec<ExpertiseRecipeDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("baseValue").forGetter(ExpertiseRecipeDisplay::baseValue),
            Codec.INT.fieldOf("bonusValue").forGetter(ExpertiseRecipeDisplay::bonusValue),
            Identifier.CODEC.optionalFieldOf("group").forGetter(ExpertiseRecipeDisplay::groupOpt)
        ).apply(instance, ExpertiseRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ExpertiseRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ExpertiseRecipeDisplay::baseValue,
            ByteBufCodecs.VAR_INT, ExpertiseRecipeDisplay::bonusValue,
            ByteBufCodecs.optional(Identifier.STREAM_CODEC), ExpertiseRecipeDisplay::groupOpt,
            ExpertiseRecipeDisplay::new);

    public ExpertiseRecipeDisplay(IHasExpertise recipe, RegistryAccess registryAccess) {
        this(recipe.getExpertiseReward(registryAccess), recipe.getBonusExpertiseReward(registryAccess), recipe.getExpertiseGroup());
    }

    public boolean isBonusEligible(Player player, ResourceKey<Recipe<?>> recipeKey) {
        return ExpertiseManager.isBonusEligible(player, this.groupOpt(), recipeKey);
    }

    public boolean hasReward() {
        return this.baseValue() > 0 || this.bonusValue() > 0;
    }
}

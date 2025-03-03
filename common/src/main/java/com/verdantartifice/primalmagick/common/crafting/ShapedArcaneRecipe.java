package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Definition for a shaped arcane recipe.  Like a vanilla shaped recipe, but has research and optional mana requirements.
 * 
 * @author Daedalus4096
 */
public class ShapedArcaneRecipe extends ShapedRecipe implements IShapedArcaneRecipePM {
    public static final int MAX_WIDTH = 3;
    public static final int MAX_HEIGHT = 3;

    protected final ShapedRecipePattern pattern;
    protected final Optional<AbstractRequirement<?>> requirement;
    protected final SourceList manaCosts;
    protected final Optional<Integer> baseExpertiseOverride;
    protected final Optional<Integer> bonusExpertiseOverride;
    protected final Optional<ResourceLocation> expertiseGroup;
    protected final Optional<ResearchDisciplineKey> disciplineOverride;
    
    public ShapedArcaneRecipe(String group, ItemStack output, ShapedRecipePattern pattern, Optional<AbstractRequirement<?>> requirement, SourceList manaCosts, Optional<Integer> baseExpertiseOverride,
            Optional<Integer> bonusExpertiseOverride, Optional<ResourceLocation> expertiseGroup, Optional<ResearchDisciplineKey> disciplineOverride) {
        super(group, CraftingBookCategory.MISC, pattern, output, false);
        this.pattern = pattern;
        this.requirement = requirement;
        this.manaCosts = manaCosts;
        this.baseExpertiseOverride = baseExpertiseOverride;
        this.bonusExpertiseOverride = bonusExpertiseOverride;
        this.expertiseGroup = expertiseGroup;
        this.disciplineOverride = disciplineOverride;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.ARCANE_CRAFTING_SHAPED.get();
    }

    @Override
    public Optional<AbstractRequirement<?>> getRequirement() {
        return this.requirement;
    }
    
    @Override
    public @NotNull SourceList getManaCosts() {
        return this.manaCosts;
    }

    @Override
    public int getExpertiseReward(RegistryAccess registryAccess) {
        return this.baseExpertiseOverride.orElseGet(() -> this.getResearchTier(registryAccess).map(ResearchTier::getDefaultExpertise).orElse(0));
    }

    @Override
    public int getBonusExpertiseReward(RegistryAccess registryAccess) {
        return this.bonusExpertiseOverride.orElseGet(() -> this.getResearchTier(registryAccess).map(ResearchTier::getDefaultBonusExpertise).orElse(0));
    }

    @Override
    public Optional<ResourceLocation> getExpertiseGroup() {
        return this.expertiseGroup;
    }

    @Override
    public Optional<ResearchDisciplineKey> getResearchDisciplineOverride() {
        return this.disciplineOverride;
    }

    public static class Serializer implements RecipeSerializer<ShapedArcaneRecipe> {
        @Override
        public MapCodec<ShapedArcaneRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(ShapedRecipe::getGroup),
                    ItemStack.CODEC.fieldOf("result").forGetter(r -> r.getResultItem(null)),
                    ShapedRecipePattern.MAP_CODEC.forGetter(r -> r.pattern),
                    AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(r -> r.requirement),
                    SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(r -> r.manaCosts),
                    Codec.INT.optionalFieldOf("baseExpertiseOverride").forGetter(r -> r.baseExpertiseOverride),
                    Codec.INT.optionalFieldOf("bonusExpertiseOverride").forGetter(r -> r.bonusExpertiseOverride),
                    ResourceLocation.CODEC.optionalFieldOf("expertiseGroup").forGetter(r -> r.expertiseGroup),
                    ResearchDisciplineKey.CODEC.codec().optionalFieldOf("disciplineOverride").forGetter(r -> r.disciplineOverride)
            ).apply(instance, ShapedArcaneRecipe::new));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShapedArcaneRecipe> streamCodec() {
            return StreamCodecUtils.composite(
                    ByteBufCodecs.STRING_UTF8, ShapedRecipe::getGroup,
                    ItemStack.STREAM_CODEC, r -> r.getResultItem(null),
                    ShapedRecipePattern.STREAM_CODEC, r -> r.pattern,
                    ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()), r -> r.requirement,
                    SourceList.STREAM_CODEC, r -> r.manaCosts,
                    ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), r -> r.baseExpertiseOverride,
                    ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), r -> r.bonusExpertiseOverride,
                    ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), r -> r.expertiseGroup,
                    ByteBufCodecs.optional(ResearchDisciplineKey.STREAM_CODEC), r -> r.disciplineOverride,
                    ShapedArcaneRecipe::new);
        }
    }
}

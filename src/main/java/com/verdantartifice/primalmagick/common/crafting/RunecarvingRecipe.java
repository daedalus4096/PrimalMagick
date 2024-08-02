package com.verdantartifice.primalmagick.common.crafting;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.crafting.inputs.RunecarvingRecipeInput;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Definition for a runecarving recipe.  Like a stonecutting recipe, but has two ingredients and a
 * research requirement.
 * 
 * @author Daedalus4096
 */
public class RunecarvingRecipe extends AbstractStackCraftingRecipe<RunecarvingRecipeInput> implements IRunecarvingRecipe {
    protected final Optional<AbstractRequirement<?>> requirement;
    protected final Ingredient ingredient1;
    protected final Ingredient ingredient2;
    protected final Optional<Integer> baseExpertiseOverride;
    protected final Optional<Integer> bonusExpertiseOverride;
    protected final Optional<ResourceLocation> expertiseGroup;
    protected final Optional<ResearchDisciplineKey> disciplineOverride;

    public RunecarvingRecipe(String group, ItemStack result, Ingredient ingredient1, Ingredient ingredient2, Optional<AbstractRequirement<?>> requirement, Optional<Integer> baseExpertiseOverride,
            Optional<Integer> bonusExpertiseOverride, Optional<ResourceLocation> expertiseGroup, Optional<ResearchDisciplineKey> disciplineOverride) {
        super(group, result);
        this.requirement = requirement;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
        this.baseExpertiseOverride = baseExpertiseOverride;
        this.bonusExpertiseOverride = bonusExpertiseOverride;
        this.expertiseGroup = expertiseGroup;
        this.disciplineOverride = disciplineOverride;
    }

    @Override
    public boolean matches(RunecarvingRecipeInput inv, Level worldIn) {
        return this.ingredient1.test(inv.getItem(0)) && this.ingredient2.test(inv.getItem(1));
    }

    @Override
    public ItemStack assemble(RunecarvingRecipeInput inv, HolderLookup.Provider registries) {
        return this.getResultItem(registries).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return (width * height) >= 2;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.ingredient1, this.ingredient2);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.RUNECARVING.get();
    }

    @Override
    public Optional<AbstractRequirement<?>> getRequirement() {
        return this.requirement;
    }

    @Override
    public int getExpertiseReward(RegistryAccess registryAccess) {
        return this.baseExpertiseOverride.orElseGet(() -> {
            return this.getResearchTier(registryAccess).map(tier -> tier.getDefaultExpertise()).orElse(0);
        });
    }

    @Override
    public int getBonusExpertiseReward(RegistryAccess registryAccess) {
        return this.bonusExpertiseOverride.orElseGet(() -> {
            return this.getResearchTier(registryAccess).map(tier -> tier.getDefaultBonusExpertise()).orElse(0);
        });
    }

    @Override
    public Optional<ResourceLocation> getExpertiseGroup() {
        return this.expertiseGroup;
    }

    @Override
    public Optional<ResearchDisciplineKey> getResearchDisciplineOverride() {
        return this.disciplineOverride;
    }

    public static class Serializer implements RecipeSerializer<RunecarvingRecipe> {
        @Override
        public MapCodec<RunecarvingRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(rr -> rr.group),
                    ItemStack.CODEC.fieldOf("result").forGetter(rr -> rr.output),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient1").forGetter(rr -> rr.ingredient1),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient2").forGetter(rr -> rr.ingredient2),
                    AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(rr -> rr.requirement),
                    Codec.INT.optionalFieldOf("baseExpertiseOverride").forGetter(r -> r.baseExpertiseOverride),
                    Codec.INT.optionalFieldOf("bonusExpertiseOverride").forGetter(r -> r.bonusExpertiseOverride),
                    ResourceLocation.CODEC.optionalFieldOf("expertiseGroup").forGetter(r -> r.expertiseGroup),
                    ResearchDisciplineKey.CODEC.codec().optionalFieldOf("disciplineOverride").forGetter(r -> r.disciplineOverride)
                ).apply(instance, RunecarvingRecipe::new)
            );
        }
        
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RunecarvingRecipe> streamCodec() {
            return StreamCodecUtils.composite(
                    ByteBufCodecs.STRING_UTF8, r -> r.group,
                    ItemStack.STREAM_CODEC, r -> r.output,
                    Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredient1,
                    Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredient2,
                    ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()), r -> r.requirement,
                    ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), r -> r.baseExpertiseOverride,
                    ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), r -> r.bonusExpertiseOverride,
                    ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), r -> r.expertiseGroup,
                    ByteBufCodecs.optional(ResearchDisciplineKey.STREAM_CODEC), r -> r.disciplineOverride,
                    RunecarvingRecipe::new);
        }
    }
}

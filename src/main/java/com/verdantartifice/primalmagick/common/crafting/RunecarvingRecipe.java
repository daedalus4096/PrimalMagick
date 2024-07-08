package com.verdantartifice.primalmagick.common.crafting;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Definition for a runecarving recipe.  Like a stonecutting recipe, but has two ingredients and a
 * research requirement.
 * 
 * @author Daedalus4096
 */
public class RunecarvingRecipe extends AbstractStackCraftingRecipe<CraftingInput> implements IRunecarvingRecipe {
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
    public boolean matches(CraftingInput inv, Level worldIn) {
        return this.ingredient1.test(inv.getItem(0)) && this.ingredient2.test(inv.getItem(1));
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
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
        public Codec<RunecarvingRecipe> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(rr -> rr.group),
                    ItemStack.CODEC.fieldOf("result").forGetter(rr -> rr.output),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient1").forGetter(rr -> rr.ingredient1),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient2").forGetter(rr -> rr.ingredient2),
                    AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(rr -> rr.requirement),
                    Codec.INT.optionalFieldOf("baseExpertiseOverride").forGetter(r -> r.baseExpertiseOverride),
                    Codec.INT.optionalFieldOf("bonusExpertiseOverride").forGetter(r -> r.bonusExpertiseOverride),
                    ResourceLocation.CODEC.optionalFieldOf("expertiseGroup").forGetter(r -> r.expertiseGroup),
                    ResearchDisciplineKey.CODEC.optionalFieldOf("disciplineOverride").forGetter(r -> r.disciplineOverride)
                ).apply(instance, RunecarvingRecipe::new)
            );
        }
        
        @Override
        public RunecarvingRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Optional<AbstractRequirement<?>> requirement = buffer.readOptional(AbstractRequirement::fromNetwork);
            Ingredient ing1 = Ingredient.fromNetwork(buffer);
            Ingredient ing2 = Ingredient.fromNetwork(buffer);
            Optional<Integer> baseExpOverride = buffer.readOptional(b -> b.readVarInt());
            Optional<Integer> bonusExpOverride = buffer.readOptional(b -> b.readVarInt());
            Optional<ResourceLocation> expGroup = buffer.readOptional(b -> b.readResourceLocation());
            Optional<ResearchDisciplineKey> discOverride = buffer.readOptional(ResearchDisciplineKey::fromNetwork);
            ItemStack result = buffer.readItem();
            return new RunecarvingRecipe(group, result, ing1, ing2, requirement, baseExpOverride, bonusExpOverride, expGroup, discOverride);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RunecarvingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeOptional(recipe.requirement, (b, r) -> r.toNetwork(b));
            recipe.ingredient1.toNetwork(buffer);
            recipe.ingredient2.toNetwork(buffer);
            buffer.writeOptional(recipe.baseExpertiseOverride, (b, e) -> b.writeVarInt(e));
            buffer.writeOptional(recipe.bonusExpertiseOverride, (b, e) -> b.writeVarInt(e));
            buffer.writeOptional(recipe.expertiseGroup, (b, g) -> b.writeResourceLocation(g));
            buffer.writeOptional(recipe.disciplineOverride, (b, d) -> d.toNetwork(b));
            buffer.writeItem(recipe.output);
        }
    }
}

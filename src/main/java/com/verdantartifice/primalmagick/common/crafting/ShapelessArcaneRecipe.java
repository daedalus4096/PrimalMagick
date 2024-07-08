package com.verdantartifice.primalmagick.common.crafting;

import java.util.Optional;
import java.util.function.Predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Definition for a shapeless arcane recipe.  Like a vanilla shapeless recipe, but has research and optional mana requirements.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.item.crafting.ShapelessRecipe}
 */
public class ShapelessArcaneRecipe extends AbstractStackCraftingRecipe<CraftingInput> implements IShapelessArcaneRecipePM {
    protected final Optional<AbstractRequirement<?>> requirement;
    protected final SourceList manaCosts;
    protected final NonNullList<Ingredient> recipeItems;
    protected final boolean isSimple;
    protected final Optional<Integer> baseExpertiseOverride;
    protected final Optional<Integer> bonusExpertiseOverride;
    protected final Optional<ResourceLocation> expertiseGroup;
    protected final Optional<ResearchDisciplineKey> disciplineOverride;

    public ShapelessArcaneRecipe(String group, ItemStack output, NonNullList<Ingredient> items, Optional<AbstractRequirement<?>> requirement, SourceList manaCosts,
            Optional<Integer> baseExpertiseOverride, Optional<Integer> bonusExpertiseOverride, Optional<ResourceLocation> expertiseGroup, Optional<ResearchDisciplineKey> disciplineOverride) {
        super(group, output);
        this.requirement = requirement;
        this.manaCosts = manaCosts;
        this.recipeItems = items;
        this.isSimple = items.stream().allMatch(Ingredient::isSimple);
        this.baseExpertiseOverride = baseExpertiseOverride;
        this.bonusExpertiseOverride = bonusExpertiseOverride;
        this.expertiseGroup = expertiseGroup;
        this.disciplineOverride = disciplineOverride;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.ARCANE_CRAFTING_SHAPELESS.get();
    }

    @Override
    public Optional<AbstractRequirement<?>> getRequirement() {
        return this.requirement;
    }
    
    @Override
    public SourceList getManaCosts() {
        return this.manaCosts;
    }

    @Override
    public boolean isSimple() {
        return this.isSimple;
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

    public static class Serializer implements RecipeSerializer<ShapelessArcaneRecipe> {
        @Override
        public Codec<ShapelessArcaneRecipe> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(sar -> sar.group),
                    ItemStack.CODEC.fieldOf("result").forGetter(sar -> sar.output),
                    Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(ingredients -> {
                        Ingredient[] ingArray = ingredients.stream().filter(Predicate.not(Ingredient::isEmpty)).toArray(Ingredient[]::new);
                        if (ingArray.length == 0) {
                            return DataResult.error(() -> "No ingredients for shapeless arcane recipe");
                        } else if (ingArray.length > ShapedArcaneRecipe.MAX_WIDTH * ShapedArcaneRecipe.MAX_HEIGHT) {
                            return DataResult.error(() -> "Too many ingredients for shapeless arcane recipe");
                        } else {
                            return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingArray));
                        }
                    }, DataResult::success).forGetter(sar -> sar.recipeItems),
                    AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(sar -> sar.requirement),
                    SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(sar -> sar.manaCosts),
                    Codec.INT.optionalFieldOf("baseExpertiseOverride").forGetter(r -> r.baseExpertiseOverride),
                    Codec.INT.optionalFieldOf("bonusExpertiseOverride").forGetter(r -> r.bonusExpertiseOverride),
                    ResourceLocation.CODEC.optionalFieldOf("expertiseGroup").forGetter(r -> r.expertiseGroup),
                    ResearchDisciplineKey.CODEC.optionalFieldOf("disciplineOverride").forGetter(r -> r.disciplineOverride)
                ).apply(instance, ShapelessArcaneRecipe::new)
            );
        }
        
        @Override
        public ShapelessArcaneRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Optional<AbstractRequirement<?>> requirement = buffer.readOptional(AbstractRequirement::fromNetwork);
            
            SourceList manaCosts = SourceList.fromNetwork(buffer);
            
            int count = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(count, Ingredient.EMPTY);
            for (int index = 0; index < ingredients.size(); index++) {
                ingredients.set(index, Ingredient.fromNetwork(buffer));
            }
            
            Optional<Integer> baseExpOverride = buffer.readOptional(b -> b.readVarInt());
            Optional<Integer> bonusExpOverride = buffer.readOptional(b -> b.readVarInt());
            Optional<ResourceLocation> expGroup = buffer.readOptional(b -> b.readResourceLocation());
            Optional<ResearchDisciplineKey> discOverride = buffer.readOptional(ResearchDisciplineKey::fromNetwork);
            
            ItemStack result = buffer.readItem();
            return new ShapelessArcaneRecipe(group, result, ingredients, requirement, manaCosts, baseExpOverride, bonusExpOverride, expGroup, discOverride);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ShapelessArcaneRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeOptional(recipe.requirement, (b, r) -> r.toNetwork(b));
            SourceList.toNetwork(buffer, recipe.manaCosts);
            buffer.writeVarInt(recipe.recipeItems.size());
            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeOptional(recipe.baseExpertiseOverride, (b, e) -> b.writeVarInt(e));
            buffer.writeOptional(recipe.bonusExpertiseOverride, (b, e) -> b.writeVarInt(e));
            buffer.writeOptional(recipe.expertiseGroup, (b, g) -> b.writeResourceLocation(g));
            buffer.writeOptional(recipe.disciplineOverride, (b, d) -> d.toNetwork(b));
            buffer.writeItem(recipe.output);
        }
    }
}

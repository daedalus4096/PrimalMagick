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
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Definition for a ritual recipe.
 * 
 * @author Daedalus4096
 */
public class RitualRecipe extends AbstractStackCraftingRecipe<CraftingInput> implements IShapelessRecipePM<CraftingInput>, IRitualRecipe {
    public static final int MIN_INSTABILITY = 0;
    public static final int MAX_INSTABILITY = 10;
    
    protected final Optional<AbstractRequirement<?>> requirement;
    protected final SourceList manaCosts;
    protected final int instability;
    protected final NonNullList<Ingredient> recipeItems;
    protected final NonNullList<BlockIngredient> recipeProps;
    protected final boolean isSimple;
    protected final Optional<Integer> baseExpertiseOverride;
    protected final Optional<Integer> bonusExpertiseOverride;
    protected final Optional<ResourceLocation> expertiseGroup;
    protected final Optional<ResearchDisciplineKey> disciplineOverride;

    public RitualRecipe(String group, ItemStack output, NonNullList<Ingredient> items, NonNullList<BlockIngredient> props, Optional<AbstractRequirement<?>> requirement, SourceList manaCosts, int instability,
            Optional<Integer> baseExpertiseOverride, Optional<Integer> bonusExpertiseOverride, Optional<ResourceLocation> expertiseGroup, Optional<ResearchDisciplineKey> disciplineOverride) {
        super(group, output);
        this.requirement = requirement;
        this.manaCosts = manaCosts;
        this.instability = instability;
        this.recipeItems = items;
        this.recipeProps = props;
        this.isSimple = items.stream().allMatch(Ingredient::isSimple);
        this.baseExpertiseOverride = baseExpertiseOverride;
        this.bonusExpertiseOverride = bonusExpertiseOverride;
        this.expertiseGroup = expertiseGroup;
        this.disciplineOverride = disciplineOverride;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        // Ritual recipes aren't space-limited
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.RITUAL.get();
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
    public NonNullList<BlockIngredient> getProps() {
        return this.recipeProps;
    }
    
    @Override
    public int getInstability() {
        return this.instability;
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

    public static class Serializer implements RecipeSerializer<RitualRecipe> {
        @Override
        public Codec<RitualRecipe> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(rr -> rr.group),
                    ItemStack.CODEC.fieldOf("result").forGetter(rr -> rr.output),
                    Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(ingredients -> {
                        Ingredient[] ingArray = ingredients.stream().filter(Predicate.not(Ingredient::isEmpty)).toArray(Ingredient[]::new);
                        if (ingArray.length == 0) {
                            return DataResult.error(() -> "No offerings for ritual recipe");
                        } else {
                            return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingArray));
                        }
                    }, DataResult::success).forGetter(rr -> rr.recipeItems),
                    BlockIngredient.CODEC_NONEMPTY.listOf().fieldOf("props").flatXmap(ingredients -> {
                        BlockIngredient[] ingArray = ingredients.stream().filter(Predicate.not(BlockIngredient::isEmpty)).toArray(BlockIngredient[]::new);
                        if (ingArray.length == 0) {
                            return DataResult.error(() -> "No props for ritual recipe");
                        } else {
                            return DataResult.success(NonNullList.of(BlockIngredient.EMPTY, ingArray));
                        }
                    }, DataResult::success).forGetter(rr -> rr.recipeProps),
                    AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(rr -> rr.requirement),
                    SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(rr -> rr.manaCosts),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("instability").forGetter(rr -> rr.instability),
                    Codec.INT.optionalFieldOf("baseExpertiseOverride").forGetter(r -> r.baseExpertiseOverride),
                    Codec.INT.optionalFieldOf("bonusExpertiseOverride").forGetter(r -> r.bonusExpertiseOverride),
                    ResourceLocation.CODEC.optionalFieldOf("expertiseGroup").forGetter(r -> r.expertiseGroup),
                    ResearchDisciplineKey.CODEC.optionalFieldOf("disciplineOverride").forGetter(r -> r.disciplineOverride)
                ).apply(instance, RitualRecipe::new)
            );
        }
        
        @Override
        public RitualRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);
            Optional<AbstractRequirement<?>> requirement = buffer.readOptional(AbstractRequirement::fromNetwork);
            int instability = buffer.readVarInt();
            
            SourceList manaCosts = SourceList.fromNetwork(buffer);
            
            int ingredientCount = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);
            for (int index = 0; index < ingredients.size(); index++) {
                ingredients.set(index, Ingredient.fromNetwork(buffer));
            }
            
            int propCount = buffer.readVarInt();
            NonNullList<BlockIngredient> props = NonNullList.withSize(propCount, BlockIngredient.EMPTY);
            for (int index = 0; index < props.size(); index++) {
                props.set(index, BlockIngredient.read(buffer));
            }
            
            Optional<Integer> baseExpOverride = buffer.readOptional(b -> b.readVarInt());
            Optional<Integer> bonusExpOverride = buffer.readOptional(b -> b.readVarInt());
            Optional<ResourceLocation> expGroup = buffer.readOptional(b -> b.readResourceLocation());
            Optional<ResearchDisciplineKey> discOverride = buffer.readOptional(ResearchDisciplineKey::fromNetwork);
            
            ItemStack result = buffer.readItem();
            return new RitualRecipe(group, result, ingredients, props, requirement, manaCosts, instability, baseExpOverride, bonusExpOverride, expGroup, discOverride);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RitualRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeOptional(recipe.requirement, (b, r) -> r.toNetwork(b));
            buffer.writeVarInt(recipe.instability);
            SourceList.toNetwork(buffer, recipe.manaCosts);
            buffer.writeVarInt(recipe.recipeItems.size());
            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeVarInt(recipe.recipeProps.size());
            for (BlockIngredient prop : recipe.recipeProps) {
                prop.write(buffer);
            }
            buffer.writeOptional(recipe.baseExpertiseOverride, (b, e) -> b.writeVarInt(e));
            buffer.writeOptional(recipe.bonusExpertiseOverride, (b, e) -> b.writeVarInt(e));
            buffer.writeOptional(recipe.expertiseGroup, (b, g) -> b.writeResourceLocation(g));
            buffer.writeOptional(recipe.disciplineOverride, (b, d) -> d.toNetwork(b));
            buffer.writeItem(recipe.output);
        }
    }
}

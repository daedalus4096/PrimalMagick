package com.verdantartifice.primalmagick.common.crafting;

import java.util.Optional;
import java.util.function.Predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Definition for a ritual recipe.
 * 
 * @author Daedalus4096
 */
public class RitualRecipe extends AbstractStackCraftingRecipe<Container> implements IShapelessRecipePM<Container>, IRitualRecipe {
    public static final int MIN_INSTABILITY = 0;
    public static final int MAX_INSTABILITY = 10;
    
    protected final Optional<AbstractRequirement<?>> requirement;
    protected final SourceList manaCosts;
    protected final int instability;
    protected final NonNullList<Ingredient> recipeItems;
    protected final NonNullList<BlockIngredient> recipeProps;
    protected final boolean isSimple;

    public RitualRecipe(String group, ItemStack output, NonNullList<Ingredient> items, NonNullList<BlockIngredient> props, Optional<AbstractRequirement<?>> requirement, SourceList manaCosts, int instability) {
        super(group, output);
        this.requirement = requirement;
        this.manaCosts = manaCosts;
        this.instability = instability;
        this.recipeItems = items;
        this.recipeProps = props;
        this.isSimple = items.stream().allMatch(Ingredient::isSimple);
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

    public static class Serializer implements RecipeSerializer<RitualRecipe> {
        @Override
        public Codec<RitualRecipe> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                    ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(rr -> rr.group),
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
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("instability").forGetter(rr -> rr.instability)
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
            
            ItemStack result = buffer.readItem();
            return new RitualRecipe(group, result, ingredients, props, requirement, manaCosts, instability);
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
            buffer.writeItem(recipe.output);
        }
    }
}

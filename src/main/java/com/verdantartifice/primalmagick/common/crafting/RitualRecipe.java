package com.verdantartifice.primalmagick.common.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

/**
 * Definition for a ritual recipe.
 * 
 * @author Daedalus4096
 */
public class RitualRecipe implements IRitualRecipe {
    public static final int MIN_INSTABILITY = 0;
    public static final int MAX_INSTABILITY = 10;
    
    protected final String group;
    protected final CompoundResearchKey research;
    protected final SourceList manaCosts;
    protected final int instability;
    protected final ItemStack recipeOutput;
    protected final NonNullList<Ingredient> recipeItems;
    protected final NonNullList<BlockIngredient> recipeProps;
    protected final boolean isSimple;

    public RitualRecipe(String group, CompoundResearchKey research, SourceList manaCosts, int instability, ItemStack output, NonNullList<Ingredient> items, NonNullList<BlockIngredient> props) {
        this.group = group;
        this.research = research;
        this.manaCosts = manaCosts;
        this.instability = instability;
        this.recipeOutput = output;
        this.recipeItems = items;
        this.recipeProps = props;
        this.isSimple = items.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        // Ritual recipe matching only considers the ingredients, not the props
        StackedContents helper = new StackedContents();
        List<ItemStack> inputs = new ArrayList<>();
        int count = 0;
        
        for (int index = 0; index < inv.getContainerSize(); index++) {
            ItemStack stack = inv.getItem(index);
            if (!stack.isEmpty()) {
                count++;
                if (this.isSimple) {
                    helper.accountStack(stack, 1);
                } else {
                    inputs.add(stack);
                }
            }
        }
        
        return (count == this.recipeItems.size()) && (this.isSimple ? helper.canCraft(this, null) : RecipeMatcher.findMatches(inputs, this.recipeItems) != null);
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        return this.recipeOutput.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        // Ritual recipes aren't space-limited
        return true;
    }

    @Override
    public CraftingBookCategory category() {
        // Ritual recipes don't use the recipe book, so an accurate crafting book category isn't needed
        return CraftingBookCategory.MISC;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.recipeOutput;
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
    public CompoundResearchKey getRequiredResearch() {
        return this.research;
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
    
    public static class Serializer implements RecipeSerializer<RitualRecipe> {
        protected static final Codec<RitualRecipe> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                    ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(rr -> rr.group),
                    CompoundResearchKey.CODEC.fieldOf("research").forGetter(rr -> rr.research),
                    SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(rr -> rr.manaCosts),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("instability").forGetter(rr -> rr.instability),
                    ItemStack.CODEC.fieldOf("result").forGetter(rr -> rr.recipeOutput),
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
                    }, DataResult::success).forGetter(rr -> rr.recipeProps)
                ).apply(instance, RitualRecipe::new);
        });
        
        @Override
        public Codec<RitualRecipe> codec() {
            return CODEC;
        }

        @Override
        public RitualRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);
            CompoundResearchKey research = CompoundResearchKey.parse(buffer.readUtf(32767));
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
            return new RitualRecipe(group, research, manaCosts, instability, result, ingredients, props);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RitualRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeUtf(recipe.research.toString());
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
            buffer.writeItem(recipe.recipeOutput);
        }
    }
}

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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

/**
 * Definition for a shapeless arcane recipe.  Like a vanilla shapeless recipe, but has research and optional mana requirements.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.item.crafting.ShapelessRecipe}
 */
public class ShapelessArcaneRecipe implements IArcaneRecipe {
    protected final String group;
    protected final CompoundResearchKey research;
    protected final SourceList manaCosts;
    protected final ItemStack recipeOutput;
    protected final NonNullList<Ingredient> recipeItems;
    protected final boolean isSimple;
    
    public ShapelessArcaneRecipe(String group, CompoundResearchKey research, SourceList manaCosts, ItemStack output, NonNullList<Ingredient> items) {
        this.group = group;
        this.research = research;
        this.manaCosts = manaCosts;
        this.recipeOutput = output;
        this.recipeItems = items;
        this.isSimple = items.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
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
        return (width * height) >= this.recipeItems.size();
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
        return RecipeSerializersPM.ARCANE_CRAFTING_SHAPELESS.get();
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
    public String getGroup() {
        return this.group;
    }

    public static class Serializer implements RecipeSerializer<ShapelessArcaneRecipe> {
        protected static final Codec<ShapelessArcaneRecipe> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                    ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(sar -> sar.group),
                    CompoundResearchKey.CODEC.fieldOf("research").forGetter(sar -> sar.research),
                    SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(sar -> sar.manaCosts),
                    ItemStack.CODEC.fieldOf("result").forGetter(sar -> sar.recipeOutput),
                    Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(ingredients -> {
                        Ingredient[] ingArray = ingredients.stream().filter(Predicate.not(Ingredient::isEmpty)).toArray(Ingredient[]::new);
                        if (ingArray.length == 0) {
                            return DataResult.error(() -> "No ingredients for shapeless arcane recipe");
                        } else if (ingArray.length > ShapedArcaneRecipe.MAX_WIDTH * ShapedArcaneRecipe.MAX_HEIGHT) {
                            return DataResult.error(() -> "Too many ingredients for shapeless arcane recipe");
                        } else {
                            return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingArray));
                        }
                    }, DataResult::success).forGetter(sar -> sar.recipeItems)
                ).apply(instance, ShapelessArcaneRecipe::new);
        });
        
        @Override
        public Codec<ShapelessArcaneRecipe> codec() {
            return CODEC;
        }

        @Override
        public ShapelessArcaneRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);
            CompoundResearchKey research = CompoundResearchKey.parse(buffer.readUtf(32767));
            
            SourceList manaCosts = SourceList.fromNetwork(buffer);
            
            int count = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(count, Ingredient.EMPTY);
            for (int index = 0; index < ingredients.size(); index++) {
                ingredients.set(index, Ingredient.fromNetwork(buffer));
            }
            
            ItemStack result = buffer.readItem();
            return new ShapelessArcaneRecipe(group, research, manaCosts, result, ingredients);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ShapelessArcaneRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeUtf(recipe.research.toString());
            SourceList.toNetwork(buffer, recipe.manaCosts);
            buffer.writeVarInt(recipe.recipeItems.size());
            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItem(recipe.recipeOutput);
        }
    }
}

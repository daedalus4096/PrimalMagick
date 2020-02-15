package com.verdantartifice.primalmagic.common.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.util.JsonUtils;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * Definition for a shapeless arcane recipe.  Like a vanilla shapeless recipe, but has research and optional mana requirements.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.item.crafting.ShapelessRecipe}
 */
public class ShapelessArcaneRecipe implements IArcaneRecipe {
    protected final ResourceLocation id;
    protected final String group;
    protected final SimpleResearchKey research;
    protected final SourceList manaCosts;
    protected final ItemStack recipeOutput;
    protected final NonNullList<Ingredient> recipeItems;
    protected final boolean isSimple;
    
    public ShapelessArcaneRecipe(ResourceLocation id, String group, SimpleResearchKey research, SourceList manaCosts, ItemStack output, NonNullList<Ingredient> items) {
        this.id = id;
        this.group = group;
        this.research = research;
        this.manaCosts = manaCosts;
        this.recipeOutput = output;
        this.recipeItems = items;
        this.isSimple = items.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        RecipeItemHelper helper = new RecipeItemHelper();
        List<ItemStack> inputs = new ArrayList<>();
        int count = 0;
        
        for (int index = 0; index < inv.getSizeInventory(); index++) {
            ItemStack stack = inv.getStackInSlot(index);
            if (!stack.isEmpty()) {
                count++;
                if (this.isSimple) {
                    helper.func_221264_a(stack, 1);
                } else {
                    inputs.add(stack);
                }
            }
        }
        
        return (count == this.recipeItems.size()) && (this.isSimple ? helper.canCraft(this, null) : RecipeMatcher.findMatches(inputs, this.recipeItems) != null);
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        return this.recipeOutput.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return (width * height) >= this.recipeItems.size();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }
    
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.ARCANE_CRAFTING_SHAPELESS.get();
    }

    @Override
    public SimpleResearchKey getRequiredResearch() {
        return this.research;
    }
    
    @Override
    public SourceList getManaCosts() {
        return this.manaCosts;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapelessArcaneRecipe> {
        @Override
        public ShapelessArcaneRecipe read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            SimpleResearchKey research = SimpleResearchKey.parse(JSONUtils.getString(json, "research", ""));
            SourceList manaCosts = JsonUtils.toSourceList(JSONUtils.getJsonObject(json, "mana", new JsonObject()));
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            NonNullList<Ingredient> ingredients = this.readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless arcane recipe");
            } else if (ingredients.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless arcane recipe the max is 9");
            } else {
                return new ShapelessArcaneRecipe(recipeId, group, research, manaCosts, result, ingredients);
            }
        }

        protected NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
            NonNullList<Ingredient> retVal = NonNullList.create();
            for (int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.deserialize(jsonArray.get(i));
                if (!ingredient.hasNoMatchingItems()) {
                    retVal.add(ingredient);
                }
            }
            return retVal;
        }
        
        @Override
        public ShapelessArcaneRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readString(32767);
            SimpleResearchKey research = SimpleResearchKey.parse(buffer.readString(32767));
            
            SourceList manaCosts = new SourceList();
            for (int index = 0; index < Source.SORTED_SOURCES.size(); index++) {
                manaCosts.add(Source.SORTED_SOURCES.get(index), buffer.readVarInt());
            }
            
            int count = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(count, Ingredient.EMPTY);
            for (int index = 0; index < ingredients.size(); index++) {
                ingredients.set(index, Ingredient.read(buffer));
            }
            
            ItemStack result = buffer.readItemStack();
            return new ShapelessArcaneRecipe(recipeId, group, research, manaCosts, result, ingredients);
        }

        @Override
        public void write(PacketBuffer buffer, ShapelessArcaneRecipe recipe) {
            buffer.writeString(recipe.group);
            buffer.writeString(recipe.research.toString());
            for (Source source : Source.SORTED_SOURCES) {
                buffer.writeVarInt(recipe.manaCosts.getAmount(source));
            }
            buffer.writeVarInt(recipe.recipeItems.size());
            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.write(buffer);
            }
            buffer.writeItemStack(recipe.recipeOutput);
        }
    }
}

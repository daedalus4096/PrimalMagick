package com.verdantartifice.primalmagick.common.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;

/**
 * Definition for a runecarving recipe.  Like a stonecutting recipe, but has two ingredients and a
 * research requirement.
 * 
 * @author Daedalus4096
 */
public class RunecarvingRecipe implements IRunecarvingRecipe {
    protected final ResourceLocation id;
    protected final String group;
    protected final CompoundResearchKey research;
    protected final Ingredient ingredient1;
    protected final Ingredient ingredient2;
    protected final ItemStack result;
    
    public RunecarvingRecipe(ResourceLocation id, String group, CompoundResearchKey research, Ingredient ingredient1, Ingredient ingredient2, ItemStack result) {
        this.id = id;
        this.group = group;
        this.research = research;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
        this.result = result;
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return this.ingredient1.test(inv.getItem(0)) && this.ingredient2.test(inv.getItem(1));
    }

    @Override
    public ItemStack assemble(Container inv) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }
    
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient1);
        nonnulllist.add(this.ingredient2);
        return nonnulllist;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }
    
    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.RUNECARVING.get();
    }

    @Override
    public CompoundResearchKey getRequiredResearch() {
        return this.research;
    }

    public static class Serializer implements RecipeSerializer<RunecarvingRecipe> {
        @Override
        public RunecarvingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            CompoundResearchKey research = CompoundResearchKey.parse(GsonHelper.getAsString(json, "research", ""));
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
            Ingredient ing1 = GsonHelper.isArrayNode(json, "ingredient1") ?
                    Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredient1")) :
                    Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient1"));
            Ingredient ing2 = GsonHelper.isArrayNode(json, "ingredient2") ?
                    Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredient2")) :
                    Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient2"));
            if (ing1.isEmpty() && ing2.isEmpty()) {
                throw new JsonParseException("No ingredients for runecarving recipe");
            } else {
                return new RunecarvingRecipe(recipeId, group, research, ing1, ing2, result);
            }
        }

        @Override
        public RunecarvingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);
            CompoundResearchKey research = CompoundResearchKey.parse(buffer.readUtf(32767));
            Ingredient ing1 = Ingredient.fromNetwork(buffer);
            Ingredient ing2 = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new RunecarvingRecipe(recipeId, group, research, ing1, ing2, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RunecarvingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeUtf(recipe.research.toString());
            recipe.ingredient1.toNetwork(buffer);
            recipe.ingredient2.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}

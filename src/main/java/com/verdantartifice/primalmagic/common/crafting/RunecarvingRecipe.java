package com.verdantartifice.primalmagic.common.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * Definition for a runecarving recipe.  Like a stonecutting recipe, but has two ingredients and a
 * research requirement.
 * 
 * @author Daedalus4096
 */
public class RunecarvingRecipe implements IRunecarvingRecipe {
    protected final ResourceLocation id;
    protected final String group;
    protected final SimpleResearchKey research;
    protected final Ingredient ingredient1;
    protected final Ingredient ingredient2;
    protected final ItemStack result;
    
    public RunecarvingRecipe(ResourceLocation id, String group, SimpleResearchKey research, Ingredient ingredient1, Ingredient ingredient2, ItemStack result) {
        this.id = id;
        this.group = group;
        this.research = research;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
        this.result = result;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return this.ingredient1.test(inv.getStackInSlot(0)) && this.ingredient2.test(inv.getStackInSlot(1));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
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
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.RUNECARVING.get();
    }

    @Override
    public SimpleResearchKey getRequiredResearch() {
        return this.research;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RunecarvingRecipe> {
        @Override
        public RunecarvingRecipe read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            SimpleResearchKey research = SimpleResearchKey.parse(JSONUtils.getString(json, "research", ""));
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            Ingredient ing1 = JSONUtils.isJsonArray(json, "ingredient1") ?
                    Ingredient.deserialize(JSONUtils.getJsonArray(json, "ingredient1")) :
                    Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient1"));
            Ingredient ing2 = JSONUtils.isJsonArray(json, "ingredient2") ?
                    Ingredient.deserialize(JSONUtils.getJsonArray(json, "ingredient2")) :
                    Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient2"));
            if (ing1.hasNoMatchingItems() && ing2.hasNoMatchingItems()) {
                throw new JsonParseException("No ingredients for runecarving recipe");
            } else {
                return new RunecarvingRecipe(recipeId, group, research, ing1, ing2, result);
            }
        }

        @Override
        public RunecarvingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readString(32767);
            SimpleResearchKey research = SimpleResearchKey.parse(buffer.readString(32767));
            Ingredient ing1 = Ingredient.read(buffer);
            Ingredient ing2 = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();
            return new RunecarvingRecipe(recipeId, group, research, ing1, ing2, result);
        }

        @Override
        public void write(PacketBuffer buffer, RunecarvingRecipe recipe) {
            buffer.writeString(recipe.group);
            buffer.writeString(recipe.research.toString());
            recipe.ingredient1.write(buffer);
            recipe.ingredient2.write(buffer);
            buffer.writeItemStack(recipe.result);
        }
    }
}

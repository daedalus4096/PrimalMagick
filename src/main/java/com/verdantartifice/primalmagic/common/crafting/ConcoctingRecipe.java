package com.verdantartifice.primalmagic.common.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.util.JsonUtils;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * Definition for a concocting recipe.  Similar to a shapeless arcane recipe, but used by the concocter
 * instead of an arcane workbench.
 * 
 * @author Daedalus4096
 */
public class ConcoctingRecipe implements IConcoctingRecipe {
    protected final ResourceLocation id;
    protected final String group;
    protected final CompoundResearchKey research;
    protected final SourceList manaCosts;
    protected final ItemStack recipeOutput;
    protected final NonNullList<Ingredient> recipeItems;
    protected final boolean isSimple;

    public ConcoctingRecipe(ResourceLocation id, String group, CompoundResearchKey research, SourceList manaCosts, ItemStack output, NonNullList<Ingredient> items) {
        this.id = id;
        this.group = group;
        this.research = research;
        this.manaCosts = manaCosts;
        this.recipeOutput = output;
        this.recipeItems = items;
        this.isSimple = items.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
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
    public ItemStack assemble(Container inv) {
        return this.recipeOutput.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return (width * height) >= this.recipeItems.size();
    }

    @Override
    public ItemStack getResultItem() {
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
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.CONCOCTING.get();
    }

    @Override
    public SourceList getManaCosts() {
        return this.manaCosts;
    }

    @Override
    public CompoundResearchKey getRequiredResearch() {
        return this.research;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ConcoctingRecipe> {
        @Override
        public ConcoctingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            CompoundResearchKey research = CompoundResearchKey.parse(GsonHelper.getAsString(json, "research", ""));
            SourceList manaCosts = JsonUtils.toSourceList(GsonHelper.getAsJsonObject(json, "mana", new JsonObject()));
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
            NonNullList<Ingredient> ingredients = this.readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for concocting recipe");
            } else if (ingredients.size() > 9) {
                throw new JsonParseException("Too many ingredients for concocting recipe the max is 9");
            } else {
                return new ConcoctingRecipe(recipeId, group, research, manaCosts, result, ingredients);
            }
        }

        protected NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
            NonNullList<Ingredient> retVal = NonNullList.create();
            for (int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                if (!ingredient.isEmpty()) {
                    retVal.add(ingredient);
                }
            }
            return retVal;
        }
        
        @Override
        public ConcoctingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);
            CompoundResearchKey research = CompoundResearchKey.parse(buffer.readUtf(32767));
            
            SourceList manaCosts = new SourceList();
            for (int index = 0; index < Source.SORTED_SOURCES.size(); index++) {
                manaCosts.add(Source.SORTED_SOURCES.get(index), buffer.readVarInt());
            }
            
            int count = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(count, Ingredient.EMPTY);
            for (int index = 0; index < ingredients.size(); index++) {
                ingredients.set(index, Ingredient.fromNetwork(buffer));
            }
            
            ItemStack result = buffer.readItem();
            return new ConcoctingRecipe(recipeId, group, research, manaCosts, result, ingredients);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ConcoctingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeUtf(recipe.research.toString());
            for (Source source : Source.SORTED_SOURCES) {
                buffer.writeVarInt(recipe.manaCosts.getAmount(source));
            }
            buffer.writeVarInt(recipe.recipeItems.size());
            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItem(recipe.recipeOutput);
        }
    }
}

package com.verdantartifice.primalmagick.common.crafting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.JsonUtils;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * Definition for a shaped arcane recipe.  Like a vanilla shaped recipe, but has research and optional mana requirements.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.item.crafting.ShapedRecipe}
 */
public class ShapedArcaneRecipe implements IArcaneRecipe, IShapedRecipe<CraftingContainer> {
    protected final int recipeWidth;
    protected final int recipeHeight;
    protected final NonNullList<Ingredient> recipeItems;
    protected final ItemStack recipeOutput;
    protected final ResourceLocation id;
    protected final String group;
    protected final CompoundResearchKey research;
    protected final SourceList manaCosts;
    
    public ShapedArcaneRecipe(ResourceLocation id, String group, CompoundResearchKey research, SourceList manaCosts, int width, int height, NonNullList<Ingredient> items, ItemStack output) {
        this.id = id;
        this.group = group;
        this.research = research;
        this.manaCosts = manaCosts;
        this.recipeWidth = width;
        this.recipeHeight = height;
        this.recipeItems = items;
        this.recipeOutput = output;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        // Determine if the contents of the given crafting recipe match this recipe.  Shift the recipe window
        // around the available space until a match is found or the search space exhausted.  E.g. a 2x2 recipe
        // could be shoved up against any of the four corners of the 3x3 crafting grid.
        for (int i = 0; i <= inv.getWidth() - this.recipeWidth; i++) {
            for (int j = 0; j <= inv.getHeight() - this.recipeHeight; j++) {
                if (this.checkMatch(inv, i, j, true)) {
                    return true;
                }
                if (this.checkMatch(inv, i, j, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean checkMatch(CraftingContainer inv, int widthOffset, int heightOffset, boolean flag) {
        for (int i = 0; i < inv.getWidth(); i++) {
            for (int j = 0; j < inv.getHeight(); j++) {
                int k = i - widthOffset;
                int l = j - heightOffset;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight) {
                    if (flag) {
                        ingredient = this.recipeItems.get(this.recipeWidth - k - 1 + l * this.recipeWidth);
                    } else {
                        ingredient = this.recipeItems.get(k + l * this.recipeWidth);
                    }
                }
                if (!ingredient.test(inv.getItem(i + j * inv.getWidth()))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        return this.recipeOutput.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.recipeWidth && height >= this.recipeHeight;
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
        return RecipeSerializersPM.ARCANE_CRAFTING_SHAPED.get();
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
    public int getRecipeWidth() {
        return this.recipeWidth;
    }

    @Override
    public int getRecipeHeight() {
        return this.recipeHeight;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    protected static Map<String, Ingredient> deserializeKey(JsonObject jsonObject) {
        Map<String, Ingredient> map = new HashMap<>();
        for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            // Validate each key in the given JSON object
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }
            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }
            
            // Store the key and corresponding deserialized ingredient for return
            map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
        }
        
        // Include an entry for empty space in the recipe
        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    protected static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> keys, int width, int height) {
        NonNullList<Ingredient> list = NonNullList.withSize(width * height, Ingredient.EMPTY);
        Set<String> keySet = new HashSet<>(keys.keySet());
        keySet.remove(" ");
        
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[0].length(); j++) {
                // For each symbol in the pattern, add the corresponding ingredient from the key to the returned list
                String s = pattern[i].substring(j, j + 1);
                Ingredient ingredient = keys.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }
                keySet.remove(s);
                list.set(j + width * i, ingredient);
            }
        }
        
        // Make sure all ingredients from the key were used in the pattern
        if (!keySet.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + keySet);
        } else {
            return list;
        }
    }

    protected static String[] shrink(String... toShrink) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;
        
        for (int i1 = 0; i1 < toShrink.length; ++i1) {
            String s = toShrink[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }
                ++l;
            } else {
                l = 0;
            }
        }
        if (toShrink.length == l) {
            return new String[0];
        } else {
            String[] astring = new String[toShrink.length - l - k];
            for (int k1 = 0; k1 < astring.length; ++k1) {
               astring[k1] = toShrink[k1 + k].substring(i, j + 1);
            }
            return astring;
        }
    }

    @Override
    public boolean isIncomplete() {
        NonNullList<Ingredient> ingredients = this.getIngredients();
        return ingredients.isEmpty() || ingredients.stream().filter(i -> {
            return !i.isEmpty();
        }).anyMatch(i -> {
            return i.getItems().length == 0;
        });
    }

    private static int firstNonSpace(String str) {
        int i;
        for (i = 0; i < str.length() && str.charAt(i) == ' '; ++i) {}
        return i;
    }

    private static int lastNonSpace(String str) {
        int i;
        for (i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i) {}
        return i;
    }

    protected static String[] patternFromJson(JsonArray jsonArray) {
        String[] astring = new String[jsonArray.size()];
        
        // Validate the pattern strings in the given JSON array
        if (astring.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        } else if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for(int i = 0; i < astring.length; ++i) {
                String s = GsonHelper.convertToString(jsonArray.get(i), "pattern[" + i + "]");
                if (s.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                }
                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }
                astring[i] = s;
            }
            return astring;
        }
    }

    public static class Serializer implements RecipeSerializer<ShapedArcaneRecipe> {
        @Override
        public ShapedArcaneRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            CompoundResearchKey research = CompoundResearchKey.parse(GsonHelper.getAsString(json, "research", ""));
            SourceList manaCosts = JsonUtils.toSourceList(GsonHelper.getAsJsonObject(json, "mana", new JsonObject()));
            Map<String, Ingredient> map = ShapedArcaneRecipe.deserializeKey(GsonHelper.getAsJsonObject(json, "key"));
            String[] patternStrs = ShapedArcaneRecipe.shrink(ShapedArcaneRecipe.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
            int width = patternStrs[0].length();
            int height = patternStrs.length;
            NonNullList<Ingredient> ingredients = ShapedArcaneRecipe.deserializeIngredients(patternStrs, map, width, height);
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
            return new ShapedArcaneRecipe(recipeId, group, research, manaCosts, width, height, ingredients, result);
        }
        
        @Override
        public ShapedArcaneRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int width = buffer.readVarInt();
            int height = buffer.readVarInt();
            String group = buffer.readUtf(32767);
            CompoundResearchKey research = CompoundResearchKey.parse(buffer.readUtf(32767));
            SourceList manaCosts = new SourceList();
            for (int index = 0; index < Source.SORTED_SOURCES.size(); index++) {
                manaCosts.add(Source.SORTED_SOURCES.get(index), buffer.readVarInt());
            }
            NonNullList<Ingredient> list = NonNullList.withSize(width * height, Ingredient.EMPTY);
            for (int index = 0; index < list.size(); index++) {
                list.set(index, Ingredient.fromNetwork(buffer));
            }
            ItemStack stack = buffer.readItem();
            return new ShapedArcaneRecipe(recipeId, group, research, manaCosts, width, height, list, stack);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ShapedArcaneRecipe recipe) {
            buffer.writeVarInt(recipe.recipeWidth);
            buffer.writeVarInt(recipe.recipeHeight);
            buffer.writeUtf(recipe.group);
            buffer.writeUtf(recipe.research.toString());
            for (Source source : Source.SORTED_SOURCES) {
                buffer.writeVarInt(recipe.manaCosts.getAmount(source));
            }
            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItem(recipe.recipeOutput);
        }
    }
}

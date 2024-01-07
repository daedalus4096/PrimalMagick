package com.verdantartifice.primalmagick.common.crafting;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.CodecUtils;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * Definition for a shaped arcane recipe.  Like a vanilla shaped recipe, but has research and optional mana requirements.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.item.crafting.ShapedRecipe}
 */
public class ShapedArcaneRecipe implements IArcaneRecipe, IShapedRecipe<CraftingContainer> {
    public static final int MAX_WIDTH = 3;
    public static final int MAX_HEIGHT = 3;

    protected final int recipeWidth;
    protected final int recipeHeight;
    protected final NonNullList<Ingredient> recipeItems;
    protected final ItemStack recipeOutput;
    protected final String group;
    protected final CompoundResearchKey research;
    protected final SourceList manaCosts;
    
    public ShapedArcaneRecipe(String group, CompoundResearchKey research, SourceList manaCosts, int width, int height, NonNullList<Ingredient> items, ItemStack output) {
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
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        return this.recipeOutput.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.recipeWidth && height >= this.recipeHeight;
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

    protected static String[] shrink(List<String> toShrink) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;
        
        for (int i1 = 0; i1 < toShrink.size(); ++i1) {
            String s = toShrink.get(i1);
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
        if (toShrink.size() == l) {
            return new String[0];
        } else {
            String[] astring = new String[toShrink.size() - l - k];
            for (int k1 = 0; k1 < astring.length; ++k1) {
               astring[k1] = toShrink.get(k1 + k).substring(i, j + 1);
            }
            return astring;
        }
    }

    @Override
    public boolean isIncomplete() {
        NonNullList<Ingredient> ingredients = this.getIngredients();
        return ingredients.isEmpty() || ingredients.stream().filter(i -> {
            return !i.isEmpty() && i.getItems() != null;
        }).anyMatch(i -> {
            return ForgeHooks.hasNoElements(i);
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

    public static class Serializer implements RecipeSerializer<ShapedArcaneRecipe> {
        protected static final Codec<List<String>> PATTERN_CODEC = Codec.STRING.listOf().flatXmap(list -> {
            if (list.size() > MAX_HEIGHT) {
                return DataResult.error(() -> "Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
            } else if (list.isEmpty()) {
                return DataResult.error(() -> "Invalid pattern: empty pattern not allowed");
            } else {
                int strLength = list.get(0).length();
                for (String str : list) {
                    if (str.length() > MAX_WIDTH) {
                        return DataResult.error(() -> "Invalid pattern: too many columns, " + MAX_WIDTH  + " is maximum");
                    }
                    if (strLength != str.length()) {
                        return DataResult.error(() -> "Invalid pattern: each row must be the same width");
                    }
                }
                return DataResult.success(list);
            }
        }, DataResult::success);
        
        protected static final Codec<ShapedArcaneRecipe> CODEC = ShapedArcaneRecipe.Serializer.RawShapedArcaneRecipe.CODEC.flatXmap(rsar -> {
            String[] patternArray = ShapedArcaneRecipe.shrink(rsar.pattern);
            int width = patternArray[0].length();
            int height = patternArray.length;
            NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);
            Set<String> symbols = new HashSet<>(rsar.key.keySet());
            
            for (int i = 0; i < height; i++) {
                String line = patternArray[i];
                for (int j = 0; j < line.length(); j++) {
                    String symbol = line.substring(j, j + 1);
                    Ingredient ing = " ".equals(symbol) ? Ingredient.EMPTY : rsar.key.get(symbol);
                    if (ing == null) {
                        return DataResult.error(() -> "Pattern references symbol '" + symbol + "' but it's not defined in the key");
                    }
                    symbols.remove(symbol);
                    ingredients.set(j + width * i, ing);
                }
            }
            
            if (!symbols.isEmpty()) {
                return DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + symbols.toString());
            } else {
                ShapedArcaneRecipe recipe = new ShapedArcaneRecipe(rsar.group, rsar.research, rsar.manaCosts, width, height, ingredients, rsar.result);
                return DataResult.success(recipe);
            }
        }, sr -> {
            throw new NotImplementedException("Serializing ShapedArcaneRecipe is not implemented yet.");
        });
        
        @Override
        public Codec<ShapedArcaneRecipe> codec() {
            return CODEC;
        }

        @Override
        public ShapedArcaneRecipe fromNetwork(FriendlyByteBuf buffer) {
            int width = buffer.readVarInt();
            int height = buffer.readVarInt();
            String group = buffer.readUtf(32767);
            CompoundResearchKey research = CompoundResearchKey.parse(buffer.readUtf(32767));
            SourceList manaCosts = SourceList.fromNetwork(buffer);
            NonNullList<Ingredient> list = NonNullList.withSize(width * height, Ingredient.EMPTY);
            for (int index = 0; index < list.size(); index++) {
                list.set(index, Ingredient.fromNetwork(buffer));
            }
            ItemStack stack = buffer.readItem();
            return new ShapedArcaneRecipe(group, research, manaCosts, width, height, list, stack);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ShapedArcaneRecipe recipe) {
            buffer.writeVarInt(recipe.recipeWidth);
            buffer.writeVarInt(recipe.recipeHeight);
            buffer.writeUtf(recipe.group);
            buffer.writeUtf(recipe.research.toString());
            SourceList.toNetwork(buffer, recipe.manaCosts);
            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItem(recipe.recipeOutput);
        }
        
        protected static record RawShapedArcaneRecipe(String group, Map<String, Ingredient> key, List<String> pattern, ItemStack result, CompoundResearchKey research, SourceList manaCosts) {
            public static final Codec<ShapedArcaneRecipe.Serializer.RawShapedArcaneRecipe> CODEC = RecordCodecBuilder.create(instance -> {
                return instance.group(
                        ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(rsar -> rsar.group),
                        ExtraCodecs.strictUnboundedMap(CodecUtils.SINGLE_CHARACTER_STRING_CODEC, Ingredient.CODEC_NONEMPTY).fieldOf("key").forGetter(rsar -> rsar.key),
                        ShapedArcaneRecipe.Serializer.PATTERN_CODEC.fieldOf("pattern").forGetter(rsar -> rsar.pattern),
                        ItemStack.CODEC.fieldOf("result").forGetter(rsar -> rsar.result),
                        CompoundResearchKey.CODEC.fieldOf("research").forGetter(rsar -> rsar.research),
                        SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(rsar -> rsar.manaCosts)
                    ).apply(instance, RawShapedArcaneRecipe::new);
            });
        }
    }
}

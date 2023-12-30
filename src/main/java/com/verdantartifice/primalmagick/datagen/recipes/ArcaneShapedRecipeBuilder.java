package com.verdantartifice.primalmagick.datagen.recipes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.json.stream.JsonGenerationException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.Util;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Definition of a recipe data file builder for shaped arcane recipes.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.data.ShapedRecipeBuilder}
 */
public class ArcaneShapedRecipeBuilder {
    protected final ItemStack result;
    protected final List<String> pattern = new ArrayList<>();
    protected final Map<Character, Ingredient> key = new LinkedHashMap<>();
    protected String group;
    protected CompoundResearchKey research;
    protected SourceList manaCosts;
    
    protected ArcaneShapedRecipeBuilder(ItemLike result, int count) {
        this.result = new ItemStack(result, count);
    }
    
    /**
     * Creates a new builder for a shaped arcane recipe.
     * 
     * @param result the output item type
     * @param count the output item quantity
     * @return a new builder for a shaped arcane recipe
     */
    public static ArcaneShapedRecipeBuilder arcaneShapedRecipe(ItemLike result, int count) {
        return new ArcaneShapedRecipeBuilder(result, count);
    }
    
    /**
     * Creates a new builder for a shaped arcane recipe.
     * 
     * @param result the output item type
     * @return a new builder for a shaped arcane recipe
     */
    public static ArcaneShapedRecipeBuilder arcaneShapedRecipe(ItemLike result) {
        return arcaneShapedRecipe(result, 1);
    }
    
    /**
     * Adds a key to the recipe pattern.
     * 
     * @param symbol the symbol to use in the key
     * @param ingredient the ingredient to use for the given symbol
     * @return the modified builder
     */
    public ArcaneShapedRecipeBuilder key(Character symbol, Ingredient ingredient) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        } else if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(symbol, ingredient);
            return this;
        }
    }
    
    /**
     * Adds a key to the recipe pattern.
     * 
     * @param symbol the symbol to use in the key
     * @param item the item to use for the given symbol
     * @return the modified builder
     */
    public ArcaneShapedRecipeBuilder key(Character symbol, ItemLike item) {
        return key(symbol, Ingredient.of(item));
    }
    
    /**
     * Adds a key to the recipe pattern.
     * 
     * @param symbol the symbol to use in the key
     * @param tag the item tag to use for the given symbol
     * @return the modified builder
     */
    public ArcaneShapedRecipeBuilder key(Character symbol, TagKey<Item> tag) {
        return key(symbol, Ingredient.of(tag));
    }
    
    /**
     * Adds a new entry to the patterns for this recipe.
     * 
     * @param pattern the pattern line to add
     * @return the modified builder
     */
    public ArcaneShapedRecipeBuilder patternLine(String pattern) {
        if (!this.pattern.isEmpty() && pattern.length() != this.pattern.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.pattern.add(pattern);
            return this;
        }
    }
    
    /**
     * Adds a group to this recipe.
     * 
     * @param group the group to add
     * @return the modified builder
     */
    public ArcaneShapedRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }
    
    /**
     * Adds a research requirement to this recipe.
     * 
     * @param research the research requirement to add
     * @return the modified builder
     */
    public ArcaneShapedRecipeBuilder research(CompoundResearchKey research) {
        this.research = research.copy();
        return this;
    }
    
    /**
     * Adds a research requirement to this recipe.  Throws if the optional is empty.
     * 
     * @param researchOpt the research requirement to add
     * @return the modified builder
     */
    public ArcaneShapedRecipeBuilder research(Optional<CompoundResearchKey> researchOpt) {
        return this.research(researchOpt.orElseThrow());
    }
    
    /**
     * Adds a mana cost to this recipe.
     * 
     * @param mana the mana cost to add
     * @return the modified builder
     */
    public ArcaneShapedRecipeBuilder manaCost(SourceList mana) {
        this.manaCosts = mana.copy();
        return this;
    }
    
    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     * 
     * @param output a consumer for the finished recipe
     * @param id the ID of the finished recipe
     */
    public void build(RecipeOutput output, ResourceLocation id) {
        this.validate(id);
        output.accept(new ArcaneShapedRecipeBuilder.Result(id, this.result, this.group == null ? "" : this.group, this.pattern, this.key, this.research, this.manaCosts));
    }
    
    /**
     * Builds this recipe into an {@link IFinishedRecipe}. Use {@link #build(RecipeOutput)} if save is the same as the ID for
     * the result.
     * 
     * @param output a consumer for the finished recipe
     * @param save custom ID for the finished recipe
     */
    public void build(RecipeOutput output, String save) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result.getItem());
        ResourceLocation saveLoc = new ResourceLocation(save);
        if (saveLoc.equals(id)) {
            throw new IllegalStateException("Arcane Shaped Recipe " + save + " should remove its 'save' argument");
        } else {
            this.build(output, saveLoc);
        }
    }
    
    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     * 
     * @param output a consumer for the finished recipe
     */
    public void build(RecipeOutput output) {
        this.build(output, ForgeRegistries.ITEMS.getKey(this.result.getItem()));
    }

    /**
     * Makes sure that this recipe is valid.
     * 
     * @param id the ID of the recipe
     */
    protected void validate(ResourceLocation id) {
        if (this.pattern.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for arcane shaped recipe " + id + "!");
        }
        if (this.research == null) {
            throw new IllegalStateException("No research is defined for arcane shaped recipe " + id + "!");
        }
        
        Set<Character> set = new HashSet<>(this.key.keySet());
        set.remove(' ');
        for (String patternStr : this.pattern) {
            for (int index = 0; index < patternStr.length(); index++) {
                char c = patternStr.charAt(index);
                if (!this.key.containsKey(c) && c != ' ') {
                    throw new IllegalStateException("Pattern in recipe " + id + " uses undefined symbol '" + c + "'");
                }
                set.remove(c);
            }
        }
        
        if (!set.isEmpty()) {
            throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + id);
        } else if (this.pattern.size() == 1 && this.pattern.get(0).length() == 1) {
            throw new IllegalStateException("Arcane shaped recipe " + id + " only takes in a single item - should it be a shapeless recipe instead?");
        }
    }
    
    public static record Result(ResourceLocation id, ItemStack result, String group, List<String> pattern, Map<Character, Ingredient> key, CompoundResearchKey research, SourceList manaCosts) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            if (this.group != null && !this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            if (this.research != null) {
                json.addProperty("research", this.research.toString());
            }
            
            if (this.manaCosts != null && !this.manaCosts.isEmpty()) {
                json.add("mana", Util.getOrThrow(SourceList.CODEC.encodeStart(JsonOps.INSTANCE, this.manaCosts), JsonGenerationException::new));
            }
            
            JsonArray patternJson = new JsonArray();
            for (String str : this.pattern) {
                patternJson.add(str);
            }
            json.add("pattern", patternJson);
            
            JsonObject keyJson = new JsonObject();
            for (Entry<Character, Ingredient> entry : this.key.entrySet()) {
                keyJson.add(String.valueOf(entry.getKey()), entry.getValue().toJson(true));
            }
            json.add("key", keyJson);
            
            json.add("result", Util.getOrThrow(ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, this.result), JsonGenerationException::new));
        }

        @Override
        public RecipeSerializer<?> type() {
            return RecipeSerializersPM.ARCANE_CRAFTING_SHAPED.get();
        }

        @Override
        public AdvancementHolder advancement() {
            // Arcane recipes don't use the vanilla advancement unlock system, so return null
            return null;
        }
    }
}

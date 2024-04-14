package com.verdantartifice.primalmagick.datagen.recipes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.crafting.RecipeSerializersPM;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

/**
 * Definition of a recipe data file builder for shapeless tag recipes.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.data.ShapelessRecipeBuilder}
 */
public class ShapelessTagRecipeBuilder {
    protected final RecipeCategory category;
    protected final TagKey<Item> resultTag;
    protected final int resultAmount;
    protected final List<Ingredient> ingredients = new ArrayList<>();
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    protected String group;

    protected ShapelessTagRecipeBuilder(RecipeCategory category, TagKey<Item> resultTag, int resultAmount) {
        this.category = category;
        this.resultTag = resultTag;
        this.resultAmount = resultAmount;
    }
    
    public static ShapelessTagRecipeBuilder shapelessTagRecipe(RecipeCategory category, TagKey<Item> resultTag, int resultAmount) {
        return new ShapelessTagRecipeBuilder(category, resultTag, resultAmount);
    }
    
    public static ShapelessTagRecipeBuilder shapelessTagRecipe(RecipeCategory category, TagKey<Item> resultTag) {
        return shapelessTagRecipe(category, resultTag, 1);
    }
    
    /**
     * Add an ingredient to the recipe multiple times.
     * 
     * @param ingredient the ingredient to be added
     * @param quantity the number of the ingredient to add
     * @return the modified builder
     */
    public ShapelessTagRecipeBuilder addIngredient(Ingredient ingredient, int quantity) {
        for (int index = 0; index < quantity; index++) {
            this.ingredients.add(ingredient);
        }
        return this;
    }
    
    /**
     * Add an ingredient to the recipe.
     * 
     * @param ingredient the ingredient to be added
     * @return the modified builder
     */
    public ShapelessTagRecipeBuilder addIngredient(Ingredient ingredient) {
        return this.addIngredient(ingredient, 1);
    }
    
    /**
     * Add an ingredient of the given item to the recipe multiple times.
     * 
     * @param item the item to be added
     * @param quantity the number of the item to add
     * @return the modified builder
     */
    public ShapelessTagRecipeBuilder addIngredient(ItemLike item, int quantity) {
        return this.addIngredient(Ingredient.of(item), quantity);
    }
    
    /**
     * Add an ingredient of the given item to the recipe.
     * 
     * @param item the item to be added
     * @return the modified builder
     */
    public ShapelessTagRecipeBuilder addIngredient(ItemLike item) {
        return this.addIngredient(item, 1);
    }
    
    /**
     * Add an ingredient to the recipe that can be any item in the given tag.
     * 
     * @param tag the tag of items to be added
     * @return the modified builder
     */
    public ShapelessTagRecipeBuilder addIngredient(TagKey<Item> tag) {
        return this.addIngredient(Ingredient.of(tag));
    }
    
    /**
     * Adds a group to this recipe.
     * 
     * @param group the group to add
     * @return the modified builder
     */
    public ShapelessTagRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    public ShapelessTagRecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
        this.criteria.put(pName, pCriterion);
        return this;
    }
    
    protected static CraftingBookCategory determineBookCategory(RecipeCategory category) {
        return switch (category) {
            case BUILDING_BLOCKS -> CraftingBookCategory.BUILDING;
            case TOOLS, COMBAT -> CraftingBookCategory.EQUIPMENT;
            case REDSTONE -> CraftingBookCategory.REDSTONE;
            default -> CraftingBookCategory.MISC;
        };
    }
    
    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     * 
     * @param output a consumer for the finished recipe
     * @param id the ID of the finished recipe
     */
    public void build(RecipeOutput output, ResourceLocation id) {
        this.validate(id);
        Advancement.Builder advancementBuilder = output.advancement().addCriterion("has_the_recipe", 
                RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancementBuilder::addCriterion);
        output.accept(new ShapelessTagRecipeBuilder.Result(id, this.resultTag, this.resultAmount, this.group == null ? "" : this.group, determineBookCategory(this.category), this.ingredients, 
                advancementBuilder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/"))));
    }

    /**
     * Makes sure that this recipe is valid.
     * 
     * @param id the ID of the recipe
     */
    protected void validate(ResourceLocation id) {
        if (this.resultTag == null) {
            throw new IllegalStateException("No result tag defined for shapeless tag recipe " + id + "!");
        }
        if (this.resultAmount <= 0) {
            throw new IllegalStateException("Invalid result amount " + this.resultAmount + " specified for shapeless tag recipe " + id + "!");
        }
        if (this.ingredients.isEmpty()) {
            throw new IllegalStateException("No ingredients defined for shapeless tag recipe " + id + "!");
        }
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }
    
    public static record Result(ResourceLocation id, TagKey<Item> resultTag, int resultAmount, String group, CraftingBookCategory category, List<Ingredient> ingredients, AdvancementHolder advancement) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            if (this.group != null && !this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            json.addProperty("category", this.category.getSerializedName());
            
            JsonArray ingredientsJson = new JsonArray();
            for (Ingredient ingredient : this.ingredients) {
                ingredientsJson.add(ingredient.toJson(true));
            }
            json.add("ingredients", ingredientsJson);
            
            json.addProperty("recipeOutputTag", this.resultTag.location().toString());
            json.addProperty("recipeOutputAmount", this.resultAmount);
        }

        @Override
        public RecipeSerializer<?> type() {
            return RecipeSerializersPM.CRAFTING_SHAPELESS_TAG.get();
        }

        @Override
        public AdvancementHolder advancement() {
            return this.advancement;
        }
    }
}

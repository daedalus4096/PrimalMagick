package com.verdantartifice.primalmagick.datagen.recipes;

import com.verdantartifice.primalmagick.common.crafting.ShapelessTagRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Definition of a recipe data file builder for shapeless tag recipes.
 * 
 * @author Daedalus4096
 */
public class ShapelessTagRecipeBuilder {
    protected final HolderGetter<Item> itemGetter;
    protected final RecipeCategory category;
    protected final TagKey<Item> resultTag;
    protected final int resultAmount;
    protected final NonNullList<Ingredient> ingredients = NonNullList.create();
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    protected String group;
    protected boolean showNotification = true;

    protected ShapelessTagRecipeBuilder(HolderGetter<Item> itemGetter, RecipeCategory category, TagKey<Item> resultTag, int resultAmount) {
        this.itemGetter = itemGetter;
        this.category = category;
        this.resultTag = resultTag;
        this.resultAmount = resultAmount;
    }
    
    public static ShapelessTagRecipeBuilder shapelessTagRecipe(HolderGetter<Item> itemGetter, RecipeCategory category, TagKey<Item> resultTag, int resultAmount) {
        return new ShapelessTagRecipeBuilder(itemGetter, category, resultTag, resultAmount);
    }
    
    public static ShapelessTagRecipeBuilder shapelessTagRecipe(HolderGetter<Item> itemGetter, RecipeCategory category, TagKey<Item> resultTag) {
        return shapelessTagRecipe(itemGetter, category, resultTag, 1);
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
        return this.addIngredient(Ingredient.of(this.itemGetter.getOrThrow(tag)));
    }

    public ShapelessTagRecipeBuilder showNotification(boolean show) {
        this.showNotification = show;
        return this;
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

    @NotNull
    protected static CraftingBookCategory determineBookCategory(RecipeCategory category) {
        return category == null ? CraftingBookCategory.MISC : switch (category) {
            case BUILDING_BLOCKS -> CraftingBookCategory.BUILDING;
            case TOOLS, COMBAT -> CraftingBookCategory.EQUIPMENT;
            case REDSTONE -> CraftingBookCategory.REDSTONE;
            default -> CraftingBookCategory.MISC;
        };
    }
    
    /**
     * Builds this recipe into a finished recipe.
     * 
     * @param output a consumer for the finished recipe
     * @param id the ID of the finished recipe
     */
    public void build(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        this.validate(id);
        Advancement.Builder advancementBuilder = output.advancement().addCriterion("has_the_recipe", 
                RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancementBuilder::addCriterion);
        ShapelessTagRecipe recipe = new ShapelessTagRecipe(
                new Recipe.CommonInfo(this.showNotification),
                new CraftingRecipe.CraftingBookInfo(
                        determineBookCategory(this.category),
                        Objects.requireNonNullElse(this.group, "")
                ),
                this.resultTag,
                this.resultAmount,
                this.ingredients);
        output.accept(id, recipe, advancementBuilder.build(id.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    public void build(RecipeOutput output, Identifier id) {
        this.build(output, ResourceKey.create(Registries.RECIPE, id));
    }

    /**
     * Makes sure that this recipe is valid.
     * 
     * @param id the ID of the recipe
     */
    protected void validate(ResourceKey<Recipe<?>> id) {
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
}

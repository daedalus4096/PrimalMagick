package com.verdantartifice.primalmagick.datagen.recipes;

import java.util.Objects;
import java.util.Optional;

import com.verdantartifice.primalmagick.common.crafting.ShapelessArcaneTagRecipe;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

/**
 * Definition of a recipe data file builder for shapeless arcane tag recipes.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.data.ShapelessRecipeBuilder}
 */
public class ArcaneShapelessTagRecipeBuilder {
    protected final TagKey<Item> resultTag;
    protected final int resultAmount;
    protected final NonNullList<Ingredient> ingredients = NonNullList.create();
    protected String group;
    protected CompoundResearchKey research;
    protected SourceList manaCosts;

    protected ArcaneShapelessTagRecipeBuilder(TagKey<Item> result, int count) {
        this.resultTag = result;
        this.resultAmount = count;
    }
    
    /**
     * Creates a new builder for a shapeless arcane recipe.
     * 
     * @param result the output item type
     * @param count the output item quantity
     * @return a new builder for a shapeless arcane recipe
     */
    public static ArcaneShapelessTagRecipeBuilder arcaneShapelessTagRecipe(TagKey<Item> result, int count) {
        return new ArcaneShapelessTagRecipeBuilder(result, count);
    }
    
    /**
     * Creates a new builder for a shapeless arcane recipe.
     * 
     * @param result the output item type
     * @return a new builder for a shapeless arcane recipe
     */
    public static ArcaneShapelessTagRecipeBuilder arcaneShapelessTagRecipe(TagKey<Item> result) {
        return arcaneShapelessTagRecipe(result, 1);
    }
    
    /**
     * Add an ingredient to the recipe multiple times.
     * 
     * @param ingredient the ingredient to be added
     * @param quantity the number of the ingredient to add
     * @return the modified builder
     */
    public ArcaneShapelessTagRecipeBuilder addIngredient(Ingredient ingredient, int quantity) {
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
    public ArcaneShapelessTagRecipeBuilder addIngredient(Ingredient ingredient) {
        return this.addIngredient(ingredient, 1);
    }
    
    /**
     * Add an ingredient of the given item to the recipe multiple times.
     * 
     * @param item the item to be added
     * @param quantity the number of the item to add
     * @return the modified builder
     */
    public ArcaneShapelessTagRecipeBuilder addIngredient(ItemLike item, int quantity) {
        return this.addIngredient(Ingredient.of(item), quantity);
    }
    
    /**
     * Add an ingredient of the given item to the recipe.
     * 
     * @param item the item to be added
     * @return the modified builder
     */
    public ArcaneShapelessTagRecipeBuilder addIngredient(ItemLike item) {
        return this.addIngredient(item, 1);
    }
    
    /**
     * Add an ingredient to the recipe that can be any item in the given tag.
     * 
     * @param tag the tag of items to be added
     * @return the modified builder
     */
    public ArcaneShapelessTagRecipeBuilder addIngredient(TagKey<Item> tag) {
        return this.addIngredient(Ingredient.of(tag));
    }
    
    /**
     * Adds a group to this recipe.
     * 
     * @param group the group to add
     * @return the modified builder
     */
    public ArcaneShapelessTagRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }
    
    /**
     * Adds a research requirement to this recipe.
     * 
     * @param research the research requirement to add
     * @return the modified builder
     */
    public ArcaneShapelessTagRecipeBuilder research(CompoundResearchKey research) {
        this.research = research.copy();
        return this;
    }
    
    /**
     * Adds a research requirement to this recipe.  Throws if the optional is empty.
     * 
     * @param researchOpt the research requirement to add
     * @return the modified builder
     */
    public ArcaneShapelessTagRecipeBuilder research(Optional<CompoundResearchKey> researchOpt) {
        return this.research(researchOpt.orElseThrow());
    }
    
    /**
     * Adds a mana cost to this recipe.
     * 
     * @param mana the mana cost to add
     * @return the modified builder
     */
    public ArcaneShapelessTagRecipeBuilder manaCost(SourceList mana) {
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
        ShapelessArcaneTagRecipe recipe = new ShapelessArcaneTagRecipe(Objects.requireNonNullElse(this.group, ""), this.resultTag, this.resultAmount, this.ingredients, this.research, Objects.requireNonNullElse(this.manaCosts, SourceList.EMPTY));
        output.accept(id, recipe, null);
    }

    /**
     * Makes sure that this recipe is valid.
     * 
     * @param id the ID of the recipe
     */
    protected void validate(ResourceLocation id) {
        if (this.ingredients.isEmpty()) {
            throw new IllegalStateException("No ingredients defined for arcane shapeless tag recipe " + id + "!");
        }
        if (this.research == null) {
            throw new IllegalStateException("No research is defined for arcane shapeless tag recipe " + id + "!");
        }
    }
}

package com.verdantartifice.primalmagick.datagen.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.crafting.ShapelessArcaneTagRecipe;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
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
    protected final List<AbstractRequirement<?>> requirements = new ArrayList<>();
    protected SourceList manaCosts;
    protected Optional<Integer> baseExpertiseOverride = Optional.empty();
    protected Optional<Integer> bonusExpertiseOverride = Optional.empty();
    protected Optional<ResourceLocation> expertiseGroup = Optional.empty();

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
    
    public ArcaneShapelessTagRecipeBuilder requirement(AbstractRequirement<?> requirement) {
        this.requirements.add(requirement);
        return this;
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
    
    public ArcaneShapelessTagRecipeBuilder expertise(int baseValue, int bonusValue) {
        this.baseExpertiseOverride = Optional.of(baseValue);
        this.bonusExpertiseOverride = Optional.of(bonusValue);
        return this;
    }
    
    public ArcaneShapelessTagRecipeBuilder noExpertise() {
        return this.expertise(0, 0);
    }
    
    public ArcaneShapelessTagRecipeBuilder expertiseTier(ResearchTier tier) {
        return this.expertise(tier.getDefaultExpertise(), tier.getDefaultBonusExpertise());
    }
    
    public ArcaneShapelessTagRecipeBuilder expertiseGroup(ResourceLocation groupLoc) {
        this.expertiseGroup = Optional.ofNullable(groupLoc);
        return this;
    }
    
    public ArcaneShapelessTagRecipeBuilder expertiseGroup(String groupName) {
        return this.expertiseGroup(PrimalMagick.resource(groupName));
    }
    
    protected Optional<AbstractRequirement<?>> getFinalRequirement() {
        if (this.requirements.isEmpty()) {
            return Optional.empty();
        } else if (this.requirements.size() == 1) {
            return Optional.of(this.requirements.get(0));
        } else {
            return Optional.of(new AndRequirement(this.requirements));
        }
    }
    
    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     * 
     * @param output a consumer for the finished recipe
     * @param id the ID of the finished recipe
     */
    public void build(RecipeOutput output, ResourceLocation id) {
        this.validate(id);
        ShapelessArcaneTagRecipe recipe = new ShapelessArcaneTagRecipe(Objects.requireNonNullElse(this.group, ""), this.resultTag, this.resultAmount, this.ingredients, this.getFinalRequirement(), 
                Objects.requireNonNullElse(this.manaCosts, SourceList.EMPTY), this.baseExpertiseOverride, this.bonusExpertiseOverride, this.expertiseGroup);
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
        if (this.requirements.isEmpty()) {
            throw new IllegalStateException("No requirement is defined for arcane shapeless tag recipe " + id + "!");
        }
    }
}

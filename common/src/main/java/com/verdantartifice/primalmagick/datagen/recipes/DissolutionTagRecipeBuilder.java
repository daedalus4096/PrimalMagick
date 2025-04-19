package com.verdantartifice.primalmagick.datagen.recipes;

import com.verdantartifice.primalmagick.common.crafting.DissolutionTagRecipe;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

/**
 * Definition of a recipe data file builder for tag-outputting dissolution recipes.
 * 
 * @author Daedalus4096
 */
public class DissolutionTagRecipeBuilder {
    protected final TagKey<Item> recipeOutputTag;
    protected final int recipeOutputAmount;
    protected Ingredient ingredient;
    protected String group;
    protected SourceList manaCosts;

    protected DissolutionTagRecipeBuilder(TagKey<Item> resultTag, int resultAmount) {
        this.recipeOutputTag = resultTag;
        this.recipeOutputAmount = resultAmount;
    }
    
    public static DissolutionTagRecipeBuilder dissolutionTagRecipe(TagKey<Item> resultTag, int resultAmount) {
        return new DissolutionTagRecipeBuilder(resultTag, resultAmount);
    }
    
    public static DissolutionTagRecipeBuilder dissolutionRecipe(TagKey<Item> resultTag) {
        return dissolutionTagRecipe(resultTag, 1);
    }
    
    public DissolutionTagRecipeBuilder ingredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }
    
    public DissolutionTagRecipeBuilder ingredient(ItemLike item) {
        return this.ingredient(Ingredient.of(item));
    }
    
    public DissolutionTagRecipeBuilder ingredient(TagKey<Item> tag) {
        return this.ingredient(Ingredient.of(tag));
    }
    
    public DissolutionTagRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    public DissolutionTagRecipeBuilder defaultManaCost() {
        return this.manaCost(SourceList.EMPTY.add(Sources.EARTH, 10));
    }

    public DissolutionTagRecipeBuilder manaCost(SourceList mana) {
        return this.centimanaCost(mana.multiply(100));
    }

    public DissolutionTagRecipeBuilder centimanaCost(SourceList centimana) {
        this.manaCosts = centimana.copy();
        return this;
    }
    
    protected void validate(ResourceLocation id) {
        if (this.recipeOutputTag == null) {
            throw new IllegalStateException("No result tag defined for dissolution tag recipe " + id + "!");
        }
        if (this.recipeOutputAmount <= 0) {
            throw new IllegalStateException("Invalid result amount " + this.recipeOutputAmount + " specified for dissolution tag recipe " + id + "!");
        }
        if (this.ingredient == null) {
            throw new IllegalStateException("No ingredient defined for dissolution recipe " + id + "!");
        }
    }
    
    public void build(RecipeOutput output, ResourceLocation id) {
        this.validate(id);
        DissolutionTagRecipe recipe = new DissolutionTagRecipe(Objects.requireNonNullElse(this.group, ""), this.recipeOutputTag, this.recipeOutputAmount, this.ingredient, Objects.requireNonNullElse(this.manaCosts, SourceList.EMPTY));
        output.accept(id, recipe, null);
    }
}

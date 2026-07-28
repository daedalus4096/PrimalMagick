package com.verdantartifice.primalmagick.datagen.recipes;

import com.verdantartifice.primalmagick.common.crafting.DissolutionTagRecipe;
import com.verdantartifice.primalmagick.common.crafting.IDissolutionRecipe;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.DissolutionBookCategory;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

/**
 * Definition of a recipe data file builder for tag-outputting dissolution recipes.
 * 
 * @author Daedalus4096
 */
public class DissolutionTagRecipeBuilder {
    protected final HolderGetter<Item> itemGetter;
    protected final TagKey<Item> recipeOutputTag;
    protected final int recipeOutputAmount;
    protected boolean showNotification = true;
    protected DissolutionBookCategory category = DissolutionBookCategory.MISC;
    protected Ingredient ingredient;
    protected String group;
    protected SourceList manaCosts;

    protected DissolutionTagRecipeBuilder(HolderGetter<Item> itemGetter, TagKey<Item> resultTag, int resultAmount) {
        this.itemGetter = itemGetter;
        this.recipeOutputTag = resultTag;
        this.recipeOutputAmount = resultAmount;
    }
    
    public static DissolutionTagRecipeBuilder dissolutionTagRecipe(HolderGetter<Item> itemGetter, TagKey<Item> resultTag, int resultAmount) {
        return new DissolutionTagRecipeBuilder(itemGetter, resultTag, resultAmount);
    }
    
    public static DissolutionTagRecipeBuilder dissolutionTagRecipe(HolderGetter<Item> itemGetter, TagKey<Item> resultTag) {
        return dissolutionTagRecipe(itemGetter, resultTag, 1);
    }
    
    public DissolutionTagRecipeBuilder ingredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }
    
    public DissolutionTagRecipeBuilder ingredient(ItemLike item) {
        return this.ingredient(Ingredient.of(item));
    }
    
    public DissolutionTagRecipeBuilder ingredient(TagKey<Item> tag) {
        return this.ingredient(Ingredient.of(this.itemGetter.getOrThrow(tag)));
    }

    public DissolutionTagRecipeBuilder showNotification(boolean show) {
        this.showNotification = show;
        return this;
    }

    public DissolutionTagRecipeBuilder category(DissolutionBookCategory category) {
        this.category = category;
        return this;
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
    
    protected void validate(ResourceKey<Recipe<?>> id) {
        if (this.category == null) {
            throw new IllegalStateException("Null category specified for dissolution tag recipe " + id + "!");
        }
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

    public void build(RecipeOutput output, Identifier id) {
        this.build(output, ResourceKey.create(Registries.RECIPE, id));
    }

    public void build(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        this.validate(id);
        DissolutionTagRecipe recipe = new DissolutionTagRecipe(
                new Recipe.CommonInfo(this.showNotification),
                new IDissolutionRecipe.DissolutionCraftingBookInfo(
                        Objects.requireNonNullElse(this.category, DissolutionBookCategory.MISC),
                        Objects.requireNonNullElse(this.group, "")),
                this.recipeOutputTag,
                this.recipeOutputAmount,
                this.ingredient,
                Objects.requireNonNullElse(this.manaCosts, SourceList.EMPTY));
        output.accept(id, recipe, null);
    }
}

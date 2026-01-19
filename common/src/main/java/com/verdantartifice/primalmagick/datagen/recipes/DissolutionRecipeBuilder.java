package com.verdantartifice.primalmagick.datagen.recipes;

import com.verdantartifice.primalmagick.common.crafting.DissolutionRecipe;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

/**
 * Definition of a recipe data file builder for dissolution recipes.
 * 
 * @author Daedalus4096
 */
public class DissolutionRecipeBuilder {
    protected final HolderGetter<Item> itemGetter;
    protected final ItemStack result;
    protected Ingredient ingredient;
    protected String group;
    protected SourceList manaCosts;

    protected DissolutionRecipeBuilder(HolderGetter<Item> itemGetter, ItemStack result) {
        this.itemGetter = itemGetter;
        this.result = result.copy();
    }
    
    public static DissolutionRecipeBuilder dissolutionRecipe(HolderGetter<Item> itemGetter, ItemStack result) {
        return new DissolutionRecipeBuilder(itemGetter, result);
    }
    
    public static DissolutionRecipeBuilder dissolutionRecipe(HolderGetter<Item> itemGetter, ItemLike item, int count) {
        return dissolutionRecipe(itemGetter, new ItemStack(item.asItem(), count));
    }
    
    public static DissolutionRecipeBuilder dissolutionRecipe(HolderGetter<Item> itemGetter, ItemLike item) {
        return dissolutionRecipe(itemGetter, item, 1);
    }
    
    public DissolutionRecipeBuilder ingredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }
    
    public DissolutionRecipeBuilder ingredient(ItemLike item) {
        return this.ingredient(Ingredient.of(item));
    }
    
    public DissolutionRecipeBuilder ingredient(TagKey<Item> tag) {
        return this.ingredient(Ingredient.of(this.itemGetter.getOrThrow(tag)));
    }
    
    public DissolutionRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    public DissolutionRecipeBuilder defaultManaCost() {
        return this.manaCost(SourceList.EMPTY.add(Sources.EARTH, 10));
    }
    
    public DissolutionRecipeBuilder manaCost(SourceList mana) {
        return this.centimanaCost(mana.multiply(100));
    }

    public DissolutionRecipeBuilder centimanaCost(SourceList centimana) {
        this.manaCosts = centimana.copy();
        return this;
    }
    
    protected void validate(ResourceKey<Recipe<?>> id) {
        if (this.ingredient == null) {
            throw new IllegalStateException("No ingredient defined for dissolution recipe " + id + "!");
        }
    }
    
    public void build(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        this.validate(id);
        DissolutionRecipe recipe = new DissolutionRecipe(Objects.requireNonNullElse(this.group, ""), this.result, this.ingredient, Objects.requireNonNullElse(this.manaCosts, SourceList.EMPTY));
        output.accept(id, recipe, null);
    }
}

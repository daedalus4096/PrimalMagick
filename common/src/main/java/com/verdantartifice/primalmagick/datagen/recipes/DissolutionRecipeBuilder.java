package com.verdantartifice.primalmagick.datagen.recipes;

import com.verdantartifice.primalmagick.common.crafting.DissolutionRecipe;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

/**
 * Definition of a recipe data file builder for dissolution recipes.
 * 
 * @author Daedalus4096
 */
public class DissolutionRecipeBuilder {
    protected final ItemStack result;
    protected Ingredient ingredient;
    protected String group;
    protected SourceList manaCosts;

    protected DissolutionRecipeBuilder(ItemStack result) {
        this.result = result.copy();
    }
    
    public static DissolutionRecipeBuilder dissolutionRecipe(ItemStack result) {
        return new DissolutionRecipeBuilder(result);
    }
    
    public static DissolutionRecipeBuilder dissolutionRecipe(ItemLike item, int count) {
        return dissolutionRecipe(new ItemStack(item.asItem(), count));
    }
    
    public static DissolutionRecipeBuilder dissolutionRecipe(ItemLike item) {
        return dissolutionRecipe(item, 1);
    }
    
    public DissolutionRecipeBuilder ingredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }
    
    public DissolutionRecipeBuilder ingredient(ItemLike item) {
        return this.ingredient(Ingredient.of(item));
    }
    
    public DissolutionRecipeBuilder ingredient(TagKey<Item> tag) {
        return this.ingredient(Ingredient.of(tag));
    }
    
    public DissolutionRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }
    
    public DissolutionRecipeBuilder manaCost(SourceList mana) {
        return this.centimanaCost(mana.multiply(100));
    }

    public DissolutionRecipeBuilder centimanaCost(SourceList centimana) {
        this.manaCosts = centimana.copy();
        return this;
    }
    
    protected void validate(ResourceLocation id) {
        if (this.ingredient == null) {
            throw new IllegalStateException("No ingredient defined for dissolution recipe " + id + "!");
        }
    }
    
    public void build(RecipeOutput output, ResourceLocation id) {
        this.validate(id);
        DissolutionRecipe recipe = new DissolutionRecipe(Objects.requireNonNullElse(this.group, ""), this.result, this.ingredient, Objects.requireNonNullElse(this.manaCosts, SourceList.EMPTY));
        output.accept(id, recipe, null);
    }
}

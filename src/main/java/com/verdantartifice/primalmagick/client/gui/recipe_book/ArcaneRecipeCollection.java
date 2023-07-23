package com.verdantartifice.primalmagick.client.gui.recipe_book;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBook;

import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.core.RegistryAccess;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public class ArcaneRecipeCollection {
    protected final RegistryAccess registryAccess;
    protected final List<Recipe<?>> recipes;
    protected final boolean singleResultItem;
    protected final Set<Recipe<?>> craftable = new HashSet<>();
    protected final Set<Recipe<?>> fitsDimensions = new HashSet<>();
    protected final Set<Recipe<?>> known = new HashSet<>();
    
    public ArcaneRecipeCollection(RegistryAccess registryAccess, List<Recipe<?>> recipes) {
        this.registryAccess = registryAccess;
        this.recipes = ImmutableList.copyOf(recipes);
        if (recipes.size() <= 1) {
            this.singleResultItem = true;
        } else {
            this.singleResultItem = allRecipesHaveSameResult(registryAccess, recipes);
        }
    }
    
    public ArcaneRecipeCollection(RecipeCollection vanillaCollection) {
        this.registryAccess = vanillaCollection.registryAccess();
        this.recipes = ImmutableList.copyOf(vanillaCollection.getRecipes());
        this.singleResultItem = vanillaCollection.hasSingleResultItem();
    }
    
    protected static boolean allRecipesHaveSameResult(RegistryAccess registryAccess, List<Recipe<?>> recipes) {
        ItemStack referenceStack = recipes.get(0).getResultItem(registryAccess);
        for (int index = 1; index < recipes.size(); index++) {
            ItemStack stack = recipes.get(index).getResultItem(registryAccess);
            if (!ItemStack.isSameItemSameTags(referenceStack, stack)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean hasKnownRecipes() {
        return !this.known.isEmpty();
    }
    
    public void updateKnownRecipes(RecipeBook vanillaBook, ArcaneRecipeBook arcaneBook) {
        for (Recipe<?> recipe : this.recipes) {
            if (vanillaBook.contains(recipe) || arcaneBook.contains(recipe)) {
                this.known.add(recipe);
            }
        }
    }
    
    public void canCraft(StackedContents contents, int gridWidth, int gridHeight, RecipeBook vanillaBook, ArcaneRecipeBook arcaneBook) {
        for (Recipe<?> recipe : this.recipes) {
            boolean flag = recipe.canCraftInDimensions(gridWidth, gridHeight) && (vanillaBook.contains(recipe) || arcaneBook.contains(recipe));
            if (flag) {
                this.fitsDimensions.add(recipe);
            } else {
                this.fitsDimensions.remove(recipe);
            }
            if (flag && contents.canCraft(recipe, null)) {
                this.craftable.add(recipe);
            } else {
                this.craftable.remove(recipe);
            }
        }
    }
    
    public boolean isCraftable(Recipe<?> recipe) {
        return this.craftable.contains(recipe);
    }
    
    public boolean hasCraftable() {
        return !this.craftable.isEmpty();
    }
    
    public boolean hasFitting() {
        return !this.fitsDimensions.isEmpty();
    }
    
    public List<Recipe<?>> getRecipes() {
        return this.recipes;
    }
    
    public RegistryAccess registryAccess() {
        return this.registryAccess;
    }
    
    public List<Recipe<?>> getRecipes(boolean isCraftable) {
        List<Recipe<?>> retVal = new ArrayList<>();
        Set<Recipe<?>> set = isCraftable ? this.craftable : this.fitsDimensions;
        
        for (Recipe<?> recipe : this.recipes) {
            if (set.contains(recipe)) {
                retVal.add(recipe);
            }
        }
        
        return retVal;
    }
    
    public List<Recipe<?>> getDisplayRecipes(boolean isCraftable) {
        List<Recipe<?>> retVal = new ArrayList<>();
        for (Recipe<?> recipe : this.recipes) {
            if (this.fitsDimensions.contains(recipe) && this.craftable.contains(recipe) == isCraftable) {
                retVal.add(recipe);
            }
        }
        return retVal;
    }
    
    public boolean hasSingleResultItem() {
        return this.singleResultItem;
    }

    @Override
    public int hashCode() {
        return this.recipes.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ArcaneRecipeCollection other = (ArcaneRecipeCollection) obj;
        return this.recipes.equals(other.getRecipes());
    }
}

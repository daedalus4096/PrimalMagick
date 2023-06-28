package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * Crafting recipe interface for an arcane recipe.  An arcane recipe is like a vanilla recipe,
 * but has a research requirement and an optional mana cost.
 *  
 * @author Daedalus4096
 */
public interface IArcaneRecipe extends CraftingRecipe, IHasManaCost, IHasRequiredResearch, IArcaneRecipeBookItem {
    @Override
    default RecipeType<?> getType() {
        return RecipeTypesPM.ARCANE_CRAFTING.get();
    }
    
    @Override
    default boolean isSpecial() {
        // Return true to keep arcane recipes from showing up in the vanilla recipe book
        return true;
    }
    
    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(BlocksPM.ARCANE_WORKBENCH.get());
    }

    @Override
    default CraftingBookCategory category() {
        // Arcane recipes use a separate recipe book, so an accurate crafting book category isn't needed
        return CraftingBookCategory.MISC;
    }
}

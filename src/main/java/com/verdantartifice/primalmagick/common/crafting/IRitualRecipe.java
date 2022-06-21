package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * Crafting recipe interface for a ritual recipe.  Ritual recipes are performed across multiple
 * blocks, with a central altar surrounded by multiple offering pedestals and props.  They also
 * have research requirements and optional mana costs.
 * 
 * @author Daedalus4096
 */
public interface IRitualRecipe extends CraftingRecipe, IHasManaCost, IHasRequiredResearch {
    /**
     * Get the list of props for the recipe.
     * 
     * @return the prop list fort he recipe
     */
    public NonNullList<BlockIngredient> getProps();
    
    /**
     * Get the instability rating of the recipe.
     * 
     * @return the instability rating of the recipe
     */
    public int getInstability();
    
    @Override
    default RecipeType<?> getType() {
        return RecipeTypesPM.RITUAL.get();
    }
    
    @Override
    default boolean isSpecial() {
        // Return true to keep ritual recipes from showing up in the vanilla recipe book
        return true;
    }
    
    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(BlocksPM.RITUAL_ALTAR.get());
    }
}

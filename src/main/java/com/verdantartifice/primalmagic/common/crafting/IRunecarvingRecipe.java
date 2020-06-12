package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

/**
 * Crafting recipe interface for a runecarving recipe.  A runecarving recipe is like a stonecutting
 * recipe, but has two ingredients and a research requirement.
 *  
 * @author Daedalus4096
 */
public interface IRunecarvingRecipe extends IRecipe<IInventory> {
    /**
     * Get the required research for the recipe.
     * 
     * @return the required research for the recipe
     */
    public SimpleResearchKey getRequiredResearch();
    
    default IRecipeType<?> getType() {
        return RecipeTypesPM.RUNECARVING;
    }
    
    @Override
    default boolean isDynamic() {
        // Return true to keep runecarving recipes from showing up in the vanilla recipe book
        return true;
    }
    
    default ItemStack getIcon() {
        return new ItemStack(BlocksPM.RUNECARVING_TABLE.get());
    }
}

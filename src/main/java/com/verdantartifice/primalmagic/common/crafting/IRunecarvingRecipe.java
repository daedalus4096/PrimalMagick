package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * Crafting recipe interface for a runecarving recipe.  A runecarving recipe is like a stonecutting
 * recipe, but has two ingredients and a research requirement.
 *  
 * @author Daedalus4096
 */
public interface IRunecarvingRecipe extends Recipe<Container> {
    /**
     * Get the required research for the recipe.
     * 
     * @return the required research for the recipe
     */
    public CompoundResearchKey getRequiredResearch();
    
    default RecipeType<?> getType() {
        return RecipeTypesPM.RUNECARVING;
    }
    
    @Override
    default boolean isSpecial() {
        // Return true to keep runecarving recipes from showing up in the vanilla recipe book
        return true;
    }
    
    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(BlocksPM.RUNECARVING_TABLE.get());
    }
}

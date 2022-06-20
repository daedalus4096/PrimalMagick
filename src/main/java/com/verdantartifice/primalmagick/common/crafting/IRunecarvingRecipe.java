package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;

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
public interface IRunecarvingRecipe extends Recipe<Container>, IHasRequiredResearch {
    @Override
    default RecipeType<?> getType() {
        return RecipeTypesPM.RUNECARVING.get();
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

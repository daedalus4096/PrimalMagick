package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;

/**
 * Crafting recipe interface for an arcane recipe.  An arcane recipe is like a vanilla recipe,
 * but has a research requirement and an optional mana cost.
 *  
 * @author Daedalus4096
 */
public interface IArcaneRecipe extends ICraftingRecipe, IHasManaCost {
    /**
     * Get the required research for the recipe.
     * 
     * @return the required research for the recipe
     */
    public SimpleResearchKey getRequiredResearch();

    default IRecipeType<?> getType() {
        return RecipeTypesPM.ARCANE_CRAFTING;
    }
    
    @Override
    default boolean isDynamic() {
        // Return true to keep arcane recipes from showing up in the vanilla recipe book
        return true;
    }
    
    default ItemStack getIcon() {
        return new ItemStack(BlocksPM.ARCANE_WORKBENCH.get());
    }
}

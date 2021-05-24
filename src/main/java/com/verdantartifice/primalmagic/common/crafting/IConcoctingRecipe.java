package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

public interface IConcoctingRecipe extends IRecipe<IInventory>, IHasManaCost {
    /**
     * Get the required research for the recipe.
     * 
     * @return the required research for the recipe
     */
    public SimpleResearchKey getRequiredResearch();

    default IRecipeType<?> getType() {
        return RecipeTypesPM.CONCOCTING;
    }
    
    @Override
    default boolean isDynamic() {
        // Return true to keep arcane recipes from showing up in the vanilla recipe book
        return true;
    }
    
    default ItemStack getIcon() {
        // FIXME Use concocter icon
        return new ItemStack(BlocksPM.ARCANE_WORKBENCH.get());
    }
}

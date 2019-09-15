package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

public interface IArcaneRecipe extends IRecipe<CraftingInventory> {
    public SimpleResearchKey getRequiredResearch();
    
    default IRecipeType<?> getType() {
        return RecipeTypesPM.ARCANE_CRAFTING;
    }
}

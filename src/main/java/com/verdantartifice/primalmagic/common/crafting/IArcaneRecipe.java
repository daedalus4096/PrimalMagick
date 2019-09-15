package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.crafting.IRecipe;

public interface IArcaneRecipe extends IRecipe<CraftingInventory> {
    public SimpleResearchKey getRequiredResearch();
}

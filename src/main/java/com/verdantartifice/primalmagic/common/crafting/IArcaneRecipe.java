package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;

public interface IArcaneRecipe extends ICraftingRecipe {
    public SimpleResearchKey getRequiredResearch();
    
    public SourceList getManaCosts();
    
    default IRecipeType<?> getType() {
        return RecipeTypesPM.ARCANE_CRAFTING;
    }
    
    @Override
    default boolean isDynamic() {
        // Return true to keep arcane recipes from showing up in the vanilla recipe book
        return true;
    }
}

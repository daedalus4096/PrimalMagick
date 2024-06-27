package com.verdantartifice.primalmagick.common.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraftforge.common.crafting.IShapedRecipe;

public interface IShapedRecipePM<T extends RecipeInput> extends IShapedRecipe<T> {
    default ItemStack assemble(T pInput, HolderLookup.Provider pRegistries) {
        return this.getResultItem(pRegistries).copy();
    }

    default boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth >= this.getRecipeWidth() && pHeight >= this.getRecipeHeight();
    }
}

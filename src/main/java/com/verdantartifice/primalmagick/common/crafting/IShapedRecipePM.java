package com.verdantartifice.primalmagick.common.crafting;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.IShapedRecipe;

public interface IShapedRecipePM<C extends Container> extends IShapedRecipe<C> {
    default ItemStack assemble(C pContainer, RegistryAccess pRegistryAccess) {
        return this.getResultItem(pRegistryAccess).copy();
    }

    default boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth >= this.getRecipeWidth() && pHeight >= this.getRecipeHeight();
    }
}

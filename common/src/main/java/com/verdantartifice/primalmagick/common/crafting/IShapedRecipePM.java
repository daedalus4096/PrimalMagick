package com.verdantartifice.primalmagick.common.crafting;

import net.minecraft.world.item.crafting.RecipeInput;

public interface IShapedRecipePM<T extends RecipeInput> {
    int getWidth();
    int getHeight();
}

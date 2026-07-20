package com.verdantartifice.primalmagick.common.menus;

import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.jetbrains.annotations.Nullable;

public interface IRecipeDisplayListener {
    void setRecipeDisplay(@Nullable RecipeDisplay display);
}

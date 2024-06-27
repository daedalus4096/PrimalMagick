package com.verdantartifice.primalmagick.common.crafting;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

public abstract class AbstractRecipe<T extends RecipeInput> implements Recipe<T> {
    protected final String group;

    protected AbstractRecipe(String group) {
        this.group = group;
    }

    @Override
    public String getGroup() {
        return this.group;
    }
}

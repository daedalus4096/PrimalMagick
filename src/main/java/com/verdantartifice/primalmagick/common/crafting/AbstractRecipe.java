package com.verdantartifice.primalmagick.common.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public abstract class AbstractRecipe<C extends Container> implements Recipe<C> {
    protected final String group;

    protected AbstractRecipe(String group) {
        this.group = group;
    }

    @Override
    public String getGroup() {
        return this.group;
    }
}

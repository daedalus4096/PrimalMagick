package com.verdantartifice.primalmagick.common.crafting;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractStackCraftingRecipe<C extends Container> extends AbstractRecipe<C> {
    protected final ItemStack output;
    
    protected AbstractStackCraftingRecipe(String group, ItemStack output) {
        super(group);
        this.output = output;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.output;
    }
}

package com.verdantartifice.primalmagick.common.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public abstract class AbstractStackCraftingRecipe<T extends RecipeInput> extends AbstractRecipe<T> {
    protected final ItemStack output;
    
    protected AbstractStackCraftingRecipe(String group, ItemStack output) {
        super(group);
        this.output = output;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.output;
    }
}

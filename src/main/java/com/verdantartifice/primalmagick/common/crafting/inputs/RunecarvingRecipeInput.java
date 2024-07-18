package com.verdantartifice.primalmagick.common.crafting.inputs;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record RunecarvingRecipeInput(ItemStack base, ItemStack etching) implements RecipeInput {
    @Override
    public ItemStack getItem(int pIndex) {
        return switch (pIndex) {
            case 0 -> this.base;
            case 1 -> this.etching;
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + pIndex);
        };
    }

    @Override
    public int size() {
        return 2;
    }
}

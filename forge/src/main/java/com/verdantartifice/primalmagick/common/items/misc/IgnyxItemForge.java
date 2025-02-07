package com.verdantartifice.primalmagick.common.items.misc;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class IgnyxItemForge extends IgnyxItem{
    public IgnyxItemForge(Properties properties) {
        super(properties);
    }

    @Override
    public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
        return BURN_TICKS;
    }
}

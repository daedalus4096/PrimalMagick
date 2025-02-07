package com.verdantartifice.primalmagick.common.items.misc;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class IgnyxItemNeoforge extends IgnyxItem{
    public IgnyxItemNeoforge(Properties properties) {
        super(properties);
    }

    @Override
    public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
        return BURN_TICKS;
    }
}

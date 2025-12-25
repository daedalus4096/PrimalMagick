package com.verdantartifice.primalmagick.common.items.misc;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IgnyxItemNeoforge extends IgnyxItem{
    public IgnyxItemNeoforge(Properties properties) {
        super(properties);
    }

    @Override
    public int getBurnTime(@NotNull ItemStack itemStack, @Nullable RecipeType<?> recipeType, @NotNull FuelValues fuelValues) {
        return BURN_TICKS;
    }
}

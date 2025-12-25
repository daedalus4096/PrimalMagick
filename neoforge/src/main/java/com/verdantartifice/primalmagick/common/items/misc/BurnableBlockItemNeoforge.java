package com.verdantartifice.primalmagick.common.items.misc;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.FuelValues;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BurnableBlockItemNeoforge extends BurnableBlockItem {
    public BurnableBlockItemNeoforge(Block blockIn, int burnTicks, Properties builder) {
        super(blockIn, burnTicks, builder);
    }

    @Override
    public int getBurnTime(@NotNull ItemStack itemStack, @Nullable RecipeType<?> recipeType, @NotNull FuelValues fuelValues) {
        return this.burnTicks;
    }
}

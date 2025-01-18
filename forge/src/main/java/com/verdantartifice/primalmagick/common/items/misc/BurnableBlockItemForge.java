package com.verdantartifice.primalmagick.common.items.misc;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

public class BurnableBlockItemForge extends BurnableBlockItem{
    public BurnableBlockItemForge(Block blockIn, int burnTicks, Properties builder) {
        super(blockIn, burnTicks, builder);
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.burnTicks;
    }
}

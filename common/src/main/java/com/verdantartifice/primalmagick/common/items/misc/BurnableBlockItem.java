package com.verdantartifice.primalmagick.common.items.misc;

import javax.annotation.Nullable;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

/**
 * Block item definition for a block that can be used as fuel in a furnace or similar device.
 * 
 * @author Daedalus4096
 */
public class BurnableBlockItem extends BlockItem {
    protected final int burnTicks;
    
    public BurnableBlockItem(Block blockIn, int burnTicks, Properties builder) {
        super(blockIn, builder);
        this.burnTicks = burnTicks;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.burnTicks;
    }
}

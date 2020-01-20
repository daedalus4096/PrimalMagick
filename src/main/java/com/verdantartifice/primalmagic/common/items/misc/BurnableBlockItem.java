package com.verdantartifice.primalmagic.common.items.misc;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

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
    public int getBurnTime(ItemStack itemStack) {
        return this.burnTicks;
    }
}

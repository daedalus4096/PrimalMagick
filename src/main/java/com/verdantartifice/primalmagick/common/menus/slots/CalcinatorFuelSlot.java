package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.common.menus.CalcinatorMenu;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Custom GUI slot for calcinator fuel.
 * 
 * @author Daedalus4096
 */
public class CalcinatorFuelSlot extends SlotItemHandler {
    protected final CalcinatorMenu container;

    public CalcinatorFuelSlot(CalcinatorMenu container, IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.container = container;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow valid calcinator fuel or empty buckets
        return this.container.isFuel(stack) || this.isBucket(stack);
    }
    
    @Override
    public int getMaxStackSize(ItemStack stack) {
        // Only one empty bucket at a time
        return this.isBucket(stack) ? 1 : super.getMaxStackSize(stack);
    }
    
    protected boolean isBucket(ItemStack stack) {
        return stack.getItem() == Items.BUCKET;
    }
}

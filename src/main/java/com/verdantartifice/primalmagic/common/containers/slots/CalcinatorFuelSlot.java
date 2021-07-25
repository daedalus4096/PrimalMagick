package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.common.containers.CalcinatorContainer;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Custom GUI slot for calcinator fuel.
 * 
 * @author Daedalus4096
 */
public class CalcinatorFuelSlot extends Slot {
    protected final CalcinatorContainer container;

    public CalcinatorFuelSlot(CalcinatorContainer container, Container inventoryIn, int index, int xPosition, int yPosition) {
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

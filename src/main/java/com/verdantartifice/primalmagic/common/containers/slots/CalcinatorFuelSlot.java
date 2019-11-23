package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.common.containers.CalcinatorContainer;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class CalcinatorFuelSlot extends Slot {
    protected final CalcinatorContainer container;

    public CalcinatorFuelSlot(CalcinatorContainer container, IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.container = container;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return this.container.isFuel(stack) || this.isBucket(stack);
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack) {
        return this.isBucket(stack) ? 1 : super.getItemStackLimit(stack);
    }
    
    protected boolean isBucket(ItemStack stack) {
        return stack.getItem() == Items.BUCKET;
    }
}

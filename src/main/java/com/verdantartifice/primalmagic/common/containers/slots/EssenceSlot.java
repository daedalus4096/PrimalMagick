package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.common.items.essence.EssenceItem;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Custom GUI slot for essence inputs.
 * 
 * @author Daedalus4096
 */
public class EssenceSlot extends Slot {
    public EssenceSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        // Only allow essences to be dropped into the slot
        return !stack.isEmpty() && stack.getItem() instanceof EssenceItem;
    }
}

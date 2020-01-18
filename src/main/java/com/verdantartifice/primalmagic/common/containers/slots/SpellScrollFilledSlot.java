package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.common.items.wands.SpellScrollItem;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Custom GUI slot for filled spell scroll inputs.
 * 
 * @author Daedalus4096
 */
public class SpellScrollFilledSlot extends Slot {
    public SpellScrollFilledSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        // Only allow filled spell scrolls to be dropped into the slot
        return stack.getItem() instanceof SpellScrollItem;
    }
}

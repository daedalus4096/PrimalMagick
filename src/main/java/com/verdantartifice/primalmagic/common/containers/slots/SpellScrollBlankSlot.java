package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Custom GUI slot for blank spell scroll inputs.
 * 
 * @author Daedalus4096
 */
public class SpellScrollBlankSlot extends Slot {
    public SpellScrollBlankSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        // Only allow blank spell scrolls to be dropped into the slot
        return stack.getItem().equals(ItemsPM.SPELL_SCROLL_BLANK);
    }
}

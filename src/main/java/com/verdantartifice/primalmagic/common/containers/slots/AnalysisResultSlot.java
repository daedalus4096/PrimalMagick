package com.verdantartifice.primalmagic.common.containers.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Custom GUI slot for analysis table results.
 * 
 * @author Daedalus4096
 */
public class AnalysisResultSlot extends Slot {
    public AnalysisResultSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        // Don't allow anything to be dropped into the slot
        return false;
    }
    
    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        // Don't allow anything to be taken out of the slot
        return false;
    }
}

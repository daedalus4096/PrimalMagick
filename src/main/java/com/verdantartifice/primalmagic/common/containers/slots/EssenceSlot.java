package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.common.items.essence.EssenceItem;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Custom GUI slot for essence inputs.
 * 
 * @author Daedalus4096
 */
public class EssenceSlot extends Slot {
    public EssenceSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow essences to be dropped into the slot
        return !stack.isEmpty() && stack.getItem() instanceof EssenceItem;
    }
}

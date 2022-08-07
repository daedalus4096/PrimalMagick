package com.verdantartifice.primalmagick.common.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/**
 * Custom GUI slot for essence cask storage.
 * 
 * @author Daedalus4096
 */
public class EssenceCaskSlot extends EssenceSlot {
    public EssenceCaskSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        // Ignore the max stack size of the item, just use the container limit
        return this.getMaxStackSize();
    }
}

package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.common.items.wands.SpellScrollItem;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Custom GUI slot for filled spell scroll inputs.
 * 
 * @author Daedalus4096
 */
public class SpellScrollFilledSlot extends Slot {
    public SpellScrollFilledSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow filled spell scrolls to be dropped into the slot
        return stack.getItem() instanceof SpellScrollItem;
    }
}

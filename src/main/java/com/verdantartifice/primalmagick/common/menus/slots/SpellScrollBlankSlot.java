package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Custom GUI slot for blank spell scroll inputs.
 * 
 * @author Daedalus4096
 */
public class SpellScrollBlankSlot extends Slot {
    public SpellScrollBlankSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow blank spell scrolls to be dropped into the slot
        return stack.getItem().equals(ItemsPM.SPELL_SCROLL_BLANK.get());
    }
}

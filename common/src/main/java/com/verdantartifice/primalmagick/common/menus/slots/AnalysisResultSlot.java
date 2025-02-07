package com.verdantartifice.primalmagick.common.menus.slots;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Custom GUI slot for analysis table results.
 * 
 * @author Daedalus4096
 */
public class AnalysisResultSlot extends Slot {
    public AnalysisResultSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Don't allow anything to be dropped into the slot
        return false;
    }
    
    @Override
    public boolean mayPickup(Player playerIn) {
        // Don't allow anything to be taken out of the slot
        return false;
    }
}

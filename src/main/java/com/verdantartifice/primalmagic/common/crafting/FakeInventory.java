package com.verdantartifice.primalmagic.common.crafting;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

/**
 * Fake inventory.  Used in situations where a real inventory is unavailable, but still necessary.
 * 
 * @author Daedalus4096
 */
public class FakeInventory extends Inventory {
    public FakeInventory(int numSlots) {
        super(numSlots);
    }
    
    public FakeInventory(ItemStack... items) {
        super(items);
    }
}

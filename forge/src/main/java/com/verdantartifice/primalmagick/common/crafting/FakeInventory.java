package com.verdantartifice.primalmagick.common.crafting;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

/**
 * Fake inventory.  Used in situations where a real inventory is unavailable, but still necessary.
 * 
 * @author Daedalus4096
 */
public class FakeInventory extends SimpleContainer {
    public FakeInventory(int numSlots) {
        super(numSlots);
    }
    
    public FakeInventory(ItemStack... items) {
        super(items);
    }
}

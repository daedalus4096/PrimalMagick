package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Custom GUI slot for honeycomb inputs.
 * 
 * @author Daedalus4096
 */
public class HoneycombSlot extends Slot {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "item/empty_honeycomb_slot");

    public HoneycombSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.setBackground(InventoryMenu.BLOCK_ATLAS, TEXTURE);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow honeycombs to be dropped in the slot
        return stack.getItem().equals(Items.HONEYCOMB);
    }
}

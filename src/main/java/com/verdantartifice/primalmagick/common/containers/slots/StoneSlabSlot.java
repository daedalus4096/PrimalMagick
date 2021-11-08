package com.verdantartifice.primalmagick.common.containers.slots;

import com.verdantartifice.primalmagick.PrimalMagic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Custom GUI slot for stone slab inputs.
 * 
 * @author Daedalus4096
 */
public class StoneSlabSlot extends Slot {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "item/empty_slab_slot");

    public StoneSlabSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.setBackground(InventoryMenu.BLOCK_ATLAS, TEXTURE);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow stone slabs to be dropped in the slot
        return stack.getItem() == Items.STONE_SLAB;
    }
}

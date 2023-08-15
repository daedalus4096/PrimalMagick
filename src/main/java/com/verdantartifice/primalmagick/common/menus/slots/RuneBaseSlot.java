package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Custom GUI slot for stone slab inputs.
 * 
 * @author Daedalus4096
 */
public class RuneBaseSlot extends Slot {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "item/empty_slab_slot");

    public RuneBaseSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.setBackground(InventoryMenu.BLOCK_ATLAS, TEXTURE);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow stone slabs to be dropped in the slot
        return stack.is(ItemTagsPM.RUNE_BASES);
    }
}

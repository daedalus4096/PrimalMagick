package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.wands.WandCoreItem;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Custom GUI slot for wand core inputs.
 * 
 * @author Daedalus4096
 */
public class WandCoreSlot extends Slot {
    protected static final ResourceLocation ATLAS_TEXTURE = new ResourceLocation("textures/atlas/blocks.png");
    protected static final ResourceLocation SLOT_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "item/empty_wand_core_slot");
    
    public WandCoreSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.setBackground(ATLAS_TEXTURE, SLOT_TEXTURE);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        // Only allow wand cores to be dropped into the slot
        return stack.getItem() instanceof WandCoreItem;
    }
}

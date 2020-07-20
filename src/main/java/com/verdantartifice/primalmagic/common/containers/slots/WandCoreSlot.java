package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.wands.StaffCoreItem;
import com.verdantartifice.primalmagic.common.items.wands.WandCoreItem;

import net.minecraft.client.renderer.texture.AtlasTexture;
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
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "item/empty_wand_core_slot");
    
    @SuppressWarnings("deprecation")
    public WandCoreSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.setBackground(AtlasTexture.LOCATION_BLOCKS_TEXTURE, TEXTURE);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        // Only allow wand or staff cores to be dropped into the slot
        return (stack.getItem() instanceof WandCoreItem) || (stack.getItem() instanceof StaffCoreItem);
    }
}

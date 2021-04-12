package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

/**
 * Custom GUI slot for empty bottle inputs.
 * 
 * @author Daedalus4096
 */
public class BottleSlot extends Slot {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "item/empty_bottle_slot");

    @SuppressWarnings("deprecation")
    public BottleSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.setBackground(AtlasTexture.LOCATION_BLOCKS_TEXTURE, TEXTURE);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        // Only allow empty bottles to be dropped in the slot
        return stack.getItem().equals(Items.GLASS_BOTTLE);
    }
}

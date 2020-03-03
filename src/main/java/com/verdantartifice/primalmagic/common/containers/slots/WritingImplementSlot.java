package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.theorycrafting.IWritingImplement;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Custom GUI slot for writing implement inputs.
 * 
 * @author Daedalus4096
 */
public class WritingImplementSlot extends Slot {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "item/empty_pencil_slot");

    @SuppressWarnings("deprecation")
    public WritingImplementSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.setBackground(AtlasTexture.LOCATION_BLOCKS_TEXTURE, TEXTURE);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        // Only allow writing implements to be dropped in the slot
        return stack.getItem() instanceof IWritingImplement;
    }
}

package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.theorycrafting.IWritingImplement;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom GUI slot for writing implement inputs.
 * 
 * @author Daedalus4096
 */
public class WritingImplementSlot extends Slot {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "item/empty_pencil_slot");

    @SuppressWarnings("deprecation")
    public WritingImplementSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.setBackground(TextureAtlas.LOCATION_BLOCKS, TEXTURE);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow writing implements to be dropped in the slot
        return stack.getItem() instanceof IWritingImplement;
    }
}

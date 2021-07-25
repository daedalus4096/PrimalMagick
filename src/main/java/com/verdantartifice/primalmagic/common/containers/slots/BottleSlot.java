package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom GUI slot for empty bottle inputs.
 * 
 * @author Daedalus4096
 */
public class BottleSlot extends Slot {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "item/empty_bottle_slot");

    @SuppressWarnings("deprecation")
    public BottleSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.setBackground(TextureAtlas.LOCATION_BLOCKS, TEXTURE);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow empty bottles to be dropped in the slot
        return stack.getItem().equals(Items.GLASS_BOTTLE);
    }
}

package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.wands.WandGemItem;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom GUI slot for wand gem inputs.
 * 
 * @author Daedalus4096
 */
public class WandGemSlot extends Slot {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "item/empty_wand_gem_slot");
    
    @SuppressWarnings("deprecation")
    public WandGemSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.setBackground(TextureAtlas.LOCATION_BLOCKS, TEXTURE);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow wand gems to be dropped into the slot
        return stack.getItem() instanceof WandGemItem;
    }
}

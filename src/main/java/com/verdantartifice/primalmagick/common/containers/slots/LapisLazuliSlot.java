package com.verdantartifice.primalmagick.common.containers.slots;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;

/**
 * Custom GUI slot for lapis lazuli inputs.
 * 
 * @author Daedalus4096
 */
public class LapisLazuliSlot extends Slot {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "item/empty_lapis_slot");

    public LapisLazuliSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.setBackground(InventoryMenu.BLOCK_ATLAS, TEXTURE);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow lapis lazuli to be dropped in the slot
        return stack.is(Tags.Items.GEMS_LAPIS);
    }
}

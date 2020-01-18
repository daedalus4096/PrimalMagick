package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Custom GUI slot for wand inputs.
 * 
 * @author Daedalus4096
 */
public class WandSlot extends Slot {
    public WandSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        // Only allow wands to be dropped into the slot
        return stack.getItem() instanceof IWand;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public String getSlotTexture() {
        // Show a texture on the background of the slot when empty
        return "primalmagic:item/empty_wand_slot";
    }
}

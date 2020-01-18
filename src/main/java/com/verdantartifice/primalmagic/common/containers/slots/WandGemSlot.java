package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.common.items.wands.WandGemItem;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Custom GUI slot for wand gem inputs.
 * 
 * @author Daedalus4096
 */
public class WandGemSlot extends Slot {
    public WandGemSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        // Only allow wand gems to be dropped into the slot
        return stack.getItem() instanceof WandGemItem;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public String getSlotTexture() {
        // Show a texture on the background of the slot when empty
        return "primalmagic:item/empty_wand_gem_slot";
    }
}

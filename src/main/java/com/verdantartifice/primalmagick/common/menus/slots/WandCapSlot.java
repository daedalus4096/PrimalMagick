package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.wands.WandCapItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Custom GUI slot for wand cap inputs.
 * 
 * @author Daedalus4096
 */
public class WandCapSlot extends Slot {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "item/empty_wand_cap_slot");
    
    public WandCapSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.setBackground(InventoryMenu.BLOCK_ATLAS, TEXTURE);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow wand caps to be dropped into the slot
        return stack.getItem() instanceof WandCapItem;
    }
}

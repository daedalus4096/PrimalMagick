package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.wands.StaffCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCoreItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Custom GUI slot for wand core inputs.
 * 
 * @author Daedalus4096
 */
public class WandCoreSlot extends Slot {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "item/empty_wand_core_slot");
    
    public WandCoreSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.setBackground(InventoryMenu.BLOCK_ATLAS, TEXTURE);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow wand or staff cores to be dropped into the slot
        return (stack.getItem() instanceof WandCoreItem) || (stack.getItem() instanceof StaffCoreItem);
    }
}

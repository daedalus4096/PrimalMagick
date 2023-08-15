package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.wands.IStaff;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Custom GUI slot for wand inputs.
 * 
 * @author Daedalus4096
 */
public class WandSlot extends Slot {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "item/empty_wand_slot");
    
    protected final boolean allowStaves;
    
    public WandSlot(Container inventoryIn, int index, int xPosition, int yPosition, boolean allowStaves) {
        super(inventoryIn, index, xPosition, yPosition);
        this.setBackground(InventoryMenu.BLOCK_ATLAS, TEXTURE);
        this.allowStaves = allowStaves;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow wands or, if allowed, staves to be dropped into the slot
        return (stack.getItem() instanceof IWand) && (this.allowStaves || !(stack.getItem() instanceof IStaff));
    }
}

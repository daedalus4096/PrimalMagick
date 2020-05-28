package com.verdantartifice.primalmagic.common.tiles.rituals;

import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * Definition of a ritual lectern tile entity.  Holds the lectern's inventory.
 * 
 * @author Daedalus4096
 */
public class RitualLecternTileEntity extends TileInventoryPM {
    public RitualLecternTileEntity() {
        super(TileEntityTypesPM.RITUAL_LECTERN.get(), 1);
    }
    
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        // Don't allow automation to add items
        return false;
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
    
    public boolean hasBook() {
        ItemStack stack = this.getStackInSlot(0);
        return !stack.isEmpty() && stack.getItem() == Items.ENCHANTED_BOOK;
    }
}

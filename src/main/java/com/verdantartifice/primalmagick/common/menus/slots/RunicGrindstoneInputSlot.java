package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.common.runes.RuneManager;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Input slot definition for the runic grindstone GUI.
 * 
 * @author Daedalus4096
 */
public class RunicGrindstoneInputSlot extends Slot {
    public RunicGrindstoneInputSlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return pStack.isDamageableItem() || pStack.is(Items.ENCHANTED_BOOK) || pStack.isEnchanted() || pStack.canGrindstoneRepair() || RuneManager.hasRunes(pStack);
    }
}

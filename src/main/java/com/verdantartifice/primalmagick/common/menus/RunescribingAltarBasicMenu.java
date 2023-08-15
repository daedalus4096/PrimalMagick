package com.verdantartifice.primalmagick.common.menus;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.menus.slots.RuneSlot;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/**
 * Server data container for this basic runescribing altar GUI.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarBasicMenu extends AbstractRunescribingAltarMenu {
    protected static final int RUNE_CAPACITY = 3;

    public RunescribingAltarBasicMenu(int id, @Nonnull Inventory playerInv) {
        super(MenuTypesPM.RUNESCRIBING_ALTAR_BASIC.get(), id, playerInv);
    }
    
    @Override
    protected int getRuneCapacity() {
        return RUNE_CAPACITY;
    }
    
    @Override
    protected Slot addRuneSlots() {
        this.addSlot(new RuneSlot(this.altarInv, 1, 44, 35));
        this.addSlot(new RuneSlot(this.altarInv, 2, 62, 35));
        return this.addSlot(new RuneSlot(this.altarInv, 3, 80, 35));
    }
}

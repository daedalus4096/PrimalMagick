package com.verdantartifice.primalmagick.common.menus;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.menus.slots.RuneSlot;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/**
 * Server data container for the enchanted runescribing altar GUI.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarEnchantedMenu extends AbstractRunescribingAltarMenu {
    protected static final int RUNE_CAPACITY = 5;
    
    public RunescribingAltarEnchantedMenu(int id, @Nonnull Inventory playerInv) {
        super(MenuTypesPM.RUNESCRIBING_ALTAR_ENCHANTED.get(), id, playerInv);
    }
    
    @Override
    protected int getRuneCapacity() {
        return RUNE_CAPACITY;
    }
    
    @Override
    protected Slot addRuneSlots() {
        this.addSlot(new RuneSlot(this.altarInv, 1, 62, 17));
        this.addSlot(new RuneSlot(this.altarInv, 2, 44, 35));
        this.addSlot(new RuneSlot(this.altarInv, 3, 62, 35));
        this.addSlot(new RuneSlot(this.altarInv, 4, 80, 35));
        return this.addSlot(new RuneSlot(this.altarInv, 5, 62, 53));
    }
}

package com.verdantartifice.primalmagick.common.menus;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.menus.slots.RuneSlot;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/**
 * Server data container for the forbidden runescribing altar GUI.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarForbiddenContainer extends AbstractRunescribingAltarContainer {
    protected static final int RUNE_CAPACITY = 7;
    
    public RunescribingAltarForbiddenContainer(int id, @Nonnull Inventory playerInv) {
        super(ContainersPM.RUNESCRIBING_ALTAR_FORBIDDEN.get(), id, playerInv);
    }
    
    @Override
    protected int getRuneCapacity() {
        return RUNE_CAPACITY;
    }
    
    @Override
    protected Slot addRuneSlots() {
        this.addSlot(new RuneSlot(this.altarInv, 1, 44, 17));
        this.addSlot(new RuneSlot(this.altarInv, 2, 62, 17));
        this.addSlot(new RuneSlot(this.altarInv, 3, 80, 17));
        this.addSlot(new RuneSlot(this.altarInv, 4, 44, 35));
        this.addSlot(new RuneSlot(this.altarInv, 5, 62, 35));
        this.addSlot(new RuneSlot(this.altarInv, 6, 80, 35));
        return this.addSlot(new RuneSlot(this.altarInv, 7, 62, 53));
    }
}

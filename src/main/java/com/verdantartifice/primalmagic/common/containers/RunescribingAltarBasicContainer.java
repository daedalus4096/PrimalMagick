package com.verdantartifice.primalmagic.common.containers;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.containers.slots.RuneSlot;

import net.minecraft.entity.player.PlayerInventory;

/**
 * Server data container for this basic runescribing altar GUI.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarBasicContainer extends AbstractRunescribingAltarContainer {
    protected static final int RUNE_CAPACITY = 3;

    public RunescribingAltarBasicContainer(int id, @Nonnull PlayerInventory playerInv) {
        super(ContainersPM.RUNESCRIBING_ALTAR_BASIC.get(), id, playerInv);
    }
    
    @Override
    protected int getRuneCapacity() {
        return RUNE_CAPACITY;
    }
    
    @Override
    protected void addRuneSlots() {
        this.addSlot(new RuneSlot(this.altarInv, 1, 44, 35));
        this.addSlot(new RuneSlot(this.altarInv, 2, 62, 35));
        this.addSlot(new RuneSlot(this.altarInv, 3, 80, 35));
    }
}

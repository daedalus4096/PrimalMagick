package com.verdantartifice.primalmagic.common.containers;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.containers.slots.RuneSlot;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;

/**
 * Server data container for the heavenly runescribing altar GUI.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarHeavenlyContainer extends AbstractRunescribingAltarContainer {
    protected static final int RUNE_CAPACITY = 9;
    
    public RunescribingAltarHeavenlyContainer(int id, @Nonnull PlayerInventory playerInv) {
        super(ContainersPM.RUNESCRIBING_ALTAR_HEAVENLY.get(), id, playerInv);
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
        this.addSlot(new RuneSlot(this.altarInv, 7, 44, 53));
        this.addSlot(new RuneSlot(this.altarInv, 8, 62, 53));
        return this.addSlot(new RuneSlot(this.altarInv, 9, 80, 53));
    }
}

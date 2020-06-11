package com.verdantartifice.primalmagic.common.containers;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.containers.slots.RuneSlot;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;

/**
 * Server data container for the enchanted runescribing altar GUI.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarEnchantedContainer extends AbstractRunescribingAltarContainer {
    protected static final int RUNE_CAPACITY = 5;
    
    public RunescribingAltarEnchantedContainer(int id, @Nonnull PlayerInventory playerInv) {
        super(ContainersPM.RUNESCRIBING_ALTAR_ENCHANTED.get(), id, playerInv);
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

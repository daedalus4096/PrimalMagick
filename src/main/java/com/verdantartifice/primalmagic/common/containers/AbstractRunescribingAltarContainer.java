package com.verdantartifice.primalmagic.common.containers;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Base server data container for the runescribing altar GUIs.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRunescribingAltarContainer extends Container {
    protected final IInventory altarInv;
    protected final World world;

    public AbstractRunescribingAltarContainer(@Nonnull ContainerType<?> type, int id, @Nonnull PlayerInventory playerInv, @Nonnull IInventory altarInv) {
        super(type, id);
        this.altarInv = altarInv;
        this.world = playerInv.player.world;
        
        // Slot 0: runescribing input
        this.addSlot(new Slot(this.altarInv, 0, 19, 35));
        
        // TODO Slot 1: runescribing output
        
        // Slots 2-(R+1), where R = rune capacity: runes
        this.addRuneSlots();
        
        // Slots (R+2)-(R+28), where R = rune capacity: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Slots (R+29)-(R+37), where R = rune capacity: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }
    
    /**
     * Get the number of runes holdable by this container.
     * 
     * @return the number of runes holdable by this container
     */
    protected abstract int getRuneCapacity();
    
    /**
     * Add slots for runes to this container.
     */
    protected abstract void addRuneSlots();
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.altarInv.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        // TODO Auto-generated method stub
        return super.transferStackInSlot(playerIn, index);
    }
}

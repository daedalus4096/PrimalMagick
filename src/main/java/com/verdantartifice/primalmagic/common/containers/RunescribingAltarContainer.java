package com.verdantartifice.primalmagic.common.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Server data container for the runescribing altar GUI.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarContainer extends Container {
    protected final IInventory altarInv;
    protected final World world;
    protected final int maxRunes;

    public RunescribingAltarContainer(int id, PlayerInventory playerInv) {
        this(id, playerInv, new Inventory(11), 9);
    }
    
    public RunescribingAltarContainer(int id, PlayerInventory playerInv, IInventory altarInv, int maxRunes) {
        super(ContainersPM.RUNESCRIBING_ALTAR.get(), id);
        this.altarInv = altarInv;
        this.world = playerInv.player.world;
        this.maxRunes = maxRunes;
        
        // TODO Slot 0: runescribing input
        // TODO Slot 1: runescribing output
        // TODO Slots 2-10: runes
        
        // Slots 11-37: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Slots 38-46: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }
    
    public int getMaxRunes() {
        return this.maxRunes;
    }

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

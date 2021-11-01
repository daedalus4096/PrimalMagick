package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.containers.slots.GenericResultSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandSlot;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Server data container for the dissolution chamber GUI.
 * 
 * @author Daedalus4096
 */
public class DissolutionChamberContainer extends AbstractContainerMenu {
    protected final Container chamberInv;
    protected final ContainerData chamberData;
    protected final Slot inputSlot;
    protected final Slot wandSlot;
    
    public DissolutionChamberContainer(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(3), new SimpleContainerData(4));
    }
    
    public DissolutionChamberContainer(int id, Inventory playerInv, Container chamberInv, ContainerData chamberData) {
        super(ContainersPM.DISSOLUTION_CHAMBER.get(), id);
        checkContainerSize(chamberInv, 3);
        checkContainerDataCount(chamberData, 4);
        this.chamberInv = chamberInv;
        this.chamberData = chamberData;
        
        // Slot 0: ore input
        this.inputSlot = this.addSlot(new Slot(this.chamberInv, 0, 44, 35));
        
        // Slots 1: chamber output
        this.addSlot(new GenericResultSlot(playerInv.player, this.chamberInv, 1, 125, 35));
        
        // Slot 2: wand input
        this.wandSlot = this.addSlot(new WandSlot(this.chamberInv, 2, 8, 62, false));
        
        // Slots 3-29: player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Slots 30-38: player hotbar
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }

        this.addDataSlots(this.chamberData);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.chamberInv.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 1) {
                // If transferring an output item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index >= 3 && index < 30) {
                // If transferring from the player's backpack, put wands in the wand slot and everything else into the input slots or hotbar, in that order
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(slotStack, 0, 1, false) && !this.moveItemStackTo(slotStack, 30, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 30 && index < 39) {
                // If transferring from the player's hotbar, put wands in the wand slot and everything else into the input slots or backpack, in that order
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(slotStack, 0, 1, false) && !this.moveItemStackTo(slotStack, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 3, 39, false)) {
                // Move all other transfers into the backpack or hotbar
                return ItemStack.EMPTY;
            }
            
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            
            slot.setChanged();
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(player, slotStack);
            this.broadcastChanges();
        }
        
        return stack;
    }

    public int getDissolutionProgressionScaled() {
        // Determine how much of the progress arrow to show
        int i = this.chamberData.get(0);
        int j = this.chamberData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
    
    public int getCurrentMana() {
        return this.chamberData.get(2);
    }
    
    public int getMaxMana() {
        return this.chamberData.get(3);
    }
}

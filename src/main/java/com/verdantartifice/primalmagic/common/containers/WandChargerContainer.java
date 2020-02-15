package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.containers.slots.EssenceSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandSlot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Server data container for the wand charger GUI.
 * 
 * @author Daedalus4096
 */
public class WandChargerContainer extends Container {
    protected final IInventory chargerInv;
    protected final IIntArray chargerData;
    protected final Slot essenceSlot;
    protected final Slot wandSlot;
    
    public WandChargerContainer(int id, PlayerInventory playerInv) {
        this(id, playerInv, new Inventory(2), new IntArray(2));
    }
    
    public WandChargerContainer(int id, PlayerInventory playerInv, IInventory chargerInv, IIntArray chargerData) {
        super(ContainersPM.WAND_CHARGER.get(), id);
        assertInventorySize(chargerInv, 2);
        assertIntArraySize(chargerData, 2);
        this.chargerInv = chargerInv;
        this.chargerData = chargerData;
        
        // Slot 0: essence input
        this.essenceSlot = this.addSlot(new EssenceSlot(this.chargerInv, 0, 52, 35));
        
        // Slot 1: wand input/output
        this.wandSlot = this.addSlot(new WandSlot(this.chargerInv, 1, 108, 35));
        
        // Slots 2-28: player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Slots 29-37: player hotbar
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }

        this.trackIntArray(this.chargerData);
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.chargerInv.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (index >= 2 && index < 29) {
                // If transferring from the backpack, move wands and essences to the appropriate slots, and everything else to the hotbar
                if (this.wandSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.essenceSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.mergeItemStack(slotStack, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index >= 29 && index < 38) {
                // If transferring from the hotbar, move wands and essences to the appropriate slots, and everything else to the backpack
                if (this.wandSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.essenceSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.mergeItemStack(slotStack, 2, 29, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(slotStack, 2, 38, false)) {
                // Move all other transfers to the backpack or hotbar
                return ItemStack.EMPTY;
            }
            
            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            ItemStack taken = slot.onTake(playerIn, slotStack);
            if (index == 0) {
                playerIn.dropItem(taken, false);
            }
        }
        return stack;
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getChargeProgressionScaled() {
        // Determine how much of the charge arrow to show
        int i = this.chargerData.get(0);
        int j = this.chargerData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
}

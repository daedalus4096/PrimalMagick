package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.containers.slots.AnalysisResultSlot;
import com.verdantartifice.primalmagic.common.research.ResearchManager;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;

/**
 * Server data container for the analysis table GUI.
 * 
 * @author Daedalus4096
 */
public class AnalysisTableContainer extends Container {
    protected final IInventory analysisInventory = new Inventory(2) {
        public int getInventoryStackLimit() {
            return 1;
        }
    };
    protected final IWorldPosCallable worldPosCallable;
    protected final PlayerEntity player;
    
    public AnalysisTableContainer(int windowId, PlayerInventory inv) {
        this(windowId, inv, IWorldPosCallable.DUMMY);
    }
    
    public AnalysisTableContainer(int windowId, PlayerInventory inv, IWorldPosCallable callable) {
        super(ContainersPM.ANALYSIS_TABLE.get(), windowId);
        this.worldPosCallable = callable;
        this.player = inv.player;
        
        // Slot 0: Item to analyze
        this.addSlot(new Slot(this.analysisInventory, 0, 56, 35));
        
        // Slot 1: Last analyzed item
        this.addSlot(new AnalysisResultSlot(this.analysisInventory, 1, 103, 35));
        
        // Slots 2-28: Player backpack
        int i, j;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Slots 29-37: Player hotbar
        for (i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
        }
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, BlocksPM.ANALYSIS_TABLE.get());
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (index == 0) {
                // If transferring the input item, move it into the player's backpack or hotbar
                if (!this.mergeItemStack(slotStack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 2 && index < 29) {
                // If transferring from the player's backpack, attempt to place it in the input item slot
                if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 29 && index < 38) {
                // If transferring from the player's hotbar, attempt to place it in the input item slot
                if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
        }
        
        return stack;
    }
    
    public ItemStack getLastScannedStack() {
        return this.analysisInventory.getStackInSlot(1);
    }
    
    public void doScan() {
        if (!this.player.world.isRemote && this.player instanceof ServerPlayerEntity) {
            // Move the input item into the recently-scanned slot and mark it as scanned
            ItemStack stack = this.analysisInventory.getStackInSlot(0).copy();
            if (!stack.isEmpty()) {
                this.analysisInventory.setInventorySlotContents(0, ItemStack.EMPTY);
                this.analysisInventory.setInventorySlotContents(1, stack);
                if (!ResearchManager.isScanned(stack, this.player)) {
                    ResearchManager.setScanned(stack, (ServerPlayerEntity)this.player);
                }
            }
        }
    }
}

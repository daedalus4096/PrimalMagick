package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.containers.slots.WandCapSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandCoreSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandGemSlot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;

public class WandAssemblyTableContainer extends Container {
    protected final IWorldPosCallable worldPosCallable;
    public final IInventory componentInv = new WandComponentInventory();    // TODO make protected?
    protected final IInventory resultInv = new WandAssemblyResultInventory();
    protected final Slot coreSlot;
    protected final Slot capSlot;
    protected final Slot gemSlot;
    
    public WandAssemblyTableContainer(int windowId, PlayerInventory inv) {
        this(windowId, inv, IWorldPosCallable.DUMMY);
    }
    
    public WandAssemblyTableContainer(int windowId, PlayerInventory inv, IWorldPosCallable callable) {
        super(ContainersPM.WAND_ASSEMBLY_TABLE, windowId);
        this.worldPosCallable = callable;
        
        // Slot 0: Result
        this.addSlot(new WandAssemblyResultSlot(this.resultInv, 0, 124, 35));
        
        // Slot 1: Wand core
        this.coreSlot = this.addSlot(new WandCoreSlot(this.componentInv, 0, 48, 35));
        
        // Slot 2: Wand gem
        this.gemSlot = this.addSlot(new WandGemSlot(this.componentInv, 1, 48, 17));
        
        // Slots 3-4: Wand caps
        this.capSlot = this.addSlot(new WandCapSlot(this.componentInv, 2, 30, 53));
        this.addSlot(new WandCapSlot(this.componentInv, 3, 66, 17));
        
        // Slots 5-31: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + (i * 9) + 9, 8 + (j * 18), 84 + (i * 18)));
            }
        }
        
        // Slots 32-40: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 8 + (i * 18), 142));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, BlocksPM.WAND_ASSEMBLY_TABLE);
    }
    
    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.worldPosCallable.consume((world, blockPos) -> {
            this.clearContainer(playerIn, world, this.componentInv);
        });
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (index == 0) {
                this.worldPosCallable.consume((world, blockPos) -> {
                    slotStack.getItem().onCreated(slotStack, world, playerIn);
                });
                if (!this.mergeItemStack(slotStack, 11, 47, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, stack);
            } else if (index >= 5 && index < 32) {
                if (this.coreSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.gemSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.capSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 3, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.mergeItemStack(slotStack, 32, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index >= 32 && index < 41) {
                if (this.coreSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.gemSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.capSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 3, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.mergeItemStack(slotStack, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(slotStack, 5, 41, false)) {
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

    protected class WandComponentInventory extends Inventory {
        public WandComponentInventory() {
            super(4);
        }
        
        @Override
        public void markDirty() {
            WandAssemblyTableContainer.this.onCraftMatrixChanged(this);
            super.markDirty();
        }
        
        @Override
        public int getInventoryStackLimit() {
            return 1;
        }
    }
    
    protected class WandAssemblyResultInventory extends CraftResultInventory {
        @Override
        public void markDirty() {
            WandAssemblyTableContainer.this.onCraftMatrixChanged(this);
            super.markDirty();
        }
    }
    
    protected class WandAssemblyResultSlot extends Slot {
        public WandAssemblyResultSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }
    }
}

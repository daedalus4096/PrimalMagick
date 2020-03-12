package com.verdantartifice.primalmagic.common.containers;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.containers.slots.PaperSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WritingImplementSlot;
import com.verdantartifice.primalmagic.common.theorycrafting.IWritingImplement;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;

/**
 * Server data container for the research table GUI.
 * 
 * @author Daedalus4096
 */
public class ResearchTableContainer extends Container implements IInventoryChangedListener {
    protected final IWorldPosCallable worldPosCallable;
    protected final PlayerEntity player;
    protected final Inventory writingInv = new Inventory(2);
    protected final Slot paperSlot;
    protected final Slot pencilSlot;
    protected final IntReferenceHolder writingReady = IntReferenceHolder.single();

    public ResearchTableContainer(int windowId, PlayerInventory inv) {
        this(windowId, inv, IWorldPosCallable.DUMMY);
    }

    public ResearchTableContainer(int windowId, PlayerInventory inv, IWorldPosCallable callable) {
        super(ContainersPM.RESEARCH_TABLE.get(), windowId);
        this.worldPosCallable = callable;
        this.player = inv.player;
        this.writingInv.addListener(this);
        
        // Slot 0: Pencil
        this.pencilSlot = this.addSlot(new WritingImplementSlot(this.writingInv, 0, 8, 8));
        
        // Slot 1: Paper
        this.paperSlot = this.addSlot(new PaperSlot(this.writingInv, 1, 206, 8));
        
        // Slots 2-28: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + (i * 9) + 9, 35 + (j * 18), 140 + (i * 18)));
            }
        }
        
        // Slots 29-37: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 35 + (i * 18), 198));
        }
        
        this.trackInt(this.writingReady).set(0);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, BlocksPM.RESEARCH_TABLE.get());
    }

    @Override
    public void onInventoryChanged(IInventory invBasic) {
        // Set whether the container has writing tools ready; 1 for yes, 0 for no
        boolean ready = (!this.getWritingImplementStack().isEmpty() && !this.getPaperStack().isEmpty());
        this.writingReady.set(ready ? 1 : 0);
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        // Return input pencil and paper to the player's inventory when the GUI is closed
        super.onContainerClosed(playerIn);
        this.worldPosCallable.consume((world, blockPos) -> {
            this.clearContainer(playerIn, world, this.writingInv);
        });
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (index >= 2 && index < 29) {
                // If transferring from the backpack, move paper and writing implements to the appropriate slots, and everything else to the hotbar
                if (this.pencilSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.paperSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.mergeItemStack(slotStack, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index >= 29 && index < 38) {
                // If transferring from the hotbar, move paper and writing implements to the appropriate slots, and everything else to the backpack
                if (this.pencilSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.paperSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 1, 2, false)) {
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
    
    @Nonnull
    protected ItemStack getWritingImplementStack() {
        return this.writingInv.getStackInSlot(0);
    }
    
    @Nonnull
    protected ItemStack getPaperStack() {
        return this.writingInv.getStackInSlot(1);
    }
    
    public boolean isWritingReady() {
        return this.writingReady.get() != 0;
    }
    
    public void consumeWritingImplements() {
        // Don't consume if in creative mode
        if (!this.player.abilities.isCreativeMode) {
            // Consume ink, if applicable
            ItemStack inkStack = this.getWritingImplementStack();
            if (!inkStack.isEmpty() && inkStack.getItem() instanceof IWritingImplement && ((IWritingImplement)inkStack.getItem()).isDamagedOnUse()) {
                inkStack.damageItem(1, this.player, (player) -> {});
            }

            // Consume paper
            this.writingInv.decrStackSize(1, 1);
        }
    }
    
    @Nonnull
    public IWorldPosCallable getWorldPosCallable() {
        return this.worldPosCallable;
    }
}

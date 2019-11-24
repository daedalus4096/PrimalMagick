package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.containers.slots.CalcinatorFuelSlot;
import com.verdantartifice.primalmagic.common.containers.slots.CalcinatorResultSlot;
import com.verdantartifice.primalmagic.common.tiles.crafting.CalcinatorTileEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CalcinatorContainer extends Container {
    protected final IInventory calcinatorInv;
    protected final IIntArray calcinatorData;
    protected final World world;
    
    public CalcinatorContainer(int id, PlayerInventory playerInv) {
        this(id, playerInv, new Inventory(11), new IntArray(4));
    }
    
    public CalcinatorContainer(int id, PlayerInventory playerInv, IInventory calcinatorInv, IIntArray calcinatorData) {
        super(ContainersPM.CALCINATOR, id);
        assertInventorySize(calcinatorInv, 11);
        assertIntArraySize(calcinatorData, 4);
        this.calcinatorInv = calcinatorInv;
        this.calcinatorData = calcinatorData;
        this.world = playerInv.player.world;
        
        // Slot 0: calcinator input
        this.addSlot(new Slot(this.calcinatorInv, 0, 34, 17));
        
        // Slot 1: calcinator fuel
        this.addSlot(new CalcinatorFuelSlot(this, this.calcinatorInv, 1, 34, 53));
        
        // Slots 2-10: calcinator output
        for (int i = 0; i < 9; i++) {
            this.addSlot(new CalcinatorResultSlot(playerInv.player, calcinatorInv, i + 2, 90 + ((i % 3) * 18), 17 + ((i / 3) * 18)));
        }
        
        // Slots 11-37: player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Slots 38-46: player hotbar
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }
        
        this.trackIntArray(this.calcinatorData);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.calcinatorInv.isUsableByPlayer(playerIn);
    }

    public boolean isFuel(ItemStack stack) {
        return CalcinatorTileEntity.isFuel(stack);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            
            if (index >= 2 && index < 11) {
                if (!this.mergeItemStack(slotStack, 11, 47, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, stack);
            } else if (index != 0 && index != 1) {
                if (this.isFuel(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                if (!this.mergeItemStack(slotStack, 11, 47, false)) {
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
            
            slot.onTake(playerIn, slotStack);
        }
        
        return stack;
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = this.calcinatorData.get(2);
        int j = this.calcinatorData.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled() {
        int total = this.calcinatorData.get(1);
        if (total == 0) {
            total = 200;
        }
        return this.calcinatorData.get(0) * 13 / total;
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean isBurning() {
        return this.calcinatorData.get(0) > 0;
    }
}

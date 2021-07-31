package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.containers.slots.CalcinatorFuelSlot;
import com.verdantartifice.primalmagic.common.containers.slots.CalcinatorResultSlot;
import com.verdantartifice.primalmagic.common.tiles.crafting.AbstractCalcinatorTileEntity;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Server data container for the calcinator GUI.
 * 
 * @author Daedalus4096
 */
public class CalcinatorContainer extends AbstractContainerMenu {
    protected final Container calcinatorInv;
    protected final ContainerData calcinatorData;
    protected final Level world;
    
    public CalcinatorContainer(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(11), new SimpleContainerData(4));
    }
    
    public CalcinatorContainer(int id, Inventory playerInv, Container calcinatorInv, ContainerData calcinatorData) {
        super(ContainersPM.CALCINATOR.get(), id);
        checkContainerSize(calcinatorInv, 11);
        checkContainerDataCount(calcinatorData, 4);
        this.calcinatorInv = calcinatorInv;
        this.calcinatorData = calcinatorData;
        this.world = playerInv.player.level;
        
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
        
        this.addDataSlots(this.calcinatorData);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.calcinatorInv.stillValid(playerIn);
    }

    public boolean isFuel(ItemStack stack) {
        return AbstractCalcinatorTileEntity.isFuel(stack);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            
            if (index >= 2 && index < 11) {
                // If transferring calcinator output, move it to the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 11, 47, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index != 0 && index != 1) {
                // If transferring from the backpack or hotbar, move fuel to the fuel slot and anything else to the input slot
                if (this.isFuel(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                // Move all other transfers to the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 11, 47, false)) {
                    return ItemStack.EMPTY;
                }
            }
            
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
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
        // Determine how much of the cook arrow to show
        int i = this.calcinatorData.get(2);
        int j = this.calcinatorData.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled() {
        // Determine how much of the fuel burn timer to show
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

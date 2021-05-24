package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.containers.slots.GenericResultSlot;
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
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Server data container for the concocter GUI.
 * 
 * @author Daedalus4096
 */
public class ConcocterContainer extends Container {
    protected final IInventory concocterInv;
    protected final IIntArray concocterData;
    protected final World world;
    protected final Slot wandSlot;
    
    public ConcocterContainer(int id, PlayerInventory playerInv) {
        this(id, playerInv, new Inventory(11), new IntArray(4));
    }
    
    public ConcocterContainer(int id, PlayerInventory playerInv, IInventory concocterInv, IIntArray concocterData) {
        super(ContainersPM.CONCOCTER.get(), id);
        assertInventorySize(concocterInv, 11);
        assertIntArraySize(concocterData, 4);
        this.concocterInv = concocterInv;
        this.concocterData = concocterData;
        this.world = playerInv.player.world;
        
        // Slots 0-8: Input slots
        int i, j;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                this.addSlot(new Slot(this.concocterInv, j + i * 3, 44 + j * 18, 17 + i * 18));
            }
        }
        
        // Slot 9: Wand slot
        this.wandSlot = this.addSlot(new WandSlot(this.concocterInv, 9, 8, 62, false));
        
        // Slot 10: Output slot
        this.addSlot(new GenericResultSlot(playerInv.player, this.concocterInv, 10, 138, 35));
        
        // Slots 11-37: Player backpack
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Slots 38-46: Player hotbar
        for (i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }
        
        this.trackIntArray(this.concocterData);
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.concocterInv.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (index == 10) {
                // If transferring the output item, move it into the player's backpack or hotbar
                if (!this.mergeItemStack(slotStack, 11, 47, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, stack);
            } else if (index >= 11 && index < 38) {
                // If transferring from the player's backpack, put wands in the wand slot and everything else into the input slots
                if (this.wandSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 9, 10, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.mergeItemStack(slotStack, 0, 9, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index >= 38 && index < 47) {
                // If transferring from the player's hotbar, put wands in the wand slot and everything else into the input slots
                if (this.wandSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 9, 10, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.mergeItemStack(slotStack, 0, 9, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(slotStack, 11, 47, false)) {
                // Move all other transfers into the backpack or hotbar
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
            if (index == 10) {
                playerIn.dropItem(taken, false);
            }
        }
        return stack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        // Determine how much of the cook arrow to show
        int i = this.concocterData.get(0);
        int j = this.concocterData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getCurrentMana() {
        return this.concocterData.get(2);
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getMaxMana() {
        return this.concocterData.get(3);
    }
}

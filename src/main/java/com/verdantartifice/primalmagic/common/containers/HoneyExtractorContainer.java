package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.containers.slots.BottleSlot;
import com.verdantartifice.primalmagic.common.containers.slots.GenericResultSlot;
import com.verdantartifice.primalmagic.common.containers.slots.HoneycombSlot;
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
 * Server data container for the honey extractor GUI.
 * 
 * @author Daedalus4096
 */
public class HoneyExtractorContainer extends Container {
    protected final IInventory extractorInv;
    protected final IIntArray extractorData;
    protected final Slot honeycombSlot;
    protected final Slot bottleSlot;
    protected final Slot wandSlot;

    public HoneyExtractorContainer(int id, PlayerInventory playerInv) {
        this(id, playerInv, new Inventory(5), new IntArray(2));
    }
    
    public HoneyExtractorContainer(int id, PlayerInventory playerInv, IInventory extractorInv, IIntArray extractorData) {
        super(ContainersPM.HONEY_EXTRACTOR.get(), id);
        assertInventorySize(extractorInv, 5);
        assertIntArraySize(extractorData, 2);
        this.extractorInv = extractorInv;
        this.extractorData = extractorData;
        
        // Slot 0: honeycomb input
        this.honeycombSlot = this.addSlot(new HoneycombSlot(this.extractorInv, 0, 30, 35));
        
        // Slot 1: bottle input
        this.bottleSlot = this.addSlot(new BottleSlot(this.extractorInv, 1, 52, 35));
        
        // Slot 2: honey output
        this.addSlot(new GenericResultSlot(playerInv.player, this.extractorInv, 2, 108, 35));
        
        // Slot 3: beeswax output
        this.addSlot(new GenericResultSlot(playerInv.player, this.extractorInv, 3, 130, 35));
        
        // Slot 4: wand input
        this.wandSlot = this.addSlot(new WandSlot(this.extractorInv, 4, 8, 62, false));
        
        // Slots 5-31: player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Slots 32-40: player hotbar
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }

        this.trackIntArray(this.extractorData);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.extractorInv.isUsableByPlayer(playerIn);
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (index == 2 || index == 3) {
                // If transferring an output item, move it into the player's backpack or hotbar
                if (!this.mergeItemStack(slotStack, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, stack);
            } else if (index == 0 || index == 1 || index == 4) {
                // If transferring one of the input items, move it into the player's backpack or hotbar
                if (!this.mergeItemStack(slotStack, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.honeycombSlot.isItemValid(slotStack)) {
                // If transferring a valid ingredient, move it into the appropriate slot
                if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.bottleSlot.isItemValid(slotStack)) {
                // If transferring a valid ingredient, move it into the appropriate slot
                if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.wandSlot.isItemValid(slotStack)) {
                // If transferring a valid wand, move it into the appropriate slot
                if (!this.mergeItemStack(slotStack, 4, 5, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 5 && index < 32) {
                // If transferring from the backpack and not a valid fit, move to the hotbar
                if (!this.mergeItemStack(slotStack, 32, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 32 && index < 41) {
                // If transferring from the hotbar and not a valid fit, move to the backpack
                if (!this.mergeItemStack(slotStack, 5, 32, false)) {
                    return ItemStack.EMPTY;
                }
            }
            
            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            
            slot.onSlotChanged();
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(playerIn, slotStack);
            this.detectAndSendChanges();
        }
        
        return stack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSpinProgressionScaled() {
        // Determine how much of the progress arrow to show
        int i = this.extractorData.get(0);
        int j = this.extractorData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
}

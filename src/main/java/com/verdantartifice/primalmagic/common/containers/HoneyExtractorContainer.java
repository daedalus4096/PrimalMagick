package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.containers.slots.BottleSlot;
import com.verdantartifice.primalmagic.common.containers.slots.GenericResultSlot;
import com.verdantartifice.primalmagic.common.containers.slots.HoneycombSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandSlot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Server data container for the honey extractor GUI.
 * 
 * @author Daedalus4096
 */
public class HoneyExtractorContainer extends AbstractContainerMenu {
    protected final Container extractorInv;
    protected final ContainerData extractorData;
    protected final Slot honeycombSlot;
    protected final Slot bottleSlot;
    protected final Slot wandSlot;

    public HoneyExtractorContainer(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(5), new SimpleContainerData(4));
    }
    
    public HoneyExtractorContainer(int id, Inventory playerInv, Container extractorInv, ContainerData extractorData) {
        super(ContainersPM.HONEY_EXTRACTOR.get(), id);
        checkContainerSize(extractorInv, 5);
        checkContainerDataCount(extractorData, 4);
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

        this.addDataSlots(this.extractorData);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.extractorInv.stillValid(playerIn);
    }
    
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 2 || index == 3) {
                // If transferring an output item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index == 0 || index == 1 || index == 4) {
                // If transferring one of the input items, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.honeycombSlot.mayPlace(slotStack)) {
                // If transferring a valid ingredient, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.bottleSlot.mayPlace(slotStack)) {
                // If transferring a valid ingredient, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.wandSlot.mayPlace(slotStack)) {
                // If transferring a valid wand, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 4, 5, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 5 && index < 32) {
                // If transferring from the backpack and not a valid fit, move to the hotbar
                if (!this.moveItemStackTo(slotStack, 32, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 32 && index < 41) {
                // If transferring from the hotbar and not a valid fit, move to the backpack
                if (!this.moveItemStackTo(slotStack, 5, 32, false)) {
                    return ItemStack.EMPTY;
                }
            }
            
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            
            slot.setChanged();
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(playerIn, slotStack);
            this.broadcastChanges();
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
    
    @OnlyIn(Dist.CLIENT)
    public int getCurrentMana() {
        return this.extractorData.get(2);
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getMaxMana() {
        return this.extractorData.get(3);
    }
}

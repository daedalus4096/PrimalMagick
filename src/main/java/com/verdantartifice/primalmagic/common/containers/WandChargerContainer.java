package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.containers.slots.EssenceSlot;
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
 * Server data container for the wand charger GUI.
 * 
 * @author Daedalus4096
 */
public class WandChargerContainer extends AbstractContainerMenu {
    protected final Container chargerInv;
    protected final ContainerData chargerData;
    protected final Slot essenceSlot;
    protected final Slot wandSlot;
    
    public WandChargerContainer(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(2), new SimpleContainerData(2));
    }
    
    public WandChargerContainer(int id, Inventory playerInv, Container chargerInv, ContainerData chargerData) {
        super(ContainersPM.WAND_CHARGER.get(), id);
        checkContainerSize(chargerInv, 2);
        checkContainerDataCount(chargerData, 2);
        this.chargerInv = chargerInv;
        this.chargerData = chargerData;
        
        // Slot 0: essence input
        this.essenceSlot = this.addSlot(new EssenceSlot(this.chargerInv, 0, 52, 35));
        
        // Slot 1: wand input/output
        this.wandSlot = this.addSlot(new WandSlot(this.chargerInv, 1, 108, 35, true));
        
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

        this.addDataSlots(this.chargerData);
    }
    
    @Override
    public boolean stillValid(Player playerIn) {
        return this.chargerInv.stillValid(playerIn);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index >= 2 && index < 29) {
                // If transferring from the backpack, move wands and essences to the appropriate slots, and everything else to the hotbar
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.essenceSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index >= 29 && index < 38) {
                // If transferring from the hotbar, move wands and essences to the appropriate slots, and everything else to the backpack
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.essenceSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 2, 29, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(slotStack, 2, 38, false)) {
                // Move all other transfers to the backpack or hotbar
                return ItemStack.EMPTY;
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
    public int getChargeProgressionScaled() {
        // Determine how much of the charge arrow to show
        int i = this.chargerData.get(0);
        int j = this.chargerData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
}

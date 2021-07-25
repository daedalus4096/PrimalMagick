package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.containers.slots.GenericResultSlot;
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
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Server data container for the concocter GUI.
 * 
 * @author Daedalus4096
 */
public class ConcocterContainer extends AbstractContainerMenu {
    protected final Container concocterInv;
    protected final ContainerData concocterData;
    protected final Level world;
    protected final Slot wandSlot;
    
    public ConcocterContainer(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(11), new SimpleContainerData(4));
    }
    
    public ConcocterContainer(int id, Inventory playerInv, Container concocterInv, ContainerData concocterData) {
        super(ContainersPM.CONCOCTER.get(), id);
        checkContainerSize(concocterInv, 11);
        checkContainerDataCount(concocterData, 4);
        this.concocterInv = concocterInv;
        this.concocterData = concocterData;
        this.world = playerInv.player.level;
        
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
        
        this.addDataSlots(this.concocterData);
    }
    
    @Override
    public boolean stillValid(Player playerIn) {
        return this.concocterInv.stillValid(playerIn);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 10) {
                // If transferring the output item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 11, 47, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index >= 11 && index < 38) {
                // If transferring from the player's backpack, put wands in the wand slot and everything else into the input slots
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 9, 10, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 0, 9, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index >= 38 && index < 47) {
                // If transferring from the player's hotbar, put wands in the wand slot and everything else into the input slots
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 9, 10, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 0, 9, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(slotStack, 11, 47, false)) {
                // Move all other transfers into the backpack or hotbar
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
            
            ItemStack taken = slot.onTake(playerIn, slotStack);
            if (index == 10) {
                playerIn.drop(taken, false);
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

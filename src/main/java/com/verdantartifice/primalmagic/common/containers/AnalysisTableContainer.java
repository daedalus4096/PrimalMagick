package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.containers.slots.AnalysisResultSlot;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.misc.ScanItemPacket;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;

public class AnalysisTableContainer extends Container {
    protected final IInventory analysisInventory = new Inventory(2);
    protected final IWorldPosCallable worldPosCallable;
    protected final PlayerEntity player;
    
    public AnalysisTableContainer(int windowId, PlayerInventory inv) {
        this(windowId, inv, IWorldPosCallable.DUMMY);
    }
    
    public AnalysisTableContainer(int windowId, PlayerInventory inv, IWorldPosCallable callable) {
        super(ContainersPM.ANALYSIS_TABLE, windowId);
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
        return isWithinUsableDistance(this.worldPosCallable, playerIn, BlocksPM.ANALYSIS_TABLE);
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        //Slot slot = this.inventorySlots.get(index);
        
        // TODO
        
        return stack;
    }
    
    public ItemStack getLastScannedStack() {
        return this.analysisInventory.getStackInSlot(1);
    }
    
    public void doScan() {
        ItemStack stack = this.analysisInventory.getStackInSlot(0).copy();
        stack.setCount(1);
        this.analysisInventory.setInventorySlotContents(0, ItemStack.EMPTY);
        this.analysisInventory.setInventorySlotContents(1, stack);
        PacketHandler.sendToServer(new ScanItemPacket(stack));
    }
}

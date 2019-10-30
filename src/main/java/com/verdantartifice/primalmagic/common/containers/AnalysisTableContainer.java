package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.containers.slots.AnalysisResultSlot;

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

public class AnalysisTableContainer extends Container {
    protected final IInventory analysisInventory;
    protected final IIntArray analysisData;
    
    public AnalysisTableContainer(int windowId, PlayerInventory inv) {
        this(windowId, inv, new Inventory(2), new IntArray(2));
    }
    
    public AnalysisTableContainer(int windowId, PlayerInventory inv, IInventory analysisInventory, IIntArray analysisData) {
        super(ContainersPM.ANALYSIS_TABLE, windowId);
        this.analysisInventory = analysisInventory;
        this.analysisData = analysisData;
        
        // Slot 0: Item to analyze
        this.addSlot(new Slot(this.analysisInventory, 0, 56, 35));
        
        // Slot 1: Last analyzed item
        this.addSlot(new AnalysisResultSlot(this.analysisInventory, 1, 116, 35));
        
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

        this.trackIntArray(this.analysisData);
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.analysisInventory.isUsableByPlayer(playerIn);
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        //Slot slot = this.inventorySlots.get(index);
        
        // TODO
        
        return stack;
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getAnalysisProgressionScaled() {
        int i = this.analysisData.get(0);
        int j = this.analysisData.get(1);
        return (i != 0 && j != 0) ? (i * 24 / j) : 0;
    }
    
    public ItemStack getLastScannedStack() {
        return this.analysisInventory.getStackInSlot(1);
    }
}

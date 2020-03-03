package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.containers.slots.PaperSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WritingImplementSlot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;

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
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, BlocksPM.RESEARCH_TABLE.get());
    }

    @Override
    public void onInventoryChanged(IInventory invBasic) {
        // TODO Auto-generated method stub
        
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
        // TODO Auto-generated method stub
        return super.transferStackInSlot(playerIn, index);
    }
}

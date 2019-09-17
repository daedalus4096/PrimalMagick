package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IWorldPosCallable;

public class ArcaneWorkbenchContainer extends Container {
    protected final CraftingInventory craftingInv = new CraftingInventory(this, 3, 3);
    protected final CraftResultInventory resultInv = new CraftResultInventory();
    protected final IWorldPosCallable worldPosCallable;
    protected final PlayerEntity player;
    
    public ArcaneWorkbenchContainer(int windowId, PlayerInventory inv) {
        this(windowId, inv, IWorldPosCallable.DUMMY);
    }
    
    public ArcaneWorkbenchContainer(int windowId, PlayerInventory inv, IWorldPosCallable callable) {
        super(ContainersPM.ARCANE_WORKBENCH, windowId);
        this.worldPosCallable = callable;
        this.player = inv.player;
        
        this.addSlot(new CraftingResultSlot(this.player, this.craftingInv, this.resultInv, 0, 124, 35));
        
        int i, j;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                this.addSlot(new Slot(this.craftingInv, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
        }
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, BlocksPM.ARCANE_WORKBENCH);
    }
}

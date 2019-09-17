package com.verdantartifice.primalmagic.common.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;

public class ArcaneWorkbenchContainer extends Container {
    protected final CraftingInventory craftingInv = new CraftingInventory(this, 3, 3);
    protected final PlayerEntity player;
    
    public ArcaneWorkbenchContainer(int windowId, PlayerInventory inv) {
        super(ContainersPM.ARCANE_WORKBENCH, windowId);
        this.player = inv.player;
        
        // TODO add output slot
        
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
        // TODO method stub
        return true;
    }
}

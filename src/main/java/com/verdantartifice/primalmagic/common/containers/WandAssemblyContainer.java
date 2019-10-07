package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.containers.slots.WandCapSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandCoreSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandGemSlot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;

public class WandAssemblyContainer extends Container {
    protected final IWorldPosCallable worldPosCallable;
    public final IInventory componentInv = new WandComponentInventory();    // TODO make protected?
    protected final IInventory resultInv = new WandAssemblyResultInventory();
    
    public WandAssemblyContainer(int windowId, PlayerInventory inv) {
        this(windowId, inv, IWorldPosCallable.DUMMY);
    }
    
    public WandAssemblyContainer(int windowId, PlayerInventory inv, IWorldPosCallable callable) {
        super(ContainersPM.WAND_ASSEMBLY, windowId);
        this.worldPosCallable = callable;
        
        // Slot 0: Result
        this.addSlot(new WandAssemblyResultSlot(this.resultInv, 0, 124, 35));
        
        // Slot 1: Wand core
        this.addSlot(new WandCoreSlot(this.componentInv, 0, 48, 35));
        
        // Slot 2: Wand gem
        this.addSlot(new WandGemSlot(this.componentInv, 1, 48, 17));
        
        // Slots 3-4: Wand caps
        this.addSlot(new WandCapSlot(this.componentInv, 2, 30, 53));
        this.addSlot(new WandCapSlot(this.componentInv, 3, 66, 17));
        
        // Slots 5-31: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + (i * 9) + 9, 8 + (j * 18), 84 + (i * 18)));
            }
        }
        
        // Slots 32-40: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 8 + (i * 18), 142));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        // TODO Auto-generated method stub
        return true;
    }

    protected class WandComponentInventory extends Inventory {
        public WandComponentInventory() {
            super(4);
        }
        
        @Override
        public void markDirty() {
            WandAssemblyContainer.this.onCraftMatrixChanged(this);
            super.markDirty();
        }
    }
    
    protected class WandAssemblyResultInventory extends CraftResultInventory {
        @Override
        public void markDirty() {
            WandAssemblyContainer.this.onCraftMatrixChanged(this);
            super.markDirty();
        }
    }
    
    protected class WandAssemblyResultSlot extends Slot {
        public WandAssemblyResultSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }
    }
}

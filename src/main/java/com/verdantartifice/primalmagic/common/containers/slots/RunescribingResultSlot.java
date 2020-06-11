package com.verdantartifice.primalmagic.common.containers.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Custom GUI slot for runescribing results.
 * 
 * @author Daedalus4096
 */
public class RunescribingResultSlot extends Slot {
    protected final PlayerEntity player;
    protected final IInventory inputInventory;

    public RunescribingResultSlot(PlayerEntity player, IInventory inputInv, IInventory outputInv, int index, int xPosition, int yPosition) {
        super(outputInv, index, xPosition, yPosition);
        this.player = player;
        this.inputInventory = inputInv;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }
    
    @Override
    protected void onCrafting(ItemStack stack) {
        // TODO Increment the player's runescribing stat
        super.onCrafting(stack);
    }
    
    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        // Handle crafting side effects
        this.onCrafting(stack);
        
        // Consume inputs
        for (int index = 0; index < this.inputInventory.getSizeInventory(); index++) {
            ItemStack materialStack = this.inputInventory.getStackInSlot(index);
            if (!materialStack.isEmpty()) {
                this.inputInventory.decrStackSize(index, 1);
            }
        }
        
        return stack;
    }
}

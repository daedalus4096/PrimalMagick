package com.verdantartifice.primalmagic.common.containers.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Custom GUI slot for calcinator outputs.
 * 
 * @author Daedalus4096
 */
public class CalcinatorResultSlot extends Slot {
    protected final PlayerEntity player;
    protected int removeCount = 0;
    
    public CalcinatorResultSlot(PlayerEntity player, IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.player = player;
    }
    
    @Override
    public boolean isItemValid(ItemStack stack) {
        // Don't allow anything to be dropped into the slot
        return false;
    }
    
    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.removeCount += Math.min(amount, this.getStack().getCount());
        }
        return super.decrStackSize(amount);
    }
    
    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        this.onCrafting(stack);
        return super.onTake(thePlayer, stack);
    }
    
    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.onCrafting(stack);
    }
    
    @Override
    protected void onCrafting(ItemStack stack) {
        stack.onCrafting(this.player.world, this.player, this.removeCount);
        this.removeCount = 0;
    }
}

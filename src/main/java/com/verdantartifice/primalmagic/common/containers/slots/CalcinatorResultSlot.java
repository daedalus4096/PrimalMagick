package com.verdantartifice.primalmagic.common.containers.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.hooks.BasicEventHooks;

/**
 * Custom GUI slot for calcinator outputs.
 * 
 * @author Daedalus4096
 */
public class CalcinatorResultSlot extends GenericResultSlot {
    public CalcinatorResultSlot(PlayerEntity player, IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(player, inventoryIn, index, xPosition, yPosition);
    }
    
    @Override
    protected void onCrafting(ItemStack stack) {
        super.onCrafting(stack);
        BasicEventHooks.firePlayerSmeltedEvent(this.player, stack);
    }
}

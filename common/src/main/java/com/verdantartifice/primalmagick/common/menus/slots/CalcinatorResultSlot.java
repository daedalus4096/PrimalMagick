package com.verdantartifice.primalmagick.common.menus.slots;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.IItemHandler;

/**
 * Custom GUI slot for calcinator outputs.
 * 
 * @author Daedalus4096
 */
public class CalcinatorResultSlot extends GenericResultSlot {
    public CalcinatorResultSlot(Player player, IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(player, inventoryIn, index, xPosition, yPosition);
    }
    
    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        super.checkTakeAchievements(stack);
        ForgeEventFactory.firePlayerSmeltedEvent(this.player, stack);
    }
}

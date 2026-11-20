package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

/**
 * Custom GUI slot for calcinator outputs.
 * 
 * @author Daedalus4096
 */
public class CalcinatorResultSlotNeoforge extends GenericResultSlotNeoforge {
    public CalcinatorResultSlotNeoforge(Player player, ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(player, handler, slotModifier, index, xPosition, yPosition);
    }
    
    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        super.checkTakeAchievements(stack);
        Services.EVENTS.firePlayerSmeltedEvent(this.player, stack, this.removeCount);
    }
}

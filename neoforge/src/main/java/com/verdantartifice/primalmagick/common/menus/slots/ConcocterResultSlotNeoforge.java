package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class ConcocterResultSlotNeoforge extends GenericResultSlotNeoforge {
    public ConcocterResultSlotNeoforge(Player player, ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(player, handler, slotModifier, index, xPosition, yPosition);
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        super.checkTakeAchievements(stack);
        StatsManager.incrementValue(this.player, StatsPM.CRAFTED_MAGITECH, stack.getCount());
    }
}

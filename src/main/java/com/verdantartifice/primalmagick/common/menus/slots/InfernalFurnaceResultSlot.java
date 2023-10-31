package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.common.tiles.devices.InfernalFurnaceTileEntity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.IItemHandler;

/**
 * Custom GUI slot for infernal furnace outputs.
 * 
 * @author Daedalus4096
 */
public class InfernalFurnaceResultSlot extends GenericResultSlot {
    public InfernalFurnaceResultSlot(Player pPlayer, IItemHandler itemHandler, int pSlot, int pXPosition, int pYPosition) {
        super(pPlayer, itemHandler, pSlot, pXPosition, pYPosition);
    }

    @Override
    protected void checkTakeAchievements(ItemStack pStack) {
        super.checkTakeAchievements(pStack);
        if (this.player instanceof ServerPlayer serverPlayer && this.container instanceof InfernalFurnaceTileEntity furnaceEntity) {
            furnaceEntity.awardUsedRecipesAndPopExperience(serverPlayer);
        }
        ForgeEventFactory.firePlayerSmeltedEvent(this.player, pStack);
    }
}

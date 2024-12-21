package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.menus.CalcinatorMenu;
import com.verdantartifice.primalmagick.common.menus.RunecarvingTableMenu;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public interface IMenuService {
    Slot makeSlot(IItemHandlerPM itemHandler, int slot, int x, int y);
    Slot makeGenericResultSlot(Player player, IItemHandlerPM itemHandler, int slot, int x, int y);
    Slot makeFilteredSlot(IItemHandlerPM itemHandler, int slot, int x, int y, FilteredSlotProperties properties);
    Slot makeWandSlot(IItemHandlerPM itemHandler, int slot, int x, int y, boolean allowStaves);
    Slot makeCalcinatorFuelSlot(CalcinatorMenu menu, IItemHandlerPM itemHandler, int slot, int x, int y);
    Slot makeCalcinatorResultSlot(Player player, IItemHandlerPM itemHandler, int slot, int x, int y);
    Slot makeInfernalFurnaceResultSlot(Player player, IItemHandlerPM itemHandler, int slot, int x, int y);
    Slot makeConcocterResultSlot(Player player, IItemHandlerPM itemHandler, int slot, int x, int y);
    Slot makeRunecarvingResultSlot(RunecarvingTableMenu menu, Player player, IItemHandlerPM itemHandler, int slot, int x, int y);
}

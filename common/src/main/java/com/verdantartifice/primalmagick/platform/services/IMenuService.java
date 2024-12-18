package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.menus.CalcinatorMenu;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
import net.minecraft.world.inventory.Slot;

public interface IMenuService {
    Slot makeFilteredSlot(IItemHandlerPM itemHandler, int slot, int x, int y, FilteredSlotProperties properties);
    Slot makeWandSlot(IItemHandlerPM itemHandler, int slot, int x, int y, boolean allowStaves);
    Slot makeCalcinatorFuelSlot(CalcinatorMenu menu, IItemHandlerPM itemHandler, int slot, int x, int y);
}

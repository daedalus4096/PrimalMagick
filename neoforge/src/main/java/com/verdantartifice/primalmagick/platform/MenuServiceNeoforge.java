package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.menus.CalcinatorMenu;
import com.verdantartifice.primalmagick.common.menus.slots.CalcinatorFuelSlotNeoforge;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotNeoforge;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
import com.verdantartifice.primalmagick.common.menus.slots.WandSlotNeoforge;
import com.verdantartifice.primalmagick.platform.services.IMenuService;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.items.IItemHandler;

public class MenuServiceNeoforge implements IMenuService {
    @Override
    public Slot makeFilteredSlot(IItemHandlerPM itemHandler, int slot, int x, int y, FilteredSlotProperties properties) {
        if (itemHandler instanceof IItemHandler neoforgeHandler) {
            return new FilteredSlotNeoforge(neoforgeHandler, slot, x, y, properties);
        } else {
            throw new IllegalArgumentException("itemHandler is not an instance of IItemHandler");
        }
    }

    @Override
    public Slot makeWandSlot(IItemHandlerPM itemHandler, int slot, int x, int y, boolean allowStaves) {
        if (itemHandler instanceof IItemHandler neoforgeHandler) {
            return new WandSlotNeoforge(neoforgeHandler, slot, x, y, allowStaves);
        } else {
            throw new IllegalArgumentException("itemHandler is not an instance of IItemHandler");
        }
    }

    @Override
    public Slot makeCalcinatorFuelSlot(CalcinatorMenu menu, IItemHandlerPM itemHandler, int slot, int x, int y) {
        if (itemHandler instanceof IItemHandler neoforgeHandler) {
            return new CalcinatorFuelSlotNeoforge(menu, neoforgeHandler, slot, x, y);
        } else {
            throw new IllegalArgumentException("itemHandler is not an instance of IItemHandler");
        }
    }
}

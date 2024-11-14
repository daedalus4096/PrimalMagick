package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotNeoforge;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
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
}

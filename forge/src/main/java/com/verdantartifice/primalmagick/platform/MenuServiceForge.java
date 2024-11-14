package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotForge;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
import com.verdantartifice.primalmagick.platform.services.IMenuService;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.IItemHandler;

public class MenuServiceForge implements IMenuService {
    @Override
    public Slot makeFilteredSlot(IItemHandlerPM itemHandler, int slot, int x, int y, FilteredSlotProperties properties) {
        if (itemHandler instanceof IItemHandler forgeHandler) {
            return new FilteredSlotForge(forgeHandler, slot, x, y, properties);
        } else {
            throw new IllegalArgumentException("itemHandler must be an instance of IItemHandler");
        }
    }
}

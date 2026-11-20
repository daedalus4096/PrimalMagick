package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.inventory.IItemHandlerPMNeoforge;
import com.verdantartifice.primalmagick.common.menus.CalcinatorMenu;
import com.verdantartifice.primalmagick.common.menus.RunecarvingTableMenu;
import com.verdantartifice.primalmagick.common.menus.slots.CalcinatorFuelSlotNeoforge;
import com.verdantartifice.primalmagick.common.menus.slots.CalcinatorResultSlotNeoforge;
import com.verdantartifice.primalmagick.common.menus.slots.ConcocterResultSlotNeoforge;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotNeoforge;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
import com.verdantartifice.primalmagick.common.menus.slots.GenericResultSlotNeoforge;
import com.verdantartifice.primalmagick.common.menus.slots.InfernalFurnaceResultSlotNeoforge;
import com.verdantartifice.primalmagick.common.menus.slots.RunecarvingResultSlotNeoforge;
import com.verdantartifice.primalmagick.common.menus.slots.WandSlotNeoforge;
import com.verdantartifice.primalmagick.platform.services.IMenuService;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class MenuServiceNeoforge implements IMenuService {
    @Override
    public Slot makeSlot(IItemHandlerPM itemHandler, int slot, int x, int y) {
        if (itemHandler instanceof IItemHandlerPMNeoforge neoforgeHandler) {
            return new ResourceHandlerSlot(neoforgeHandler, neoforgeHandler, slot, x, y);
        } else {
            throw new IllegalArgumentException("itemHandler is not an instance of IItemHandlerPMNeoforge");
        }
    }

    @Override
    public Slot makeGenericResultSlot(Player player, IItemHandlerPM itemHandler, int slot, int x, int y) {
        if (itemHandler instanceof IItemHandlerPMNeoforge neoforgeHandler) {
            return new GenericResultSlotNeoforge(player, neoforgeHandler, neoforgeHandler, slot, x, y);
        } else {
            throw new IllegalArgumentException("itemHandler is not an instance of IItemHandlerPMNeoforge");
        }
    }

    @Override
    public Slot makeFilteredSlot(IItemHandlerPM itemHandler, int slot, int x, int y, FilteredSlotProperties properties) {
        if (itemHandler instanceof IItemHandlerPMNeoforge neoforgeHandler) {
            return new FilteredSlotNeoforge(neoforgeHandler, neoforgeHandler, slot, x, y, properties);
        } else {
            throw new IllegalArgumentException("itemHandler is not an instance of IItemHandlerPMNeoforge");
        }
    }

    @Override
    public Slot makeWandSlot(IItemHandlerPM itemHandler, int slot, int x, int y, boolean allowStaves) {
        if (itemHandler instanceof IItemHandlerPMNeoforge neoforgeHandler) {
            return new WandSlotNeoforge(neoforgeHandler, neoforgeHandler, slot, x, y, allowStaves);
        } else {
            throw new IllegalArgumentException("itemHandler is not an instance of IItemHandlerPMNeoforge");
        }
    }

    @Override
    public Slot makeCalcinatorFuelSlot(CalcinatorMenu menu, IItemHandlerPM itemHandler, int slot, int x, int y) {
        if (itemHandler instanceof IItemHandlerPMNeoforge neoforgeHandler) {
            return new CalcinatorFuelSlotNeoforge(menu, neoforgeHandler, neoforgeHandler, slot, x, y);
        } else {
            throw new IllegalArgumentException("itemHandler is not an instance of IItemHandlerPMNeoforge");
        }
    }

    @Override
    public Slot makeCalcinatorResultSlot(Player player, IItemHandlerPM itemHandler, int slot, int x, int y) {
        if (itemHandler instanceof IItemHandlerPMNeoforge neoforgeHandler) {
            return new CalcinatorResultSlotNeoforge(player, neoforgeHandler, neoforgeHandler, slot, x, y);
        } else {
            throw new IllegalArgumentException("itemHandler is not an instance of IItemHandlerPMNeoforge");
        }
    }

    @Override
    public Slot makeInfernalFurnaceResultSlot(Player player, IItemHandlerPM itemHandler, int slot, int x, int y) {
        if (itemHandler instanceof IItemHandlerPMNeoforge neoforgeHandler) {
            return new InfernalFurnaceResultSlotNeoforge(player, neoforgeHandler, neoforgeHandler, slot, x, y);
        } else {
            throw new IllegalArgumentException("itemHandler is not an instance of IItemHandlerPMNeoforge");
        }
    }

    @Override
    public Slot makeConcocterResultSlot(Player player, IItemHandlerPM itemHandler, int slot, int x, int y) {
        if (itemHandler instanceof IItemHandlerPMNeoforge neoforgeHandler) {
            return new ConcocterResultSlotNeoforge(player, neoforgeHandler, neoforgeHandler, slot, x, y);
        } else {
            throw new IllegalArgumentException("itemHandler is not an instance of IItemHandlerPMNeoforge");
        }
    }

    @Override
    public Slot makeRunecarvingResultSlot(RunecarvingTableMenu menu, Player player, IItemHandlerPM itemHandler, int slot, int x, int y) {
        if (itemHandler instanceof IItemHandlerPMNeoforge neoforgeHandler) {
            return new RunecarvingResultSlotNeoforge(menu, player, neoforgeHandler, neoforgeHandler, slot, x, y);
        } else {
            throw new IllegalArgumentException("itemHandler is not an instance of IItemHandlerPMNeoforge");
        }
    }
}

package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.menus.CalcinatorMenu;
import com.verdantartifice.primalmagick.common.menus.RunecarvingTableMenu;
import com.verdantartifice.primalmagick.common.menus.slots.CalcinatorFuelSlotForge;
import com.verdantartifice.primalmagick.common.menus.slots.CalcinatorResultSlotForge;
import com.verdantartifice.primalmagick.common.menus.slots.ConcocterResultSlotForge;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotForge;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
import com.verdantartifice.primalmagick.common.menus.slots.GenericResultSlotForge;
import com.verdantartifice.primalmagick.common.menus.slots.InfernalFurnaceResultSlotForge;
import com.verdantartifice.primalmagick.common.menus.slots.RunecarvingResultSlotForge;
import com.verdantartifice.primalmagick.common.menus.slots.WandSlotForge;
import com.verdantartifice.primalmagick.platform.services.IMenuService;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class MenuServiceForge implements IMenuService {
    @Override
    public Slot makeSlot(IItemHandlerPM itemHandler, int slot, int x, int y) {
        if (itemHandler instanceof IItemHandler forgeHandler) {
            return new SlotItemHandler(forgeHandler, slot, x, y);
        } else {
            throw new IllegalArgumentException("itemHandler must be an instance of IItemHandler");
        }
    }

    @Override
    public Slot makeGenericResultSlot(Player player, IItemHandlerPM itemHandler, int slot, int x, int y) {
        if (itemHandler instanceof IItemHandler forgeHandler) {
            return new GenericResultSlotForge(player, forgeHandler, slot, x, y);
        } else {
            throw new IllegalArgumentException("itemHandler must be an instance of IItemHandler");
        }
    }

    @Override
    public Slot makeFilteredSlot(IItemHandlerPM itemHandler, int slot, int x, int y, FilteredSlotProperties properties) {
        if (itemHandler instanceof IItemHandler forgeHandler) {
            return new FilteredSlotForge(forgeHandler, slot, x, y, properties);
        } else {
            throw new IllegalArgumentException("itemHandler must be an instance of IItemHandler");
        }
    }

    @Override
    public Slot makeWandSlot(IItemHandlerPM itemHandler, int slot, int x, int y, boolean allowStaves) {
        if (itemHandler instanceof IItemHandler forgeHandler) {
            return new WandSlotForge(forgeHandler, slot, x, y, allowStaves);
        } else {
            throw new IllegalArgumentException("itemHandler must be an instance of IItemHandler");
        }
    }

    @Override
    public Slot makeCalcinatorFuelSlot(CalcinatorMenu menu, IItemHandlerPM itemHandler, int slot, int x, int y) {
        if (itemHandler instanceof IItemHandler forgeHandler) {
            return new CalcinatorFuelSlotForge(menu, forgeHandler, slot, x, y);
        } else {
            throw new IllegalArgumentException("itemHandler must be an instance of IItemHandler");
        }
    }

    @Override
    public Slot makeCalcinatorResultSlot(Player player, IItemHandlerPM itemHandler, int slot, int x, int y) {
        if (itemHandler instanceof IItemHandler forgeHandler) {
            return new CalcinatorResultSlotForge(player, forgeHandler, slot, x, y);
        } else {
            throw new IllegalArgumentException("itemHandler must be an instance of IItemHandler");
        }
    }

    @Override
    public Slot makeInfernalFurnaceResultSlot(Player player, IItemHandlerPM itemHandler, int slot, int x, int y) {
        if (itemHandler instanceof IItemHandler forgeHandler) {
            return new InfernalFurnaceResultSlotForge(player, forgeHandler, slot, x, y);
        } else {
            throw new IllegalArgumentException("itemHandler must be an instance of IItemHandler");
        }
    }

    @Override
    public Slot makeConcocterResultSlot(Player player, IItemHandlerPM itemHandler, int slot, int x, int y) {
        if (itemHandler instanceof IItemHandler forgeHandler) {
            return new ConcocterResultSlotForge(player, forgeHandler, slot, x, y);
        } else {
            throw new IllegalArgumentException("itemHandler must be an instance of IItemHandler");
        }
    }

    @Override
    public Slot makeRunecarvingResultSlot(RunecarvingTableMenu menu, Player player, IItemHandlerPM itemHandler, int slot, int x, int y) {
        if (itemHandler instanceof IItemHandler forgeHandler) {
            return new RunecarvingResultSlotForge(menu, player, forgeHandler, slot, x, y);
        } else {
            throw new IllegalArgumentException("itemHandler must be an instance of IItemHandler");
        }
    }
}

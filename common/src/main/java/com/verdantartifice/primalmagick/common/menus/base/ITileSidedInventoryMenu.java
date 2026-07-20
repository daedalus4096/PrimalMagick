package com.verdantartifice.primalmagick.common.menus.base;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import net.minecraft.core.Direction;

public interface ITileSidedInventoryMenu<T extends AbstractTileSidedInventoryPM> extends ITileMenu<T> {
    IItemHandlerPM getTileInventory(Direction face);

    IItemHandlerPM getTileInventory(int index);
}

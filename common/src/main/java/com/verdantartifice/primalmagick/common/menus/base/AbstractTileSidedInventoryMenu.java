package com.verdantartifice.primalmagick.common.menus.base;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;

/**
 * Base class for a menu that serves a mod block entity with an attached sided item handler.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTileSidedInventoryMenu<T extends AbstractTileSidedInventoryPM> extends AbstractTileMenu<T> {
    protected AbstractTileSidedInventoryMenu(MenuType<?> menuType, int containerId, Class<T> tileClass, Level level, BlockPos tilePos, T tile) {
        super(menuType, containerId, tileClass, level, tilePos, tile);
    }
    
    public IItemHandlerPM getTileInventory(Direction face) {
        return Services.CAPABILITIES.itemHandler(this.tile, face).orElseThrow(IllegalStateException::new);
    }

    public IItemHandlerPM getTileInventory(int index) {
        IItemHandlerPM retVal = this.tile.getRawItemHandler(index);
        if (retVal == null) {
            throw new IllegalStateException("No tile inventory found for index " + index);
        } else {
            return retVal;
        }
    }
}

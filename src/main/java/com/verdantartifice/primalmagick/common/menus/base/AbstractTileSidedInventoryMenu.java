package com.verdantartifice.primalmagick.common.menus.base;

import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

/**
 * Base class for a menu that serves a mod block entity with an attached sided item handler.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTileSidedInventoryMenu<T extends AbstractTileSidedInventoryPM> extends AbstractTileMenu<T> {
    protected AbstractTileSidedInventoryMenu(MenuType<?> menuType, int containerId, Class<T> tileClass, Level level, BlockPos tilePos, T tile) {
        super(menuType, containerId, tileClass, level, tilePos, tile);
    }
    
    public IItemHandler getTileInventory(Direction face) {
        return this.tile.getCapability(ForgeCapabilities.ITEM_HANDLER, face).orElseThrow(IllegalStateException::new);
    }
}

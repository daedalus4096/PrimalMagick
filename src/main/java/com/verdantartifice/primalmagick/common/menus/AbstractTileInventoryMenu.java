package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.tiles.base.TileInventoryPM;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Base class for a menu that serves a mod block entity with an attached item handler.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTileInventoryMenu<T extends TileInventoryPM> extends AbstractTileMenu<T> {
    protected final IItemHandler tileInv;
    
    protected AbstractTileInventoryMenu(MenuType<?> menuType, int containerId, Class<T> tileClass, Level level, BlockPos tilePos, T tile) {
        super(menuType, containerId, tileClass, level, tilePos, tile);
        this.tileInv = this.tile == null ? new ItemStackHandler(this.tile.getInventorySize()) : this.tile.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(IllegalStateException::new);
    }
    
    public IItemHandler getTileInventory() {
        return this.tileInv;
    }
}

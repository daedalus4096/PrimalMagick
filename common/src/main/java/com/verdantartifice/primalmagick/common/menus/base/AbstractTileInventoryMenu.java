package com.verdantartifice.primalmagick.common.menus.base;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileInventoryPM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

/**
 * Base class for a menu that serves a mod block entity with an attached item handler.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTileInventoryMenu<T extends AbstractTileInventoryPM> extends AbstractTileMenu<T> {
    protected final IItemHandlerPM tileInv;
    
    protected AbstractTileInventoryMenu(MenuType<?> menuType, int containerId, Class<T> tileClass, Level level, BlockPos tilePos, T tile) {
        super(menuType, containerId, tileClass, level, tilePos, tile);
        this.tileInv = this.tile == null ? Services.ITEM_HANDLERS.create(this.tile) : this.tile.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(IllegalStateException::new);
    }
    
    public IItemHandlerPM getTileInventory() {
        return this.tileInv;
    }
}

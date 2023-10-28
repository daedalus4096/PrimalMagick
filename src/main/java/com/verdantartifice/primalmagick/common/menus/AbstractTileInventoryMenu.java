package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.tiles.base.TileInventoryPM;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
public abstract class AbstractTileInventoryMenu<T extends TileInventoryPM> extends AbstractContainerMenu {
    protected final T tile;
    protected final IItemHandler tileInv;
    protected final Level level;
    
    protected AbstractTileInventoryMenu(MenuType<?> menuType, int containerId, T tile) {
        super(menuType, containerId);
        this.tile = tile;
        this.tileInv = this.tile == null ? new ItemStackHandler(this.tile.getInventorySize()) : this.tile.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(IllegalStateException::new);
        this.level = this.tile.getLevel();
    }
    
    public T getTile() {
        return this.tile;
    }
    
    public IItemHandler getTileInventory() {
        return this.tileInv;
    }
    
    public Level getLevel() {
        return this.level;
    }
    
    @Override
    public boolean stillValid(Player pPlayer) {
        return this.tile == null ? false : this.tile.stillValid(pPlayer);
    }
}

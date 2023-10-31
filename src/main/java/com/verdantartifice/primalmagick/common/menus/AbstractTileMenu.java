package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.tiles.base.TilePM;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;

/**
 * Base class for a menu that serves a mod block entity.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTileMenu<T extends TilePM> extends AbstractContainerMenu {
    protected final T tile;
    protected final Level level;
    
    protected AbstractTileMenu(MenuType<?> menuType, int containerId, T tile) {
        super(menuType, containerId);
        this.tile = tile;
        this.level = this.tile.getLevel();
    }
    
    public T getTile() {
        return this.tile;
    }
    
    public Level getLevel() {
        return this.level;
    }
    
    @Override
    public boolean stillValid(Player pPlayer) {
        return this.tile == null ? false : this.tile.stillValid(pPlayer);
    }
}

package com.verdantartifice.primalmagick.common.menus.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;

/**
 * Base class for a menu that serves a mod block entity.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTileMenu<T extends AbstractTilePM> extends AbstractContainerMenu {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    protected final T tile;
    protected final Level level;
    protected final BlockPos tilePos;
    protected final ContainerLevelAccess containerLevelAccess;
    
    @SuppressWarnings("unchecked")
    protected AbstractTileMenu(MenuType<?> menuType, int containerId, Class<T> tileClass, Level level, BlockPos tilePos, T tile) {
        super(menuType, containerId);
        this.level = level;
        this.tilePos = tilePos;
        this.containerLevelAccess = ContainerLevelAccess.create(level, tilePos);
        this.tile = tile != null ? tile : (tileClass.isInstance(level.getBlockEntity(tilePos)) ? (T)level.getBlockEntity(tilePos) : null);
        if (this.tile == null) {
            LOGGER.error("Block entity at {} is not of the expected type for menu", tilePos.toString());
            throw new IllegalStateException("Block entity at " + tilePos.toString() + " is not of the expected type for menu");
        }
    }
    
    public T getTile() {
        return this.tile;
    }
    
    public Level getLevel() {
        return this.level;
    }
    
    public BlockPos getTilePos() {
        return this.tilePos;
    }
    
    public ContainerLevelAccess getContainerLevelAccess() {
        return this.containerLevelAccess;
    }
    
    @Override
    public boolean stillValid(Player pPlayer) {
        return this.tile == null ? false : this.tile.stillValid(pPlayer);
    }
}

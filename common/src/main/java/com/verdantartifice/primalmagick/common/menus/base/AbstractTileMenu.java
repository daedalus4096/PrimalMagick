package com.verdantartifice.primalmagick.common.menus.base;

import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for a menu that serves a mod block entity.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTileMenu<T extends AbstractTilePM> extends AbstractContainerMenu implements ITileMenu<T> {
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
    
    @Override
    public T getTile() {
        return this.tile;
    }
    
    @Override
    public Level getLevel() {
        return this.level;
    }
    
    @Override
    public BlockPos getTilePos() {
        return this.tilePos;
    }
    
    @Override
    public ContainerLevelAccess getContainerLevelAccess() {
        return this.containerLevelAccess;
    }
    
    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return this.tile != null && this.tile.stillValid(pPlayer);
    }
}

package com.verdantartifice.primalmagick.common.menus.base;

import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;

public interface ITileMenu<T extends AbstractTilePM> {
    T getTile();

    Level getLevel();

    BlockPos getTilePos();

    ContainerLevelAccess getContainerLevelAccess();
}

package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.tiles.misc.CarvedBookshelfTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface IBlockEntityPrototypeService {
    BlockEntityType.BlockEntitySupplier<CarvedBookshelfTileEntity> carvedBookshelf();
}

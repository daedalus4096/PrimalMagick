package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.tiles.crafting.AbstractCalcinatorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.misc.CarvedBookshelfTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface IBlockEntityPrototypeService {
    // Crafting tiles
    BlockEntityType.BlockEntitySupplier<AbstractCalcinatorTileEntity> calcinator();
    BlockEntityType.BlockEntitySupplier<AbstractCalcinatorTileEntity> essenceFurnace();

    // Misc tiles
    BlockEntityType.BlockEntitySupplier<CarvedBookshelfTileEntity> carvedBookshelf();
}

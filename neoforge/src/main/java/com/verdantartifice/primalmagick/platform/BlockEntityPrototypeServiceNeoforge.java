package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.tiles.misc.CarvedBookshelfTileEntity;
import com.verdantartifice.primalmagick.common.tiles.misc.CarvedBookshelfTileEntityNeoforge;
import com.verdantartifice.primalmagick.platform.services.IBlockEntityPrototypeService;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityPrototypeServiceNeoforge implements IBlockEntityPrototypeService {
    @Override
    public BlockEntityType.BlockEntitySupplier<CarvedBookshelfTileEntity> carvedBookshelf() {
        return CarvedBookshelfTileEntityNeoforge::new;
    }
}

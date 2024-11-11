package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.tiles.misc.CarvedBookshelfTileEntity;
import com.verdantartifice.primalmagick.common.tiles.misc.CarvedBookshelfTileEntityForge;
import com.verdantartifice.primalmagick.platform.services.IBlockEntityPrototypeService;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityPrototypeServiceForge implements IBlockEntityPrototypeService {
    @Override
    public BlockEntityType.BlockEntitySupplier<CarvedBookshelfTileEntity> carvedBookshelf() {
        return CarvedBookshelfTileEntityForge::new;
    }
}

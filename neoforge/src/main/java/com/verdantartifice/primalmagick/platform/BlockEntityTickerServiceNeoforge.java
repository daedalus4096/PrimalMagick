package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.tiles.crafting.AbstractCalcinatorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.CalcinatorTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.crafting.EssenceFurnaceTileEntityNeoforge;
import com.verdantartifice.primalmagick.platform.services.IBlockEntityTickerService;
import net.minecraft.world.level.block.entity.BlockEntityTicker;

public class BlockEntityTickerServiceNeoforge implements IBlockEntityTickerService {
    @Override
    public BlockEntityTicker<AbstractCalcinatorTileEntity> calcinator() {
        return CalcinatorTileEntityNeoforge::tick;
    }

    @Override
    public BlockEntityTicker<AbstractCalcinatorTileEntity> essenceFurnace() {
        return EssenceFurnaceTileEntityNeoforge::tick;
    }
}

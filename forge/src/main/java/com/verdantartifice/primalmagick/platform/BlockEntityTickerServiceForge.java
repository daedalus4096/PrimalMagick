package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.tiles.crafting.AbstractCalcinatorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.CalcinatorTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.crafting.ConcocterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.ConcocterTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.crafting.EssenceFurnaceTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.devices.DissolutionChamberTileEntity;
import com.verdantartifice.primalmagick.platform.services.IBlockEntityTickerService;
import net.minecraft.world.level.block.entity.BlockEntityTicker;

public class BlockEntityTickerServiceForge implements IBlockEntityTickerService {
    @Override
    public BlockEntityTicker<AbstractCalcinatorTileEntity> calcinator() {
        return CalcinatorTileEntityForge::tick;
    }

    @Override
    public BlockEntityTicker<AbstractCalcinatorTileEntity> essenceFurnace() {
        return EssenceFurnaceTileEntityForge::tick;
    }

    @Override
    public BlockEntityTicker<ConcocterTileEntity> concocter() {
        return ConcocterTileEntityForge::tick;
    }

    @Override
    public BlockEntityTicker<DissolutionChamberTileEntity> dissolutionChamber() {
        return DissolutionChamberTileEntity::tick;
    }
}

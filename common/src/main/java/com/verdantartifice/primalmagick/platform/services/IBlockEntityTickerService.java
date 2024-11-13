package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.tiles.crafting.AbstractCalcinatorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.ConcocterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.DissolutionChamberTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceTransmuterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.HoneyExtractorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.InfernalFurnaceTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;

public interface IBlockEntityTickerService {
    // Crafting tiles
    BlockEntityTicker<AbstractCalcinatorTileEntity> calcinator();
    BlockEntityTicker<AbstractCalcinatorTileEntity> essenceFurnace();
    BlockEntityTicker<ConcocterTileEntity> concocter();

    // Device tiles
    BlockEntityTicker<DissolutionChamberTileEntity> dissolutionChamber();
    BlockEntityTicker<EssenceTransmuterTileEntity> essenceTransmuter();
    BlockEntityTicker<HoneyExtractorTileEntity> honeyExtractor();
    BlockEntityTicker<InfernalFurnaceTileEntity> infernalFurnace();
}

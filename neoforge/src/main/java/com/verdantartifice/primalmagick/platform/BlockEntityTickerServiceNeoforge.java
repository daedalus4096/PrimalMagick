package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.tiles.crafting.AbstractCalcinatorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.CalcinatorTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.crafting.ConcocterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.ConcocterTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.crafting.EssenceFurnaceTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.devices.DissolutionChamberTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.DissolutionChamberTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceCaskTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceCaskTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceTransmuterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceTransmuterTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.devices.HoneyExtractorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.HoneyExtractorTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.devices.InfernalFurnaceTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.InfernalFurnaceTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.devices.SanguineCrucibleTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.SanguineCrucibleTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.mana.AutoChargerTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.AutoChargerTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaBatteryTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaBatteryTileEntityNeoforge;
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

    @Override
    public BlockEntityTicker<ConcocterTileEntity> concocter() {
        return ConcocterTileEntityNeoforge::tick;
    }

    @Override
    public BlockEntityTicker<DissolutionChamberTileEntity> dissolutionChamber() {
        return DissolutionChamberTileEntityNeoforge::tick;
    }

    @Override
    public BlockEntityTicker<EssenceCaskTileEntity> essenceCask() {
        return EssenceCaskTileEntityNeoforge::tick;
    }

    @Override
    public BlockEntityTicker<EssenceTransmuterTileEntity> essenceTransmuter() {
        return EssenceTransmuterTileEntityNeoforge::tick;
    }

    @Override
    public BlockEntityTicker<HoneyExtractorTileEntity> honeyExtractor() {
        return HoneyExtractorTileEntityNeoforge::tick;
    }

    @Override
    public BlockEntityTicker<InfernalFurnaceTileEntity> infernalFurnace() {
        return InfernalFurnaceTileEntityNeoforge::tick;
    }

    @Override
    public BlockEntityTicker<SanguineCrucibleTileEntity> sanguineCrucible() {
        return SanguineCrucibleTileEntityNeoforge::tick;
    }

    @Override
    public BlockEntityTicker<AutoChargerTileEntity> autoCharger() {
        return AutoChargerTileEntityNeoforge::tick;
    }

    @Override
    public BlockEntityTicker<ManaBatteryTileEntity> manaBattery() {
        return ManaBatteryTileEntityNeoforge::tick;
    }
}

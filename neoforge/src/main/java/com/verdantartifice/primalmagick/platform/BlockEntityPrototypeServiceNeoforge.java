package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.tiles.crafting.AbstractCalcinatorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.CalcinatorTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.crafting.ConcocterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.ConcocterTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.crafting.EssenceFurnaceTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunecarvingTableTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunecarvingTableTileEntityNeoforge;
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
import com.verdantartifice.primalmagick.common.tiles.devices.ResearchTableTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.ResearchTableTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.devices.SanguineCrucibleTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.SanguineCrucibleTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.devices.ScribeTableTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.ScribeTableTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.mana.AutoChargerTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.AutoChargerTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaBatteryTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaBatteryTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.misc.CarvedBookshelfTileEntity;
import com.verdantartifice.primalmagick.common.tiles.misc.CarvedBookshelfTileEntityNeoforge;
import com.verdantartifice.primalmagick.common.tiles.rituals.OfferingPedestalTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.OfferingPedestalTileEntityNeoforge;
import com.verdantartifice.primalmagick.platform.services.IBlockEntityPrototypeService;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityPrototypeServiceNeoforge implements IBlockEntityPrototypeService {
    @Override
    public BlockEntityType.BlockEntitySupplier<AbstractCalcinatorTileEntity> calcinator() {
        return CalcinatorTileEntityNeoforge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<AbstractCalcinatorTileEntity> essenceFurnace() {
        return EssenceFurnaceTileEntityNeoforge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<ConcocterTileEntity> concocter() {
        return ConcocterTileEntityNeoforge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<RunecarvingTableTileEntity> runecarvingTable() {
        return RunecarvingTableTileEntityNeoforge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<DissolutionChamberTileEntity> dissolutionChamber() {
        return DissolutionChamberTileEntityNeoforge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<EssenceCaskTileEntity> essenceCask() {
        return EssenceCaskTileEntityNeoforge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<EssenceTransmuterTileEntity> essenceTransmuter() {
        return EssenceTransmuterTileEntityNeoforge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<HoneyExtractorTileEntity> honeyExtractor() {
        return HoneyExtractorTileEntityNeoforge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<InfernalFurnaceTileEntity> infernalFurnace() {
        return InfernalFurnaceTileEntityNeoforge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<ResearchTableTileEntity> researchTable() {
        return ResearchTableTileEntityNeoforge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<SanguineCrucibleTileEntity> sanguineCrucible() {
        return SanguineCrucibleTileEntityNeoforge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<ScribeTableTileEntity> scribeTable() {
        return ScribeTableTileEntityNeoforge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<AutoChargerTileEntity> autoCharger() {
        return AutoChargerTileEntityNeoforge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<ManaBatteryTileEntity> manaBattery() {
        return ManaBatteryTileEntityNeoforge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<CarvedBookshelfTileEntity> carvedBookshelf() {
        return CarvedBookshelfTileEntityNeoforge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<OfferingPedestalTileEntity> offeringPedestal() {
        return OfferingPedestalTileEntityNeoforge::new;
    }
}

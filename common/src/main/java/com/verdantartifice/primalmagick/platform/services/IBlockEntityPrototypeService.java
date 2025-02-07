package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.tiles.crafting.AbstractCalcinatorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.ConcocterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunecarvingTableTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.DissolutionChamberTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceCaskTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceTransmuterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.HoneyExtractorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.InfernalFurnaceTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.ResearchTableTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.SanguineCrucibleTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.ScribeTableTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.AutoChargerTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaBatteryTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.WandChargerTileEntity;
import com.verdantartifice.primalmagick.common.tiles.misc.CarvedBookshelfTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.OfferingPedestalTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualLecternTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface IBlockEntityPrototypeService {
    // Crafting tiles
    BlockEntityType.BlockEntitySupplier<AbstractCalcinatorTileEntity> calcinator();
    BlockEntityType.BlockEntitySupplier<AbstractCalcinatorTileEntity> essenceFurnace();
    BlockEntityType.BlockEntitySupplier<ConcocterTileEntity> concocter();
    BlockEntityType.BlockEntitySupplier<RunecarvingTableTileEntity> runecarvingTable();

    // Device tiles
    BlockEntityType.BlockEntitySupplier<DissolutionChamberTileEntity> dissolutionChamber();
    BlockEntityType.BlockEntitySupplier<EssenceCaskTileEntity> essenceCask();
    BlockEntityType.BlockEntitySupplier<EssenceTransmuterTileEntity> essenceTransmuter();
    BlockEntityType.BlockEntitySupplier<HoneyExtractorTileEntity> honeyExtractor();
    BlockEntityType.BlockEntitySupplier<InfernalFurnaceTileEntity> infernalFurnace();
    BlockEntityType.BlockEntitySupplier<ResearchTableTileEntity> researchTable();
    BlockEntityType.BlockEntitySupplier<SanguineCrucibleTileEntity> sanguineCrucible();
    BlockEntityType.BlockEntitySupplier<ScribeTableTileEntity> scribeTable();

    // Mana tiles
    BlockEntityType.BlockEntitySupplier<AutoChargerTileEntity> autoCharger();
    BlockEntityType.BlockEntitySupplier<ManaBatteryTileEntity> manaBattery();
    BlockEntityType.BlockEntitySupplier<WandChargerTileEntity> wandCharger();

    // Misc tiles
    BlockEntityType.BlockEntitySupplier<CarvedBookshelfTileEntity> carvedBookshelf();

    // Ritual tiles
    BlockEntityType.BlockEntitySupplier<OfferingPedestalTileEntity> offeringPedestal();
    BlockEntityType.BlockEntitySupplier<RitualAltarTileEntity> ritualAltar();
    BlockEntityType.BlockEntitySupplier<RitualLecternTileEntity> ritualLectern();
}

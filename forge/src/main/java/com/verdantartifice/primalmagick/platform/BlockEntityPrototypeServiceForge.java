package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.tiles.crafting.AbstractCalcinatorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.CalcinatorTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.crafting.ConcocterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.ConcocterTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.crafting.EssenceFurnaceTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunecarvingTableTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunecarvingTableTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.devices.DissolutionChamberTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.DissolutionChamberTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceCaskTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceCaskTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceTransmuterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceTransmuterTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.devices.HoneyExtractorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.HoneyExtractorTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.devices.InfernalFurnaceTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.InfernalFurnaceTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.devices.ResearchTableTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.ResearchTableTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.devices.SanguineCrucibleTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.SanguineCrucibleTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.devices.ScribeTableTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.ScribeTableTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.mana.AutoChargerTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.AutoChargerTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaBatteryTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaBatteryTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaInjectorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaInjectorTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaRelayTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaRelayTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.mana.WandChargerTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.WandChargerTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.misc.CarvedBookshelfTileEntity;
import com.verdantartifice.primalmagick.common.tiles.misc.CarvedBookshelfTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.rituals.OfferingPedestalTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.OfferingPedestalTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntityForge;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualLecternTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualLecternTileEntityForge;
import com.verdantartifice.primalmagick.platform.services.IBlockEntityPrototypeService;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityPrototypeServiceForge implements IBlockEntityPrototypeService {
    @Override
    public BlockEntityType.BlockEntitySupplier<AbstractCalcinatorTileEntity> calcinator() {
        return CalcinatorTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<AbstractCalcinatorTileEntity> essenceFurnace() {
        return EssenceFurnaceTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<ConcocterTileEntity> concocter() {
        return ConcocterTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<RunecarvingTableTileEntity> runecarvingTable() {
        return RunecarvingTableTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<DissolutionChamberTileEntity> dissolutionChamber() {
        return DissolutionChamberTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<EssenceCaskTileEntity> essenceCask() {
        return EssenceCaskTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<EssenceTransmuterTileEntity> essenceTransmuter() {
        return EssenceTransmuterTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<HoneyExtractorTileEntity> honeyExtractor() {
        return HoneyExtractorTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<InfernalFurnaceTileEntity> infernalFurnace() {
        return InfernalFurnaceTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<ResearchTableTileEntity> researchTable() {
        return ResearchTableTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<SanguineCrucibleTileEntity> sanguineCrucible() {
        return SanguineCrucibleTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<ScribeTableTileEntity> scribeTable() {
        return ScribeTableTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<AutoChargerTileEntity> autoCharger() {
        return AutoChargerTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<ManaBatteryTileEntity> manaBattery() {
        return ManaBatteryTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<ManaInjectorTileEntity> manaInjector() {
        return ManaInjectorTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<ManaRelayTileEntity> manaRelay() {
        return ManaRelayTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<WandChargerTileEntity> wandCharger() {
        return WandChargerTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<CarvedBookshelfTileEntity> carvedBookshelf() {
        return CarvedBookshelfTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<OfferingPedestalTileEntity> offeringPedestal() {
        return OfferingPedestalTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<RitualAltarTileEntity> ritualAltar() {
        return RitualAltarTileEntityForge::new;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<RitualLecternTileEntity> ritualLectern() {
        return RitualLecternTileEntityForge::new;
    }
}

package com.verdantartifice.primalmagick.common.tiles;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.tiles.crafting.CalcinatorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.ConcocterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.EssenceFurnaceTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunecarvingTableTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunescribingAltarTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.SpellcraftingAltarTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.DissolutionChamberTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceCaskTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceTransmuterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.HoneyExtractorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.InfernalFurnaceTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.ResearchTableTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.SanguineCrucibleTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.ScribeTableTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.SunlampTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.WindGeneratorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.AncientManaFontTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.ArtificialManaFontTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.AutoChargerTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaBatteryTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.WandChargerTileEntity;
import com.verdantartifice.primalmagick.common.tiles.misc.CarvedBookshelfTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.BloodletterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.CelestialHarpTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.EntropySinkTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.IncenseBrazierTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.OfferingPedestalTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualBellTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualCandleTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualLecternTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.SoulAnvilTileEntity;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

/**
 * Deferred registry for mod tile entity types.
 * 
 * @author Daedalus4096
 */
public class BlockEntityTypesPM {
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<AncientManaFontTileEntity>> ANCIENT_MANA_FONT = register("ancient_mana_font", () -> BlockEntityType.Builder.of(AncientManaFontTileEntity::new, BlocksPM.ANCIENT_FONT_EARTH.get(), BlocksPM.ANCIENT_FONT_SEA.get(), BlocksPM.ANCIENT_FONT_SKY.get(), BlocksPM.ANCIENT_FONT_SUN.get(), BlocksPM.ANCIENT_FONT_MOON.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<ArtificialManaFontTileEntity>> ARTIFICIAL_MANA_FONT = register("artificial_mana_font", () -> BlockEntityType.Builder.of(ArtificialManaFontTileEntity::new, BlocksPM.ARTIFICIAL_FONT_EARTH.get(), BlocksPM.ARTIFICIAL_FONT_SEA.get(), BlocksPM.ARTIFICIAL_FONT_SKY.get(), BlocksPM.ARTIFICIAL_FONT_SUN.get(), BlocksPM.ARTIFICIAL_FONT_MOON.get(), BlocksPM.ARTIFICIAL_FONT_BLOOD.get(), BlocksPM.ARTIFICIAL_FONT_INFERNAL.get(), BlocksPM.ARTIFICIAL_FONT_VOID.get(), BlocksPM.ARTIFICIAL_FONT_HALLOWED.get(), BlocksPM.FORBIDDEN_FONT_EARTH.get(), BlocksPM.FORBIDDEN_FONT_SEA.get(), BlocksPM.FORBIDDEN_FONT_SKY.get(), BlocksPM.FORBIDDEN_FONT_SUN.get(), BlocksPM.FORBIDDEN_FONT_MOON.get(), BlocksPM.FORBIDDEN_FONT_BLOOD.get(), BlocksPM.FORBIDDEN_FONT_INFERNAL.get(), BlocksPM.FORBIDDEN_FONT_VOID.get(), BlocksPM.FORBIDDEN_FONT_HALLOWED.get(), BlocksPM.HEAVENLY_FONT_EARTH.get(), BlocksPM.HEAVENLY_FONT_SEA.get(), BlocksPM.HEAVENLY_FONT_SKY.get(), BlocksPM.HEAVENLY_FONT_SUN.get(), BlocksPM.HEAVENLY_FONT_MOON.get(), BlocksPM.HEAVENLY_FONT_BLOOD.get(), BlocksPM.HEAVENLY_FONT_INFERNAL.get(), BlocksPM.HEAVENLY_FONT_VOID.get(), BlocksPM.HEAVENLY_FONT_HALLOWED.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<EssenceFurnaceTileEntity>> ESSENCE_FURNACE = register("essence_furnace", () -> BlockEntityType.Builder.of(EssenceFurnaceTileEntity::new, BlocksPM.ESSENCE_FURNACE.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<CalcinatorTileEntity>> CALCINATOR = register("calcinator", () -> BlockEntityType.Builder.of(CalcinatorTileEntity::new, BlocksPM.CALCINATOR_BASIC.get(), BlocksPM.CALCINATOR_ENCHANTED.get(), BlocksPM.CALCINATOR_FORBIDDEN.get(), BlocksPM.CALCINATOR_HEAVENLY.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<WandChargerTileEntity>> WAND_CHARGER = register("wand_charger", () -> BlockEntityType.Builder.of(WandChargerTileEntity::new, BlocksPM.WAND_CHARGER.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<RitualAltarTileEntity>> RITUAL_ALTAR = register("ritual_altar", () -> BlockEntityType.Builder.of(RitualAltarTileEntity::new, BlocksPM.RITUAL_ALTAR.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<SunlampTileEntity>> SUNLAMP = register("sunlamp", () -> BlockEntityType.Builder.of(SunlampTileEntity::new, BlocksPM.SUNLAMP.get(), BlocksPM.SPIRIT_LANTERN.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<OfferingPedestalTileEntity>> OFFERING_PEDESTAL = register("offering_pedestal", () -> BlockEntityType.Builder.of(OfferingPedestalTileEntity::new, BlocksPM.OFFERING_PEDESTAL.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<RitualCandleTileEntity>> RITUAL_CANDLE = register("ritual_candle", () -> BlockEntityType.Builder.of(RitualCandleTileEntity::new, BlocksPM.RITUAL_CANDLE_BLACK.get(), BlocksPM.RITUAL_CANDLE_BLUE.get(), BlocksPM.RITUAL_CANDLE_BROWN.get(), BlocksPM.RITUAL_CANDLE_CYAN.get(), BlocksPM.RITUAL_CANDLE_GRAY.get(), BlocksPM.RITUAL_CANDLE_GREEN.get(), BlocksPM.RITUAL_CANDLE_LIGHT_BLUE.get(), BlocksPM.RITUAL_CANDLE_LIGHT_GRAY.get(), BlocksPM.RITUAL_CANDLE_LIME.get(), BlocksPM.RITUAL_CANDLE_MAGENTA.get(), BlocksPM.RITUAL_CANDLE_ORANGE.get(), BlocksPM.RITUAL_CANDLE_PINK.get(), BlocksPM.RITUAL_CANDLE_PURPLE.get(), BlocksPM.RITUAL_CANDLE_RED.get(), BlocksPM.RITUAL_CANDLE_WHITE.get(), BlocksPM.RITUAL_CANDLE_YELLOW.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<IncenseBrazierTileEntity>> INCENSE_BRAZIER = register("incense_brazier", () -> BlockEntityType.Builder.of(IncenseBrazierTileEntity::new, BlocksPM.INCENSE_BRAZIER.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<RitualLecternTileEntity>> RITUAL_LECTERN = register("ritual_lectern", () -> BlockEntityType.Builder.of(RitualLecternTileEntity::new, BlocksPM.RITUAL_LECTERN.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<RitualBellTileEntity>> RITUAL_BELL = register("ritual_bell", () -> BlockEntityType.Builder.of(RitualBellTileEntity::new, BlocksPM.RITUAL_BELL.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<BloodletterTileEntity>> BLOODLETTER = register("bloodletter", () -> BlockEntityType.Builder.of(BloodletterTileEntity::new, BlocksPM.BLOODLETTER.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<SoulAnvilTileEntity>> SOUL_ANVIL = register("soul_anvil", () -> BlockEntityType.Builder.of(SoulAnvilTileEntity::new, BlocksPM.SOUL_ANVIL.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<RunescribingAltarTileEntity>> RUNESCRIBING_ALTAR = register("runescribing_altar", () -> BlockEntityType.Builder.of(RunescribingAltarTileEntity::new, BlocksPM.RUNESCRIBING_ALTAR_BASIC.get(), BlocksPM.RUNESCRIBING_ALTAR_ENCHANTED.get(), BlocksPM.RUNESCRIBING_ALTAR_FORBIDDEN.get(), BlocksPM.RUNESCRIBING_ALTAR_HEAVENLY.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<HoneyExtractorTileEntity>> HONEY_EXTRACTOR = register("honey_extractor", () -> BlockEntityType.Builder.of(HoneyExtractorTileEntity::new, BlocksPM.HONEY_EXTRACTOR.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<SanguineCrucibleTileEntity>> SANGUINE_CRUCIBLE = register("sanguine_crucible", () -> BlockEntityType.Builder.of(SanguineCrucibleTileEntity::new, BlocksPM.SANGUINE_CRUCIBLE.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<ConcocterTileEntity>> CONCOCTER = register("concocter", () -> BlockEntityType.Builder.of(ConcocterTileEntity::new, BlocksPM.CONCOCTER.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<CelestialHarpTileEntity>> CELESTIAL_HARP = register("celestial_harp", () -> BlockEntityType.Builder.of(CelestialHarpTileEntity::new, BlocksPM.CELESTIAL_HARP.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<EntropySinkTileEntity>> ENTROPY_SINK = register("entropy_sink", () -> BlockEntityType.Builder.of(EntropySinkTileEntity::new, BlocksPM.ENTROPY_SINK.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<ResearchTableTileEntity>> RESEARCH_TABLE = register("research_table", () -> BlockEntityType.Builder.of(ResearchTableTileEntity::new, BlocksPM.RESEARCH_TABLE.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<AutoChargerTileEntity>> AUTO_CHARGER = register("auto_charger", () -> BlockEntityType.Builder.of(AutoChargerTileEntity::new, BlocksPM.AUTO_CHARGER.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<RunecarvingTableTileEntity>> RUNECARVING_TABLE = register("runecarving_table", () -> BlockEntityType.Builder.of(RunecarvingTableTileEntity::new, BlocksPM.RUNECARVING_TABLE.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<EssenceTransmuterTileEntity>> ESSENCE_TRANSMUTER = register("essence_transmuter", () -> BlockEntityType.Builder.of(EssenceTransmuterTileEntity::new, BlocksPM.ESSENCE_TRANSMUTER.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<DissolutionChamberTileEntity>> DISSOLUTION_CHAMBER = register("dissolution_chamber", () -> BlockEntityType.Builder.of(DissolutionChamberTileEntity::new, BlocksPM.DISSOLUTION_CHAMBER.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<SpellcraftingAltarTileEntity>> SPELLCRAFTING_ALTAR = register("spellcrafting_altar", () -> BlockEntityType.Builder.of(SpellcraftingAltarTileEntity::new, BlocksPM.SPELLCRAFTING_ALTAR.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<WindGeneratorTileEntity>> WIND_GENERATOR = register("wind_generator", () -> BlockEntityType.Builder.of(WindGeneratorTileEntity::new, BlocksPM.ZEPHYR_ENGINE.get(), BlocksPM.VOID_TURBINE.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<EssenceCaskTileEntity>> ESSENCE_CASK = register("essence_cask", () -> BlockEntityType.Builder.of(EssenceCaskTileEntity::new, BlocksPM.ESSENCE_CASK_ENCHANTED.get(), BlocksPM.ESSENCE_CASK_FORBIDDEN.get(), BlocksPM.ESSENCE_CASK_HEAVENLY.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<InfernalFurnaceTileEntity>> INFERNAL_FURNACE = register("infernal_furnace", () -> BlockEntityType.Builder.of(InfernalFurnaceTileEntity::new, BlocksPM.INFERNAL_FURNACE.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<ManaBatteryTileEntity>> MANA_BATTERY = register("mana_battery", () -> BlockEntityType.Builder.of(ManaBatteryTileEntity::new, BlocksPM.MANA_NEXUS.get(), BlocksPM.MANA_SINGULARITY.get(), BlocksPM.MANA_SINGULARITY_CREATIVE.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<ScribeTableTileEntity>> SCRIBE_TABLE = register("scribe_table", () -> BlockEntityType.Builder.of(ScribeTableTileEntity::new, BlocksPM.SCRIBE_TABLE.get()).build(null));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<CarvedBookshelfTileEntity>> CARVED_BOOKSHELF = register("carved_bookshelf", () -> BlockEntityType.Builder.of(CarvedBookshelfTileEntity::new, BlocksPM.MARBLE_BOOKSHELF.get(), BlocksPM.MARBLE_ENCHANTED_BOOKSHELF.get(), BlocksPM.MARBLE_SMOKED_BOOKSHELF.get(), BlocksPM.MARBLE_HALLOWED_BOOKSHELF.get()).build(null));

    private static <T extends BlockEntity> IRegistryItem<BlockEntityType<?>, BlockEntityType<T>> register(String name, Supplier<BlockEntityType<T>> typeSupplier) {
        return Services.BLOCK_ENTITY_TYPES.register(name, typeSupplier);
    }
}
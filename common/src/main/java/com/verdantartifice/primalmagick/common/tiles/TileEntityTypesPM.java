package com.verdantartifice.primalmagick.common.tiles;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod tile entity types.
 * 
 * @author Daedalus4096
 */
public class TileEntityTypesPM {
    private static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MOD_ID);
    
    public static void init() {
        TILE_ENTITIES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
    
    public static final RegistryObject<BlockEntityType<AncientManaFontTileEntity>> ANCIENT_MANA_FONT = TILE_ENTITIES.register("ancient_mana_font", () -> BlockEntityType.Builder.of(AncientManaFontTileEntity::new, BlockRegistration.ANCIENT_FONT_EARTH.get(), BlockRegistration.ANCIENT_FONT_SEA.get(), BlockRegistration.ANCIENT_FONT_SKY.get(), BlockRegistration.ANCIENT_FONT_SUN.get(), BlockRegistration.ANCIENT_FONT_MOON.get()).build(null));
    public static final RegistryObject<BlockEntityType<ArtificialManaFontTileEntity>> ARTIFICIAL_MANA_FONT = TILE_ENTITIES.register("artificial_mana_font", () -> BlockEntityType.Builder.of(ArtificialManaFontTileEntity::new, BlockRegistration.ARTIFICIAL_FONT_EARTH.get(), BlockRegistration.ARTIFICIAL_FONT_SEA.get(), BlockRegistration.ARTIFICIAL_FONT_SKY.get(), BlockRegistration.ARTIFICIAL_FONT_SUN.get(), BlockRegistration.ARTIFICIAL_FONT_MOON.get(), BlockRegistration.ARTIFICIAL_FONT_BLOOD.get(), BlockRegistration.ARTIFICIAL_FONT_INFERNAL.get(), BlockRegistration.ARTIFICIAL_FONT_VOID.get(), BlockRegistration.ARTIFICIAL_FONT_HALLOWED.get(), BlockRegistration.FORBIDDEN_FONT_EARTH.get(), BlockRegistration.FORBIDDEN_FONT_SEA.get(), BlockRegistration.FORBIDDEN_FONT_SKY.get(), BlockRegistration.FORBIDDEN_FONT_SUN.get(), BlockRegistration.FORBIDDEN_FONT_MOON.get(), BlockRegistration.FORBIDDEN_FONT_BLOOD.get(), BlockRegistration.FORBIDDEN_FONT_INFERNAL.get(), BlockRegistration.FORBIDDEN_FONT_VOID.get(), BlockRegistration.FORBIDDEN_FONT_HALLOWED.get(), BlockRegistration.HEAVENLY_FONT_EARTH.get(), BlockRegistration.HEAVENLY_FONT_SEA.get(), BlockRegistration.HEAVENLY_FONT_SKY.get(), BlockRegistration.HEAVENLY_FONT_SUN.get(), BlockRegistration.HEAVENLY_FONT_MOON.get(), BlockRegistration.HEAVENLY_FONT_BLOOD.get(), BlockRegistration.HEAVENLY_FONT_INFERNAL.get(), BlockRegistration.HEAVENLY_FONT_VOID.get(), BlockRegistration.HEAVENLY_FONT_HALLOWED.get()).build(null));
    public static final RegistryObject<BlockEntityType<EssenceFurnaceTileEntity>> ESSENCE_FURNACE = TILE_ENTITIES.register("essence_furnace", () -> BlockEntityType.Builder.of(EssenceFurnaceTileEntity::new, BlockRegistration.ESSENCE_FURNACE.get()).build(null));
    public static final RegistryObject<BlockEntityType<CalcinatorTileEntity>> CALCINATOR = TILE_ENTITIES.register("calcinator", () -> BlockEntityType.Builder.of(CalcinatorTileEntity::new, BlockRegistration.CALCINATOR_BASIC.get(), BlockRegistration.CALCINATOR_ENCHANTED.get(), BlockRegistration.CALCINATOR_FORBIDDEN.get(), BlockRegistration.CALCINATOR_HEAVENLY.get()).build(null));
    public static final RegistryObject<BlockEntityType<WandChargerTileEntity>> WAND_CHARGER = TILE_ENTITIES.register("wand_charger", () -> BlockEntityType.Builder.of(WandChargerTileEntity::new, BlockRegistration.WAND_CHARGER.get()).build(null));
    public static final RegistryObject<BlockEntityType<RitualAltarTileEntity>> RITUAL_ALTAR = TILE_ENTITIES.register("ritual_altar", () -> BlockEntityType.Builder.of(RitualAltarTileEntity::new, BlockRegistration.RITUAL_ALTAR.get()).build(null));
    public static final RegistryObject<BlockEntityType<SunlampTileEntity>> SUNLAMP = TILE_ENTITIES.register("sunlamp", () -> BlockEntityType.Builder.of(SunlampTileEntity::new, BlockRegistration.SUNLAMP.get(), BlockRegistration.SPIRIT_LANTERN.get()).build(null));
    public static final RegistryObject<BlockEntityType<OfferingPedestalTileEntity>> OFFERING_PEDESTAL = TILE_ENTITIES.register("offering_pedestal", () -> BlockEntityType.Builder.of(OfferingPedestalTileEntity::new, BlockRegistration.OFFERING_PEDESTAL.get()).build(null));
    public static final RegistryObject<BlockEntityType<RitualCandleTileEntity>> RITUAL_CANDLE = TILE_ENTITIES.register("ritual_candle", () -> BlockEntityType.Builder.of(RitualCandleTileEntity::new, BlockRegistration.RITUAL_CANDLE_BLACK.get(), BlockRegistration.RITUAL_CANDLE_BLUE.get(), BlockRegistration.RITUAL_CANDLE_BROWN.get(), BlockRegistration.RITUAL_CANDLE_CYAN.get(), BlockRegistration.RITUAL_CANDLE_GRAY.get(), BlockRegistration.RITUAL_CANDLE_GREEN.get(), BlockRegistration.RITUAL_CANDLE_LIGHT_BLUE.get(), BlockRegistration.RITUAL_CANDLE_LIGHT_GRAY.get(), BlockRegistration.RITUAL_CANDLE_LIME.get(), BlockRegistration.RITUAL_CANDLE_MAGENTA.get(), BlockRegistration.RITUAL_CANDLE_ORANGE.get(), BlockRegistration.RITUAL_CANDLE_PINK.get(), BlockRegistration.RITUAL_CANDLE_PURPLE.get(), BlockRegistration.RITUAL_CANDLE_RED.get(), BlockRegistration.RITUAL_CANDLE_WHITE.get(), BlockRegistration.RITUAL_CANDLE_YELLOW.get()).build(null));
    public static final RegistryObject<BlockEntityType<IncenseBrazierTileEntity>> INCENSE_BRAZIER = TILE_ENTITIES.register("incense_brazier", () -> BlockEntityType.Builder.of(IncenseBrazierTileEntity::new, BlockRegistration.INCENSE_BRAZIER.get()).build(null));
    public static final RegistryObject<BlockEntityType<RitualLecternTileEntity>> RITUAL_LECTERN = TILE_ENTITIES.register("ritual_lectern", () -> BlockEntityType.Builder.of(RitualLecternTileEntity::new, BlockRegistration.RITUAL_LECTERN.get()).build(null));
    public static final RegistryObject<BlockEntityType<RitualBellTileEntity>> RITUAL_BELL = TILE_ENTITIES.register("ritual_bell", () -> BlockEntityType.Builder.of(RitualBellTileEntity::new, BlockRegistration.RITUAL_BELL.get()).build(null));
    public static final RegistryObject<BlockEntityType<BloodletterTileEntity>> BLOODLETTER = TILE_ENTITIES.register("bloodletter", () -> BlockEntityType.Builder.of(BloodletterTileEntity::new, BlockRegistration.BLOODLETTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<SoulAnvilTileEntity>> SOUL_ANVIL = TILE_ENTITIES.register("soul_anvil", () -> BlockEntityType.Builder.of(SoulAnvilTileEntity::new, BlockRegistration.SOUL_ANVIL.get()).build(null));
    public static final RegistryObject<BlockEntityType<RunescribingAltarTileEntity>> RUNESCRIBING_ALTAR = TILE_ENTITIES.register("runescribing_altar", () -> BlockEntityType.Builder.of(RunescribingAltarTileEntity::new, BlockRegistration.RUNESCRIBING_ALTAR_BASIC.get(), BlockRegistration.RUNESCRIBING_ALTAR_ENCHANTED.get(), BlockRegistration.RUNESCRIBING_ALTAR_FORBIDDEN.get(), BlockRegistration.RUNESCRIBING_ALTAR_HEAVENLY.get()).build(null));
    public static final RegistryObject<BlockEntityType<HoneyExtractorTileEntity>> HONEY_EXTRACTOR = TILE_ENTITIES.register("honey_extractor", () -> BlockEntityType.Builder.of(HoneyExtractorTileEntity::new, BlockRegistration.HONEY_EXTRACTOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<SanguineCrucibleTileEntity>> SANGUINE_CRUCIBLE = TILE_ENTITIES.register("sanguine_crucible", () -> BlockEntityType.Builder.of(SanguineCrucibleTileEntity::new, BlockRegistration.SANGUINE_CRUCIBLE.get()).build(null));
    public static final RegistryObject<BlockEntityType<ConcocterTileEntity>> CONCOCTER = TILE_ENTITIES.register("concocter", () -> BlockEntityType.Builder.of(ConcocterTileEntity::new, BlockRegistration.CONCOCTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<CelestialHarpTileEntity>> CELESTIAL_HARP = TILE_ENTITIES.register("celestial_harp", () -> BlockEntityType.Builder.of(CelestialHarpTileEntity::new, BlockRegistration.CELESTIAL_HARP.get()).build(null));
    public static final RegistryObject<BlockEntityType<EntropySinkTileEntity>> ENTROPY_SINK = TILE_ENTITIES.register("entropy_sink", () -> BlockEntityType.Builder.of(EntropySinkTileEntity::new, BlockRegistration.ENTROPY_SINK.get()).build(null));
    public static final RegistryObject<BlockEntityType<ResearchTableTileEntity>> RESEARCH_TABLE = TILE_ENTITIES.register("research_table", () -> BlockEntityType.Builder.of(ResearchTableTileEntity::new, BlockRegistration.RESEARCH_TABLE.get()).build(null));
    public static final RegistryObject<BlockEntityType<AutoChargerTileEntity>> AUTO_CHARGER = TILE_ENTITIES.register("auto_charger", () -> BlockEntityType.Builder.of(AutoChargerTileEntity::new, BlockRegistration.AUTO_CHARGER.get()).build(null));
    public static final RegistryObject<BlockEntityType<RunecarvingTableTileEntity>> RUNECARVING_TABLE = TILE_ENTITIES.register("runecarving_table", () -> BlockEntityType.Builder.of(RunecarvingTableTileEntity::new, BlockRegistration.RUNECARVING_TABLE.get()).build(null));
    public static final RegistryObject<BlockEntityType<EssenceTransmuterTileEntity>> ESSENCE_TRANSMUTER = TILE_ENTITIES.register("essence_transmuter", () -> BlockEntityType.Builder.of(EssenceTransmuterTileEntity::new, BlockRegistration.ESSENCE_TRANSMUTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<DissolutionChamberTileEntity>> DISSOLUTION_CHAMBER = TILE_ENTITIES.register("dissolution_chamber", () -> BlockEntityType.Builder.of(DissolutionChamberTileEntity::new, BlockRegistration.DISSOLUTION_CHAMBER.get()).build(null));
    public static final RegistryObject<BlockEntityType<SpellcraftingAltarTileEntity>> SPELLCRAFTING_ALTAR = TILE_ENTITIES.register("spellcrafting_altar", () -> BlockEntityType.Builder.of(SpellcraftingAltarTileEntity::new, BlockRegistration.SPELLCRAFTING_ALTAR.get()).build(null));
    public static final RegistryObject<BlockEntityType<WindGeneratorTileEntity>> WIND_GENERATOR = TILE_ENTITIES.register("wind_generator", () -> BlockEntityType.Builder.of(WindGeneratorTileEntity::new, BlockRegistration.ZEPHYR_ENGINE.get(), BlockRegistration.VOID_TURBINE.get()).build(null));
    public static final RegistryObject<BlockEntityType<EssenceCaskTileEntity>> ESSENCE_CASK = TILE_ENTITIES.register("essence_cask", () -> BlockEntityType.Builder.of(EssenceCaskTileEntity::new, BlockRegistration.ESSENCE_CASK_ENCHANTED.get(), BlockRegistration.ESSENCE_CASK_FORBIDDEN.get(), BlockRegistration.ESSENCE_CASK_HEAVENLY.get()).build(null));
    public static final RegistryObject<BlockEntityType<InfernalFurnaceTileEntity>> INFERNAL_FURNACE = TILE_ENTITIES.register("infernal_furnace", () -> BlockEntityType.Builder.of(InfernalFurnaceTileEntity::new, BlockRegistration.INFERNAL_FURNACE.get()).build(null));
    public static final RegistryObject<BlockEntityType<ManaBatteryTileEntity>> MANA_BATTERY = TILE_ENTITIES.register("mana_battery", () -> BlockEntityType.Builder.of(ManaBatteryTileEntity::new, BlockRegistration.MANA_NEXUS.get(), BlockRegistration.MANA_SINGULARITY.get(), BlockRegistration.MANA_SINGULARITY_CREATIVE.get()).build(null));
    public static final RegistryObject<BlockEntityType<ScribeTableTileEntity>> SCRIBE_TABLE = TILE_ENTITIES.register("scribe_table", () -> BlockEntityType.Builder.of(ScribeTableTileEntity::new, BlockRegistration.SCRIBE_TABLE.get()).build(null));
    public static final RegistryObject<BlockEntityType<CarvedBookshelfTileEntity>> CARVED_BOOKSHELF = TILE_ENTITIES.register("carved_bookshelf", () -> BlockEntityType.Builder.of(CarvedBookshelfTileEntity::new, BlockRegistration.MARBLE_BOOKSHELF.get(), BlockRegistration.MARBLE_ENCHANTED_BOOKSHELF.get(), BlockRegistration.MARBLE_SMOKED_BOOKSHELF.get(), BlockRegistration.MARBLE_HALLOWED_BOOKSHELF.get()).build(null));
}

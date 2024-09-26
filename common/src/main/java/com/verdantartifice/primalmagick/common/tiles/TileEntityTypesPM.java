package com.verdantartifice.primalmagick.common.tiles;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
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
    
    public static final RegistryObject<BlockEntityType<AncientManaFontTileEntity>> ANCIENT_MANA_FONT = TILE_ENTITIES.register("ancient_mana_font", () -> BlockEntityType.Builder.of(AncientManaFontTileEntity::new, BlocksPM.get(BlocksPM.ANCIENT_FONT_EARTH), BlocksPM.get(BlocksPM.ANCIENT_FONT_SEA), BlocksPM.get(BlocksPM.ANCIENT_FONT_SKY), BlocksPM.get(BlocksPM.ANCIENT_FONT_SUN), BlocksPM.get(BlocksPM.ANCIENT_FONT_MOON)).build(null));
    public static final RegistryObject<BlockEntityType<ArtificialManaFontTileEntity>> ARTIFICIAL_MANA_FONT = TILE_ENTITIES.register("artificial_mana_font", () -> BlockEntityType.Builder.of(ArtificialManaFontTileEntity::new, BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_EARTH), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_SEA), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_SKY), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_SUN), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_MOON), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_BLOOD), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_INFERNAL), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_VOID), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_HALLOWED), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_EARTH), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_SEA), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_SKY), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_SUN), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_MOON), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_BLOOD), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_INFERNAL), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_VOID), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_HALLOWED), BlocksPM.get(BlocksPM.HEAVENLY_FONT_EARTH), BlocksPM.get(BlocksPM.HEAVENLY_FONT_SEA), BlocksPM.get(BlocksPM.HEAVENLY_FONT_SKY), BlocksPM.get(BlocksPM.HEAVENLY_FONT_SUN), BlocksPM.get(BlocksPM.HEAVENLY_FONT_MOON), BlocksPM.get(BlocksPM.HEAVENLY_FONT_BLOOD), BlocksPM.get(BlocksPM.HEAVENLY_FONT_INFERNAL), BlocksPM.get(BlocksPM.HEAVENLY_FONT_VOID), BlocksPM.get(BlocksPM.HEAVENLY_FONT_HALLOWED)).build(null));
    public static final RegistryObject<BlockEntityType<EssenceFurnaceTileEntity>> ESSENCE_FURNACE = TILE_ENTITIES.register("essence_furnace", () -> BlockEntityType.Builder.of(EssenceFurnaceTileEntity::new, BlocksPM.get(BlocksPM.ESSENCE_FURNACE)).build(null));
    public static final RegistryObject<BlockEntityType<CalcinatorTileEntity>> CALCINATOR = TILE_ENTITIES.register("calcinator", () -> BlockEntityType.Builder.of(CalcinatorTileEntity::new, BlocksPM.get(BlocksPM.CALCINATOR_BASIC), BlocksPM.get(BlocksPM.CALCINATOR_ENCHANTED), BlocksPM.get(BlocksPM.CALCINATOR_FORBIDDEN), BlocksPM.get(BlocksPM.CALCINATOR_HEAVENLY)).build(null));
    public static final RegistryObject<BlockEntityType<WandChargerTileEntity>> WAND_CHARGER = TILE_ENTITIES.register("wand_charger", () -> BlockEntityType.Builder.of(WandChargerTileEntity::new, BlocksPM.get(BlocksPM.WAND_CHARGER)).build(null));
    public static final RegistryObject<BlockEntityType<RitualAltarTileEntity>> RITUAL_ALTAR = TILE_ENTITIES.register("ritual_altar", () -> BlockEntityType.Builder.of(RitualAltarTileEntity::new, BlocksPM.get(BlocksPM.RITUAL_ALTAR)).build(null));
    public static final RegistryObject<BlockEntityType<SunlampTileEntity>> SUNLAMP = TILE_ENTITIES.register("sunlamp", () -> BlockEntityType.Builder.of(SunlampTileEntity::new, BlocksPM.get(BlocksPM.SUNLAMP), BlocksPM.get(BlocksPM.SPIRIT_LANTERN)).build(null));
    public static final RegistryObject<BlockEntityType<OfferingPedestalTileEntity>> OFFERING_PEDESTAL = TILE_ENTITIES.register("offering_pedestal", () -> BlockEntityType.Builder.of(OfferingPedestalTileEntity::new, BlocksPM.get(BlocksPM.OFFERING_PEDESTAL)).build(null));
    public static final RegistryObject<BlockEntityType<RitualCandleTileEntity>> RITUAL_CANDLE = TILE_ENTITIES.register("ritual_candle", () -> BlockEntityType.Builder.of(RitualCandleTileEntity::new, BlocksPM.get(BlocksPM.RITUAL_CANDLE_BLACK), BlocksPM.get(BlocksPM.RITUAL_CANDLE_BLUE), BlocksPM.get(BlocksPM.RITUAL_CANDLE_BROWN), BlocksPM.get(BlocksPM.RITUAL_CANDLE_CYAN), BlocksPM.get(BlocksPM.RITUAL_CANDLE_GRAY), BlocksPM.get(BlocksPM.RITUAL_CANDLE_GREEN), BlocksPM.get(BlocksPM.RITUAL_CANDLE_LIGHT_BLUE), BlocksPM.get(BlocksPM.RITUAL_CANDLE_LIGHT_GRAY), BlocksPM.get(BlocksPM.RITUAL_CANDLE_LIME), BlocksPM.get(BlocksPM.RITUAL_CANDLE_MAGENTA), BlocksPM.get(BlocksPM.RITUAL_CANDLE_ORANGE), BlocksPM.get(BlocksPM.RITUAL_CANDLE_PINK), BlocksPM.get(BlocksPM.RITUAL_CANDLE_PURPLE), BlocksPM.get(BlocksPM.RITUAL_CANDLE_RED), BlocksPM.get(BlocksPM.RITUAL_CANDLE_WHITE), BlocksPM.get(BlocksPM.RITUAL_CANDLE_YELLOW)).build(null));
    public static final RegistryObject<BlockEntityType<IncenseBrazierTileEntity>> INCENSE_BRAZIER = TILE_ENTITIES.register("incense_brazier", () -> BlockEntityType.Builder.of(IncenseBrazierTileEntity::new, BlocksPM.get(BlocksPM.INCENSE_BRAZIER)).build(null));
    public static final RegistryObject<BlockEntityType<RitualLecternTileEntity>> RITUAL_LECTERN = TILE_ENTITIES.register("ritual_lectern", () -> BlockEntityType.Builder.of(RitualLecternTileEntity::new, BlocksPM.get(BlocksPM.RITUAL_LECTERN)).build(null));
    public static final RegistryObject<BlockEntityType<RitualBellTileEntity>> RITUAL_BELL = TILE_ENTITIES.register("ritual_bell", () -> BlockEntityType.Builder.of(RitualBellTileEntity::new, BlocksPM.get(BlocksPM.RITUAL_BELL)).build(null));
    public static final RegistryObject<BlockEntityType<BloodletterTileEntity>> BLOODLETTER = TILE_ENTITIES.register("bloodletter", () -> BlockEntityType.Builder.of(BloodletterTileEntity::new, BlocksPM.get(BlocksPM.BLOODLETTER)).build(null));
    public static final RegistryObject<BlockEntityType<SoulAnvilTileEntity>> SOUL_ANVIL = TILE_ENTITIES.register("soul_anvil", () -> BlockEntityType.Builder.of(SoulAnvilTileEntity::new, BlocksPM.get(BlocksPM.SOUL_ANVIL)).build(null));
    public static final RegistryObject<BlockEntityType<RunescribingAltarTileEntity>> RUNESCRIBING_ALTAR = TILE_ENTITIES.register("runescribing_altar", () -> BlockEntityType.Builder.of(RunescribingAltarTileEntity::new, BlocksPM.get(BlocksPM.RUNESCRIBING_ALTAR_BASIC), BlocksPM.get(BlocksPM.RUNESCRIBING_ALTAR_ENCHANTED), BlocksPM.get(BlocksPM.RUNESCRIBING_ALTAR_FORBIDDEN), BlocksPM.get(BlocksPM.RUNESCRIBING_ALTAR_HEAVENLY)).build(null));
    public static final RegistryObject<BlockEntityType<HoneyExtractorTileEntity>> HONEY_EXTRACTOR = TILE_ENTITIES.register("honey_extractor", () -> BlockEntityType.Builder.of(HoneyExtractorTileEntity::new, BlocksPM.get(BlocksPM.HONEY_EXTRACTOR)).build(null));
    public static final RegistryObject<BlockEntityType<SanguineCrucibleTileEntity>> SANGUINE_CRUCIBLE = TILE_ENTITIES.register("sanguine_crucible", () -> BlockEntityType.Builder.of(SanguineCrucibleTileEntity::new, BlocksPM.get(BlocksPM.SANGUINE_CRUCIBLE)).build(null));
    public static final RegistryObject<BlockEntityType<ConcocterTileEntity>> CONCOCTER = TILE_ENTITIES.register("concocter", () -> BlockEntityType.Builder.of(ConcocterTileEntity::new, BlocksPM.get(BlocksPM.CONCOCTER)).build(null));
    public static final RegistryObject<BlockEntityType<CelestialHarpTileEntity>> CELESTIAL_HARP = TILE_ENTITIES.register("celestial_harp", () -> BlockEntityType.Builder.of(CelestialHarpTileEntity::new, BlocksPM.get(BlocksPM.CELESTIAL_HARP)).build(null));
    public static final RegistryObject<BlockEntityType<EntropySinkTileEntity>> ENTROPY_SINK = TILE_ENTITIES.register("entropy_sink", () -> BlockEntityType.Builder.of(EntropySinkTileEntity::new, BlocksPM.get(BlocksPM.ENTROPY_SINK)).build(null));
    public static final RegistryObject<BlockEntityType<ResearchTableTileEntity>> RESEARCH_TABLE = TILE_ENTITIES.register("research_table", () -> BlockEntityType.Builder.of(ResearchTableTileEntity::new, BlocksPM.get(BlocksPM.RESEARCH_TABLE)).build(null));
    public static final RegistryObject<BlockEntityType<AutoChargerTileEntity>> AUTO_CHARGER = TILE_ENTITIES.register("auto_charger", () -> BlockEntityType.Builder.of(AutoChargerTileEntity::new, BlocksPM.get(BlocksPM.AUTO_CHARGER)).build(null));
    public static final RegistryObject<BlockEntityType<RunecarvingTableTileEntity>> RUNECARVING_TABLE = TILE_ENTITIES.register("runecarving_table", () -> BlockEntityType.Builder.of(RunecarvingTableTileEntity::new, BlocksPM.get(BlocksPM.RUNECARVING_TABLE)).build(null));
    public static final RegistryObject<BlockEntityType<EssenceTransmuterTileEntity>> ESSENCE_TRANSMUTER = TILE_ENTITIES.register("essence_transmuter", () -> BlockEntityType.Builder.of(EssenceTransmuterTileEntity::new, BlocksPM.get(BlocksPM.ESSENCE_TRANSMUTER)).build(null));
    public static final RegistryObject<BlockEntityType<DissolutionChamberTileEntity>> DISSOLUTION_CHAMBER = TILE_ENTITIES.register("dissolution_chamber", () -> BlockEntityType.Builder.of(DissolutionChamberTileEntity::new, BlocksPM.get(BlocksPM.DISSOLUTION_CHAMBER)).build(null));
    public static final RegistryObject<BlockEntityType<SpellcraftingAltarTileEntity>> SPELLCRAFTING_ALTAR = TILE_ENTITIES.register("spellcrafting_altar", () -> BlockEntityType.Builder.of(SpellcraftingAltarTileEntity::new, BlocksPM.get(BlocksPM.SPELLCRAFTING_ALTAR)).build(null));
    public static final RegistryObject<BlockEntityType<WindGeneratorTileEntity>> WIND_GENERATOR = TILE_ENTITIES.register("wind_generator", () -> BlockEntityType.Builder.of(WindGeneratorTileEntity::new, BlocksPM.get(BlocksPM.ZEPHYR_ENGINE), BlocksPM.get(BlocksPM.VOID_TURBINE)).build(null));
    public static final RegistryObject<BlockEntityType<EssenceCaskTileEntity>> ESSENCE_CASK = TILE_ENTITIES.register("essence_cask", () -> BlockEntityType.Builder.of(EssenceCaskTileEntity::new, BlocksPM.get(BlocksPM.ESSENCE_CASK_ENCHANTED), BlocksPM.get(BlocksPM.ESSENCE_CASK_FORBIDDEN), BlocksPM.get(BlocksPM.ESSENCE_CASK_HEAVENLY)).build(null));
    public static final RegistryObject<BlockEntityType<InfernalFurnaceTileEntity>> INFERNAL_FURNACE = TILE_ENTITIES.register("infernal_furnace", () -> BlockEntityType.Builder.of(InfernalFurnaceTileEntity::new, BlocksPM.get(BlocksPM.INFERNAL_FURNACE)).build(null));
    public static final RegistryObject<BlockEntityType<ManaBatteryTileEntity>> MANA_BATTERY = TILE_ENTITIES.register("mana_battery", () -> BlockEntityType.Builder.of(ManaBatteryTileEntity::new, BlocksPM.get(BlocksPM.MANA_NEXUS), BlocksPM.get(BlocksPM.MANA_SINGULARITY), BlocksPM.get(BlocksPM.MANA_SINGULARITY_CREATIVE)).build(null));
    public static final RegistryObject<BlockEntityType<ScribeTableTileEntity>> SCRIBE_TABLE = TILE_ENTITIES.register("scribe_table", () -> BlockEntityType.Builder.of(ScribeTableTileEntity::new, BlocksPM.get(BlocksPM.SCRIBE_TABLE)).build(null));
    public static final RegistryObject<BlockEntityType<CarvedBookshelfTileEntity>> CARVED_BOOKSHELF = TILE_ENTITIES.register("carved_bookshelf", () -> BlockEntityType.Builder.of(CarvedBookshelfTileEntity::new, BlocksPM.get(BlocksPM.MARBLE_BOOKSHELF), BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_BOOKSHELF), BlocksPM.get(BlocksPM.MARBLE_SMOKED_BOOKSHELF), BlocksPM.get(BlocksPM.MARBLE_HALLOWED_BOOKSHELF)).build(null));
}

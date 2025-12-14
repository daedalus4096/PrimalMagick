package com.verdantartifice.primalmagick.common.tiles;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.tiles.crafting.AbstractCalcinatorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.ConcocterTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunecarvingTableTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunescribingAltarTileEntity;
import com.verdantartifice.primalmagick.common.tiles.crafting.SpellcraftingAltarTileEntity;
import com.verdantartifice.primalmagick.common.tiles.devices.DesalinatorTileEntity;
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
import com.verdantartifice.primalmagick.common.tiles.mana.ManaInjectorTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaRelayTileEntity;
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

import java.util.Set;
import java.util.function.Supplier;

/**
 * Deferred registry for mod tile entity types.
 * 
 * @author Daedalus4096
 */
public class BlockEntityTypesPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.BLOCK_ENTITY_TYPES_REGISTRY.init();
    }

    // TODO Consolidate mana font block and block entity types in next major release
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<AncientManaFontTileEntity>> ANCIENT_MANA_FONT = register("ancient_mana_font", () -> new BlockEntityType<>(AncientManaFontTileEntity::new, Set.of(BlocksPM.ANCIENT_FONT_EARTH.get(), BlocksPM.ANCIENT_FONT_SEA.get(), BlocksPM.ANCIENT_FONT_SKY.get(), BlocksPM.ANCIENT_FONT_SUN.get(), BlocksPM.ANCIENT_FONT_MOON.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<ArtificialManaFontTileEntity>> ARTIFICIAL_MANA_FONT = register("artificial_mana_font", () -> new BlockEntityType<>(ArtificialManaFontTileEntity::new, Set.of(BlocksPM.ARTIFICIAL_FONT_EARTH.get(), BlocksPM.ARTIFICIAL_FONT_SEA.get(), BlocksPM.ARTIFICIAL_FONT_SKY.get(), BlocksPM.ARTIFICIAL_FONT_SUN.get(), BlocksPM.ARTIFICIAL_FONT_MOON.get(), BlocksPM.ARTIFICIAL_FONT_BLOOD.get(), BlocksPM.ARTIFICIAL_FONT_INFERNAL.get(), BlocksPM.ARTIFICIAL_FONT_VOID.get(), BlocksPM.ARTIFICIAL_FONT_HALLOWED.get(), BlocksPM.FORBIDDEN_FONT_EARTH.get(), BlocksPM.FORBIDDEN_FONT_SEA.get(), BlocksPM.FORBIDDEN_FONT_SKY.get(), BlocksPM.FORBIDDEN_FONT_SUN.get(), BlocksPM.FORBIDDEN_FONT_MOON.get(), BlocksPM.FORBIDDEN_FONT_BLOOD.get(), BlocksPM.FORBIDDEN_FONT_INFERNAL.get(), BlocksPM.FORBIDDEN_FONT_VOID.get(), BlocksPM.FORBIDDEN_FONT_HALLOWED.get(), BlocksPM.HEAVENLY_FONT_EARTH.get(), BlocksPM.HEAVENLY_FONT_SEA.get(), BlocksPM.HEAVENLY_FONT_SKY.get(), BlocksPM.HEAVENLY_FONT_SUN.get(), BlocksPM.HEAVENLY_FONT_MOON.get(), BlocksPM.HEAVENLY_FONT_BLOOD.get(), BlocksPM.HEAVENLY_FONT_INFERNAL.get(), BlocksPM.HEAVENLY_FONT_VOID.get(), BlocksPM.HEAVENLY_FONT_HALLOWED.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<AbstractCalcinatorTileEntity>> ESSENCE_FURNACE = register("essence_furnace", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.essenceFurnace(), Set.of(BlocksPM.ESSENCE_FURNACE.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<AbstractCalcinatorTileEntity>> CALCINATOR = register("calcinator", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.calcinator(), Set.of(BlocksPM.CALCINATOR_BASIC.get(), BlocksPM.CALCINATOR_ENCHANTED.get(), BlocksPM.CALCINATOR_FORBIDDEN.get(), BlocksPM.CALCINATOR_HEAVENLY.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<WandChargerTileEntity>> WAND_CHARGER = register("wand_charger", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.wandCharger(), Set.of(BlocksPM.WAND_CHARGER.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<RitualAltarTileEntity>> RITUAL_ALTAR = register("ritual_altar", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.ritualAltar(), Set.of(BlocksPM.RITUAL_ALTAR.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<SunlampTileEntity>> SUNLAMP = register("sunlamp", () -> new BlockEntityType<>(SunlampTileEntity::new, Set.of(BlocksPM.SUNLAMP.get(), BlocksPM.SPIRIT_LANTERN.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<OfferingPedestalTileEntity>> OFFERING_PEDESTAL = register("offering_pedestal", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.offeringPedestal(), Set.of(BlocksPM.OFFERING_PEDESTAL.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<RitualCandleTileEntity>> RITUAL_CANDLE = register("ritual_candle", () -> new BlockEntityType<>(RitualCandleTileEntity::new, Set.of(BlocksPM.RITUAL_CANDLE_BLACK.get(), BlocksPM.RITUAL_CANDLE_BLUE.get(), BlocksPM.RITUAL_CANDLE_BROWN.get(), BlocksPM.RITUAL_CANDLE_CYAN.get(), BlocksPM.RITUAL_CANDLE_GRAY.get(), BlocksPM.RITUAL_CANDLE_GREEN.get(), BlocksPM.RITUAL_CANDLE_LIGHT_BLUE.get(), BlocksPM.RITUAL_CANDLE_LIGHT_GRAY.get(), BlocksPM.RITUAL_CANDLE_LIME.get(), BlocksPM.RITUAL_CANDLE_MAGENTA.get(), BlocksPM.RITUAL_CANDLE_ORANGE.get(), BlocksPM.RITUAL_CANDLE_PINK.get(), BlocksPM.RITUAL_CANDLE_PURPLE.get(), BlocksPM.RITUAL_CANDLE_RED.get(), BlocksPM.RITUAL_CANDLE_WHITE.get(), BlocksPM.RITUAL_CANDLE_YELLOW.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<IncenseBrazierTileEntity>> INCENSE_BRAZIER = register("incense_brazier", () -> new BlockEntityType<>(IncenseBrazierTileEntity::new, Set.of(BlocksPM.INCENSE_BRAZIER.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<RitualLecternTileEntity>> RITUAL_LECTERN = register("ritual_lectern", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.ritualLectern(), Set.of(BlocksPM.RITUAL_LECTERN.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<RitualBellTileEntity>> RITUAL_BELL = register("ritual_bell", () -> new BlockEntityType<>(RitualBellTileEntity::new, Set.of(BlocksPM.RITUAL_BELL.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<BloodletterTileEntity>> BLOODLETTER = register("bloodletter", () -> new BlockEntityType<>(BloodletterTileEntity::new, Set.of(BlocksPM.BLOODLETTER.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<SoulAnvilTileEntity>> SOUL_ANVIL = register("soul_anvil", () -> new BlockEntityType<>(SoulAnvilTileEntity::new, Set.of(BlocksPM.SOUL_ANVIL.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<RunescribingAltarTileEntity>> RUNESCRIBING_ALTAR = register("runescribing_altar", () -> new BlockEntityType<>(RunescribingAltarTileEntity::new, Set.of(BlocksPM.RUNESCRIBING_ALTAR_BASIC.get(), BlocksPM.RUNESCRIBING_ALTAR_ENCHANTED.get(), BlocksPM.RUNESCRIBING_ALTAR_FORBIDDEN.get(), BlocksPM.RUNESCRIBING_ALTAR_HEAVENLY.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<HoneyExtractorTileEntity>> HONEY_EXTRACTOR = register("honey_extractor", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.honeyExtractor(), Set.of(BlocksPM.HONEY_EXTRACTOR.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<SanguineCrucibleTileEntity>> SANGUINE_CRUCIBLE = register("sanguine_crucible", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.sanguineCrucible(), Set.of(BlocksPM.SANGUINE_CRUCIBLE.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<ConcocterTileEntity>> CONCOCTER = register("concocter", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.concocter(), Set.of(BlocksPM.CONCOCTER.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<CelestialHarpTileEntity>> CELESTIAL_HARP = register("celestial_harp", () -> new BlockEntityType<>(CelestialHarpTileEntity::new, Set.of(BlocksPM.CELESTIAL_HARP.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<EntropySinkTileEntity>> ENTROPY_SINK = register("entropy_sink", () -> new BlockEntityType<>(EntropySinkTileEntity::new, Set.of(BlocksPM.ENTROPY_SINK.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<ResearchTableTileEntity>> RESEARCH_TABLE = register("research_table", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.researchTable(), Set.of(BlocksPM.RESEARCH_TABLE.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<AutoChargerTileEntity>> AUTO_CHARGER = register("auto_charger", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.autoCharger(), Set.of(BlocksPM.AUTO_CHARGER.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<RunecarvingTableTileEntity>> RUNECARVING_TABLE = register("runecarving_table", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.runecarvingTable(), Set.of(BlocksPM.RUNECARVING_TABLE.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<EssenceTransmuterTileEntity>> ESSENCE_TRANSMUTER = register("essence_transmuter", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.essenceTransmuter(), Set.of(BlocksPM.ESSENCE_TRANSMUTER.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<DissolutionChamberTileEntity>> DISSOLUTION_CHAMBER = register("dissolution_chamber", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.dissolutionChamber(), Set.of(BlocksPM.DISSOLUTION_CHAMBER.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<SpellcraftingAltarTileEntity>> SPELLCRAFTING_ALTAR = register("spellcrafting_altar", () -> new BlockEntityType<>(SpellcraftingAltarTileEntity::new, Set.of(BlocksPM.SPELLCRAFTING_ALTAR.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<WindGeneratorTileEntity>> WIND_GENERATOR = register("wind_generator", () -> new BlockEntityType<>(WindGeneratorTileEntity::new, Set.of(BlocksPM.ZEPHYR_ENGINE.get(), BlocksPM.VOID_TURBINE.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<EssenceCaskTileEntity>> ESSENCE_CASK = register("essence_cask", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.essenceCask(), Set.of(BlocksPM.ESSENCE_CASK_ENCHANTED.get(), BlocksPM.ESSENCE_CASK_FORBIDDEN.get(), BlocksPM.ESSENCE_CASK_HEAVENLY.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<InfernalFurnaceTileEntity>> INFERNAL_FURNACE = register("infernal_furnace", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.infernalFurnace(), Set.of(BlocksPM.INFERNAL_FURNACE.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<ManaBatteryTileEntity>> MANA_BATTERY = register("mana_battery", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.manaBattery(), Set.of(BlocksPM.MANA_NEXUS.get(), BlocksPM.MANA_SINGULARITY.get(), BlocksPM.MANA_SINGULARITY_CREATIVE.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<ScribeTableTileEntity>> SCRIBE_TABLE = register("scribe_table", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.scribeTable(), Set.of(BlocksPM.SCRIBE_TABLE.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<CarvedBookshelfTileEntity>> CARVED_BOOKSHELF = register("carved_bookshelf", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.carvedBookshelf(), Set.of(BlocksPM.MARBLE_BOOKSHELF.get(), BlocksPM.MARBLE_ENCHANTED_BOOKSHELF.get(), BlocksPM.MARBLE_SMOKED_BOOKSHELF.get(), BlocksPM.MARBLE_HALLOWED_BOOKSHELF.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<ManaRelayTileEntity>> MANA_RELAY = register("mana_relay", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.manaRelay(), Set.of(BlocksPM.MANA_RELAY_BASIC.get(), BlocksPM.MANA_RELAY_ENCHANTED.get(), BlocksPM.MANA_RELAY_FORBIDDEN.get(), BlocksPM.MANA_RELAY_HEAVENLY.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<ManaInjectorTileEntity>> MANA_INJECTOR = register("mana_injector", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.manaInjector(), Set.of(BlocksPM.MANA_INJECTOR_BASIC.get(), BlocksPM.MANA_INJECTOR_ENCHANTED.get(), BlocksPM.MANA_INJECTOR_FORBIDDEN.get(), BlocksPM.MANA_INJECTOR_HEAVENLY.get())));
    public static final IRegistryItem<BlockEntityType<?>, BlockEntityType<DesalinatorTileEntity>> DESALINATOR = register("desalinator", () -> new BlockEntityType<>(Services.BLOCK_ENTITY_PROTOTYPES.desalinator(), Set.of(BlocksPM.DESALINATOR.get())));

    private static <T extends BlockEntity> IRegistryItem<BlockEntityType<?>, BlockEntityType<T>> register(String name, Supplier<BlockEntityType<T>> typeSupplier) {
        return Services.BLOCK_ENTITY_TYPES_REGISTRY.register(name, typeSupplier);
    }
}

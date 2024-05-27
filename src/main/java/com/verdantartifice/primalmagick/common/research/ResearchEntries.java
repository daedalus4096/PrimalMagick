package com.verdantartifice.primalmagick.common.research;

import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.datagen.research.ResearchAddendumBuilder;
import com.verdantartifice.primalmagick.datagen.research.ResearchEntryBuilder;
import com.verdantartifice.primalmagick.datagen.research.ResearchStageBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

/**
 * Datapack registry for the mod's research entries, the backbone of its progression system.
 * 
 * @author Daedalus4096
 */
public class ResearchEntries {
    // Fundamentals research entries
    public static final ResourceKey<ResearchEntry> FIRST_STEPS = create("first_steps");
    public static final ResourceKey<ResearchEntry> THEORYCRAFTING = create("theorycrafting");
    public static final ResourceKey<ResearchEntry> ATTUNEMENTS = create("attunements");
    public static final ResourceKey<ResearchEntry> LINGUISTICS = create("linguistics");
    public static final ResourceKey<ResearchEntry> UNLOCK_MANAWEAVING = create("unlock_manaweaving");
    public static final ResourceKey<ResearchEntry> UNLOCK_ALCHEMY = create("unlock_alchemy");
    public static final ResourceKey<ResearchEntry> UNLOCK_SORCERY = create("unlock_sorcery");
    public static final ResourceKey<ResearchEntry> UNLOCK_RUNEWORKING = create("unlock_runeworking");
    public static final ResourceKey<ResearchEntry> UNLOCK_RITUAL = create("unlock_ritual");
    public static final ResourceKey<ResearchEntry> UNLOCK_MAGITECH = create("unlock_magitech");
    public static final ResourceKey<ResearchEntry> TERRESTRIAL_MAGICK = create("terrestrial_magick");
    public static final ResourceKey<ResearchEntry> SOURCE_EARTH = create("source_earth");
    public static final ResourceKey<ResearchEntry> SOURCE_SEA = create("source_sea");
    public static final ResourceKey<ResearchEntry> SOURCE_SKY = create("source_sky");
    public static final ResourceKey<ResearchEntry> SOURCE_SUN = create("source_sun");
    public static final ResourceKey<ResearchEntry> SOURCE_MOON = create("source_moon");
    public static final ResourceKey<ResearchEntry> FORBIDDEN_MAGICK = create("forbidden_magick");
    public static final ResourceKey<ResearchEntry> SOURCE_BLOOD = create("source_blood");
    public static final ResourceKey<ResearchEntry> SOURCE_INFERNAL = create("source_infernal");
    public static final ResourceKey<ResearchEntry> SOURCE_VOID = create("source_void");
    public static final ResourceKey<ResearchEntry> HEAVENLY_MAGICK = create("heavenly_magick");
    public static final ResourceKey<ResearchEntry> SOURCE_HALLOWED = create("source_hallowed");
    public static final ResourceKey<ResearchEntry> SECRETS_OF_THE_UNIVERSE = create("secrets_of_the_universe");
    public static final ResourceKey<ResearchEntry> COMPLETE_BASICS = create("complete_basics");
    public static final ResourceKey<ResearchEntry> THEORY_OF_EVERYTHING = create("theory_of_everything");
    
    // Manaweaving research entries
    // TODO Place in correct order after bootstrapping
    public static final ResourceKey<ResearchEntry> BASIC_MANAWEAVING = create("basic_manaweaving");
    public static final ResourceKey<ResearchEntry> EXPERT_MANAWEAVING = create("expert_manaweaving");
    public static final ResourceKey<ResearchEntry> MASTER_MANAWEAVING = create("master_manaweaving");
    public static final ResourceKey<ResearchEntry> SUPREME_MANAWEAVING = create("supreme_manaweaving");
    public static final ResourceKey<ResearchEntry> COMPLETE_MANAWEAVING = create("complete_manaweaving");
    public static final ResourceKey<ResearchEntry> MANA_ARROWS = create("mana_arrows");
    public static final ResourceKey<ResearchEntry> WAND_CHARGER = create("wand_charger");
    public static final ResourceKey<ResearchEntry> MANA_SALTS = create("mana_salts");
    public static final ResourceKey<ResearchEntry> ADVANCED_WANDMAKING = create("advanced_wandmaking");
    public static final ResourceKey<ResearchEntry> STAVES = create("staves");
    public static final ResourceKey<ResearchEntry> EARTHSHATTER_HAMMER = create("earthshatter_hammer");
    public static final ResourceKey<ResearchEntry> SUNLAMP = create("sunlamp");
    public static final ResourceKey<ResearchEntry> IMBUED_WOOL = create("imbued_wool");
    public static final ResourceKey<ResearchEntry> SPELLCLOTH = create("spellcloth");
    public static final ResourceKey<ResearchEntry> HEXWEAVE = create("hexweave");
    public static final ResourceKey<ResearchEntry> SAINTSWOOL = create("saintswool");
    public static final ResourceKey<ResearchEntry> ARTIFICIAL_MANA_FONTS = create("artificial_mana_fonts");
    public static final ResourceKey<ResearchEntry> FORBIDDEN_MANA_FONTS = create("forbidden_mana_fonts");
    public static final ResourceKey<ResearchEntry> HEAVENLY_MANA_FONTS = create("heavenly_mana_fonts");
    public static final ResourceKey<ResearchEntry> ESSENCE_CASK_ENCHANTED = create("essence_cask_enchanted");
    public static final ResourceKey<ResearchEntry> ESSENCE_CASK_FORBIDDEN = create("essence_cask_forbidden");
    public static final ResourceKey<ResearchEntry> ESSENCE_CASK_HEAVENLY = create("essence_cask_heavenly");
    public static final ResourceKey<ResearchEntry> WAND_GLAMOUR_TABLE = create("wand_glamour_table");
    public static final ResourceKey<ResearchEntry> ATTUNEMENT_SHACKLES = create("attunement_shackles");
    public static final ResourceKey<ResearchEntry> WAND_CORE_HEARTWOOD = create("wand_core_heartwood");
    public static final ResourceKey<ResearchEntry> WAND_CORE_OBSIDIAN = create("wand_core_obsidian");
    public static final ResourceKey<ResearchEntry> WAND_CORE_CORAL = create("wand_core_coral");
    public static final ResourceKey<ResearchEntry> WAND_CORE_BAMBOO = create("wand_core_bamboo");
    public static final ResourceKey<ResearchEntry> WAND_CORE_SUNWOOD = create("wand_core_sunwood");
    public static final ResourceKey<ResearchEntry> WAND_CORE_MOONWOOD = create("wand_core_moonwood");
    public static final ResourceKey<ResearchEntry> WAND_CORE_BONE = create("wand_core_bone");
    public static final ResourceKey<ResearchEntry> WAND_CORE_BLAZE_ROD = create("wand_core_blaze_rod");
    public static final ResourceKey<ResearchEntry> WAND_CORE_PURPUR = create("wand_core_purpur");
    public static final ResourceKey<ResearchEntry> WAND_CAP_IRON = create("wand_cap_iron");
    public static final ResourceKey<ResearchEntry> WAND_CAP_GOLD = create("wand_cap_gold");
    public static final ResourceKey<ResearchEntry> WAND_CAP_PRIMALITE = create("wand_cap_primalite");
    public static final ResourceKey<ResearchEntry> WAND_CAP_HEXIUM = create("wand_cap_hexium");
    public static final ResourceKey<ResearchEntry> WAND_CAP_HALLOWSTEEL = create("wand_cap_hallowsteel");
    public static final ResourceKey<ResearchEntry> WAND_GEM_APPRENTICE = create("wand_gem_apprentice");
    public static final ResourceKey<ResearchEntry> WAND_GEM_ADEPT = create("wand_gem_adept");
    public static final ResourceKey<ResearchEntry> WAND_GEM_WIZARD = create("wand_gem_wizard");
    public static final ResourceKey<ResearchEntry> WAND_GEM_ARCHMAGE = create("wand_gem_archmage");

    // Alchemy research entries
    // TODO Place in correct order after bootstrapping
    public static final ResourceKey<ResearchEntry> BASIC_ALCHEMY = create("basic_alchemy");
    public static final ResourceKey<ResearchEntry> EXPERT_ALCHEMY = create("expert_alchemy");
    public static final ResourceKey<ResearchEntry> MASTER_ALCHEMY = create("master_alchemy");
    public static final ResourceKey<ResearchEntry> SUPREME_ALCHEMY = create("supreme_alchemy");
    public static final ResourceKey<ResearchEntry> COMPLETE_ALCHEMY = create("complete_alchemy");
    public static final ResourceKey<ResearchEntry> STONEMELDING = create("stonemelding");
    public static final ResourceKey<ResearchEntry> SKYGLASS = create("skyglass");
    public static final ResourceKey<ResearchEntry> SHARD_SYNTHESIS = create("shard_synthesis");
    public static final ResourceKey<ResearchEntry> SHARD_DESYNTHESIS = create("shard_desynthesis");
    public static final ResourceKey<ResearchEntry> PRIMALITE = create("primalite");
    public static final ResourceKey<ResearchEntry> CRYSTAL_SYNTHESIS = create("crystal_synthesis");
    public static final ResourceKey<ResearchEntry> CRYSTAL_DESYNTHESIS = create("crystal_desynthesis");
    public static final ResourceKey<ResearchEntry> HEXIUM = create("hexium");
    public static final ResourceKey<ResearchEntry> CLUSTER_SYNTHESIS = create("cluster_synthesis");
    public static final ResourceKey<ResearchEntry> CLUSTER_DESYNTHESIS = create("cluster_desynthesis");
    public static final ResourceKey<ResearchEntry> HALLOWSTEEL = create("hallowsteel");
    public static final ResourceKey<ResearchEntry> CALCINATOR_BASIC = create("calcinator_basic");
    public static final ResourceKey<ResearchEntry> CALCINATOR_ENCHANTED = create("calcinator_enchanted");
    public static final ResourceKey<ResearchEntry> CALCINATOR_FORBIDDEN = create("calcinator_forbidden");
    public static final ResourceKey<ResearchEntry> CALCINATOR_HEAVENLY = create("calcinator_heavenly");
    public static final ResourceKey<ResearchEntry> CRYOTREATMENT = create("cryotreatment");
    public static final ResourceKey<ResearchEntry> SANGUINE_CRUCIBLE = create("sanguine_crucible");
    public static final ResourceKey<ResearchEntry> SANGUINE_CORE_LAND_ANIMALS = create("sanguine_core_land_animals");
    public static final ResourceKey<ResearchEntry> SANGUINE_CORE_SEA_CREATURES = create("sanguine_core_sea_creatures");
    public static final ResourceKey<ResearchEntry> SANGUINE_CORE_FLYING_CREATURES = create("sanguine_core_flying_creatures");
    public static final ResourceKey<ResearchEntry> SANGUINE_CORE_PLANTS = create("sanguine_core_plants");
    public static final ResourceKey<ResearchEntry> SANGUINE_CORE_UNDEAD = create("sanguine_core_undead");
    public static final ResourceKey<ResearchEntry> SANGUINE_CORE_SAPIENTS = create("sanguine_core_sapients");
    public static final ResourceKey<ResearchEntry> SANGUINE_CORE_DEMONS = create("sanguine_core_demons");
    public static final ResourceKey<ResearchEntry> SANGUINE_CORE_ALIENS = create("sanguine_core_aliens");
    public static final ResourceKey<ResearchEntry> IGNYX = create("ignyx");
    public static final ResourceKey<ResearchEntry> SYNTHETIC_GEM_BUDS = create("synthetic_gem_buds");

    // Sorcery research entries
    // TODO Place in correct order after bootstrapping
    public static final ResourceKey<ResearchEntry> BASIC_SORCERY = create("basic_sorcery");
    public static final ResourceKey<ResearchEntry> EXPERT_SORCERY = create("expert_sorcery");
    public static final ResourceKey<ResearchEntry> MASTER_SORCERY = create("master_sorcery");
    public static final ResourceKey<ResearchEntry> SUPREME_SORCERY = create("supreme_sorcery");
    public static final ResourceKey<ResearchEntry> COMPLETE_SORCERY = create("complete_sorcery");
    public static final ResourceKey<ResearchEntry> WAND_INSCRIPTION = create("wand_inscription");
    public static final ResourceKey<ResearchEntry> SPELL_VEHICLE_PROJECTILE = create("spell_vehicle_projectile");
    public static final ResourceKey<ResearchEntry> SPELL_VEHICLE_BOLT = create("spell_vehicle_bolt");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_FROST = create("spell_payload_frost");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_LIGHTNING = create("spell_payload_lightning");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_SOLAR = create("spell_payload_solar");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_LUNAR = create("spell_payload_lunar");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_BLOOD = create("spell_payload_blood");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_FLAME = create("spell_payload_flame");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_VOID = create("spell_payload_void");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_HOLY = create("spell_payload_holy");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_BREAK = create("spell_payload_break");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_CONJURE_STONE = create("spell_payload_conjure_stone");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_CONJURE_WATER = create("spell_payload_conjure_water");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_SHEAR = create("spell_payload_shear");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_FLIGHT = create("spell_payload_flight");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_CONJURE_LIGHT = create("spell_payload_conjure_light");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_HEALING = create("spell_payload_healing");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_POLYMORPH = create("spell_payload_polymorph");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_POLYMORPH_SHEEP = create("spell_payload_polymorph_sheep");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_CONJURE_ANIMAL = create("spell_payload_conjure_animal");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_CONJURE_LAVA = create("spell_payload_conjure_lava");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_DRAIN_SOUL = create("spell_payload_drain_soul");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_TELEPORT = create("spell_payload_teleport");
    public static final ResourceKey<ResearchEntry> SPELL_PAYLOAD_CONSECRATE = create("spell_payload_consecrate");
    public static final ResourceKey<ResearchEntry> SPELL_MOD_AMPLIFY = create("spell_mod_amplify");
    public static final ResourceKey<ResearchEntry> SPELL_MOD_BURST = create("spell_mod_burst");
    public static final ResourceKey<ResearchEntry> SPELL_MOD_QUICKEN = create("spell_mod_quicken");
    public static final ResourceKey<ResearchEntry> SPELL_MOD_MINE = create("spell_mod_mine");
    public static final ResourceKey<ResearchEntry> SPELL_MOD_FORK = create("spell_mod_fork");
    
    // Runeworking research entries
    // TODO Place in correct order after bootstrapping
    public static final ResourceKey<ResearchEntry> BASIC_RUNEWORKING = create("basic_runeworking");
    public static final ResourceKey<ResearchEntry> EXPERT_RUNEWORKING = create("expert_runeworking");
    public static final ResourceKey<ResearchEntry> MASTER_RUNEWORKING = create("master_runeworking");
    public static final ResourceKey<ResearchEntry> SUPREME_RUNEWORKING = create("supreme_runeworking");
    public static final ResourceKey<ResearchEntry> COMPLETE_RUNEWORKING = create("complete_runeworking");
    public static final ResourceKey<ResearchEntry> RUNE_EARTH = create("rune_earth");
    public static final ResourceKey<ResearchEntry> RUNE_SEA = create("rune_sea");
    public static final ResourceKey<ResearchEntry> RUNE_SKY = create("rune_sky");
    public static final ResourceKey<ResearchEntry> RUNE_SUN = create("rune_sun");
    public static final ResourceKey<ResearchEntry> RUNE_MOON = create("rune_moon");
    public static final ResourceKey<ResearchEntry> RUNE_BLOOD = create("rune_blood");
    public static final ResourceKey<ResearchEntry> RUNE_INFERNAL = create("rune_infernal");
    public static final ResourceKey<ResearchEntry> RUNE_VOID = create("rune_void");
    public static final ResourceKey<ResearchEntry> RUNE_HALLOWED = create("rune_hallowed");
    public static final ResourceKey<ResearchEntry> RUNE_PROJECT = create("rune_project");
    public static final ResourceKey<ResearchEntry> RUNE_PROTECT = create("rune_protect");
    public static final ResourceKey<ResearchEntry> RUNE_ABSORB = create("rune_absorb");
    public static final ResourceKey<ResearchEntry> RUNE_DISPEL = create("rune_dispel");
    public static final ResourceKey<ResearchEntry> RUNE_SUMMON = create("rune_summon");
    public static final ResourceKey<ResearchEntry> RUNE_ITEM = create("rune_item");
    public static final ResourceKey<ResearchEntry> RUNE_SELF = create("rune_self");
    public static final ResourceKey<ResearchEntry> RUNE_CREATURE = create("rune_creature");
    public static final ResourceKey<ResearchEntry> RUNE_AREA = create("rune_area");
    public static final ResourceKey<ResearchEntry> RUNE_INSIGHT = create("rune_insight");
    public static final ResourceKey<ResearchEntry> RUNE_POWER = create("rune_power");
    public static final ResourceKey<ResearchEntry> RUNE_GRACE = create("rune_grace");
    public static final ResourceKey<ResearchEntry> RUNIC_GRINDSTONE = create("runic_grindstone");
    public static final ResourceKey<ResearchEntry> RECALL_STONE = create("recall_stone");
    public static final ResourceKey<ResearchEntry> RUNIC_TRIM = create("runic_trim");
    public static final ResourceKey<ResearchEntry> ENDERWARD = create("enderward");
    
    // Ritual Magick research entries
    // TODO Place in correct order after bootstrapping
    public static final ResourceKey<ResearchEntry> BASIC_RITUAL = create("basic_ritual");
    public static final ResourceKey<ResearchEntry> EXPERT_RITUAL = create("expert_ritual");
    public static final ResourceKey<ResearchEntry> MASTER_RITUAL = create("master_ritual");
    public static final ResourceKey<ResearchEntry> SUPREME_RITUAL = create("supreme_ritual");
    public static final ResourceKey<ResearchEntry> COMPLETE_RITUAL = create("complete_ritual");
    public static final ResourceKey<ResearchEntry> RITUAL_CANDLES = create("ritual_candles");
    public static final ResourceKey<ResearchEntry> INCENSE_BRAZIER = create("incense_brazier");
    public static final ResourceKey<ResearchEntry> MANAFRUIT = create("manafruit");
    public static final ResourceKey<ResearchEntry> RITUAL_LECTERN = create("ritual_lectern");
    public static final ResourceKey<ResearchEntry> RITUAL_BELL = create("ritual_bell");
    public static final ResourceKey<ResearchEntry> BLOODLETTER = create("bloodletter");
    public static final ResourceKey<ResearchEntry> SOUL_ANVIL = create("soul_anvil");
    public static final ResourceKey<ResearchEntry> CELESTIAL_HARP = create("celestial_harp");
    public static final ResourceKey<ResearchEntry> WAND_CORE_PRIMAL = create("wand_core_primal");
    public static final ResourceKey<ResearchEntry> WAND_CORE_DARK_PRIMAL = create("wand_core_dark_primal");
    public static final ResourceKey<ResearchEntry> WAND_CORE_PURE_PRIMAL = create("wand_core_pure_primal");
    public static final ResourceKey<ResearchEntry> PIXIES = create("pixies");
    public static final ResourceKey<ResearchEntry> GRAND_PIXIES = create("grand_pixies");
    public static final ResourceKey<ResearchEntry> MAJESTIC_PIXIES = create("majestic_pixies");
    public static final ResourceKey<ResearchEntry> AMBROSIA = create("ambrosia");
    public static final ResourceKey<ResearchEntry> GREATER_AMBROSIA = create("greater_ambrosia");
    public static final ResourceKey<ResearchEntry> SUPREME_AMBROSIA = create("supreme_ambrosia");
    public static final ResourceKey<ResearchEntry> FLYING_CARPET = create("flying_carpet");
    public static final ResourceKey<ResearchEntry> CLEANSING_RITE = create("cleansing_rite");
    public static final ResourceKey<ResearchEntry> PRIMAL_SHOVEL = create("primal_shovel");
    public static final ResourceKey<ResearchEntry> PRIMAL_FISHING_ROD = create("primal_fishing_rod");
    public static final ResourceKey<ResearchEntry> PRIMAL_AXE = create("primal_axe");
    public static final ResourceKey<ResearchEntry> PRIMAL_HOE = create("primal_hoe");
    public static final ResourceKey<ResearchEntry> PRIMAL_PICKAXE = create("primal_pickaxe");
    public static final ResourceKey<ResearchEntry> FORBIDDEN_TRIDENT = create("forbidden_trident");
    public static final ResourceKey<ResearchEntry> FORBIDDEN_BOW = create("forbidden_bow");
    public static final ResourceKey<ResearchEntry> FORBIDDEN_SWORD = create("forbidden_sword");
    public static final ResourceKey<ResearchEntry> SACRED_SHIELD = create("sacred_shield");
    public static final ResourceKey<ResearchEntry> DREAM_VISION_TALISMAN = create("dream_vision_talisman");
    public static final ResourceKey<ResearchEntry> DOWSING_ROD = create("dowsing_rod");
    public static final ResourceKey<ResearchEntry> HYDROMELON = create("hydromelon");
    public static final ResourceKey<ResearchEntry> BLOOD_ROSE = create("blood_rose");
    public static final ResourceKey<ResearchEntry> EMBERFLOWER = create("emberflower");

    // TODO Magitech research entries
    // TODO Place in correct order after bootstrapping
    public static final ResourceKey<ResearchEntry> BASIC_MAGITECH = create("basic_magitech");
    public static final ResourceKey<ResearchEntry> EXPERT_MAGITECH = create("expert_magitech");
    public static final ResourceKey<ResearchEntry> MASTER_MAGITECH = create("master_magitech");
    public static final ResourceKey<ResearchEntry> SUPREME_MAGITECH = create("supreme_magitech");
    public static final ResourceKey<ResearchEntry> COMPLETE_MAGITECH = create("complete_magitech");
    public static final ResourceKey<ResearchEntry> HONEY_EXTRACTOR = create("honey_extractor");
    public static final ResourceKey<ResearchEntry> SEASCRIBE_PEN = create("seascribe_pen");
    public static final ResourceKey<ResearchEntry> ARCANOMETER = create("arcanometer");
    public static final ResourceKey<ResearchEntry> PRIMALITE_GOLEM = create("primalite_golem");
    public static final ResourceKey<ResearchEntry> HEXIUM_GOLEM = create("hexium_golem");
    public static final ResourceKey<ResearchEntry> HALLOWSTEEL_GOLEM = create("hallowsteel_golem");
    public static final ResourceKey<ResearchEntry> CONCOCTING_TINCTURES = create("concocting_tinctures");
    public static final ResourceKey<ResearchEntry> CONCOCTING_PHILTERS = create("concocting_philters");
    public static final ResourceKey<ResearchEntry> CONCOCTING_ELIXIRS = create("concocting_elixirs");
    public static final ResourceKey<ResearchEntry> CONCOCTING_BOMBS = create("concocting_bombs");
    public static final ResourceKey<ResearchEntry> ENTROPY_SINK = create("entropy_sink");
    public static final ResourceKey<ResearchEntry> AUTO_CHARGER = create("auto_charger");
    public static final ResourceKey<ResearchEntry> ESSENCE_TRANSMUTER = create("essence_transmuter");
    public static final ResourceKey<ResearchEntry> DISSOLUTION_CHAMBER = create("dissolution_chamber");
    public static final ResourceKey<ResearchEntry> ZEPHYR_ENGINE = create("zephyr_engine");
    public static final ResourceKey<ResearchEntry> VOID_TURBINE = create("void_turbine");
    public static final ResourceKey<ResearchEntry> INFERNAL_FURNACE = create("infernal_furnace");
    public static final ResourceKey<ResearchEntry> MANA_NEXUS = create("mana_nexus");
    public static final ResourceKey<ResearchEntry> MANA_SINGULARITY = create("mana_singularity");
    public static final ResourceKey<ResearchEntry> WARDING_MODULE = create("warding_module");
    public static final ResourceKey<ResearchEntry> GREATER_WARDING_MODULE = create("greater_warding_module");
    public static final ResourceKey<ResearchEntry> SUPREME_WARDING_MODULE = create("supreme_warding_module");

    // Scans research entries
    // TODO Place in correct order after bootstrapping
    public static final ResourceKey<ResearchEntry> RAW_MARBLE = create("raw_marble");
    public static final ResourceKey<ResearchEntry> ROCK_SALT = create("rock_salt");
    public static final ResourceKey<ResearchEntry> QUARTZ = create("quartz");
    public static final ResourceKey<ResearchEntry> SUNWOOD_TREES = create("sunwood_trees");
    public static final ResourceKey<ResearchEntry> MOONWOOD_TREES = create("moonwood_trees");
    public static final ResourceKey<ResearchEntry> HALLOWED_ORB = create("hallowed_orb");
    public static final ResourceKey<ResearchEntry> HALLOWOOD_TREES = create("hallowood_trees");
    public static final ResourceKey<ResearchEntry> BOOKSHELF = create("bookshelf");
    public static final ResourceKey<ResearchEntry> BEEHIVE = create("beehive");
    public static final ResourceKey<ResearchEntry> BEACON = create("beacon");
    public static final ResourceKey<ResearchEntry> DRAGON_EGG = create("dragon_egg");
    public static final ResourceKey<ResearchEntry> DRAGON_HEAD = create("dragon_head");
    public static final ResourceKey<ResearchEntry> INNER_DEMON = create("inner_demon");
    public static final ResourceKey<ResearchEntry> TREEFOLK = create("treefolk");
    public static final ResourceKey<ResearchEntry> ALCHEMICAL_WASTE = create("alchemical_waste");
    public static final ResourceKey<ResearchEntry> MYSTICAL_RELIC = create("mystical_relic");
    public static final ResourceKey<ResearchEntry> HUMMING_ARTIFACT = create("humming_artifact");
    
    // Internal research entries
    public static final ResourceKey<ResearchEntry> UNLOCK_SCANS = create("unlock_scans");
    public static final ResourceKey<ResearchEntry> UNLOCK_RUNE_ENCHANTMENTS = create("unlock_rune_enchantments");
    public static final ResourceKey<ResearchEntry> DISCOVER_BLOOD = create("discover_blood");
    public static final ResourceKey<ResearchEntry> DISCOVER_INFERNAL = create("discover_infernal");
    public static final ResourceKey<ResearchEntry> DISCOVER_VOID = create("discover_void");
    public static final ResourceKey<ResearchEntry> DISCOVER_FORBIDDEN = create("discover_forbidden");
    public static final ResourceKey<ResearchEntry> DISCOVER_HALLOWED = create("discover_hallowed");
    public static final ResourceKey<ResearchEntry> ENV_EARTH = create("env_earth");
    public static final ResourceKey<ResearchEntry> ENV_SEA = create("env_sea");
    public static final ResourceKey<ResearchEntry> ENV_SKY = create("env_sky");
    public static final ResourceKey<ResearchEntry> ENV_SUN = create("env_sun");
    public static final ResourceKey<ResearchEntry> ENV_MOON = create("env_moon");
    public static final ResourceKey<ResearchEntry> SOTU_DISCOVER_BLOOD = create("sotu_discover_blood");
    public static final ResourceKey<ResearchEntry> SOTU_DISCOVER_INFERNAL = create("sotu_discover_infernal");
    public static final ResourceKey<ResearchEntry> SOTU_DISCOVER_VOID = create("sotu_discover_void");
    public static final ResourceKey<ResearchEntry> SOTU_RESEARCH_ARCANOMETER = create("sotu_research_arcanometer");
    public static final ResourceKey<ResearchEntry> SOTU_RESEARCH_HEXIUM = create("sotu_research_hexium");
    public static final ResourceKey<ResearchEntry> SOTU_RESEARCH_POWER_RUNE = create("sotu_research_power_rune");
    public static final ResourceKey<ResearchEntry> SOTU_RESEARCH_SANGUINE_CRUCIBLE = create("sotu_research_sanguine_crucible");
    public static final ResourceKey<ResearchEntry> SOTU_RESEARCH_CLEANSING_RITE = create("sotu_research_cleansing_rite");
    public static final ResourceKey<ResearchEntry> SOTU_SCAN_HALLOWED_ORB = create("sotu_scan_hallowed_orb");
    public static final ResourceKey<ResearchEntry> SCAN_PRIMALITE = create("scan_primalite");
    public static final ResourceKey<ResearchEntry> SCAN_HEXIUM = create("scan_hexium");
    public static final ResourceKey<ResearchEntry> SCAN_HALLOWSTEEL = create("scan_hallowsteel");
    // TODO Place in correct order after bootstrapping
    public static final ResourceKey<ResearchEntry> WAND_TRANSFORM_HINT = create("wand_transform_hint");
    public static final ResourceKey<ResearchEntry> FOUND_SHRINE = create("found_shrine");
    public static final ResourceKey<ResearchEntry> GOT_DREAM = create("got_dream");
    public static final ResourceKey<ResearchEntry> SIPHON_PROMPT = create("siphon_prompt");
    public static final ResourceKey<ResearchEntry> DROWN_A_LITTLE = create("drown_a_little");
    public static final ResourceKey<ResearchEntry> FEEL_THE_BURN = create("feel_the_burn");
    public static final ResourceKey<ResearchEntry> FURRY_FRIEND = create("furry_friend");
    public static final ResourceKey<ResearchEntry> BREED_ANIMAL = create("breed_animal");
    public static final ResourceKey<ResearchEntry> NEAR_DEATH_EXPERIENCE = create("near_death_experience");
    public static final ResourceKey<ResearchEntry> SCAN_FLYING_CREATURE = create("scan_flying_creature");
    public static final ResourceKey<ResearchEntry> SCAN_GOLEM = create("scan_golem");
    public static final ResourceKey<ResearchEntry> SCAN_NETHER_STAR = create("scan_nether_star");
    public static final ResourceKey<ResearchEntry> UNKNOWN_RUNE = create("unknown_rune");
    
    // Commonly used research icons
    private static final ResourceLocation ICON_MANAWEAVING = PrimalMagick.resource("textures/research/discipline_manaweaving.png");
    private static final ResourceLocation ICON_ALCHEMY = PrimalMagick.resource("textures/research/discipline_alchemy.png");
    private static final ResourceLocation ICON_SORCERY = PrimalMagick.resource("textures/research/discipline_sorcery.png");
    private static final ResourceLocation ICON_RUNEWORKING = PrimalMagick.resource("textures/research/discipline_runeworking.png");
    private static final ResourceLocation ICON_RITUAL = PrimalMagick.resource("textures/research/discipline_ritual.png");
    private static final ResourceLocation ICON_MAGITECH = PrimalMagick.resource("textures/research/discipline_magitech.png");
    private static final ResourceLocation ICON_BAG = PrimalMagick.resource("textures/research/research_bag.png");
    private static final ResourceLocation ICON_MAP = PrimalMagick.resource("textures/research/research_map.png");
    private static final ResourceLocation ICON_TUBE = PrimalMagick.resource("textures/research/research_tube.png");
    private static final ResourceLocation ICON_UNKNOWN = PrimalMagick.resource("textures/research/research_unknown.png");
    
    public static ResourceKey<ResearchEntry> create(String name) {
        return ResourceKey.create(RegistryKeysPM.RESEARCH_ENTRIES, PrimalMagick.resource(name));
    }
    
    public static void bootstrap(BootstapContext<ResearchEntry> context) {
        bootstrapBasicsEntries(context);
        bootstrapManaweavingEntries(context); 
        bootstrapAlchemyEntries(context);
        bootstrapSorceryEntries(context);
        bootstrapRuneworkingEntries(context);
        bootstrapRitualEntries(context);
        bootstrapMagitechEntries(context);
        bootstrapScanEntries(context);
        bootstrapInternalEntries(context);
    }
    
    private static void bootstrapBasicsEntries(BootstapContext<ResearchEntry> context) {
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.BASICS;
        register(context, FIRST_STEPS, key -> ResearchEntry.builder(key).discipline(discipline).icon(ItemsPM.GRIMOIRE.get())
                .stage().requiredCraft(ItemsPM.ARCANE_WORKBENCH.get()).recipe(ItemsPM.MUNDANE_WAND.get()).end()
                .stage().requiredStat(StatsPM.MANA_SIPHONED, 10).recipe(ItemsPM.MUNDANE_WAND.get()).end()
                .stage().requiredStat(StatsPM.OBSERVATIONS_MADE, 1).recipe(ItemsPM.MUNDANE_WAND.get()).recipe(ItemsPM.WOOD_TABLE.get()).recipe(ItemsPM.MAGNIFYING_GLASS.get())
                        .recipe(ItemsPM.ANALYSIS_TABLE.get()).end()
                .stage().recipe(ItemsPM.MUNDANE_WAND.get()).recipe(ItemsPM.WOOD_TABLE.get()).recipe(ItemsPM.MAGNIFYING_GLASS.get()).recipe(ItemsPM.ANALYSIS_TABLE.get()).end()
                .build());
        register(context, THEORYCRAFTING, key -> ResearchEntry.builder(key).discipline(discipline).icon("textures/research/knowledge_theory.png").parent(FIRST_STEPS)
                .stage().requiredObservations(1).end()
                .stage().requiredCraft(ItemsPM.RESEARCH_TABLE.get()).requiredCraft(ItemsPM.ENCHANTED_INK_AND_QUILL.get()).recipe(ItemsPM.RESEARCH_TABLE.get()).recipe(ItemsPM.ENCHANTED_INK.get())
                        .recipe(ItemsPM.ENCHANTED_INK_AND_QUILL.get()).end()
                .stage().requiredStat(StatsPM.THEORIES_FORMED, 1).recipe(ItemsPM.RESEARCH_TABLE.get()).recipe(ItemsPM.ENCHANTED_INK.get()).recipe(ItemsPM.ENCHANTED_INK_AND_QUILL.get()).end()
                .stage().recipe(ItemsPM.RESEARCH_TABLE.get()).recipe(ItemsPM.ENCHANTED_INK.get()).recipe(ItemsPM.ENCHANTED_INK_AND_QUILL.get()).end()
                .build());
        register(context, ATTUNEMENTS, key -> ResearchEntry.builder(key).discipline(discipline).parent(FIRST_STEPS)
                .stage().requiredObservations(1).end()
                .stage().end()
                .build());
        // FIXME Re-add for 1.21 release
/*
        register(context, LINGUISTICS, key -> ResearchEntry.builder(key).discipline(discipline).icon(Items.WRITABLE_BOOK).parent(FIRST_STEPS)
                .stage().requiredObservations(1).requiredStat(StatsPM.ANCIENT_BOOKS_READ, 1).end()
                .stage().recipe(ItemsPM.SCRIBE_TABLE.get()).end()
                .build());
*/
        register(context, UNLOCK_MANAWEAVING, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_MANAWEAVING).parent(FIRST_STEPS)
                .stage().requiredObservations(1).end()
                .stage().end()
                .build());
        register(context, UNLOCK_ALCHEMY, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_ALCHEMY).parent(MANA_ARROWS)
                .stage().requiredObservations(1).end()
                .stage().end()
                .build());
        register(context, UNLOCK_SORCERY, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_SORCERY).parent(WAND_CHARGER)
                .stage().requiredObservations(1).end()
                .stage().end()
                .build());
        register(context, UNLOCK_RUNEWORKING, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_RUNEWORKING).parent(CALCINATOR_BASIC)
                .stage().requiredObservations(1).end()
                .stage().end()
                .build());
        register(context, UNLOCK_RITUAL, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_RITUAL).parent(WAND_INSCRIPTION).parent(RUNE_PROJECT)
                .stage().requiredObservations(1).end()
                .stage().end()
                .build());
        register(context, UNLOCK_MAGITECH, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_MAGITECH).parent(MANAFRUIT).parent(MANA_SALTS)
                .stage().requiredObservations(1).end()
                .stage().end()
                .build());
        register(context, TERRESTRIAL_MAGICK, key -> ResearchEntry.builder(key).discipline(discipline).parent(ATTUNEMENTS)
                .stage().end()
                .build());
        register(context, SOURCE_EARTH, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.EARTH.getImage()).parent(TERRESTRIAL_MAGICK)
                .stage().requiredItem(Tags.Items.OBSIDIAN).requiredItem(Tags.Items.GEMS_DIAMOND).requiredObservations(1).requiredStat(StatsPM.SHRINE_FOUND_EARTH, 1)
                        .requiredResearch(ENV_EARTH).requiredStat(StatsPM.MANA_SPENT_EARTH, 100).end()
                .stage().attunement(Sources.EARTH, 5).end()
                .build());
        register(context, SOURCE_SEA, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.SEA.getImage()).parent(TERRESTRIAL_MAGICK)
                .stage().requiredItem(ItemTagsPM.CORAL_BLOCKS).requiredItem(Items.ICE).requiredObservations(1).requiredStat(StatsPM.SHRINE_FOUND_SEA, 1)
                        .requiredResearch(ENV_SEA).requiredStat(StatsPM.MANA_SPENT_SEA, 100).end()
                .stage().attunement(Sources.SEA, 5).end()
                .build());
        register(context, SOURCE_SKY, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.SKY.getImage()).parent(TERRESTRIAL_MAGICK)
                .stage().requiredItem(Items.BAMBOO).requiredItem(ItemTags.LEAVES).requiredObservations(1).requiredStat(StatsPM.SHRINE_FOUND_SKY, 1)
                        .requiredResearch(ENV_SKY).requiredStat(StatsPM.MANA_SPENT_SKY, 100).end()
                .stage().attunement(Sources.SKY, 5).end()
                .build());
        register(context, SOURCE_SUN, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.SUN.getImage()).parent(TERRESTRIAL_MAGICK)
                .stage().requiredItem(ItemTagsPM.SUNWOOD_LOGS).requiredItem(Tags.Items.SANDSTONE).requiredObservations(1).requiredStat(StatsPM.SHRINE_FOUND_SUN, 1)
                        .requiredResearch(ENV_SUN).requiredStat(StatsPM.MANA_SPENT_SUN, 100).end()
                .stage().attunement(Sources.SUN, 5).end()
                .build());
        register(context, SOURCE_MOON, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.MOON.getImage()).parent(TERRESTRIAL_MAGICK)
                .stage().requiredItem(ItemTagsPM.MOONWOOD_LOGS).requiredItem(Tags.Items.MUSHROOMS).requiredObservations(1).requiredStat(StatsPM.SHRINE_FOUND_MOON, 1)
                        .requiredResearch(ENV_MOON).requiredStat(StatsPM.MANA_SPENT_MOON, 100).end()
                .stage().attunement(Sources.MOON, 5).end()
                .build());
        register(context, FORBIDDEN_MAGICK, key -> ResearchEntry.builder(key).discipline(discipline).parent(TERRESTRIAL_MAGICK).parent(DISCOVER_FORBIDDEN)
                .stage().end()
                .build());
        register(context, SOURCE_BLOOD, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.BLOOD.getImage()).parent(FORBIDDEN_MAGICK).parent(DISCOVER_BLOOD)
                .stage().requiredItem(Tags.Items.BONES).requiredItem(ItemsPM.BLOODY_FLESH.get()).requiredObservations(1).requiredStat(StatsPM.MANA_SPENT_BLOOD, 100).end()
                .stage().attunement(Sources.BLOOD, 5).end()
                .build());
        register(context, SOURCE_INFERNAL, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.INFERNAL.getImage()).parent(FORBIDDEN_MAGICK).parent(DISCOVER_INFERNAL)
                .stage().requiredItem(Tags.Items.RODS_BLAZE).requiredItem(Items.SOUL_SAND).requiredObservations(1).requiredStat(StatsPM.MANA_SPENT_INFERNAL, 100).end()
                .stage().attunement(Sources.INFERNAL, 5).end()
                .build());
        register(context, SOURCE_VOID, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.VOID.getImage()).parent(FORBIDDEN_MAGICK).parent(DISCOVER_VOID)
                .stage().requiredItem(Tags.Items.END_STONES).requiredItem(Tags.Items.ENDER_PEARLS).requiredObservations(1).requiredStat(StatsPM.MANA_SPENT_VOID, 100).end()
                .stage().attunement(Sources.VOID, 5).end()
                .build());
        register(context, HEAVENLY_MAGICK, key -> ResearchEntry.builder(key).discipline(discipline).parent(FORBIDDEN_MAGICK).parent(DISCOVER_HALLOWED)
                .stage().end()
                .build());
        register(context, SOURCE_HALLOWED, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.HALLOWED.getImage()).parent(HEAVENLY_MAGICK)
                .stage().requiredItem(Tags.Items.NETHER_STARS).requiredObservations(1).requiredStat(StatsPM.MANA_SPENT_HALLOWED, 100).end()
                .stage().attunement(Sources.HALLOWED, 5).end()
                .build());
        register(context, SECRETS_OF_THE_UNIVERSE, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ICON_UNKNOWN)
                .stage().requiredResearch(SOTU_DISCOVER_BLOOD).requiredResearch(SOTU_DISCOVER_INFERNAL).requiredResearch(SOTU_DISCOVER_VOID)
                        .requiredResearch(SOTU_RESEARCH_ARCANOMETER).requiredResearch(SOTU_RESEARCH_HEXIUM).requiredResearch(SOTU_RESEARCH_POWER_RUNE)
                        .requiredResearch(SOTU_RESEARCH_SANGUINE_CRUCIBLE).requiredResearch(SOTU_RESEARCH_CLEANSING_RITE).requiredResearch(SOTU_SCAN_HALLOWED_ORB).end()
                .stage().attunement(Sources.HALLOWED, 4).end()
                .build());
        register(context, COMPLETE_BASICS, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ItemsPM.GRIMOIRE.get()).finale(discipline)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .attunement(Sources.BLOOD, 1).attunement(Sources.INFERNAL, 1).attunement(Sources.VOID, 1).attunement(Sources.HALLOWED, 1).end()
                .build());
        register(context, THEORY_OF_EVERYTHING, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ItemsPM.GRIMOIRE.get()).finale(ResearchDisciplines.BASICS)
                .finale(ResearchDisciplines.ALCHEMY).finale(ResearchDisciplines.MAGITECH).finale(ResearchDisciplines.MANAWEAVING)
                .finale(ResearchDisciplines.RITUAL).finale(ResearchDisciplines.RUNEWORKING).finale(ResearchDisciplines.SORCERY)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 2).attunement(Sources.SEA, 2).attunement(Sources.SKY, 2).attunement(Sources.SUN, 2).attunement(Sources.MOON, 2)
                        .attunement(Sources.BLOOD, 2).attunement(Sources.INFERNAL, 2).attunement(Sources.VOID, 2).attunement(Sources.HALLOWED, 2).end()
                .build());
    }
    
    private static void bootstrapManaweavingEntries(BootstapContext<ResearchEntry> context) {
        // TODO Define research entries
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.MANAWEAVING;
        register(context, BASIC_MANAWEAVING, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_MANAWEAVING).parent(UNLOCK_MANAWEAVING)
                .stage().recipe(ItemsPM.MANA_PRISM.get()).end()
                .build());
        register(context, EXPERT_MANAWEAVING, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_MANAWEAVING).parent(MANA_ARROWS).parent(WAND_CHARGER)
                .stage().requiredStat(StatsPM.CRAFTED_MANAWEAVING, 10).end()
                .stage().recipe(ItemsPM.MARBLE_ENCHANTED.get()).recipe(ItemsPM.MARBLE_ENCHANTED_BRICK_SLAB.get()).recipe(ItemsPM.MARBLE_ENCHANTED_BRICK_STAIRS.get())
                        .recipe(ItemsPM.MARBLE_ENCHANTED_BRICK_WALL.get()).recipe(ItemsPM.MARBLE_ENCHANTED_BRICKS.get()).recipe(ItemsPM.MARBLE_ENCHANTED_CHISELED.get())
                        .recipe(ItemsPM.MARBLE_ENCHANTED_PILLAR.get()).recipe(ItemsPM.MARBLE_ENCHANTED_RUNED.get()).recipe(ItemsPM.MARBLE_ENCHANTED_SLAB.get())
                        .recipe(ItemsPM.MARBLE_ENCHANTED_STAIRS.get()).recipe(ItemsPM.MARBLE_ENCHANTED_WALL.get()).recipe(ItemsPM.MARBLE_ENCHANTED_BOOKSHELF.get()).end()
                .build());
        register(context, MASTER_MANAWEAVING, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_MANAWEAVING).parent(WAND_CAP_GOLD).parent(WAND_GEM_ADEPT)
                .stage().requiredResearch(DISCOVER_FORBIDDEN).requiredStat(StatsPM.CRAFTED_MANAWEAVING, 50).end()
                .stage().recipe(ItemsPM.MARBLE_SMOKED.get()).recipe(ItemsPM.MARBLE_SMOKED_BRICK_SLAB.get()).recipe(ItemsPM.MARBLE_SMOKED_BRICK_STAIRS.get())
                        .recipe(ItemsPM.MARBLE_SMOKED_BRICK_WALL.get()).recipe(ItemsPM.MARBLE_SMOKED_BRICKS.get()).recipe(ItemsPM.MARBLE_SMOKED_CHISELED.get())
                        .recipe(ItemsPM.MARBLE_SMOKED_PILLAR.get()).recipe(ItemsPM.MARBLE_SMOKED_RUNED.get()).recipe(ItemsPM.MARBLE_SMOKED_SLAB.get())
                        .recipe(ItemsPM.MARBLE_SMOKED_STAIRS.get()).recipe(ItemsPM.MARBLE_SMOKED_WALL.get()).recipe(ItemsPM.MARBLE_SMOKED_BOOKSHELF.get()).end()
                .build());
        register(context, SUPREME_MANAWEAVING, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_MANAWEAVING).parent(WAND_CAP_HEXIUM).parent(WAND_GEM_WIZARD)
                .stage().requiredResearch(DISCOVER_HALLOWED).requiredStat(StatsPM.CRAFTED_MANAWEAVING, 250).reveals(SECRETS_OF_THE_UNIVERSE).end()
                .stage().recipe(ItemsPM.MARBLE_HALLOWED.get()).recipe(ItemsPM.MARBLE_HALLOWED_BRICK_SLAB.get()).recipe(ItemsPM.MARBLE_HALLOWED_BRICK_STAIRS.get())
                        .recipe(ItemsPM.MARBLE_HALLOWED_BRICK_WALL.get()).recipe(ItemsPM.MARBLE_HALLOWED_BRICKS.get()).recipe(ItemsPM.MARBLE_HALLOWED_CHISELED.get())
                        .recipe(ItemsPM.MARBLE_HALLOWED_PILLAR.get()).recipe(ItemsPM.MARBLE_HALLOWED_RUNED.get()).recipe(ItemsPM.MARBLE_HALLOWED_SLAB.get())
                        .recipe(ItemsPM.MARBLE_HALLOWED_STAIRS.get()).recipe(ItemsPM.MARBLE_HALLOWED_WALL.get()).recipe(ItemsPM.MARBLE_HALLOWED_BOOKSHELF.get()).end()
                .build());
        register(context, COMPLETE_MANAWEAVING, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ICON_MANAWEAVING).finale(discipline)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .attunement(Sources.BLOOD, 1).attunement(Sources.INFERNAL, 1).attunement(Sources.VOID, 1).attunement(Sources.HALLOWED, 1).end()
                .build());

    }
    
    private static void bootstrapAlchemyEntries(BootstapContext<ResearchEntry> context) {
        // TODO Define research entries
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.ALCHEMY;
        register(context, BASIC_ALCHEMY, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_ALCHEMY).parent(UNLOCK_ALCHEMY)
                .stage().requiredCraft(ItemsPM.ESSENCE_FURNACE.get()).end()
                .stage().end()
                .build());
        register(context, EXPERT_ALCHEMY, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_ALCHEMY).parent(CALCINATOR_BASIC).parent(STONEMELDING)
                .stage().requiredStat(StatsPM.CRAFTED_ALCHEMY, 50).end()
                .stage().end()
                .build());
        register(context, MASTER_ALCHEMY, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_ALCHEMY).parent(CALCINATOR_ENCHANTED).parent(PRIMALITE)
                .stage().requiredResearch(DISCOVER_FORBIDDEN).requiredStat(StatsPM.CRAFTED_ALCHEMY, 250).end()
                .stage().end()
                .build());
        register(context, SUPREME_ALCHEMY, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_ALCHEMY).parent(CALCINATOR_FORBIDDEN).parent(HEXIUM)
                .stage().requiredResearch(DISCOVER_HALLOWED).requiredStat(StatsPM.CRAFTED_ALCHEMY, 1000).reveals(SECRETS_OF_THE_UNIVERSE).end()
                .stage().end()
                .build());
        register(context, COMPLETE_ALCHEMY, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ICON_ALCHEMY).finale(discipline)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .attunement(Sources.BLOOD, 1).attunement(Sources.INFERNAL, 1).attunement(Sources.VOID, 1).attunement(Sources.HALLOWED, 1).end()
                .build());
        
    }
    
    private static void bootstrapSorceryEntries(BootstapContext<ResearchEntry> context) {
        // TODO Define research entries
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.SORCERY;
        register(context, BASIC_SORCERY, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_SORCERY).parent(UNLOCK_SORCERY)
                .stage().attunement(Sources.EARTH, 1).recipe(ItemsPM.SPELL_SCROLL_BLANK.get()).recipe(ItemsPM.SPELLCRAFTING_ALTAR.get()).end()
                .build());
        register(context, EXPERT_SORCERY, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_SORCERY).parent(SPELL_PAYLOAD_LIGHTNING).parent(SPELL_PAYLOAD_FROST)
                .stage().requiredStat(StatsPM.SPELLS_CRAFTED, 1).requiredStat(StatsPM.SPELLS_CAST, 10).end()
                .stage().end()
                .build());
        register(context, MASTER_SORCERY, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_SORCERY).parent(SPELL_VEHICLE_PROJECTILE).parent(SPELL_MOD_AMPLIFY)
                .stage().requiredResearch(DISCOVER_FORBIDDEN).requiredStat(StatsPM.SPELLS_CAST, 50).requiredStat(StatsPM.SPELLS_CRAFTED_MAX_COST, 50).end()
                .stage().end()
                .build());
        register(context, SUPREME_SORCERY, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_SORCERY).parent(SPELL_VEHICLE_BOLT).parent(SPELL_MOD_QUICKEN)
                .stage().requiredResearch(DISCOVER_HALLOWED).requiredStat(StatsPM.SPELLS_CAST, 250).requiredStat(StatsPM.SPELLS_CRAFTED_MAX_COST, 250).reveals(SECRETS_OF_THE_UNIVERSE).end()
                .stage().end()
                .build());
        register(context, COMPLETE_SORCERY, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ICON_SORCERY).finale(discipline)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .attunement(Sources.BLOOD, 1).attunement(Sources.INFERNAL, 1).attunement(Sources.VOID, 1).attunement(Sources.HALLOWED, 1).end()
                .build());

    }
    
    private static void bootstrapRuneworkingEntries(BootstapContext<ResearchEntry> context) {
        // TODO Define research entries
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.RUNEWORKING;
        register(context, BASIC_RUNEWORKING, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_RUNEWORKING).parent(UNLOCK_RUNEWORKING)
                .stage().recipe(ItemsPM.RUNECARVING_TABLE.get()).recipe(ItemsPM.RUNE_UNATTUNED.get()).recipe(ItemsPM.RUNESCRIBING_ALTAR_BASIC.get()).end()
                .build());
        register(context, EXPERT_RUNEWORKING, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_RUNEWORKING).parent(RUNE_EARTH).parent(RUNE_PROJECT).parent(RUNE_ITEM)
                .stage().requiredStat(StatsPM.CRAFTED_RUNEWORKING, 10).requiredStat(StatsPM.ITEMS_RUNESCRIBED, 2).end()
                .stage().recipe(ItemsPM.RUNESCRIBING_ALTAR_ENCHANTED.get()).end()
                .build());
        register(context, MASTER_RUNEWORKING, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_RUNEWORKING).parent(RUNE_ABSORB).parent(RUNE_CREATURE)
                .stage().requiredResearch(DISCOVER_FORBIDDEN).requiredStat(StatsPM.CRAFTED_RUNEWORKING, 50).requiredStat(StatsPM.ITEMS_RUNESCRIBED, 10).end()
                .stage().recipe(ItemsPM.RUNESCRIBING_ALTAR_FORBIDDEN.get()).end()
                .build());
        register(context, SUPREME_RUNEWORKING, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_RUNEWORKING).parent(RUNE_POWER)
                .stage().requiredResearch(DISCOVER_HALLOWED).requiredStat(StatsPM.CRAFTED_RUNEWORKING, 250).requiredStat(StatsPM.ITEMS_RUNESCRIBED, 50).reveals(SECRETS_OF_THE_UNIVERSE).end()
                .stage().recipe(ItemsPM.RUNESCRIBING_ALTAR_HEAVENLY.get()).end()
                .build());
        register(context, COMPLETE_RUNEWORKING, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ICON_RUNEWORKING).finale(discipline)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .attunement(Sources.BLOOD, 1).attunement(Sources.INFERNAL, 1).attunement(Sources.VOID, 1).attunement(Sources.HALLOWED, 1).end()
                .build());

    }
    
    private static void bootstrapRitualEntries(BootstapContext<ResearchEntry> context) {
        // TODO Define research entries
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.RITUAL;
        register(context, BASIC_RITUAL, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_RITUAL).parent(UNLOCK_RITUAL)
                .stage().recipe(ItemsPM.RITUAL_ALTAR.get()).recipe(ItemsPM.OFFERING_PEDESTAL.get()).end()
                .build());
        register(context, EXPERT_RITUAL, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_RITUAL).parent(RITUAL_CANDLES).parent(INCENSE_BRAZIER)
                .stage().requiredStat(StatsPM.RITUALS_COMPLETED, 2).end()
                .stage().end()
                .build());
        register(context, MASTER_RITUAL, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_RITUAL).parent(RITUAL_LECTERN).parent(RITUAL_BELL)
                .stage().requiredResearch(DISCOVER_FORBIDDEN).requiredStat(StatsPM.RITUALS_COMPLETED, 10).end()
                .stage().end()
                .build());
        register(context, SUPREME_RITUAL, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_RITUAL).parent(BLOODLETTER).parent(SOUL_ANVIL)
                .stage().requiredResearch(DISCOVER_HALLOWED).requiredStat(StatsPM.RITUALS_COMPLETED, 50).reveals(SECRETS_OF_THE_UNIVERSE).end()
                .stage().end()
                .build());
        register(context, COMPLETE_RITUAL, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ICON_RITUAL).finale(discipline)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .attunement(Sources.BLOOD, 1).attunement(Sources.INFERNAL, 1).attunement(Sources.VOID, 1).attunement(Sources.HALLOWED, 1).end()
                .build());

    }
    
    private static void bootstrapMagitechEntries(BootstapContext<ResearchEntry> context) {
        // TODO Define research entries
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.MAGITECH;
        register(context, BASIC_MAGITECH, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_MAGITECH).parent(UNLOCK_MAGITECH)
                .stage().recipe(ItemsPM.MAGITECH_PARTS_BASIC.get()).end()
                .build());
        register(context, EXPERT_MAGITECH, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_MAGITECH).parent(HONEY_EXTRACTOR).parent(SEASCRIBE_PEN)
                .stage().requiredStat(StatsPM.CRAFTED_MAGITECH, 5).requiredResearch(SCAN_PRIMALITE).end()
                .stage().recipe(ItemsPM.MAGITECH_PARTS_ENCHANTED.get()).end()
                .build());
        register(context, MASTER_MAGITECH, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_MAGITECH).parent(ARCANOMETER).parent(PRIMALITE_GOLEM)
                .stage().requiredResearch(DISCOVER_FORBIDDEN).requiredStat(StatsPM.CRAFTED_MAGITECH, 25).requiredResearch(SCAN_HEXIUM).end()
                .stage().recipe(ItemsPM.MAGITECH_PARTS_FORBIDDEN.get()).end()
                .build());
        register(context, SUPREME_MAGITECH, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_MAGITECH).parent(HEXIUM_GOLEM)
                .stage().requiredResearch(DISCOVER_HALLOWED).requiredStat(StatsPM.CRAFTED_MAGITECH, 100).requiredResearch(SCAN_HALLOWSTEEL).reveals(SECRETS_OF_THE_UNIVERSE).end()
                .stage().recipe(ItemsPM.MAGITECH_PARTS_HEAVENLY.get()).end()
                .build());
        register(context, COMPLETE_MAGITECH, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ICON_MAGITECH).finale(discipline)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .attunement(Sources.BLOOD, 1).attunement(Sources.INFERNAL, 1).attunement(Sources.VOID, 1).attunement(Sources.HALLOWED, 1).end()
                .build());

    }
    
    private static void bootstrapScanEntries(BootstapContext<ResearchEntry> context) {
        // TODO Define research entries
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.SCANS;
        register(context, RAW_MARBLE, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ItemsPM.MARBLE_RAW.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemsPM.MARBLE_SLAB.get()).recipe(ItemsPM.MARBLE_STAIRS.get()).recipe(ItemsPM.MARBLE_WALL.get()).recipe(ItemsPM.MARBLE_BRICKS.get())
                        .recipe(ItemsPM.MARBLE_BRICK_SLAB.get()).recipe(ItemsPM.MARBLE_BRICK_STAIRS.get()).recipe(ItemsPM.MARBLE_BRICK_WALL.get()).recipe(ItemsPM.MARBLE_PILLAR.get())
                        .recipe(ItemsPM.MARBLE_CHISELED.get()).recipe(ItemsPM.MARBLE_TILES.get()).recipe(ItemsPM.MARBLE_RUNED.get()).recipe(ItemsPM.MARBLE_BOOKSHELF.get()).end()
                .build());
        register(context, HALLOWED_ORB, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ItemsPM.HALLOWED_ORB.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemsPM.HALLOWOOD_SAPLING.get()).end()
                .build());
        register(context, HALLOWOOD_TREES, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ItemsPM.HALLOWOOD_SAPLING.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemsPM.HALLOWOOD_WOOD.get()).recipe(ItemsPM.STRIPPED_HALLOWOOD_WOOD.get()).recipe(ItemsPM.HALLOWOOD_PLANKS.get())
                        .recipe(ItemsPM.HALLOWOOD_SLAB.get()).recipe(ItemsPM.HALLOWOOD_STAIRS.get()).recipe(ItemsPM.HALLOWOOD_PILLAR.get()).end()
                .build());
        register(context, SUNWOOD_TREES, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ItemsPM.SUNWOOD_SAPLING.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemsPM.SUNWOOD_WOOD.get()).recipe(ItemsPM.STRIPPED_SUNWOOD_WOOD.get()).recipe(ItemsPM.SUNWOOD_PLANKS.get())
                        .recipe(ItemsPM.SUNWOOD_SLAB.get()).recipe(ItemsPM.SUNWOOD_STAIRS.get()).recipe(ItemsPM.SUNWOOD_PILLAR.get()).end()
                .build());
        register(context, MOONWOOD_TREES, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ItemsPM.MOONWOOD_SAPLING.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemsPM.MOONWOOD_WOOD.get()).recipe(ItemsPM.STRIPPED_MOONWOOD_WOOD.get()).recipe(ItemsPM.MOONWOOD_PLANKS.get())
                        .recipe(ItemsPM.MOONWOOD_SLAB.get()).recipe(ItemsPM.MOONWOOD_STAIRS.get()).recipe(ItemsPM.MOONWOOD_PILLAR.get()).end()
                .build());
        register(context, ROCK_SALT, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ItemsPM.REFINED_SALT.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemsPM.REFINED_SALT.get()).recipe(PrimalMagick.resource("rock_salt_from_smelting"))
                        .recipe(ItemsPM.SALT_BLOCK.get()).recipe(PrimalMagick.resource("refined_salt_from_salt_block"))
                        .recipe(ItemsPM.SALTED_BAKED_POTATO.get()).recipe(ItemsPM.SALTED_BEETROOT_SOUP.get()).recipe(ItemsPM.SALTED_COOKED_BEEF.get())
                        .recipe(ItemsPM.SALTED_COOKED_CHICKEN.get()).recipe(ItemsPM.SALTED_COOKED_COD.get()).recipe(ItemsPM.SALTED_COOKED_MUTTON.get()).recipe(ItemsPM.SALTED_COOKED_PORKCHOP.get())
                        .recipe(ItemsPM.SALTED_COOKED_RABBIT.get()).recipe(ItemsPM.SALTED_COOKED_SALMON.get()).recipe(ItemsPM.SALTED_MUSHROOM_STEW.get()).recipe(ItemsPM.SALTED_RABBIT_STEW.get()).end()
                .build());
        register(context, ALCHEMICAL_WASTE, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ItemsPM.ALCHEMICAL_WASTE.get()).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
        register(context, QUARTZ, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(Items.QUARTZ).parent(UNLOCK_SCANS)
                .stage().recipe(ItemsPM.QUARTZ_NUGGET.get()).recipe(PrimalMagick.resource("quartz_from_nuggets")).recipe(PrimalMagick.resource("quartz_from_smelting")).end()
                .build());
        register(context, INNER_DEMON, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ItemsPM.SANGUINE_CORE_BLANK.get()).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
        register(context, BOOKSHELF, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(Items.BOOKSHELF).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
        register(context, BEEHIVE, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(Items.BEEHIVE).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
        register(context, BEACON, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(Items.BEACON).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
        register(context, DRAGON_EGG, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(Items.DRAGON_EGG).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
        register(context, DRAGON_HEAD, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(Items.DRAGON_HEAD).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
        register(context, MYSTICAL_RELIC, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ItemsPM.MYSTICAL_RELIC.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemsPM.MYSTICAL_RELIC.get()).end()
                .build());
        register(context, HUMMING_ARTIFACT, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemsPM.HUMMING_ARTIFACT_EARTH.get()).recipe(ItemsPM.HUMMING_ARTIFACT_SEA.get()).recipe(ItemsPM.HUMMING_ARTIFACT_SKY.get())
                        .recipe(ItemsPM.HUMMING_ARTIFACT_SUN.get()).recipe(ItemsPM.HUMMING_ARTIFACT_MOON.get()).end()
                .addendum().requiredResearch(DISCOVER_BLOOD).recipe(ItemsPM.HUMMING_ARTIFACT_BLOOD.get()).end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).recipe(ItemsPM.HUMMING_ARTIFACT_INFERNAL.get()).end()
                .addendum().requiredResearch(DISCOVER_VOID).recipe(ItemsPM.HUMMING_ARTIFACT_VOID.get()).end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).recipe(ItemsPM.HUMMING_ARTIFACT_HALLOWED.get()).end()
                .build());
        register(context, TREEFOLK, key -> ResearchEntry.builder(key).discipline(discipline).hidden().icon(ItemsPM.HEARTWOOD.get()).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
    }
    
    private static void bootstrapInternalEntries(BootstapContext<ResearchEntry> context) {
        register(context, UNLOCK_SCANS, key -> ResearchEntry.builder(key).internal().build());
        register(context, UNLOCK_RUNE_ENCHANTMENTS, key -> ResearchEntry.builder(key).internal().build());
        register(context, DISCOVER_BLOOD, key -> ResearchEntry.builder(key).internal().icon(ICON_TUBE)
                .stage().sibling(DISCOVER_FORBIDDEN).sibling(SOTU_DISCOVER_BLOOD).end()
                .build());
        register(context, DISCOVER_INFERNAL, key -> ResearchEntry.builder(key).internal().icon(ICON_TUBE)
                .stage().sibling(DISCOVER_FORBIDDEN).sibling(SOTU_DISCOVER_INFERNAL).end()
                .build());
        register(context, DISCOVER_VOID, key -> ResearchEntry.builder(key).internal().icon(ICON_TUBE)
                .stage().sibling(DISCOVER_FORBIDDEN).sibling(SOTU_DISCOVER_VOID).end()
                .build());
        register(context, DISCOVER_FORBIDDEN, key -> ResearchEntry.builder(key).internal().build());
        register(context, DISCOVER_HALLOWED, key -> ResearchEntry.builder(key).internal().icon(ICON_TUBE).build());
        register(context, ENV_EARTH, key -> ResearchEntry.builder(key).internal().icon(ICON_MAP).build());
        register(context, ENV_SEA, key -> ResearchEntry.builder(key).internal().icon(ICON_MAP).build());
        register(context, ENV_SKY, key -> ResearchEntry.builder(key).internal().icon(ICON_MAP).build());
        register(context, ENV_SUN, key -> ResearchEntry.builder(key).internal().icon(ICON_MAP).build());
        register(context, ENV_MOON, key -> ResearchEntry.builder(key).internal().icon(ICON_MAP).build());
        register(context, SOTU_DISCOVER_BLOOD, key -> ResearchEntry.builder(key).internal().icon(ICON_MAP).hasHint().build());
        register(context, SOTU_DISCOVER_INFERNAL, key -> ResearchEntry.builder(key).internal().icon(ICON_MAP).hasHint().build());
        register(context, SOTU_DISCOVER_VOID, key -> ResearchEntry.builder(key).internal().icon(ICON_MAP).hasHint().build());
        register(context, SOTU_RESEARCH_ARCANOMETER, key -> ResearchEntry.builder(key).internal().icon(ICON_TUBE).hasHint().build());
        register(context, SOTU_RESEARCH_HEXIUM, key -> ResearchEntry.builder(key).internal().icon(ICON_TUBE).hasHint().build());
        register(context, SOTU_RESEARCH_POWER_RUNE, key -> ResearchEntry.builder(key).internal().icon(ICON_TUBE).hasHint().build());
        register(context, SOTU_RESEARCH_SANGUINE_CRUCIBLE, key -> ResearchEntry.builder(key).internal().icon(ICON_TUBE).hasHint().build());
        register(context, SOTU_RESEARCH_CLEANSING_RITE, key -> ResearchEntry.builder(key).internal().icon(ICON_TUBE).hasHint().build());
        register(context, SOTU_SCAN_HALLOWED_ORB, key -> ResearchEntry.builder(key).internal().icon(ICON_BAG).hasHint().build());
        register(context, SCAN_PRIMALITE, key -> ResearchEntry.builder(key).internal().icon(ICON_BAG).hasHint().build());
        register(context, SCAN_HEXIUM, key -> ResearchEntry.builder(key).internal().icon(ICON_BAG).hasHint().build());
        register(context, SCAN_HALLOWSTEEL, key -> ResearchEntry.builder(key).internal().icon(ICON_BAG).hasHint().build());
    }
    
    private static Holder.Reference<ResearchEntry> register(BootstapContext<ResearchEntry> context, ResourceKey<ResearchEntry> key, Function<ResourceKey<ResearchEntry>, ResearchEntry> supplier) {
        return context.register(key, supplier.apply(key));
    }
    
    @Nullable
    public static ResearchEntry getEntry(RegistryAccess registryAccess, ResearchEntryKey key) {
        return getEntry(registryAccess, key.getRootKey());
    }
    
    @Nullable
    public static ResearchEntry getEntry(RegistryAccess registryAccess, ResourceKey<ResearchEntry> rawKey) {
        return registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_ENTRIES).get(rawKey);
    }
    
    public static Stream<ResearchEntry> stream(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_ENTRIES).stream();
    }
}

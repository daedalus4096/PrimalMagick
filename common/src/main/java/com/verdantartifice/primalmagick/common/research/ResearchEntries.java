package com.verdantartifice.primalmagick.common.research;

import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsForgeExt;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
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

    // Magitech research entries
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
    public static final ResourceKey<ResearchEntry> DROWN_A_LITTLE = create("drown_a_little");
    public static final ResourceKey<ResearchEntry> NEAR_DEATH_EXPERIENCE = create("near_death_experience");
    public static final ResourceKey<ResearchEntry> FURRY_FRIEND = create("furry_friend");
    public static final ResourceKey<ResearchEntry> BREED_ANIMAL = create("breed_animal");
    public static final ResourceKey<ResearchEntry> FEEL_THE_BURN = create("feel_the_burn");
    public static final ResourceKey<ResearchEntry> SCAN_NETHER_STAR = create("scan_nether_star");
    public static final ResourceKey<ResearchEntry> SCAN_FLYING_CREATURE = create("scan_flying_creature");
    public static final ResourceKey<ResearchEntry> SCAN_GOLEM = create("scan_golem");
    public static final ResourceKey<ResearchEntry> WAND_TRANSFORM_HINT = create("wand_transform_hint");
    public static final ResourceKey<ResearchEntry> FOUND_SHRINE = create("found_shrine");
    public static final ResourceKey<ResearchEntry> GOT_DREAM = create("got_dream");
    public static final ResourceKey<ResearchEntry> SIPHON_PROMPT = create("siphon_prompt");
    public static final ResourceKey<ResearchEntry> UNKNOWN_RUNE = create("unknown_rune");
    
    // Commonly used research icons
    private static final ResourceLocation ICON_MANAWEAVING = ResourceUtils.loc("textures/research/discipline_manaweaving.png");
    private static final ResourceLocation ICON_ALCHEMY = ResourceUtils.loc("textures/research/discipline_alchemy.png");
    private static final ResourceLocation ICON_SORCERY = ResourceUtils.loc("textures/research/discipline_sorcery.png");
    private static final ResourceLocation ICON_RUNEWORKING = ResourceUtils.loc("textures/research/discipline_runeworking.png");
    private static final ResourceLocation ICON_RITUAL = ResourceUtils.loc("textures/research/discipline_ritual.png");
    private static final ResourceLocation ICON_MAGITECH = ResourceUtils.loc("textures/research/discipline_magitech.png");
    private static final ResourceLocation ICON_BAG = ResourceUtils.loc("textures/research/research_bag.png");
    private static final ResourceLocation ICON_MAP = ResourceUtils.loc("textures/research/research_map.png");
    private static final ResourceLocation ICON_TUBE = ResourceUtils.loc("textures/research/research_tube.png");
    private static final ResourceLocation ICON_UNKNOWN = ResourceUtils.loc("textures/research/research_unknown.png");
    
    public static ResourceKey<ResearchEntry> create(String name) {
        return ResourceKey.create(RegistryKeysPM.RESEARCH_ENTRIES, ResourceUtils.loc(name));
    }
    
    public static void bootstrap(BootstrapContext<ResearchEntry> context) {
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
    
    private static void bootstrapBasicsEntries(BootstrapContext<ResearchEntry> context) {
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.BASICS;
        register(context, FIRST_STEPS, key -> ResearchEntry.builder(key).discipline(discipline).icon(ItemRegistration.GRIMOIRE.get())
                .stage().requiredCraft(ItemRegistration.ARCANE_WORKBENCH.get()).recipe(ItemRegistration.MUNDANE_WAND.get()).end()
                .stage().requiredStat(StatsPM.MANA_SIPHONED, 10).recipe(ItemRegistration.MUNDANE_WAND.get()).end()
                .stage().requiredStat(StatsPM.OBSERVATIONS_MADE, 1).recipe(ItemRegistration.MUNDANE_WAND.get()).recipe(ItemRegistration.WOOD_TABLE.get()).recipe(ItemRegistration.MAGNIFYING_GLASS.get())
                        .recipe(ItemRegistration.ANALYSIS_TABLE.get()).end()
                .stage().recipe(ItemRegistration.MUNDANE_WAND.get()).recipe(ItemRegistration.WOOD_TABLE.get()).recipe(ItemRegistration.MAGNIFYING_GLASS.get()).recipe(ItemRegistration.ANALYSIS_TABLE.get()).end()
                .build());
        register(context, THEORYCRAFTING, key -> ResearchEntry.builder(key).discipline(discipline).icon("textures/research/knowledge_theory.png").parent(FIRST_STEPS)
                .stage().requiredObservations(1).end()
                .stage().requiredCraft(ItemRegistration.RESEARCH_TABLE.get()).requiredCraft(ItemRegistration.ENCHANTED_INK_AND_QUILL.get()).recipe(ItemRegistration.RESEARCH_TABLE.get()).recipe(ItemRegistration.ENCHANTED_INK.get())
                        .recipe(ItemRegistration.ENCHANTED_INK_AND_QUILL.get()).end()
                .stage().requiredStat(StatsPM.THEORIES_FORMED, 1).recipe(ItemRegistration.RESEARCH_TABLE.get()).recipe(ItemRegistration.ENCHANTED_INK.get()).recipe(ItemRegistration.ENCHANTED_INK_AND_QUILL.get()).end()
                .stage().recipe(ItemRegistration.RESEARCH_TABLE.get()).recipe(ItemRegistration.ENCHANTED_INK.get()).recipe(ItemRegistration.ENCHANTED_INK_AND_QUILL.get()).end()
                .build());
        register(context, ATTUNEMENTS, key -> ResearchEntry.builder(key).discipline(discipline).parent(FIRST_STEPS)
                .stage().requiredObservations(1).end()
                .stage().end()
                .build());
        register(context, LINGUISTICS, key -> ResearchEntry.builder(key).discipline(discipline).icon(Items.WRITABLE_BOOK).parent(FIRST_STEPS)
                .stage().requiredObservations(1).requiredStat(StatsPM.ANCIENT_BOOKS_READ, 1).end()
                .stage().recipe(ItemRegistration.SCRIBE_TABLE.get()).recipe(ItemRegistration.LORE_TABLET_DIRTY.get()).end()
                .build());
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
                .stage().requiredItem(Tags.Items.BONES).requiredItem(ItemRegistration.BLOODY_FLESH.get()).requiredObservations(1).requiredStat(StatsPM.MANA_SPENT_BLOOD, 100).end()
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
        register(context, SECRETS_OF_THE_UNIVERSE, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ICON_UNKNOWN)
                .stage().requiredResearch(SOTU_DISCOVER_BLOOD).requiredResearch(SOTU_DISCOVER_INFERNAL).requiredResearch(SOTU_DISCOVER_VOID)
                        .requiredResearch(SOTU_RESEARCH_ARCANOMETER).requiredResearch(SOTU_RESEARCH_HEXIUM).requiredResearch(SOTU_RESEARCH_POWER_RUNE)
                        .requiredResearch(SOTU_RESEARCH_SANGUINE_CRUCIBLE).requiredResearch(SOTU_RESEARCH_CLEANSING_RITE).requiredResearch(SOTU_SCAN_HALLOWED_ORB).end()
                .stage().attunement(Sources.HALLOWED, 4).end()
                .build());
        register(context, COMPLETE_BASICS, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ItemRegistration.GRIMOIRE.get()).finale(discipline)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .attunement(Sources.BLOOD, 1).attunement(Sources.INFERNAL, 1).attunement(Sources.VOID, 1).attunement(Sources.HALLOWED, 1).end()
                .build());
        register(context, THEORY_OF_EVERYTHING, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ItemRegistration.GRIMOIRE.get())
                .finale(ResearchDisciplines.BASICS).finale(ResearchDisciplines.ALCHEMY).finale(ResearchDisciplines.MAGITECH).finale(ResearchDisciplines.MANAWEAVING)
                .finale(ResearchDisciplines.RITUAL).finale(ResearchDisciplines.RUNEWORKING).finale(ResearchDisciplines.SORCERY)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 2).attunement(Sources.SEA, 2).attunement(Sources.SKY, 2).attunement(Sources.SUN, 2).attunement(Sources.MOON, 2)
                        .attunement(Sources.BLOOD, 2).attunement(Sources.INFERNAL, 2).attunement(Sources.VOID, 2).attunement(Sources.HALLOWED, 2).end()
                .build());
    }
    
    private static void bootstrapManaweavingEntries(BootstrapContext<ResearchEntry> context) {
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.MANAWEAVING;
        register(context, BASIC_MANAWEAVING, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ICON_MANAWEAVING).parent(UNLOCK_MANAWEAVING)
                .stage().recipe(ItemRegistration.MANA_PRISM.get()).end()
                .build());
        register(context, EXPERT_MANAWEAVING, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ICON_MANAWEAVING).parent(MANA_ARROWS).parent(WAND_CHARGER)
                .stage().requiredExpertise(discipline, ResearchTier.EXPERT).end()
                .stage().recipe(ItemRegistration.MARBLE_ENCHANTED.get()).recipe(ItemRegistration.MARBLE_ENCHANTED_BRICK_SLAB.get()).recipe(ItemRegistration.MARBLE_ENCHANTED_BRICK_STAIRS.get())
                        .recipe(ItemRegistration.MARBLE_ENCHANTED_BRICK_WALL.get()).recipe(ItemRegistration.MARBLE_ENCHANTED_BRICKS.get()).recipe(ItemRegistration.MARBLE_ENCHANTED_CHISELED.get())
                        .recipe(ItemRegistration.MARBLE_ENCHANTED_PILLAR.get()).recipe(ItemRegistration.MARBLE_ENCHANTED_RUNED.get()).recipe(ItemRegistration.MARBLE_ENCHANTED_SLAB.get())
                        .recipe(ItemRegistration.MARBLE_ENCHANTED_STAIRS.get()).recipe(ItemRegistration.MARBLE_ENCHANTED_WALL.get()).recipe(ItemRegistration.MARBLE_ENCHANTED_BOOKSHELF.get()).end()
                .build());
        register(context, MASTER_MANAWEAVING, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ICON_MANAWEAVING).parent(WAND_CAP_GOLD).parent(WAND_GEM_ADEPT)
                .stage().requiredResearch(DISCOVER_FORBIDDEN).requiredExpertise(discipline, ResearchTier.MASTER).end()
                .stage().recipe(ItemRegistration.MARBLE_SMOKED.get()).recipe(ItemRegistration.MARBLE_SMOKED_BRICK_SLAB.get()).recipe(ItemRegistration.MARBLE_SMOKED_BRICK_STAIRS.get())
                        .recipe(ItemRegistration.MARBLE_SMOKED_BRICK_WALL.get()).recipe(ItemRegistration.MARBLE_SMOKED_BRICKS.get()).recipe(ItemRegistration.MARBLE_SMOKED_CHISELED.get())
                        .recipe(ItemRegistration.MARBLE_SMOKED_PILLAR.get()).recipe(ItemRegistration.MARBLE_SMOKED_RUNED.get()).recipe(ItemRegistration.MARBLE_SMOKED_SLAB.get())
                        .recipe(ItemRegistration.MARBLE_SMOKED_STAIRS.get()).recipe(ItemRegistration.MARBLE_SMOKED_WALL.get()).recipe(ItemRegistration.MARBLE_SMOKED_BOOKSHELF.get()).end()
                .build());
        register(context, SUPREME_MANAWEAVING, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ICON_MANAWEAVING).parent(WAND_CAP_HEXIUM).parent(WAND_GEM_WIZARD)
                .stage().requiredResearch(DISCOVER_HALLOWED).requiredExpertise(discipline, ResearchTier.SUPREME).reveals(SECRETS_OF_THE_UNIVERSE).end()
                .stage().recipe(ItemRegistration.MARBLE_HALLOWED.get()).recipe(ItemRegistration.MARBLE_HALLOWED_BRICK_SLAB.get()).recipe(ItemRegistration.MARBLE_HALLOWED_BRICK_STAIRS.get())
                        .recipe(ItemRegistration.MARBLE_HALLOWED_BRICK_WALL.get()).recipe(ItemRegistration.MARBLE_HALLOWED_BRICKS.get()).recipe(ItemRegistration.MARBLE_HALLOWED_CHISELED.get())
                        .recipe(ItemRegistration.MARBLE_HALLOWED_PILLAR.get()).recipe(ItemRegistration.MARBLE_HALLOWED_RUNED.get()).recipe(ItemRegistration.MARBLE_HALLOWED_SLAB.get())
                        .recipe(ItemRegistration.MARBLE_HALLOWED_STAIRS.get()).recipe(ItemRegistration.MARBLE_HALLOWED_WALL.get()).recipe(ItemRegistration.MARBLE_HALLOWED_BOOKSHELF.get()).end()
                .build());
        register(context, COMPLETE_MANAWEAVING, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ICON_MANAWEAVING).finale(discipline)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .attunement(Sources.BLOOD, 1).attunement(Sources.INFERNAL, 1).attunement(Sources.VOID, 1).attunement(Sources.HALLOWED, 1).end()
                .build());
        register(context, WAND_CHARGER, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.WAND_CHARGER.get()).parent(BASIC_MANAWEAVING)
                .stage().requiredItem(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS).requiredObservations(1).end()
                .stage().recipe(ItemRegistration.WAND_CHARGER.get()).end()
                .build());
        register(context, MANA_SALTS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.MANA_SALTS.get()).parent(WAND_CHARGER)
                .stage().requiredItem(ItemRegistration.ESSENCE_DUST_EARTH.get()).requiredItem(ItemRegistration.ESSENCE_DUST_SEA.get()).requiredItem(ItemRegistration.ESSENCE_DUST_SKY.get())
                        .requiredItem(ItemRegistration.ESSENCE_DUST_SUN.get()).requiredItem(ItemRegistration.ESSENCE_DUST_MOON.get()).requiredObservations(1).end()
                .stage().recipe(ItemRegistration.MANA_SALTS.get()).end()
                .build());
        register(context, ADVANCED_WANDMAKING, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).parent(WAND_CHARGER)
                .stage().requiredObservations(1).requiredStat(StatsPM.MANA_SPENT_TOTAL, 20).end()
                .stage().recipe(ItemRegistration.WAND_ASSEMBLY_TABLE.get()).end()
                .build());
        register(context, STAVES, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).parent(EXPERT_MANAWEAVING).parent(WAND_GEM_ADEPT)
                .parent(WAND_INSCRIPTION).parent(SHARD_SYNTHESIS)
                .stage().requiredTheories(1).end()
                .stage().end()
                .build());
        register(context, WAND_CORE_HEARTWOOD, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.HEARTWOOD_WAND_CORE_ITEM.get()).parent(ADVANCED_WANDMAKING)
                .stage().requiredItem(ItemRegistration.HEARTWOOD.get()).requiredObservations(1).end()
                .stage().recipe(ItemRegistration.HEARTWOOD_WAND_CORE_ITEM.get()).recipe("charcoal_from_smelting_heartwood").end()
                .addendum().requiredResearch(STAVES).recipe(ItemRegistration.HEARTWOOD_STAFF_CORE_ITEM.get()).end()
                .build());
        register(context, WAND_CAP_IRON, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.IRON_WAND_CAP_ITEM.get()).parent(ADVANCED_WANDMAKING)
                .stage().requiredItem(Tags.Items.INGOTS_IRON).requiredObservations(1).end()
                .stage().recipe(ItemRegistration.IRON_WAND_CAP_ITEM.get()).end()
                .build());
        register(context, WAND_GEM_APPRENTICE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.APPRENTICE_WAND_GEM_ITEM.get()).parent(ADVANCED_WANDMAKING)
                .stage().requiredItem(Tags.Items.GEMS_DIAMOND).requiredObservations(1).end()
                .stage().recipe(ItemRegistration.APPRENTICE_WAND_GEM_ITEM.get()).end()
                .build());
        register(context, EARTHSHATTER_HAMMER, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.EARTHSHATTER_HAMMER.get())
                .parent(EXPERT_MANAWEAVING).parent(SHARD_SYNTHESIS)
                .stage().requiredItem(Items.RAW_IRON).requiredItem(Items.RAW_GOLD).requiredItem(Items.RAW_COPPER).requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 2).recipe(ItemRegistration.EARTHSHATTER_HAMMER.get()).recipe("iron_grit_from_ore")
                        .recipe("iron_grit_from_raw_metal").recipe("gold_grit_from_ore")
                        .recipe("gold_grit_from_raw_metal").recipe("copper_grit_from_ore")
                        .recipe("copper_grit_from_raw_metal").recipe("tin_dust_from_ore")
                        .recipe("tin_dust_from_raw_metal").recipe("lead_dust_from_ore")
                        .recipe("lead_dust_from_raw_metal").recipe("silver_dust_from_ore")
                        .recipe("silver_dust_from_raw_metal").recipe("uranium_dust_from_ore")
                        .recipe("uranium_dust_from_raw_metal").recipe("iron_ingot_from_grit_smelting")
                        .recipe("gold_ingot_from_grit_smelting").recipe("copper_ingot_from_grit_smelting")
                        .recipe("cobblestone_from_earthshatter_hammer").recipe("cobbled_deepslate_from_earthshatter_hammer")
                        .recipe("gravel_from_earthshatter_hammer").recipe("sand_from_earthshatter_hammer")
                        .recipe("rock_salt_from_earthshatter_hammer").recipe("refined_salt_from_earthshatter_hammer")
                        .recipe("netherite_scrap_from_earthshatter_hammer").end()
                .build());
        register(context, SUNLAMP, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.SUNLAMP.get()).parent(EXPERT_MANAWEAVING).parent(PRIMALITE)
                .stage().requiredItem(Items.LANTERN).requiredTheories(1).end()
                .stage().attunement(Sources.SUN, 2).recipe(ItemRegistration.SUNLAMP.get()).end()
                .addendum().requiredResearch(HEXIUM).attunement(Sources.INFERNAL, 2).recipe(ItemRegistration.SPIRIT_LANTERN.get()).end()
                .build());
        register(context, WAND_GEM_ADEPT, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.ADEPT_WAND_GEM_ITEM.get())
                .parent(EXPERT_MANAWEAVING).parent(WAND_GEM_APPRENTICE).parent(SHARD_SYNTHESIS)
                .stage().requiredItem(Tags.Items.GEMS_DIAMOND).requiredTheories(1).end()
                .stage().recipe(ItemRegistration.ADEPT_WAND_GEM_ITEM.get()).end()
                .build());
        register(context, WAND_GEM_WIZARD, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.WIZARD_WAND_GEM_ITEM.get())
                .parent(MASTER_MANAWEAVING).parent(WAND_GEM_ADEPT).parent(CRYSTAL_SYNTHESIS)
                .stage().requiredItem(Tags.Items.GEMS_DIAMOND).requiredTheories(2).end()
                .stage().recipe(ItemRegistration.WIZARD_WAND_GEM_ITEM.get()).end()
                .build());
        register(context, WAND_GEM_ARCHMAGE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.ARCHMAGE_WAND_GEM_ITEM.get())
                .parent(SUPREME_MANAWEAVING).parent(WAND_GEM_WIZARD).parent(CLUSTER_SYNTHESIS)
                .stage().requiredItem(Tags.Items.GEMS_DIAMOND).requiredTheories(3).end()
                .stage().recipe(ItemRegistration.ARCHMAGE_WAND_GEM_ITEM.get()).end()
                .build());
        register(context, WAND_CAP_GOLD, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.GOLD_WAND_CAP_ITEM.get())
                .parent(EXPERT_MANAWEAVING).parent(WAND_CAP_IRON)
                .stage().requiredItem(Tags.Items.INGOTS_GOLD).requiredTheories(1).end()
                .stage().recipe(ItemRegistration.GOLD_WAND_CAP_ITEM.get()).end()
                .build());
        register(context, WAND_CAP_PRIMALITE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.PRIMALITE_WAND_CAP_ITEM.get())
                .parent(WAND_CAP_GOLD).parent(PRIMALITE)
                .stage().requiredItem(ItemTagsPM.INGOTS_PRIMALITE).requiredTheories(1).end()
                .stage().recipe(ItemRegistration.PRIMALITE_WAND_CAP_ITEM.get()).end()
                .build());
        register(context, WAND_CAP_HEXIUM, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.HEXIUM_WAND_CAP_ITEM.get())
                .parent(MASTER_MANAWEAVING).parent(WAND_CAP_PRIMALITE).parent(HEXIUM)
                .stage().requiredItem(ItemTagsPM.INGOTS_HEXIUM).requiredTheories(2).end()
                .stage().recipe(ItemRegistration.HEXIUM_WAND_CAP_ITEM.get()).end()
                .build());
        register(context, WAND_CAP_HALLOWSTEEL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.HALLOWSTEEL_WAND_CAP_ITEM.get())
                .parent(SUPREME_MANAWEAVING).parent(WAND_CAP_HEXIUM).parent(HALLOWSTEEL)
                .stage().requiredItem(ItemTagsPM.INGOTS_HALLOWSTEEL).requiredTheories(3).end()
                .stage().recipe(ItemRegistration.HALLOWSTEEL_WAND_CAP_ITEM.get()).end()
                .build());
        register(context, WAND_CORE_OBSIDIAN, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.OBSIDIAN_WAND_CORE_ITEM.get())
                .parent(EXPERT_MANAWEAVING).parent(WAND_CORE_HEARTWOOD)
                .stage().requiredItem(Tags.Items.OBSIDIAN).requiredTheories(1).end()
                .stage().attunement(Sources.EARTH, 2).recipe(ItemRegistration.OBSIDIAN_WAND_CORE_ITEM.get()).end()
                .addendum().requiredResearch(STAVES).recipe(ItemRegistration.OBSIDIAN_STAFF_CORE_ITEM.get()).end()
                .build());
        register(context, WAND_CORE_CORAL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.CORAL_WAND_CORE_ITEM.get())
                .parent(EXPERT_MANAWEAVING).parent(WAND_CORE_OBSIDIAN)
                .stage().requiredItem(ItemTagsPM.CORAL_BLOCKS).requiredTheories(1).end()
                .stage().attunement(Sources.SEA, 2).recipe(ItemRegistration.CORAL_WAND_CORE_ITEM.get()).end()
                .addendum().requiredResearch(STAVES).recipe(ItemRegistration.CORAL_STAFF_CORE_ITEM.get()).end()
                .build());
        register(context, WAND_CORE_BAMBOO, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.BAMBOO_WAND_CORE_ITEM.get())
                .parent(EXPERT_MANAWEAVING).parent(WAND_CORE_OBSIDIAN)
                .stage().requiredItem(Items.BAMBOO).requiredTheories(1).end()
                .stage().attunement(Sources.SKY, 2).recipe(ItemRegistration.BAMBOO_WAND_CORE_ITEM.get()).end()
                .addendum().requiredResearch(STAVES).recipe(ItemRegistration.BAMBOO_STAFF_CORE_ITEM.get()).end()
                .build());
        register(context, WAND_CORE_SUNWOOD, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.SUNWOOD_WAND_CORE_ITEM.get())
                .parent(EXPERT_MANAWEAVING).parent(WAND_CORE_OBSIDIAN)
                .stage().requiredItem(ItemRegistration.SUNWOOD_LOG.get()).requiredTheories(1).end()
                .stage().attunement(Sources.SUN, 2).recipe(ItemRegistration.SUNWOOD_WAND_CORE_ITEM.get()).end()
                .addendum().requiredResearch(STAVES).recipe(ItemRegistration.SUNWOOD_STAFF_CORE_ITEM.get()).end()
                .build());
        register(context, WAND_CORE_MOONWOOD, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.MOONWOOD_WAND_CORE_ITEM.get())
                .parent(EXPERT_MANAWEAVING).parent(WAND_CORE_OBSIDIAN)
                .stage().requiredItem(ItemRegistration.MOONWOOD_LOG.get()).requiredTheories(1).end()
                .stage().attunement(Sources.MOON, 2).recipe(ItemRegistration.MOONWOOD_WAND_CORE_ITEM.get()).end()
                .addendum().requiredResearch(STAVES).recipe(ItemRegistration.MOONWOOD_STAFF_CORE_ITEM.get()).end()
                .build());
        register(context, WAND_CORE_BONE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.BONE_WAND_CORE_ITEM.get())
                .parent(MASTER_MANAWEAVING).parent(WAND_CORE_HEARTWOOD).parent(DISCOVER_BLOOD)
                .stage().requiredItem(Items.BONE).requiredTheories(2).end()
                .stage().attunement(Sources.BLOOD, 3).recipe(ItemRegistration.BONE_WAND_CORE_ITEM.get()).end()
                .addendum().requiredResearch(STAVES).recipe(ItemRegistration.BONE_STAFF_CORE_ITEM.get()).end()
                .build());
        register(context, WAND_CORE_BLAZE_ROD, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.BLAZE_ROD_WAND_CORE_ITEM.get())
                .parent(MASTER_MANAWEAVING).parent(WAND_CORE_HEARTWOOD).parent(DISCOVER_INFERNAL)
                .stage().requiredItem(Tags.Items.RODS_BLAZE).requiredTheories(2).end()
                .stage().attunement(Sources.INFERNAL, 3).recipe(ItemRegistration.BLAZE_ROD_WAND_CORE_ITEM.get()).end()
                .addendum().requiredResearch(STAVES).recipe(ItemRegistration.BLAZE_ROD_STAFF_CORE_ITEM.get()).end()
                .build());
        register(context, WAND_CORE_PURPUR, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.PURPUR_WAND_CORE_ITEM.get())
                .parent(MASTER_MANAWEAVING).parent(WAND_CORE_HEARTWOOD).parent(DISCOVER_VOID)
                .stage().requiredItem(Items.PURPUR_BLOCK).requiredTheories(2).end()
                .stage().attunement(Sources.VOID, 3).recipe(ItemRegistration.PURPUR_WAND_CORE_ITEM.get()).end()
                .addendum().requiredResearch(STAVES).recipe(ItemRegistration.PURPUR_STAFF_CORE_ITEM.get()).end()
                .build());
        register(context, IMBUED_WOOL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.IMBUED_WOOL_HEAD.get()).parent(MANA_ARROWS)
                .stage().requiredItem(ItemTags.WOOL).requiredObservations(1).end()
                .stage().recipe(ItemRegistration.IMBUED_WOOL_HEAD.get()).recipe(ItemRegistration.IMBUED_WOOL_CHEST.get()).recipe(ItemRegistration.IMBUED_WOOL_LEGS.get())
                        .recipe(ItemRegistration.IMBUED_WOOL_FEET.get()).end()
                .build());
        register(context, SPELLCLOTH, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.SPELLCLOTH_HEAD.get())
                .parent(EXPERT_MANAWEAVING).parent(IMBUED_WOOL).parent(EARTHSHATTER_HAMMER)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.SPELLCLOTH.get()).recipe(ItemRegistration.SPELLCLOTH_HEAD.get()).recipe(ItemRegistration.SPELLCLOTH_CHEST.get())
                        .recipe(ItemRegistration.SPELLCLOTH_LEGS.get()).recipe(ItemRegistration.SPELLCLOTH_FEET.get()).end()
                .build());
        register(context, HEXWEAVE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.HEXWEAVE_HEAD.get())
                .parent(MASTER_MANAWEAVING).parent(SPELLCLOTH).parent(SHARD_SYNTHESIS).parent(DISCOVER_BLOOD).parent(DISCOVER_INFERNAL).parent(DISCOVER_VOID)
                .stage().requiredTheories(2).end()
                .stage().recipe(ItemRegistration.HEXWEAVE.get()).recipe(ItemRegistration.HEXWEAVE_HEAD.get()).recipe(ItemRegistration.HEXWEAVE_CHEST.get())
                        .recipe(ItemRegistration.HEXWEAVE_LEGS.get()).recipe(ItemRegistration.HEXWEAVE_FEET.get()).end()
                .build());
        register(context, SAINTSWOOL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.SAINTSWOOL_HEAD.get())
                .parent(SUPREME_MANAWEAVING).parent(HEXWEAVE).parent(CRYSTAL_SYNTHESIS)
                .stage().requiredTheories(3).end()
                .stage().recipe(ItemRegistration.SAINTSWOOL.get()).recipe(ItemRegistration.SAINTSWOOL_HEAD.get()).recipe(ItemRegistration.SAINTSWOOL_CHEST.get())
                        .recipe(ItemRegistration.SAINTSWOOL_LEGS.get()).recipe(ItemRegistration.SAINTSWOOL_FEET.get()).end()
                .build());
        register(context, ARTIFICIAL_MANA_FONTS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.ARTIFICIAL_FONT_EARTH.get())
                .parent(EXPERT_MANAWEAVING).parent(SHARD_SYNTHESIS).parent(PRIMALITE).parent(SUNLAMP)
                .stage().requiredTheories(1).requiredStat(StatsPM.MANA_SIPHONED, 1000).end()
                .stage().recipe(ItemRegistration.ARTIFICIAL_FONT_EARTH.get()).recipe(ItemRegistration.ARTIFICIAL_FONT_SEA.get()).recipe(ItemRegistration.ARTIFICIAL_FONT_SKY.get())
                        .recipe(ItemRegistration.ARTIFICIAL_FONT_SUN.get()).recipe(ItemRegistration.ARTIFICIAL_FONT_MOON.get()).end()
                .addendum().requiredResearch(DISCOVER_BLOOD).recipe(ItemRegistration.ARTIFICIAL_FONT_BLOOD.get()).end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).recipe(ItemRegistration.ARTIFICIAL_FONT_INFERNAL.get()).end()
                .addendum().requiredResearch(DISCOVER_VOID).recipe(ItemRegistration.ARTIFICIAL_FONT_VOID.get()).end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).recipe(ItemRegistration.ARTIFICIAL_FONT_HALLOWED.get()).end()
                .build());
        register(context, FORBIDDEN_MANA_FONTS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.FORBIDDEN_FONT_EARTH.get())
                .parent(ARTIFICIAL_MANA_FONTS).parent(MASTER_MANAWEAVING).parent(CRYSTAL_SYNTHESIS).parent(HEXIUM)
                .stage().requiredTheories(2).end()
                .stage().recipe(ItemRegistration.FORBIDDEN_FONT_EARTH.get()).recipe(ItemRegistration.FORBIDDEN_FONT_SEA.get()).recipe(ItemRegistration.FORBIDDEN_FONT_SKY.get())
                        .recipe(ItemRegistration.FORBIDDEN_FONT_SUN.get()).recipe(ItemRegistration.FORBIDDEN_FONT_MOON.get()).end()
                .addendum().requiredResearch(DISCOVER_BLOOD).recipe(ItemRegistration.FORBIDDEN_FONT_BLOOD.get()).end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).recipe(ItemRegistration.FORBIDDEN_FONT_INFERNAL.get()).end()
                .addendum().requiredResearch(DISCOVER_VOID).recipe(ItemRegistration.FORBIDDEN_FONT_VOID.get()).end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).recipe(ItemRegistration.FORBIDDEN_FONT_HALLOWED.get()).end()
                .build());
        register(context, HEAVENLY_MANA_FONTS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.HEAVENLY_FONT_EARTH.get())
                .parent(FORBIDDEN_MANA_FONTS).parent(SUPREME_MANAWEAVING).parent(CLUSTER_SYNTHESIS).parent(HALLOWSTEEL)
                .stage().requiredTheories(3).end()
                .stage().recipe(ItemRegistration.HEAVENLY_FONT_EARTH.get()).recipe(ItemRegistration.HEAVENLY_FONT_SEA.get()).recipe(ItemRegistration.HEAVENLY_FONT_SKY.get())
                        .recipe(ItemRegistration.HEAVENLY_FONT_SUN.get()).recipe(ItemRegistration.HEAVENLY_FONT_MOON.get()).end()
                .addendum().requiredResearch(DISCOVER_BLOOD).recipe(ItemRegistration.HEAVENLY_FONT_BLOOD.get()).end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).recipe(ItemRegistration.HEAVENLY_FONT_INFERNAL.get()).end()
                .addendum().requiredResearch(DISCOVER_VOID).recipe(ItemRegistration.HEAVENLY_FONT_VOID.get()).end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).recipe(ItemRegistration.HEAVENLY_FONT_HALLOWED.get()).end()
                .build());
        register(context, MANA_ARROWS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.MANA_ARROW_EARTH.get()).parent(BASIC_MANAWEAVING)
                .stage().requiredObservations(1).requiredCraft(Items.ARROW).end()
                .stage().recipe(ItemRegistration.MANA_ARROW_EARTH.get()).recipe(ItemRegistration.MANA_ARROW_SEA.get()).recipe(ItemRegistration.MANA_ARROW_SKY.get()).recipe(ItemRegistration.MANA_ARROW_SUN.get())
                        .recipe(ItemRegistration.MANA_ARROW_MOON.get()).end()
                .addendum().requiredResearch(DISCOVER_BLOOD).recipe(ItemRegistration.MANA_ARROW_BLOOD.get()).end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).recipe(ItemRegistration.MANA_ARROW_INFERNAL.get()).end()
                .addendum().requiredResearch(DISCOVER_VOID).recipe(ItemRegistration.MANA_ARROW_VOID.get()).end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).recipe(ItemRegistration.MANA_ARROW_HALLOWED.get()).end()
                .build());
        register(context, ESSENCE_CASK_ENCHANTED, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.ESSENCE_CASK_ENCHANTED.get())
                .parent(EXPERT_MANAWEAVING).parent(WAND_CORE_HEARTWOOD).parent(SHARD_SYNTHESIS).parent(PRIMALITE)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.ESSENCE_CASK_ENCHANTED.get()).end()
                .build());
        register(context, ESSENCE_CASK_FORBIDDEN, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.ESSENCE_CASK_FORBIDDEN.get())
                .parent(MASTER_MANAWEAVING).parent(ESSENCE_CASK_ENCHANTED).parent(CRYSTAL_SYNTHESIS).parent(HEXIUM)
                .stage().requiredTheories(2).end()
                .stage().recipe(ItemRegistration.ESSENCE_CASK_FORBIDDEN.get()).end()
                .build());
        register(context, ESSENCE_CASK_HEAVENLY, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.ESSENCE_CASK_HEAVENLY.get())
                .parent(SUPREME_MANAWEAVING).parent(ESSENCE_CASK_FORBIDDEN).parent(CLUSTER_SYNTHESIS).parent(HALLOWSTEEL)
                .stage().requiredTheories(3).end()
                .stage().recipe(ItemRegistration.ESSENCE_CASK_HEAVENLY.get()).end()
                .build());
        register(context, WAND_GLAMOUR_TABLE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.WAND_GLAMOUR_TABLE.get())
                .parent(EXPERT_MANAWEAVING).parent(WAND_CORE_HEARTWOOD)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.MOON, 2).recipe(ItemRegistration.WAND_GLAMOUR_TABLE.get()).end()
                .build());
        register(context, ATTUNEMENT_SHACKLES, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.ATTUNEMENT_SHACKLES_EARTH.get())
                .parent(EXPERT_MANAWEAVING).parent(SHARD_SYNTHESIS).parent(ATTUNEMENTS)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.ATTUNEMENT_SHACKLES_EARTH.get()).recipe(ItemRegistration.ATTUNEMENT_SHACKLES_SEA.get())
                        .recipe(ItemRegistration.ATTUNEMENT_SHACKLES_SKY.get()).recipe(ItemRegistration.ATTUNEMENT_SHACKLES_SUN.get()).recipe(ItemRegistration.ATTUNEMENT_SHACKLES_MOON.get()).end()
                .addendum().requiredResearch(DISCOVER_BLOOD).recipe(ItemRegistration.ATTUNEMENT_SHACKLES_BLOOD.get()).end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).recipe(ItemRegistration.ATTUNEMENT_SHACKLES_INFERNAL.get()).end()
                .addendum().requiredResearch(DISCOVER_VOID).recipe(ItemRegistration.ATTUNEMENT_SHACKLES_VOID.get()).end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).recipe(ItemRegistration.ATTUNEMENT_SHACKLES_HALLOWED.get()).end()
                .build());
    }
    
    private static void bootstrapAlchemyEntries(BootstrapContext<ResearchEntry> context) {
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.ALCHEMY;
        register(context, BASIC_ALCHEMY, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ICON_ALCHEMY).parent(UNLOCK_ALCHEMY)
                .stage().requiredCraft(ItemRegistration.ESSENCE_FURNACE.get()).end()
                .stage().end()
                .build());
        register(context, EXPERT_ALCHEMY, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ICON_ALCHEMY).parent(CALCINATOR_BASIC).parent(STONEMELDING)
                .stage().requiredExpertise(discipline, ResearchTier.EXPERT).end()
                .stage().end()
                .build());
        register(context, MASTER_ALCHEMY, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ICON_ALCHEMY).parent(CALCINATOR_ENCHANTED).parent(PRIMALITE)
                .stage().requiredResearch(DISCOVER_FORBIDDEN).requiredExpertise(discipline, ResearchTier.MASTER).end()
                .stage().end()
                .build());
        register(context, SUPREME_ALCHEMY, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ICON_ALCHEMY).parent(CALCINATOR_FORBIDDEN).parent(HEXIUM)
                .stage().requiredResearch(DISCOVER_HALLOWED).requiredExpertise(discipline, ResearchTier.SUPREME).reveals(SECRETS_OF_THE_UNIVERSE).end()
                .stage().end()
                .build());
        register(context, COMPLETE_ALCHEMY, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ICON_ALCHEMY).finale(discipline)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .attunement(Sources.BLOOD, 1).attunement(Sources.INFERNAL, 1).attunement(Sources.VOID, 1).attunement(Sources.HALLOWED, 1).end()
                .build());
        register(context, STONEMELDING, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(Items.STONE).parent(BASIC_ALCHEMY)
                .stage().requiredItem(ItemRegistration.ESSENCE_DUST_EARTH.get()).requiredCraft(Items.STONE).requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).recipe("stone_from_stonemelding").recipe("deepslate_from_stonemelding")
                        .recipe("cobblestone_from_stonemelding").recipe("gravel_from_stonemelding").end()
                .build());
        register(context, SKYGLASS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.SKYGLASS.get()).parent(STONEMELDING)
                .stage().requiredItem(ItemRegistration.ESSENCE_DUST_SKY.get()).requiredCraft(Items.GLASS).requiredObservations(1).end()
                .stage().attunement(Sources.SKY, 1)
                        .recipe(ItemRegistration.SKYGLASS.get()).recipe(ItemRegistration.SKYGLASS_PANE.get())
                        .recipe(ItemRegistration.STAINED_SKYGLASS_BLACK.get()).recipe(ItemRegistration.STAINED_SKYGLASS_BLUE.get()).recipe(ItemRegistration.STAINED_SKYGLASS_BROWN.get()).recipe(ItemRegistration.STAINED_SKYGLASS_CYAN.get())
                        .recipe(ItemRegistration.STAINED_SKYGLASS_GRAY.get()).recipe(ItemRegistration.STAINED_SKYGLASS_GREEN.get()).recipe(ItemRegistration.STAINED_SKYGLASS_LIGHT_BLUE.get()).recipe(ItemRegistration.STAINED_SKYGLASS_LIGHT_GRAY.get())
                        .recipe(ItemRegistration.STAINED_SKYGLASS_LIME.get()).recipe(ItemRegistration.STAINED_SKYGLASS_MAGENTA.get()).recipe(ItemRegistration.STAINED_SKYGLASS_ORANGE.get()).recipe(ItemRegistration.STAINED_SKYGLASS_PINK.get())
                        .recipe(ItemRegistration.STAINED_SKYGLASS_PURPLE.get()).recipe(ItemRegistration.STAINED_SKYGLASS_RED.get()).recipe(ItemRegistration.STAINED_SKYGLASS_WHITE.get()).recipe(ItemRegistration.STAINED_SKYGLASS_YELLOW.get())
                        .recipe("stained_skyglass_pane_black_from_blocks").recipe("stained_skyglass_pane_black_from_panes")
                        .recipe("stained_skyglass_pane_blue_from_blocks").recipe("stained_skyglass_pane_blue_from_panes")
                        .recipe("stained_skyglass_pane_brown_from_blocks").recipe("stained_skyglass_pane_brown_from_panes")
                        .recipe("stained_skyglass_pane_cyan_from_blocks").recipe("stained_skyglass_pane_cyan_from_panes")
                        .recipe("stained_skyglass_pane_gray_from_blocks").recipe("stained_skyglass_pane_gray_from_panes")
                        .recipe("stained_skyglass_pane_green_from_blocks").recipe("stained_skyglass_pane_green_from_panes")
                        .recipe("stained_skyglass_pane_light_blue_from_blocks").recipe("stained_skyglass_pane_light_blue_from_panes")
                        .recipe("stained_skyglass_pane_light_gray_from_blocks").recipe("stained_skyglass_pane_light_gray_from_panes")
                        .recipe("stained_skyglass_pane_lime_from_blocks").recipe("stained_skyglass_pane_lime_from_panes")
                        .recipe("stained_skyglass_pane_magenta_from_blocks").recipe("stained_skyglass_pane_magenta_from_panes")
                        .recipe("stained_skyglass_pane_orange_from_blocks").recipe("stained_skyglass_pane_orange_from_panes")
                        .recipe("stained_skyglass_pane_pink_from_blocks").recipe("stained_skyglass_pane_pink_from_panes")
                        .recipe("stained_skyglass_pane_purple_from_blocks").recipe("stained_skyglass_pane_purple_from_panes")
                        .recipe("stained_skyglass_pane_red_from_blocks").recipe("stained_skyglass_pane_red_from_panes")
                        .recipe("stained_skyglass_pane_white_from_blocks").recipe("stained_skyglass_pane_white_from_panes")
                        .recipe("stained_skyglass_pane_yellow_from_blocks").recipe("stained_skyglass_pane_yellow_from_panes")
                        .end()
                .build());
        register(context, SHARD_SYNTHESIS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.ESSENCE_SHARD_EARTH.get()).parent(EXPERT_ALCHEMY)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .recipe("essence_shard_earth_from_dust").recipe("essence_shard_sea_from_dust").recipe("essence_shard_sky_from_dust").recipe("essence_shard_sun_from_dust")
                        .recipe("essence_shard_moon_from_dust").end()
                .addendum().requiredResearch(DISCOVER_BLOOD).attunement(Sources.BLOOD, 1).recipe("essence_shard_blood_from_dust").end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 1).recipe("essence_shard_infernal_from_dust").end()
                .addendum().requiredResearch(DISCOVER_VOID).attunement(Sources.VOID, 1).recipe("essence_shard_void_from_dust").end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 1).recipe("essence_shard_hallowed_from_dust").end()
                .build());
        register(context, SHARD_DESYNTHESIS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.ESSENCE_SHARD_EARTH.get()).parent(SHARD_SYNTHESIS)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .recipe("essence_dust_earth_from_shard").recipe("essence_dust_sea_from_shard").recipe("essence_dust_sky_from_shard").recipe("essence_dust_sun_from_shard")
                        .recipe("essence_dust_moon_from_shard").end()
                .addendum().requiredResearch(DISCOVER_BLOOD).attunement(Sources.BLOOD, 1).recipe("essence_dust_blood_from_shard").end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 1).recipe("essence_dust_infernal_from_shard").end()
                .addendum().requiredResearch(DISCOVER_VOID).attunement(Sources.VOID, 1).recipe("essence_dust_void_from_shard").end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 1).recipe("essence_dust_hallowed_from_shard").end()
                .build());
        register(context, PRIMALITE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.PRIMALITE_INGOT.get()).parent(EXPERT_ALCHEMY)
                .stage().requiredItem(ItemRegistration.ESSENCE_DUST_EARTH.get()).requiredItem(ItemRegistration.ESSENCE_DUST_SEA.get()).requiredItem(ItemRegistration.ESSENCE_DUST_SKY.get())
                        .requiredItem(ItemRegistration.ESSENCE_DUST_SUN.get()).requiredItem(ItemRegistration.ESSENCE_DUST_MOON.get()).requiredTheories(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .recipe(ItemRegistration.PRIMALITE_INGOT.get()).recipe(ItemRegistration.PRIMALITE_NUGGET.get()).recipe("primalite_ingot_from_nuggets").recipe(ItemRegistration.PRIMALITE_BLOCK.get())
                        .recipe("primalite_ingots_from_block").recipe(ItemRegistration.PRIMALITE_SWORD.get()).recipe(ItemRegistration.PRIMALITE_TRIDENT.get())
                        .recipe(ItemRegistration.PRIMALITE_BOW.get()).recipe(ItemRegistration.PRIMALITE_SHOVEL.get()).recipe(ItemRegistration.PRIMALITE_PICKAXE.get()).recipe(ItemRegistration.PRIMALITE_AXE.get())
                        .recipe(ItemRegistration.PRIMALITE_HOE.get()).recipe(ItemRegistration.PRIMALITE_FISHING_ROD.get()).recipe(ItemRegistration.PRIMALITE_HEAD.get()).recipe(ItemRegistration.PRIMALITE_CHEST.get())
                        .recipe(ItemRegistration.PRIMALITE_LEGS.get()).recipe(ItemRegistration.PRIMALITE_FEET.get()).recipe(ItemRegistration.PRIMALITE_SHIELD.get()).end()
                .build());
        register(context, CRYSTAL_SYNTHESIS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.ESSENCE_CRYSTAL_EARTH.get())
                .parent(MASTER_ALCHEMY).parent(SHARD_SYNTHESIS)
                .stage().requiredTheories(2).end()
                .stage().attunement(Sources.EARTH, 2).attunement(Sources.SEA, 2).attunement(Sources.SKY, 2).attunement(Sources.SUN, 2).attunement(Sources.MOON, 2)
                        .recipe("essence_crystal_earth_from_shard").recipe("essence_crystal_sea_from_shard").recipe("essence_crystal_sky_from_shard").recipe("essence_crystal_sun_from_shard")
                        .recipe("essence_crystal_moon_from_shard").end()
                .addendum().requiredResearch(DISCOVER_BLOOD).attunement(Sources.BLOOD, 2).recipe("essence_crystal_blood_from_shard").end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 2).recipe("essence_crystal_infernal_from_shard").end()
                .addendum().requiredResearch(DISCOVER_VOID).attunement(Sources.VOID, 2).recipe("essence_crystal_void_from_shard").end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 2).recipe("essence_crystal_hallowed_from_shard").end()
                .build());
        register(context, CRYSTAL_DESYNTHESIS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.ESSENCE_CRYSTAL_EARTH.get()).parent(CRYSTAL_SYNTHESIS)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .recipe("essence_shard_earth_from_crystal").recipe("essence_shard_sea_from_crystal").recipe("essence_shard_sky_from_crystal").recipe("essence_shard_sun_from_crystal")
                        .recipe("essence_shard_moon_from_crystal").end()
                .addendum().requiredResearch(DISCOVER_BLOOD).attunement(Sources.BLOOD, 1).recipe("essence_shard_blood_from_crystal").end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 1).recipe("essence_shard_infernal_from_crystal").end()
                .addendum().requiredResearch(DISCOVER_VOID).attunement(Sources.VOID, 1).recipe("essence_shard_void_from_crystal").end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 1).recipe("essence_shard_hallowed_from_crystal").end()
                .build());
        register(context, HEXIUM, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.HEXIUM_INGOT.get())
                .parent(MASTER_ALCHEMY).parent(PRIMALITE).parent(SHARD_SYNTHESIS).parent(DISCOVER_BLOOD).parent(DISCOVER_INFERNAL).parent(DISCOVER_VOID)
                .stage().requiredItem(ItemRegistration.ESSENCE_SHARD_BLOOD.get()).requiredItem(ItemRegistration.ESSENCE_SHARD_INFERNAL.get())
                        .requiredItem(ItemRegistration.ESSENCE_SHARD_VOID.get()).requiredCraft(ItemRegistration.PRIMALITE_INGOT.get()).requiredTheories(2).end()
                .stage().attunement(Sources.BLOOD, 2).attunement(Sources.INFERNAL, 2).attunement(Sources.VOID, 2).sibling(SOTU_RESEARCH_HEXIUM)
                        .recipe(ItemRegistration.HEXIUM_INGOT.get()).recipe(ItemRegistration.HEXIUM_NUGGET.get()).recipe("hexium_ingot_from_nuggets")
                        .recipe(ItemRegistration.HEXIUM_BLOCK.get()).recipe("hexium_ingots_from_block")
                        .recipe(ItemRegistration.HEXIUM_SWORD.get()).recipe(ItemRegistration.HEXIUM_TRIDENT.get()).recipe(ItemRegistration.HEXIUM_BOW.get()).recipe(ItemRegistration.HEXIUM_SHOVEL.get())
                        .recipe(ItemRegistration.HEXIUM_PICKAXE.get()).recipe(ItemRegistration.HEXIUM_AXE.get()).recipe(ItemRegistration.HEXIUM_HOE.get()).recipe(ItemRegistration.HEXIUM_FISHING_ROD.get())
                        .recipe(ItemRegistration.HEXIUM_HEAD.get()).recipe(ItemRegistration.HEXIUM_CHEST.get()).recipe(ItemRegistration.HEXIUM_LEGS.get()).recipe(ItemRegistration.HEXIUM_FEET.get())
                        .recipe(ItemRegistration.HEXIUM_SHIELD.get()).end()
                .build());
        register(context, CLUSTER_SYNTHESIS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.ESSENCE_CLUSTER_EARTH.get())
                .parent(SUPREME_ALCHEMY).parent(CRYSTAL_SYNTHESIS)
                .stage().requiredTheories(3).end()
                .stage().attunement(Sources.EARTH, 3).attunement(Sources.SEA, 3).attunement(Sources.SKY, 3).attunement(Sources.SUN, 3).attunement(Sources.MOON, 3)
                        .recipe("essence_cluster_earth_from_crystal").recipe("essence_cluster_sea_from_crystal").recipe("essence_cluster_sky_from_crystal").recipe("essence_cluster_sun_from_crystal")
                        .recipe("essence_cluster_moon_from_crystal").end()
                .addendum().requiredResearch(DISCOVER_BLOOD).attunement(Sources.BLOOD, 3).recipe("essence_cluster_blood_from_crystal").end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 3).recipe("essence_cluster_infernal_from_crystal").end()
                .addendum().requiredResearch(DISCOVER_VOID).attunement(Sources.VOID, 3).recipe("essence_cluster_void_from_crystal").end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 3).recipe("essence_cluster_hallowed_from_crystal").end()
                .build());
        register(context, CLUSTER_DESYNTHESIS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.ESSENCE_CLUSTER_EARTH.get()).parent(CLUSTER_SYNTHESIS)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .recipe("essence_crystal_earth_from_cluster").recipe("essence_crystal_sea_from_cluster").recipe("essence_crystal_sky_from_cluster").recipe("essence_crystal_sun_from_cluster")
                        .recipe("essence_crystal_moon_from_cluster").end()
                .addendum().requiredResearch(DISCOVER_BLOOD).attunement(Sources.BLOOD, 1).recipe("essence_crystal_blood_from_cluster").end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 1).recipe("essence_crystal_infernal_from_cluster").end()
                .addendum().requiredResearch(DISCOVER_VOID).attunement(Sources.VOID, 1).recipe("essence_crystal_void_from_cluster").end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 1).recipe("essence_crystal_hallowed_from_cluster").end()
                .build());
        register(context, HALLOWSTEEL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.HALLOWSTEEL_INGOT.get())
                .parent(SUPREME_ALCHEMY).parent(HEXIUM).parent(CRYSTAL_SYNTHESIS).parent(DISCOVER_HALLOWED)
                .stage().requiredItem(ItemRegistration.ESSENCE_CRYSTAL_HALLOWED.get()).requiredCraft(ItemRegistration.HEXIUM_INGOT.get())
                        .requiredTheories(3).end()
                .stage().attunement(Sources.HALLOWED, 3).recipe(ItemRegistration.HALLOWSTEEL_INGOT.get()).recipe(ItemRegistration.HALLOWSTEEL_NUGGET.get())
                        .recipe("hallowsteel_ingot_from_nuggets").recipe(ItemRegistration.HALLOWSTEEL_BLOCK.get())
                        .recipe("hallowsteel_ingots_from_block").recipe(ItemRegistration.HALLOWSTEEL_SWORD.get())
                        .recipe(ItemRegistration.HALLOWSTEEL_TRIDENT.get()).recipe(ItemRegistration.HALLOWSTEEL_BOW.get()).recipe(ItemRegistration.HALLOWSTEEL_SHOVEL.get()).recipe(ItemRegistration.HALLOWSTEEL_PICKAXE.get())
                        .recipe(ItemRegistration.HALLOWSTEEL_AXE.get()).recipe(ItemRegistration.HALLOWSTEEL_HOE.get()).recipe(ItemRegistration.HALLOWSTEEL_FISHING_ROD.get()).recipe(ItemRegistration.HALLOWSTEEL_HEAD.get())
                        .recipe(ItemRegistration.HALLOWSTEEL_CHEST.get()).recipe(ItemRegistration.HALLOWSTEEL_LEGS.get()).recipe(ItemRegistration.HALLOWSTEEL_FEET.get()).recipe(ItemRegistration.HALLOWSTEEL_SHIELD.get()).end()
                .build());
        register(context, CALCINATOR_BASIC, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.CALCINATOR_BASIC.get()).parent(BASIC_ALCHEMY)
                .stage().requiredObservations(1).requiredCraft(ItemRegistration.ESSENCE_DUST_EARTH.get()).requiredCraft(ItemRegistration.ESSENCE_DUST_SEA.get())
                        .requiredCraft(ItemRegistration.ESSENCE_DUST_SKY.get()).requiredCraft(ItemRegistration.ESSENCE_DUST_SUN.get()).requiredCraft(ItemRegistration.ESSENCE_DUST_MOON.get()).end()
                .stage().recipe(ItemRegistration.CALCINATOR_BASIC.get()).end()
                .build());
        register(context, CALCINATOR_ENCHANTED, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.CALCINATOR_ENCHANTED.get())
                .parent(EXPERT_ALCHEMY).parent(EXPERT_MANAWEAVING).parent(SHARD_SYNTHESIS).parent(CALCINATOR_BASIC)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.CALCINATOR_ENCHANTED.get()).end()
                .build());
        register(context, CALCINATOR_FORBIDDEN, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.CALCINATOR_FORBIDDEN.get())
                .parent(MASTER_ALCHEMY).parent(MASTER_MANAWEAVING).parent(CRYSTAL_SYNTHESIS).parent(CALCINATOR_ENCHANTED) .parent(DISCOVER_BLOOD).parent(DISCOVER_INFERNAL).parent(DISCOVER_VOID)
                .stage().requiredTheories(2).end()
                .stage().recipe(ItemRegistration.CALCINATOR_FORBIDDEN.get()).end()
                .build());
        register(context, CALCINATOR_HEAVENLY, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.CALCINATOR_HEAVENLY.get())
                .parent(SUPREME_ALCHEMY).parent(SUPREME_MANAWEAVING).parent(CLUSTER_SYNTHESIS).parent(CALCINATOR_FORBIDDEN).parent(DISCOVER_HALLOWED)
                .stage().requiredTheories(3).end()
                .stage().recipe(ItemRegistration.CALCINATOR_HEAVENLY.get()).end()
                .build());
        register(context, CRYOTREATMENT, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(Items.ICE).parent(STONEMELDING)
                .stage().requiredItem(ItemRegistration.ESSENCE_DUST_SEA.get()).requiredObservations(1).end()
                .stage().attunement(Sources.SEA, 1).recipe("ice_from_cryotreatment").recipe("obsidian_from_cryotreatment").recipe("slime_ball_from_cryotreatment").end()
                .build());
        register(context, SANGUINE_CRUCIBLE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.SANGUINE_CRUCIBLE.get())
                .parent(MASTER_ALCHEMY).parent(HEXIUM).parent(CRYSTAL_SYNTHESIS).parent(SPELL_PAYLOAD_CONJURE_ANIMAL).parent(SPELL_PAYLOAD_DRAIN_SOUL)
                .stage().requiredTheories(2).end()
                .stage().attunement(Sources.BLOOD, 3).attunement(Sources.INFERNAL, 3).sibling(SOTU_RESEARCH_SANGUINE_CRUCIBLE).recipe(ItemRegistration.SANGUINE_CRUCIBLE.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_BLANK.get()).end()
                .build());
        register(context, SANGUINE_CORE_LAND_ANIMALS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.SANGUINE_CORE_BLANK.get()).parent(SANGUINE_CRUCIBLE)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.EARTH, 2).recipe(ItemRegistration.SANGUINE_CORE_ARMADILLO.get()).recipe(ItemRegistration.SANGUINE_CORE_CAT.get()).recipe(ItemRegistration.SANGUINE_CORE_CAMEL.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_CAVE_SPIDER.get()).recipe(ItemRegistration.SANGUINE_CORE_COW.get()) .recipe(ItemRegistration.SANGUINE_CORE_DONKEY.get()).recipe(ItemRegistration.SANGUINE_CORE_FOX.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_GOAT.get()).recipe(ItemRegistration.SANGUINE_CORE_HORSE.get()) .recipe(ItemRegistration.SANGUINE_CORE_LLAMA.get()).recipe(ItemRegistration.SANGUINE_CORE_OCELOT.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_PANDA.get()).recipe(ItemRegistration.SANGUINE_CORE_PIG.get()).recipe(ItemRegistration.SANGUINE_CORE_RABBIT.get()).recipe(ItemRegistration.SANGUINE_CORE_RAVAGER.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_SHEEP.get()).recipe(ItemRegistration.SANGUINE_CORE_SILVERFISH.get()).recipe(ItemRegistration.SANGUINE_CORE_SLIME.get()).recipe(ItemRegistration.SANGUINE_CORE_SNIFFER.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_SPIDER.get()).recipe(ItemRegistration.SANGUINE_CORE_WOLF.get()).end()
                .build());
        register(context, SANGUINE_CORE_SEA_CREATURES, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.SANGUINE_CORE_BLANK.get()).parent(SANGUINE_CRUCIBLE)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.SEA, 2).recipe(ItemRegistration.SANGUINE_CORE_AXOLOTL.get()).recipe(ItemRegistration.SANGUINE_CORE_COD.get()).recipe(ItemRegistration.SANGUINE_CORE_DOLPHIN.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_ELDER_GUARDIAN.get()).recipe(ItemRegistration.SANGUINE_CORE_GLOW_SQUID.get()).recipe(ItemRegistration.SANGUINE_CORE_GUARDIAN.get()).recipe(ItemRegistration.SANGUINE_CORE_POLAR_BEAR.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_PUFFERFISH.get()).recipe(ItemRegistration.SANGUINE_CORE_SALMON.get()).recipe(ItemRegistration.SANGUINE_CORE_SQUID.get()).recipe(ItemRegistration.SANGUINE_CORE_TROPICAL_FISH.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_TURTLE.get()).recipe(ItemRegistration.SANGUINE_CORE_FROG.get()).end()
                .build());
        register(context, SANGUINE_CORE_FLYING_CREATURES, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.SANGUINE_CORE_BLANK.get()).parent(SANGUINE_CRUCIBLE)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.SKY, 2).recipe(ItemRegistration.SANGUINE_CORE_BAT.get()).recipe(ItemRegistration.SANGUINE_CORE_BEE.get()).recipe(ItemRegistration.SANGUINE_CORE_BREEZE.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_CHICKEN.get()).recipe(ItemRegistration.SANGUINE_CORE_PARROT.get()).recipe(ItemRegistration.SANGUINE_CORE_VEX.get()).recipe(ItemRegistration.SANGUINE_CORE_ALLAY.get()).end()
                .build());
        register(context, SANGUINE_CORE_PLANTS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.SANGUINE_CORE_BLANK.get()).parent(SANGUINE_CRUCIBLE)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.SUN, 2).recipe(ItemRegistration.SANGUINE_CORE_CREEPER.get()).recipe(ItemRegistration.SANGUINE_CORE_MOOSHROOM.get()).recipe(ItemRegistration.SANGUINE_CORE_TREEFOLK.get()).end()
                .build());
        register(context, SANGUINE_CORE_UNDEAD, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.SANGUINE_CORE_BLANK.get()).parent(SANGUINE_CRUCIBLE)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.MOON, 2).recipe(ItemRegistration.SANGUINE_CORE_BOGGED.get()).recipe(ItemRegistration.SANGUINE_CORE_DROWNED.get()).recipe(ItemRegistration.SANGUINE_CORE_HUSK.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_PHANTOM.get()).recipe(ItemRegistration.SANGUINE_CORE_SKELETON.get()).recipe(ItemRegistration.SANGUINE_CORE_SKELETON_HORSE.get()).recipe(ItemRegistration.SANGUINE_CORE_STRAY.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_WITHER_SKELETON.get()).recipe(ItemRegistration.SANGUINE_CORE_ZOGLIN.get()).recipe(ItemRegistration.SANGUINE_CORE_ZOMBIE.get()).recipe(ItemRegistration.SANGUINE_CORE_ZOMBIE_HORSE.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_ZOMBIE_VILLAGER.get()).recipe(ItemRegistration.SANGUINE_CORE_ZOMBIFIED_PIGLIN.get()).end()
                .build());
        register(context, SANGUINE_CORE_SAPIENTS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.SANGUINE_CORE_BLANK.get()).parent(SANGUINE_CRUCIBLE)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.BLOOD, 2).recipe(ItemRegistration.SANGUINE_CORE_EVOKER.get()).recipe(ItemRegistration.SANGUINE_CORE_PILLAGER.get()).recipe(ItemRegistration.SANGUINE_CORE_VILLAGER.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_VINDICATOR.get()).recipe(ItemRegistration.SANGUINE_CORE_WITCH.get()).end()
                .build());
        register(context, SANGUINE_CORE_DEMONS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.SANGUINE_CORE_BLANK.get()).parent(SANGUINE_CRUCIBLE)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.INFERNAL, 2).recipe(ItemRegistration.SANGUINE_CORE_BLAZE.get()).recipe(ItemRegistration.SANGUINE_CORE_GHAST.get()).recipe(ItemRegistration.SANGUINE_CORE_HOGLIN.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_MAGMA_CUBE.get()).recipe(ItemRegistration.SANGUINE_CORE_PIGLIN.get()).recipe(ItemRegistration.SANGUINE_CORE_PIGLIN_BRUTE.get())
                        .recipe(ItemRegistration.SANGUINE_CORE_STRIDER.get()).end()
                .build());
        register(context, SANGUINE_CORE_ALIENS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.SANGUINE_CORE_BLANK.get()).parent(SANGUINE_CRUCIBLE)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.VOID, 2).recipe(ItemRegistration.SANGUINE_CORE_ENDERMAN.get()).recipe(ItemRegistration.SANGUINE_CORE_ENDERMITE.get()).recipe(ItemRegistration.SANGUINE_CORE_SHULKER.get()).end()
                .build());
        register(context, IGNYX, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.IGNYX.get())
                .parent(EXPERT_ALCHEMY).parent(CALCINATOR_BASIC).parent(STONEMELDING).parent(DISCOVER_INFERNAL)
                .stage().requiredTheories(1).requiredItem(ItemTags.COALS).end()
                .stage().attunement(Sources.INFERNAL, 2).recipe(ItemRegistration.IGNYX.get()).recipe(ItemRegistration.IGNYX_BLOCK.get()).recipe("ignyx_from_storage_block").recipe("torch_from_ignyx").end()
                .build());
        register(context, SYNTHETIC_GEM_BUDS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(Items.AMETHYST_CLUSTER)
                .parent(MASTER_ALCHEMY).parent(SHARD_SYNTHESIS).parent(STONEMELDING)
                .stage().requiredTheories(2).requiredItem(Tags.Items.GEMS_AMETHYST).end()
                .stage().attunement(SourceList.builder().withEarth(3).withSun(3).build()).recipe(ItemRegistration.ENERGIZED_AMETHYST.get()).recipe(ItemRegistration.DAMAGED_BUDDING_AMETHYST_BLOCK.get())
                        .recipe(ItemRegistration.CHIPPED_BUDDING_AMETHYST_BLOCK.get()).recipe(ItemRegistration.FLAWED_BUDDING_AMETHYST_BLOCK.get()).end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 3).recipe(ItemRegistration.ENERGIZED_DIAMOND.get())
                        .recipe(ItemRegistration.DAMAGED_BUDDING_DIAMOND_BLOCK.get()).recipe(ItemRegistration.CHIPPED_BUDDING_DIAMOND_BLOCK.get()).recipe(ItemRegistration.FLAWED_BUDDING_DIAMOND_BLOCK.get())
                        .recipe(ItemRegistration.ENERGIZED_EMERALD.get()).recipe(ItemRegistration.DAMAGED_BUDDING_EMERALD_BLOCK.get()).recipe(ItemRegistration.CHIPPED_BUDDING_EMERALD_BLOCK.get())
                        .recipe(ItemRegistration.FLAWED_BUDDING_EMERALD_BLOCK.get()).recipe(ItemRegistration.ENERGIZED_QUARTZ.get()).recipe(ItemRegistration.DAMAGED_BUDDING_QUARTZ_BLOCK.get())
                        .recipe(ItemRegistration.CHIPPED_BUDDING_QUARTZ_BLOCK.get()).recipe(ItemRegistration.FLAWED_BUDDING_QUARTZ_BLOCK.get()).end()
                .build());
    }
    
    private static void bootstrapSorceryEntries(BootstrapContext<ResearchEntry> context) {
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.SORCERY;
        register(context, BASIC_SORCERY, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_SORCERY).parent(UNLOCK_SORCERY)
                .stage().attunement(Sources.EARTH, 1).recipe(ItemRegistration.SPELL_SCROLL_BLANK.get()).recipe(ItemRegistration.SPELLCRAFTING_ALTAR.get()).end()
                .build());
        register(context, EXPERT_SORCERY, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_SORCERY).parent(SPELL_PAYLOAD_LIGHTNING).parent(SPELL_PAYLOAD_FROST)
                .stage().requiredExpertise(discipline, ResearchTier.EXPERT).end()
                .stage().end()
                .build());
        register(context, MASTER_SORCERY, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_SORCERY).parent(SPELL_VEHICLE_PROJECTILE).parent(SPELL_MOD_AMPLIFY)
                .stage().requiredResearch(DISCOVER_FORBIDDEN).requiredExpertise(discipline, ResearchTier.MASTER).requiredStat(StatsPM.SPELLS_CRAFTED_MAX_COST, 50).end()
                .stage().end()
                .build());
        register(context, SUPREME_SORCERY, key -> ResearchEntry.builder(key).discipline(discipline).icon(ICON_SORCERY).parent(SPELL_VEHICLE_BOLT).parent(SPELL_MOD_QUICKEN)
                .stage().requiredResearch(DISCOVER_HALLOWED).requiredExpertise(discipline, ResearchTier.SUPREME).requiredStat(StatsPM.SPELLS_CRAFTED_MAX_COST, 250).reveals(SECRETS_OF_THE_UNIVERSE).end()
                .stage().end()
                .build());
        register(context, COMPLETE_SORCERY, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ICON_SORCERY).finale(discipline)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .attunement(Sources.BLOOD, 1).attunement(Sources.INFERNAL, 1).attunement(Sources.VOID, 1).attunement(Sources.HALLOWED, 1).end()
                .build());
        register(context, WAND_INSCRIPTION, key -> ResearchEntry.builder(key).discipline(discipline).icon(ItemRegistration.WAND_INSCRIPTION_TABLE.get()).parent(BASIC_SORCERY).parent(ADVANCED_WANDMAKING)
                .stage().requiredStat(StatsPM.SPELLS_CRAFTED, 1).requiredObservations(1).end()
                .stage().recipe(ItemRegistration.WAND_INSCRIPTION_TABLE.get()).end()
                .build());
        register(context, SPELL_VEHICLE_PROJECTILE, key -> ResearchEntry.builder(key).discipline(discipline).parent(EXPERT_SORCERY)
                .stage().requiredTheories(1).end()
                .stage().end()
                .build());
        register(context, SPELL_VEHICLE_BOLT, key -> ResearchEntry.builder(key).discipline(discipline).parent(MASTER_SORCERY).parent(SPELL_VEHICLE_PROJECTILE)
                .stage().requiredTheories(2).end()
                .stage().end()
                .build());
        register(context, SPELL_PAYLOAD_FROST, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.SEA.getImage()).parent(BASIC_SORCERY)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.SEA, 1).end()
                .build());
        register(context, SPELL_PAYLOAD_LIGHTNING, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.SKY.getImage()).parent(BASIC_SORCERY)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.SKY, 1).end()
                .build());
        register(context, SPELL_PAYLOAD_SOLAR, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.SUN.getImage()).parent(SPELL_PAYLOAD_LIGHTNING)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.SUN, 1).end()
                .build());
        register(context, SPELL_PAYLOAD_LUNAR, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.MOON.getImage()).parent(SPELL_PAYLOAD_FROST)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.MOON, 1).end()
                .build());
        register(context, SPELL_PAYLOAD_BLOOD, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.BLOOD.getImage()).parent(EXPERT_SORCERY).parent(DISCOVER_BLOOD)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.BLOOD, 1).end()
                .build());
        register(context, SPELL_PAYLOAD_FLAME, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.INFERNAL.getImage()).parent(EXPERT_SORCERY).parent(DISCOVER_INFERNAL)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.INFERNAL, 1).end()
                .build());
        register(context, SPELL_PAYLOAD_VOID, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.VOID.getImage()).parent(EXPERT_SORCERY).parent(DISCOVER_VOID)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.VOID, 1).end()
                .build());
        register(context, SPELL_PAYLOAD_HOLY, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.HALLOWED.getImage()).parent(MASTER_SORCERY).parent(DISCOVER_HALLOWED)
                .stage().requiredTheories(2).end()
                .stage().attunement(Sources.HALLOWED, 1).end()
                .build());
        register(context, SPELL_PAYLOAD_BREAK, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.EARTH.getImage()).parent(EXPERT_SORCERY)
                .stage().requiredStat(StatsPM.BLOCKS_BROKEN_BAREHANDED, 50).requiredTheories(1).end()
                .stage().attunement(Sources.EARTH, 3).end()
                .build());
        register(context, SPELL_PAYLOAD_CONJURE_STONE, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.EARTH.getImage()).parent(EXPERT_SORCERY)
                .stage().requiredVanillaItemUsedStat(Items.COBBLESTONE, 100).requiredTheories(1).end()
                .stage().attunement(Sources.EARTH, 3).end()
                .build());
        register(context, SPELL_PAYLOAD_CONJURE_WATER, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.SEA.getImage()).parent(EXPERT_SORCERY).parent(SPELL_PAYLOAD_FROST)
                .stage().requiredResearch(DROWN_A_LITTLE).requiredTheories(1).end()
                .stage().attunement(Sources.SEA, 3).end()
                .build());
        register(context, SPELL_PAYLOAD_SHEAR, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.SKY.getImage()).parent(EXPERT_SORCERY)
                .parent(SPELL_PAYLOAD_LIGHTNING).parent(SPELL_PAYLOAD_BREAK)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.SKY, 3).end()
                .build());
        register(context, SPELL_PAYLOAD_FLIGHT, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.SKY.getImage()).parent(SUPREME_SORCERY).parent(SPELL_PAYLOAD_LIGHTNING)
                .stage().requiredVanillaCustomStat(Stats.AVIATE_ONE_CM, 100000, IconDefinition.of(Items.ELYTRA)).requiredTheories(3).end()
                .stage().attunement(Sources.SKY, 3).end()
                .build());
        register(context, SPELL_PAYLOAD_CONJURE_LIGHT, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.SUN.getImage()).parent(EXPERT_SORCERY).parent(SPELL_PAYLOAD_SOLAR)
                .stage().requiredVanillaItemUsedStat(Items.TORCH, 100).requiredTheories(1).end()
                .stage().attunement(Sources.SUN, 3).end()
                .build());
        register(context, SPELL_PAYLOAD_HEALING, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.SUN.getImage()).parent(EXPERT_SORCERY).parent(SPELL_PAYLOAD_SOLAR)
                .stage().requiredResearch(NEAR_DEATH_EXPERIENCE).requiredTheories(1).end()
                .stage().attunement(Sources.SUN, 3).end()
                .build());
        register(context, SPELL_PAYLOAD_POLYMORPH, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.MOON.getImage()).parent(EXPERT_SORCERY).parent(SPELL_PAYLOAD_LUNAR)
                .stage().requiredResearch(FURRY_FRIEND).requiredTheories(1).end()
                .stage().attunement(Sources.MOON, 3).end()
                .build());
        register(context, SPELL_PAYLOAD_POLYMORPH_SHEEP, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden().finaleExempt()).icon(Sources.MOON.getImage())
                .stage().end()
                .build());
        register(context, SPELL_PAYLOAD_CONJURE_ANIMAL, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.BLOOD.getImage()).parent(MASTER_SORCERY).parent(SPELL_PAYLOAD_BLOOD)
                .stage().requiredResearch(BREED_ANIMAL).requiredTheories(2).end()
                .stage().attunement(Sources.BLOOD, 3).end()
                .build());
        register(context, SPELL_PAYLOAD_CONJURE_LAVA, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.INFERNAL.getImage()).parent(MASTER_SORCERY)
                .parent(SPELL_PAYLOAD_CONJURE_WATER).parent(SPELL_PAYLOAD_FLAME)
                .stage().requiredResearch(FEEL_THE_BURN).requiredTheories(2).end()
                .stage().attunement(Sources.INFERNAL, 3).end()
                .build());
        register(context, SPELL_PAYLOAD_DRAIN_SOUL, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.INFERNAL.getImage()).parent(MASTER_SORCERY).parent(SPELL_PAYLOAD_FLAME)
                .stage().requiredItem(Items.SOUL_SAND).requiredItem(Items.SOUL_SOIL).requiredTheories(2).end()
                .stage().attunement(Sources.INFERNAL, 3).recipe(ItemRegistration.SOUL_GEM.get()).end()
                .build());
        register(context, SPELL_PAYLOAD_TELEPORT, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.VOID.getImage()).parent(MASTER_SORCERY).parent(SPELL_PAYLOAD_VOID)
                .stage().requiredStat(StatsPM.DISTANCE_TELEPORTED_CM, 10000).requiredTheories(2).end()
                .stage().attunement(Sources.VOID, 3).end()
                .build());
        register(context, SPELL_PAYLOAD_CONSECRATE, key -> ResearchEntry.builder(key).discipline(discipline).icon(Sources.HALLOWED.getImage()).parent(SUPREME_SORCERY).parent(SPELL_PAYLOAD_HOLY)
                .stage().requiredResearch(SCAN_NETHER_STAR).requiredTheories(3).end()
                .stage().attunement(Sources.HALLOWED, 3).end()
                .build());
        register(context, SPELL_MOD_AMPLIFY, key -> ResearchEntry.builder(key).discipline(discipline).parent(EXPERT_SORCERY)
                .stage().requiredTheories(1).end()
                .stage().end()
                .build());
        register(context, SPELL_MOD_MINE, key -> ResearchEntry.builder(key).discipline(discipline).parent(EXPERT_SORCERY)
                .stage().requiredTheories(1).end()
                .stage().end()
                .build());
        register(context, SPELL_MOD_QUICKEN, key -> ResearchEntry.builder(key).discipline(discipline).parent(MASTER_SORCERY).parent(SPELL_MOD_AMPLIFY)
                .stage().requiredTheories(2).end()
                .stage().end()
                .build());
        register(context, SPELL_MOD_BURST, key -> ResearchEntry.builder(key).discipline(discipline).parent(MASTER_SORCERY).parent(SPELL_MOD_MINE)
                .stage().requiredTheories(2).end()
                .stage().end()
                .build());
        register(context, SPELL_MOD_FORK, key -> ResearchEntry.builder(key).discipline(discipline).parent(SUPREME_SORCERY).parent(SPELL_MOD_QUICKEN).parent(SPELL_MOD_BURST)
                .stage().requiredTheories(3).end()
                .stage().end()
                .build());
    }
    
    private static void bootstrapRuneworkingEntries(BootstrapContext<ResearchEntry> context) {
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.RUNEWORKING;
        register(context, BASIC_RUNEWORKING, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ICON_RUNEWORKING).parent(UNLOCK_RUNEWORKING)
                .stage().recipe(ItemRegistration.RUNECARVING_TABLE.get()).recipe(ItemRegistration.RUNE_UNATTUNED.get()).recipe(ItemRegistration.RUNESCRIBING_ALTAR_BASIC.get()).end()
                .build());
        register(context, EXPERT_RUNEWORKING, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ICON_RUNEWORKING)
                .parent(RUNE_EARTH).parent(RUNE_PROJECT).parent(RUNE_ITEM)
                .stage().requiredExpertise(discipline, ResearchTier.EXPERT).end()
                .stage().recipe(ItemRegistration.RUNESCRIBING_ALTAR_ENCHANTED.get()).end()
                .build());
        register(context, MASTER_RUNEWORKING, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ICON_RUNEWORKING).parent(RUNE_ABSORB).parent(RUNE_CREATURE)
                .stage().requiredResearch(DISCOVER_FORBIDDEN).requiredExpertise(discipline, ResearchTier.MASTER).end()
                .stage().recipe(ItemRegistration.RUNESCRIBING_ALTAR_FORBIDDEN.get()).end()
                .build());
        register(context, SUPREME_RUNEWORKING, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ICON_RUNEWORKING).parent(RUNE_POWER)
                .stage().requiredResearch(DISCOVER_HALLOWED).requiredExpertise(discipline, ResearchTier.SUPREME).reveals(SECRETS_OF_THE_UNIVERSE).end()
                .stage().recipe(ItemRegistration.RUNESCRIBING_ALTAR_HEAVENLY.get()).end()
                .build());
        register(context, COMPLETE_RUNEWORKING, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ICON_RUNEWORKING).finale(discipline)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .attunement(Sources.BLOOD, 1).attunement(Sources.INFERNAL, 1).attunement(Sources.VOID, 1).attunement(Sources.HALLOWED, 1).end()
                .build());
        register(context, RUNE_EARTH, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.RUNE_EARTH.get()).parent(BASIC_RUNEWORKING)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).recipe(ItemRegistration.RUNE_EARTH.get()).end()
                .build());
        register(context, RUNE_SEA, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.RUNE_SEA.get()).parent(RUNE_EARTH)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.SEA, 1).recipe(ItemRegistration.RUNE_SEA.get()).end()
                .build());
        register(context, RUNE_SKY, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.RUNE_SKY.get()).parent(RUNE_EARTH)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.SKY, 1).recipe(ItemRegistration.RUNE_SKY.get()).end()
                .build());
        register(context, RUNE_SUN, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.RUNE_SUN.get()).parent(RUNE_SKY)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.SUN, 1).recipe(ItemRegistration.RUNE_SUN.get()).end()
                .build());
        register(context, RUNE_MOON, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.RUNE_MOON.get()).parent(RUNE_SEA)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.MOON, 1).recipe(ItemRegistration.RUNE_MOON.get()).end()
                .build());
        register(context, RUNE_PROJECT, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.RUNE_PROJECT.get()).parent(BASIC_RUNEWORKING)
                .stage().requiredObservations(1).end()
                .stage().recipe(ItemRegistration.RUNE_PROJECT.get()).end()
                .build());
        register(context, RUNE_PROTECT, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.RUNE_PROTECT.get()).parent(RUNE_PROJECT)
                .stage().requiredObservations(1).end()
                .stage().recipe(ItemRegistration.RUNE_PROTECT.get()).end()
                .build());
        register(context, RUNE_ITEM, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.RUNE_ITEM.get()).parent(BASIC_RUNEWORKING)
                .stage().requiredObservations(1).end()
                .stage().recipe(ItemRegistration.RUNE_ITEM.get()).end()
                .build());
        register(context, RUNE_SELF, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.RUNE_SELF.get()).parent(RUNE_ITEM)
                .stage().requiredObservations(1).end()
                .stage().recipe(ItemRegistration.RUNE_SELF.get()).end()
                .build());
        register(context, RUNE_BLOOD, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.RUNE_BLOOD.get()).parent(EXPERT_RUNEWORKING).parent(DISCOVER_BLOOD)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.BLOOD, 1).recipe(ItemRegistration.RUNE_BLOOD.get()).end()
                .build());
        register(context, RUNE_INFERNAL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.RUNE_INFERNAL.get())
                .parent(EXPERT_RUNEWORKING).parent(DISCOVER_INFERNAL)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.INFERNAL, 1).recipe(ItemRegistration.RUNE_INFERNAL.get()).end()
                .build());
        register(context, RUNE_VOID, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.RUNE_VOID.get()).parent(EXPERT_RUNEWORKING).parent(DISCOVER_VOID)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.VOID, 1).recipe(ItemRegistration.RUNE_VOID.get()).end()
                .build());
        register(context, RUNE_ABSORB, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.RUNE_ABSORB.get()).parent(EXPERT_RUNEWORKING)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.RUNE_ABSORB.get()).end()
                .build());
        register(context, RUNE_DISPEL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.RUNE_DISPEL.get()).parent(RUNE_ABSORB)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.RUNE_DISPEL.get()).end()
                .build());
        register(context, RUNE_SUMMON, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.RUNE_SUMMON.get()).parent(RUNE_ABSORB)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.RUNE_SUMMON.get()).end()
                .build());
        register(context, RUNE_AREA, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.RUNE_AREA.get()).parent(RUNE_CREATURE)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.RUNE_AREA.get()).end()
                .build());
        register(context, RUNE_CREATURE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.RUNE_CREATURE.get()).parent(EXPERT_RUNEWORKING)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.RUNE_CREATURE.get()).end()
                .build());
        register(context, RUNE_HALLOWED, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.RUNE_HALLOWED.get())
                .parent(MASTER_RUNEWORKING).parent(DISCOVER_HALLOWED)
                .stage().requiredTheories(2).end()
                .stage().recipe(ItemRegistration.RUNE_HALLOWED.get()).end()
                .build());
        register(context, RUNE_INSIGHT, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.RUNE_INSIGHT.get()).parent(EXPERT_RUNEWORKING).parent(SHARD_SYNTHESIS)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.RUNE_INSIGHT.get()).end()
                .build());
        register(context, RUNE_POWER, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.RUNE_POWER.get())
                .parent(MASTER_RUNEWORKING).parent(CRYSTAL_SYNTHESIS).parent(DISCOVER_BLOOD).parent(DISCOVER_INFERNAL).parent(DISCOVER_VOID).parent(RUNE_INSIGHT)
                .stage().requiredTheories(2).end()
                .stage().sibling(SOTU_RESEARCH_POWER_RUNE).recipe(ItemRegistration.RUNE_POWER.get()).end()
                .build());
        register(context, RUNE_GRACE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.RUNE_GRACE.get())
                .parent(SUPREME_RUNEWORKING).parent(CLUSTER_SYNTHESIS).parent(DISCOVER_HALLOWED).parent(RUNE_POWER)
                .stage().requiredTheories(3).end()
                .stage().recipe(ItemRegistration.RUNE_GRACE.get()).end()
                .build());
        register(context, RUNIC_GRINDSTONE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.RUNIC_GRINDSTONE.get()).parent(RUNE_DISPEL)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.RUNIC_GRINDSTONE.get()).end()
                .build());
        register(context, RECALL_STONE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.RECALL_STONE.get())
                .parent(EXPERT_RUNEWORKING).parent(RUNE_SUMMON).parent(RUNE_SELF)
                .stage().requiredTheories(1).requiredCraft(ItemTags.BEDS).end()
                .stage().recipe(ItemRegistration.RECALL_STONE.get()).end()
                .build());
        register(context, RUNIC_TRIM, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.RUNIC_ARMOR_TRIM_SMITHING_TEMPLATE.get())
                .parent(EXPERT_RUNEWORKING).parent(IMBUED_WOOL).parent(RUNE_EARTH).parent(RUNE_SEA).parent(RUNE_SKY).parent(RUNE_SUN).parent(RUNE_MOON)
                .stage().requiredTheories(1).requiredItem(ItemRegistration.RUNE_EARTH.get()).requiredItem(ItemRegistration.RUNE_SEA.get())
                        .requiredItem(ItemRegistration.RUNE_SKY.get()).requiredItem(ItemRegistration.RUNE_SUN.get()).requiredItem(ItemRegistration.RUNE_MOON.get()).end()
                .stage().recipe(ItemRegistration.RUNIC_ARMOR_TRIM_SMITHING_TEMPLATE.get()).end()
                .build());
        register(context, ENDERWARD, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.ENDERWARD.get())
                .parent(MASTER_RUNEWORKING).parent(RUNE_DISPEL).parent(DISCOVER_VOID)
                .stage().requiredTheories(2).end()
                .stage().attunement(Sources.VOID, 3).recipe(ItemRegistration.ENDERWARD.get()).end()
                .addendum().requiredResearch(RECALL_STONE).end()
                .addendum().requiredResearch(SPELL_PAYLOAD_TELEPORT).end()
                .build());
    }
    
    private static void bootstrapRitualEntries(BootstrapContext<ResearchEntry> context) {
        HolderGetter<Enchantment> enchGetter = context.lookup(Registries.ENCHANTMENT);
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.RITUAL;
        register(context, BASIC_RITUAL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ICON_RITUAL).parent(UNLOCK_RITUAL)
                .stage().recipe(ItemRegistration.RITUAL_ALTAR.get()).recipe(ItemRegistration.OFFERING_PEDESTAL.get()).end()
                .build());
        register(context, EXPERT_RITUAL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ICON_RITUAL).parent(RITUAL_CANDLES).parent(INCENSE_BRAZIER)
                .stage().requiredExpertise(discipline, ResearchTier.EXPERT).end()
                .stage().end()
                .build());
        register(context, MASTER_RITUAL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ICON_RITUAL).parent(RITUAL_LECTERN).parent(RITUAL_BELL)
                .stage().requiredResearch(DISCOVER_FORBIDDEN).requiredExpertise(discipline, ResearchTier.MASTER).end()
                .stage().end()
                .build());
        register(context, SUPREME_RITUAL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ICON_RITUAL).parent(BLOODLETTER).parent(SOUL_ANVIL)
                .stage().requiredResearch(DISCOVER_HALLOWED).requiredExpertise(discipline, ResearchTier.SUPREME).reveals(SECRETS_OF_THE_UNIVERSE).end()
                .stage().end()
                .build());
        register(context, COMPLETE_RITUAL, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ICON_RITUAL).finale(discipline)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .attunement(Sources.BLOOD, 1).attunement(Sources.INFERNAL, 1).attunement(Sources.VOID, 1).attunement(Sources.HALLOWED, 1).end()
                .build());
        register(context, MANAFRUIT, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.MANAFRUIT.get())
                .parent(BASIC_RITUAL).parent(MANA_SALTS).parent(RITUAL_CANDLES)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.MANAFRUIT.get()).end()
                .build());
        register(context, RITUAL_CANDLES, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.RITUAL_CANDLE_WHITE.get()).parent(BASIC_RITUAL)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.SUN, 1).recipe(ItemRegistration.TALLOW.get()).recipe("ritual_candle_white_from_tallow")
                        .recipe(ItemRegistration.RITUAL_CANDLE_BLACK.get()).recipe(ItemRegistration.RITUAL_CANDLE_BLUE.get()).recipe(ItemRegistration.RITUAL_CANDLE_BROWN.get()).recipe(ItemRegistration.RITUAL_CANDLE_CYAN.get())
                        .recipe(ItemRegistration.RITUAL_CANDLE_GRAY.get()).recipe(ItemRegistration.RITUAL_CANDLE_GREEN.get()).recipe(ItemRegistration.RITUAL_CANDLE_LIGHT_BLUE.get()).recipe(ItemRegistration.RITUAL_CANDLE_LIGHT_GRAY.get())
                        .recipe(ItemRegistration.RITUAL_CANDLE_LIME.get()).recipe(ItemRegistration.RITUAL_CANDLE_MAGENTA.get()).recipe(ItemRegistration.RITUAL_CANDLE_ORANGE.get()).recipe(ItemRegistration.RITUAL_CANDLE_PINK.get())
                        .recipe(ItemRegistration.RITUAL_CANDLE_PURPLE.get()).recipe(ItemRegistration.RITUAL_CANDLE_RED.get()).recipe(ItemRegistration.RITUAL_CANDLE_WHITE.get()).recipe(ItemRegistration.RITUAL_CANDLE_YELLOW.get())
                        .end()
                .addendum().requiredResearch(HONEY_EXTRACTOR).recipe("ritual_candle_white_from_beeswax").end()
                .build());
        register(context, INCENSE_BRAZIER, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.INCENSE_BRAZIER.get()).parent(BASIC_RITUAL)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.SKY, 1).recipe(ItemRegistration.INCENSE_BRAZIER.get()).recipe(ItemRegistration.INCENSE_STICK.get()).end()
                .build());
        register(context, RITUAL_LECTERN, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.RITUAL_LECTERN.get()).parent(EXPERT_RITUAL)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.MOON, 1).recipe(ItemRegistration.RITUAL_LECTERN.get()).end()
                .build());
        register(context, RITUAL_BELL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.RITUAL_BELL.get()).parent(EXPERT_RITUAL)
                .stage().requiredTheories(1).end()
                .stage().attunement(Sources.SEA, 1).recipe(ItemRegistration.RITUAL_BELL.get()).end()
                .build());
        register(context, BLOODLETTER, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.BLOODLETTER.get()).parent(MASTER_RITUAL).parent(DISCOVER_BLOOD)
                .stage().requiredTheories(2).end()
                .stage().attunement(Sources.BLOOD, 1).recipe(ItemRegistration.BLOODLETTER.get()).end()
                .build());
        register(context, SOUL_ANVIL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.SOUL_ANVIL.get())
                .parent(MASTER_RITUAL).parent(HEXIUM).parent(SPELL_PAYLOAD_DRAIN_SOUL)
                .stage().requiredTheories(2).end()
                .stage().attunement(Sources.INFERNAL, 1).recipe(ItemRegistration.SOUL_ANVIL.get()).end()
                .build());
        register(context, CELESTIAL_HARP, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.CELESTIAL_HARP.get())
                .parent(SUPREME_RITUAL).parent(DISCOVER_HALLOWED)
                .stage().requiredItem(Items.NOTE_BLOCK).requiredItem(Items.JUKEBOX).requiredTheories(3).end()
                .stage().attunement(Sources.HALLOWED, 1).recipe(ItemRegistration.CELESTIAL_HARP.get()).end()
                .build());
        register(context, WAND_CORE_PRIMAL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.PRIMAL_WAND_CORE_ITEM.get())
                .parent(EXPERT_RITUAL).parent(WAND_CORE_OBSIDIAN).parent(WAND_CORE_CORAL).parent(WAND_CORE_BAMBOO).parent(WAND_CORE_SUNWOOD).parent(WAND_CORE_MOONWOOD).parent(MANA_SALTS)
                .parent(RITUAL_CANDLES).parent(RITUAL_LECTERN).parent(RITUAL_BELL)
                .stage().requiredItem(ItemRegistration.HEARTWOOD_WAND_CORE_ITEM.get()).requiredTheories(1).end()
                .stage().attunement(Sources.EARTH, 3).attunement(Sources.SEA, 3).attunement(Sources.SKY, 3).attunement(Sources.SUN, 3).attunement(Sources.MOON, 3)
                        .recipe(ItemRegistration.PRIMAL_WAND_CORE_ITEM.get()).end()
                .addendum().requiredResearch(STAVES).recipe(ItemRegistration.PRIMAL_STAFF_CORE_ITEM.get()).end()
                .build());
        register(context, WAND_CORE_DARK_PRIMAL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.DARK_PRIMAL_WAND_CORE_ITEM.get())
                .parent(MASTER_RITUAL).parent(WAND_CORE_PRIMAL).parent(WAND_CORE_BONE).parent(WAND_CORE_BLAZE_ROD).parent(WAND_CORE_PURPUR).parent(BLOODLETTER).parent(SOUL_ANVIL)
                .stage().requiredItem(ItemRegistration.HEARTWOOD_WAND_CORE_ITEM.get()).requiredTheories(2).end()
                .stage().attunement(Sources.BLOOD, 4).attunement(Sources.INFERNAL, 4).attunement(Sources.VOID, 4).recipe(ItemRegistration.DARK_PRIMAL_WAND_CORE_ITEM.get()).end()
                .addendum().requiredResearch(STAVES).recipe(ItemRegistration.DARK_PRIMAL_STAFF_CORE_ITEM.get()).end()
                .build());
        register(context, WAND_CORE_PURE_PRIMAL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.PURE_PRIMAL_WAND_CORE_ITEM.get())
                .parent(SUPREME_RITUAL).parent(WAND_CORE_DARK_PRIMAL).parent(CELESTIAL_HARP)
                .stage().requiredItem(ItemRegistration.HEARTWOOD_WAND_CORE_ITEM.get()).requiredTheories(3).end()
                .stage().attunement(Sources.HALLOWED, 5).recipe(ItemRegistration.PURE_PRIMAL_WAND_CORE_ITEM.get()).end()
                .addendum().requiredResearch(STAVES).recipe(ItemRegistration.PURE_PRIMAL_STAFF_CORE_ITEM.get()).end()
                .build());
        register(context, PIXIES, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.BASIC_EARTH_PIXIE.get())
                .parent(EXPERT_RITUAL).parent(MANA_SALTS).parent(SHARD_SYNTHESIS).parent(RUNE_SUMMON).parent(RUNE_CREATURE).parent(INCENSE_BRAZIER).parent(RITUAL_BELL)
                .stage().requiredItem(ItemRegistration.RUNE_SUMMON.get()).requiredItem(ItemRegistration.RUNE_CREATURE.get()).requiredTheories(1).end()
                .stage().attunement(Sources.EARTH, 2).attunement(Sources.SEA, 2).attunement(Sources.SKY, 2).attunement(Sources.SUN, 2).attunement(Sources.MOON, 2)
                        .recipe(ItemRegistration.BASIC_EARTH_PIXIE.get()).recipe("pixie_basic_earth_revive")
                        .recipe(ItemRegistration.BASIC_SEA_PIXIE.get()).recipe("pixie_basic_sea_revive")
                        .recipe(ItemRegistration.BASIC_SKY_PIXIE.get()).recipe("pixie_basic_sky_revive")
                        .recipe(ItemRegistration.BASIC_SUN_PIXIE.get()).recipe("pixie_basic_sun_revive")
                        .recipe(ItemRegistration.BASIC_MOON_PIXIE.get()).recipe("pixie_basic_moon_revive").end()
                .addendum().requiredResearch(DISCOVER_BLOOD).attunement(Sources.BLOOD, 2).recipe(ItemRegistration.BASIC_BLOOD_PIXIE.get()).recipe("pixie_basic_blood_revive").end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 2).recipe(ItemRegistration.BASIC_INFERNAL_PIXIE.get()).recipe("pixie_basic_infernal_revive").end()
                .addendum().requiredResearch(DISCOVER_VOID).attunement(Sources.VOID, 2).recipe(ItemRegistration.BASIC_VOID_PIXIE.get()).recipe("pixie_basic_void_revive").end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 2).recipe(ItemRegistration.BASIC_HALLOWED_PIXIE.get()).recipe("pixie_basic_hallowed_revive").end()
                .build());
        register(context, GRAND_PIXIES, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.GRAND_EARTH_PIXIE.get())
                .parent(MASTER_RITUAL).parent(PIXIES).parent(CRYSTAL_SYNTHESIS).parent(RUNE_POWER).parent(SOUL_ANVIL)
                .stage().requiredItem(ItemRegistration.RUNE_INSIGHT.get()).requiredTheories(2).end()
                .stage().attunement(Sources.EARTH, 3).attunement(Sources.SEA, 3).attunement(Sources.SKY, 3).attunement(Sources.SUN, 3).attunement(Sources.MOON, 3)
                        .recipe(ItemRegistration.GRAND_EARTH_PIXIE.get()).recipe("pixie_grand_earth_revive")
                        .recipe(ItemRegistration.GRAND_SEA_PIXIE.get()).recipe("pixie_grand_sea_revive")
                        .recipe(ItemRegistration.GRAND_SKY_PIXIE.get()).recipe("pixie_grand_sky_revive")
                        .recipe(ItemRegistration.GRAND_SUN_PIXIE.get()).recipe("pixie_grand_sun_revive")
                        .recipe(ItemRegistration.GRAND_MOON_PIXIE.get()).recipe("pixie_grand_moon_revive").end()
                .addendum().requiredResearch(DISCOVER_BLOOD).attunement(Sources.BLOOD, 3).recipe(ItemRegistration.GRAND_BLOOD_PIXIE.get()).recipe("pixie_grand_blood_revive").end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 3).recipe(ItemRegistration.GRAND_INFERNAL_PIXIE.get()).recipe("pixie_grand_infernal_revive").end()
                .addendum().requiredResearch(DISCOVER_VOID).attunement(Sources.VOID, 3).recipe(ItemRegistration.GRAND_VOID_PIXIE.get()).recipe("pixie_grand_void_revive").end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 3).recipe(ItemRegistration.GRAND_HALLOWED_PIXIE.get()).recipe("pixie_grand_hallowed_revive").end()
                .build());
        register(context, MAJESTIC_PIXIES, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.MAJESTIC_EARTH_PIXIE.get())
                .parent(SUPREME_RITUAL).parent(GRAND_PIXIES).parent(CLUSTER_SYNTHESIS).parent(CELESTIAL_HARP)
                .stage().requiredItem(ItemRegistration.RUNE_POWER.get()).requiredTheories(3).end()
                .stage().attunement(Sources.EARTH, 4).attunement(Sources.SEA, 4).attunement(Sources.SKY, 4).attunement(Sources.SUN, 4).attunement(Sources.MOON, 4)
                        .recipe(ItemRegistration.MAJESTIC_EARTH_PIXIE.get()).recipe("pixie_majestic_earth_revive")
                        .recipe(ItemRegistration.MAJESTIC_SEA_PIXIE.get()).recipe("pixie_majestic_sea_revive")
                        .recipe(ItemRegistration.MAJESTIC_SKY_PIXIE.get()).recipe("pixie_majestic_sky_revive")
                        .recipe(ItemRegistration.MAJESTIC_SUN_PIXIE.get()).recipe("pixie_majestic_sun_revive")
                        .recipe(ItemRegistration.MAJESTIC_MOON_PIXIE.get()).recipe("pixie_majestic_moon_revive").end()
                .addendum().requiredResearch(DISCOVER_BLOOD).attunement(Sources.BLOOD, 4).recipe(ItemRegistration.MAJESTIC_BLOOD_PIXIE.get()).recipe("pixie_majestic_blood_revive").end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 4).recipe(ItemRegistration.MAJESTIC_INFERNAL_PIXIE.get()).recipe("pixie_majestic_infernal_revive").end()
                .addendum().requiredResearch(DISCOVER_VOID).attunement(Sources.VOID, 4).recipe(ItemRegistration.MAJESTIC_VOID_PIXIE.get()).recipe("pixie_majestic_void_revive").end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 4).recipe(ItemRegistration.MAJESTIC_HALLOWED_PIXIE.get()).recipe("pixie_majestic_hallowed_revive").end()
                .build());
        register(context, AMBROSIA, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.BASIC_EARTH_AMBROSIA.get())
                .parent(EXPERT_RITUAL).parent(ATTUNEMENTS).parent(MANAFRUIT).parent(SHARD_SYNTHESIS).parent(RUNE_ABSORB).parent(RUNE_SELF).parent(RITUAL_LECTERN)
                .stage().requiredItem(ItemRegistration.RUNE_ABSORB.get()).requiredItem(ItemRegistration.RUNE_SELF.get()).requiredTheories(1).end()
                .stage().attunement(Sources.EARTH, 2).attunement(Sources.SEA, 2).attunement(Sources.SKY, 2).attunement(Sources.SUN, 2).attunement(Sources.MOON, 2)
                        .recipe(ItemRegistration.BASIC_EARTH_AMBROSIA.get()).recipe(ItemRegistration.BASIC_SEA_AMBROSIA.get()).recipe(ItemRegistration.BASIC_SKY_AMBROSIA.get()).recipe(ItemRegistration.BASIC_SUN_AMBROSIA.get())
                        .recipe(ItemRegistration.BASIC_MOON_AMBROSIA.get()).end()
                .addendum().requiredResearch(DISCOVER_BLOOD).attunement(Sources.BLOOD, 2).recipe(ItemRegistration.BASIC_BLOOD_AMBROSIA.get()).end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 2).recipe(ItemRegistration.BASIC_INFERNAL_AMBROSIA.get()).end()
                .addendum().requiredResearch(DISCOVER_VOID).attunement(Sources.VOID, 2).recipe(ItemRegistration.BASIC_VOID_AMBROSIA.get()).end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 2).recipe(ItemRegistration.BASIC_HALLOWED_AMBROSIA.get()).end()
                .build());
        register(context, GREATER_AMBROSIA, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.GREATER_EARTH_AMBROSIA.get())
                .parent(MASTER_RITUAL).parent(AMBROSIA).parent(CRYSTAL_SYNTHESIS).parent(RUNE_POWER).parent(BLOODLETTER)
                .stage().requiredItem(ItemRegistration.RUNE_INSIGHT.get()).requiredTheories(2).end()
                .stage().attunement(Sources.EARTH, 3).attunement(Sources.SEA, 3).attunement(Sources.SKY, 3).attunement(Sources.SUN, 3).attunement(Sources.MOON, 3)
                        .recipe(ItemRegistration.GREATER_EARTH_AMBROSIA.get()).recipe(ItemRegistration.GREATER_SEA_AMBROSIA.get()).recipe(ItemRegistration.GREATER_SKY_AMBROSIA.get()).recipe(ItemRegistration.GREATER_SUN_AMBROSIA.get())
                        .recipe(ItemRegistration.GREATER_MOON_AMBROSIA.get()).end()
                .addendum().requiredResearch(DISCOVER_BLOOD).attunement(Sources.BLOOD, 3).recipe(ItemRegistration.GREATER_BLOOD_AMBROSIA.get()).end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 3).recipe(ItemRegistration.GREATER_INFERNAL_AMBROSIA.get()).end()
                .addendum().requiredResearch(DISCOVER_VOID).attunement(Sources.VOID, 3).recipe(ItemRegistration.GREATER_VOID_AMBROSIA.get()).end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 3).recipe(ItemRegistration.GREATER_HALLOWED_AMBROSIA.get()).end()
                .build());
        register(context, SUPREME_AMBROSIA, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.SUPREME_EARTH_AMBROSIA.get())
                .parent(SUPREME_RITUAL).parent(GREATER_AMBROSIA).parent(CLUSTER_SYNTHESIS).parent(CELESTIAL_HARP)
                .stage().requiredItem(ItemRegistration.RUNE_POWER.get()).requiredTheories(3).end()
                .stage().attunement(Sources.EARTH, 4).attunement(Sources.SEA, 4).attunement(Sources.SKY, 4).attunement(Sources.SUN, 4).attunement(Sources.MOON, 4)
                        .recipe(ItemRegistration.SUPREME_EARTH_AMBROSIA.get()).recipe(ItemRegistration.SUPREME_SEA_AMBROSIA.get()).recipe(ItemRegistration.SUPREME_SKY_AMBROSIA.get()).recipe(ItemRegistration.SUPREME_SUN_AMBROSIA.get())
                        .recipe(ItemRegistration.SUPREME_MOON_AMBROSIA.get()).end()
                .addendum().requiredResearch(DISCOVER_BLOOD).attunement(Sources.BLOOD, 4).recipe(ItemRegistration.SUPREME_BLOOD_AMBROSIA.get()).end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).attunement(Sources.INFERNAL, 4).recipe(ItemRegistration.SUPREME_INFERNAL_AMBROSIA.get()).end()
                .addendum().requiredResearch(DISCOVER_VOID).attunement(Sources.VOID, 4).recipe(ItemRegistration.SUPREME_VOID_AMBROSIA.get()).end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).attunement(Sources.HALLOWED, 4).recipe(ItemRegistration.SUPREME_HALLOWED_AMBROSIA.get()).end()
                .build());
        register(context, FLYING_CARPET, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.FLYING_CARPET.get())
                .parent(MASTER_RITUAL).parent(CRYSTAL_SYNTHESIS).parent(MANA_SALTS).parent(RUNE_PROJECT).parent(RUNE_ITEM).parent(RUNE_POWER).parent(INCENSE_BRAZIER).parent(RITUAL_LECTERN)
                .parent(RITUAL_BELL)
                .stage().requiredResearch(SCAN_FLYING_CREATURE).requiredTheories(2).end()
                .stage().attunement(Sources.SKY, 3).recipe(ItemRegistration.FLYING_CARPET.get()).end()
                .build());
        register(context, CLEANSING_RITE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.SANGUINE_CORE_BLANK.get())
                .parent(MASTER_RITUAL).parent(SANGUINE_CRUCIBLE).parent(RUNE_SUMMON).parent(RUNE_SELF).parent(RUNE_POWER).parent(RITUAL_CANDLES).parent(RITUAL_BELL).parent(RITUAL_LECTERN)
                .parent(BLOODLETTER).parent(SOUL_ANVIL)
                .stage().requiredTheories(2).end()
                .stage().recipe(ItemRegistration.SANGUINE_CORE_INNER_DEMON.get()).sibling(SOTU_RESEARCH_CLEANSING_RITE).end()
                .build());
        register(context, PRIMAL_SHOVEL, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.PRIMAL_SHOVEL.get())
                .parent(EXPERT_RITUAL).parent(PRIMALITE).parent(SHARD_SYNTHESIS).parent(MANA_SALTS).parent(RUNE_EARTH).parent(RITUAL_CANDLES).parent(RITUAL_BELL)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.PRIMAL_SHOVEL.get()).end()
                .addendum().requiredResearch(MASTER_RUNEWORKING).requiredResearch(RUNE_PROJECT).requiredResearch(RUNE_AREA).requiredResearch(RUNE_EARTH)
                        .siblingEnchantment(enchGetter.getOrThrow(EnchantmentsPM.REVERBERATION)).siblingResearch(UNLOCK_RUNE_ENCHANTMENTS).end()
                .build());
        register(context, PRIMAL_FISHING_ROD, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.PRIMAL_FISHING_ROD.get())
                .parent(EXPERT_RITUAL).parent(PRIMALITE).parent(SHARD_SYNTHESIS).parent(MANA_SALTS).parent(RUNE_SEA).parent(RITUAL_BELL).parent(RITUAL_LECTERN)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.PRIMAL_FISHING_ROD.get()).end()
                .addendum().requiredResearch(MASTER_RUNEWORKING).requiredResearch(RUNE_SUMMON).requiredResearch(RUNE_AREA).requiredResearch(RUNE_SEA)
                        .siblingEnchantment(enchGetter.getOrThrow(EnchantmentsPM.BOUNTY)).siblingResearch(UNLOCK_RUNE_ENCHANTMENTS).end()
                .build());
        register(context, PRIMAL_AXE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.PRIMAL_AXE.get())
                .parent(EXPERT_RITUAL).parent(PRIMALITE).parent(SHARD_SYNTHESIS).parent(MANA_SALTS).parent(RUNE_SKY).parent(RITUAL_BELL).parent(INCENSE_BRAZIER)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.PRIMAL_AXE.get()).end()
                .addendum().requiredResearch(MASTER_RUNEWORKING).requiredResearch(RUNE_PROJECT).requiredResearch(RUNE_AREA).requiredResearch(RUNE_SKY)
                        .siblingEnchantment(enchGetter.getOrThrow(EnchantmentsPM.DISINTEGRATION)).siblingResearch(UNLOCK_RUNE_ENCHANTMENTS).end()
                .build());
        register(context, PRIMAL_HOE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.PRIMAL_HOE.get())
                .parent(EXPERT_RITUAL).parent(PRIMALITE).parent(SHARD_SYNTHESIS).parent(MANA_SALTS).parent(RUNE_SUN).parent(RITUAL_CANDLES).parent(INCENSE_BRAZIER)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.PRIMAL_HOE.get()).end()
                .addendum().requiredResearch(MASTER_RUNEWORKING).requiredResearch(RUNE_SUMMON).requiredResearch(RUNE_CREATURE).requiredResearch(RUNE_SUN)
                        .siblingEnchantment(enchGetter.getOrThrow(EnchantmentsPM.VERDANT)).siblingResearch(UNLOCK_RUNE_ENCHANTMENTS).end()
                .build());
        register(context, PRIMAL_PICKAXE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.PRIMAL_PICKAXE.get())
                .parent(EXPERT_RITUAL).parent(PRIMALITE).parent(SHARD_SYNTHESIS).parent(MANA_SALTS).parent(RUNE_MOON).parent(RITUAL_LECTERN).parent(INCENSE_BRAZIER)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.PRIMAL_PICKAXE.get()).end()
                .addendum().requiredResearch(MASTER_RUNEWORKING).requiredResearch(RUNE_SUMMON).requiredResearch(RUNE_ITEM).requiredResearch(RUNE_MOON)
                        .siblingEnchantment(enchGetter.getOrThrow(EnchantmentsPM.LUCKY_STRIKE)).siblingResearch(UNLOCK_RUNE_ENCHANTMENTS).end()
                .build());
        register(context, FORBIDDEN_TRIDENT, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.FORBIDDEN_TRIDENT.get())
                .parent(DISCOVER_BLOOD).parent(MASTER_RITUAL).parent(HEXIUM).parent(SHARD_SYNTHESIS).parent(MANA_SALTS).parent(RUNE_BLOOD).parent(BLOODLETTER)
                .stage().requiredTheories(2).end()
                .stage().recipe(ItemRegistration.FORBIDDEN_TRIDENT.get()).end()
                .addendum().requiredResearch(MASTER_RUNEWORKING).requiredResearch(RUNE_PROJECT).requiredResearch(RUNE_CREATURE).requiredResearch(RUNE_BLOOD)
                        .siblingEnchantment(enchGetter.getOrThrow(EnchantmentsPM.RENDING)).siblingResearch(UNLOCK_RUNE_ENCHANTMENTS).end()
                .build());
        register(context, FORBIDDEN_BOW, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.FORBIDDEN_BOW.get())
                .parent(DISCOVER_INFERNAL).parent(MASTER_RITUAL).parent(HEXIUM).parent(SHARD_SYNTHESIS).parent(MANA_SALTS).parent(RUNE_INFERNAL).parent(SOUL_ANVIL)
                .stage().requiredTheories(2).end()
                .stage().recipe(ItemRegistration.FORBIDDEN_BOW.get()).end()
                .addendum().requiredResearch(MASTER_RUNEWORKING).requiredResearch(RUNE_ABSORB).requiredResearch(RUNE_CREATURE).requiredResearch(RUNE_INFERNAL)
                        .siblingEnchantment(enchGetter.getOrThrow(EnchantmentsPM.SOULPIERCING)).siblingResearch(UNLOCK_RUNE_ENCHANTMENTS).end()
                .build());
        register(context, FORBIDDEN_SWORD, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.FORBIDDEN_SWORD.get())
                .parent(DISCOVER_VOID).parent(MASTER_RITUAL).parent(HEXIUM).parent(SHARD_SYNTHESIS).parent(MANA_SALTS).parent(RUNE_VOID).parent(BLOODLETTER).parent(SOUL_ANVIL)
                .stage().requiredTheories(2).end()
                .stage().recipe(ItemRegistration.FORBIDDEN_SWORD.get()).end()
                .addendum().requiredResearch(MASTER_RUNEWORKING).requiredResearch(RUNE_SUMMON).requiredResearch(RUNE_ITEM).requiredResearch(RUNE_VOID)
                        .siblingEnchantment(enchGetter.getOrThrow(EnchantmentsPM.ESSENCE_THIEF)).siblingResearch(UNLOCK_RUNE_ENCHANTMENTS).end()
                .build());
        register(context, SACRED_SHIELD, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.SACRED_SHIELD.get())
                .parent(DISCOVER_HALLOWED).parent(SUPREME_RITUAL).parent(HALLOWSTEEL).parent(SHARD_SYNTHESIS).parent(MANA_SALTS).parent(RUNE_HALLOWED).parent(CELESTIAL_HARP)
                .stage().requiredTheories(3).end()
                .stage().recipe(ItemRegistration.SACRED_SHIELD.get()).end()
                .addendum().requiredResearch(MASTER_RUNEWORKING).requiredResearch(RUNE_PROTECT).requiredResearch(RUNE_SELF).requiredResearch(RUNE_HALLOWED)
                        .siblingEnchantment(enchGetter.getOrThrow(EnchantmentsPM.BULWARK)).siblingResearch(UNLOCK_RUNE_ENCHANTMENTS).end()
                .build());
        register(context, DREAM_VISION_TALISMAN, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.DREAM_VISION_TALISMAN.get())
                .parent(EXPERT_RITUAL).parent(RITUAL_CANDLES).parent(INCENSE_BRAZIER).parent(RITUAL_LECTERN)
                .stage().requiredTheories(1).requiredStat(StatsPM.OBSERVATIONS_MADE, 25).end()
                .stage().recipe(ItemRegistration.DREAM_VISION_TALISMAN.get()).end()
                .build());
        register(context, DOWSING_ROD, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.DOWSING_ROD.get()).parent(BASIC_RITUAL)
                .stage().requiredObservations(1).end()
                .stage().recipe(ItemRegistration.DOWSING_ROD.get()).end()
                .build());
        register(context, HYDROMELON, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.HYDROMELON_SLICE.get())
                .parent(EXPERT_RITUAL).parent(RITUAL_CANDLES).parent(RITUAL_BELL).parent(RUNE_SEA).parent(SHARD_SYNTHESIS)
                .stage().requiredItem(ItemRegistration.SUNWOOD_SAPLING.get()).requiredItem(ItemRegistration.MOONWOOD_SAPLING.get()).requiredItem(Items.MELON).requiredTheories(1).end()
                .stage().attunement(Sources.SEA, 2).recipe("hydromelon_seeds_from_ritual").recipe(ItemRegistration.HYDROMELON.get()).recipe(ItemRegistration.HYDROMELON_SEEDS.get()).end()
                .build());
        register(context, BLOOD_ROSE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.BLOOD_ROSE.get())
                .parent(DISCOVER_BLOOD).parent(EXPERT_RITUAL).parent(HYDROMELON).parent(BLOODLETTER).parent(RUNE_BLOOD)
                .stage().requiredItem(Items.ROSE_BUSH).requiredTheories(1).end()
                .stage().attunement(Sources.BLOOD, 2).recipe(ItemRegistration.BLOOD_ROSE.get()).end()
                .build());
        register(context, EMBERFLOWER, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.EMBERFLOWER.get())
                .parent(DISCOVER_INFERNAL).parent(EXPERT_RITUAL).parent(HYDROMELON).parent(BLOODLETTER).parent(RUNE_INFERNAL)
                .stage().requiredItem(Items.SUNFLOWER).requiredTheories(1).end()
                .stage().attunement(Sources.INFERNAL, 2).recipe(ItemRegistration.EMBERFLOWER.get()).recipe("blaze_powder_from_emberflower").end()
                .build());
    }
    
    private static void bootstrapMagitechEntries(BootstrapContext<ResearchEntry> context) {
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.MAGITECH;
        register(context, BASIC_MAGITECH, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ICON_MAGITECH).parent(UNLOCK_MAGITECH)
                .stage().recipe(ItemRegistration.MAGITECH_PARTS_BASIC.get()).end()
                .build());
        register(context, EXPERT_MAGITECH, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ICON_MAGITECH).parent(HONEY_EXTRACTOR).parent(SEASCRIBE_PEN)
                .stage().requiredExpertise(discipline, ResearchTier.EXPERT).requiredResearch(SCAN_PRIMALITE).end()
                .stage().recipe(ItemRegistration.MAGITECH_PARTS_ENCHANTED.get()).end()
                .build());
        register(context, MASTER_MAGITECH, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ICON_MAGITECH).parent(ARCANOMETER).parent(PRIMALITE_GOLEM)
                .stage().requiredResearch(DISCOVER_FORBIDDEN).requiredExpertise(discipline, ResearchTier.MASTER).requiredResearch(SCAN_HEXIUM).end()
                .stage().recipe(ItemRegistration.MAGITECH_PARTS_FORBIDDEN.get()).end()
                .build());
        register(context, SUPREME_MAGITECH, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ICON_MAGITECH).parent(HEXIUM_GOLEM)
                .stage().requiredResearch(DISCOVER_HALLOWED).requiredExpertise(discipline, ResearchTier.SUPREME).requiredResearch(SCAN_HALLOWSTEEL).reveals(SECRETS_OF_THE_UNIVERSE).end()
                .stage().recipe(ItemRegistration.MAGITECH_PARTS_HEAVENLY.get()).end()
                .build());
        register(context, COMPLETE_MAGITECH, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ICON_MAGITECH).finale(discipline)
                .stage().requiredObservations(1).end()
                .stage().attunement(Sources.EARTH, 1).attunement(Sources.SEA, 1).attunement(Sources.SKY, 1).attunement(Sources.SUN, 1).attunement(Sources.MOON, 1)
                        .attunement(Sources.BLOOD, 1).attunement(Sources.INFERNAL, 1).attunement(Sources.VOID, 1).attunement(Sources.HALLOWED, 1).end()
                .build());
        register(context, HONEY_EXTRACTOR, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.HONEY_EXTRACTOR.get()).parent(BASIC_MAGITECH)
                .stage().requiredItem(Items.HONEYCOMB).requiredItem(Items.HONEY_BOTTLE).requiredObservations(1).end()
                .stage().recipe(ItemRegistration.HONEY_EXTRACTOR.get()).end()
                .build());
        register(context, SEASCRIBE_PEN, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.BASIC).icon(ItemRegistration.SEASCRIBE_PEN.get()).parent(BASIC_MAGITECH).parent(THEORYCRAFTING)
                .stage().requiredItem(ItemRegistration.ENCHANTED_INK.get()).requiredStat(StatsPM.RESEARCH_PROJECTS_COMPLETED, 10).requiredObservations(1).end()
                .stage().recipe(ItemRegistration.SEASCRIBE_PEN.get()).end()
                .build());
        register(context, ARCANOMETER, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.ARCANOMETER.get()).parent(EXPERT_MAGITECH)
                .stage().requiredStat(StatsPM.ITEMS_ANALYZED, 25).requiredTheories(1).end()
                .stage().sibling(SOTU_RESEARCH_ARCANOMETER).recipe(ItemRegistration.ARCANOMETER.get()).end()
                .build());
        register(context, PRIMALITE_GOLEM, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.PRIMALITE_GOLEM_CONTROLLER.get())
                .parent(EXPERT_MAGITECH).parent(PRIMALITE)
                .stage().requiredResearch(SCAN_GOLEM).requiredTheories(1).end()
                .stage().recipe(ItemRegistration.PRIMALITE_GOLEM_CONTROLLER.get()).end()
                .build());
        register(context, HEXIUM_GOLEM, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.HEXIUM_GOLEM_CONTROLLER.get())
                .parent(MASTER_MAGITECH).parent(PRIMALITE_GOLEM).parent(HEXIUM)
                .stage().requiredTheories(2).end()
                .stage().recipe(ItemRegistration.HEXIUM_GOLEM_CONTROLLER.get()).end()
                .build());
        register(context, HALLOWSTEEL_GOLEM, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.HALLOWSTEEL_GOLEM_CONTROLLER.get())
                .parent(SUPREME_MAGITECH).parent(HEXIUM_GOLEM).parent(HALLOWSTEEL)
                .stage().requiredTheories(3).end()
                .stage().recipe(ItemRegistration.HALLOWSTEEL_GOLEM_CONTROLLER.get()).end()
                .build());
        register(context, CONCOCTING_TINCTURES, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.CONCOCTER.get())
                .parent(EXPERT_MAGITECH).parent(SKYGLASS).parent(DISCOVER_INFERNAL)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.SKYGLASS_FLASK.get()).recipe(ItemRegistration.CONCOCTER.get()).recipe("night_vision_tincture")
                        .recipe("long_night_vision_tincture").recipe("invisibility_tincture")
                        .recipe("long_invisibility_tincture").recipe("leaping_tincture")
                        .recipe("long_leaping_tincture").recipe("strong_leaping_tincture")
                        .recipe("swiftness_tincture").recipe("long_swiftness_tincture")
                        .recipe("strong_swiftness_tincture").recipe("turtle_master_tincture")
                        .recipe("long_turtle_master_tincture").recipe("strong_turtle_master_tincture")
                        .recipe("water_breathing_tincture").recipe("long_water_breathing_tincture")
                        .recipe("strength_tincture").recipe("long_strength_tincture")
                        .recipe("strong_strength_tincture").recipe("slow_falling_tincture")
                        .recipe("long_slow_falling_tincture").recipe("luck_tincture").end()
                .addendum().requiredResearch(DISCOVER_BLOOD).recipe("healing_tincture")
                        .recipe("strong_healing_tincture").recipe("regeneration_tincture")
                        .recipe("long_regeneration_tincture").recipe("strong_regeneration_tincture").end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).recipe("fire_resistance_tincture")
                        .recipe("long_fire_resistance_tincture").end()
                .build());
        register(context, CONCOCTING_PHILTERS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.CONCOCTER.get())
                .parent(MASTER_MAGITECH).parent(CONCOCTING_TINCTURES).parent(SHARD_SYNTHESIS)
                .stage().requiredTheories(2).end()
                .stage().recipe("night_vision_philter")
                        .recipe("long_night_vision_philter").recipe("invisibility_philter")
                        .recipe("long_invisibility_philter").recipe("leaping_philter")
                        .recipe("long_leaping_philter").recipe("strong_leaping_philter")
                        .recipe("swiftness_philter").recipe("long_swiftness_philter")
                        .recipe("strong_swiftness_philter").recipe("turtle_master_philter")
                        .recipe("long_turtle_master_philter").recipe("strong_turtle_master_philter")
                        .recipe("water_breathing_philter").recipe("long_water_breathing_philter")
                        .recipe("strength_philter").recipe("long_strength_philter")
                        .recipe("strong_strength_philter").recipe("slow_falling_philter")
                        .recipe("long_slow_falling_philter").recipe("luck_philter").end()
                .addendum().requiredResearch(DISCOVER_BLOOD).recipe("healing_philter")
                        .recipe("strong_healing_philter").recipe("regeneration_philter")
                        .recipe("long_regeneration_philter").recipe("strong_regeneration_philter").end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).recipe("fire_resistance_philter")
                        .recipe("long_fire_resistance_philter").end()
                .build());
        register(context, CONCOCTING_ELIXIRS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.CONCOCTER.get())
                .parent(SUPREME_MAGITECH).parent(CONCOCTING_PHILTERS).parent(CRYSTAL_SYNTHESIS)
                .stage().requiredTheories(3).end()
                .stage().recipe("night_vision_elixir")
                        .recipe("long_night_vision_elixir").recipe("invisibility_elixir")
                        .recipe("long_invisibility_elixir").recipe("leaping_elixir")
                        .recipe("long_leaping_elixir").recipe("strong_leaping_elixir")
                        .recipe("swiftness_elixir").recipe("long_swiftness_elixir")
                        .recipe("strong_swiftness_elixir").recipe("turtle_master_elixir")
                        .recipe("long_turtle_master_elixir").recipe("strong_turtle_master_elixir")
                        .recipe("water_breathing_elixir").recipe("long_water_breathing_elixir")
                        .recipe("strength_elixir").recipe("long_strength_elixir")
                        .recipe("strong_strength_elixir").recipe("slow_falling_elixir")
                        .recipe("long_slow_falling_elixir").recipe("luck_elixir").end()
                .addendum().requiredResearch(DISCOVER_BLOOD).recipe("healing_elixir")
                        .recipe("strong_healing_elixir").recipe("regeneration_elixir")
                        .recipe("long_regeneration_elixir").recipe("strong_regeneration_elixir").end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).recipe("fire_resistance_elixir")
                        .recipe("long_fire_resistance_elixir").end()
                .build());
        register(context, CONCOCTING_BOMBS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.CONCOCTER.get())
                .parent(MASTER_MAGITECH).parent(CONCOCTING_TINCTURES).parent(SHARD_SYNTHESIS)
                .stage().requiredTheories(2).end()
                .stage().recipe(ItemRegistration.BOMB_CASING.get()).recipe("night_vision_bomb")
                        .recipe("long_night_vision_bomb").recipe("invisibility_bomb")
                        .recipe("long_invisibility_bomb").recipe("leaping_bomb")
                        .recipe("long_leaping_bomb").recipe("strong_leaping_bomb")
                        .recipe("swiftness_bomb").recipe("long_swiftness_bomb")
                        .recipe("strong_swiftness_bomb").recipe("turtle_master_bomb")
                        .recipe("long_turtle_master_bomb").recipe("strong_turtle_master_bomb")
                        .recipe("water_breathing_bomb").recipe("long_water_breathing_bomb")
                        .recipe("strength_bomb").recipe("long_strength_bomb")
                        .recipe("strong_strength_bomb").recipe("slow_falling_bomb")
                        .recipe("long_slow_falling_bomb").recipe("luck_bomb").end()
                .addendum().requiredResearch(DISCOVER_BLOOD).recipe("healing_bomb")
                        .recipe("strong_healing_bomb").recipe("regeneration_bomb")
                        .recipe("long_regeneration_bomb").recipe("strong_regeneration_bomb")
                        .recipe("harming_bomb").recipe("strong_harming_bomb")
                        .recipe("poison_bomb").recipe("long_poison_bomb")
                        .recipe("strong_poison_bomb").end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).recipe("fire_resistance_bomb")
                        .recipe("long_fire_resistance_bomb").end()
                .addendum().requiredResearch(DISCOVER_VOID).recipe("slowness_bomb")
                        .recipe("long_slowness_bomb").recipe("strong_slowness_bomb")
                        .recipe("weakness_bomb").recipe("long_weakness_bomb").end()
                .build());
        register(context, ENTROPY_SINK, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.ENTROPY_SINK.get())
                .parent(EXPERT_MAGITECH).parent(EXPERT_MANAWEAVING).parent(MANAFRUIT)
                .stage().requiredTheories(1).requiredStat(StatsPM.RITUAL_MISHAPS, 1).end()
                .stage().recipe(ItemRegistration.ENTROPY_SINK.get()).end()
                .build());
        register(context, AUTO_CHARGER, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.AUTO_CHARGER.get())
                .parent(EXPERT_MAGITECH).parent(EXPERT_MANAWEAVING).parent(WAND_CHARGER).parent(ARTIFICIAL_MANA_FONTS)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.AUTO_CHARGER.get()).end()
                .build());
        register(context, ESSENCE_TRANSMUTER, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.ESSENCE_TRANSMUTER.get())
                .parent(EXPERT_MAGITECH).parent(EXPERT_MANAWEAVING)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.ESSENCE_TRANSMUTER.get()).end()
                .build());
        register(context, DISSOLUTION_CHAMBER, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.DISSOLUTION_CHAMBER.get())
                .parent(MASTER_MAGITECH).parent(MASTER_MANAWEAVING).parent(EARTHSHATTER_HAMMER)
                .stage().requiredItem(ItemTagsForgeExt.DUSTS_IRON, 20).requiredItem(ItemTagsForgeExt.DUSTS_COPPER, 20).requiredItem(ItemTagsForgeExt.DUSTS_GOLD, 10)
                        .requiredTheories(2).end()
                .stage().attunement(Sources.EARTH, 3).recipe(ItemRegistration.DISSOLUTION_CHAMBER.get()).recipe("iron_grit_from_dissolving_ore")
                        .recipe("iron_grit_from_dissolving_raw_metal").recipe("gold_grit_from_dissolving_ore")
                        .recipe("gold_grit_from_dissolving_raw_metal").recipe("copper_grit_from_dissolving_ore")
                        .recipe("copper_grit_from_dissolving_raw_metal").recipe("tin_dust_from_dissolving_ore")
                        .recipe("tin_dust_from_dissolving_raw_metal").recipe("lead_dust_from_dissolving_ore")
                        .recipe("lead_dust_from_dissolving_raw_metal").recipe("silver_dust_from_dissolving_ore")
                        .recipe("silver_dust_from_dissolving_raw_metal").recipe("uranium_dust_from_dissolving_ore")
                        .recipe("uranium_dust_from_dissolving_raw_metal").recipe("cobblestone_from_dissolving_surface_stone")
                        .recipe("cobbled_deepslate_from_dissolving_deep_stone").recipe("gravel_from_dissolving_cobblestone")
                        .recipe("sand_from_dissolving_gravel").recipe("bone_meal_from_dissolving_bone")
                        .recipe("blaze_powder_from_dissolving_blaze_rod").recipe("string_from_dissolving_wool")
                        .recipe("quartz_from_dissolving_quartz_block").recipe("glowstone_dust_from_dissolving_glowstone_block")
                        .recipe("rock_salt_from_dissolving_rock_salt_ore").recipe("refined_salt_from_dissolving_rock_salt")
                        .recipe("netherite_scrap_from_dissolving_ancient_debris").end()
                .build());
        register(context, ZEPHYR_ENGINE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.ZEPHYR_ENGINE.get())
                .parent(EXPERT_MAGITECH).parent(PRIMALITE).parent(SHARD_SYNTHESIS)
                .stage().requiredTheories(1).requiredItem(Items.PISTON).end()
                .stage().attunement(Sources.SKY, 2).recipe(ItemRegistration.ZEPHYR_ENGINE.get()).end()
                .build());
        register(context, VOID_TURBINE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.VOID_TURBINE.get())
                .parent(MASTER_MAGITECH).parent(HEXIUM).parent(SHARD_SYNTHESIS)
                .stage().requiredTheories(2).requiredItem(Items.STICKY_PISTON).end()
                .stage().attunement(Sources.VOID, 3).recipe(ItemRegistration.VOID_TURBINE.get()).end()
                .build());
        register(context, INFERNAL_FURNACE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.INFERNAL_FURNACE.get())
                .parent(MASTER_MAGITECH).parent(HEXIUM).parent(CRYSTAL_SYNTHESIS)
                .stage().requiredTheories(2).end()
                .stage().attunement(Sources.INFERNAL, 3).recipe(ItemRegistration.INFERNAL_FURNACE.get()).end()
                .addendum().requiredResearch(IGNYX).end()
                .build());
        register(context, MANA_NEXUS, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.MANA_NEXUS.get())
                .parent(MASTER_MAGITECH).parent(AUTO_CHARGER).parent(HEXIUM).parent(WAND_GEM_WIZARD)
                .stage().requiredTheories(2).end()
                .stage().recipe(ItemRegistration.MANA_NEXUS.get()).end()
                .build());
        register(context, MANA_SINGULARITY, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.MANA_SINGULARITY.get())
                .parent(SUPREME_MAGITECH).parent(MANA_NEXUS).parent(HALLOWSTEEL).parent(WAND_GEM_ARCHMAGE)
                .stage().requiredTheories(3).end()
                .stage().recipe(ItemRegistration.MANA_SINGULARITY.get()).end()
                .build());
        register(context, WARDING_MODULE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.EXPERT).icon(ItemRegistration.BASIC_WARDING_MODULE.get())
                .parent(EXPERT_MAGITECH).parent(RUNE_PROTECT).parent(RUNE_SELF).parent(RUNE_INSIGHT).parent(PRIMALITE).parent(WAND_CHARGER)
                .stage().requiredTheories(1).end()
                .stage().recipe(ItemRegistration.BASIC_WARDING_MODULE.get()).end()
                .build());
        register(context, GREATER_WARDING_MODULE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.MASTER).icon(ItemRegistration.GREATER_WARDING_MODULE.get())
                .parent(MASTER_MAGITECH).parent(RUNE_POWER).parent(WARDING_MODULE)
                .stage().requiredTheories(2).end()
                .stage().recipe(ItemRegistration.GREATER_WARDING_MODULE.get()).end()
                .build());
        register(context, SUPREME_WARDING_MODULE, key -> ResearchEntry.builder(key).discipline(discipline).tier(ResearchTier.SUPREME).icon(ItemRegistration.SUPREME_WARDING_MODULE.get())
                .parent(SUPREME_MAGITECH).parent(RUNE_GRACE).parent(GREATER_WARDING_MODULE)
                .stage().requiredTheories(3).end()
                .stage().recipe(ItemRegistration.SUPREME_WARDING_MODULE.get()).end()
                .build());
    }
    
    private static void bootstrapScanEntries(BootstrapContext<ResearchEntry> context) {
        ResourceKey<ResearchDiscipline> discipline = ResearchDisciplines.SCANS;
        register(context, RAW_MARBLE, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ItemRegistration.MARBLE_RAW.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemRegistration.MARBLE_SLAB.get()).recipe(ItemRegistration.MARBLE_STAIRS.get()).recipe(ItemRegistration.MARBLE_WALL.get()).recipe(ItemRegistration.MARBLE_BRICKS.get())
                        .recipe(ItemRegistration.MARBLE_BRICK_SLAB.get()).recipe(ItemRegistration.MARBLE_BRICK_STAIRS.get()).recipe(ItemRegistration.MARBLE_BRICK_WALL.get()).recipe(ItemRegistration.MARBLE_PILLAR.get())
                        .recipe(ItemRegistration.MARBLE_CHISELED.get()).recipe(ItemRegistration.MARBLE_TILES.get()).recipe(ItemRegistration.MARBLE_RUNED.get()).recipe(ItemRegistration.MARBLE_BOOKSHELF.get()).end()
                .build());
        register(context, HALLOWED_ORB, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ItemRegistration.HALLOWED_ORB.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemRegistration.HALLOWOOD_SAPLING.get()).end()
                .build());
        register(context, HALLOWOOD_TREES, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ItemRegistration.HALLOWOOD_SAPLING.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemRegistration.HALLOWOOD_WOOD.get()).recipe(ItemRegistration.STRIPPED_HALLOWOOD_WOOD.get()).recipe(ItemRegistration.HALLOWOOD_PLANKS.get())
                        .recipe(ItemRegistration.HALLOWOOD_SLAB.get()).recipe(ItemRegistration.HALLOWOOD_STAIRS.get()).recipe(ItemRegistration.HALLOWOOD_PILLAR.get()).end()
                .build());
        register(context, SUNWOOD_TREES, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ItemRegistration.SUNWOOD_SAPLING.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemRegistration.SUNWOOD_WOOD.get()).recipe(ItemRegistration.STRIPPED_SUNWOOD_WOOD.get()).recipe(ItemRegistration.SUNWOOD_PLANKS.get())
                        .recipe(ItemRegistration.SUNWOOD_SLAB.get()).recipe(ItemRegistration.SUNWOOD_STAIRS.get()).recipe(ItemRegistration.SUNWOOD_PILLAR.get()).end()
                .build());
        register(context, MOONWOOD_TREES, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ItemRegistration.MOONWOOD_SAPLING.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemRegistration.MOONWOOD_WOOD.get()).recipe(ItemRegistration.STRIPPED_MOONWOOD_WOOD.get()).recipe(ItemRegistration.MOONWOOD_PLANKS.get())
                        .recipe(ItemRegistration.MOONWOOD_SLAB.get()).recipe(ItemRegistration.MOONWOOD_STAIRS.get()).recipe(ItemRegistration.MOONWOOD_PILLAR.get()).end()
                .build());
        register(context, ROCK_SALT, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ItemRegistration.REFINED_SALT.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemRegistration.REFINED_SALT.get()).recipe("rock_salt_from_smelting")
                        .recipe(ItemRegistration.SALT_BLOCK.get()).recipe("refined_salt_from_salt_block")
                        .recipe(ItemRegistration.SALTED_BAKED_POTATO.get()).recipe(ItemRegistration.SALTED_BEETROOT_SOUP.get()).recipe(ItemRegistration.SALTED_COOKED_BEEF.get())
                        .recipe(ItemRegistration.SALTED_COOKED_CHICKEN.get()).recipe(ItemRegistration.SALTED_COOKED_COD.get()).recipe(ItemRegistration.SALTED_COOKED_MUTTON.get()).recipe(ItemRegistration.SALTED_COOKED_PORKCHOP.get())
                        .recipe(ItemRegistration.SALTED_COOKED_RABBIT.get()).recipe(ItemRegistration.SALTED_COOKED_SALMON.get()).recipe(ItemRegistration.SALTED_MUSHROOM_STEW.get()).recipe(ItemRegistration.SALTED_RABBIT_STEW.get()).end()
                .build());
        register(context, ALCHEMICAL_WASTE, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ItemRegistration.ALCHEMICAL_WASTE.get()).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
        register(context, QUARTZ, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(Items.QUARTZ).parent(UNLOCK_SCANS)
                .stage().recipe(ItemRegistration.QUARTZ_NUGGET.get()).recipe("quartz_from_nuggets").recipe("quartz_from_smelting").end()
                .build());
        register(context, INNER_DEMON, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ItemRegistration.SANGUINE_CORE_BLANK.get()).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
        register(context, BOOKSHELF, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(Items.BOOKSHELF).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
        register(context, BEEHIVE, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(Items.BEEHIVE).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
        register(context, BEACON, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(Items.BEACON).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
        register(context, DRAGON_EGG, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(Items.DRAGON_EGG).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
        register(context, DRAGON_HEAD, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(Items.DRAGON_HEAD).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
        register(context, MYSTICAL_RELIC, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ItemRegistration.MYSTICAL_RELIC.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemRegistration.MYSTICAL_RELIC.get()).end()
                .build());
        register(context, HUMMING_ARTIFACT, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ItemRegistration.HUMMING_ARTIFACT_UNATTUNED.get()).parent(UNLOCK_SCANS)
                .stage().recipe(ItemRegistration.HUMMING_ARTIFACT_EARTH.get()).recipe(ItemRegistration.HUMMING_ARTIFACT_SEA.get()).recipe(ItemRegistration.HUMMING_ARTIFACT_SKY.get())
                        .recipe(ItemRegistration.HUMMING_ARTIFACT_SUN.get()).recipe(ItemRegistration.HUMMING_ARTIFACT_MOON.get()).end()
                .addendum().requiredResearch(DISCOVER_BLOOD).recipe(ItemRegistration.HUMMING_ARTIFACT_BLOOD.get()).end()
                .addendum().requiredResearch(DISCOVER_INFERNAL).recipe(ItemRegistration.HUMMING_ARTIFACT_INFERNAL.get()).end()
                .addendum().requiredResearch(DISCOVER_VOID).recipe(ItemRegistration.HUMMING_ARTIFACT_VOID.get()).end()
                .addendum().requiredResearch(DISCOVER_HALLOWED).recipe(ItemRegistration.HUMMING_ARTIFACT_HALLOWED.get()).end()
                .build());
        register(context, TREEFOLK, key -> ResearchEntry.builder(key).discipline(discipline).flags(ResearchEntry.Flags.builder().hidden()).icon(ItemRegistration.HEARTWOOD.get()).parent(UNLOCK_SCANS)
                .stage().end()
                .build());
    }
    
    private static void bootstrapInternalEntries(BootstrapContext<ResearchEntry> context) {
        register(context, UNLOCK_SCANS, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).build());
        register(context, UNLOCK_RUNE_ENCHANTMENTS, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).build());
        register(context, DISCOVER_BLOOD, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_TUBE)
                .stage().sibling(DISCOVER_FORBIDDEN).sibling(SOTU_DISCOVER_BLOOD).end()
                .build());
        register(context, DISCOVER_INFERNAL, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_TUBE)
                .stage().sibling(DISCOVER_FORBIDDEN).sibling(SOTU_DISCOVER_INFERNAL).end()
                .build());
        register(context, DISCOVER_VOID, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_TUBE)
                .stage().sibling(DISCOVER_FORBIDDEN).sibling(SOTU_DISCOVER_VOID).end()
                .build());
        register(context, DISCOVER_FORBIDDEN, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_UNKNOWN).build());
        register(context, DISCOVER_HALLOWED, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_UNKNOWN).build());
        register(context, ENV_EARTH, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_MAP).build());
        register(context, ENV_SEA, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_MAP).build());
        register(context, ENV_SKY, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_MAP).build());
        register(context, ENV_SUN, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_MAP).build());
        register(context, ENV_MOON, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_MAP).build());
        register(context, SOTU_DISCOVER_BLOOD, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_MAP).build());
        register(context, SOTU_DISCOVER_INFERNAL, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_MAP).build());
        register(context, SOTU_DISCOVER_VOID, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_MAP).build());
        register(context, SOTU_RESEARCH_ARCANOMETER, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_TUBE).build());
        register(context, SOTU_RESEARCH_HEXIUM, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_TUBE).build());
        register(context, SOTU_RESEARCH_POWER_RUNE, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_TUBE).build());
        register(context, SOTU_RESEARCH_SANGUINE_CRUCIBLE, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_TUBE).build());
        register(context, SOTU_RESEARCH_CLEANSING_RITE, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_TUBE).build());
        register(context, SOTU_SCAN_HALLOWED_ORB, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_BAG).build());
        register(context, SCAN_PRIMALITE, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_BAG).build());
        register(context, SCAN_HEXIUM, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_BAG).build());
        register(context, SCAN_HALLOWSTEEL, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_BAG).build());
        register(context, DROWN_A_LITTLE, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_MAP).build());
        register(context, NEAR_DEATH_EXPERIENCE, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_MAP).build());
        register(context, FURRY_FRIEND, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_MAP).build());
        register(context, BREED_ANIMAL, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_MAP).build());
        register(context, FEEL_THE_BURN, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_MAP).build());
        register(context, SCAN_NETHER_STAR, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal().hasHint()).icon(ICON_BAG).build());
        register(context, SCAN_FLYING_CREATURE, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_MAP).build());
        register(context, SCAN_GOLEM, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_MAP).build());
        register(context, WAND_TRANSFORM_HINT, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_MAP).build());
        register(context, FOUND_SHRINE, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_MAP).build());
        register(context, GOT_DREAM, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_MAP).build());
        register(context, SIPHON_PROMPT, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_MAP).build());
        register(context, UNKNOWN_RUNE, key -> ResearchEntry.builder(key).flags(ResearchEntry.Flags.builder().internal()).icon(ICON_UNKNOWN).build());
    }
    
    private static Holder.Reference<ResearchEntry> register(BootstrapContext<ResearchEntry> context, ResourceKey<ResearchEntry> key, Function<ResourceKey<ResearchEntry>, ResearchEntry> supplier) {
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
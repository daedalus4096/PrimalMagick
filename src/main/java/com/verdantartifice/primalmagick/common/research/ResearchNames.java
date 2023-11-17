package com.verdantartifice.primalmagick.common.research;

import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod research names.
 * 
 * @author Daedalus4096
 */
public class ResearchNames {
    private static final DeferredRegister<ResearchName> DEFERRED_NAMES = DeferredRegister.create(RegistryKeysPM.RESEARCH_NAMES, PrimalMagick.MODID);
    
    public static final Supplier<IForgeRegistry<ResearchName>> NAMES = DEFERRED_NAMES.makeRegistry(() -> new RegistryBuilder<ResearchName>().hasTags().onValidate(ResearchNames::validate));
    
    public static void init() {
        DEFERRED_NAMES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    protected static RegistryObject<ResearchName> register(String id, Supplier<ResearchName> nameSupplier) {
        return DEFERRED_NAMES.register(id, nameSupplier);
    }
    
    @Nonnull
    public static Optional<ResearchName> find(String name) {
        return NAMES.get().getValues().stream().filter(rn -> rn.matches(name)).findFirst();
    }
    
    @Nonnull
    public static Supplier<SimpleResearchKey> simpleKey(Supplier<ResearchName> nameSupplier) {
        return () -> nameSupplier.get().simpleKey();
    }
    
    @Nonnull
    public static Supplier<SimpleResearchKey> simpleKey(Supplier<ResearchName> nameSupplier, int stage) {
        return () -> nameSupplier.get().simpleKey(stage);
    }
    
    private static void validate(IForgeRegistryInternal<ResearchName> owner, RegistryManager stage, int id, ResourceLocation key, ResearchName obj) {
        // Validate that each registry object is the only one with its given root name
        if (NAMES.get().getValues().stream().filter(rn -> rn.matches(obj.rootName())).count() > 1L) {
            throw new IllegalStateException("Research name " + key.toString() + " has a duplicate root name!");
        }
    }
    
    // Register Fundamentals research
    public static final RegistryObject<ResearchName> FIRST_STEPS = register("first_steps", () -> new ResearchName("FIRST_STEPS"));
    public static final RegistryObject<ResearchName> THEORYCRAFTING = register("theorycrafting", () -> new ResearchName("THEORYCRAFTING"));
    public static final RegistryObject<ResearchName> ATTUNEMENTS = register("attunements", () -> new ResearchName("ATTUNEMENTS"));
    public static final RegistryObject<ResearchName> UNLOCK_MANAWEAVING = register("unlock_manaweaving", () -> new ResearchName("UNLOCK_MANAWEAVING"));
    public static final RegistryObject<ResearchName> UNLOCK_ALCHEMY = register("unlock_alchemy", () -> new ResearchName("UNLOCK_ALCHEMY"));
    public static final RegistryObject<ResearchName> UNLOCK_SORCERY = register("unlock_sorcery", () -> new ResearchName("UNLOCK_SORCERY"));
    public static final RegistryObject<ResearchName> UNLOCK_RUNEWORKING = register("unlock_runeworking", () -> new ResearchName("UNLOCK_RUNEWORKING"));
    public static final RegistryObject<ResearchName> UNLOCK_RITUAL = register("unlock_ritual", () -> new ResearchName("UNLOCK_RITUAL"));
    public static final RegistryObject<ResearchName> UNLOCK_MAGITECH = register("unlock_magitech", () -> new ResearchName("UNLOCK_MAGITECH"));
    public static final RegistryObject<ResearchName> UNLOCK_SCANS = register("unlock_scans", () -> new ResearchName("UNLOCK_SCANS"));
    public static final RegistryObject<ResearchName> UNLOCK_RUNE_ENCHANTMENTS = register("unlock_rune_enchantments", () -> new ResearchName("UNLOCK_RUNE_ENCHANTMENTS"));
    public static final RegistryObject<ResearchName> TERRESTRIAL_MAGICK = register("terrestrial_magick", () -> new ResearchName("TERRESTRIAL_MAGICK"));
    public static final RegistryObject<ResearchName> FORBIDDEN_MAGICK = register("forbidden_magick", () -> new ResearchName("FORBIDDEN_MAGICK"));
    public static final RegistryObject<ResearchName> HEAVENLY_MAGICK = register("heavenly_magick", () -> new ResearchName("HEAVENLY_MAGICK"));
    public static final RegistryObject<ResearchName> SOURCE_EARTH = register("source_earth", () -> new ResearchName("SOURCE_EARTH"));
    public static final RegistryObject<ResearchName> SOURCE_SEA = register("source_sea", () -> new ResearchName("SOURCE_SEA"));
    public static final RegistryObject<ResearchName> SOURCE_SKY = register("source_sky", () -> new ResearchName("SOURCE_SKY"));
    public static final RegistryObject<ResearchName> SOURCE_SUN = register("source_sun", () -> new ResearchName("SOURCE_SUN"));
    public static final RegistryObject<ResearchName> SOURCE_MOON = register("source_moon", () -> new ResearchName("SOURCE_MOON"));
    public static final RegistryObject<ResearchName> SOURCE_BLOOD = register("source_blood", () -> new ResearchName("SOURCE_BLOOD"));
    public static final RegistryObject<ResearchName> SOURCE_INFERNAL = register("source_infernal", () -> new ResearchName("SOURCE_INFERNAL"));
    public static final RegistryObject<ResearchName> SOURCE_VOID = register("source_void", () -> new ResearchName("SOURCE_VOID"));
    public static final RegistryObject<ResearchName> SOURCE_HALLOWED = register("source_hallowed", () -> new ResearchName("SOURCE_HALLOWED"));
    public static final RegistryObject<ResearchName> SECRETS_OF_THE_UNIVERSE = register("secrets_of_the_universe", () -> new ResearchName("SECRETS_OF_THE_UNIVERSE"));
    public static final RegistryObject<ResearchName> COMPLETE_BASICS = register("complete_basics", () -> new ResearchName("COMPLETE_BASICS"));
    public static final RegistryObject<ResearchName> THEORY_OF_EVERYTHING = register("theory_of_everything", () -> new ResearchName("THEORY_OF_EVERYTHING"));
    
    // Register Manaweaving research
    public static final RegistryObject<ResearchName> BASIC_MANAWEAVING = register("basic_manaweaving", () -> new ResearchName("BASIC_MANAWEAVING"));
    public static final RegistryObject<ResearchName> EXPERT_MANAWEAVING = register("expert_manaweaving", () -> new ResearchName("EXPERT_MANAWEAVING"));
    public static final RegistryObject<ResearchName> MASTER_MANAWEAVING = register("master_manaweaving", () -> new ResearchName("MASTER_MANAWEAVING"));
    public static final RegistryObject<ResearchName> SUPREME_MANAWEAVING = register("supreme_manaweaving", () -> new ResearchName("SUPREME_MANAWEAVING"));
    public static final RegistryObject<ResearchName> COMPLETE_MANAWEAVING = register("complete_manaweaving", () -> new ResearchName("COMPLETE_MANAWEAVING"));
    public static final RegistryObject<ResearchName> WAND_CHARGER = register("wand_charger", () -> new ResearchName("WAND_CHARGER"));
    public static final RegistryObject<ResearchName> MANA_SALTS = register("mana_salts", () -> new ResearchName("MANA_SALTS"));
    public static final RegistryObject<ResearchName> ADVANCED_WANDMAKING = register("advanced_wandmaking", () -> new ResearchName("ADVANCED_WANDMAKING"));
    public static final RegistryObject<ResearchName> STAVES = register("staves", () -> new ResearchName("STAVES"));
    public static final RegistryObject<ResearchName> WAND_CORE_HEARTWOOD = register("wand_core_heartwood", () -> new ResearchName("WAND_CORE_HEARTWOOD"));
    public static final RegistryObject<ResearchName> WAND_CAP_IRON = register("wand_cap_iron", () -> new ResearchName("WAND_CAP_IRON"));
    public static final RegistryObject<ResearchName> WAND_GEM_APPRENTICE = register("wand_gem_apprentice", () -> new ResearchName("WAND_GEM_APPRENTICE"));
    public static final RegistryObject<ResearchName> EARTHSHATTER_HAMMER = register("earthshatter_hammer", () -> new ResearchName("EARTHSHATTER_HAMMER"));
    public static final RegistryObject<ResearchName> SUNLAMP = register("sunlamp", () -> new ResearchName("SUNLAMP"));
    public static final RegistryObject<ResearchName> WAND_GEM_ADEPT = register("wand_gem_adept", () -> new ResearchName("WAND_GEM_ADEPT"));
    public static final RegistryObject<ResearchName> WAND_GEM_WIZARD = register("wand_gem_wizard", () -> new ResearchName("WAND_GEM_WIZARD"));
    public static final RegistryObject<ResearchName> WAND_GEM_ARCHMAGE = register("wand_gem_archmage", () -> new ResearchName("WAND_GEM_ARCHMAGE"));
    public static final RegistryObject<ResearchName> WAND_CAP_GOLD = register("wand_cap_gold", () -> new ResearchName("WAND_CAP_GOLD"));
    public static final RegistryObject<ResearchName> WAND_CAP_PRIMALITE = register("wand_cap_primalite", () -> new ResearchName("WAND_CAP_PRIMALITE"));
    public static final RegistryObject<ResearchName> WAND_CAP_HEXIUM = register("wand_cap_hexium", () -> new ResearchName("WAND_CAP_HEXIUM"));
    public static final RegistryObject<ResearchName> WAND_CAP_HALLOWSTEEL = register("wand_cap_hallowsteel", () -> new ResearchName("WAND_CAP_HALLOWSTEEL"));
    public static final RegistryObject<ResearchName> WAND_CORE_OBSIDIAN = register("wand_core_obsidian", () -> new ResearchName("WAND_CORE_OBSIDIAN"));
    public static final RegistryObject<ResearchName> WAND_CORE_CORAL = register("wand_core_coral", () -> new ResearchName("WAND_CORE_CORAL"));
    public static final RegistryObject<ResearchName> WAND_CORE_BAMBOO = register("wand_core_bamboo", () -> new ResearchName("WAND_CORE_BAMBOO"));
    public static final RegistryObject<ResearchName> WAND_CORE_SUNWOOD = register("wand_core_sunwood", () -> new ResearchName("WAND_CORE_SUNWOOD"));
    public static final RegistryObject<ResearchName> WAND_CORE_MOONWOOD = register("wand_core_moonwood", () -> new ResearchName("WAND_CORE_MOONWOOD"));
    public static final RegistryObject<ResearchName> WAND_CORE_BONE = register("wand_core_bone", () -> new ResearchName("WAND_CORE_BONE"));
    public static final RegistryObject<ResearchName> WAND_CORE_BLAZE_ROD = register("wand_core_blaze_rod", () -> new ResearchName("WAND_CORE_BLAZE_ROD"));
    public static final RegistryObject<ResearchName> WAND_CORE_PURPUR = register("wand_core_purpur", () -> new ResearchName("WAND_CORE_PURPUR"));
    public static final RegistryObject<ResearchName> IMBUED_WOOL = register("imbued_wool", () -> new ResearchName("IMBUED_WOOL"));
    public static final RegistryObject<ResearchName> SPELLCLOTH = register("spellcloth", () -> new ResearchName("SPELLCLOTH"));
    public static final RegistryObject<ResearchName> HEXWEAVE = register("hexweave", () -> new ResearchName("HEXWEAVE"));
    public static final RegistryObject<ResearchName> SAINTSWOOL = register("saintswool", () -> new ResearchName("SAINTSWOOL"));
    public static final RegistryObject<ResearchName> ARTIFICIAL_MANA_FONTS = register("artificial_mana_fonts", () -> new ResearchName("ARTIFICIAL_MANA_FONTS"));
    public static final RegistryObject<ResearchName> FORBIDDEN_MANA_FONTS = register("forbidden_mana_fonts", () -> new ResearchName("FORBIDDEN_MANA_FONTS"));
    public static final RegistryObject<ResearchName> HEAVENLY_MANA_FONTS = register("heavenly_mana_fonts", () -> new ResearchName("HEAVENLY_MANA_FONTS"));
    public static final RegistryObject<ResearchName> MANA_ARROWS = register("mana_arrows", () -> new ResearchName("MANA_ARROWS"));
    public static final RegistryObject<ResearchName> ESSENCE_CASK_ENCHANTED = register("essence_cask_enchanted", () -> new ResearchName("ESSENCE_CASK_ENCHANTED"));
    public static final RegistryObject<ResearchName> ESSENCE_CASK_FORBIDDEN = register("essence_cask_forbidden", () -> new ResearchName("ESSENCE_CASK_FORBIDDEN"));
    public static final RegistryObject<ResearchName> ESSENCE_CASK_HEAVENLY = register("essence_cask_heavenly", () -> new ResearchName("ESSENCE_CASK_HEAVENLY"));
    public static final RegistryObject<ResearchName> WAND_GLAMOUR_TABLE = register("wand_glamour_table", () -> new ResearchName("WAND_GLAMOUR_TABLE"));
    public static final RegistryObject<ResearchName> ATTUNEMENT_SHACKLES = register("attunement_shackles", () -> new ResearchName("ATTUNEMENT_SHACKLES"));
    public static final RegistryObject<ResearchName> ENDERWARD = register("enderward", () -> new ResearchName("ENDERWARD"));
    
    // Register Alchemy research
    public static final RegistryObject<ResearchName> BASIC_ALCHEMY = register("basic_alchemy", () -> new ResearchName("BASIC_ALCHEMY"));
    public static final RegistryObject<ResearchName> EXPERT_ALCHEMY = register("expert_alchemy", () -> new ResearchName("EXPERT_ALCHEMY"));
    public static final RegistryObject<ResearchName> MASTER_ALCHEMY = register("master_alchemy", () -> new ResearchName("MASTER_ALCHEMY"));
    public static final RegistryObject<ResearchName> SUPREME_ALCHEMY = register("supreme_alchemy", () -> new ResearchName("SUPREME_ALCHEMY"));
    public static final RegistryObject<ResearchName> COMPLETE_ALCHEMY = register("complete_alchemy", () -> new ResearchName("COMPLETE_ALCHEMY"));
    public static final RegistryObject<ResearchName> STONEMELDING = register("stonemelding", () -> new ResearchName("STONEMELDING"));
    public static final RegistryObject<ResearchName> SKYGLASS = register("skyglass", () -> new ResearchName("SKYGLASS"));
    public static final RegistryObject<ResearchName> SHARD_SYNTHESIS = register("shard_synthesis", () -> new ResearchName("SHARD_SYNTHESIS"));
    public static final RegistryObject<ResearchName> SHARD_DESYNTHESIS = register("shard_desynthesis", () -> new ResearchName("SHARD_DESYNTHESIS"));
    public static final RegistryObject<ResearchName> PRIMALITE = register("primalite", () -> new ResearchName("PRIMALITE"));
    public static final RegistryObject<ResearchName> CRYSTAL_SYNTHESIS = register("crystal_synthesis", () -> new ResearchName("CRYSTAL_SYNTHESIS"));
    public static final RegistryObject<ResearchName> CRYSTAL_DESYNTHESIS = register("crystal_desynthesis", () -> new ResearchName("CRYSTAL_DESYNTHESIS"));
    public static final RegistryObject<ResearchName> HEXIUM = register("hexium", () -> new ResearchName("HEXIUM"));
    public static final RegistryObject<ResearchName> CLUSTER_SYNTHESIS = register("cluster_synthesis", () -> new ResearchName("CLUSTER_SYNTHESIS"));
    public static final RegistryObject<ResearchName> CLUSTER_DESYNTHESIS = register("cluster_desynthesis", () -> new ResearchName("CLUSTER_DESYNTHESIS"));
    public static final RegistryObject<ResearchName> HALLOWSTEEL = register("hallowsteel", () -> new ResearchName("HALLOWSTEEL"));
    public static final RegistryObject<ResearchName> CALCINATOR_BASIC = register("calcinator_basic", () -> new ResearchName("CALCINATOR_BASIC"));
    public static final RegistryObject<ResearchName> CALCINATOR_ENCHANTED = register("calcinator_enchanted", () -> new ResearchName("CALCINATOR_ENCHANTED"));
    public static final RegistryObject<ResearchName> CALCINATOR_FORBIDDEN = register("calcinator_forbidden", () -> new ResearchName("CALCINATOR_FORBIDDEN"));
    public static final RegistryObject<ResearchName> CALCINATOR_HEAVENLY = register("calcinator_heavenly", () -> new ResearchName("CALCINATOR_HEAVENLY"));
    public static final RegistryObject<ResearchName> CRYOTREATMENT = register("cryotreatment", () -> new ResearchName("CRYOTREATMENT"));
    public static final RegistryObject<ResearchName> SANGUINE_CRUCIBLE = register("sanguine_crucible", () -> new ResearchName("SANGUINE_CRUCIBLE"));
    public static final RegistryObject<ResearchName> SANGUINE_CORE_LAND_ANIMALS = register("sanguine_core_land_animals", () -> new ResearchName("SANGUINE_CORE_LAND_ANIMALS"));
    public static final RegistryObject<ResearchName> SANGUINE_CORE_SEA_CREATURES = register("sanguine_core_sea_creatures", () -> new ResearchName("SANGUINE_CORE_SEA_CREATURES"));
    public static final RegistryObject<ResearchName> SANGUINE_CORE_FLYING_CREATURES = register("sanguine_core_flying_creatures", () -> new ResearchName("SANGUINE_CORE_FLYING_CREATURES"));
    public static final RegistryObject<ResearchName> SANGUINE_CORE_PLANTS = register("sanguine_core_plants", () -> new ResearchName("SANGUINE_CORE_PLANTS"));
    public static final RegistryObject<ResearchName> SANGUINE_CORE_UNDEAD = register("sanguine_core_undead", () -> new ResearchName("SANGUINE_CORE_UNDEAD"));
    public static final RegistryObject<ResearchName> SANGUINE_CORE_SAPIENTS = register("sanguine_core_sapients", () -> new ResearchName("SANGUINE_CORE_SAPIENTS"));
    public static final RegistryObject<ResearchName> SANGUINE_CORE_DEMONS = register("sanguine_core_demons", () -> new ResearchName("SANGUINE_CORE_DEMONS"));
    public static final RegistryObject<ResearchName> SANGUINE_CORE_ALIENS = register("sanguine_core_aliens", () -> new ResearchName("SANGUINE_CORE_ALIENS"));
    public static final RegistryObject<ResearchName> IGNYX = register("ignyx", () -> new ResearchName("IGNYX"));
    public static final RegistryObject<ResearchName> SYNTHETIC_GEM_BUDS = register("synthetic_gem_buds", () -> new ResearchName("SYNTHETIC_GEM_BUDS"));
    
    // Register Sorcery research
    public static final RegistryObject<ResearchName> BASIC_SORCERY = register("basic_sorcery", () -> new ResearchName("BASIC_SORCERY"));
    public static final RegistryObject<ResearchName> EXPERT_SORCERY = register("expert_sorcery", () -> new ResearchName("EXPERT_SORCERY"));
    public static final RegistryObject<ResearchName> MASTER_SORCERY = register("master_sorcery", () -> new ResearchName("MASTER_SORCERY"));
    public static final RegistryObject<ResearchName> SUPREME_SORCERY = register("supreme_sorcery", () -> new ResearchName("SUPREME_SORCERY"));
    public static final RegistryObject<ResearchName> COMPLETE_SORCERY = register("complete_sorcery", () -> new ResearchName("COMPLETE_SORCERY"));
    public static final RegistryObject<ResearchName> WAND_INSCRIPTION = register("wand_inscription", () -> new ResearchName("WAND_INSCRIPTION"));
    public static final RegistryObject<ResearchName> SPELL_VEHICLE_PROJECTILE = register("spell_vehicle_projectile", () -> new ResearchName("SPELL_VEHICLE_PROJECTILE"));
    public static final RegistryObject<ResearchName> SPELL_VEHICLE_BOLT = register("spell_vehicle_bolt", () -> new ResearchName("SPELL_VEHICLE_BOLT"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_FROST = register("spell_payload_frost", () -> new ResearchName("SPELL_PAYLOAD_FROST"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_LIGHTNING = register("spell_payload_lightning", () -> new ResearchName("SPELL_PAYLOAD_LIGHTNING"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_SOLAR = register("spell_payload_solar", () -> new ResearchName("SPELL_PAYLOAD_SOLAR"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_LUNAR = register("spell_payload_lunar", () -> new ResearchName("SPELL_PAYLOAD_LUNAR"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_BLOOD = register("spell_payload_blood", () -> new ResearchName("SPELL_PAYLOAD_BLOOD"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_FLAME = register("spell_payload_flame", () -> new ResearchName("SPELL_PAYLOAD_FLAME"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_VOID = register("spell_payload_void", () -> new ResearchName("SPELL_PAYLOAD_VOID"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_HOLY = register("spell_payload_holy", () -> new ResearchName("SPELL_PAYLOAD_HOLY"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_BREAK = register("spell_payload_break", () -> new ResearchName("SPELL_PAYLOAD_BREAK"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_CONJURE_STONE = register("spell_payload_conjure_stone", () -> new ResearchName("SPELL_PAYLOAD_CONJURE_STONE"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_CONJURE_WATER = register("spell_payload_conjure_water", () -> new ResearchName("SPELL_PAYLOAD_CONJURE_WATER"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_SHEAR = register("spell_payload_shear", () -> new ResearchName("SPELL_PAYLOAD_SHEAR"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_FLIGHT = register("spell_payload_flight", () -> new ResearchName("SPELL_PAYLOAD_FLIGHT"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_CONJURE_LIGHT = register("spell_payload_conjure_light", () -> new ResearchName("SPELL_PAYLOAD_CONJURE_LIGHT"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_HEALING = register("spell_payload_healing", () -> new ResearchName("SPELL_PAYLOAD_HEALING"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_POLYMORPH = register("spell_payload_polymorph", () -> new ResearchName("SPELL_PAYLOAD_POLYMORPH"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_POLYMORPH_SHEEP = register("spell_payload_polymorph_sheep", () -> new ResearchName("SPELL_PAYLOAD_POLYMORPH_SHEEP"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_CONJURE_ANIMAL = register("spell_payload_conjure_animal", () -> new ResearchName("SPELL_PAYLOAD_CONJURE_ANIMAL"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_CONJURE_LAVA = register("spell_payload_conjure_lava", () -> new ResearchName("SPELL_PAYLOAD_CONJURE_LAVA"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_DRAIN_SOUL = register("spell_payload_drain_soul", () -> new ResearchName("SPELL_PAYLOAD_DRAIN_SOUL"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_TELEPORT = register("spell_payload_teleport", () -> new ResearchName("SPELL_PAYLOAD_TELEPORT"));
    public static final RegistryObject<ResearchName> SPELL_PAYLOAD_CONSECRATE = register("spell_payload_consecrate", () -> new ResearchName("SPELL_PAYLOAD_CONSECRATE"));
    public static final RegistryObject<ResearchName> SPELL_MOD_AMPLIFY = register("spell_mod_amplify", () -> new ResearchName("SPELL_MOD_AMPLIFY"));
    public static final RegistryObject<ResearchName> SPELL_MOD_MINE = register("spell_mod_mine", () -> new ResearchName("SPELL_MOD_MINE"));
    public static final RegistryObject<ResearchName> SPELL_MOD_QUICKEN = register("spell_mod_quicken", () -> new ResearchName("SPELL_MOD_QUICKEN"));
    public static final RegistryObject<ResearchName> SPELL_MOD_BURST = register("spell_mod_burst", () -> new ResearchName("SPELL_MOD_BURST"));
    public static final RegistryObject<ResearchName> SPELL_MOD_FORK = register("spell_mod_fork", () -> new ResearchName("SPELL_MOD_FORK"));
    
    // Register Runeworking research
    public static final RegistryObject<ResearchName> BASIC_RUNEWORKING = register("basic_runeworking", () -> new ResearchName("BASIC_RUNEWORKING"));
    public static final RegistryObject<ResearchName> EXPERT_RUNEWORKING = register("expert_runeworking", () -> new ResearchName("EXPERT_RUNEWORKING"));
    public static final RegistryObject<ResearchName> MASTER_RUNEWORKING = register("master_runeworking", () -> new ResearchName("MASTER_RUNEWORKING"));
    public static final RegistryObject<ResearchName> SUPREME_RUNEWORKING = register("supreme_runeworking", () -> new ResearchName("SUPREME_RUNEWORKING"));
    public static final RegistryObject<ResearchName> COMPLETE_RUNEWORKING = register("complete_runeworking", () -> new ResearchName("COMPLETE_RUNEWORKING"));
    public static final RegistryObject<ResearchName> RUNE_EARTH = register("rune_earth", () -> new ResearchName("RUNE_EARTH"));
    public static final RegistryObject<ResearchName> RUNE_SEA = register("rune_sea", () -> new ResearchName("RUNE_SEA"));
    public static final RegistryObject<ResearchName> RUNE_SKY = register("rune_sky", () -> new ResearchName("RUNE_SKY"));
    public static final RegistryObject<ResearchName> RUNE_SUN = register("rune_sun", () -> new ResearchName("RUNE_SUN"));
    public static final RegistryObject<ResearchName> RUNE_MOON = register("rune_moon", () -> new ResearchName("RUNE_MOON"));
    public static final RegistryObject<ResearchName> RUNE_PROJECT = register("rune_project", () -> new ResearchName("RUNE_PROJECT"));
    public static final RegistryObject<ResearchName> RUNE_PROTECT = register("rune_protect", () -> new ResearchName("RUNE_PROTECT"));
    public static final RegistryObject<ResearchName> RUNE_ITEM = register("rune_item", () -> new ResearchName("RUNE_ITEM"));
    public static final RegistryObject<ResearchName> RUNE_SELF = register("rune_self", () -> new ResearchName("RUNE_SELF"));
    public static final RegistryObject<ResearchName> RUNE_BLOOD = register("rune_blood", () -> new ResearchName("RUNE_BLOOD"));
    public static final RegistryObject<ResearchName> RUNE_INFERNAL = register("rune_infernal", () -> new ResearchName("RUNE_INFERNAL"));
    public static final RegistryObject<ResearchName> RUNE_VOID = register("rune_void", () -> new ResearchName("RUNE_VOID"));
    public static final RegistryObject<ResearchName> RUNE_ABSORB = register("rune_absorb", () -> new ResearchName("RUNE_ABSORB"));
    public static final RegistryObject<ResearchName> RUNE_DISPEL = register("rune_dispel", () -> new ResearchName("RUNE_DISPEL"));
    public static final RegistryObject<ResearchName> RUNE_SUMMON = register("rune_summon", () -> new ResearchName("RUNE_SUMMON"));
    public static final RegistryObject<ResearchName> RUNE_AREA = register("rune_area", () -> new ResearchName("RUNE_AREA"));
    public static final RegistryObject<ResearchName> RUNE_CREATURE = register("rune_creature", () -> new ResearchName("RUNE_CREATURE"));
    public static final RegistryObject<ResearchName> RUNE_HALLOWED = register("rune_hallowed", () -> new ResearchName("RUNE_HALLOWED"));
    public static final RegistryObject<ResearchName> RUNE_INSIGHT = register("rune_insight", () -> new ResearchName("RUNE_INSIGHT"));
    public static final RegistryObject<ResearchName> RUNE_POWER = register("rune_power", () -> new ResearchName("RUNE_POWER"));
    public static final RegistryObject<ResearchName> RUNE_GRACE = register("rune_grace", () -> new ResearchName("RUNE_GRACE"));
    public static final RegistryObject<ResearchName> RUNIC_GRINDSTONE = register("runic_grindstone", () -> new ResearchName("RUNIC_GRINDSTONE"));
    public static final RegistryObject<ResearchName> RECALL_STONE = register("recall_stone", () -> new ResearchName("RECALL_STONE"));
    public static final RegistryObject<ResearchName> RUNIC_TRIM = register("runic_trim", () -> new ResearchName("RUNIC_TRIM"));

    // Register Ritual Magick research
    public static final RegistryObject<ResearchName> BASIC_RITUAL = register("basic_ritual", () -> new ResearchName("BASIC_RITUAL"));
    public static final RegistryObject<ResearchName> EXPERT_RITUAL = register("expert_ritual", () -> new ResearchName("EXPERT_RITUAL"));
    public static final RegistryObject<ResearchName> MASTER_RITUAL = register("master_ritual", () -> new ResearchName("MASTER_RITUAL"));
    public static final RegistryObject<ResearchName> SUPREME_RITUAL = register("supreme_ritual", () -> new ResearchName("SUPREME_RITUAL"));
    public static final RegistryObject<ResearchName> COMPLETE_RITUAL = register("complete_ritual", () -> new ResearchName("COMPLETE_RITUAL"));
    public static final RegistryObject<ResearchName> MANAFRUIT = register("manafruit", () -> new ResearchName("MANAFRUIT"));
    public static final RegistryObject<ResearchName> RITUAL_CANDLES = register("ritual_candles", () -> new ResearchName("RITUAL_CANDLES"));
    public static final RegistryObject<ResearchName> INCENSE_BRAZIER = register("incense_brazier", () -> new ResearchName("INCENSE_BRAZIER"));
    public static final RegistryObject<ResearchName> RITUAL_LECTERN = register("ritual_lectern", () -> new ResearchName("RITUAL_LECTERN"));
    public static final RegistryObject<ResearchName> RITUAL_BELL = register("ritual_bell", () -> new ResearchName("RITUAL_BELL"));
    public static final RegistryObject<ResearchName> BLOODLETTER = register("bloodletter", () -> new ResearchName("BLOODLETTER"));
    public static final RegistryObject<ResearchName> SOUL_ANVIL = register("soul_anvil", () -> new ResearchName("SOUL_ANVIL"));
    public static final RegistryObject<ResearchName> CELESTIAL_HARP = register("celestial_harp", () -> new ResearchName("CELESTIAL_HARP"));
    public static final RegistryObject<ResearchName> WAND_CORE_PRIMAL = register("wand_core_primal", () -> new ResearchName("WAND_CORE_PRIMAL"));
    public static final RegistryObject<ResearchName> WAND_CORE_DARK_PRIMAL = register("wand_core_dark_primal", () -> new ResearchName("WAND_CORE_DARK_PRIMAL"));
    public static final RegistryObject<ResearchName> WAND_CORE_PURE_PRIMAL = register("wand_core_pure_primal", () -> new ResearchName("WAND_CORE_PURE_PRIMAL"));
    public static final RegistryObject<ResearchName> PIXIES = register("pixies", () -> new ResearchName("PIXIES"));
    public static final RegistryObject<ResearchName> GRAND_PIXIES = register("grand_pixies", () -> new ResearchName("GRAND_PIXIES"));
    public static final RegistryObject<ResearchName> MAJESTIC_PIXIES = register("majestic_pixies", () -> new ResearchName("MAJESTIC_PIXIES"));
    public static final RegistryObject<ResearchName> AMBROSIA = register("ambrosia", () -> new ResearchName("AMBROSIA"));
    public static final RegistryObject<ResearchName> GREATER_AMBROSIA = register("greater_ambrosia", () -> new ResearchName("GREATER_AMBROSIA"));
    public static final RegistryObject<ResearchName> SUPREME_AMBROSIA = register("supreme_ambrosia", () -> new ResearchName("SUPREME_AMBROSIA"));
    public static final RegistryObject<ResearchName> FLYING_CARPET = register("flying_carpet", () -> new ResearchName("FLYING_CARPET"));
    public static final RegistryObject<ResearchName> CLEANSING_RITE = register("cleansing_rite", () -> new ResearchName("CLEANSING_RITE"));
    public static final RegistryObject<ResearchName> PRIMAL_SHOVEL = register("primal_shovel", () -> new ResearchName("PRIMAL_SHOVEL"));
    public static final RegistryObject<ResearchName> PRIMAL_FISHING_ROD = register("primal_fishing_rod", () -> new ResearchName("PRIMAL_FISHING_ROD"));
    public static final RegistryObject<ResearchName> PRIMAL_AXE = register("primal_axe", () -> new ResearchName("PRIMAL_AXE"));
    public static final RegistryObject<ResearchName> PRIMAL_HOE = register("primal_hoe", () -> new ResearchName("PRIMAL_HOE"));
    public static final RegistryObject<ResearchName> PRIMAL_PICKAXE = register("primal_pickaxe", () -> new ResearchName("PRIMAL_PICKAXE"));
    public static final RegistryObject<ResearchName> FORBIDDEN_TRIDENT = register("forbidden_trident", () -> new ResearchName("FORBIDDEN_TRIDENT"));
    public static final RegistryObject<ResearchName> FORBIDDEN_BOW = register("forbidden_bow", () -> new ResearchName("FORBIDDEN_BOW"));
    public static final RegistryObject<ResearchName> FORBIDDEN_SWORD = register("forbidden_sword", () -> new ResearchName("FORBIDDEN_SWORD"));
    public static final RegistryObject<ResearchName> SACRED_SHIELD = register("sacred_shield", () -> new ResearchName("SACRED_SHIELD"));
    public static final RegistryObject<ResearchName> DREAM_VISION_TALISMAN = register("dream_vision_talisman", () -> new ResearchName("DREAM_VISION_TALISMAN"));
    public static final RegistryObject<ResearchName> DOWSING_ROD = register("dowsing_rod", () -> new ResearchName("DOWSING_ROD"));
    public static final RegistryObject<ResearchName> HYDROMELON = register("hydromelon", () -> new ResearchName("HYDROMELON"));
    public static final RegistryObject<ResearchName> BLOOD_ROSE = register("blood_rose", () -> new ResearchName("BLOOD_ROSE"));
    public static final RegistryObject<ResearchName> EMBERFLOWER = register("emberflower", () -> new ResearchName("EMBERFLOWER"));

    // Register Magitech research
    public static final RegistryObject<ResearchName> BASIC_MAGITECH = register("basic_magitech", () -> new ResearchName("BASIC_MAGITECH"));
    public static final RegistryObject<ResearchName> EXPERT_MAGITECH = register("expert_magitech", () -> new ResearchName("EXPERT_MAGITECH"));
    public static final RegistryObject<ResearchName> MASTER_MAGITECH = register("master_magitech", () -> new ResearchName("MASTER_MAGITECH"));
    public static final RegistryObject<ResearchName> SUPREME_MAGITECH = register("supreme_magitech", () -> new ResearchName("SUPREME_MAGITECH"));
    public static final RegistryObject<ResearchName> COMPLETE_MAGITECH = register("complete_magitech", () -> new ResearchName("COMPLETE_MAGITECH"));
    public static final RegistryObject<ResearchName> HONEY_EXTRACTOR = register("honey_extractor", () -> new ResearchName("HONEY_EXTRACTOR"));
    public static final RegistryObject<ResearchName> SEASCRIBE_PEN = register("seascribe_pen", () -> new ResearchName("SEASCRIBE_PEN"));
    public static final RegistryObject<ResearchName> ARCANOMETER = register("arcanometer", () -> new ResearchName("ARCANOMETER"));
    public static final RegistryObject<ResearchName> PRIMALITE_GOLEM = register("primalite_golem", () -> new ResearchName("PRIMALITE_GOLEM"));
    public static final RegistryObject<ResearchName> HEXIUM_GOLEM = register("hexium_golem", () -> new ResearchName("HEXIUM_GOLEM"));
    public static final RegistryObject<ResearchName> HALLOWSTEEL_GOLEM = register("hallowsteel_golem", () -> new ResearchName("HALLOWSTEEL_GOLEM"));
    public static final RegistryObject<ResearchName> CONCOCTING_TINCTURES = register("concocting_tinctures", () -> new ResearchName("CONCOCTING_TINCTURES"));
    public static final RegistryObject<ResearchName> CONCOCTING_PHILTERS = register("concocting_philters", () -> new ResearchName("CONCOCTING_PHILTERS"));
    public static final RegistryObject<ResearchName> CONCOCTING_ELIXIRS = register("concocting_elixirs", () -> new ResearchName("CONCOCTING_ELIXIRS"));
    public static final RegistryObject<ResearchName> CONCOCTING_BOMBS = register("concocting_bombs", () -> new ResearchName("CONCOCTING_BOMBS"));
    public static final RegistryObject<ResearchName> ENTROPY_SINK = register("entropy_sink", () -> new ResearchName("ENTROPY_SINK"));
    public static final RegistryObject<ResearchName> AUTO_CHARGER = register("auto_charger", () -> new ResearchName("AUTO_CHARGER"));
    public static final RegistryObject<ResearchName> ESSENCE_TRANSMUTER = register("essence_transmuter", () -> new ResearchName("ESSENCE_TRANSMUTER"));
    public static final RegistryObject<ResearchName> DISSOLUTION_CHAMBER = register("dissolution_chamber", () -> new ResearchName("DISSOLUTION_CHAMBER"));
    public static final RegistryObject<ResearchName> ZEPHYR_ENGINE = register("zephyr_engine", () -> new ResearchName("ZEPHYR_ENGINE"));
    public static final RegistryObject<ResearchName> VOID_TURBINE = register("void_turbine", () -> new ResearchName("VOID_TURBINE"));
    public static final RegistryObject<ResearchName> INFERNAL_FURNACE = register("infernal_furnace", () -> new ResearchName("INFERNAL_FURNACE"));
    public static final RegistryObject<ResearchName> MANA_NEXUS = register("mana_nexus", () -> new ResearchName("MANA_NEXUS"));
    public static final RegistryObject<ResearchName> MANA_SINGULARITY = register("mana_singularity", () -> new ResearchName("MANA_SINGULARITY"));
    public static final RegistryObject<ResearchName> WARDING_MODULE = register("warding_module", () -> new ResearchName("WARDING_MODULE"));
    public static final RegistryObject<ResearchName> GREATER_WARDING_MODULE = register("greater_warding_module", () -> new ResearchName("GREATER_WARDING_MODULE"));
    public static final RegistryObject<ResearchName> SUPREME_WARDING_MODULE = register("supreme_warding_module", () -> new ResearchName("SUPREME_WARDING_MODULE"));
    
    // Register scan research
    public static final RegistryObject<ResearchName> RAW_MARBLE = register("raw_marble", () -> new ResearchName("RAW_MARBLE"));
    public static final RegistryObject<ResearchName> HALLOWED_ORB = register("hallowed_orb", () -> new ResearchName("HALLOWED_ORB"));
    public static final RegistryObject<ResearchName> HALLOWOOD_TREES = register("hallowood_trees", () -> new ResearchName("HALLOWOOD_TREES"));
    public static final RegistryObject<ResearchName> SUNWOOD_TREES = register("sunwood_trees", () -> new ResearchName("SUNWOOD_TREES"));
    public static final RegistryObject<ResearchName> MOONWOOD_TREES = register("moonwood_trees", () -> new ResearchName("MOONWOOD_TREES"));
    public static final RegistryObject<ResearchName> ROCK_SALT = register("rock_salt", () -> new ResearchName("ROCK_SALT"));
    public static final RegistryObject<ResearchName> ALCHEMICAL_WASTE = register("alchemical_waste", () -> new ResearchName("ALCHEMICAL_WASTE"));
    public static final RegistryObject<ResearchName> QUARTZ = register("quartz", () -> new ResearchName("QUARTZ"));
    public static final RegistryObject<ResearchName> INNER_DEMON = register("inner_demon", () -> new ResearchName("INNER_DEMON"));
    public static final RegistryObject<ResearchName> BOOKSHELF = register("bookshelf", () -> new ResearchName("BOOKSHELF"));
    public static final RegistryObject<ResearchName> BEEHIVE = register("beehive", () -> new ResearchName("BEEHIVE"));
    public static final RegistryObject<ResearchName> BEACON = register("beacon", () -> new ResearchName("BEACON"));
    public static final RegistryObject<ResearchName> DRAGON_EGG = register("dragon_egg", () -> new ResearchName("DRAGON_EGG"));
    public static final RegistryObject<ResearchName> DRAGON_HEAD = register("dragon_head", () -> new ResearchName("DRAGON_HEAD"));
    public static final RegistryObject<ResearchName> MYSTICAL_RELIC = register("mystical_relic", () -> new ResearchName("MYSTICAL_RELIC"));
    public static final RegistryObject<ResearchName> HUMMING_ARTIFACT = register("humming_artifact", () -> new ResearchName("HUMMING_ARTIFACT"));
    public static final RegistryObject<ResearchName> TREEFOLK = register("treefolk", () -> new ResearchName("TREEFOLK"));
    
    // Register internal research
    public static final RegistryObject<ResearchName> INTERNAL_GOT_DREAM = register("got_dream", () -> new ResearchName("t_got_dream"));
    public static final RegistryObject<ResearchName> INTERNAL_DISCOVER_BLOOD = register("discover_blood", () -> new ResearchName("t_discover_blood"));
    public static final RegistryObject<ResearchName> INTERNAL_DISCOVER_INFERNAL = register("discover_infernal", () -> new ResearchName("t_discover_infernal"));
    public static final RegistryObject<ResearchName> INTERNAL_DISCOVER_VOID = register("discover_void", () -> new ResearchName("t_discover_void"));
    public static final RegistryObject<ResearchName> INTERNAL_DISCOVER_FORBIDDEN = register("discover_forbidden", () -> new ResearchName("t_discover_forbidden"));
    public static final RegistryObject<ResearchName> INTERNAL_DISCOVER_HALLOWED = register("discover_hallowed", () -> new ResearchName("t_discover_hallowed"));
    public static final RegistryObject<ResearchName> INTERNAL_UNKNOWN_RUNE = register("unknown_rune", () -> new ResearchName("u_unknown_rune"));
    public static final RegistryObject<ResearchName> INTERNAL_SPELLS_CAST_EXPERT = register("spells_cast_expert", () -> new ResearchName("t_spells_cast_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_SPELLS_CAST_MASTER = register("spells_cast_master", () -> new ResearchName("t_spells_cast_master"));
    public static final RegistryObject<ResearchName> INTERNAL_SPELLS_CAST_SUPREME = register("spells_cast_supreme", () -> new ResearchName("t_spells_cast_supreme"));
    public static final RegistryObject<ResearchName> INTERNAL_SPELLS_CRAFTED_EXPERT = register("spells_crafted_expert", () -> new ResearchName("t_spells_crafted_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_SPELL_COST_MASTER = register("spell_cost_master", () -> new ResearchName("t_spell_cost_master"));
    public static final RegistryObject<ResearchName> INTERNAL_SPELL_COST_SUPREME = register("spell_cost_supreme", () -> new ResearchName("t_spell_cost_supreme"));
    public static final RegistryObject<ResearchName> INTERNAL_FOUND_SHRINE = register("found_shrine", () -> new ResearchName("m_found_shrine"));
    public static final RegistryObject<ResearchName> INTERNAL_FOUND_SHRINE_EARTH = register("found_shrine_earth", () -> new ResearchName("m_found_shrine_earth"));
    public static final RegistryObject<ResearchName> INTERNAL_FOUND_SHRINE_SEA = register("found_shrine_sea", () -> new ResearchName("m_found_shrine_sea"));
    public static final RegistryObject<ResearchName> INTERNAL_FOUND_SHRINE_SKY = register("found_shrine_sky", () -> new ResearchName("m_found_shrine_sky"));
    public static final RegistryObject<ResearchName> INTERNAL_FOUND_SHRINE_SUN = register("found_shrine_sun", () -> new ResearchName("m_found_shrine_sun"));
    public static final RegistryObject<ResearchName> INTERNAL_FOUND_SHRINE_MOON = register("found_shrine_moon", () -> new ResearchName("m_found_shrine_moon"));
    public static final RegistryObject<ResearchName> INTERNAL_MANA_SPENT_EARTH_EXPERT = register("mana_spent_earth_expert", () -> new ResearchName("t_mana_spent_earth_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_MANA_SPENT_SEA_EXPERT = register("mana_spent_sea_expert", () -> new ResearchName("t_mana_spent_sea_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_MANA_SPENT_SKY_EXPERT = register("mana_spent_sky_expert", () -> new ResearchName("t_mana_spent_sky_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_MANA_SPENT_SUN_EXPERT = register("mana_spent_sun_expert", () -> new ResearchName("t_mana_spent_sun_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_MANA_SPENT_MOON_EXPERT = register("mana_spent_moon_expert", () -> new ResearchName("t_mana_spent_moon_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_MANA_SPENT_BLOOD_EXPERT = register("mana_spent_blood_expert", () -> new ResearchName("t_mana_spent_blood_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_MANA_SPENT_INFERNAL_EXPERT = register("mana_spent_infernal_expert", () -> new ResearchName("t_mana_spent_infernal_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_MANA_SPENT_VOID_EXPERT = register("mana_spent_void_expert", () -> new ResearchName("t_mana_spent_void_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_MANA_SPENT_HALLOWED_EXPERT = register("mana_spent_hallowed_expert", () -> new ResearchName("t_mana_spent_hallowed_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_ENV_EARTH = register("env_earth", () -> new ResearchName("m_env_earth"));
    public static final RegistryObject<ResearchName> INTERNAL_ENV_SEA = register("env_sea", () -> new ResearchName("m_env_sea"));
    public static final RegistryObject<ResearchName> INTERNAL_ENV_SKY = register("env_sky", () -> new ResearchName("m_env_sky"));
    public static final RegistryObject<ResearchName> INTERNAL_ENV_SUN = register("env_sun", () -> new ResearchName("m_env_sun"));
    public static final RegistryObject<ResearchName> INTERNAL_ENV_MOON = register("env_moon", () -> new ResearchName("m_env_moon"));
    public static final RegistryObject<ResearchName> INTERNAL_CRAFTED_ALCHEMY_EXPERT = register("crafted_alchemy_expert", () -> new ResearchName("b_crafted_alchemy_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_CRAFTED_ALCHEMY_MASTER = register("crafted_alchemy_master", () -> new ResearchName("b_crafted_alchemy_master"));
    public static final RegistryObject<ResearchName> INTERNAL_CRAFTED_ALCHEMY_SUPREME = register("crafted_alchemy_supreme", () -> new ResearchName("b_crafted_alchemy_supreme"));
    public static final RegistryObject<ResearchName> INTERNAL_CRAFTED_MANAWEAVING_EXPERT = register("crafted_manaweaving_expert", () -> new ResearchName("b_crafted_manaweaving_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_CRAFTED_MANAWEAVING_MASTER = register("crafted_manaweaving_master", () -> new ResearchName("b_crafted_manaweaving_master"));
    public static final RegistryObject<ResearchName> INTERNAL_CRAFTED_MANAWEAVING_SUPREME = register("crafted_manaweaving_supreme", () -> new ResearchName("b_crafted_manaweaving_supreme"));
    public static final RegistryObject<ResearchName> INTERNAL_CRAFTED_RUNEWORKING_EXPERT = register("crafted_runeworking_expert", () -> new ResearchName("b_crafted_runeworking_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_CRAFTED_RUNEWORKING_MASTER = register("crafted_runeworking_master", () -> new ResearchName("b_crafted_runeworking_master"));
    public static final RegistryObject<ResearchName> INTERNAL_CRAFTED_RUNEWORKING_SUPREME = register("crafted_runeworking_supreme", () -> new ResearchName("b_crafted_runeworking_supreme"));
    public static final RegistryObject<ResearchName> INTERNAL_RITUALS_COMPLETED_EXPERT = register("rituals_completed_expert", () -> new ResearchName("t_rituals_completed_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_RITUALS_COMPLETED_MASTER = register("rituals_completed_master", () -> new ResearchName("t_rituals_completed_master"));
    public static final RegistryObject<ResearchName> INTERNAL_RITUALS_COMPLETED_SUPREME = register("rituals_completed_supreme", () -> new ResearchName("t_rituals_completed_supreme"));
    public static final RegistryObject<ResearchName> INTERNAL_RITUAL_MISHAPS_BASIC = register("ritual_mishaps_basic", () -> new ResearchName("t_ritual_mishaps_basic"));
    public static final RegistryObject<ResearchName> INTERNAL_CRAFTED_MAGITECH_EXPERT = register("crafted_magitech_expert", () -> new ResearchName("b_crafted_magitech_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_CRAFTED_MAGITECH_MASTER = register("crafted_magitech_master", () -> new ResearchName("b_crafted_magitech_master"));
    public static final RegistryObject<ResearchName> INTERNAL_CRAFTED_MAGITECH_SUPREME = register("crafted_magitech_supreme", () -> new ResearchName("b_crafted_magitech_supreme"));
    public static final RegistryObject<ResearchName> INTERNAL_ITEMS_RUNESCRIBED_EXPERT = register("items_runescribed_expert", () -> new ResearchName("t_items_runescribed_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_ITEMS_RUNESCRIBED_MASTER = register("items_runescribed_master", () -> new ResearchName("t_items_runescribed_master"));
    public static final RegistryObject<ResearchName> INTERNAL_ITEMS_RUNESCRIBED_SUPREME = register("items_runescribed_supreme", () -> new ResearchName("t_items_runescribed_supreme"));
    public static final RegistryObject<ResearchName> INTERNAL_RESEARCH_PROJECTS_COMPLETED = register("research_projects_completed", () -> new ResearchName("t_research_projects_completed"));
    public static final RegistryObject<ResearchName> INTERNAL_ITEMS_ANALYZED = register("items_analyzed", () -> new ResearchName("t_items_analyzed"));
    public static final RegistryObject<ResearchName> INTERNAL_SCAN_PRIMALITE = register("scan_primalite", () -> new ResearchName("b_scan_primalite"));
    public static final RegistryObject<ResearchName> INTERNAL_SCAN_HEXIUM = register("scan_hexium", () -> new ResearchName("b_scan_hexium"));
    public static final RegistryObject<ResearchName> INTERNAL_SCAN_HALLOWSTEEL = register("scan_hallowsteel", () -> new ResearchName("b_scan_hallowsteel"));
    public static final RegistryObject<ResearchName> INTERNAL_SCAN_FLYING_CREATURE = register("flying_creature", () -> new ResearchName("t_flying_creature"));
    public static final RegistryObject<ResearchName> INTERNAL_SCAN_GOLEM = register("golem", () -> new ResearchName("t_golem"));
    public static final RegistryObject<ResearchName> INTERNAL_OBSERVATIONS_MADE_BASICS = register("observations_made_basics", () -> new ResearchName("t_observations_made_basics"));
    public static final RegistryObject<ResearchName> INTERNAL_OBSERVATIONS_MADE_EXPERT = register("observations_made_expert", () -> new ResearchName("t_observations_made_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_THEORIES_FORMED_BASICS = register("theories_formed_basics", () -> new ResearchName("t_theories_formed_basics"));
    public static final RegistryObject<ResearchName> INTERNAL_BLOCKS_BROKEN_BAREHANDED_EXPERT = register("blocks_broken_barehanded_expert", () -> new ResearchName("m_blocks_broken_barehanded_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_PLACE_STONE_EXPERT = register("place_stone_expert", () -> new ResearchName("b_place_stone_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_DROWN_A_LITTLE = register("drown_a_little", () -> new ResearchName("m_drown_a_little"));
    public static final RegistryObject<ResearchName> INTERNAL_SHEARS_USED = register("shears_used", () -> new ResearchName("b_shears_used_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_FLY_ELYTRA = register("fly_elytra", () -> new ResearchName("m_fly_elytra"));
    public static final RegistryObject<ResearchName> INTERNAL_PLACE_TORCH_EXPERT = register("place_torch_expert", () -> new ResearchName("b_place_torch_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_NEAR_DEATH_EXPERIENCE = register("near_death_experience", () -> new ResearchName("m_near_death_experience"));
    public static final RegistryObject<ResearchName> INTERNAL_FURRY_FRIEND = register("furry_friend", () -> new ResearchName("m_furry_friend"));
    public static final RegistryObject<ResearchName> INTERNAL_BREED_ANIMAL = register("breed_animal", () -> new ResearchName("m_breed_animal"));
    public static final RegistryObject<ResearchName> INTERNAL_FEEL_THE_BURN = register("feel_the_burn", () -> new ResearchName("m_feel_the_burn"));
    public static final RegistryObject<ResearchName> INTERNAL_TELEPORT_A_LOT = register("teleport_a_lot", () -> new ResearchName("m_teleport_a_lot"));
    public static final RegistryObject<ResearchName> INTERNAL_SCAN_NETHER_STAR = register("scan_nether_star", () -> new ResearchName("b_scan_nether_star"));
    public static final RegistryObject<ResearchName> INTERNAL_MANA_SIPHONED_BASICS = register("mana_siphoned_basics", () -> new ResearchName("t_mana_siphoned_basics"));
    public static final RegistryObject<ResearchName> INTERNAL_MANA_SIPHONED_EXPERT = register("mana_siphoned_expert", () -> new ResearchName("t_mana_siphoned_expert"));
    public static final RegistryObject<ResearchName> INTERNAL_MANA_SPENT_TOTAL_BASICS = register("mana_spent_total_basics", () -> new ResearchName("t_mana_spent_total_basics"));
    public static final RegistryObject<ResearchName> INTERNAL_SOTU_DISCOVER_BLOOD = register("sotu_discover_blood", () -> new ResearchName("m_sotu_discover_blood"));
    public static final RegistryObject<ResearchName> INTERNAL_SOTU_DISCOVER_INFERNAL = register("sotu_discover_infernal", () -> new ResearchName("m_sotu_discover_infernal"));
    public static final RegistryObject<ResearchName> INTERNAL_SOTU_DISCOVER_VOID = register("sotu_discover_void", () -> new ResearchName("m_sotu_discover_void"));
    public static final RegistryObject<ResearchName> INTERNAL_SOTU_RESEARCH_ARCANOMETER = register("sotu_research_arcanometer", () -> new ResearchName("t_sotu_research_arcanometer"));
    public static final RegistryObject<ResearchName> INTERNAL_SOTU_RESEARCH_HEXIUM = register("sotu_research_hexium", () -> new ResearchName("t_sotu_research_hexium"));
    public static final RegistryObject<ResearchName> INTERNAL_SOTU_RESEARCH_POWER_RUNE = register("sotu_research_power_rune", () -> new ResearchName("t_sotu_research_power_rune"));
    public static final RegistryObject<ResearchName> INTERNAL_SOTU_RESEARCH_SANGUINE_CRUCIBLE = register("sotu_research_sanguine_crucible", () -> new ResearchName("t_sotu_research_sanguine_crucible"));
    public static final RegistryObject<ResearchName> INTERNAL_SOTU_RESEARCH_CLEANSING_RITE = register("sotu_research_cleansing_rite", () -> new ResearchName("t_sotu_research_cleansing_rite"));
    public static final RegistryObject<ResearchName> INTERNAL_SOTU_SCAN_HALLOWED_ORB = register("sotu_scan_hallowed_orb", () -> new ResearchName("b_sotu_scan_hallowed_orb"));
    public static final RegistryObject<ResearchName> INTERNAL_WAND_TRANSFORM_HINT = register("wand_transform_hint", () -> new ResearchName("m_wand_transform_hint"));
    public static final RegistryObject<ResearchName> INTERNAL_SIPHON_PROMPT = register("siphon_prompt", () -> new ResearchName("m_siphon_prompt"));
}

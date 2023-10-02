package com.verdantartifice.primalmagick.common.research;

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
    public static ResearchName find(String name) {
        return NAMES.get().getValues().stream().filter(rn -> rn.matches(name)).findFirst().orElseThrow(() -> new IllegalArgumentException("Unrecognized research name"));
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
}

package com.verdantartifice.primalmagick.test;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.damagesource.DamageSourcesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadsPM;
import com.verdantartifice.primalmagick.platform.Services;
import com.verdantartifice.primalmagick.test.attunements.AttunementTests;
import com.verdantartifice.primalmagick.test.capabilities.PlayerKnowledgeTests;
import com.verdantartifice.primalmagick.test.capabilities.ItemHandlerTests;
import com.verdantartifice.primalmagick.test.crafting.RepairTests;
import com.verdantartifice.primalmagick.test.crafting.RunecarvingTests;
import com.verdantartifice.primalmagick.test.crafting.CraftingRequirementsTests;
import com.verdantartifice.primalmagick.test.crafting.CalcinatorTests;
import com.verdantartifice.primalmagick.test.crafting.ArcaneWorkbenchTests;
import com.verdantartifice.primalmagick.test.enchantments.RitualEnchantmentTests;
import com.verdantartifice.primalmagick.test.enchantments.CasterEnchantingTests;
import com.verdantartifice.primalmagick.test.ftux.FtuxTests;
import com.verdantartifice.primalmagick.test.items.WandManaTests;
import com.verdantartifice.primalmagick.test.items.DispenserTests;
import com.verdantartifice.primalmagick.test.items.BeeswaxTests;
import com.verdantartifice.primalmagick.test.research.ResearchTests;
import com.verdantartifice.primalmagick.test.research.ResearchRequirementsTests;
import com.verdantartifice.primalmagick.test.research.ResearchKeysTests;
import com.verdantartifice.primalmagick.test.spells.WandSpellcastTests;
import com.verdantartifice.primalmagick.test.tiles.WandChargerTests;
import com.verdantartifice.primalmagick.test.tiles.ManaFontTests;
import com.verdantartifice.primalmagick.test.tiles.ManaBatteryTests;
import com.verdantartifice.primalmagick.test.tiles.AutoChargerTests;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class TestFunctionsPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.TEST_FUNCTIONS_REGISTRY.init();
    }

    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CANARY = Services.TEST_FUNCTIONS_REGISTRY.register("canary", () -> CanaryTest::canary);

    // Attunement buff tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_EARTH = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_earth", () -> (helper) -> AttunementTests.minor_attunement_gives_mana_discount(helper, Sources.EARTH));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_SEA = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_sea", () -> (helper) -> AttunementTests.minor_attunement_gives_mana_discount(helper, Sources.SEA));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_SKY = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_sky", () -> (helper) -> AttunementTests.minor_attunement_gives_mana_discount(helper, Sources.SKY));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_SUN = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_sun", () -> (helper) -> AttunementTests.minor_attunement_gives_mana_discount(helper, Sources.SUN));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_MOON = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_moon", () -> (helper) -> AttunementTests.minor_attunement_gives_mana_discount(helper, Sources.MOON));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_BLOOD = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_blood", () -> (helper) -> AttunementTests.minor_attunement_gives_mana_discount(helper, Sources.BLOOD));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_INFERNAL = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_infernal", () -> (helper) -> AttunementTests.minor_attunement_gives_mana_discount(helper, Sources.INFERNAL));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_VOID = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_void", () -> (helper) -> AttunementTests.minor_attunement_gives_mana_discount(helper, Sources.VOID));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MINOR_ATTUNEMENT_DISCOUNT_HALLOWED = Services.TEST_FUNCTIONS_REGISTRY.register("minor_attunement_gives_mana_discount_hallowed", () -> (helper) -> AttunementTests.minor_attunement_gives_mana_discount(helper, Sources.HALLOWED));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LESSER_EARTH_ATTUNEMENT_BUFF = Services.TEST_FUNCTIONS_REGISTRY.register("lesser_earth_attunement_gives_haste_modifier", () -> AttunementTests::lesser_earth_attunement_gives_haste_modifier);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GREATER_EARTH_ATTUNEMENT_BUFF = Services.TEST_FUNCTIONS_REGISTRY.register("greater_earth_attunement_gives_step_height_modifier", () -> AttunementTests::greater_earth_attunement_gives_step_height_modifier);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LESSER_SEA_ATTUNEMENT_BUFF = Services.TEST_FUNCTIONS_REGISTRY.register("lesser_sea_attunement_gives_increased_swim_speed", () -> AttunementTests::lesser_sea_attunement_gives_increased_swim_speed);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GREATER_SEA_ATTUNEMENT_BUFF = Services.TEST_FUNCTIONS_REGISTRY.register("greater_sea_attunement_gives_water_breathing", () -> AttunementTests::greater_sea_attunement_gives_water_breathing);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LESSER_SKY_ATTUNEMENT_BUFF1 = Services.TEST_FUNCTIONS_REGISTRY.register("lesser_sky_attunement_gives_movement_speed_modifier", () -> AttunementTests::lesser_sky_attunement_gives_movement_speed_modifier);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LESSER_SKY_ATTUNEMENT_BUFF2 = Services.TEST_FUNCTIONS_REGISTRY.register("lesser_sky_attunement_reduces_fall_damage_taken", () -> AttunementTests::lesser_sky_attunement_reduces_fall_damage_taken);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GREATER_SKY_ATTUNEMENT_BUFF = Services.TEST_FUNCTIONS_REGISTRY.register("greater_sky_attunement_increases_jump_strength", () -> AttunementTests::greater_sky_attunement_increases_jump_strength);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LESSER_SUN_ATTUNEMENT_DAY_BUFF = Services.TEST_FUNCTIONS_REGISTRY.register("lesser_sun_attunement_regenerates_food_during_day", () -> AttunementTests::lesser_sun_attunement_regenerates_food_during_day);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LESSER_SUN_ATTUNEMENT_NIGHT_BUFF = Services.TEST_FUNCTIONS_REGISTRY.register("lesser_sun_attunement_does_not_regenerate_food_during_night", () -> AttunementTests::lesser_sun_attunement_does_not_regenerate_food_during_night);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LESSER_MOON_ATTUNEMENT_BUFF = Services.TEST_FUNCTIONS_REGISTRY.register("lesser_moon_attunement_grants_invisibility_chance_on_hurt", () -> AttunementTests::lesser_moon_attunement_grants_invisibility_chance_on_hurt);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GREATER_MOON_ATTUNEMENT_BUFF = Services.TEST_FUNCTIONS_REGISTRY.register("greater_moon_attunement_grants_night_vision", () -> AttunementTests::greater_moon_attunement_grants_night_vision);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LESSER_BLOOD_ATTUNEMENT_BUFF = Services.TEST_FUNCTIONS_REGISTRY.register("lesser_blood_attunement_inflicts_bleeding", () -> AttunementTests::lesser_blood_attunement_inflicts_bleeding);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GREATER_BLOOD_ATTUNEMENT_BUFF = Services.TEST_FUNCTIONS_REGISTRY.register("greater_blood_attunement_grants_chance_at_self_healing", () -> AttunementTests::greater_blood_attunement_grants_chance_at_self_healing);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GREATER_INFERNAL_ATTUNEMENT_BUFF_IN_FIRE = Services.TEST_FUNCTIONS_REGISTRY.register("greater_infernal_attunement_prevents_fire_damage_in_fire", () -> (helper) -> AttunementTests.greater_infernal_attunement_prevents_fire_damage(helper, registryAccess -> new DamageSources(registryAccess).inFire()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GREATER_INFERNAL_ATTUNEMENT_BUFF_ON_FIRE = Services.TEST_FUNCTIONS_REGISTRY.register("greater_infernal_attunement_prevents_fire_damage_on_fire", () -> (helper) -> AttunementTests.greater_infernal_attunement_prevents_fire_damage(helper, registryAccess -> new DamageSources(registryAccess).onFire()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GREATER_INFERNAL_ATTUNEMENT_BUFF_LAVA = Services.TEST_FUNCTIONS_REGISTRY.register("greater_infernal_attunement_prevents_fire_damage_lava", () -> (helper) -> AttunementTests.greater_infernal_attunement_prevents_fire_damage(helper, registryAccess -> new DamageSources(registryAccess).lava()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GREATER_INFERNAL_ATTUNEMENT_BUFF_HOT_FLOOR = Services.TEST_FUNCTIONS_REGISTRY.register("greater_infernal_attunement_prevents_fire_damage_hot_floor", () -> (helper) -> AttunementTests.greater_infernal_attunement_prevents_fire_damage(helper, registryAccess -> new DamageSources(registryAccess).hotFloor()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GREATER_INFERNAL_ATTUNEMENT_BUFF_INFERNAL_SORCERY = Services.TEST_FUNCTIONS_REGISTRY.register("greater_infernal_attunement_prevents_fire_damage_infernal_sorcery", () -> (helper) -> AttunementTests.greater_infernal_attunement_prevents_fire_damage(helper, registryAccess -> DamageSourcesPM.sorcery(registryAccess, Sources.INFERNAL, null)));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LESSER_VOID_ATTUNEMENT_BUFF = Services.TEST_FUNCTIONS_REGISTRY.register("lesser_void_attunement_reduces_damage_taken", () -> AttunementTests::lesser_void_attunement_reduces_damage_taken);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GREATER_VOID_ATTUNEMENT_BUFF = Services.TEST_FUNCTIONS_REGISTRY.register("greater_void_attunement_increases_damage_dealt", () -> AttunementTests::greater_void_attunement_increases_damage_dealt);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LESSER_HALLOWED_ATTUNEMENT_BUFF = Services.TEST_FUNCTIONS_REGISTRY.register("lesser_hallowed_attunement_doubles_damage_dealt_to_undead", () -> AttunementTests::lesser_hallowed_attunement_doubles_damage_dealt_to_undead);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GREATER_HALLOWED_ATTUNEMENT_BUFF = Services.TEST_FUNCTIONS_REGISTRY.register("greater_hallowed_attunement_prevents_death", () -> AttunementTests::greater_hallowed_attunement_prevents_death);

    // Item handler tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> ITEM_HANDLER_NULL_DIRECTION_RESEARCH_TABLE = Services.TEST_FUNCTIONS_REGISTRY.register("block_entity_can_retrieve_item_handler_with_null_direction_research_table", () -> (helper) -> ItemHandlerTests.block_entity_can_retrieve_item_handler_with_null_direction(helper, BlocksPM.RESEARCH_TABLE.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> ITEM_HANDLER_NULL_DIRECTION_WAND_CHARGER = Services.TEST_FUNCTIONS_REGISTRY.register("block_entity_can_retrieve_item_handler_with_null_direction_wand_charger", () -> (helper) -> ItemHandlerTests.block_entity_can_retrieve_item_handler_with_null_direction(helper, BlocksPM.WAND_CHARGER.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> ITEM_HANDLER_NULL_DIRECTION_CALCINATOR_BASIC = Services.TEST_FUNCTIONS_REGISTRY.register("block_entity_can_retrieve_item_handler_with_null_direction_calcinator_basic", () -> (helper) -> ItemHandlerTests.block_entity_can_retrieve_item_handler_with_null_direction(helper, BlocksPM.CALCINATOR_BASIC.get()));

    // Player knowledge tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> ADD_AND_CHECK_RESEARCH = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_add_and_check_research", () -> PlayerKnowledgeTests::player_knowledge_add_and_check_research);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CANNOT_ADD_DUPLICATE_RESEARCH = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_cannot_add_duplicate_research", () -> PlayerKnowledgeTests::player_knowledge_cannot_add_duplicate_research);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> REMOVE_RESEARCH = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_remove_research", () -> PlayerKnowledgeTests::player_knowledge_remove_research);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GET_RESEARCH_SET = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_get_research_set", () -> PlayerKnowledgeTests::player_knowledge_get_research_set);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GET_SET_RESEARCH_STAGE = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_get_set_research_stage", () -> PlayerKnowledgeTests::player_knowledge_get_set_research_stage);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GET_SET_RESEARCH_FLAG = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_get_set_research_flag", () -> PlayerKnowledgeTests::player_knowledge_get_set_research_flag);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> REMOVE_RESEARCH_FLAG = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_remove_research_flag", () -> PlayerKnowledgeTests::player_knowledge_remove_research_flag);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GET_RESEARCH_FLAGS = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_get_research_flags", () -> PlayerKnowledgeTests::player_knowledge_get_research_flags);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GET_RESEARCH_STATUS = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_get_research_status", () -> PlayerKnowledgeTests::player_knowledge_get_research_status);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> IS_RESEARCH_COMPLETE = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_is_research_complete", () -> PlayerKnowledgeTests::player_knowledge_is_research_complete);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GET_SET_KNOWLEDGE_RAW = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_get_set_knowledge_raw", () -> PlayerKnowledgeTests::player_knowledge_get_set_knowledge_raw);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GET_KNOWLEDGE_LEVELS = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_get_knowledge_levels", () -> PlayerKnowledgeTests::player_knowledge_get_knowledge_levels);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GET_SET_ACTIVE_RESEARCH_PROJECT = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_get_set_active_research_project", () -> PlayerKnowledgeTests::player_knowledge_get_set_active_research_project);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GET_SET_LAST_RESEARCH_TOPIC = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_get_set_last_research_topic", () -> PlayerKnowledgeTests::player_knowledge_get_set_last_research_topic);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> GET_SET_RESEARCH_TOPIC_HISTORY = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_get_set_research_topic_history", () -> PlayerKnowledgeTests::player_knowledge_get_set_research_topic_history);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> KNOWLEDGE_SERIALIZATION = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_serialization", () -> PlayerKnowledgeTests::player_knowledge_serialization);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> ADD_AND_CHECK_RESEARCH_POST_SERIALIZATION = Services.TEST_FUNCTIONS_REGISTRY.register("player_knowledge_add_and_check_research_post_serialization", () -> PlayerKnowledgeTests::player_knowledge_add_and_check_research_post_serialization);

    // Arcane workbench tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> ARCANE_WORKBENCH_CRAFT_WORKS = Services.TEST_FUNCTIONS_REGISTRY.register("arcane_workbench_craft_works", () -> ArcaneWorkbenchTests::arcane_workbench_craft_works);

    // Calcinator tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CALCINATOR_WORKS_WITH_PLAYER_PRESENT = Services.TEST_FUNCTIONS_REGISTRY.register("calcinator_works_with_player_present", () -> (helper) -> CalcinatorTests.calcinator_works(helper, true));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CALCINATOR_WORKS_WITHOUT_PLAYER_PRESENT = Services.TEST_FUNCTIONS_REGISTRY.register("calcinator_works_without_player_present", () -> (helper) -> CalcinatorTests.calcinator_works(helper, false));

    // Crafting requirement tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CRAFTING_REQUIREMENT_ARCANE_RECIPE = Services.TEST_FUNCTIONS_REGISTRY.register("crafting_requirement_arcane_recipe", () -> CraftingRequirementsTests::arcane_recipe);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CRAFTING_REQUIREMENT_RITUAL_RECIPE = Services.TEST_FUNCTIONS_REGISTRY.register("crafting_requirement_ritual_recipe", () -> CraftingRequirementsTests::ritual_recipe);

    // Repair tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> EARTHSHATTER_HAMMER_CANNOT_BE_REPAIRED = Services.TEST_FUNCTIONS_REGISTRY.register("earthshatter_hammer_cannot_be_repaired", () -> RepairTests::earthshatter_hammer_cannot_be_repaired);

    // Runecarving tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RUNECARVING_CRAFT_WORKS = Services.TEST_FUNCTIONS_REGISTRY.register("runecarving_craft_works", () -> RunecarvingTests::craft_works);

    // Caster enchanting tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CASTER_ENCHANTABLE_MUNDANE_WAND = Services.TEST_FUNCTIONS_REGISTRY.register("caster_enchantable_mundane_wand", () -> (helper) -> CasterEnchantingTests.caster_can_be_enchanted(helper, ItemsPM.MUNDANE_WAND.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CASTER_ENCHANTABLE_MODULAR_WAND = Services.TEST_FUNCTIONS_REGISTRY.register("caster_enchantable_modular_wand", () -> (helper) -> CasterEnchantingTests.caster_can_be_enchanted(helper, ItemsPM.MODULAR_WAND.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CASTER_ENCHANTABLE_MODULAR_STAFF = Services.TEST_FUNCTIONS_REGISTRY.register("caster_enchantable_modular_staff", () -> (helper) -> CasterEnchantingTests.caster_can_be_enchanted(helper, ItemsPM.MODULAR_STAFF.get()));

    // Ritual enchantment tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> ENCHANTMENT_ESSENCE_THIEF1 = Services.TEST_FUNCTIONS_REGISTRY.register("enchantment_essence_thief1", () -> (helper) -> RitualEnchantmentTests.enchantment_essence_thief(helper, 1));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> ENCHANTMENT_ESSENCE_THIEF2 = Services.TEST_FUNCTIONS_REGISTRY.register("enchantment_essence_thief2", () -> (helper) -> RitualEnchantmentTests.enchantment_essence_thief(helper, 2));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> ENCHANTMENT_ESSENCE_THIEF3 = Services.TEST_FUNCTIONS_REGISTRY.register("enchantment_essence_thief3", () -> (helper) -> RitualEnchantmentTests.enchantment_essence_thief(helper, 3));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> ENCHANTMENT_ESSENCE_THIEF4 = Services.TEST_FUNCTIONS_REGISTRY.register("enchantment_essence_thief4", () -> (helper) -> RitualEnchantmentTests.enchantment_essence_thief(helper, 4));

    // FTUX tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> FONT_DISCOVERY_EARTH = Services.TEST_FUNCTIONS_REGISTRY.register("font_discovery_earth", () -> (helper) -> FtuxTests.font_discovery(helper, BlocksPM.ANCIENT_FONT_EARTH.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> FONT_DISCOVERY_SEA = Services.TEST_FUNCTIONS_REGISTRY.register("font_discovery_sea", () -> (helper) -> FtuxTests.font_discovery(helper, BlocksPM.ANCIENT_FONT_SEA.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> FONT_DISCOVERY_SKY = Services.TEST_FUNCTIONS_REGISTRY.register("font_discovery_sky", () -> (helper) -> FtuxTests.font_discovery(helper, BlocksPM.ANCIENT_FONT_SKY.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> FONT_DISCOVERY_SUN = Services.TEST_FUNCTIONS_REGISTRY.register("font_discovery_sun", () -> (helper) -> FtuxTests.font_discovery(helper, BlocksPM.ANCIENT_FONT_SUN.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> FONT_DISCOVERY_MOON = Services.TEST_FUNCTIONS_REGISTRY.register("font_discovery_moon", () -> (helper) -> FtuxTests.font_discovery(helper, BlocksPM.ANCIENT_FONT_MOON.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> SLEEP_AFTER_SHRINE_GRANTS_DREAM = Services.TEST_FUNCTIONS_REGISTRY.register("sleeping_after_shrine_grants_dream", () -> FtuxTests::sleeping_after_shrine_grants_dream);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MUNDANE_WAND_CRAFTING_EARTH = Services.TEST_FUNCTIONS_REGISTRY.register("mundane_wand_crafting_earth", () -> (helper) -> FtuxTests.mundane_wand_crafting(helper, ItemsPM.ESSENCE_DUST_EARTH.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MUNDANE_WAND_CRAFTING_SEA = Services.TEST_FUNCTIONS_REGISTRY.register("mundane_wand_crafting_sea", () -> (helper) -> FtuxTests.mundane_wand_crafting(helper, ItemsPM.ESSENCE_DUST_SEA.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MUNDANE_WAND_CRAFTING_SKY = Services.TEST_FUNCTIONS_REGISTRY.register("mundane_wand_crafting_sky", () -> (helper) -> FtuxTests.mundane_wand_crafting(helper, ItemsPM.ESSENCE_DUST_SKY.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MUNDANE_WAND_CRAFTING_SUN = Services.TEST_FUNCTIONS_REGISTRY.register("mundane_wand_crafting_sun", () -> (helper) -> FtuxTests.mundane_wand_crafting(helper, ItemsPM.ESSENCE_DUST_SUN.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MUNDANE_WAND_CRAFTING_MOON = Services.TEST_FUNCTIONS_REGISTRY.register("mundane_wand_crafting_moon", () -> (helper) -> FtuxTests.mundane_wand_crafting(helper, ItemsPM.ESSENCE_DUST_MOON.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TRANSFORM_ABORT_GIVES_HINT = Services.TEST_FUNCTIONS_REGISTRY.register("transform_abort_gives_hint", () -> FtuxTests::transform_abort_gives_hint);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TRANSFORM_WITHOUT_DREAM_DOES_NOTHING = Services.TEST_FUNCTIONS_REGISTRY.register("transform_without_dream_does_nothing", () -> FtuxTests::transform_without_dream_does_nothing);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> TRANSFORM_GRIMOIRE = Services.TEST_FUNCTIONS_REGISTRY.register("transform_grimoire", () -> FtuxTests::transform_grimoire);

    // Beeswax item tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> APPLY_BEESWAX_DIRECTLY_CLEAN = Services.TEST_FUNCTIONS_REGISTRY.register("apply_beeswax_directly_clean", () -> (helper) -> BeeswaxTests.apply_beeswax_directly(helper, Blocks.COPPER_BLOCK, Blocks.WAXED_COPPER_BLOCK));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> APPLY_BEESWAX_DIRECTLY_EXPOSED = Services.TEST_FUNCTIONS_REGISTRY.register("apply_beeswax_directly_exposed", () -> (helper) -> BeeswaxTests.apply_beeswax_directly(helper, Blocks.EXPOSED_COPPER, Blocks.WAXED_EXPOSED_COPPER));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> APPLY_BEESWAX_DIRECTLY_WEATHERED = Services.TEST_FUNCTIONS_REGISTRY.register("apply_beeswax_directly_weathered", () -> (helper) -> BeeswaxTests.apply_beeswax_directly(helper, Blocks.WEATHERED_COPPER, Blocks.WAXED_WEATHERED_COPPER));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> APPLY_BEESWAX_DIRECTLY_OXIDIZED = Services.TEST_FUNCTIONS_REGISTRY.register("apply_beeswax_directly_oxidized", () -> (helper) -> BeeswaxTests.apply_beeswax_directly(helper, Blocks.OXIDIZED_COPPER, Blocks.WAXED_OXIDIZED_COPPER));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> APPLY_BEESWAX_VIA_CRAFTING_CLEAN = Services.TEST_FUNCTIONS_REGISTRY.register("apply_beeswax_via_crafting_clean", () -> (helper) -> BeeswaxTests.apply_beeswax_via_crafting(helper, Blocks.COPPER_BLOCK, Blocks.WAXED_COPPER_BLOCK));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> APPLY_BEESWAX_VIA_CRAFTING_EXPOSED = Services.TEST_FUNCTIONS_REGISTRY.register("apply_beeswax_via_crafting_exposed", () -> (helper) -> BeeswaxTests.apply_beeswax_via_crafting(helper, Blocks.EXPOSED_COPPER, Blocks.WAXED_EXPOSED_COPPER));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> APPLY_BEESWAX_VIA_CRAFTING_WEATHERED = Services.TEST_FUNCTIONS_REGISTRY.register("apply_beeswax_via_crafting_weathered", () -> (helper) -> BeeswaxTests.apply_beeswax_via_crafting(helper, Blocks.WEATHERED_COPPER, Blocks.WAXED_WEATHERED_COPPER));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> APPLY_BEESWAX_VIA_CRAFTING_OXIDIZED = Services.TEST_FUNCTIONS_REGISTRY.register("apply_beeswax_via_crafting_oxidized", () -> (helper) -> BeeswaxTests.apply_beeswax_via_crafting(helper, Blocks.OXIDIZED_COPPER, Blocks.WAXED_OXIDIZED_COPPER));

    // Dispenser item tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_ARROWS_FIRED_FROM_DISPENSER_EARTH = Services.TEST_FUNCTIONS_REGISTRY.register("mana_arrows_fired_from_dispenser_earth", () -> (helper) -> DispenserTests.mana_arrows_fired_from_dispenser(helper, ItemsPM.MANA_ARROW_EARTH.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_ARROWS_FIRED_FROM_DISPENSER_SEA = Services.TEST_FUNCTIONS_REGISTRY.register("mana_arrows_fired_from_dispenser_sea", () -> (helper) -> DispenserTests.mana_arrows_fired_from_dispenser(helper, ItemsPM.MANA_ARROW_SEA.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_ARROWS_FIRED_FROM_DISPENSER_SKY = Services.TEST_FUNCTIONS_REGISTRY.register("mana_arrows_fired_from_dispenser_sky", () -> (helper) -> DispenserTests.mana_arrows_fired_from_dispenser(helper, ItemsPM.MANA_ARROW_SKY.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_ARROWS_FIRED_FROM_DISPENSER_SUN = Services.TEST_FUNCTIONS_REGISTRY.register("mana_arrows_fired_from_dispenser_sun", () -> (helper) -> DispenserTests.mana_arrows_fired_from_dispenser(helper, ItemsPM.MANA_ARROW_SUN.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_ARROWS_FIRED_FROM_DISPENSER_MOON = Services.TEST_FUNCTIONS_REGISTRY.register("mana_arrows_fired_from_dispenser_moon", () -> (helper) -> DispenserTests.mana_arrows_fired_from_dispenser(helper, ItemsPM.MANA_ARROW_MOON.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_ARROWS_FIRED_FROM_DISPENSER_BLOOD = Services.TEST_FUNCTIONS_REGISTRY.register("mana_arrows_fired_from_dispenser_blood", () -> (helper) -> DispenserTests.mana_arrows_fired_from_dispenser(helper, ItemsPM.MANA_ARROW_BLOOD.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_ARROWS_FIRED_FROM_DISPENSER_INFERNAL = Services.TEST_FUNCTIONS_REGISTRY.register("mana_arrows_fired_from_dispenser_infernal", () -> (helper) -> DispenserTests.mana_arrows_fired_from_dispenser(helper, ItemsPM.MANA_ARROW_INFERNAL.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_ARROWS_FIRED_FROM_DISPENSER_VOID = Services.TEST_FUNCTIONS_REGISTRY.register("mana_arrows_fired_from_dispenser_void", () -> (helper) -> DispenserTests.mana_arrows_fired_from_dispenser(helper, ItemsPM.MANA_ARROW_VOID.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_ARROWS_FIRED_FROM_DISPENSER_HALLOWED = Services.TEST_FUNCTIONS_REGISTRY.register("mana_arrows_fired_from_dispenser_hallowed", () -> (helper) -> DispenserTests.mana_arrows_fired_from_dispenser(helper, ItemsPM.MANA_ARROW_HALLOWED.get()));

    // Wand mana tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CAN_GET_AND_ADD_MANA = Services.TEST_FUNCTIONS_REGISTRY.register("wand_can_get_and_add_mana", () -> WandManaTests::wand_can_get_and_add_mana);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CAN_GET_AND_ADD_REAL_MANA = Services.TEST_FUNCTIONS_REGISTRY.register("wand_can_get_and_add_real_mana", () -> WandManaTests::wand_can_get_and_add_real_mana);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CANNOT_ADD_TOO_MUCH_MANA = Services.TEST_FUNCTIONS_REGISTRY.register("wand_cannot_add_too_much_mana", () -> WandManaTests::wand_cannot_add_too_much_mana);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CAN_GET_ALL_MANA = Services.TEST_FUNCTIONS_REGISTRY.register("wand_can_get_all_mana", () -> WandManaTests::wand_can_get_all_mana);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CAN_CONSUME_MANA = Services.TEST_FUNCTIONS_REGISTRY.register("wand_can_consume_mana", () -> WandManaTests::wand_can_consume_mana);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CANNOT_CONSUME_MORE_MANA_THAN_IT_HAS = Services.TEST_FUNCTIONS_REGISTRY.register("wand_cannot_consume_more_mana_than_it_has", () -> WandManaTests::wand_cannot_consume_more_mana_than_it_has);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CAN_CONSUME_MULTIPLE_TYPES_OF_MANA = Services.TEST_FUNCTIONS_REGISTRY.register("wand_can_consume_multiple_types_of_mana", () -> WandManaTests::wand_can_consume_multiple_types_of_mana);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CANNOT_CONSUME_MORE_MANA_THAN_IT_HAS_WITH_MULTIPLE_TYPES = Services.TEST_FUNCTIONS_REGISTRY.register("wand_cannot_consume_more_mana_than_it_has_with_multiple_types", () -> WandManaTests::wand_cannot_consume_more_mana_than_it_has_with_multiple_types);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CAN_REMOVE_MANA_RAW = Services.TEST_FUNCTIONS_REGISTRY.register("wand_can_remove_mana_raw", () -> WandManaTests::wand_can_remove_mana_raw);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CANNOT_REMOVE_MORE_RAW_MANA_THAN_IT_HAS = Services.TEST_FUNCTIONS_REGISTRY.register("wand_cannot_remove_more_raw_mana_than_it_has", () -> WandManaTests::wand_cannot_remove_more_raw_mana_than_it_has);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CONTAINS_MANA = Services.TEST_FUNCTIONS_REGISTRY.register("wand_contains_mana", () -> WandManaTests::wand_contains_mana);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CONTAINS_MANA_LIST = Services.TEST_FUNCTIONS_REGISTRY.register("wand_contains_mana_list", () -> WandManaTests::wand_contains_mana_list);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CONTAINS_MANA_RAW = Services.TEST_FUNCTIONS_REGISTRY.register("wand_contains_mana_raw", () -> WandManaTests::wand_contains_mana_raw);

    // Research key tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_KEY_DISCIPLINE = Services.TEST_FUNCTIONS_REGISTRY.register("research_key_discipline", () -> ResearchKeysTests::research_discipline);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_KEY_ENTRY = Services.TEST_FUNCTIONS_REGISTRY.register("research_key_entry", () -> ResearchKeysTests::research_entry);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_KEY_STAGE = Services.TEST_FUNCTIONS_REGISTRY.register("research_key_stage", () -> ResearchKeysTests::research_stage);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_KEY_ITEM_SCAN = Services.TEST_FUNCTIONS_REGISTRY.register("research_key_item_scan", () -> ResearchKeysTests::item_scan);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_KEY_ENTITY_SCAN = Services.TEST_FUNCTIONS_REGISTRY.register("research_key_entity_scan", () -> ResearchKeysTests::entity_scan);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_KEY_STACK_CRAFTED = Services.TEST_FUNCTIONS_REGISTRY.register("research_key_stack_crafted", () -> ResearchKeysTests::stack_crafted);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_KEY_TAG_CRAFTED = Services.TEST_FUNCTIONS_REGISTRY.register("research_key_tag_crafted", () -> ResearchKeysTests::tag_crafted);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_KEY_RUNE_ENCHANTMENT = Services.TEST_FUNCTIONS_REGISTRY.register("research_key_rune_enchantment", () -> ResearchKeysTests::rune_enchantment);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_KEY_RUNE_ENCHANTMENT_PARTIAL = Services.TEST_FUNCTIONS_REGISTRY.register("research_key_rune_enchantment_partial", () -> ResearchKeysTests::rune_enchantment_partial);

    // Research requirement tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_REQUIREMENT_RESEARCH = Services.TEST_FUNCTIONS_REGISTRY.register("research_requirement_research", () -> ResearchRequirementsTests::research_requirement);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_REQUIREMENT_KNOWLEDGE = Services.TEST_FUNCTIONS_REGISTRY.register("research_requirement_knowledge", () -> ResearchRequirementsTests::knowledge_requirement);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_REQUIREMENT_ITEM_STACK = Services.TEST_FUNCTIONS_REGISTRY.register("research_requirement_item_stack", () -> ResearchRequirementsTests::item_stack_requirement);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_REQUIREMENT_ITEM_TAG = Services.TEST_FUNCTIONS_REGISTRY.register("research_requirement_item_tag", () -> ResearchRequirementsTests::item_tag_requirement);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_REQUIREMENT_STAT = Services.TEST_FUNCTIONS_REGISTRY.register("research_requirement_stat", () -> ResearchRequirementsTests::stat_requirement);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_REQUIREMENT_EXPERTISE = Services.TEST_FUNCTIONS_REGISTRY.register("research_requirement_expertise", () -> ResearchRequirementsTests::expertise_requirement);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_REQUIREMENT_VANILLA_ITEM_USED_STAT = Services.TEST_FUNCTIONS_REGISTRY.register("research_requirement_vanilla_item_used_stat", () -> ResearchRequirementsTests::vanilla_item_used_stat_requirement);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_REQUIREMENT_VANILLA_CUSTOM_STAT = Services.TEST_FUNCTIONS_REGISTRY.register("research_requirement_vanilla_custom_stat", () -> ResearchRequirementsTests::vanilla_custom_stat_requirement);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_REQUIREMENT_AND = Services.TEST_FUNCTIONS_REGISTRY.register("research_requirement_and", () -> ResearchRequirementsTests::and_requirement);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_REQUIREMENT_OR = Services.TEST_FUNCTIONS_REGISTRY.register("research_requirement_or", () -> ResearchRequirementsTests::or_requirement);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_REQUIREMENT_QUORUM = Services.TEST_FUNCTIONS_REGISTRY.register("research_requirement_quorum", () -> ResearchRequirementsTests::quorum_requirement);

    // Research system tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> RESEARCH_GRANT_WORKS = Services.TEST_FUNCTIONS_REGISTRY.register("research_grant_works", () -> ResearchTests::research_grant_works);

    // Spell tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DAMAGE_SPELLS_WORK_EARTH = Services.TEST_FUNCTIONS_REGISTRY.register("damage_spells_work_earth", () -> (helper) -> WandSpellcastTests.damage_spells_deduct_mana_from_wand_and_award_expertise(helper, SpellPayloadsPM.EARTH_DAMAGE.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DAMAGE_SPELLS_WORK_SEA = Services.TEST_FUNCTIONS_REGISTRY.register("damage_spells_work_sea", () -> (helper) -> WandSpellcastTests.damage_spells_deduct_mana_from_wand_and_award_expertise(helper, SpellPayloadsPM.FROST_DAMAGE.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DAMAGE_SPELLS_WORK_SKY = Services.TEST_FUNCTIONS_REGISTRY.register("damage_spells_work_sky", () -> (helper) -> WandSpellcastTests.damage_spells_deduct_mana_from_wand_and_award_expertise(helper, SpellPayloadsPM.LIGHTNING_DAMAGE.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DAMAGE_SPELLS_WORK_SUN = Services.TEST_FUNCTIONS_REGISTRY.register("damage_spells_work_sun", () -> (helper) -> WandSpellcastTests.damage_spells_deduct_mana_from_wand_and_award_expertise(helper, SpellPayloadsPM.SOLAR_DAMAGE.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DAMAGE_SPELLS_WORK_MOON = Services.TEST_FUNCTIONS_REGISTRY.register("damage_spells_work_moon", () -> (helper) -> WandSpellcastTests.damage_spells_deduct_mana_from_wand_and_award_expertise(helper, SpellPayloadsPM.LUNAR_DAMAGE.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DAMAGE_SPELLS_WORK_BLOOD = Services.TEST_FUNCTIONS_REGISTRY.register("damage_spells_work_blood", () -> (helper) -> WandSpellcastTests.damage_spells_deduct_mana_from_wand_and_award_expertise(helper, SpellPayloadsPM.BLOOD_DAMAGE.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DAMAGE_SPELLS_WORK_INFERNAL = Services.TEST_FUNCTIONS_REGISTRY.register("damage_spells_work_infernal", () -> (helper) -> WandSpellcastTests.damage_spells_deduct_mana_from_wand_and_award_expertise(helper, SpellPayloadsPM.FLAME_DAMAGE.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DAMAGE_SPELLS_WORK_VOID = Services.TEST_FUNCTIONS_REGISTRY.register("damage_spells_work_void", () -> (helper) -> WandSpellcastTests.damage_spells_deduct_mana_from_wand_and_award_expertise(helper, SpellPayloadsPM.VOID_DAMAGE.get()));
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DAMAGE_SPELLS_WORK_HALLOWED = Services.TEST_FUNCTIONS_REGISTRY.register("damage_spells_work_hallowed", () -> (helper) -> WandSpellcastTests.damage_spells_deduct_mana_from_wand_and_award_expertise(helper, SpellPayloadsPM.HOLY_DAMAGE.get()));

    // Auto charger tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> AUTO_CHARGER_OUTPUT_ALLOWS_CHARGEABLE_ITEMS = Services.TEST_FUNCTIONS_REGISTRY.register("auto_charger_output_allows_chargeable_items", () -> AutoChargerTests::auto_charger_output_allows_chargeable_items);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> AUTO_CHARGER_OUTPUT_DOES_NOT_ALLOW_UNCHARGEABLE_ITEMS = Services.TEST_FUNCTIONS_REGISTRY.register("auto_charger_output_does_not_allow_unchargeable_items", () -> AutoChargerTests::auto_charger_output_does_not_allow_unchargeable_items);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> AUTO_CHARGER_CAN_HAVE_CHARGEABLE_ITEMS_INSERTED = Services.TEST_FUNCTIONS_REGISTRY.register("auto_charger_can_have_chargeable_items_inserted", () -> AutoChargerTests::auto_charger_can_have_chargeable_items_inserted);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> AUTO_CHARGER_CANNOT_HAVE_UNCHARGEABLE_ITEMS_INSERTED = Services.TEST_FUNCTIONS_REGISTRY.register("auto_charger_cannot_have_unchargeable_items_inserted", () -> AutoChargerTests::auto_charger_cannot_have_unchargeable_items_inserted);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> AUTO_CHARGER_CAN_HAVE_CHARGEABLE_ITEMS_REMOVED = Services.TEST_FUNCTIONS_REGISTRY.register("auto_charger_can_have_chargeable_items_removed", () -> AutoChargerTests::auto_charger_can_have_chargeable_items_removed);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> AUTO_CHARGER_SIPHONS_INTO_CHARGEABLE_ITEMS = Services.TEST_FUNCTIONS_REGISTRY.register("auto_charger_siphons_into_chargeable_items", () -> AutoChargerTests::auto_charger_siphons_into_chargeable_items);

    // Mana battery tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_BATTERY_CAN_HAVE_ITS_MENU_OPENED = Services.TEST_FUNCTIONS_REGISTRY.register("mana_battery_can_have_its_menu_opened", () -> ManaBatteryTests::mana_battery_can_have_its_menu_opened);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_BATTERY_OUTPUT_ALLOWS_CHARGEABLE_ITEMS = Services.TEST_FUNCTIONS_REGISTRY.register("mana_battery_output_allows_chargeable_items", () -> ManaBatteryTests::mana_battery_output_allows_chargeable_items);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_BATTERY_OUTPUT_DOES_NOT_ALLOW_UNCHARGEABLE_ITEMS = Services.TEST_FUNCTIONS_REGISTRY.register("mana_battery_output_does_not_allow_unchargeable_items", () -> ManaBatteryTests::mana_battery_output_does_not_allow_unchargeable_items);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_BATTERY_INPUT_ALLOWS_ESSENCE = Services.TEST_FUNCTIONS_REGISTRY.register("mana_battery_input_allows_essence", () -> ManaBatteryTests::mana_battery_input_allows_essence);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_BATTERY_INPUT_ALLOWS_WANDS = Services.TEST_FUNCTIONS_REGISTRY.register("mana_battery_input_allows_wands", () -> ManaBatteryTests::mana_battery_input_allows_wands);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_BATTERY_SIPHONS_FROM_NEARBY_FONTS = Services.TEST_FUNCTIONS_REGISTRY.register("mana_battery_siphons_from_nearby_fonts", () -> ManaBatteryTests::mana_battery_siphons_from_nearby_fonts);

    // Mana font tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_FONT_SIPHONED_BY_WAND = Services.TEST_FUNCTIONS_REGISTRY.register("mana_font_siphoned_by_wand", () -> ManaFontTests::mana_font_siphoned_by_wand);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MANA_FONT_RECHARGES = Services.TEST_FUNCTIONS_REGISTRY.register("mana_font_recharges", () -> ManaFontTests::mana_font_recharges);

    // Wand charger tests
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CHARGER_CAN_HAVE_ITS_MENU_OPENED = Services.TEST_FUNCTIONS_REGISTRY.register("wand_charger_can_have_its_menu_opened", () -> WandChargerTests::wand_charger_can_have_its_menu_opened);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CHARGER_OUTPUT_ALLOWS_CHARGEABLE_ITEMS = Services.TEST_FUNCTIONS_REGISTRY.register("wand_charger_output_allows_chargeable_items", () -> WandChargerTests::wand_charger_output_allows_chargeable_items);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CHARGER_OUTPUT_DOES_NOT_ALLOW_UNCHARGEABLE_ITEMS = Services.TEST_FUNCTIONS_REGISTRY.register("wand_charger_output_does_not_allow_unchargeable_items", () -> WandChargerTests::wand_charger_output_does_not_allow_unchargeable_items);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CHARGER_INPUT_ALLOWS_ESSENCE = Services.TEST_FUNCTIONS_REGISTRY.register("wand_charger_input_allows_essence", () -> WandChargerTests::wand_charger_input_allows_essence);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CHARGER_INPUT_DOES_NOT_ALLOW_NON_ESSENCE = Services.TEST_FUNCTIONS_REGISTRY.register("wand_charger_input_does_not_allow_non_essence", () -> WandChargerTests::wand_charger_input_does_not_allow_non_essence);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CHARGER_CAN_CHARGE_WITH_RIGHT_ITEMS = Services.TEST_FUNCTIONS_REGISTRY.register("wand_charger_can_charge_with_right_items", () -> WandChargerTests::wand_charger_can_charge_with_right_items);
    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> WAND_CHARGER_DO_CHARGE_WITH_RIGHT_ITEMS = Services.TEST_FUNCTIONS_REGISTRY.register("wand_charger_do_charge_with_right_items", () -> WandChargerTests::wand_charger_do_charge_with_right_items);
}

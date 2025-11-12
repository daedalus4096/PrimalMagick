package com.verdantartifice.primalmagick.test;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class TestInstancesPM {
    public static final ResourceKey<GameTestInstance> CANARY = createInstanceKey("canary");

    // Attunement buff tests
    public static final ResourceKey<GameTestInstance> MINOR_ATTUNEMENT_DISCOUNT_EARTH = createInstanceKey("minor_attunement_discount_earth");
    public static final ResourceKey<GameTestInstance> MINOR_ATTUNEMENT_DISCOUNT_SEA = createInstanceKey("minor_attunement_discount_sea");
    public static final ResourceKey<GameTestInstance> MINOR_ATTUNEMENT_DISCOUNT_SKY = createInstanceKey("minor_attunement_discount_sky");
    public static final ResourceKey<GameTestInstance> MINOR_ATTUNEMENT_DISCOUNT_SUN = createInstanceKey("minor_attunement_discount_sun");
    public static final ResourceKey<GameTestInstance> MINOR_ATTUNEMENT_DISCOUNT_MOON = createInstanceKey("minor_attunement_discount_moon");
    public static final ResourceKey<GameTestInstance> MINOR_ATTUNEMENT_DISCOUNT_BLOOD = createInstanceKey("minor_attunement_discount_blood");
    public static final ResourceKey<GameTestInstance> MINOR_ATTUNEMENT_DISCOUNT_INFERNAL = createInstanceKey("minor_attunement_discount_infernal");
    public static final ResourceKey<GameTestInstance> MINOR_ATTUNEMENT_DISCOUNT_VOID = createInstanceKey("minor_attunement_discount_void");
    public static final ResourceKey<GameTestInstance> MINOR_ATTUNEMENT_DISCOUNT_HALLOWED = createInstanceKey("minor_attunement_discount_hallowed");
    public static final ResourceKey<GameTestInstance> LESSER_EARTH_ATTUNEMENT_BUFF = createInstanceKey("lesser_earth_attunement_buff");
    public static final ResourceKey<GameTestInstance> GREATER_EARTH_ATTUNEMENT_BUFF = createInstanceKey("greater_earth_attunement_buff");
    public static final ResourceKey<GameTestInstance> LESSER_SEA_ATTUNEMENT_BUFF = createInstanceKey("lesser_sea_attunement_buff");
    public static final ResourceKey<GameTestInstance> GREATER_SEA_ATTUNEMENT_BUFF = createInstanceKey("greater_sea_attunement_buff");
    public static final ResourceKey<GameTestInstance> LESSER_SKY_ATTUNEMENT_BUFF1 = createInstanceKey("lesser_sky_attunement_buff1");
    public static final ResourceKey<GameTestInstance> LESSER_SKY_ATTUNEMENT_BUFF2 = createInstanceKey("lesser_sky_attunement_buff2");
    public static final ResourceKey<GameTestInstance> GREATER_SKY_ATTUNEMENT_BUFF = createInstanceKey("greater_sky_attunement_buff");
    public static final ResourceKey<GameTestInstance> LESSER_SUN_ATTUNEMENT_DAY_BUFF = createInstanceKey("lesser_sun_attunement_day_buff");
    public static final ResourceKey<GameTestInstance> LESSER_SUN_ATTUNEMENT_NIGHT_BUFF = createInstanceKey("lesser_sun_attunement_night_buff");
    public static final ResourceKey<GameTestInstance> LESSER_MOON_ATTUNEMENT_BUFF = createInstanceKey("lesser_moon_attunement_buff");
    public static final ResourceKey<GameTestInstance> GREATER_MOON_ATTUNEMENT_BUFF = createInstanceKey("greater_moon_attunement_buff");
    public static final ResourceKey<GameTestInstance> LESSER_BLOOD_ATTUNEMENT_BUFF = createInstanceKey("lesser_blood_attunement_buff");
    public static final ResourceKey<GameTestInstance> GREATER_BLOOD_ATTUNEMENT_BUFF = createInstanceKey("greater_blood_attunement_buff");
    public static final ResourceKey<GameTestInstance> GREATER_INFERNAL_ATTUNEMENT_BUFF_IN_FIRE = createInstanceKey("greater_infernal_attunement_buff_in_fire");
    public static final ResourceKey<GameTestInstance> GREATER_INFERNAL_ATTUNEMENT_BUFF_ON_FIRE = createInstanceKey("greater_infernal_attunement_buff_on_fire");
    public static final ResourceKey<GameTestInstance> GREATER_INFERNAL_ATTUNEMENT_BUFF_LAVA = createInstanceKey("greater_infernal_attunement_buff_lava");
    public static final ResourceKey<GameTestInstance> GREATER_INFERNAL_ATTUNEMENT_BUFF_HOT_FLOOR = createInstanceKey("greater_infernal_attunement_buff_hot_floor");
    public static final ResourceKey<GameTestInstance> GREATER_INFERNAL_ATTUNEMENT_BUFF_INFERNAL_SORCERY = createInstanceKey("greater_infernal_attunement_buff_infernal_sorcery");
    public static final ResourceKey<GameTestInstance> LESSER_VOID_ATTUNEMENT_BUFF = createInstanceKey("lesser_void_attunement_buff");
    public static final ResourceKey<GameTestInstance> GREATER_VOID_ATTUNEMENT_BUFF = createInstanceKey("greater_void_attunement_buff");
    public static final ResourceKey<GameTestInstance> LESSER_HALLOWED_ATTUNEMENT_BUFF = createInstanceKey("lesser_allowed_attunement_buff");
    public static final ResourceKey<GameTestInstance> GREATER_HALLOWED_ATTUNEMENT_BUFF = createInstanceKey("greater_allowed_attunement_buff");

    // Item handler tests
    public static final ResourceKey<GameTestInstance> ITEM_HANDLER_NULL_DIRECTION_RESEARCH_TABLE = createInstanceKey("item_handler_null_direction_research_table");
    public static final ResourceKey<GameTestInstance> ITEM_HANDLER_NULL_DIRECTION_WAND_CHARGER = createInstanceKey("item_handler_null_direction_wand_charger");
    public static final ResourceKey<GameTestInstance> ITEM_HANDLER_NULL_DIRECTION_CALCINATOR_BASIC = createInstanceKey("item_handler_null_direction_calculator_basic");

    // Player knowledge tests
    public static final ResourceKey<GameTestInstance> ADD_AND_CHECK_RESEARCH = createInstanceKey("add_and_check_research");
    public static final ResourceKey<GameTestInstance> CANNOT_ADD_DUPLICATE_RESEARCH = createInstanceKey("cannot_add_duplicate_research");
    public static final ResourceKey<GameTestInstance> REMOVE_RESEARCH = createInstanceKey("remove_research");
    public static final ResourceKey<GameTestInstance> GET_RESEARCH_SET = createInstanceKey("get_research_set");
    public static final ResourceKey<GameTestInstance> GET_SET_RESEARCH_STAGE = createInstanceKey("get_set_research_stage");
    public static final ResourceKey<GameTestInstance> GET_SET_RESEARCH_FLAG = createInstanceKey("get_set_research_flag");
    public static final ResourceKey<GameTestInstance> REMOVE_RESEARCH_FLAG = createInstanceKey("remove_research_flag");
    public static final ResourceKey<GameTestInstance> GET_RESEARCH_FLAGS = createInstanceKey("get_research_flags");
    public static final ResourceKey<GameTestInstance> GET_RESEARCH_STATUS = createInstanceKey("get_research_status");
    public static final ResourceKey<GameTestInstance> IS_RESEARCH_COMPLETE = createInstanceKey("is_research_complete");
    public static final ResourceKey<GameTestInstance> GET_SET_KNOWLEDGE_RAW = createInstanceKey("get_set_knowledge_raw");
    public static final ResourceKey<GameTestInstance> GET_KNOWLEDGE_LEVELS = createInstanceKey("get_knowledge_levels");
    public static final ResourceKey<GameTestInstance> GET_SET_ACTIVE_RESEARCH_PROJECT = createInstanceKey("get_set_active_research_project");
    public static final ResourceKey<GameTestInstance> GET_SET_LAST_RESEARCH_TOPIC = createInstanceKey("get_set_last_research_topic");
    public static final ResourceKey<GameTestInstance> GET_SET_RESEARCH_TOPIC_HISTORY = createInstanceKey("get_set_research_topic_history");
    public static final ResourceKey<GameTestInstance> KNOWLEDGE_SERIALIZATION = createInstanceKey("knowledge_serialization");
    public static final ResourceKey<GameTestInstance> DESERIALIZE_FROM_LEGACY_FORMAT = createInstanceKey("deserialize_from_legacy_format");
    public static final ResourceKey<GameTestInstance> ADD_AND_CHECK_RESEARCH_POST_SERIALIZATION = createInstanceKey("add_and_check_research_post_serialization");
    public static final ResourceKey<GameTestInstance> KNOWLEDGE_SCHEMA_VERSION = createInstanceKey("knowledge_schema_version");
    public static final ResourceKey<GameTestInstance> MARKS_DEFAULT_ENTRIES_AS_READ_ON_UPVERSION = createInstanceKey("marks_default_entries_as_read_on_upversion");
    public static final ResourceKey<GameTestInstance> DOES_NOT_MARK_NON_DEFAULT_ENTRIES_AS_READ_ON_UPVERSION = createInstanceKey("does_not_mark_non_default_entries_as_read_on_upversion");

    // Arcane workbench tests
    public static final ResourceKey<GameTestInstance> ARCANE_WORKBENCH_CRAFT_WORKS = createInstanceKey("arcane_workbench_craft_works");

    // Calcinator tests
    public static final ResourceKey<GameTestInstance> CALCINATOR_WORKS_WITH_PLAYER_PRESENT = createInstanceKey("calcinator_works_with_player_present");
    public static final ResourceKey<GameTestInstance> CALCINATOR_WORKS_WITHOUT_PLAYER_PRESENT = createInstanceKey("calcinator_works_without_player_present");

    // Crafting requirement tests
    public static final ResourceKey<GameTestInstance> CRAFTING_REQUIREMENT_ARCANE_RECIPE = createInstanceKey("crafting_requirement_arcane_recipe");
    public static final ResourceKey<GameTestInstance> CRAFTING_REQUIREMENT_RITUAL_RECIPE = createInstanceKey("crafting_requirement_ritual_recipe");

    // Repair tests
    public static final ResourceKey<GameTestInstance> EARTHSHATTER_HAMMER_CANNOT_BE_REPAIRED = createInstanceKey("earthshatter_hammer_cannot_be_repaired");

    // Runecarving tests
    public static final ResourceKey<GameTestInstance> RUNECARVING_CRAFT_WORKS = createInstanceKey("runecarving_craft_works");

    // Caster enchantability tests
    public static final ResourceKey<GameTestInstance> CASTER_ENCHANTABLE_MUNDANE_WAND = createInstanceKey("caster_enchantable_mundane_wand");
    public static final ResourceKey<GameTestInstance> CASTER_ENCHANTABLE_MODULAR_WAND = createInstanceKey("caster_enchantable_modular_wand");
    public static final ResourceKey<GameTestInstance> CASTER_ENCHANTABLE_MODULAR_STAFF = createInstanceKey("caster_enchantable_modular_staff");

    // Ritual enchantment tests
    public static final ResourceKey<GameTestInstance> ENCHANTMENT_ESSENCE_THIEF1 = createInstanceKey("enchantment_essence_thief1");
    public static final ResourceKey<GameTestInstance> ENCHANTMENT_ESSENCE_THIEF2 = createInstanceKey("enchantment_essence_thief2");
    public static final ResourceKey<GameTestInstance> ENCHANTMENT_ESSENCE_THIEF3 = createInstanceKey("enchantment_essence_thief3");
    public static final ResourceKey<GameTestInstance> ENCHANTMENT_ESSENCE_THIEF4 = createInstanceKey("enchantment_essence_thief4");

    // FTUX tests
    public static final ResourceKey<GameTestInstance> FONT_DISCOVERY_EARTH = createInstanceKey("font_discovery_earth");
    public static final ResourceKey<GameTestInstance> FONT_DISCOVERY_SEA = createInstanceKey("font_discovery_sea");
    public static final ResourceKey<GameTestInstance> FONT_DISCOVERY_SKY = createInstanceKey("font_discovery_sky");
    public static final ResourceKey<GameTestInstance> FONT_DISCOVERY_SUN = createInstanceKey("font_discovery_sun");
    public static final ResourceKey<GameTestInstance> FONT_DISCOVERY_MOON = createInstanceKey("font_discovery_moon");
    public static final ResourceKey<GameTestInstance> SLEEP_AFTER_SHRINE_GRANTS_DREAM = createInstanceKey("sleep_after_shrine_grants_dream");
    public static final ResourceKey<GameTestInstance> MUNDANE_WAND_CRAFTING_EARTH = createInstanceKey("mundane_wand_crafting_earth");
    public static final ResourceKey<GameTestInstance> MUNDANE_WAND_CRAFTING_SEA = createInstanceKey("mundane_wand_crafting_sea");
    public static final ResourceKey<GameTestInstance> MUNDANE_WAND_CRAFTING_SKY = createInstanceKey("mundane_wand_crafting_sky");
    public static final ResourceKey<GameTestInstance> MUNDANE_WAND_CRAFTING_SUN = createInstanceKey("mundane_wand_crafting_sun");
    public static final ResourceKey<GameTestInstance> MUNDANE_WAND_CRAFTING_MOON = createInstanceKey("mundane_wand_crafting_moon");
    public static final ResourceKey<GameTestInstance> TRANSFORM_ABORT_GIVES_HINT = createInstanceKey("transform_abort_gives_hint");
    public static final ResourceKey<GameTestInstance> TRANSFORM_WITHOUT_DREAM_DOES_NOTHING = createInstanceKey("transform_without_dream_does_nothing");
    public static final ResourceKey<GameTestInstance> TRANSFORM_GRIMOIRE = createInstanceKey("transform_grimoire");

    // Beeswax item tests
    public static final ResourceKey<GameTestInstance> APPLY_BEESWAX_DIRECTLY_CLEAN = createInstanceKey("apply_beeswax_directly_clean");
    public static final ResourceKey<GameTestInstance> APPLY_BEESWAX_DIRECTLY_EXPOSED = createInstanceKey("apply_beeswax_directly_exposed");
    public static final ResourceKey<GameTestInstance> APPLY_BEESWAX_DIRECTLY_WEATHERED = createInstanceKey("apply_beeswax_directly_weathered");
    public static final ResourceKey<GameTestInstance> APPLY_BEESWAX_DIRECTLY_OXIDIZED = createInstanceKey("apply_beeswax_directly_oxidized");
    public static final ResourceKey<GameTestInstance> APPLY_BEESWAX_VIA_CRAFTING_CLEAN = createInstanceKey("apply_beeswax_via_crafting_clean");
    public static final ResourceKey<GameTestInstance> APPLY_BEESWAX_VIA_CRAFTING_EXPOSED = createInstanceKey("apply_beeswax_via_crafting_exposed");
    public static final ResourceKey<GameTestInstance> APPLY_BEESWAX_VIA_CRAFTING_WEATHERED = createInstanceKey("apply_beeswax_via_crafting_weathered");
    public static final ResourceKey<GameTestInstance> APPLY_BEESWAX_VIA_CRAFTING_OXIDIZED = createInstanceKey("apply_beeswax_via_crafting_oxidized");

    // Dispenser item tests
    public static final ResourceKey<GameTestInstance> MANA_ARROWS_FIRED_FROM_DISPENSER_EARTH = createInstanceKey("mana_arrows_fired_from_dispenser_earth");
    public static final ResourceKey<GameTestInstance> MANA_ARROWS_FIRED_FROM_DISPENSER_SEA = createInstanceKey("mana_arrows_fired_from_dispenser_sea");
    public static final ResourceKey<GameTestInstance> MANA_ARROWS_FIRED_FROM_DISPENSER_SKY = createInstanceKey("mana_arrows_fired_from_dispenser_sky");
    public static final ResourceKey<GameTestInstance> MANA_ARROWS_FIRED_FROM_DISPENSER_SUN = createInstanceKey("mana_arrows_fired_from_dispenser_sun");
    public static final ResourceKey<GameTestInstance> MANA_ARROWS_FIRED_FROM_DISPENSER_MOON = createInstanceKey("mana_arrows_fired_from_dispenser_moon");
    public static final ResourceKey<GameTestInstance> MANA_ARROWS_FIRED_FROM_DISPENSER_BLOOD = createInstanceKey("mana_arrows_fired_from_dispenser_blood");
    public static final ResourceKey<GameTestInstance> MANA_ARROWS_FIRED_FROM_DISPENSER_INFERNAL = createInstanceKey("mana_arrows_fired_from_dispenser_infernal");
    public static final ResourceKey<GameTestInstance> MANA_ARROWS_FIRED_FROM_DISPENSER_VOID = createInstanceKey("mana_arrows_fired_from_dispenser_void");
    public static final ResourceKey<GameTestInstance> MANA_ARROWS_FIRED_FROM_DISPENSER_HALLOWED = createInstanceKey("mana_arrows_fired_from_dispenser_hallowed");

    // Wand mana tests
    public static final ResourceKey<GameTestInstance> WAND_CAN_GET_AND_ADD_MANA = createInstanceKey("wand_can_get_and_add_mana");
    public static final ResourceKey<GameTestInstance> WAND_CAN_GET_AND_ADD_REAL_MANA = createInstanceKey("wand_can_get_and_add_real_mana");
    public static final ResourceKey<GameTestInstance> WAND_CANNOT_ADD_TOO_MUCH_MANA = createInstanceKey("wand_cannot_add_too_much_mana");
    public static final ResourceKey<GameTestInstance> WAND_CAN_GET_ALL_MANA = createInstanceKey("wand_can_get_all_mana");
    public static final ResourceKey<GameTestInstance> WAND_CAN_CONSUME_MANA = createInstanceKey("wand_can_consume_mana");
    public static final ResourceKey<GameTestInstance> WAND_CANNOT_CONSUME_MORE_MANA_THAN_IT_HAS = createInstanceKey("wand_cannot_consume_more_mana_than_it_has");
    public static final ResourceKey<GameTestInstance> WAND_CAN_CONSUME_MULTIPLE_TYPES_OF_MANA = createInstanceKey("wand_can_consume_multiple_types_of_mana");
    public static final ResourceKey<GameTestInstance> WAND_CANNOT_CONSUME_MORE_MANA_THAN_IT_HAS_WITH_MULTIPLE_TYPES = createInstanceKey("wand_cannot_consume_more_mana_than_it_has_with_multiple_types");
    public static final ResourceKey<GameTestInstance> WAND_CAN_REMOVE_MANA_RAW = createInstanceKey("wand_can_remove_mana_raw");
    public static final ResourceKey<GameTestInstance> WAND_CANNOT_REMOVE_MORE_RAW_MANA_THAN_IT_HAS = createInstanceKey("wand_cannot_remove_more_raw_mana_than_it_has");
    public static final ResourceKey<GameTestInstance> WAND_CONTAINS_MANA = createInstanceKey("wand_contains_mana");
    public static final ResourceKey<GameTestInstance> WAND_CONTAINS_MANA_LIST = createInstanceKey("wand_contains_mana_list");
    public static final ResourceKey<GameTestInstance> WAND_CONTAINS_MANA_RAW = createInstanceKey("wand_contains_mana_raw");

    // Research key tests
    public static final ResourceKey<GameTestInstance> RESEARCH_KEY_DISCIPLINE = createInstanceKey("research_key_discipline");
    public static final ResourceKey<GameTestInstance> RESEARCH_KEY_ENTRY = createInstanceKey("research_key_entry");
    public static final ResourceKey<GameTestInstance> RESEARCH_KEY_STAGE = createInstanceKey("research_key_stage");
    public static final ResourceKey<GameTestInstance> RESEARCH_KEY_ITEM_SCAN = createInstanceKey("research_key_item_scan");
    public static final ResourceKey<GameTestInstance> RESEARCH_KEY_ENTITY_SCAN = createInstanceKey("research_key_entity_scan");
    public static final ResourceKey<GameTestInstance> RESEARCH_KEY_STACK_CRAFTED = createInstanceKey("research_key_stack_crafted");
    public static final ResourceKey<GameTestInstance> RESEARCH_KEY_TAG_CRAFTED = createInstanceKey("research_key_tag_crafted");
    public static final ResourceKey<GameTestInstance> RESEARCH_KEY_RUNE_ENCHANTMENT = createInstanceKey("research_key_rune_enchantment");
    public static final ResourceKey<GameTestInstance> RESEARCH_KEY_RUNE_ENCHANTMENT_PARTIAL = createInstanceKey("research_key_rune_enchantment_partial");

    // Research requirement tests
    public static final ResourceKey<GameTestInstance> RESEARCH_REQUIREMENT_RESEARCH = createInstanceKey("research_requirement_research");
    public static final ResourceKey<GameTestInstance> RESEARCH_REQUIREMENT_KNOWLEDGE = createInstanceKey("research_requirement_knowledge");
    public static final ResourceKey<GameTestInstance> RESEARCH_REQUIREMENT_ITEM_STACK = createInstanceKey("research_requirement_item_stack");
    public static final ResourceKey<GameTestInstance> RESEARCH_REQUIREMENT_ITEM_TAG = createInstanceKey("research_requirement_item_tag");
    public static final ResourceKey<GameTestInstance> RESEARCH_REQUIREMENT_STAT = createInstanceKey("research_requirement_stat");
    public static final ResourceKey<GameTestInstance> RESEARCH_REQUIREMENT_EXPERTISE = createInstanceKey("research_requirement_expertise");
    public static final ResourceKey<GameTestInstance> RESEARCH_REQUIREMENT_VANILLA_ITEM_USED_STAT = createInstanceKey("research_requirement_vanilla_item_used_stat");
    public static final ResourceKey<GameTestInstance> RESEARCH_REQUIREMENT_VANILLA_CUSTOM_STAT = createInstanceKey("research_requirement_vanilla_custom_stat");
    public static final ResourceKey<GameTestInstance> RESEARCH_REQUIREMENT_AND = createInstanceKey("research_requirement_and");
    public static final ResourceKey<GameTestInstance> RESEARCH_REQUIREMENT_OR = createInstanceKey("research_requirement_or");
    public static final ResourceKey<GameTestInstance> RESEARCH_REQUIREMENT_QUORUM = createInstanceKey("research_requirement_quorum");

    // Research system tests
    public static final ResourceKey<GameTestInstance> RESEARCH_GRANT_WORKS = createInstanceKey("research_grant_works");

    public static void bootstrap(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, CANARY, TestFunctionsPM.CANARY.getKey());
        registerAttunementBuffTests(context);
        registerItemHandlerTests(context);
        registerPlayerKnowledgeTests(context);
        registerArcaneWorkbenchTests(context);
        registerCalcinatorTests(context);
        registerCraftingRequirementTests(context);
        registerRepairTests(context);
        registerRunecarvingTests(context);
        registerCasterEnchantabilityTests(context);
        registerRitualEnchantmentTests(context);
        registerFtuxTests(context);
        registerBeeswaxItemTests(context);
        registerDispenserItemTests(context);
        registerWandManaTests(context);
        registerResearchKeyTests(context);
        registerResearchRequirementTests(context);
        registerResearchTests(context);
    }

    public static void registerResearchTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, RESEARCH_GRANT_WORKS, TestFunctionsPM.RESEARCH_GRANT_WORKS.getKey());
    }

    public static void registerResearchRequirementTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, RESEARCH_REQUIREMENT_RESEARCH, TestFunctionsPM.RESEARCH_REQUIREMENT_RESEARCH.getKey());
        registerFunction(context, RESEARCH_REQUIREMENT_KNOWLEDGE, TestFunctionsPM.RESEARCH_REQUIREMENT_KNOWLEDGE.getKey());
        registerFunction(context, RESEARCH_REQUIREMENT_ITEM_STACK, TestFunctionsPM.RESEARCH_REQUIREMENT_ITEM_STACK.getKey());
        registerFunction(context, RESEARCH_REQUIREMENT_ITEM_TAG, TestFunctionsPM.RESEARCH_REQUIREMENT_ITEM_TAG.getKey());
        registerFunction(context, RESEARCH_REQUIREMENT_STAT, TestFunctionsPM.RESEARCH_REQUIREMENT_STAT.getKey());
        registerFunction(context, RESEARCH_REQUIREMENT_EXPERTISE, TestFunctionsPM.RESEARCH_REQUIREMENT_EXPERTISE.getKey());
        registerFunction(context, RESEARCH_REQUIREMENT_VANILLA_ITEM_USED_STAT, TestFunctionsPM.RESEARCH_REQUIREMENT_VANILLA_ITEM_USED_STAT.getKey(), ResourceUtils.loc("test/floor5x5x5"));
        registerFunction(context, RESEARCH_REQUIREMENT_VANILLA_CUSTOM_STAT, TestFunctionsPM.RESEARCH_REQUIREMENT_VANILLA_CUSTOM_STAT.getKey(), ResourceUtils.loc("test/floor5x5x5"));
        registerFunction(context, RESEARCH_REQUIREMENT_AND, TestFunctionsPM.RESEARCH_REQUIREMENT_AND.getKey());
        registerFunction(context, RESEARCH_REQUIREMENT_OR, TestFunctionsPM.RESEARCH_REQUIREMENT_OR.getKey());
        registerFunction(context, RESEARCH_REQUIREMENT_QUORUM, TestFunctionsPM.RESEARCH_REQUIREMENT_QUORUM.getKey());
    }

    public static void registerResearchKeyTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, RESEARCH_KEY_DISCIPLINE, TestFunctionsPM.RESEARCH_KEY_DISCIPLINE.getKey());
        registerFunction(context, RESEARCH_KEY_ENTRY, TestFunctionsPM.RESEARCH_KEY_ENTRY.getKey());
        registerFunction(context, RESEARCH_KEY_STAGE, TestFunctionsPM.RESEARCH_KEY_STAGE.getKey());
        registerFunction(context, RESEARCH_KEY_ITEM_SCAN, TestFunctionsPM.RESEARCH_KEY_ITEM_SCAN.getKey());
        registerFunction(context, RESEARCH_KEY_ENTITY_SCAN, TestFunctionsPM.RESEARCH_KEY_ENTITY_SCAN.getKey());
        registerFunction(context, RESEARCH_KEY_STACK_CRAFTED, TestFunctionsPM.RESEARCH_KEY_STACK_CRAFTED.getKey());
        registerFunction(context, RESEARCH_KEY_TAG_CRAFTED, TestFunctionsPM.RESEARCH_KEY_TAG_CRAFTED.getKey());
        registerFunction(context, RESEARCH_KEY_RUNE_ENCHANTMENT, TestFunctionsPM.RESEARCH_KEY_RUNE_ENCHANTMENT.getKey());
        registerFunction(context, RESEARCH_KEY_RUNE_ENCHANTMENT_PARTIAL, TestFunctionsPM.RESEARCH_KEY_RUNE_ENCHANTMENT_PARTIAL.getKey());
    }

    public static void registerWandManaTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, WAND_CAN_GET_AND_ADD_MANA, TestFunctionsPM.WAND_CAN_GET_AND_ADD_MANA.getKey());
        registerFunction(context, WAND_CAN_GET_AND_ADD_REAL_MANA, TestFunctionsPM.WAND_CAN_GET_AND_ADD_REAL_MANA.getKey());
        registerFunction(context, WAND_CANNOT_ADD_TOO_MUCH_MANA, TestFunctionsPM.WAND_CANNOT_ADD_TOO_MUCH_MANA.getKey());
        registerFunction(context, WAND_CAN_GET_ALL_MANA, TestFunctionsPM.WAND_CAN_GET_ALL_MANA.getKey());
        registerFunction(context, WAND_CAN_CONSUME_MANA, TestFunctionsPM.WAND_CAN_CONSUME_MANA.getKey());
        registerFunction(context, WAND_CANNOT_CONSUME_MORE_MANA_THAN_IT_HAS, TestFunctionsPM.WAND_CANNOT_CONSUME_MORE_MANA_THAN_IT_HAS.getKey());
        registerFunction(context, WAND_CAN_CONSUME_MULTIPLE_TYPES_OF_MANA, TestFunctionsPM.WAND_CAN_CONSUME_MULTIPLE_TYPES_OF_MANA.getKey());
        registerFunction(context, WAND_CANNOT_CONSUME_MORE_MANA_THAN_IT_HAS_WITH_MULTIPLE_TYPES, TestFunctionsPM.WAND_CANNOT_CONSUME_MORE_MANA_THAN_IT_HAS_WITH_MULTIPLE_TYPES.getKey());
        registerFunction(context, WAND_CAN_REMOVE_MANA_RAW, TestFunctionsPM.WAND_CAN_REMOVE_MANA_RAW.getKey());
        registerFunction(context, WAND_CANNOT_REMOVE_MORE_RAW_MANA_THAN_IT_HAS, TestFunctionsPM.WAND_CANNOT_REMOVE_MORE_RAW_MANA_THAN_IT_HAS.getKey());
        registerFunction(context, WAND_CONTAINS_MANA, TestFunctionsPM.WAND_CONTAINS_MANA.getKey());
        registerFunction(context, WAND_CONTAINS_MANA_LIST, TestFunctionsPM.WAND_CONTAINS_MANA_LIST.getKey());
        registerFunction(context, WAND_CONTAINS_MANA_RAW, TestFunctionsPM.WAND_CONTAINS_MANA_RAW.getKey());
    }

    public static void registerDispenserItemTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, MANA_ARROWS_FIRED_FROM_DISPENSER_EARTH, TestFunctionsPM.MANA_ARROWS_FIRED_FROM_DISPENSER_EARTH.getKey());
        registerFunction(context, MANA_ARROWS_FIRED_FROM_DISPENSER_SEA, TestFunctionsPM.MANA_ARROWS_FIRED_FROM_DISPENSER_SEA.getKey());
        registerFunction(context, MANA_ARROWS_FIRED_FROM_DISPENSER_SKY, TestFunctionsPM.MANA_ARROWS_FIRED_FROM_DISPENSER_SKY.getKey());
        registerFunction(context, MANA_ARROWS_FIRED_FROM_DISPENSER_SUN, TestFunctionsPM.MANA_ARROWS_FIRED_FROM_DISPENSER_SUN.getKey());
        registerFunction(context, MANA_ARROWS_FIRED_FROM_DISPENSER_MOON, TestFunctionsPM.MANA_ARROWS_FIRED_FROM_DISPENSER_MOON.getKey());
        registerFunction(context, MANA_ARROWS_FIRED_FROM_DISPENSER_BLOOD, TestFunctionsPM.MANA_ARROWS_FIRED_FROM_DISPENSER_BLOOD.getKey());
        registerFunction(context, MANA_ARROWS_FIRED_FROM_DISPENSER_INFERNAL, TestFunctionsPM.MANA_ARROWS_FIRED_FROM_DISPENSER_INFERNAL.getKey());
        registerFunction(context, MANA_ARROWS_FIRED_FROM_DISPENSER_VOID, TestFunctionsPM.MANA_ARROWS_FIRED_FROM_DISPENSER_VOID.getKey());
        registerFunction(context, MANA_ARROWS_FIRED_FROM_DISPENSER_HALLOWED, TestFunctionsPM.MANA_ARROWS_FIRED_FROM_DISPENSER_HALLOWED.getKey());
    }

    public static void registerBeeswaxItemTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, APPLY_BEESWAX_DIRECTLY_CLEAN, TestFunctionsPM.APPLY_BEESWAX_DIRECTLY_CLEAN.getKey());
        registerFunction(context, APPLY_BEESWAX_DIRECTLY_EXPOSED, TestFunctionsPM.APPLY_BEESWAX_DIRECTLY_EXPOSED.getKey());
        registerFunction(context, APPLY_BEESWAX_DIRECTLY_WEATHERED, TestFunctionsPM.APPLY_BEESWAX_DIRECTLY_WEATHERED.getKey());
        registerFunction(context, APPLY_BEESWAX_DIRECTLY_OXIDIZED, TestFunctionsPM.APPLY_BEESWAX_DIRECTLY_OXIDIZED.getKey());
        registerFunction(context, APPLY_BEESWAX_VIA_CRAFTING_CLEAN, TestFunctionsPM.APPLY_BEESWAX_VIA_CRAFTING_CLEAN.getKey());
        registerFunction(context, APPLY_BEESWAX_VIA_CRAFTING_EXPOSED, TestFunctionsPM.APPLY_BEESWAX_VIA_CRAFTING_EXPOSED.getKey());
        registerFunction(context, APPLY_BEESWAX_VIA_CRAFTING_WEATHERED, TestFunctionsPM.APPLY_BEESWAX_VIA_CRAFTING_WEATHERED.getKey());
        registerFunction(context, APPLY_BEESWAX_VIA_CRAFTING_OXIDIZED, TestFunctionsPM.APPLY_BEESWAX_VIA_CRAFTING_OXIDIZED.getKey());
    }

    public static void registerFtuxTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, FONT_DISCOVERY_EARTH, TestFunctionsPM.FONT_DISCOVERY_EARTH.getKey());
        registerFunction(context, FONT_DISCOVERY_SEA, TestFunctionsPM.FONT_DISCOVERY_SEA.getKey());
        registerFunction(context, FONT_DISCOVERY_SKY, TestFunctionsPM.FONT_DISCOVERY_SKY.getKey());
        registerFunction(context, FONT_DISCOVERY_SUN, TestFunctionsPM.FONT_DISCOVERY_SUN.getKey());
        registerFunction(context, FONT_DISCOVERY_MOON, TestFunctionsPM.FONT_DISCOVERY_MOON.getKey());
        registerFunction(context, SLEEP_AFTER_SHRINE_GRANTS_DREAM, TestFunctionsPM.SLEEP_AFTER_SHRINE_GRANTS_DREAM.getKey(), ResourceUtils.loc("test/floor5x5x5"));
        registerFunction(context, MUNDANE_WAND_CRAFTING_EARTH, TestFunctionsPM.MUNDANE_WAND_CRAFTING_EARTH.getKey());
        registerFunction(context, MUNDANE_WAND_CRAFTING_SEA, TestFunctionsPM.MUNDANE_WAND_CRAFTING_SEA.getKey());
        registerFunction(context, MUNDANE_WAND_CRAFTING_SKY, TestFunctionsPM.MUNDANE_WAND_CRAFTING_SKY.getKey());
        registerFunction(context, MUNDANE_WAND_CRAFTING_SUN, TestFunctionsPM.MUNDANE_WAND_CRAFTING_SUN.getKey());
        registerFunction(context, MUNDANE_WAND_CRAFTING_MOON, TestFunctionsPM.MUNDANE_WAND_CRAFTING_MOON.getKey());
        registerFunction(context, TRANSFORM_ABORT_GIVES_HINT, TestFunctionsPM.TRANSFORM_ABORT_GIVES_HINT.getKey());
        registerFunction(context, TRANSFORM_WITHOUT_DREAM_DOES_NOTHING, TestFunctionsPM.TRANSFORM_WITHOUT_DREAM_DOES_NOTHING.getKey());
        registerFunction(context, TRANSFORM_GRIMOIRE, TestFunctionsPM.TRANSFORM_GRIMOIRE.getKey());
    }

    public static void registerRitualEnchantmentTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, ENCHANTMENT_ESSENCE_THIEF1, TestFunctionsPM.ENCHANTMENT_ESSENCE_THIEF1.getKey());
        registerFunction(context, ENCHANTMENT_ESSENCE_THIEF2, TestFunctionsPM.ENCHANTMENT_ESSENCE_THIEF2.getKey());
        registerFunction(context, ENCHANTMENT_ESSENCE_THIEF3, TestFunctionsPM.ENCHANTMENT_ESSENCE_THIEF3.getKey());
        registerFunction(context, ENCHANTMENT_ESSENCE_THIEF4, TestFunctionsPM.ENCHANTMENT_ESSENCE_THIEF4.getKey());
    }

    public static void registerCasterEnchantabilityTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, CASTER_ENCHANTABLE_MUNDANE_WAND, TestFunctionsPM.CASTER_ENCHANTABLE_MUNDANE_WAND.getKey());
        registerFunction(context, CASTER_ENCHANTABLE_MODULAR_WAND, TestFunctionsPM.CASTER_ENCHANTABLE_MODULAR_WAND.getKey());
        registerFunction(context, CASTER_ENCHANTABLE_MODULAR_STAFF, TestFunctionsPM.CASTER_ENCHANTABLE_MODULAR_STAFF.getKey());
    }

    private static void registerRunecarvingTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, RUNECARVING_CRAFT_WORKS, TestFunctionsPM.RUNECARVING_CRAFT_WORKS.getKey());
    }

    private static void registerRepairTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, EARTHSHATTER_HAMMER_CANNOT_BE_REPAIRED, TestFunctionsPM.EARTHSHATTER_HAMMER_CANNOT_BE_REPAIRED.getKey());
    }

    private static void registerCraftingRequirementTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, CRAFTING_REQUIREMENT_ARCANE_RECIPE, TestFunctionsPM.CRAFTING_REQUIREMENT_ARCANE_RECIPE.getKey());
        registerFunction(context, CRAFTING_REQUIREMENT_RITUAL_RECIPE, TestFunctionsPM.CRAFTING_REQUIREMENT_RITUAL_RECIPE.getKey());
    }

    private static void registerCalcinatorTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, CALCINATOR_WORKS_WITH_PLAYER_PRESENT, TestFunctionsPM.CALCINATOR_WORKS_WITH_PLAYER_PRESENT.getKey());
        registerFunction(context, CALCINATOR_WORKS_WITHOUT_PLAYER_PRESENT, TestFunctionsPM.CALCINATOR_WORKS_WITHOUT_PLAYER_PRESENT.getKey());
    }

    private static void registerArcaneWorkbenchTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, ARCANE_WORKBENCH_CRAFT_WORKS, TestFunctionsPM.ARCANE_WORKBENCH_CRAFT_WORKS.getKey());
    }

    private static void registerPlayerKnowledgeTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, ADD_AND_CHECK_RESEARCH, TestFunctionsPM.ADD_AND_CHECK_RESEARCH.getKey());
        registerFunction(context, CANNOT_ADD_DUPLICATE_RESEARCH, TestFunctionsPM.CANNOT_ADD_DUPLICATE_RESEARCH.getKey());
        registerFunction(context, REMOVE_RESEARCH, TestFunctionsPM.REMOVE_RESEARCH.getKey());
        registerFunction(context, GET_RESEARCH_SET, TestFunctionsPM.GET_RESEARCH_SET.getKey());
        registerFunction(context, GET_SET_RESEARCH_STAGE, TestFunctionsPM.GET_SET_RESEARCH_STAGE.getKey());
        registerFunction(context, GET_SET_RESEARCH_FLAG, TestFunctionsPM.GET_SET_RESEARCH_FLAG.getKey());
        registerFunction(context, REMOVE_RESEARCH_FLAG, TestFunctionsPM.REMOVE_RESEARCH_FLAG.getKey());
        registerFunction(context, GET_RESEARCH_FLAGS, TestFunctionsPM.GET_RESEARCH_FLAGS.getKey());
        registerFunction(context, GET_RESEARCH_STATUS, TestFunctionsPM.GET_RESEARCH_STATUS.getKey());
        registerFunction(context, IS_RESEARCH_COMPLETE, TestFunctionsPM.IS_RESEARCH_COMPLETE.getKey());
        registerFunction(context, GET_SET_KNOWLEDGE_RAW, TestFunctionsPM.GET_SET_KNOWLEDGE_RAW.getKey());
        registerFunction(context, GET_KNOWLEDGE_LEVELS, TestFunctionsPM.GET_KNOWLEDGE_LEVELS.getKey());
        registerFunction(context, GET_SET_ACTIVE_RESEARCH_PROJECT, TestFunctionsPM.GET_SET_ACTIVE_RESEARCH_PROJECT.getKey());
        registerFunction(context, GET_SET_LAST_RESEARCH_TOPIC, TestFunctionsPM.GET_SET_LAST_RESEARCH_TOPIC.getKey());
        registerFunction(context, GET_SET_RESEARCH_TOPIC_HISTORY, TestFunctionsPM.GET_SET_RESEARCH_TOPIC_HISTORY.getKey());
        registerFunction(context, KNOWLEDGE_SERIALIZATION, TestFunctionsPM.KNOWLEDGE_SERIALIZATION.getKey());
        registerFunction(context, DESERIALIZE_FROM_LEGACY_FORMAT, TestFunctionsPM.DESERIALIZE_FROM_LEGACY_FORMAT.getKey());
        registerFunction(context, ADD_AND_CHECK_RESEARCH_POST_SERIALIZATION, TestFunctionsPM.ADD_AND_CHECK_RESEARCH_POST_SERIALIZATION.getKey());
        registerFunction(context, KNOWLEDGE_SCHEMA_VERSION, TestFunctionsPM.KNOWLEDGE_SCHEMA_VERSION.getKey());
        registerFunction(context, MARKS_DEFAULT_ENTRIES_AS_READ_ON_UPVERSION, TestFunctionsPM.MARKS_DEFAULT_ENTRIES_AS_READ_ON_UPVERSION.getKey());
        registerFunction(context, DOES_NOT_MARK_NON_DEFAULT_ENTRIES_AS_READ_ON_UPVERSION, TestFunctionsPM.DOES_NOT_MARK_NON_DEFAULT_ENTRIES_AS_READ_ON_UPVERSION.getKey());
    }

    private static void registerItemHandlerTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, ITEM_HANDLER_NULL_DIRECTION_RESEARCH_TABLE, TestFunctionsPM.ITEM_HANDLER_NULL_DIRECTION_RESEARCH_TABLE.getKey());
        registerFunction(context, ITEM_HANDLER_NULL_DIRECTION_WAND_CHARGER, TestFunctionsPM.ITEM_HANDLER_NULL_DIRECTION_WAND_CHARGER.getKey());
        registerFunction(context, ITEM_HANDLER_NULL_DIRECTION_CALCINATOR_BASIC, TestFunctionsPM.ITEM_HANDLER_NULL_DIRECTION_CALCINATOR_BASIC.getKey());
    }

    private static void registerAttunementBuffTests(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, MINOR_ATTUNEMENT_DISCOUNT_EARTH, TestFunctionsPM.MINOR_ATTUNEMENT_DISCOUNT_EARTH.getKey());
        registerFunction(context, MINOR_ATTUNEMENT_DISCOUNT_SEA, TestFunctionsPM.MINOR_ATTUNEMENT_DISCOUNT_SEA.getKey());
        registerFunction(context, MINOR_ATTUNEMENT_DISCOUNT_SKY, TestFunctionsPM.MINOR_ATTUNEMENT_DISCOUNT_SKY.getKey());
        registerFunction(context, MINOR_ATTUNEMENT_DISCOUNT_SUN, TestFunctionsPM.MINOR_ATTUNEMENT_DISCOUNT_SUN.getKey());
        registerFunction(context, MINOR_ATTUNEMENT_DISCOUNT_MOON, TestFunctionsPM.MINOR_ATTUNEMENT_DISCOUNT_MOON.getKey());
        registerFunction(context, MINOR_ATTUNEMENT_DISCOUNT_BLOOD, TestFunctionsPM.MINOR_ATTUNEMENT_DISCOUNT_BLOOD.getKey());
        registerFunction(context, MINOR_ATTUNEMENT_DISCOUNT_INFERNAL, TestFunctionsPM.MINOR_ATTUNEMENT_DISCOUNT_INFERNAL.getKey());
        registerFunction(context, MINOR_ATTUNEMENT_DISCOUNT_VOID, TestFunctionsPM.MINOR_ATTUNEMENT_DISCOUNT_VOID.getKey());
        registerFunction(context, MINOR_ATTUNEMENT_DISCOUNT_HALLOWED, TestFunctionsPM.MINOR_ATTUNEMENT_DISCOUNT_HALLOWED.getKey());
        registerFunction(context, LESSER_EARTH_ATTUNEMENT_BUFF, TestFunctionsPM.LESSER_EARTH_ATTUNEMENT_BUFF.getKey());
        registerFunction(context, GREATER_EARTH_ATTUNEMENT_BUFF, TestFunctionsPM.GREATER_EARTH_ATTUNEMENT_BUFF.getKey());
        registerFunction(context, LESSER_SEA_ATTUNEMENT_BUFF, TestFunctionsPM.LESSER_SEA_ATTUNEMENT_BUFF.getKey());
        registerFunction(context, GREATER_SEA_ATTUNEMENT_BUFF, TestFunctionsPM.GREATER_SEA_ATTUNEMENT_BUFF.getKey());
        registerFunction(context, LESSER_SKY_ATTUNEMENT_BUFF1, TestFunctionsPM.LESSER_SKY_ATTUNEMENT_BUFF1.getKey());
        registerFunction(context, LESSER_SKY_ATTUNEMENT_BUFF2, TestFunctionsPM.LESSER_SKY_ATTUNEMENT_BUFF2.getKey());
        registerFunction(context, GREATER_SKY_ATTUNEMENT_BUFF, TestFunctionsPM.GREATER_SKY_ATTUNEMENT_BUFF.getKey());
        registerFunction(context, LESSER_SUN_ATTUNEMENT_DAY_BUFF, TestFunctionsPM.LESSER_SUN_ATTUNEMENT_DAY_BUFF.getKey(), TestEnvironmentsPM.DAYTIME_ENV);
        registerFunction(context, LESSER_SUN_ATTUNEMENT_NIGHT_BUFF, TestFunctionsPM.LESSER_SUN_ATTUNEMENT_NIGHT_BUFF.getKey(), TestEnvironmentsPM.NIGHTTIME_ENV);
        registerFunction(context, LESSER_MOON_ATTUNEMENT_BUFF, TestFunctionsPM.LESSER_MOON_ATTUNEMENT_BUFF.getKey());
        registerFunction(context, GREATER_MOON_ATTUNEMENT_BUFF, TestFunctionsPM.GREATER_MOON_ATTUNEMENT_BUFF.getKey());
        registerFunction(context, LESSER_BLOOD_ATTUNEMENT_BUFF, TestFunctionsPM.LESSER_BLOOD_ATTUNEMENT_BUFF.getKey());
        registerFunction(context, GREATER_BLOOD_ATTUNEMENT_BUFF, TestFunctionsPM.GREATER_BLOOD_ATTUNEMENT_BUFF.getKey());
        registerFunction(context, GREATER_INFERNAL_ATTUNEMENT_BUFF_IN_FIRE, TestFunctionsPM.GREATER_INFERNAL_ATTUNEMENT_BUFF_IN_FIRE.getKey());
        registerFunction(context, GREATER_INFERNAL_ATTUNEMENT_BUFF_ON_FIRE, TestFunctionsPM.GREATER_INFERNAL_ATTUNEMENT_BUFF_ON_FIRE.getKey());
        registerFunction(context, GREATER_INFERNAL_ATTUNEMENT_BUFF_LAVA, TestFunctionsPM.GREATER_INFERNAL_ATTUNEMENT_BUFF_LAVA.getKey());
        registerFunction(context, GREATER_INFERNAL_ATTUNEMENT_BUFF_HOT_FLOOR, TestFunctionsPM.GREATER_INFERNAL_ATTUNEMENT_BUFF_HOT_FLOOR.getKey());
        registerFunction(context, GREATER_INFERNAL_ATTUNEMENT_BUFF_INFERNAL_SORCERY, TestFunctionsPM.GREATER_INFERNAL_ATTUNEMENT_BUFF_INFERNAL_SORCERY.getKey());
        registerFunction(context, LESSER_VOID_ATTUNEMENT_BUFF, TestFunctionsPM.LESSER_VOID_ATTUNEMENT_BUFF.getKey());
        registerFunction(context, GREATER_VOID_ATTUNEMENT_BUFF, TestFunctionsPM.GREATER_VOID_ATTUNEMENT_BUFF.getKey());
        registerFunction(context, LESSER_HALLOWED_ATTUNEMENT_BUFF, TestFunctionsPM.LESSER_HALLOWED_ATTUNEMENT_BUFF.getKey());
        registerFunction(context, GREATER_HALLOWED_ATTUNEMENT_BUFF, TestFunctionsPM.GREATER_HALLOWED_ATTUNEMENT_BUFF.getKey());
    }

    private static ResourceKey<GameTestInstance> createInstanceKey(String name) {
        return ResourceKey.create(Registries.TEST_INSTANCE, ResourceUtils.loc(name));
    }

    private static Holder.Reference<GameTestInstance> registerFunction(BootstrapContext<GameTestInstance> context,
                                                                       ResourceKey<GameTestInstance> instanceKey,
                                                                       ResourceKey<Consumer<GameTestHelper>> funcKey) {
        return registerFunction(context, instanceKey, funcKey, TestEnvironmentsPM.DEFAULT, TestUtils.DEFAULT_TEMPLATE);
    }

    private static Holder.Reference<GameTestInstance> registerFunction(BootstrapContext<GameTestInstance> context,
                                                                       ResourceKey<GameTestInstance> instanceKey,
                                                                       ResourceKey<Consumer<GameTestHelper>> funcKey,
                                                                       ResourceKey<TestEnvironmentDefinition> envKey) {
        return registerFunction(context, instanceKey, funcKey, envKey, TestUtils.DEFAULT_TEMPLATE);
    }


    private static Holder.Reference<GameTestInstance> registerFunction(BootstrapContext<GameTestInstance> context,
                                                                       ResourceKey<GameTestInstance> instanceKey,
                                                                       ResourceKey<Consumer<GameTestHelper>> funcKey,
                                                                       ResourceLocation templateLoc) {
        return registerFunction(context, instanceKey, funcKey, TestEnvironmentsPM.DEFAULT, templateLoc);
    }

    private static Holder.Reference<GameTestInstance> registerFunction(BootstrapContext<GameTestInstance> context,
                                                                       ResourceKey<GameTestInstance> instanceKey,
                                                                       ResourceKey<Consumer<GameTestHelper>> funcKey,
                                                                       ResourceKey<TestEnvironmentDefinition> envKey,
                                                                       ResourceLocation templateLoc) {
        HolderGetter<TestEnvironmentDefinition> envs = context.lookup(Registries.TEST_ENVIRONMENT);
        return registerFunction(context, instanceKey, funcKey, TestDataBuilder.withEnvironment(envKey, envs).template(templateLoc).build());
    }

    private static Holder.Reference<GameTestInstance> registerFunction(BootstrapContext<GameTestInstance> context,
                                                                       ResourceKey<GameTestInstance> instanceKey,
                                                                       ResourceKey<Consumer<GameTestHelper>> funcKey,
                                                                       TestData<Holder<TestEnvironmentDefinition>> testData) {
        return context.register(instanceKey, new FunctionGameTestInstance(funcKey, testData));
    }
}

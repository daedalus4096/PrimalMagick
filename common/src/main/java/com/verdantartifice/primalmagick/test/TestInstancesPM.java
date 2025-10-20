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

    public static void bootstrap(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, CANARY, TestFunctionsPM.CANARY.getKey());
        registerAttunementBuffTests(context);
        registerItemHandlerTests(context);
        registerPlayerKnowledgeTests(context);
        registerArcaneWorkbenchTests(context);
        registerCalcinatorTests(context);
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

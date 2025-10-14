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

    public static void bootstrap(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, CANARY, TestFunctionsPM.CANARY.getKey());
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

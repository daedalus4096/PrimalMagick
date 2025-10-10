package com.verdantartifice.primalmagick.test;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class TestEnvironmentsPM {
    public static final ResourceKey<TestEnvironmentDefinition> DEFAULT = ResourceKey.create(Registries.TEST_ENVIRONMENT, ResourceLocation.withDefaultNamespace("default"));

    public static final ResourceKey<TestEnvironmentDefinition> DAYTIME_ENV = ResourceKey.create(Registries.TEST_ENVIRONMENT, ResourceUtils.loc("daytime"));
    public static final ResourceKey<TestEnvironmentDefinition> NIGHTTIME_ENV = ResourceKey.create(Registries.TEST_ENVIRONMENT, ResourceUtils.loc("nighttime"));

    public static void bootstrap(BootstrapContext<TestEnvironmentDefinition> context) {
        context.register(DAYTIME_ENV, new TestEnvironmentDefinition.TimeOfDay(6000));
        context.register(NIGHTTIME_ENV, new TestEnvironmentDefinition.TimeOfDay(18000));
    }
}

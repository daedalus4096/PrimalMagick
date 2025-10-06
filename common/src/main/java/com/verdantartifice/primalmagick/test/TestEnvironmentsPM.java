package com.verdantartifice.primalmagick.test;

import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class TestEnvironmentsPM {
    public static final ResourceKey<TestEnvironmentDefinition> DEFAULT = ResourceKey.create(Registries.TEST_ENVIRONMENT, ResourceLocation.withDefaultNamespace("default"));;
}

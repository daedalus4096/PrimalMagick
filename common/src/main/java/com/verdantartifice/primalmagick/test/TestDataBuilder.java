package com.verdantartifice.primalmagick.test;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;

public class TestDataBuilder {
    private final Holder<TestEnvironmentDefinition> envHolder;
    private ResourceLocation template = TestUtils.DEFAULT_TEMPLATE;
    private int maxTicks = 400;
    private int setupTicks = 50;
    private boolean required = true;
    private Rotation rotation = Rotation.NONE;
    private boolean manualOnly = false;
    private int maxAttempts = 3;
    private int requiredSuccesses = 1;
    private boolean skyAccess = false;

    private TestDataBuilder(ResourceKey<TestEnvironmentDefinition> envKey, HolderGetter<TestEnvironmentDefinition> holderGetter) {
        this.envHolder = holderGetter.getOrThrow(envKey);
    }

    public static TestDataBuilder withEnvironment(ResourceKey<TestEnvironmentDefinition> envKey, HolderGetter<TestEnvironmentDefinition> holderGetter) {
        return new TestDataBuilder(envKey, holderGetter);
    }

    public TestDataBuilder template(ResourceLocation template) {
        this.template = template;
        return this;
    }

    public TestDataBuilder maxTicks(int maxTicks) {
        this.maxTicks = maxTicks;
        return this;
    }

    public TestDataBuilder setupTicks(int setupTicks) {
        this.setupTicks = setupTicks;
        return this;
    }

    public TestDataBuilder required(boolean required) {
        this.required = required;
        return this;
    }

    public TestDataBuilder rotation(Rotation rotation) {
        this.rotation = rotation;
        return this;
    }

    public TestDataBuilder manualOnly(boolean manualOnly) {
        this.manualOnly = manualOnly;
        return this;
    }

    public TestDataBuilder maxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
        return this;
    }

    public TestDataBuilder requiredSuccesses(int requiredSuccesses) {
        this.requiredSuccesses = requiredSuccesses;
        return this;
    }

    public TestDataBuilder skyAccess(boolean skyAccess) {
        this.skyAccess = skyAccess;
        return this;
    }

    public TestData<Holder<TestEnvironmentDefinition>> build() {
        return new TestData<>(this.envHolder, this.template, this.maxTicks, this.setupTicks, this.required, this.rotation, this.manualOnly, this.maxAttempts, this.requiredSuccesses, this.skyAccess);
    }
}

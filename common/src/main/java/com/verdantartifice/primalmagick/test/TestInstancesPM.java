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
import net.minecraft.world.level.block.Rotation;

import java.util.function.Consumer;

public class TestInstancesPM {
    public static final ResourceKey<GameTestInstance> CANARY = ResourceKey.create(Registries.TEST_INSTANCE, ResourceUtils.loc("canary"));

    public static void bootstrap(BootstrapContext<GameTestInstance> context) {
        registerFunction(context, CANARY, TestFunctionsPM.CANARY.getKey());
    }

    private static Holder.Reference<GameTestInstance> registerFunction(BootstrapContext<GameTestInstance> context,
                                                                       ResourceKey<GameTestInstance> instanceKey,
                                                                       ResourceKey<Consumer<GameTestHelper>> funcKey) {
        HolderGetter<TestEnvironmentDefinition> envs = context.lookup(Registries.TEST_ENVIRONMENT);
        return registerFunction(context, instanceKey, funcKey, new TestData<>(
                envs.getOrThrow(TestEnvironmentsPM.DEFAULT),
                TestUtils.DEFAULT_TEMPLATE,
                400,
                50,
                true,
                Rotation.NONE,
                false,
                3,
                1,
                false));
    }

    private static Holder.Reference<GameTestInstance> registerFunction(BootstrapContext<GameTestInstance> context,
                                                                       ResourceKey<GameTestInstance> instanceKey,
                                                                       ResourceKey<Consumer<GameTestHelper>> funcKey,
                                                                       TestData<Holder<TestEnvironmentDefinition>> testData) {
        return context.register(instanceKey, new FunctionGameTestInstance(funcKey, testData));
    }
}

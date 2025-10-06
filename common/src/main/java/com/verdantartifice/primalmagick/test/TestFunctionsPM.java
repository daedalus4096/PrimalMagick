package com.verdantartifice.primalmagick.test;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.gametest.framework.GameTestHelper;

import java.util.function.Consumer;

public class TestFunctionsPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.TEST_FUNCTIONS_REGISTRY.init();
    }

    public static final IRegistryItem<Consumer<GameTestHelper>, Consumer<GameTestHelper>> CANARY = Services.TEST_FUNCTIONS_REGISTRY.register("canary", () -> CanaryTest::canary);
}

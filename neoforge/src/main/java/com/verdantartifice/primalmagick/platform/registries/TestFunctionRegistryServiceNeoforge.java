package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.platform.services.registries.ITestFunctionRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TestFunctionRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<Consumer<GameTestHelper>> implements ITestFunctionRegistryService {
    private static final DeferredRegister<Consumer<GameTestHelper>> TYPES = DeferredRegister.create(Registries.TEST_FUNCTION, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<Consumer<GameTestHelper>>> getDeferredRegisterSupplier() {
        return () -> TYPES;
    }

    @Override
    protected Registry<Consumer<GameTestHelper>> getRegistry() {
        return BuiltInRegistries.TEST_FUNCTION;
    }
}

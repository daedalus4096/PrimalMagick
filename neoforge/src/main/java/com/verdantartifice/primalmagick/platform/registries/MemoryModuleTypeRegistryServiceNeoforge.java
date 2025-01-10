package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IMemoryModuleTypeRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the memory module type registry service.
 *
 * @author Daedalus4096
 */
public class MemoryModuleTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<MemoryModuleType<?>> implements IMemoryModuleTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<MemoryModuleType<?>>> getDeferredRegisterSupplier() {
        return MemoryModuleTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<MemoryModuleType<?>> getRegistry() {
        return BuiltInRegistries.MEMORY_MODULE_TYPE;
    }
}

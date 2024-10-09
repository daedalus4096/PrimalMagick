package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.IMemoryModuleTypeService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the memory module type registry service.
 *
 * @author Daedalus4096
 */
public class MemoryModuleTypeServiceForge extends AbstractRegistryServiceForge<MemoryModuleType<?>> implements IMemoryModuleTypeService {
    @Override
    protected Supplier<DeferredRegister<MemoryModuleType<?>>> getDeferredRegisterSupplier() {
        return MemoryModuleTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<MemoryModuleType<?>> getRegistry() {
        return BuiltInRegistries.MEMORY_MODULE_TYPE;
    }
}

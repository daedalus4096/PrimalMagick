package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.platform.services.registries.IDataComponentTypeRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the data component type registry service.
 *
 * @author Daedalus4096
 */
public class DataComponentTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<DataComponentType<?>> implements IDataComponentTypeRegistryService {
    private static final DeferredRegister<DataComponentType<?>> TYPES = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<DataComponentType<?>>> getDeferredRegisterSupplier() {
        return () -> TYPES;
    }

    @Override
    protected Registry<DataComponentType<?>> getRegistry() {
        return BuiltInRegistries.DATA_COMPONENT_TYPE;
    }
}

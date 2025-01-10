package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.components.DataComponentTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IDataComponentTypeRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the data component type registry service.
 *
 * @author Daedalus4096
 */
public class DataComponentTypeRegistryServiceForge extends AbstractBuiltInRegistryServiceForge<DataComponentType<?>> implements IDataComponentTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<DataComponentType<?>>> getDeferredRegisterSupplier() {
        return DataComponentTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<DataComponentType<?>> getRegistry() {
        return BuiltInRegistries.DATA_COMPONENT_TYPE;
    }
}

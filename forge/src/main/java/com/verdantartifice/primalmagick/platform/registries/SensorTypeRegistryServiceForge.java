package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.entities.ai.sensing.SensorTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.ISensorTypeRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the sensor type registry service.
 *
 * @author Daedalus4096
 */
public class SensorTypeRegistryServiceForge extends AbstractBuiltInRegistryServiceForge<SensorType<?>> implements ISensorTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<SensorType<?>>> getDeferredRegisterSupplier() {
        return SensorTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<SensorType<?>> getRegistry() {
        return BuiltInRegistries.SENSOR_TYPE;
    }
}

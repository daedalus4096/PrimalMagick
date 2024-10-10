package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.entities.ai.sensing.SensorTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.ISensorTypeService;
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
public class SensorTypeServiceForge extends AbstractBuiltInRegistryServiceForge<SensorType<?>> implements ISensorTypeService {
    @Override
    protected Supplier<DeferredRegister<SensorType<?>>> getDeferredRegisterSupplier() {
        return SensorTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<SensorType<?>> getRegistry() {
        return BuiltInRegistries.SENSOR_TYPE;
    }
}

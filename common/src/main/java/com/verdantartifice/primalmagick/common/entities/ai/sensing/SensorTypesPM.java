package com.verdantartifice.primalmagick.common.entities.ai.sensing;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;

import java.util.function.Supplier;

/**
 * Deferred registry for mod AI sensor types.
 * 
 * @author Daedalus4096
 */
public class SensorTypesPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.SENSOR_TYPES_REGISTRY.init();
    }

    public static final IRegistryItem<SensorType<?>, SensorType<TreefolkSpecificSensor>> TREEFOLK_SPECIFIC_SENSOR = register("treefolk_specific_sensor", () -> new SensorType<>(TreefolkSpecificSensor::new));
    public static final IRegistryItem<SensorType<?>, SensorType<NearestValidFertilizableBlockSensor>> NEAREST_VALID_FERTILIZABLE_BLOCKS = register("nearest_valid_fertilizable_block_sensor", () -> new SensorType<>(NearestValidFertilizableBlockSensor::new));

    private static <T extends Sensor<?>> IRegistryItem<SensorType<?>, SensorType<T>> register(String name, Supplier<SensorType<T>> supplier) {
        return Services.SENSOR_TYPES_REGISTRY.register(name, supplier);
    }
}

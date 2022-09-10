package com.verdantartifice.primalmagick.common.entities.ai.sensing;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod AI sensor types.
 * 
 * @author Daedalus4096
 */
public class SensorTypesPM {
    private static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, PrimalMagick.MODID);
    
    public static void init() {
        SENSOR_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<SensorType<TreefolkSpecificSensor>> TREEFOLK_SPECIFIC_SENSOR = SENSOR_TYPES.register("treefolk_specific_sensor", () -> new SensorType<>(TreefolkSpecificSensor::new));
    public static final RegistryObject<SensorType<NearestValidFertilizableBlockSensor>> NEAREST_VALID_FERTILIZABLE_BLOCKS = SENSOR_TYPES.register("nearest_valid_fertilizable_block_sensor", () -> new SensorType<>(NearestValidFertilizableBlockSensor::new));
}

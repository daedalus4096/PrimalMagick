package com.verdantartifice.primalmagick.common.entities.ai.sensing;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Deferred registry for mod sensor types in Forge.
 * 
 * @author Daedalus4096
 */
public class SensorTypeRegistration {
    private static final DeferredRegister<SensorType<?>> TYPES = DeferredRegister.create(Registries.SENSOR_TYPE, Constants.MOD_ID);

    public static DeferredRegister<SensorType<?>> getDeferredRegister() {
        return TYPES;
    }
    
    public static void init() {
        TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}

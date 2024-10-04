package com.verdantartifice.primalmagick.common.components;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Deferred registry for mod data component types in Forge.
 * 
 * @author Daedalus4096
 */
public class DataComponentTypeRegistration {
    private static final DeferredRegister<DataComponentType<?>> TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);

    public static DeferredRegister<DataComponentType<?>> getDeferredRegister() {
        return TYPES;
    }
    
    public static void init() {
        TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}

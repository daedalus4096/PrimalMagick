package com.verdantartifice.primalmagick.common.entities.ai.memory;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Deferred registry for mod memory module types in Forge.
 * 
 * @author Daedalus4096
 */
public class MemoryModuleTypeRegistration {
    private static final DeferredRegister<MemoryModuleType<?>> TYPES = DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, Constants.MOD_ID);

    public static DeferredRegister<MemoryModuleType<?>> getDeferredRegister() {
        return TYPES;
    }
    
    public static void init() {
        TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}

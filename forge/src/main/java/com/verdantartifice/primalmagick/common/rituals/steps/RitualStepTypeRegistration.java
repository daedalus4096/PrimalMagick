package com.verdantartifice.primalmagick.common.rituals.steps;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Deferred registry for mod ritual step types in Forge.
 * 
 * @author Daedalus4096
 */
public class RitualStepTypeRegistration {
    private static final DeferredRegister<RitualStepType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.RITUAL_STEP_TYPES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<RitualStepType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);

    public static DeferredRegister<RitualStepType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static Supplier<IForgeRegistry<RitualStepType<?>>> getRegistry() {
        return TYPES;
    }
    
    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}

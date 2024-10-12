package com.verdantartifice.primalmagick.common.rituals.steps;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

/**
 * Deferred registry for mod ritual step types in Neoforge.
 *
 * @author Daedalus4096
 */
public class RitualStepTypeRegistration {
    public static final Registry<RitualStepType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.RITUAL_STEP_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<RitualStepType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    public static DeferredRegister<RitualStepType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getEventBus());
    }
}

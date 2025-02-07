package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.rituals.steps.RitualStepType;
import com.verdantartifice.primalmagick.platform.services.registries.IRitualStepTypeRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the ritual step type registry service.
 *
 * @author Daedalus4096
 */
public class RitualStepTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<RitualStepType<?>> implements IRitualStepTypeRegistryService {
    public static final Registry<RitualStepType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.RITUAL_STEP_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<RitualStepType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<RitualStepType<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Registry<RitualStepType<?>> getRegistry() {
        return TYPES;
    }
}

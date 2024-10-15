package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.rituals.steps.RitualStepType;
import com.verdantartifice.primalmagick.common.rituals.steps.RitualStepTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IRitualStepTypeService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the ritual step type registry service.
 *
 * @author Daedalus4096
 */
public class RitualStepTypeServiceNeoforge extends AbstractRegistryServiceNeoforge<RitualStepType<?>> implements IRitualStepTypeService {
    @Override
    protected Supplier<DeferredRegister<RitualStepType<?>>> getDeferredRegisterSupplier() {
        return RitualStepTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<RitualStepType<?>> getRegistry() {
        return RitualStepTypeRegistration.TYPES;
    }
}

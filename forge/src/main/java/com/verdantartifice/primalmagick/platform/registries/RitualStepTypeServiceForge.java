package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.rituals.steps.RitualStepType;
import com.verdantartifice.primalmagick.common.rituals.steps.RitualStepTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IRitualStepTypeService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the ritual step type registry service.
 *
 * @author Daedalus4096
 */
public class RitualStepTypeServiceForge extends AbstractCustomRegistryServiceForge<RitualStepType<?>> implements IRitualStepTypeService {
    @Override
    protected Supplier<DeferredRegister<RitualStepType<?>>> getDeferredRegisterSupplier() {
        return RitualStepTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Supplier<IForgeRegistry<RitualStepType<?>>> getRegistry() {
        return RitualStepTypeRegistration.getRegistry();
    }
}

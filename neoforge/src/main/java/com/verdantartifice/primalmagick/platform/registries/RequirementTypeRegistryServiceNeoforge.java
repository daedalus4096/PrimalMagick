package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.research.requirements.RequirementType;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IRequirementTypeRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the requirement type registry service.
 *
 * @author Daedalus4096
 */
public class RequirementTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<RequirementType<?>> implements IRequirementTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<RequirementType<?>>> getDeferredRegisterSupplier() {
        return RequirementTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<RequirementType<?>> getRegistry() {
        return RequirementTypeRegistration.TYPES;
    }
}

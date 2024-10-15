package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.research.requirements.RequirementType;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IRequirementTypeService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the requirement type registry service.
 *
 * @author Daedalus4096
 */
public class RequirementTypeServiceForge extends AbstractCustomRegistryServiceForge<RequirementType<?>> implements IRequirementTypeService {
    @Override
    protected Supplier<DeferredRegister<RequirementType<?>>> getDeferredRegisterSupplier() {
        return RequirementTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Supplier<IForgeRegistry<RequirementType<?>>> getRegistry() {
        return RequirementTypeRegistration.getRegistry();
    }
}

package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.research.keys.ResearchKeyType;
import com.verdantartifice.primalmagick.common.research.keys.ResearchKeyTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.IResearchKeyTypeService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the research key type registry service.
 *
 * @author Daedalus4096
 */
public class ResearchKeyTypeServiceNeoforge extends AbstractRegistryServiceNeoforge<ResearchKeyType<?>> implements IResearchKeyTypeService {
    @Override
    protected Supplier<DeferredRegister<ResearchKeyType<?>>> getDeferredRegisterSupplier() {
        return ResearchKeyTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<ResearchKeyType<?>> getRegistry() {
        return ResearchKeyTypeRegistration.TYPES;
    }
}
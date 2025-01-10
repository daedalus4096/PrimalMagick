package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.research.keys.ResearchKeyType;
import com.verdantartifice.primalmagick.common.research.keys.ResearchKeyTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IResearchKeyTypeRegistryService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the research key type registry service.
 *
 * @author Daedalus4096
 */
public class ResearchKeyTypeRegistryServiceForge extends AbstractCustomRegistryServiceForge<ResearchKeyType<?>> implements IResearchKeyTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<ResearchKeyType<?>>> getDeferredRegisterSupplier() {
        return ResearchKeyTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Supplier<IForgeRegistry<ResearchKeyType<?>>> getRegistry() {
        return ResearchKeyTypeRegistration.getRegistry();
    }
}

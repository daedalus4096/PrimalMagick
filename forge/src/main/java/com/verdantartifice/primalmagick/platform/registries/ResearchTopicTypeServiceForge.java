package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.research.topics.ResearchTopicType;
import com.verdantartifice.primalmagick.common.research.topics.ResearchTopicTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IResearchTopicTypeService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the research topic type registry service.
 *
 * @author Daedalus4096
 */
public class ResearchTopicTypeServiceForge extends AbstractCustomRegistryServiceForge<ResearchTopicType<?>> implements IResearchTopicTypeService {
    @Override
    protected Supplier<DeferredRegister<ResearchTopicType<?>>> getDeferredRegisterSupplier() {
        return ResearchTopicTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Supplier<IForgeRegistry<ResearchTopicType<?>>> getRegistry() {
        return ResearchTopicTypeRegistration.getRegistry();
    }
}

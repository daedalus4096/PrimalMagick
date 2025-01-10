package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.research.topics.ResearchTopicType;
import com.verdantartifice.primalmagick.common.research.topics.ResearchTopicTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IResearchTopicTypeRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the research topic type registry service.
 *
 * @author Daedalus4096
 */
public class ResearchTopicTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<ResearchTopicType<?>> implements IResearchTopicTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<ResearchTopicType<?>>> getDeferredRegisterSupplier() {
        return ResearchTopicTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<ResearchTopicType<?>> getRegistry() {
        return ResearchTopicTypeRegistration.TYPES;
    }
}

package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.topics.ResearchTopicType;
import com.verdantartifice.primalmagick.platform.services.registries.IResearchTopicTypeRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the research topic type registry service.
 *
 * @author Daedalus4096
 */
public class ResearchTopicTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<ResearchTopicType<?>> implements IResearchTopicTypeRegistryService {
    public static final Registry<ResearchTopicType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.RESEARCH_TOPIC_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<ResearchTopicType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<ResearchTopicType<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Registry<ResearchTopicType<?>> getRegistry() {
        return TYPES;
    }
}

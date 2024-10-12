package com.verdantartifice.primalmagick.common.research.topics;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

/**
 * Deferred registry for mod research topic types in Neoforge.
 *
 * @author Daedalus4096
 */
public class ResearchTopicTypeRegistration {
    public static final Registry<ResearchTopicType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.RESEARCH_TOPIC_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<ResearchTopicType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    public static DeferredRegister<ResearchTopicType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getEventBus());
    }
}

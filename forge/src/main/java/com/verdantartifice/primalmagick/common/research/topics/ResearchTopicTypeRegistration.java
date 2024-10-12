package com.verdantartifice.primalmagick.common.research.topics;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Deferred registry for mod research topic types in Forge.
 * 
 * @author Daedalus4096
 */
public class ResearchTopicTypeRegistration {
    private static final DeferredRegister<ResearchTopicType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.RESEARCH_TOPIC_TYPES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<ResearchTopicType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);

    public static DeferredRegister<ResearchTopicType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static Supplier<IForgeRegistry<ResearchTopicType<?>>> getRegistry() {
        return TYPES;
    }
    
    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}

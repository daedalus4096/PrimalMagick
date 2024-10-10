package com.verdantartifice.primalmagick.common.research.keys;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Deferred registry for mod research key types in Forge.
 * 
 * @author Daedalus4096
 */
public class ResearchKeyTypeRegistration {
    private static final DeferredRegister<ResearchKeyType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.RESEARCH_KEY_TYPES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<ResearchKeyType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);

    public static DeferredRegister<ResearchKeyType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static Supplier<IForgeRegistry<ResearchKeyType<?>>> getRegistry() {
        return TYPES;
    }
    
    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}

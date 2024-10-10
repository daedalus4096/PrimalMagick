package com.verdantartifice.primalmagick.common.research.keys;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

/**
 * Deferred registry for mod research key types in Neoforge.
 *
 * @author Daedalus4096
 */
public class ResearchKeyTypeRegistration {
    public static final Registry<ResearchKeyType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.RESEARCH_KEY_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<ResearchKeyType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    public static DeferredRegister<ResearchKeyType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getEventBus());
    }
}

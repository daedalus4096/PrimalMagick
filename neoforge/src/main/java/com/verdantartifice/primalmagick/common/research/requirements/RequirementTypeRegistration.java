package com.verdantartifice.primalmagick.common.research.requirements;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

/**
 * Deferred registry for mod requirement types in Neoforge.
 *
 * @author Daedalus4096
 */
public class RequirementTypeRegistration {
    public static final Registry<RequirementType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.REQUIREMENT_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<RequirementType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    public static DeferredRegister<RequirementType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getEventBus());
    }
}

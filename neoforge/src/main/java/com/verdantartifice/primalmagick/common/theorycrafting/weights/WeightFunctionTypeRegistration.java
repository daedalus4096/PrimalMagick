package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

/**
 * Deferred registry for mod weight function types in Neoforge.
 *
 * @author Daedalus4096
 */
public class WeightFunctionTypeRegistration {
    public static final Registry<WeightFunctionType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.PROJECT_WEIGHT_FUNCTION_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<WeightFunctionType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    public static DeferredRegister<WeightFunctionType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getEventBus());
    }
}

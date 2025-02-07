package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.WeightFunctionType;
import com.verdantartifice.primalmagick.platform.services.registries.IWeightFunctionTypeRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the weight function type registry service.
 *
 * @author Daedalus4096
 */
public class WeightFunctionTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<WeightFunctionType<?>> implements IWeightFunctionTypeRegistryService {
    public static final Registry<WeightFunctionType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.PROJECT_WEIGHT_FUNCTION_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<WeightFunctionType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<WeightFunctionType<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Registry<WeightFunctionType<?>> getRegistry() {
        return TYPES;
    }
}

package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.theorycrafting.weights.WeightFunctionType;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.WeightFunctionTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IWeightFunctionTypeRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the weight function type registry service.
 *
 * @author Daedalus4096
 */
public class WeightFunctionTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<WeightFunctionType<?>> implements IWeightFunctionTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<WeightFunctionType<?>>> getDeferredRegisterSupplier() {
        return WeightFunctionTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<WeightFunctionType<?>> getRegistry() {
        return WeightFunctionTypeRegistration.TYPES;
    }
}

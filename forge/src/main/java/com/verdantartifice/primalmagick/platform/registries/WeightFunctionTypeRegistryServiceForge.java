package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.theorycrafting.weights.WeightFunctionType;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.WeightFunctionTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IWeightFunctionTypeRegistryService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the weight function type registry service.
 *
 * @author Daedalus4096
 */
public class WeightFunctionTypeRegistryServiceForge extends AbstractCustomRegistryServiceForge<WeightFunctionType<?>> implements IWeightFunctionTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<WeightFunctionType<?>>> getDeferredRegisterSupplier() {
        return WeightFunctionTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Supplier<IForgeRegistry<WeightFunctionType<?>>> getRegistry() {
        return WeightFunctionTypeRegistration.getRegistry();
    }
}

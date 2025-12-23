package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.platform.services.registries.IAffinityTypeRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the affinity type registry service.
 *
 * @author Daedalus4096
 */
public class AffinityTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<AffinityType<?>> implements IAffinityTypeRegistryService {
    public static final Registry<AffinityType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.AFFINITY_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<AffinityType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<AffinityType<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Registry<AffinityType<?>> getRegistry() {
        return TYPES;
    }
}

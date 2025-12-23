package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.platform.services.registries.IAffinityTypeRegistryService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Forge implementation of the affinity type registry service.
 *
 * @author Daedalus4096
 */
public class AffinityTypeRegistryServiceForge extends AbstractCustomRegistryServiceForge<AffinityType<?>> implements IAffinityTypeRegistryService {
    private static final DeferredRegister<AffinityType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.AFFINITY_TYPES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<AffinityType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);

    @Override
    protected Supplier<DeferredRegister<AffinityType<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Supplier<IForgeRegistry<AffinityType<?>>> getRegistry() {
        return TYPES;
    }
}

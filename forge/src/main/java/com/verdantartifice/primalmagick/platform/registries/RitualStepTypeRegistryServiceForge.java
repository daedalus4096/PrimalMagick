package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.rituals.steps.RitualStepType;
import com.verdantartifice.primalmagick.platform.services.registries.IRitualStepTypeRegistryService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Forge implementation of the ritual step type registry service.
 *
 * @author Daedalus4096
 */
public class RitualStepTypeRegistryServiceForge extends AbstractCustomRegistryServiceForge<RitualStepType<?>> implements IRitualStepTypeRegistryService {
    private static final DeferredRegister<RitualStepType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.RITUAL_STEP_TYPES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<RitualStepType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);

    @Override
    protected Supplier<DeferredRegister<RitualStepType<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Supplier<IForgeRegistry<RitualStepType<?>>> getRegistry() {
        return TYPES;
    }
}

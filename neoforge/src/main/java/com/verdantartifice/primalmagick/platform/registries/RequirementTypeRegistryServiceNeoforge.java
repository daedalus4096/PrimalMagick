package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementType;
import com.verdantartifice.primalmagick.platform.services.registries.IRequirementTypeRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the requirement type registry service.
 *
 * @author Daedalus4096
 */
public class RequirementTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<RequirementType<?>> implements IRequirementTypeRegistryService {
    public static final Registry<RequirementType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.REQUIREMENT_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<RequirementType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<RequirementType<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Registry<RequirementType<?>> getRegistry() {
        return TYPES;
    }
}

package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.platform.services.registries.ICriterionTriggerRegistryService;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CriterionTriggerRegistryServiceForge extends AbstractBuiltInRegistryServiceForge<CriterionTrigger<?>> implements ICriterionTriggerRegistryService {
    private static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<CriterionTrigger<?>>> getDeferredRegisterSupplier() {
        return () -> TRIGGERS;
    }

    @Override
    protected Registry<CriterionTrigger<?>> getRegistry() {
        return BuiltInRegistries.TRIGGER_TYPES;
    }
}

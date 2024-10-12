package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadType;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.ISpellPayloadTypeService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the spell payload type registry service.
 *
 * @author Daedalus4096
 */
public class SpellPayloadTypeServiceNeoforge extends AbstractRegistryServiceNeoforge<SpellPayloadType<?>> implements ISpellPayloadTypeService {
    @Override
    protected Supplier<DeferredRegister<SpellPayloadType<?>>> getDeferredRegisterSupplier() {
        return SpellPayloadTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<SpellPayloadType<?>> getRegistry() {
        return SpellPayloadTypeRegistration.TYPES;
    }
}

package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadType;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellPayloadTypeRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the spell payload type registry service.
 *
 * @author Daedalus4096
 */
public class SpellPayloadTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<SpellPayloadType<?>> implements ISpellPayloadTypeRegistryService {
    public static final Registry<SpellPayloadType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.SPELL_PAYLOAD_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<SpellPayloadType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<SpellPayloadType<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Registry<SpellPayloadType<?>> getRegistry() {
        return TYPES;
    }
}

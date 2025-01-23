package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadType;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellPayloadTypeRegistryService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Forge implementation of the spell payload type registry service.
 *
 * @author Daedalus4096
 */
public class SpellPayloadTypeRegistryServiceForge extends AbstractCustomRegistryServiceForge<SpellPayloadType<?>> implements ISpellPayloadTypeRegistryService {
    private static final DeferredRegister<SpellPayloadType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.SPELL_PAYLOAD_TYPES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<SpellPayloadType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);

    @Override
    protected Supplier<DeferredRegister<SpellPayloadType<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Supplier<IForgeRegistry<SpellPayloadType<?>>> getRegistry() {
        return TYPES;
    }
}

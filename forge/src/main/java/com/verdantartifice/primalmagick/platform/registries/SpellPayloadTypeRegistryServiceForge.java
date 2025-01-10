package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadType;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellPayloadTypeRegistryService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the spell payload type registry service.
 *
 * @author Daedalus4096
 */
public class SpellPayloadTypeRegistryServiceForge extends AbstractCustomRegistryServiceForge<SpellPayloadType<?>> implements ISpellPayloadTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<SpellPayloadType<?>>> getDeferredRegisterSupplier() {
        return SpellPayloadTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Supplier<IForgeRegistry<SpellPayloadType<?>>> getRegistry() {
        return SpellPayloadTypeRegistration.getRegistry();
    }
}

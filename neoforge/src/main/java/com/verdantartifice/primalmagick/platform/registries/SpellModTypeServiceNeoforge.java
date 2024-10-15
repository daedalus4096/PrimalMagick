package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.spells.mods.SpellModType;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellModTypeService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the spell mod type registry service.
 *
 * @author Daedalus4096
 */
public class SpellModTypeServiceNeoforge extends AbstractRegistryServiceNeoforge<SpellModType<?>> implements ISpellModTypeService {
    @Override
    protected Supplier<DeferredRegister<SpellModType<?>>> getDeferredRegisterSupplier() {
        return SpellModTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<SpellModType<?>> getRegistry() {
        return SpellModTypeRegistration.TYPES;
    }
}

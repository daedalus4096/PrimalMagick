package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellPropertyService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the spell property registry service.
 *
 * @author Daedalus4096
 */
public class SpellPropertyServiceNeoforge extends AbstractRegistryServiceNeoforge<SpellProperty> implements ISpellPropertyService {
    @Override
    protected Supplier<DeferredRegister<SpellProperty>> getDeferredRegisterSupplier() {
        return SpellPropertyRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<SpellProperty> getRegistry() {
        return SpellPropertyRegistration.PROPERTIES;
    }
}

package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellPropertyRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the spell property registry service.
 *
 * @author Daedalus4096
 */
public class SpellPropertyRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<SpellProperty> implements ISpellPropertyRegistryService {
    public static final Registry<SpellProperty> PROPERTIES = new RegistryBuilder<>(RegistryKeysPM.SPELL_PROPERTIES)
            .sync(true)
            .create();
    private static final DeferredRegister<SpellProperty> DEFERRED_PROPERTIES = DeferredRegister.create(PROPERTIES, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<SpellProperty>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_PROPERTIES;
    }

    @Override
    protected Registry<SpellProperty> getRegistry() {
        return PROPERTIES;
    }
}

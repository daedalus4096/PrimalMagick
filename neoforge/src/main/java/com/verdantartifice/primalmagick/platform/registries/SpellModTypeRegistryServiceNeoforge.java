package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModType;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellModTypeRegistryService;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the spell mod type registry service.
 *
 * @author Daedalus4096
 */
public class SpellModTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<SpellModType<?>> implements ISpellModTypeRegistryService {
    public static final Registry<SpellModType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.SPELL_MOD_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<SpellModType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<SpellModType<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Registry<SpellModType<?>> getRegistry() {
        return TYPES;
    }
}

package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModType;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellModTypeRegistryService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Forge implementation of the spell mod type registry service.
 *
 * @author Daedalus4096
 */
public class SpellModTypeRegistryServiceForge extends AbstractCustomRegistryServiceForge<SpellModType<?>> implements ISpellModTypeRegistryService {
    private static final DeferredRegister<SpellModType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.SPELL_MOD_TYPES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<SpellModType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);

    @Override
    protected Supplier<DeferredRegister<SpellModType<?>>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_TYPES;
    }

    @Override
    protected Supplier<IForgeRegistry<SpellModType<?>>> getRegistry() {
        return TYPES;
    }
}

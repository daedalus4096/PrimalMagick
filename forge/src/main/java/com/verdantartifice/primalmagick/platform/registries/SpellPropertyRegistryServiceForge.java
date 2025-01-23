package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellPropertyRegistryService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Forge implementation of the spell property registry service.
 *
 * @author Daedalus4096
 */
public class SpellPropertyRegistryServiceForge extends AbstractCustomRegistryServiceForge<SpellProperty> implements ISpellPropertyRegistryService {
    private static final DeferredRegister<SpellProperty> DEFERRED_PROPERTIES = DeferredRegister.create(RegistryKeysPM.SPELL_PROPERTIES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<SpellProperty>> PROPERTIES = DEFERRED_PROPERTIES.makeRegistry(() -> new RegistryBuilder<SpellProperty>().hasTags());

    @Override
    protected Supplier<DeferredRegister<SpellProperty>> getDeferredRegisterSupplier() {
        return () -> DEFERRED_PROPERTIES;
    }

    @Override
    protected Supplier<IForgeRegistry<SpellProperty>> getRegistry() {
        return PROPERTIES;
    }
}

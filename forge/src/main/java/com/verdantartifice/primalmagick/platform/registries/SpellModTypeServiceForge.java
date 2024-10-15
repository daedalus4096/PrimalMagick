package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.spells.mods.SpellModType;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.ISpellModTypeService;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the spell mod type registry service.
 *
 * @author Daedalus4096
 */
public class SpellModTypeServiceForge extends AbstractCustomRegistryServiceForge<SpellModType<?>> implements ISpellModTypeService {
    @Override
    protected Supplier<DeferredRegister<SpellModType<?>>> getDeferredRegisterSupplier() {
        return SpellModTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Supplier<IForgeRegistry<SpellModType<?>>> getRegistry() {
        return SpellModTypeRegistration.getRegistry();
    }
}

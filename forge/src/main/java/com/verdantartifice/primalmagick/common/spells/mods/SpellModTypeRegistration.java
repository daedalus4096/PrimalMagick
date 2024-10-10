package com.verdantartifice.primalmagick.common.spells.mods;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Deferred registry for mod spell mod types in Forge.
 * 
 * @author Daedalus4096
 */
public class SpellModTypeRegistration {
    private static final DeferredRegister<SpellModType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.SPELL_MOD_TYPES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<SpellModType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);

    public static DeferredRegister<SpellModType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static Supplier<IForgeRegistry<SpellModType<?>>> getRegistry() {
        return TYPES;
    }
    
    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}

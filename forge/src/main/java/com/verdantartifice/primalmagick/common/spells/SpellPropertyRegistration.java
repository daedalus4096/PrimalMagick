package com.verdantartifice.primalmagick.common.spells;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Deferred registry for mod spell properties in Forge.
 * 
 * @author Daedalus4096
 */
public class SpellPropertyRegistration {
    private static final DeferredRegister<SpellProperty> DEFERRED_PROPERTIES = DeferredRegister.create(RegistryKeysPM.SPELL_PROPERTIES, Constants.MOD_ID);
    private static final Supplier<IForgeRegistry<SpellProperty>> PROPERTIES = DEFERRED_PROPERTIES.makeRegistry(() -> new RegistryBuilder<SpellProperty>().hasTags());

    public static DeferredRegister<SpellProperty> getDeferredRegister() {
        return DEFERRED_PROPERTIES;
    }

    public static Supplier<IForgeRegistry<SpellProperty>> getRegistry() {
        return PROPERTIES;
    }
    
    public static void init() {
        DEFERRED_PROPERTIES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}

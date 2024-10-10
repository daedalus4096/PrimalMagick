package com.verdantartifice.primalmagick.common.spells;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

/**
 * Deferred registry for mod spell properties in Neoforge.
 *
 * @author Daedalus4096
 */
public class SpellPropertyRegistration {
    public static final Registry<SpellProperty> PROPERTIES = new RegistryBuilder<>(RegistryKeysPM.SPELL_PROPERTIES)
            .sync(true)
            .create();
    private static final DeferredRegister<SpellProperty> DEFERRED_PROPERTIES = DeferredRegister.create(PROPERTIES, Constants.MOD_ID);

    public static DeferredRegister<SpellProperty> getDeferredRegister() {
        return DEFERRED_PROPERTIES;
    }

    public static void init() {
        DEFERRED_PROPERTIES.register(PrimalMagick.getEventBus());
    }
}

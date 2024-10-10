package com.verdantartifice.primalmagick.common.spells.mods;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

/**
 * Deferred registry for mod spell mod types in Neoforge.
 *
 * @author Daedalus4096
 */
public class SpellModTypeRegistration {
    public static final Registry<SpellModType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.SPELL_MOD_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<SpellModType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    public static DeferredRegister<SpellModType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getEventBus());
    }
}

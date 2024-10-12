package com.verdantartifice.primalmagick.common.spells.payloads;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

/**
 * Deferred registry for mod spell payload types in Neoforge.
 *
 * @author Daedalus4096
 */
public class SpellPayloadTypeRegistration {
    public static final Registry<SpellPayloadType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.SPELL_PAYLOAD_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<SpellPayloadType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    public static DeferredRegister<SpellPayloadType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getEventBus());
    }
}

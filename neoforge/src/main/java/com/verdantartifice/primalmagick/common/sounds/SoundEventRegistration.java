package com.verdantartifice.primalmagick.common.sounds;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Deferred registry for mod sound events in Neoforge.
 *
 * @author Daedalus4096
 */
public class SoundEventRegistration {
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, Constants.MOD_ID);

    public static DeferredRegister<SoundEvent> getDeferredRegister() {
        return SOUNDS;
    }

    public static void init() {
        SOUNDS.register(PrimalMagick.getEventBus());
    }
}

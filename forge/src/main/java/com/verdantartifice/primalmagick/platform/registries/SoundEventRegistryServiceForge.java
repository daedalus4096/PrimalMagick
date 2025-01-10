package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.sounds.SoundEventRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.ISoundEventRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the sound event registry service.
 *
 * @author Daedalus4096
 */
public class SoundEventRegistryServiceForge extends AbstractBuiltInRegistryServiceForge<SoundEvent> implements ISoundEventRegistryService {
    @Override
    protected Supplier<DeferredRegister<SoundEvent>> getDeferredRegisterSupplier() {
        return SoundEventRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<SoundEvent> getRegistry() {
        return BuiltInRegistries.SOUND_EVENT;
    }
}

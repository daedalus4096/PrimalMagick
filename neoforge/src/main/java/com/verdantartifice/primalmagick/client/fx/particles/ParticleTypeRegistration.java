package com.verdantartifice.primalmagick.client.fx.particles;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Deferred registry for mod particle types in Neoforge.
 *
 * @author Daedalus4096
 */
public class ParticleTypeRegistration {
    private static final DeferredRegister<ParticleType<?>> TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, Constants.MOD_ID);

    public static DeferredRegister<ParticleType<?>> getDeferredRegister() {
        return TYPES;
    }

    public static void init() {
        TYPES.register(PrimalMagick.getEventBus());
    }
}

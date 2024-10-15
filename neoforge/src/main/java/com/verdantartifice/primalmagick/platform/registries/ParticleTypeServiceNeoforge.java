package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IParticleTypeService;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the particle type registry service.
 *
 * @author Daedalus4096
 */
public class ParticleTypeServiceNeoforge extends AbstractRegistryServiceNeoforge<ParticleType<?>> implements IParticleTypeService {
    @Override
    protected Supplier<DeferredRegister<ParticleType<?>>> getDeferredRegisterSupplier() {
        return ParticleTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<ParticleType<?>> getRegistry() {
        return BuiltInRegistries.PARTICLE_TYPE;
    }
}

package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.IParticleTypeService;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the particle type registry service.
 *
 * @author Daedalus4096
 */
public class ParticleTypeServiceForge extends AbstractRegistryServiceForge<ParticleType<?>> implements IParticleTypeService {
    @Override
    protected Supplier<DeferredRegister<ParticleType<?>>> getDeferredRegisterSupplier() {
        return ParticleTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<ParticleType<?>> getRegistry() {
        return BuiltInRegistries.PARTICLE_TYPE;
    }
}

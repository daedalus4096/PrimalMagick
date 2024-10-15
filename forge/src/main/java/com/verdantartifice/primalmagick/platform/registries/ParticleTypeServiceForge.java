package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypeRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IParticleTypeService;
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
public class ParticleTypeServiceForge extends AbstractBuiltInRegistryServiceForge<ParticleType<?>> implements IParticleTypeService {
    @Override
    protected Supplier<DeferredRegister<ParticleType<?>>> getDeferredRegisterSupplier() {
        return ParticleTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<ParticleType<?>> getRegistry() {
        return BuiltInRegistries.PARTICLE_TYPE;
    }
}

package com.verdantartifice.primalmagick.client.fx.particles;

import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import org.jetbrains.annotations.NotNull;

/**
 * Particle group for spell bolts, to keep track of the particle type and create its render state.
 *
 * @author Daedalus4096
 */
public class SpellBoltParticleGroup extends ParticleGroup<SpellBoltParticle> {
    public SpellBoltParticleGroup(ParticleEngine engine) {
        super(engine);
    }

    @Override
    @NotNull
    public ParticleGroupRenderState extractRenderState(@NotNull Frustum frustum, @NotNull Camera camera, float partialTick) {
        return new SpellBoltParticleGroupRenderState(this.particles.stream().map(SpellBoltParticle::getRenderState).toList());
    }
}

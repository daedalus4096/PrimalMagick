package com.verdantartifice.primalmagick.common.entities.projectiles;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.misc.SinCrystalEntity;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellTrailPacket;
import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.PowerParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Entity definition for an inner demon's sin crash projectile.
 * 
 * @author Daedalus4096
 */
public class SinCrashEntity extends AbstractHurtingProjectile {
    public SinCrashEntity(EntityType<? extends SinCrashEntity> entityType, Level world) {
        super(entityType, world);
    }
    
    public SinCrashEntity(Level world, LivingEntity shooter, Vec3 motion) {
        super(EntityTypesPM.SIN_CRASH.get(), shooter, motion, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel && this.isAlive() && this.tickCount % 2 == 0) {
            // Leave a trail of particles in this entity's wake
            PacketHandler.sendToAllAround(
                    new SpellTrailPacket(this.position(), Sources.VOID.getColor()),
                    serverLevel,
                    this.blockPosition(), 
                    64.0D);
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        // Only impact when hitting a block
        super.onHitBlock(result);
        Level level = this.level();
        if (!level.isClientSide()) {
            SinCrystalEntity crystal = new SinCrystalEntity(level, result.getLocation().x, result.getLocation().y, result.getLocation().z);
            level.addFreshEntity(crystal);
            this.discard();
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource source, float amount) {
        return false;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F);
    }
}

package com.verdantartifice.primalmagic.common.entities.projectiles;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.entities.misc.SinCrystalEntity;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.SpellTrailPacket;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

/**
 * Entity definition for an inner demon's sin crash projectile.
 * 
 * @author Daedalus4096
 */
public class SinCrashEntity extends AbstractHurtingProjectile {
    public SinCrashEntity(EntityType<? extends SinCrashEntity> entityType, Level world) {
        super(entityType, world);
    }
    
    public SinCrashEntity(Level world, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(EntityTypesPM.SIN_CRASH.get(), shooter, accelX, accelY, accelZ, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.isAlive() && this.tickCount % 2 == 0) {
            // Leave a trail of particles in this entity's wake
            PacketHandler.sendToAllAround(
                    new SpellTrailPacket(this.position(), Source.VOID.getColor()), 
                    this.level.dimension(), 
                    this.blockPosition(), 
                    64.0D);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        // Only impact when hitting a block
        super.onHitBlock(result);
        if (!this.level.isClientSide) {
            SinCrystalEntity crystal = new SinCrystalEntity(this.level, result.getLocation().x, result.getLocation().y, result.getLocation().z);
            this.level.addFreshEntity(crystal);
            this.remove();
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.DRAGON_BREATH;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

package com.verdantartifice.primalmagic.common.entities.projectiles;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.SpellTrailPacket;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Entity definition for an inner demon's sin crash projectile.
 * 
 * @author Daedalus4096
 */
public class SinCrashEntity extends DamagingProjectileEntity {
    public SinCrashEntity(EntityType<? extends SinCrashEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public SinCrashEntity(World world, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(EntityTypesPM.SIN_CRASH.get(), shooter, accelX, accelY, accelZ, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isRemote && this.isAlive() && this.ticksExisted % 2 == 0) {
            // Leave a trail of particles in this entity's wake
            PacketHandler.sendToAllAround(
                    new SpellTrailPacket(this.getPositionVec(), Source.VOID.getColor()), 
                    this.world.getDimensionKey(), 
                    this.getPosition(), 
                    64.0D);
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if (!this.world.isRemote) {
            // TODO Spawn sin crystal
            this.remove();
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    protected IParticleData getParticle() {
        return ParticleTypes.DRAGON_BREATH;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

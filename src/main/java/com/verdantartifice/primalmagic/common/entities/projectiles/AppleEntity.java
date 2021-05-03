package com.verdantartifice.primalmagic.common.entities.projectiles;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Definition for a thrown apple entity.  Thrown by treefolk.  Does light damage on impact.
 * 
 * @author Daedalus4096
 */
public class AppleEntity extends ProjectileItemEntity {
    public AppleEntity(EntityType<? extends AppleEntity> type, World worldIn) {
        super(type, worldIn);
    }
    
    public AppleEntity(World world, LivingEntity thrower) {
        super(EntityTypesPM.APPLE.get(), thrower, world);
    }
    
    public AppleEntity(World world, double x, double y, double z) {
        super(EntityTypesPM.APPLE.get(), x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.APPLE;
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData makeParticle() {
        return new ItemParticleData(ParticleTypes.ITEM, this.getItem());
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            IParticleData particleData = this.makeParticle();
            for (int index = 0; index < 8; index++) {
                this.world.addParticle(particleData, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        result.getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), 2.0F);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

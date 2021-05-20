package com.verdantartifice.primalmagic.common.entities.misc;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Definition of a sin crystal entity.  Created by an inner demon to heal it, similar to an ender crystal.
 * 
 * @author Daedalus4096
 */
public class SinCrystalEntity extends Entity {
    private static final DataParameter<Optional<BlockPos>> BEAM_TARGET = EntityDataManager.createKey(SinCrystalEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
    private static final DataParameter<Optional<UUID>> DAMAGE_CLOUD = EntityDataManager.createKey(SinCrystalEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    
    public int innerRotation;

    public SinCrystalEntity(EntityType<? extends SinCrystalEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.preventEntitySpawning = true;
        this.innerRotation = this.rand.nextInt(100000);
    }
    
    public SinCrystalEntity(World worldIn, double x, double y, double z) {
        this(EntityTypesPM.SIN_CRYSTAL.get(), worldIn);
        this.setPosition(x, y, z);
    }

    @Override
    protected void registerData() {
        this.getDataManager().register(BEAM_TARGET, Optional.empty());
        this.getDataManager().register(DAMAGE_CLOUD, Optional.empty());
    }

    @Override
    public void tick() {
        super.tick();
        this.innerRotation++;
        
        // Create or extend damage cloud
        if (!this.world.isRemote && this.world instanceof ServerWorld) {
            UUID cloudId = this.getDamageCloud();
            if (cloudId == null) {
                AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ());
                cloud.setParticleData(ParticleTypes.DRAGON_BREATH);
                cloud.setRadius(3.0F);
                cloud.setDuration(5);
                cloud.setWaitTime(0);
                cloud.addEffect(new EffectInstance(Effects.INSTANT_DAMAGE, 1, 1));
                this.world.addEntity(cloud);
                this.setDamageCloud(cloud.getUniqueID());
            } else {
                ServerWorld serverWorld = (ServerWorld)this.world;
                Entity entity = serverWorld.getEntityByUuid(cloudId);
                if (entity instanceof AreaEffectCloudEntity) {
                    AreaEffectCloudEntity cloud = (AreaEffectCloudEntity)entity;
                    cloud.setDuration(1 + cloud.getDuration());     // Extend the cloud's duration by a tick so that it lives as long as the crystal
                } else {
                    this.setDamageCloud(null);
                }
            }
        }
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        if (compound.contains("BeamTarget", Constants.NBT.TAG_COMPOUND)) {
            this.setBeamTarget(NBTUtil.readBlockPos(compound.getCompound("BeamTarget")));
        }
        if (compound.contains("DamageCloudUUID", Constants.NBT.TAG_COMPOUND)) {
            this.setDamageCloud(compound.getUniqueId("DamageCloudUUID"));
        }
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        if (this.getBeamTarget() != null) {
            compound.put("BeamTarget", NBTUtil.writeBlockPos(this.getBeamTarget()));
        }
        if (this.getDamageCloud() != null) {
            compound.putUniqueId("DamageCloudUUID", this.getDamageCloud());
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (source.getTrueSource() instanceof InnerDemonEntity) {
            return false;
        } else {
            if (this.isAlive() && !this.world.isRemote) {
                this.remove();
                if (!source.isExplosion()) {
                    // TODO Create explosion
                }
            }
            return true;
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void setBeamTarget(@Nullable BlockPos beamTarget) {
        this.getDataManager().set(BEAM_TARGET, Optional.ofNullable(beamTarget));
    }

    @Nullable
    public BlockPos getBeamTarget() {
        return this.getDataManager().get(BEAM_TARGET).orElse((BlockPos)null);
    }
    
    public void setDamageCloud(@Nullable UUID cloudId) {
        this.getDataManager().set(DAMAGE_CLOUD, Optional.ofNullable(cloudId));
    }
    
    @Nullable
    public UUID getDamageCloud() {
        return this.getDataManager().get(DAMAGE_CLOUD).orElse(null);
    }
    
    /**
     * Checks if the entity is in range to render.
     */
    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        return super.isInRangeToRenderDist(distance) || this.getBeamTarget() != null;
    }
}

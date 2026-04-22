package com.verdantartifice.primalmagick.common.entities.misc;

import com.verdantartifice.primalmagick.common.entities.EntityDataSerializersPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.util.EntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.PowerParticleOption;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Definition of a sin crystal entity.  Created by an inner demon to heal it, similar to an ender crystal.
 * 
 * @author Daedalus4096
 */
public class SinCrystalEntity extends Entity {
    private static final EntityDataAccessor<Optional<BlockPos>> BEAM_TARGET = SynchedEntityData.defineId(SinCrystalEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final EntityDataAccessor<Optional<EntityReference<Entity>>> DAMAGE_CLOUD = SynchedEntityData.defineId(SinCrystalEntity.class, EntityDataSerializersPM.OPTIONAL_ENTITY_REFERENCE);
    
    public int innerRotation;

    public SinCrystalEntity(EntityType<? extends SinCrystalEntity> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.blocksBuilding = true;
        this.innerRotation = this.random.nextInt(100000);
    }
    
    public SinCrystalEntity(Level worldIn, double x, double y, double z) {
        this(EntityTypesPM.SIN_CRYSTAL.get(), worldIn);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(BEAM_TARGET, Optional.empty());
        pBuilder.define(DAMAGE_CLOUD, Optional.empty());
    }

    @Override
    public void tick() {
        super.tick();
        this.innerRotation++;
        
        // Create or extend damage cloud
        Level level = this.level();
        if (!level.isClientSide()) {
            this.getDamageCloud().ifPresentOrElse(entity -> {
                if (entity instanceof AreaEffectCloud cloud) {
                    cloud.setDuration(1 + cloud.getDuration());     // Extend the cloud's duration by a tick so that it lives as long as the crystal
                } else {
                    this.setDamageCloud(null);
                }
            }, () -> {
                AreaEffectCloud cloud = new AreaEffectCloud(level, this.getX(), this.getY(), this.getZ());
                cloud.setCustomParticle(PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F));
                cloud.setRadius(3.0F);
                cloud.setDuration(5);
                cloud.setWaitTime(0);
                cloud.addEffect(new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 1));
                level.addFreshEntity(cloud);
                this.setDamageCloud(EntityReference.of(cloud));
            });
        }
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        this.setBeamTargetInner(input.read("BeamTarget", BlockPos.CODEC));
        this.setDamageCloudInner(input.read("DamageCloud", EntityReference.codec()));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        this.getBeamTarget().ifPresent(beamTarget -> output.store("BeamTarget", BlockPos.CODEC, beamTarget));
        this.getDamageCloudReference().ifPresent(cloudRef -> output.store("DamageCloud", EntityReference.codec(), cloudRef));
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        if (this.isInvulnerableToBase(source)) {
            return false;
        } else if (source.getEntity() instanceof InnerDemonEntity) {
            return false;
        } else {
            if (this.isAlive()) {
                // Cause backlash to any inner demons being healed by this crystal
                List<InnerDemonEntity> demonsInRange = EntityUtils.getEntitiesInRange(level, this.position(), null, InnerDemonEntity.class, InnerDemonEntity.HEAL_RANGE);
                if (!demonsInRange.isEmpty()) {
                    LivingEntity trueSource = source.getEntity() instanceof LivingEntity ? (LivingEntity)source.getEntity() : null;
                    for (InnerDemonEntity demon : demonsInRange) {
                        demon.hurtServer(level, level.damageSources().explosion(trueSource, null), 10.0F);
                    }
                }
                
                // Detonate when attacked
                this.discard();
                if (!source.is(DamageTypeTags.IS_EXPLOSION)) {
                    level.explode(null, this.getX(), this.getY(), this.getZ(), 4.0F, Level.ExplosionInteraction.BLOCK);
                }
            }
            return true;
        }
    }

    protected void setBeamTargetInner(@NotNull Optional<BlockPos> beamTargetOpt) {
        this.getEntityData().set(BEAM_TARGET, beamTargetOpt);
    }

    // FIXME Shouldn't this be called from somewhere?
    public void setBeamTarget(@Nullable BlockPos beamTarget) {
        this.setBeamTargetInner(Optional.ofNullable(beamTarget));
    }

    @NotNull
    public Optional<BlockPos> getBeamTarget() {
        return this.getEntityData().get(BEAM_TARGET);
    }

    protected void setDamageCloudInner(@NotNull Optional<EntityReference<Entity>> cloudRefOpt) {
        this.getEntityData().set(DAMAGE_CLOUD, cloudRefOpt);
    }

    public void setDamageCloud(@Nullable EntityReference<Entity> cloudId) {
        this.setDamageCloudInner(Optional.ofNullable(cloudId));
    }

    @NotNull
    protected Optional<Entity> getDamageCloud() {
        return this.getDamageCloudReference().map(ref -> EntityReference.getEntity(ref, this.level()));
    }
    
    @NotNull
    protected Optional<EntityReference<Entity>> getDamageCloudReference() {
        return this.getEntityData().get(DAMAGE_CLOUD);
    }
    
    /**
     * Checks if the entity is in range to render.
     */
    public boolean shouldRenderAtSqrDistance(double distance) {
        return super.shouldRenderAtSqrDistance(distance) || this.getBeamTarget().isPresent();
    }
}

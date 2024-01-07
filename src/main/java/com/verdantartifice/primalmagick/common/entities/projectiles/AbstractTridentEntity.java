package com.verdantartifice.primalmagick.common.entities.projectiles;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Base class definition for a thrown magickal metal trident entity.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTridentEntity extends AbstractArrow {
    protected static final EntityDataAccessor<Byte> LOYALTY_LEVEL = SynchedEntityData.defineId(AbstractTridentEntity.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Boolean> HAS_GLINT = SynchedEntityData.defineId(AbstractTridentEntity.class, EntityDataSerializers.BOOLEAN);
    protected ItemStack thrownStack;
    protected boolean dealtDamage;
    public int returningTicks;
    
    public AbstractTridentEntity(EntityType<? extends AbstractTridentEntity> type, Level worldIn) {
        super(type, worldIn);
    }
    
    public AbstractTridentEntity(EntityType<? extends AbstractTridentEntity> type, Level worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(type, thrower, worldIn);
        this.thrownStack = thrownStackIn.copy();
        this.entityData.set(LOYALTY_LEVEL, (byte)EnchantmentHelper.getLoyalty(this.thrownStack));
        this.entityData.set(HAS_GLINT, this.thrownStack.hasFoil());
    }
    
    public AbstractTridentEntity(EntityType<? extends AbstractTridentEntity> type, Level worldIn, double x, double y, double z) {
        super(type, x, y, z, worldIn);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LOYALTY_LEVEL, (byte)0);
        this.entityData.define(HAS_GLINT, false);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        
        Entity shooter = this.getOwner();
        Level level = this.level();
        if ((this.dealtDamage || this.isNoPhysics()) && shooter != null) {
            int loyalty = this.entityData.get(LOYALTY_LEVEL);
            if (loyalty > 0 && !this.shouldReturnToThrower()) {
                if (!level.isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }
                this.discard();
            } else if (loyalty > 0) {
                this.setNoPhysics(true);
                Vec3 vector3d = new Vec3(shooter.getX() - this.getX(), shooter.getEyeY() - this.getY(), shooter.getZ() - this.getZ());
                this.setPosRaw(this.getX(), this.getY() + vector3d.y * 0.015D * (double)loyalty, this.getZ());
                if (level.isClientSide) {
                    this.yOld = this.getY();
                }
                
                double d0 = 0.05D * (double)loyalty;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vector3d.normalize().scale(d0)));
                if (this.returningTicks == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }
                
                this.returningTicks++;
            }
        }
        
        super.tick();
    }

    private boolean shouldReturnToThrower() {
        Entity shooter = this.getOwner();
        if (shooter != null && shooter.isAlive()) {
            return !(shooter instanceof ServerPlayer) || !shooter.isSpectator();
        } else {
            return false;
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.thrownStack.copy();
    }

    public boolean hasGlint() {
        return this.entityData.get(HAS_GLINT);
    }

    @Override
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }
    
    public abstract double getBaseDamage();

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        float damage = (float)this.getBaseDamage();
        if (entity instanceof LivingEntity) {
            damage += EnchantmentHelper.getDamageBonus(this.thrownStack, ((LivingEntity)entity).getMobType());
        }
        
        Entity shooter = this.getOwner();
        DamageSource damageSource = this.level().damageSources().trident(this, (Entity)(shooter == null ? this : shooter));
        this.dealtDamage = true;
        SoundEvent soundEvent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damageSource, damage)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                if (shooter instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity, shooter);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)shooter, livingEntity);
                }
                this.doPostHurtEffects(livingEntity);
            }
        }
        
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float volume = 1.0F;
        Level level = this.level();
        if (level instanceof ServerLevel && level.isThundering() && EnchantmentHelper.hasChanneling(this.thrownStack)) {
            BlockPos pos = entity.blockPosition();
            if (level.canSeeSky(pos)) {
                LightningBolt lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(level);
                lightningBoltEntity.moveTo(Vec3.atBottomCenterOf(pos));
                lightningBoltEntity.setCause(shooter instanceof ServerPlayer ? (ServerPlayer)shooter : null);
                level.addFreshEntity(lightningBoltEntity);
                soundEvent = SoundEvents.TRIDENT_THUNDER;
                volume = 5.0F;
            }
        }
        
        this.playSound(soundEvent, volume, 1.0F);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(Player entityIn) {
        Entity shooter = this.getOwner();
        if (shooter == null || shooter.getUUID() == entityIn.getUUID()) {
            super.playerTouch(entityIn);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Trident", Tag.TAG_COMPOUND)) {
            this.thrownStack = ItemStack.of(compound.getCompound("Trident"));
        }
        this.dealtDamage = compound.getBoolean("DealtDamage");
        this.entityData.set(LOYALTY_LEVEL, (byte)EnchantmentHelper.getLoyalty(this.thrownStack));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Trident", this.thrownStack.save(new CompoundTag()));
        compound.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    protected void tickDespawn() {
        if (this.pickup != AbstractArrow.Pickup.ALLOWED || this.entityData.get(LOYALTY_LEVEL) <= 0) {
            super.tickDespawn();
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}

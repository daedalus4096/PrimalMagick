package com.verdantartifice.primalmagick.common.entities.companions.golems;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCompanions.CompanionType;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.entities.ai.goals.CompanionOwnerHurtByTargetGoal;
import com.verdantartifice.primalmagick.common.entities.ai.goals.CompanionOwnerHurtTargetGoal;
import com.verdantartifice.primalmagick.common.entities.ai.goals.CompanionStayGoal;
import com.verdantartifice.primalmagick.common.entities.ai.goals.FollowCompanionOwnerGoal;
import com.verdantartifice.primalmagick.common.entities.companions.AbstractCompanionEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

/**
 * Base definition for an enchanted golem entity.  Like an iron golem, but follows the player as a
 * companion.  Can be made from a variety of magickal metals.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractEnchantedGolemEntity extends AbstractCompanionEntity implements NeutralMob {
    protected static final EntityDataAccessor<Integer> ANGER_TIME = SynchedEntityData.defineId(AbstractEnchantedGolemEntity.class, EntityDataSerializers.INT);
    protected static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);

    protected int attackTimer;
    protected UUID angerTarget;
    protected long lastStayChangeTime;

    public AbstractEnchantedGolemEntity(EntityType<? extends AbstractEnchantedGolemEntity> type, Level worldIn) {
        super(type, worldIn);
        this.setMaxUpStep(1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.addPersistentAngerSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        Level level = this.level();
        super.readAdditionalSaveData(compound);
        if (!level.isClientSide) {
            this.readPersistentAngerSaveData((ServerLevel)level, compound);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new CompanionStayGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.addGoal(6, new FollowCompanionOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new CompanionOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new CompanionOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANGER_TIME, 0);
    }

    @Override
    protected int decreaseAirSupply(int air) {
        // Golems don't have to breathe
        return air;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void doPush(Entity entityIn) {
        if (entityIn instanceof Enemy && !(entityIn instanceof Creeper) && this.getRandom().nextInt(20) == 0) {
            this.setTarget((LivingEntity)entityIn);
        }
        super.doPush(entityIn);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        
        Level level = this.level();
        
        if (this.attackTimer > 0) {
            this.attackTimer--;
        }
        
        if (this.getDeltaMovement().horizontalDistanceSqr() > (double)2.5000003E-7F && this.random.nextInt(5) == 0) {
            int i = Mth.floor(this.getX());
            int j = Mth.floor(this.getY() - (double)0.2F);
            int k = Mth.floor(this.getZ());
            BlockPos pos = new BlockPos(i, j, k);
            BlockState blockstate = level.getBlockState(pos);
            
            boolean isAir = blockstate.isAir();
            if (!isAir) {
                level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(pos), this.getX() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), this.getY() + 0.1D, this.getZ() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), 4.0D * ((double)this.random.nextFloat() - 0.5D), 0.5D, ((double)this.random.nextFloat() - 0.5D) * 4.0D);
            }
        }
        
        if (!level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)level, true);
        }
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return this.isCompanionOwner(target) ? false : super.canAttack(target);
    }

    @Override
    public boolean shouldAttackEntity(LivingEntity target, Player owner) {
        if (target instanceof Creeper) {
            return false;
        } else {
            return super.shouldAttackEntity(target, owner);
        }
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.entityData.set(ANGER_TIME, time);
    }

    @Override
    public UUID getPersistentAngerTarget() {
        return this.angerTarget;
    }

    @Override
    public void setPersistentAngerTarget(UUID target) {
        this.angerTarget = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
    }

    @Override
    public CompanionType getCompanionType() {
        return CompanionType.GOLEM;
    }

    public AbstractEnchantedGolemEntity.Cracks getCrackLevel() {
        return AbstractEnchantedGolemEntity.Cracks.getForHealthPercentage(this.getHealth() / this.getMaxHealth());
    }
    
    @Override
    public boolean hurt(DamageSource source, float amount) {
        AbstractEnchantedGolemEntity.Cracks cracksBefore = this.getCrackLevel();
        boolean success = super.hurt(source, amount);
        if (success && cracksBefore != this.getCrackLevel()) {
            this.playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
        }
        return success;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        this.attackTimer = 10;
        this.level().broadcastEntityEvent(this, (byte)4);
        float rawDamage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float damage = ((int)rawDamage > 0) ? (rawDamage / 2.0F) + (float)this.random.nextInt((int)rawDamage) : rawDamage;
        boolean flag = entityIn.hurt(this.level().damageSources().mobAttack(this), damage);
        if (flag) {
            entityIn.setDeltaMovement(entityIn.getDeltaMovement().add(0.0D, 0.4D, 0.0D));
            this.doEnchantDamageEffects(this, entityIn);
        }
        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.attackTimer = 10;
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        } else {
            super.handleEntityEvent(id);
        }
    }
    
    public int getAttackTimer() {
        return this.attackTimer;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }
    
    protected abstract TagKey<Item> getRepairMaterialTag();
    
    protected abstract float getRepairHealAmount();

    @Override
    protected InteractionResult mobInteract(Player playerIn, InteractionHand hand) {
        Level level = this.level();
        ItemStack itemstack = playerIn.getItemInHand(hand);
        if (!itemstack.is(this.getRepairMaterialTag())) {
            InteractionResult actionResult = super.mobInteract(playerIn, hand);
            if (!actionResult.consumesAction() && this.isCompanionOwner(playerIn) && !level.isClientSide) {
                long time = playerIn.level().getGameTime();
                if (this.lastStayChangeTime != time) {
                    this.setCompanionStaying(!this.isCompanionStaying());
                    if (this.isCompanionStaying()) {
                        playerIn.sendSystemMessage(Component.translatable("event.primalmagick.golem.stay"));
                    } else {
                        playerIn.sendSystemMessage(Component.translatable("event.primalmagick.golem.follow"));
                    }
                    this.lastStayChangeTime = time;
                }
                this.navigation.stop();
                this.setTarget(null);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        } else {
            float healthBefore = this.getHealth();
            this.heal(this.getRepairHealAmount());
            if (this.getHealth() == healthBefore) {
                return InteractionResult.PASS;
            } else {
                this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                if (!playerIn.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        BlockPos downPos = this.blockPosition().below();
        BlockState downState = worldIn.getBlockState(downPos);
        if (!downState.entityCanStandOn(worldIn, downPos, this)) {
            return false;
        } else {
            for (int i = 1; i < 3; i++) {
                BlockPos upPos = this.blockPosition().above(i);
                BlockState upState = worldIn.getBlockState(upPos);
                if (!NaturalSpawner.isValidEmptySpawnBlock(worldIn, upPos, upState, upState.getFluidState(), this.getType())) {
                    return false;
                }
            }
            return NaturalSpawner.isValidEmptySpawnBlock(worldIn, this.blockPosition(), worldIn.getBlockState(this.blockPosition()), Fluids.EMPTY.defaultFluidState(), this.getType()) && 
                    worldIn.isUnobstructed(this);
        }
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, (double)(0.875F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffectIn) {
        MobEffect effect = potioneffectIn.getEffect();
        return (effect == MobEffects.POISON || effect == EffectsPM.BLEEDING.get()) ? false : super.canBeAffected(potioneffectIn);
    }

    public static enum Cracks {
        NONE(1.0F),
        LOW(0.75F),
        MEDIUM(0.5F),
        HIGH(0.25F);
        
        private static final List<AbstractEnchantedGolemEntity.Cracks> SORTED_VALUES = Stream.of(values()).sorted(Comparator.comparingDouble((c) -> {
            return (double)c.healthPercentage;
        })).collect(ImmutableList.toImmutableList());
        
        private final float healthPercentage;
        
        private Cracks(float healthPercentage) {
            this.healthPercentage = healthPercentage;
        }
        
        public static AbstractEnchantedGolemEntity.Cracks getForHealthPercentage(float percentage) {
            for (AbstractEnchantedGolemEntity.Cracks cracks : SORTED_VALUES) {
                if (percentage < cracks.healthPercentage) {
                    return cracks;
                }
            }
            return NONE;
        }
    }
}

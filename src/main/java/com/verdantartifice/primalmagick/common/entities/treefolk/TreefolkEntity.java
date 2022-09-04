package com.verdantartifice.primalmagick.common.entities.treefolk;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import com.verdantartifice.primalmagick.common.entities.ai.goals.LongDistanceRangedAttackGoal;
import com.verdantartifice.primalmagick.common.entities.projectiles.AppleEntity;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.util.EntityUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.Brain.Provider;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * Definition of a treefolk entity, tree-like neutral bipeds that spawn in forest biomes.
 * 
 * @author Daedalus4096
 */
public class TreefolkEntity extends PathfinderMob implements /* NeutralMob, */ RangedAttackMob {
//    protected static final EntityDataAccessor<Integer> ANGER_TIME = SynchedEntityData.defineId(TreefolkEntity.class, EntityDataSerializers.INT);
//    protected static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    protected static final String DREADED_NAME = "Verdus";
    protected static final ImmutableList<SensorType<? extends Sensor<? super TreefolkEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, 
            SensorType.NEAREST_ITEMS, SensorType.HURT_BY);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_LIVING_ENTITIES, 
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, 
            MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, 
            MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.ADMIRING_ITEM, 
            MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleType.CELEBRATE_LOCATION, MemoryModuleType.DANCING, 
            MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM);

//    protected UUID angerTarget;

    public TreefolkEntity(EntityType<? extends TreefolkEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 5;
    }
    
    public static AttributeSupplier.Builder getAttributeModifiers() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.ARMOR, 4.0D).add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    public static boolean canSpawnOn(EntityType<? extends Mob> typeIn, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return Mob.checkMobSpawnRules(typeIn, worldIn, reason, pos, randomIn);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
//        this.addPersistentAngerSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
//        if (!this.level.isClientSide) {
//            this.readPersistentAngerSaveData((ServerLevel)this.level, compound);
//        }
    }

//    @Override
//    protected void registerGoals() {
//        this.goalSelector.addGoal(1, new FloatGoal(this));
//        this.goalSelector.addGoal(3, new LongDistanceRangedAttackGoal<>(this, 1.0D, 30, 4.0F, 16.0F, true));
//        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
//        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
//        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
//        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
//        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
//        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
//        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
//    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
//        this.entityData.define(ANGER_TIME, 0);
    }
    
    public boolean isAngry() {
        return this.getBrain().hasMemoryValue(MemoryModuleType.ANGRY_AT);
    }
    
    public boolean isAdult() {
        return true;
    }

//    @Override
//    public int getRemainingPersistentAngerTime() {
//        return this.entityData.get(ANGER_TIME);
//    }
//
//    @Override
//    public void setRemainingPersistentAngerTime(int time) {
//        this.entityData.set(ANGER_TIME, time);
//    }
//
//    @Override
//    public UUID getPersistentAngerTarget() {
//        return this.angerTarget;
//    }
//
//    @Override
//    public void setPersistentAngerTarget(UUID target) {
//        this.angerTarget = target;
//    }
//
//    @Override
//    public void startPersistentAngerTimer() {
//        this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
//    }

    @Override
    protected Provider<TreefolkEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> pDynamic) {
        return TreefolkAi.makeBrain(this, this.brainProvider().makeBrain(pDynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Brain<TreefolkEntity> getBrain() {
        return (Brain<TreefolkEntity>)super.getBrain();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsPM.TREEFOLK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsPM.TREEFOLK_DEATH.get();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        
        if (this.hasCustomName() && DREADED_NAME.equals(this.getCustomName().getString())) {
            this.setSecondsOnFire(8);
        }
        
//        if (!this.level.isClientSide) {
//            this.updatePersistentAnger((ServerLevel)this.level, true);
//        }
    }

    @Override
    public void setCustomName(Component name) {
        super.setCustomName(name);
        if (!this.level.isClientSide && DREADED_NAME.equals(name.getString())) {
            List<Player> nearby = EntityUtils.getEntitiesInRange(this.level, this.position(), null, Player.class, 6.0D);
            for (Player player : nearby) {
                StatsManager.incrementValue(player, StatsPM.TREANTS_NAMED);
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        AppleEntity missile = new AppleEntity(this.level, this);
        missile.setItem(new ItemStack(Items.APPLE));
        double d0 = target.getEyeY() - (double)1.1F;
        double d1 = target.getX() - this.getX();
        double d2 = d0 - missile.getY();
        double d3 = target.getZ() - this.getZ();
        float f = (float)Math.sqrt(d1 * d1 + d3 * d3) * 0.2F;
        missile.shoot(d1, d2 + (double)f, d3, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SNOWBALL_THROW, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(missile);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.FLINT_AND_STEEL)) {
            this.level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.level.isClientSide) {
                this.setSecondsOnFire(10);
                this.setLastHurtByMob(player);
                stack.hurtAndBreak(1, player, p -> {
                    p.broadcastBreakEvent(hand);
                });
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            return super.mobInteract(player, hand);
        }
    }
}

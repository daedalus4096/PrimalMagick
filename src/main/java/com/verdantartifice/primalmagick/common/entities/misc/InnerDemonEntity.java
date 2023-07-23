package com.verdantartifice.primalmagick.common.entities.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.entities.ai.goals.LongDistanceRangedAttackGoal;
import com.verdantartifice.primalmagick.common.entities.projectiles.SinCrashEntity;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.payloads.BloodDamageSpellPayload;
import com.verdantartifice.primalmagick.common.spells.vehicles.ProjectileSpellVehicle;
import com.verdantartifice.primalmagick.common.util.EntityUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Definition of an inner demon entity, a boss monster summoned via a sanguine crucible.
 * 
 * @author Daedalus4096
 */
public class InnerDemonEntity extends Monster implements RangedAttackMob, PowerableMob {
    public static final double HEAL_RANGE = 16.0D;
    protected static final double SIN_CRASH_RANGE = 12.0D;

    protected final ServerBossEvent bossInfo = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
    protected boolean isSuffocating = false;
    protected List<SinCrystalEntity> crystalsInRange = new ArrayList<>();

    public InnerDemonEntity(EntityType<? extends InnerDemonEntity> type, Level worldIn) {
        super(type, worldIn);
        this.getNavigation().setCanFloat(true);
        this.xpReward = 50;
    }
    
    public static AttributeSupplier.Builder getAttributeModifiers() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 400.0D).add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.FOLLOW_RANGE, 40.0D).add(Attributes.ARMOR, 4.0D).add(Attributes.KNOCKBACK_RESISTANCE, 0.5D).add(Attributes.ATTACK_DAMAGE, 12.0D).add(Attributes.ATTACK_KNOCKBACK, 1.5D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new InnerDemonEntity.SinCrashGoal(this, 100, 600));
        this.goalSelector.addGoal(3, new LongDistanceRangedAttackGoal<>(this, 1.0D, 30, 4.0F, 16.0F, true));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 0, false, false, null));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(Component name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }
    
    @Nonnull
    protected SpellPackage getSpellPackage() {
        SpellPackage spell = new SpellPackage("Blood Ball");
        ProjectileSpellVehicle vehicle = new ProjectileSpellVehicle();
        spell.setVehicle(vehicle);
        BloodDamageSpellPayload payload = new BloodDamageSpellPayload();
        Difficulty difficulty = this.level().getDifficulty();
        payload.getProperty("power").setValue(difficulty == Difficulty.EASY ? 1 : (difficulty == Difficulty.HARD ? 5 : 3));
        spell.setPayload(payload);
        return spell;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        this.getSpellPackage().cast(this.level(), this, null);
    }

    @Override
    public boolean isPowered() {
        return true;
    }

    @Override
    public void aiStep() {
        // Detect nearby sin crystals and heal for each
        Level level = this.level();
        this.crystalsInRange = EntityUtils.getEntitiesInRange(level, this.position(), null, SinCrystalEntity.class, HEAL_RANGE);
        if (!this.crystalsInRange.isEmpty() && this.tickCount % 10 == 0 && !level.isClientSide) {
            this.heal((float)this.crystalsInRange.size());
        }
        super.aiStep();
    }

    @Override
    protected void customServerAiStep() {
        Level level = this.level();
        if (!level.isClientSide) {
            // Explode if suffocating
            if (this.isSuffocating && this.tickCount % 20 == 0) {
                Level.ExplosionInteraction mode = ForgeEventFactory.getMobGriefingEvent(level, this) ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE;
                level.explode(this, this.getX(), this.getY(), this.getZ(), 7.0F, false, mode);
                this.isSuffocating = false;
            }
            
            this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
        }
        super.customServerAiStep();
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean retVal = super.doHurtTarget(entityIn);
        entityIn.setSecondsOnFire(5);
        return retVal;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source == this.level().damageSources().drown() || source.getEntity() instanceof InnerDemonEntity) {
            return false;
        } else {
            if (source == this.level().damageSources().inWall()) {
                this.isSuffocating = true;
            }
            return super.hurt(source, amount);
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);
        ItemEntity itemEntity = this.spawnAtLocation(ItemsPM.HALLOWED_ORB.get());
        if (itemEntity != null) {
            itemEntity.setExtendedLifetime();
        }
    }

    @Override
    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffectIn) {
        return false;
    }

    @Override
    protected boolean canRide(Entity entityIn) {
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }
    
    @Nonnull
    public List<SinCrystalEntity> getCrystalsInRange() {
        return Collections.unmodifiableList(this.crystalsInRange);
    }
    
    public void doSinCrash() {
        Level level = this.level();
        if (!level.isClientSide) {
            double demonPosX = this.getX();
            double demonPosY = this.getEyeY();
            double demonPosZ = this.getZ();
            Difficulty difficulty = level.getDifficulty();
            int crashCount = difficulty == Difficulty.EASY ? 1 : (difficulty == Difficulty.HARD ? 3 : 2);
            for (int index = 0; index < crashCount; index++) {
                double dx = level.random.nextGaussian() * SIN_CRASH_RANGE * (level.random.nextBoolean() ? 1.0D : -1.0D);
                double dy = -1.0D * (double)this.getEyeHeight();
                double dz = level.random.nextGaussian() * SIN_CRASH_RANGE * (level.random.nextBoolean() ? 1.0D : -1.0D);
                SinCrashEntity crash = new SinCrashEntity(level, this, dx, dy, dz);
                crash.moveTo(demonPosX, demonPosY, demonPosZ, 0.0F, 0.0F);
                level.addFreshEntity(crash);
            }
            level.playSound(null, demonPosX, demonPosY, demonPosZ, SoundsPM.WHISPERS.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
        }
    }
    
    public static class SinCrashGoal extends Goal {
        protected final InnerDemonEntity demon;
        protected final int startDelayTicks;
        protected final int cooldownTicks;
        protected int timer = -1;
        
        public SinCrashGoal(InnerDemonEntity demon, int startDelayTicks, int cooldownTicks) {
            this.demon = demon;
            this.startDelayTicks = startDelayTicks;
            this.cooldownTicks = cooldownTicks;
            this.timer = this.startDelayTicks;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.demon.getTarget();
            return (target != null && target.isAlive());
        }

        @Override
        public void stop() {
            this.timer = this.startDelayTicks;
        }

        @Override
        public void tick() {
            if (--this.timer == 0) {
                this.demon.doSinCrash();
                this.timer = this.cooldownTicks;
            } else if (this.timer < 0) {
                this.timer = this.cooldownTicks;
            }
        }
    }
}

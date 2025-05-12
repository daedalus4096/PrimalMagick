package com.verdantartifice.primalmagick.common.entities.pixies.guardians;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.entities.misc.PixieHouseEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.IPixie;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.payloads.AbstractSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadsPM;
import com.verdantartifice.primalmagick.common.spells.vehicles.BoltSpellVehicle;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

/**
 * Definition of a guardian pixie that is deployed by a Pixie House in self-defense.
 *
 * @author Daedalus4096
 */
public abstract class AbstractGuardianPixieEntity extends PathfinderMob implements NeutralMob, FlyingAnimal, RangedAttackMob, IPixie {
    protected static final EntityDataAccessor<Integer> ANGER_TIME = SynchedEntityData.defineId(AbstractGuardianPixieEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Optional<UUID>> HOME = SynchedEntityData.defineId(AbstractGuardianPixieEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    protected static final byte PIXIE_DUST_EVENT = 15;

    @Nullable protected Source source;
    @Nullable protected SpellPackage spellCache;
    protected int attackTimer;
    @Nullable protected UUID angerTarget;
    @Nullable protected PixieHouseEntity homeCache;

    protected AbstractGuardianPixieEntity(EntityType<? extends AbstractGuardianPixieEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 20, false);
    }

    public static <T extends AbstractGuardianPixieEntity> T spawn(EntityType<T> entityType, Source source, PixieHouseEntity home, ServerLevel level, BlockPos pos) {
        T pixie = entityType.create(level, $ -> {}, pos, MobSpawnType.SPAWN_EGG, true, true);
        if (pixie != null) {
            pixie.setPixieSource(source);
            pixie.setHome(home);
            level.addFreshEntityWithPassengers(pixie);
        }
        return pixie;
    }

    @Override
    public Source getPixieSource() {
        return this.source;
    }

    protected void setPixieSource(Source source) {
        this.source = source;
    }

    @Nullable
    public PixieHouseEntity getHome() {
        if (this.homeCache == null && this.level() instanceof ServerLevel serverLevel) {
            this.homeCache = this.entityData.get(HOME).map(u -> serverLevel.getEntity(u) instanceof PixieHouseEntity house ? house : null).orElse(null);
        }
        return this.homeCache;
    }

    public void setHome(@NotNull PixieHouseEntity home) {
        this.homeCache = home;
        this.entityData.set(HOME, Optional.of(home.getUUID()));
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Nonnull
    protected SpellPackage createSpellPackage() {
        // Not all pixie spells need the duration property, but those that don't will ignore it
        return SpellPackage.builder().name("Pixie Bolt")
                .vehicle().type(BoltSpellVehicle.INSTANCE).with(SpellPropertiesPM.RANGE.get(), 5).end()
                .payload().type(this.getSpellPayload()).with(SpellPropertiesPM.POWER.get(), this.getSpellPower()).with(SpellPropertiesPM.DURATION.get(), this.getSpellPower()).end()
                .build();
    }

    @Nonnull
    protected AbstractSpellPayload<?> getSpellPayload() {
        return SpellPayloadsPM.getSpellDamagePayload(this.getPixieSource());
    }

    @Nonnull
    protected SpellPackage getSpellPackage() {
        if (this.spellCache == null) {
            this.spellCache = this.createSpellPackage();
        }
        return this.spellCache;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new StayNearHomeGoal(this, 0.9D, 16F, 10F));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 20, 30, 16.0F));
        this.goalSelector.addGoal(3, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.addGoal(4, new EnterHomeGoal(this, 0.5F));
        this.goalSelector.addGoal(5, new ReturnHomeGoal(this, 0.9D, 0.5F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HomeAngerTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(ANGER_TIME, 0);
        pBuilder.define(HOME, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.addPersistentAngerSaveData(pCompound);
        if (this.getHome() != null) {
            pCompound.putUUID("HomeEntity", this.getHome().getUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (this.level() instanceof ServerLevel serverLevel) {
            this.readPersistentAngerSaveData(serverLevel, pCompound);
            if (pCompound.hasUUID("HomeEntity") && serverLevel.getEntity(pCompound.getUUID("HomeEntity")) instanceof PixieHouseEntity house) {
                this.setHome(house);
            }
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        // Pixies fly, not fall
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
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
    public boolean canAttack(LivingEntity pTarget) {
        // TODO Don't allow attacking the home tree's owner
        return super.canAttack(pTarget);
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
    public @Nullable UUID getPersistentAngerTarget() {
        return this.angerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.angerTarget = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.attackTimer > 0) {
            this.attackTimer--;
        }

        Level level = this.level();
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            this.updatePersistentAnger(serverLevel, true);
            if (this.isAlive()) {
                level.broadcastEntityEvent(this, PIXIE_DUST_EVENT);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
    }

    /**
     * Handler for {@link Level#broadcastEntityEvent}
     */
    @Override
    public void handleEntityEvent(byte id) {
        if (id == PIXIE_DUST_EVENT && this.getPixieSource() != null) {
            FxDispatcher.INSTANCE.pixieDust(this.getX() + (this.random.nextGaussian() * 0.25D), this.getY() + 0.25D, this.getZ() + (this.random.nextGaussian() * 0.25D), this.getPixieSource().getColor());
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        // TODO Replace with custom sounds
        return SoundEvents.BAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        // TODO Replace with custom sounds
        return SoundEvents.BAT_DEATH;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entityIn) {
        // Pixies pass through other entities
    }

    @Override
    protected void pushEntities() {
        // Pixies pass through other entities
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, worldIn);
        nav.setCanOpenDoors(false);
        nav.setCanPassDoors(true);
        return nav;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        this.getSpellPackage().cast(this.level(), this, ItemStack.EMPTY);
    }

    protected static class StayNearHomeGoal extends Goal {
        private final AbstractGuardianPixieEntity mob;
        @Nullable private LivingEntity home;
        @Nullable private Vec3 wantedPos;
        private final double speedModifier;
        private final double startDistanceSqr;
        private final double stopDistanceSqr;

        public StayNearHomeGoal(AbstractGuardianPixieEntity mob, double speedModifier, float startDistance, float stopDistance) {
            this.mob = mob;
            this.speedModifier = speedModifier;
            this.startDistanceSqr = startDistance * startDistance;
            this.stopDistanceSqr = stopDistance * stopDistance;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.home = this.mob.getHome();
            if (this.home == null) {
                return false;
            } else if (this.home.distanceToSqr(this.mob) < this.startDistanceSqr) {
                return false;
            } else {
                this.wantedPos = this.home.position().add(0D, 1.5D, 0D);
                return true;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return !this.mob.getNavigation().isDone() && this.home != null && this.home.isAlive() && this.home.distanceToSqr(this.mob) > this.stopDistanceSqr;
        }

        @Override
        public void stop() {
            this.home = null;
        }

        @Override
        public void start() {
            this.mob.getNavigation().moveTo(this.wantedPos.x(), this.wantedPos.y(), this.wantedPos.z(), this.speedModifier);
        }
    }

    protected static class EnterHomeGoal extends Goal {
        private final AbstractGuardianPixieEntity mob;
        @Nullable private PixieHouseEntity home;
        private final double withinDistanceSqr;

        public EnterHomeGoal(AbstractGuardianPixieEntity mob, float withinDistance) {
            this.mob = mob;
            this.withinDistanceSqr = withinDistance * withinDistance;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.home = this.mob.getHome();
            if (this.home == null) {
                return false;
            } else if (this.mob.getPersistentAngerTarget() != null) {
                // Don't return home if there's still a target to attack
                return false;
            } else {
                return this.home.distanceToSqr(this.mob) < this.withinDistanceSqr;
            }
        }

        @Override
        public void start() {
            if (this.home != null) {
                this.home.undeployGuardian();
            }
        }
    }

    protected static class ReturnHomeGoal extends Goal {
        private final AbstractGuardianPixieEntity mob;
        @Nullable private LivingEntity home;
        @Nullable private Vec3 wantedPos;
        private final double speedModifier;
        private final double targetDistanceSqr;

        public ReturnHomeGoal(AbstractGuardianPixieEntity mob, double speedModifier, float targetDistance) {
            this.mob = mob;
            this.speedModifier = speedModifier;
            this.targetDistanceSqr = targetDistance * targetDistance;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.home = this.mob.getHome();
            if (this.home == null) {
                return false;
            } else if (this.mob.getPersistentAngerTarget() != null) {
                // Don't return home if there's still a target to attack
                return false;
            } else if (this.home.distanceToSqr(this.mob) < this.targetDistanceSqr) {
                return false;
            } else {
                this.wantedPos = this.home.position().add(0D, 1.5D, 0D);
                return true;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return !this.mob.getNavigation().isDone() && this.home != null && this.home.isAlive() && this.home.distanceToSqr(this.mob) > this.targetDistanceSqr;
        }

        @Override
        public void stop() {
            this.home = null;
        }

        @Override
        public void start() {
            this.mob.getNavigation().moveTo(this.wantedPos.x(), this.wantedPos.y(), this.wantedPos.z(), this.speedModifier);
        }
    }

    protected static class HomeAngerTargetGoal extends TargetGoal {
        private final AbstractGuardianPixieEntity mob;
        private LivingEntity attacked;
        private int timestamp;

        public HomeAngerTargetGoal(AbstractGuardianPixieEntity mob) {
            super(mob, false);
            this.mob = mob;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            PixieHouseEntity home = this.mob.getHome();
            if (home != null && home.getTarget() != null) {
                this.attacked = home.getTarget();
                int time = home.getTargetTimestamp();
                return time != this.timestamp && this.canAttack(this.attacked, TargetingConditions.DEFAULT);
            } else {
                return false;
            }
        }

        @Override
        public void start() {
            this.mob.setPersistentAngerTarget(this.attacked.getUUID());
            this.mob.setTarget(this.attacked);
            PixieHouseEntity home = this.mob.getHome();
            if (home != null) {
                this.timestamp = home.getTargetTimestamp();
            }
            super.start();
        }
    }
}

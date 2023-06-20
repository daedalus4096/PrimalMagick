package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import java.util.EnumSet;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCompanions.CompanionType;
import com.verdantartifice.primalmagick.common.entities.ai.goals.FollowCompanionOwnerGoal;
import com.verdantartifice.primalmagick.common.entities.companions.AbstractCompanionEntity;
import com.verdantartifice.primalmagick.common.entities.companions.CompanionManager;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Base definition for a pixie entity.  Follows the player around as a companion.  Has other capabilities
 * determined by subclasses.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractPixieEntity extends AbstractCompanionEntity implements NeutralMob, FlyingAnimal, IPixie {
    protected static final EntityDataAccessor<Integer> ANGER_TIME = SynchedEntityData.defineId(AbstractPixieEntity.class, EntityDataSerializers.INT);
    protected static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);

    protected int attackTimer;
    protected UUID angerTarget;
    protected SpellPackage spellCache;

    public AbstractPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
        this.moveControl = new FlyingMoveControl(this, 20, false);
    }

    @Nonnull
    protected abstract Source getPixieSource();
    
    @Nonnull
    protected abstract PixieItem getSpawnItem();
    
    @Nonnull
    protected abstract SpellPackage createSpellPackage();
    
    @Nonnull
    protected SpellPackage getSpellPackage() {
        if (this.spellCache == null) {
            this.spellCache = this.createSpellPackage();
        }
        return this.spellCache;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.addPersistentAngerSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        Level level = this.level();
        if (!level.isClientSide) {
            this.readPersistentAngerSaveData((ServerLevel)level, compound);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new FollowCompanionOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
        this.goalSelector.addGoal(6, new AbstractPixieEntity.RandomFlyGoal(this, 16.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANGER_TIME, 0);
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
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
    public void aiStep() {
        super.aiStep();
        
        if (this.attackTimer > 0) {
            this.attackTimer--;
        }
        
        Level level = this.level();
        if (!level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)level, true);
            if (this.isAlive()) {
                level.broadcastEntityEvent(this, (byte)15);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 15) {
            FxDispatcher.INSTANCE.pixieDust(this.getX() + (this.random.nextGaussian() * 0.25D), this.getY() + 0.25D, this.getZ() + (this.random.nextGaussian() * 0.25D), this.getPixieSource().getColor());
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return this.isCompanionOwner(target) ? false : super.canAttack(target);
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
        return CompanionType.PIXIE;
    }

    public int getAttackTimer() {
        return this.attackTimer;
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
    protected InteractionResult mobInteract(Player playerIn, InteractionHand hand) {
        InteractionResult actionResult = super.mobInteract(playerIn, hand);
        if (!actionResult.consumesAction() && !this.level().isClientSide && this.isCompanionOwner(playerIn)) {
            ItemStack held = playerIn.getItemInHand(hand);
            ItemStack stack = new ItemStack(this.getSpawnItem());
            if (ItemStack.isSameItem(held, stack)) {
                held.grow(1);
            } else if (held.isEmpty()) {
                playerIn.setItemInHand(hand, stack);
            } else {
                return InteractionResult.FAIL;
            }
            CompanionManager.removeCompanion(this.getCompanionOwner(), this);
            this.playSound(this.getHurtSound(null), 1.0F, 1.0F);
            this.discard();
            return InteractionResult.SUCCESS;
        } else {
            return actionResult;
        }
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
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, worldIn);
        nav.setCanOpenDoors(false);
        nav.setCanPassDoors(true);
        return nav;
    }

    protected static class RandomFlyGoal extends Goal {
        protected final AbstractPixieEntity pixie;
        protected final PathNavigation navigator;
        protected final float wanderDistance;
        protected int timeToRecalcPath;
        
        public RandomFlyGoal(AbstractPixieEntity pixie, float wanderDistance) {
            this.pixie = pixie;
            this.navigator = this.pixie.getNavigation();
            this.wanderDistance = wanderDistance;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl movementcontroller = this.pixie.getMoveControl();
            if (!movementcontroller.hasWanted()) {
                return true;
            } else {
                double dx = movementcontroller.getWantedX() - this.pixie.getX();
                double dy = movementcontroller.getWantedY() - this.pixie.getY();
                double dz = movementcontroller.getWantedZ() - this.pixie.getZ();
                double dist = dx * dx + dy * dy + dz * dz;
                return dist < 1.0D || dist > 3600.0D;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.timeToRecalcPath > 0;
        }

        @Override
        public void start() {
            this.timeToRecalcPath = 0;
        }

        @Override
        public void tick() {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 40;
                RandomSource random = this.pixie.getRandom();
                double d0 = this.pixie.getX() + (double)((random.nextFloat() * 2.0F - 1.0F) * this.wanderDistance);
                double d1 = this.pixie.getY() + (double)((random.nextFloat() * 2.0F - 1.0F) * 2.0F);
                double d2 = this.pixie.getZ() + (double)((random.nextFloat() * 2.0F - 1.0F) * this.wanderDistance);
                this.navigator.moveTo(d0, d1, d2, 1.0D);
            }
        }
    }
}

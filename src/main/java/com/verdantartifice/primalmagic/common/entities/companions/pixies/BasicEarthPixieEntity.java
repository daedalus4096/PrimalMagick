package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import java.util.EnumSet;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerCompanions.CompanionType;
import com.verdantartifice.primalmagic.common.entities.ai.goals.CompanionOwnerHurtByTargetGoal;
import com.verdantartifice.primalmagic.common.entities.ai.goals.CompanionOwnerHurtTargetGoal;
import com.verdantartifice.primalmagic.common.entities.ai.goals.FollowCompanionOwnerGoal;
import com.verdantartifice.primalmagic.common.entities.companions.AbstractCompanionEntity;
import com.verdantartifice.primalmagic.common.entities.companions.CompanionManager;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.PixieItem;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.payloads.EarthDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.vehicles.BoltSpellVehicle;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.ai.goal.ResetAngerGoal;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Definition for a basic earth pixie entity.  Follows the player around as a companion and attacks
 * with earth spells.
 * 
 * @author Daedalus4096
 */
public class BasicEarthPixieEntity extends AbstractCompanionEntity implements IAngerable, IFlyingAnimal, IRangedAttackMob {
    protected static final DataParameter<Integer> ANGER_TIME = EntityDataManager.createKey(BasicEarthPixieEntity.class, DataSerializers.VARINT);
    protected static final RangedInteger ANGER_TIME_RANGE = TickRangeConverter.convertRange(20, 39);

    protected int attackTimer;
    protected UUID angerTarget;
    protected SpellPackage spellCache;

    public BasicEarthPixieEntity(EntityType<? extends BasicEarthPixieEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new FlyingMovementController(this, 20, false);
    }

    public static AttributeModifierMap.MutableAttribute getAttributeModifiers() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 6.0D).createMutableAttribute(Attributes.FLYING_SPEED, 0.6D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D);
    }
    
    @Nonnull
    protected Source getPixieSource() {
        return Source.EARTH;
    }
    
    @Nonnull
    protected PixieItem getSpawnItem() {
        return ItemsPM.BASIC_EARTH_PIXIE.get();
    }
    
    protected int getSpellPower() {
        return 1;
    }
    
    @Nonnull
    protected SpellPackage createSpellPackage() {
        SpellPackage spell = new SpellPackage("Pixie Bolt");
        BoltSpellVehicle vehicle = new BoltSpellVehicle();
        vehicle.getProperty("range").setValue(5);
        spell.setVehicle(vehicle);
        EarthDamageSpellPayload payload = new EarthDamageSpellPayload();
        payload.getProperty("power").setValue(this.getSpellPower());
        spell.setPayload(payload);
        return spell;
    }
    
    @Nonnull
    protected SpellPackage getSpellPackage() {
        if (this.spellCache == null) {
            this.spellCache = this.createSpellPackage();
        }
        return this.spellCache;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        this.writeAngerNBT(compound);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (!this.world.isRemote) {
            this.readAngerNBT((ServerWorld)this.world, compound);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 20, 30, 16.0F));
        this.goalSelector.addGoal(3, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.addGoal(5, new FollowCompanionOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
        this.goalSelector.addGoal(6, new BasicEarthPixieEntity.RandomFlyGoal(this, 16.0F));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new CompanionOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new CompanionOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::func_233680_b_));
        this.targetSelector.addGoal(5, new ResetAngerGoal<>(this, false));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ANGER_TIME, 0);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        // Pixies fly, not fall
        return false;
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        // Pixies fly, not fall
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Override
    public int getTalkInterval() {
        return 120;
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void livingTick() {
        super.livingTick();
        
        if (this.attackTimer > 0) {
            this.attackTimer--;
        }
        
        if (!this.world.isRemote) {
            this.func_241359_a_((ServerWorld)this.world, true);
            if (this.isAlive()) {
                this.world.setEntityState(this, (byte)15);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.setMotion(this.getMotion().mul(1.0D, 0.6D, 1.0D));
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 15) {
            FxDispatcher.INSTANCE.pixieDust(this.getPosX() + (this.rand.nextGaussian() * 0.25D), this.getPosY() + 0.25D, this.getPosZ() + (this.rand.nextGaussian() * 0.25D), this.getPixieSource().getColor());
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return this.isCompanionOwner(target) ? false : super.canAttack(target);
    }

    @Override
    public int getAngerTime() {
        return this.dataManager.get(ANGER_TIME);
    }

    @Override
    public void setAngerTime(int time) {
        this.dataManager.set(ANGER_TIME, time);
    }

    @Override
    public UUID getAngerTarget() {
        return this.angerTarget;
    }

    @Override
    public void setAngerTarget(UUID target) {
        this.angerTarget = target;
    }

    @Override
    public void func_230258_H__() {
        this.setAngerTime(ANGER_TIME_RANGE.getRandomWithinRange(this.rand));
    }

    @Override
    public CompanionType getCompanionType() {
        return CompanionType.PIXIE;
    }

    @OnlyIn(Dist.CLIENT)
    public int getAttackTimer() {
        return this.attackTimer;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        // TODO Replace with custom sounds
        return SoundEvents.ENTITY_BAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        // TODO Replace with custom sounds
        return SoundEvents.ENTITY_BAT_DEATH;
    }

    @Override
    protected ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
        ActionResultType actionResult = super.getEntityInteractionResult(playerIn, hand);
        if (!actionResult.isSuccessOrConsume() && !this.world.isRemote && this.isCompanionOwner(playerIn)) {
            ItemStack held = playerIn.getHeldItem(hand);
            ItemStack stack = new ItemStack(this.getSpawnItem());
            if (held.isItemEqual(stack)) {
                held.grow(1);
            } else if (held.isEmpty()) {
                playerIn.setHeldItem(hand, stack);
            } else {
                return ActionResultType.FAIL;
            }
            CompanionManager.removeCompanion(this.getCompanionOwner(), this);
            this.playSound(this.getHurtSound(null), 1.0F, 1.0F);
            this.remove();
            return ActionResultType.SUCCESS;
        } else {
            return actionResult;
        }
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        // Pixies pass through other entities
    }

    @Override
    protected void collideWithNearbyEntities() {
        // Pixies pass through other entities
    }

    @Override
    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        FlyingPathNavigator nav = new FlyingPathNavigator(this, worldIn);
        nav.setCanOpenDoors(false);
        nav.setCanEnterDoors(true);
        return nav;
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        this.getSpellPackage().cast(this.world, this, null);
    }
    
    protected static class RandomFlyGoal extends Goal {
        protected final BasicEarthPixieEntity pixie;
        protected final PathNavigator navigator;
        protected final float wanderDistance;
        protected int timeToRecalcPath;
        
        public RandomFlyGoal(BasicEarthPixieEntity pixie, float wanderDistance) {
            this.pixie = pixie;
            this.navigator = this.pixie.getNavigator();
            this.wanderDistance = wanderDistance;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            MovementController movementcontroller = this.pixie.getMoveHelper();
            if (!movementcontroller.isUpdating()) {
                return true;
            } else {
                double dx = movementcontroller.getX() - this.pixie.getPosX();
                double dy = movementcontroller.getY() - this.pixie.getPosY();
                double dz = movementcontroller.getZ() - this.pixie.getPosZ();
                double dist = dx * dx + dy * dy + dz * dz;
                return dist < 1.0D || dist > 3600.0D;
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            return this.timeToRecalcPath > 0;
        }

        @Override
        public void startExecuting() {
            this.timeToRecalcPath = 0;
        }

        @Override
        public void tick() {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 40;
                Random random = this.pixie.getRNG();
                double d0 = this.pixie.getPosX() + (double)((random.nextFloat() * 2.0F - 1.0F) * this.wanderDistance);
                double d1 = this.pixie.getPosY() + (double)((random.nextFloat() * 2.0F - 1.0F) * 2.0F);
                double d2 = this.pixie.getPosZ() + (double)((random.nextFloat() * 2.0F - 1.0F) * this.wanderDistance);
                this.navigator.tryMoveToXYZ(d0, d1, d2, 1.0D);
            }
        }
    }
}

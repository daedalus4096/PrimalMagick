package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import java.util.UUID;

import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerCompanions.CompanionType;
import com.verdantartifice.primalmagic.common.entities.companions.AbstractCompanionEntity;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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
public class BasicEarthPixieEntity extends AbstractCompanionEntity implements IAngerable {
    protected static final DataParameter<Integer> ANGER_TIME = EntityDataManager.createKey(BasicEarthPixieEntity.class, DataSerializers.VARINT);
    protected static final RangedInteger ANGER_TIME_RANGE = TickRangeConverter.convertRange(20, 39);

    protected int attackTimer;
    protected UUID angerTarget;

    public BasicEarthPixieEntity(EntityType<? extends BasicEarthPixieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute getAttributeModifiers() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 6.0D);
    }
    
    protected Source getPixieSource() {
        return Source.EARTH;
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
        // TODO Auto-generated method stub
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

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        // TODO Auto-generated method stub
        return super.attackEntityAsMob(entityIn);
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
        // TODO Auto-generated method stub
        return super.getEntityInteractionResult(playerIn, hand);
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
}

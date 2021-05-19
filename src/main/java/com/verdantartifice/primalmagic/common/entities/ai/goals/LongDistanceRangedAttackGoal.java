package com.verdantartifice.primalmagic.common.entities.ai.goals;

import java.util.EnumSet;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;

/**
 * AI goal for a creature to attack with ranged attacks if it's out of range of melee attacks.
 * 
 * @author Daedalus4096
 */
public class LongDistanceRangedAttackGoal<T extends CreatureEntity & IRangedAttackMob> extends Goal {
    protected final T entity;
    protected final double moveSpeed;
    protected final int minAttackTime;
    protected final int maxAttackTime;
    protected final float minDistanceSq;
    protected final float maxDistanceSq;
    protected final float maxDistance;
    protected final boolean advanceToMelee;
    protected LivingEntity attackTarget;
    protected int rangedAttackTime = -1;
    protected int seeTime;
    
    public LongDistanceRangedAttackGoal(T attacker, double moveSpeed, int maxAttackTime, float minDistance, float maxDistance, boolean advanceToMelee) {
        this(attacker, moveSpeed, maxAttackTime, maxAttackTime, minDistance, maxDistance, advanceToMelee);
    }
    
    public LongDistanceRangedAttackGoal(T attacker, double moveSpeed, int minAttackTime, int maxAttackTime, float minDistance, float maxDistance, boolean advanceToMelee) {
        this.entity = attacker;
        this.moveSpeed = moveSpeed;
        this.minAttackTime = minAttackTime;
        this.maxAttackTime = maxAttackTime;
        this.minDistanceSq = minDistance * minDistance;
        this.maxDistanceSq = maxDistance * maxDistance;
        this.maxDistance = maxDistance;
        this.advanceToMelee = advanceToMelee;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity target = this.entity.getAttackTarget();
        if (target != null && target.isAlive() && this.entity.getDistanceSq(target) > this.minDistanceSq) {
            this.attackTarget = target;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute() || !this.entity.getNavigator().noPath();
    }

    @Override
    public void resetTask() {
        this.attackTarget = null;
        this.seeTime = 0;
        this.rangedAttackTime = -1;
    }

    @Override
    public void tick() {
        double distSq = this.entity.getDistanceSq(this.attackTarget);
        boolean canSee = this.entity.getEntitySenses().canSee(this.attackTarget);
        if (canSee) {
            this.seeTime++;
        } else {
            this.seeTime = 0;
        }
        
        if (this.seeTime < 5 || distSq > this.maxDistanceSq || (distSq > this.minDistanceSq && this.advanceToMelee)) {
            this.entity.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.moveSpeed);
        } else {
            this.entity.getNavigator().clearPath();
        }
        
        this.entity.getLookController().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
        if (--this.rangedAttackTime == 0) {
            if (!canSee) {
                return;
            }
            float f = MathHelper.sqrt(distSq) / this.maxDistance;
            this.entity.attackEntityWithRangedAttack(this.attackTarget, MathHelper.clamp(f, 0.1F, 1.0F));
            this.rangedAttackTime = MathHelper.floor(f * (float)(this.maxAttackTime - this.minAttackTime) + (float)this.minAttackTime);
        } else if (this.rangedAttackTime < 0) {
            float f = MathHelper.sqrt(distSq) / this.maxDistance;
            this.rangedAttackTime = MathHelper.floor(f * (float)(this.maxAttackTime - this.minAttackTime) + (float)this.minAttackTime);
        }
    }
}

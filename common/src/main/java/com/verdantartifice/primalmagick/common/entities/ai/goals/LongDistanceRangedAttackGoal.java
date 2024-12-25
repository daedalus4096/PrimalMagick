package com.verdantartifice.primalmagick.common.entities.ai.goals;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;

import java.util.EnumSet;

/**
 * AI goal for a creature to attack with ranged attacks if it's out of range of melee attacks.
 * 
 * @author Daedalus4096
 */
public class LongDistanceRangedAttackGoal<T extends PathfinderMob & RangedAttackMob> extends Goal {
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
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        if (target != null && target.isAlive() && this.entity.distanceToSqr(target) > this.minDistanceSq) {
            this.attackTarget = target;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() || !this.entity.getNavigation().isDone();
    }

    @Override
    public void stop() {
        this.attackTarget = null;
        this.seeTime = 0;
        this.rangedAttackTime = -1;
    }

    @Override
    public void tick() {
        float distSq = (float)this.entity.distanceToSqr(this.attackTarget);
        boolean canSee = this.entity.getSensing().hasLineOfSight(this.attackTarget);
        if (canSee) {
            this.seeTime++;
        } else {
            this.seeTime = 0;
        }
        
        if (this.seeTime < 5 || distSq > this.maxDistanceSq || (distSq > this.minDistanceSq && this.advanceToMelee)) {
            this.entity.getNavigation().moveTo(this.attackTarget, this.moveSpeed);
        } else {
            this.entity.getNavigation().stop();
        }
        
        this.entity.getLookControl().setLookAt(this.attackTarget, 30.0F, 30.0F);
        if (--this.rangedAttackTime == 0) {
            if (!canSee) {
                return;
            }
            float f = Mth.sqrt(distSq) / this.maxDistance;
            this.entity.performRangedAttack(this.attackTarget, Mth.clamp(f, 0.1F, 1.0F));
            this.rangedAttackTime = Mth.floor(f * (float)(this.maxAttackTime - this.minAttackTime) + (float)this.minAttackTime);
        } else if (this.rangedAttackTime < 0) {
            float f = Mth.sqrt(distSq) / this.maxDistance;
            this.rangedAttackTime = Mth.floor(f * (float)(this.maxAttackTime - this.minAttackTime) + (float)this.minAttackTime);
        }
    }
}

package com.verdantartifice.primalmagic.common.entities.ai.goals;

import java.util.EnumSet;

import com.verdantartifice.primalmagic.common.entities.companions.AbstractCompanionEntity;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;

/**
 * AI goal for a companion to target any entities that attack their owner.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal}
 */
public class CompanionOwnerHurtByTargetGoal extends TargetGoal {
    protected final AbstractCompanionEntity entity;
    protected LivingEntity attacker;
    protected int timestamp;

    public CompanionOwnerHurtByTargetGoal(AbstractCompanionEntity defendingCompanion) {
        super(defendingCompanion, false);
        this.entity = defendingCompanion;
        this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean shouldExecute() {
        if (this.entity.hasCompanionOwner() && !this.entity.isCompanionStaying()) {
            PlayerEntity owner = this.entity.getCompanionOwner();
            if (owner == null) {
                return false;
            } else {
                this.attacker = owner.getRevengeTarget();
                int time = owner.getRevengeTimer();
                return time != this.timestamp && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT) && this.entity.shouldAttackEntity(this.attacker, owner);
            }
        } else {
            return false;
        }
    }

    @Override
    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.attacker);
        PlayerEntity owner = this.entity.getCompanionOwner();
        if (owner != null) {
            this.timestamp = owner.getRevengeTimer();
        }
        super.startExecuting();
    }
}

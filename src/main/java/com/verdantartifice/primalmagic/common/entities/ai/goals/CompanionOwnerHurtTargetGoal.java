package com.verdantartifice.primalmagic.common.entities.ai.goals;

import java.util.EnumSet;

import com.verdantartifice.primalmagic.common.entities.companions.AbstractCompanionEntity;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;

/**
 * AI goal for a companion to target its owner's last target.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.entity.ai.goal.OwnerHurtTargetGoal}
 */
public class CompanionOwnerHurtTargetGoal extends TargetGoal {
    protected final AbstractCompanionEntity entity;
    protected LivingEntity attacked;
    protected int timestamp;
    
    public CompanionOwnerHurtTargetGoal(AbstractCompanionEntity entity) {
        super(entity, false);
        this.entity = entity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean shouldExecute() {
        if (this.entity.hasCompanionOwner() && !this.entity.isCompanionStaying()) {
            PlayerEntity owner = this.entity.getCompanionOwner();
            if (owner == null) {
                return false;
            } else {
                this.attacked = owner.getLastAttackedEntity();
                int time = owner.getLastAttackedEntityTime();
                return time != this.timestamp && this.isSuitableTarget(this.attacked, EntityPredicate.DEFAULT) && this.entity.shouldAttackEntity(this.attacked, owner);
            }
        } else {
            return false;
        }
    }

    @Override
    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.attacked);
        PlayerEntity owner = this.entity.getCompanionOwner();
        if (owner != null) {
            this.timestamp = owner.getLastAttackedEntityTime();
        }
        super.startExecuting();
    }
}

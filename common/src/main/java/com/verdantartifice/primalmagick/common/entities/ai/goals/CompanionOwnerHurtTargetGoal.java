package com.verdantartifice.primalmagick.common.entities.ai.goals;

import com.verdantartifice.primalmagick.common.entities.companions.AbstractCompanionEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

/**
 * AI goal for a companion to target its owner's last target.
 * 
 * @author Daedalus4096
 * @see net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal
 */
public class CompanionOwnerHurtTargetGoal extends TargetGoal {
    protected final AbstractCompanionEntity entity;
    protected LivingEntity attacked;
    protected int timestamp;
    
    public CompanionOwnerHurtTargetGoal(AbstractCompanionEntity entity) {
        super(entity, false);
        this.entity = entity;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.entity.hasCompanionOwner() && !this.entity.isCompanionStaying()) {
            Player owner = this.entity.getCompanionOwner();
            if (owner == null) {
                return false;
            } else {
                this.attacked = owner.getLastHurtMob();
                int time = owner.getLastHurtMobTimestamp();
                return time != this.timestamp && this.canAttack(this.attacked, TargetingConditions.DEFAULT) && this.entity.shouldAttackEntity(this.attacked, owner);
            }
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacked);
        Player owner = this.entity.getCompanionOwner();
        if (owner != null) {
            this.timestamp = owner.getLastHurtMobTimestamp();
        }
        super.start();
    }
}

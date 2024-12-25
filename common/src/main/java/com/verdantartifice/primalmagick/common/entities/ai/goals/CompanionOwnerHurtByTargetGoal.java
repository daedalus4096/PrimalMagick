package com.verdantartifice.primalmagick.common.entities.ai.goals;

import com.verdantartifice.primalmagick.common.entities.companions.AbstractCompanionEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

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
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.entity.hasCompanionOwner() && !this.entity.isCompanionStaying()) {
            Player owner = this.entity.getCompanionOwner();
            if (owner == null) {
                return false;
            } else {
                this.attacker = owner.getLastHurtByMob();
                int time = owner.getLastHurtByMobTimestamp();
                return time != this.timestamp && this.canAttack(this.attacker, TargetingConditions.DEFAULT) && this.entity.shouldAttackEntity(this.attacker, owner);
            }
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacker);
        Player owner = this.entity.getCompanionOwner();
        if (owner != null) {
            this.timestamp = owner.getLastHurtByMobTimestamp();
        }
        super.start();
    }
}

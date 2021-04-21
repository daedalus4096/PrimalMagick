package com.verdantartifice.primalmagic.common.entities.ai.goals;

import java.util.EnumSet;

import com.verdantartifice.primalmagic.common.entities.companions.AbstractCompanionEntity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

/**
 * AI goal for a companion to stay put at its owner's command.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.entity.ai.goal.SitGoal}
 */
public class CompanionStayGoal extends Goal {
    protected final AbstractCompanionEntity entity;
    
    public CompanionStayGoal(AbstractCompanionEntity entity) {
        this.entity = entity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return this.entity.isCompanionStaying();
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.hasCompanionOwner()) {
            return false;
        } else if (this.entity.isInWaterOrBubbleColumn()) {
            return false;
        } else if (!this.entity.isOnGround()) {
            return false;
        } else {
            PlayerEntity owner = this.entity.getCompanionOwner();
            if (owner == null) {
                return true;
            } else {
                return this.entity.getDistanceSq(owner) < 144.0D && owner.getRevengeTarget() != null ? false : this.entity.isCompanionStaying();
            }
        }
    }

    @Override
    public void startExecuting() {
        this.entity.getNavigator().clearPath();
        this.entity.setCompanionStaying(true);
    }

    @Override
    public void resetTask() {
        this.entity.setCompanionStaying(false);
    }
}

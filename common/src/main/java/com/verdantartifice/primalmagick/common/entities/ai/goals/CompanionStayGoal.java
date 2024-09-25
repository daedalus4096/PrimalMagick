package com.verdantartifice.primalmagick.common.entities.ai.goals;

import java.util.EnumSet;

import com.verdantartifice.primalmagick.common.entities.companions.AbstractCompanionEntity;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

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
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.entity.isCompanionStaying();
    }

    @Override
    public boolean canUse() {
        if (!this.entity.hasCompanionOwner()) {
            return false;
        } else if (this.entity.isInWaterOrBubble()) {
            return false;
        } else if (!this.entity.onGround()) {
            return false;
        } else {
            Player owner = this.entity.getCompanionOwner();
            if (owner == null) {
                return true;
            } else {
                return this.entity.distanceToSqr(owner) < 144.0D && owner.getLastHurtByMob() != null ? false : this.entity.isCompanionStaying();
            }
        }
    }

    @Override
    public void start() {
        this.entity.getNavigation().stop();
        this.entity.setCompanionStaying(true);
    }

    @Override
    public void stop() {
        this.entity.setCompanionStaying(false);
    }
}

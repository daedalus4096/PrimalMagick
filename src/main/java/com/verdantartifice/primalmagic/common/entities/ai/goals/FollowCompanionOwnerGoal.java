package com.verdantartifice.primalmagic.common.entities.ai.goals;

import java.util.EnumSet;

import com.verdantartifice.primalmagic.common.entities.companions.AbstractCompanionEntity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.world.IWorldReader;

/**
 * AI goal for a companion to follow its owner.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.entity.ai.goal.FollowOwnerGoal}
 */
public class FollowCompanionOwnerGoal extends Goal {
    protected final AbstractCompanionEntity entity;
    protected PlayerEntity owner;
    protected final IWorldReader world;
    protected final double followSpeed;
    protected final PathNavigator navigator;
    protected int timeToRecalcPath;
    protected final float maxDist;
    protected final float minDist;
    protected float oldWaterCost;
    protected final boolean teleportToLeaves;
    
    public FollowCompanionOwnerGoal(AbstractCompanionEntity entity, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
        this.entity = entity;
        this.world = entity.world;
        this.followSpeed = speed;
        this.navigator = entity.getNavigator();
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.teleportToLeaves = teleportToLeaves;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(this.navigator instanceof GroundPathNavigator) && !(this.navigator instanceof FlyingPathNavigator)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowCompanionOwnerGoal");
        }
    }

    @Override
    public boolean shouldExecute() {
        PlayerEntity owner = this.entity.getCompanionOwner();
        if (owner == null) {
            return false;
        } else if (owner.isSpectator()) {
            return false;
        } else if (this.entity.isCompanionStaying()) {
            return false;
        } else if (this.entity.getDistanceSq(owner) < (double)(this.minDist * this.minDist)) {
            return false;
        } else {
            this.owner = owner;
            return true;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.navigator.noPath()) {
            return false;
        } else if (this.entity.isCompanionStaying()) {
            return false;
        } else {
            return !(this.entity.getDistanceSq(this.owner) <= (double)(this.maxDist * this.maxDist));
        }
    }

    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.entity.getPathPriority(PathNodeType.WATER);
        this.entity.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void resetTask() {
        this.owner = null;
        this.navigator.clearPath();
        this.entity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        // TODO Auto-generated method stub
        super.tick();
    }

}

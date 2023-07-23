package com.verdantartifice.primalmagick.common.entities.ai.goals;

import java.util.EnumSet;

import com.verdantartifice.primalmagick.common.entities.companions.AbstractCompanionEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

/**
 * AI goal for a companion to follow its owner.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.entity.ai.goal.FollowOwnerGoal}
 */
public class FollowCompanionOwnerGoal extends Goal {
    protected final AbstractCompanionEntity entity;
    protected Player owner;
    protected final LevelReader world;
    protected final double followSpeed;
    protected final PathNavigation navigator;
    protected int timeToRecalcPath;
    protected final float maxDist;
    protected final float minDist;
    protected float oldWaterCost;
    protected final boolean teleportToLeaves;
    
    public FollowCompanionOwnerGoal(AbstractCompanionEntity entity, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
        this.entity = entity;
        this.world = entity.level();
        this.followSpeed = speed;
        this.navigator = entity.getNavigation();
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.teleportToLeaves = teleportToLeaves;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(this.navigator instanceof GroundPathNavigation) && !(this.navigator instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowCompanionOwnerGoal");
        }
    }

    @Override
    public boolean canUse() {
        Player owner = this.entity.getCompanionOwner();
        if (owner == null) {
            return false;
        } else if (owner.isSpectator()) {
            return false;
        } else if (this.entity.isCompanionStaying()) {
            return false;
        } else if (this.entity.distanceToSqr(owner) < (double)(this.minDist * this.minDist)) {
            return false;
        } else {
            this.owner = owner;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.navigator.isDone()) {
            return false;
        } else if (this.entity.isCompanionStaying()) {
            return false;
        } else {
            return !(this.entity.distanceToSqr(this.owner) <= (double)(this.maxDist * this.maxDist));
        }
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.entity.getPathfindingMalus(BlockPathTypes.WATER);
        this.entity.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigator.stop();
        this.entity.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.entity.getLookControl().setLookAt(this.owner, 10.0F, (float)this.entity.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.entity.isLeashed() && !this.entity.isPassenger()) {
                if (this.entity.distanceToSqr(this.owner) >= 324.0D) {
                    this.tryToTeleportNearEntity();
                } else {
                    this.navigator.moveTo(this.owner, this.followSpeed);
                }
            }
        }
    }

    protected void tryToTeleportNearEntity() {
        BlockPos pos = this.owner.blockPosition();
        for (int attempts = 0; attempts < 10; attempts++) {
            int dx = this.getRandomNumber(-2, 2);
            int dy = this.getRandomNumber(-1, 1);
            int dz = this.getRandomNumber(-2, 2);
            if (this.tryToTeleportToLocation(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz)) {
                return;
            }
        }
    }
    
    protected boolean tryToTeleportToLocation(int x, int y, int z) {
        if (Math.abs((double)x - this.owner.getX()) < 2.0D && Math.abs((double)z - this.owner.getZ()) < 2.0D) {
            return false;
        } else if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
            return false;
        } else {
            this.entity.moveTo((double)x + 0.5D, (double)y, (double)z + 0.5D, this.entity.getYRot(), this.entity.getXRot());
            this.navigator.stop();
            return true;
        }
    }
    
    protected boolean isTeleportFriendlyBlock(BlockPos pos) {
        BlockPathTypes type = WalkNodeEvaluator.getBlockPathTypeStatic(this.world, pos.mutable());
        if (type != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = this.world.getBlockState(pos.below());
            if (!this.teleportToLeaves && blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pos.subtract(this.entity.blockPosition());
                return this.world.noCollision(this.entity, this.entity.getBoundingBox().move(blockpos));
            }
        }
    }
    
    protected int getRandomNumber(int min, int max) {
        return this.entity.getRandom().nextInt(max - min + 1) + min;
    }
}

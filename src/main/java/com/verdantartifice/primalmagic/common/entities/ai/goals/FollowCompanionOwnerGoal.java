package com.verdantartifice.primalmagic.common.entities.ai.goals;

import java.util.EnumSet;

import com.verdantartifice.primalmagic.common.entities.companions.AbstractCompanionEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
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
        this.entity.getLookController().setLookPositionWithEntity(this.owner, 10.0F, (float)this.entity.getVerticalFaceSpeed());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.entity.getLeashed() && !this.entity.isPassenger()) {
                if (this.entity.getDistanceSq(this.owner) >= 144.0D) {
                    this.tryToTeleportNearEntity();
                } else {
                    this.navigator.tryMoveToEntityLiving(this.owner, this.followSpeed);
                }
            }
        }
    }

    protected void tryToTeleportNearEntity() {
        BlockPos pos = this.owner.getPosition();
        for (int attempts = 0; attempts < 10; attempts++) {
            int dx = this.getRandomNumber(-3, 3);
            int dy = this.getRandomNumber(-1, 1);
            int dz = this.getRandomNumber(-3, 3);
            if (this.tryToTeleportToLocation(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz)) {
                return;
            }
        }
    }
    
    protected boolean tryToTeleportToLocation(int x, int y, int z) {
        if (Math.abs((double)x - this.owner.getPosX()) < 2.0D && Math.abs((double)z - this.owner.getPosZ()) < 2.0D) {
            return false;
        } else if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
            return false;
        } else {
            this.entity.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, this.entity.rotationYaw, this.entity.rotationPitch);
            this.navigator.clearPath();
            return true;
        }
    }
    
    protected boolean isTeleportFriendlyBlock(BlockPos pos) {
        PathNodeType type = WalkNodeProcessor.getFloorNodeType(this.world, pos.toMutable());
        if (type != PathNodeType.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = this.world.getBlockState(pos.down());
            if (!this.teleportToLeaves && blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pos.subtract(this.entity.getPosition());
                return this.world.hasNoCollisions(this.entity, this.entity.getBoundingBox().offset(blockpos));
            }
        }
    }
    
    protected int getRandomNumber(int min, int max) {
        return this.entity.getRNG().nextInt(max - min + 1) + min;
    }
}

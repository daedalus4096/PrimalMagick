package com.verdantartifice.primalmagick.common.entities.ai.goals;

import java.util.EnumSet;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class ZoomAtTargetGoal extends Goal {
    protected final Mob leaper;
    protected final float leapMotionY;
    protected final float minRangeSq;
    protected final float maxRangeSq;
    protected LivingEntity leapTarget;
    
    public ZoomAtTargetGoal(Mob leaper, float minRange, float maxRange, float leapMotionY) {
        this.leaper = leaper;
        this.minRangeSq = minRange * minRange;
        this.maxRangeSq = maxRange * maxRange;
        this.leapMotionY = leapMotionY;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.leaper.isVehicle()) {
            return false;
        } else {
            this.leapTarget = this.leaper.getTarget();
            if (this.leapTarget == null) {
                return false;
            } else {
                double distanceSq = this.leaper.distanceToSqr(this.leapTarget);
                return (distanceSq >= this.minRangeSq) && (distanceSq <= this.maxRangeSq);
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.leaper.onGround();
    }

    @Override
    public void start() {
        Vec3 vector3d = this.leaper.getDeltaMovement();
        Vec3 vector3d1 = new Vec3(this.leapTarget.getX() - this.leaper.getX(), 0.0D, this.leapTarget.getZ() - this.leaper.getZ());
        if (vector3d1.lengthSqr() > 1.0E-7D) {
            vector3d1 = vector3d1.normalize().scale(0.8D).add(vector3d.scale(0.2D));
        }
        this.leaper.setDeltaMovement(vector3d1.x, (double)this.leapMotionY, vector3d1.z);
    }
}

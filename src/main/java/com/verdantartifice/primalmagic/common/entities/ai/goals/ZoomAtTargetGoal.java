package com.verdantartifice.primalmagic.common.entities.ai.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

public class ZoomAtTargetGoal extends Goal {
    protected final MobEntity leaper;
    protected final float leapMotionY;
    protected final float minRangeSq;
    protected final float maxRangeSq;
    protected LivingEntity leapTarget;
    
    public ZoomAtTargetGoal(MobEntity leaper, float minRange, float maxRange, float leapMotionY) {
        this.leaper = leaper;
        this.minRangeSq = minRange * minRange;
        this.maxRangeSq = maxRange * maxRange;
        this.leapMotionY = leapMotionY;
    }

    @Override
    public boolean shouldExecute() {
        if (this.leaper.isBeingRidden()) {
            return false;
        } else {
            this.leapTarget = this.leaper.getAttackTarget();
            if (this.leapTarget == null) {
                return false;
            } else {
                double distanceSq = this.leaper.getDistanceSq(this.leapTarget);
                return (distanceSq >= this.minRangeSq) && (distanceSq <= this.maxRangeSq);
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.leaper.isOnGround();
    }

    @Override
    public void startExecuting() {
        Vector3d vector3d = this.leaper.getMotion();
        Vector3d vector3d1 = new Vector3d(this.leapTarget.getPosX() - this.leaper.getPosX(), 0.0D, this.leapTarget.getPosZ() - this.leaper.getPosZ());
        if (vector3d1.lengthSquared() > 1.0E-7D) {
            vector3d1 = vector3d1.normalize().scale(0.8D).add(vector3d.scale(0.2D));
        }
        this.leaper.setMotion(vector3d1.x, (double)this.leapMotionY, vector3d1.z);
    }
}

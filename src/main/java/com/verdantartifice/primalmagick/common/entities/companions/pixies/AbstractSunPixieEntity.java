package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import java.util.EnumSet;

import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.payloads.HealingSpellPayload;
import com.verdantartifice.primalmagick.common.spells.vehicles.BoltSpellVehicle;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

/**
 * Base definition for a sun pixie.  In addition to following the player as a companion, heals them
 * when below 75% health.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractSunPixieEntity extends AbstractPixieEntity {
    public AbstractSunPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected Source getPixieSource() {
        return Source.SUN;
    }

    @Override
    protected SpellPackage createSpellPackage() {
        SpellPackage spell = new SpellPackage("Pixie Bolt");
        BoltSpellVehicle vehicle = new BoltSpellVehicle();
        vehicle.getProperty("range").setValue(5);
        spell.setVehicle(vehicle);
        HealingSpellPayload payload = new HealingSpellPayload();
        payload.getProperty("power").setValue(this.getSpellPower());
        spell.setPayload(payload);
        return spell;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new AbstractSunPixieEntity.HealOwnerGoal(this, 1.0D, 20, 30, 16.0F));
    }
    
    public void castSpell() {
        this.getSpellPackage().cast(this.level(), this, null);
    }
    
    /**
     * Modified ranged attack goal that casts a healing spell package on the pixie's owner.
     * 
     * @author Daedalus4096
     */
    protected static class HealOwnerGoal extends Goal {
        protected final AbstractSunPixieEntity pixie;
        protected final double moveSpeed;
        protected final int castIntervalMin;
        protected final int castIntervalMax;
        protected final float castRadius;
        protected final float maxCastDistanceSq;
        protected LivingEntity castTarget;
        protected int seeTime;
        protected int castTime = -1;
        
        public HealOwnerGoal(AbstractSunPixieEntity pixie, double moveSpeed, int minCastTime, int maxCastTime, float maxCastDistance) {
            this.pixie = pixie;
            this.moveSpeed = moveSpeed;
            this.castIntervalMin = minCastTime;
            this.castIntervalMax = maxCastTime;
            this.castRadius = maxCastDistance;
            this.maxCastDistanceSq = maxCastDistance * maxCastDistance;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.pixie.getCompanionOwner();
            if (target != null && target.isAlive() && (target.getHealth() / target.getMaxHealth()) < 0.75F) {
                this.castTarget = target;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse() || !this.pixie.getNavigation().isDone();
        }

        @Override
        public void stop() {
            this.castTarget = null;
            this.seeTime = 0;
            this.castTime = -1;
        }

        @Override
        public void tick() {
            float distSq = (float)this.pixie.distanceToSqr(this.castTarget);
            boolean canSee = this.pixie.getSensing().hasLineOfSight(this.castTarget);
            if (canSee) {
                this.seeTime++;
            } else {
                this.seeTime = 0;
            }
            
            if (distSq <= this.maxCastDistanceSq && this.seeTime >= 5) {
                this.pixie.getNavigation().stop();
            } else {
                this.pixie.getNavigation().moveTo(this.castTarget, this.moveSpeed);
            }
            
            this.pixie.getLookControl().setLookAt(this.castTarget, 30.0F, 30.0F);
            if (--this.castTime == 0) {
                if (!canSee) {
                    return;
                }
                float f = Mth.sqrt(distSq) / this.castRadius;
                this.pixie.castSpell();
                this.castTime = Mth.floor(f * (float)(this.castIntervalMax - this.castIntervalMin) + (float)this.castIntervalMin);
            } else if (this.castTime < 0) {
                float f = Mth.sqrt(distSq) / this.castRadius;
                this.castTime = Mth.floor(f * (float)(this.castIntervalMax - this.castIntervalMin) + (float)this.castIntervalMin);
            }
        }
    }
}

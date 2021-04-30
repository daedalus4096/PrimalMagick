package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import java.util.EnumSet;

import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.payloads.HealingSpellPayload;
import com.verdantartifice.primalmagic.common.spells.vehicles.BoltSpellVehicle;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Base definition for a sun pixie.  In addition to following the player as a companion, heals them
 * when below 75% health.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractSunPixieEntity extends AbstractPixieEntity {
    public AbstractSunPixieEntity(EntityType<? extends AbstractPixieEntity> type, World worldIn) {
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
        this.getSpellPackage().cast(this.world, this, null);
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
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean shouldExecute() {
            LivingEntity target = this.pixie.getCompanionOwner();
            if (target != null && target.isAlive() && (target.getHealth() / target.getMaxHealth()) < 0.75F) {
                this.castTarget = target;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            return this.shouldExecute() || !this.pixie.getNavigator().noPath();
        }

        @Override
        public void resetTask() {
            this.castTarget = null;
            this.seeTime = 0;
            this.castTime = -1;
        }

        @Override
        public void tick() {
            double distSq = this.pixie.getDistanceSq(this.castTarget);
            boolean canSee = this.pixie.getEntitySenses().canSee(this.castTarget);
            if (canSee) {
                this.seeTime++;
            } else {
                this.seeTime = 0;
            }
            
            if (distSq <= this.maxCastDistanceSq && this.seeTime >= 5) {
                this.pixie.getNavigator().clearPath();
            } else {
                this.pixie.getNavigator().tryMoveToEntityLiving(this.castTarget, this.moveSpeed);
            }
            
            this.pixie.getLookController().setLookPositionWithEntity(this.castTarget, 30.0F, 30.0F);
            if (--this.castTime == 0) {
                if (!canSee) {
                    return;
                }
                float f = MathHelper.sqrt(distSq) / this.castRadius;
                this.pixie.castSpell();
                this.castTime = MathHelper.floor(f * (float)(this.castIntervalMax - this.castIntervalMin) + (float)this.castIntervalMin);
            } else if (this.castTime < 0) {
                float f = MathHelper.sqrt(distSq) / this.castRadius;
                this.castTime = MathHelper.floor(f * (float)(this.castIntervalMax - this.castIntervalMin) + (float)this.castIntervalMin);
            }
        }
    }
}

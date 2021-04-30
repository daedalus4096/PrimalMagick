package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.entities.ai.goals.CompanionOwnerHurtByTargetGoal;
import com.verdantartifice.primalmagic.common.entities.ai.goals.CompanionOwnerHurtTargetGoal;
import com.verdantartifice.primalmagic.common.entities.ai.goals.ZoomAtTargetGoal;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.payloads.FlameDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.vehicles.BoltSpellVehicle;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;

/**
 * Base definition for an infernal pixie.  In addition to following the player as a companion, attacks with
 * kamikaze explosive charges.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractInfernalPixieEntity extends AbstractPixieEntity {
    public AbstractInfernalPixieEntity(EntityType<? extends AbstractPixieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected Source getPixieSource() {
        return Source.INFERNAL;
    }

    @Override
    protected SpellPackage createSpellPackage() {
        SpellPackage spell = new SpellPackage("Pixie Bolt");
        BoltSpellVehicle vehicle = new BoltSpellVehicle();
        vehicle.getProperty("range").setValue(5);
        spell.setVehicle(vehicle);
        FlameDamageSpellPayload payload = new FlameDamageSpellPayload();
        payload.getProperty("power").setValue(this.getSpellPower());
        payload.getProperty("duration").setValue(this.getSpellPower());
        spell.setPayload(payload);
        return spell;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new AbstractInfernalPixieEntity.ExplodeOnTargetGoal(this, 1.0F));
        this.goalSelector.addGoal(3, new ZoomAtTargetGoal(this, 2.0F, 8.0F, 0.4F));
        this.goalSelector.addGoal(4, new MoveTowardsTargetGoal(this, 1.0D, 32.0F));
        this.targetSelector.addGoal(1, new CompanionOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new CompanionOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::func_233680_b_));
    }
    
    public void explode() {
        this.dead = true;
        this.remove();
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), (float)this.getSpellPower(), true, Mode.BREAK);
        }
    }
    
    public static class ExplodeOnTargetGoal extends Goal {
        protected final AbstractInfernalPixieEntity pixie;
        protected final float maxRangeSq;
        
        public ExplodeOnTargetGoal(AbstractInfernalPixieEntity pixie, float maxRange) {
            this.pixie = pixie;
            this.maxRangeSq = maxRange * maxRange;
        }
        
        @Override
        public boolean shouldExecute() {
            LivingEntity target = this.pixie.getAttackTarget();
            return target != null && this.pixie.getDistanceSq(target) <= this.maxRangeSq;
        }

        @Override
        public void startExecuting() {
            this.pixie.explode();
        }
    }
}

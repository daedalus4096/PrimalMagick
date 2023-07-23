package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.entities.ai.goals.CompanionOwnerHurtByTargetGoal;
import com.verdantartifice.primalmagick.common.entities.ai.goals.CompanionOwnerHurtTargetGoal;
import com.verdantartifice.primalmagick.common.entities.ai.goals.ZoomAtTargetGoal;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.payloads.FlameDamageSpellPayload;
import com.verdantartifice.primalmagick.common.spells.vehicles.BoltSpellVehicle;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

/**
 * Base definition for an infernal pixie.  In addition to following the player as a companion, attacks with
 * kamikaze explosive charges.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractInfernalPixieEntity extends AbstractPixieEntity {
    public AbstractInfernalPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
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
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
    }
    
    public void explode() {
        Level level = this.level();
        if (!level.isClientSide) {
            Explosion explosion = level.explode(this, this.getX(), this.getY(), this.getZ(), (float)this.getSpellPower(), true, Level.ExplosionInteraction.TNT);
            this.hurt(this.level().damageSources().explosion(explosion), Float.MAX_VALUE);
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
        public boolean canUse() {
            LivingEntity target = this.pixie.getTarget();
            return target != null && this.pixie.distanceToSqr(target) <= this.maxRangeSq;
        }

        @Override
        public void start() {
            this.pixie.explode();
        }
    }
}

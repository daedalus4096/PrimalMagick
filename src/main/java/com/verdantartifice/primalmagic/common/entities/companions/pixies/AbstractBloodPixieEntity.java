package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.entities.ai.goals.CompanionOwnerHurtByTargetGoal;
import com.verdantartifice.primalmagic.common.entities.ai.goals.CompanionOwnerHurtTargetGoal;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.payloads.BloodDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.vehicles.BoltSpellVehicle;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Base definition for a blood pixie.  In addition to following the player as a companion, attacks with
 * blood damage spells.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractBloodPixieEntity extends AbstractPixieEntity implements RangedAttackMob {
    public AbstractBloodPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected Source getPixieSource() {
        return Source.BLOOD;
    }

    @Override
    protected SpellPackage createSpellPackage() {
        SpellPackage spell = new SpellPackage("Pixie Bolt");
        BoltSpellVehicle vehicle = new BoltSpellVehicle();
        vehicle.getProperty("range").setValue(5);
        spell.setVehicle(vehicle);
        BloodDamageSpellPayload payload = new BloodDamageSpellPayload();
        payload.getProperty("power").setValue(this.getSpellPower());
        spell.setPayload(payload);
        return spell;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 20, 30, 16.0F));
        this.goalSelector.addGoal(3, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        this.targetSelector.addGoal(1, new CompanionOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new CompanionOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
    }
    
    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        this.getSpellPackage().cast(this.level, this, null);
    }
}

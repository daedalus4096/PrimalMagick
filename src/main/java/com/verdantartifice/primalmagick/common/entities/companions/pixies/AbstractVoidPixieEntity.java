package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import com.verdantartifice.primalmagick.common.entities.ai.goals.CompanionOwnerHurtByTargetGoal;
import com.verdantartifice.primalmagick.common.entities.ai.goals.CompanionOwnerHurtTargetGoal;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.spells.payloads.AbstractSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.VoidDamageSpellPayload;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Base definition for a void pixie.  In addition to following the player as a companion, attacks with
 * void damage spells.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractVoidPixieEntity extends AbstractPixieEntity implements RangedAttackMob {
    public AbstractVoidPixieEntity(EntityType<? extends AbstractPixieEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected Source getPixieSource() {
        return Sources.VOID;
    }

    @Override
    protected AbstractSpellPayload<?> getSpellPayload() {
        return VoidDamageSpellPayload.INSTANCE;
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
        this.getSpellPackage().cast(this.level(), this, ItemStack.EMPTY);
    }
}

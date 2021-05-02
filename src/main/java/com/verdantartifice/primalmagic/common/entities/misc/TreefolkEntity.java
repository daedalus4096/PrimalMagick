package com.verdantartifice.primalmagic.common.entities.misc;

import java.util.List;
import java.util.UUID;

import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.stats.StatsPM;
import com.verdantartifice.primalmagic.common.util.EntityUtils;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.ResetAngerGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Definition of a treefolk entity, tree-like neutral bipeds that spawn in forest biomes.
 * 
 * @author Daedalus4096
 */
public class TreefolkEntity extends CreatureEntity implements IAngerable {
    protected static final DataParameter<Integer> ANGER_TIME = EntityDataManager.createKey(TreefolkEntity.class, DataSerializers.VARINT);
    protected static final RangedInteger ANGER_TIME_RANGE = TickRangeConverter.convertRange(20, 39);
    protected static final String DREADED_NAME = "Verdus";

    protected UUID angerTarget;

    public TreefolkEntity(EntityType<? extends TreefolkEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public static AttributeModifierMap.MutableAttribute getAttributeModifiers() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 20.0D).createMutableAttribute(Attributes.ARMOR, 4.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        this.writeAngerNBT(compound);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (!this.world.isRemote) {
            this.readAngerNBT((ServerWorld)this.world, compound);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setCallsForHelp());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::func_233680_b_));
        this.targetSelector.addGoal(8, new ResetAngerGoal<>(this, true));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ANGER_TIME, 0);
    }

    @Override
    public int getAngerTime() {
        return this.dataManager.get(ANGER_TIME);
    }

    @Override
    public void setAngerTime(int time) {
        this.dataManager.set(ANGER_TIME, time);
    }

    @Override
    public UUID getAngerTarget() {
        return this.angerTarget;
    }

    @Override
    public void setAngerTarget(UUID target) {
        this.angerTarget = target;
    }

    @Override
    public void func_230258_H__() {
        this.setAngerTime(ANGER_TIME_RANGE.getRandomWithinRange(this.rand));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsPM.TREEFOLK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsPM.TREEFOLK_DEATH.get();
    }

    @Override
    public void livingTick() {
        super.livingTick();
        
        if (this.hasCustomName() && DREADED_NAME.equals(this.getCustomName().getString())) {
            this.setFire(8);
        }
        
        if (!this.world.isRemote) {
            this.func_241359_a_((ServerWorld)this.world, true);
        }
    }

    @Override
    public void setCustomName(ITextComponent name) {
        super.setCustomName(name);
        if (!this.world.isRemote && DREADED_NAME.equals(name.getString())) {
            List<PlayerEntity> nearby = EntityUtils.getEntitiesInRange(this.world, this.getPositionVec(), null, PlayerEntity.class, 6.0D);
            for (PlayerEntity player : nearby) {
                StatsManager.incrementValue(player, StatsPM.TREANTS_NAMED);
            }
        }
    }
}

package com.verdantartifice.primalmagick.common.entities.misc;

import java.util.UUID;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;

/**
 * Definition of a friendly witch entity who will trade with players.  RIP Corspilla.
 * 
 * @author Daedalus4096
 */
public class FriendlyWitchEntity extends AbstractVillager implements RangedAttackMob {
    public static final String HONORED_NAME = "Corspilla";
    private static final UUID SPEED_MODIFIER_DRINKING_UUID = UUID.fromString("6171c72b-f71a-46cd-8afb-d199d159ad56");
    private static final AttributeModifier SPEED_MODIFIER_DRINKING = new AttributeModifier(SPEED_MODIFIER_DRINKING_UUID, "Drinking speed penalty", -0.25D, AttributeModifier.Operation.ADDITION);
    private static final EntityDataAccessor<Boolean> DATA_USING_ITEM = SynchedEntityData.defineId(FriendlyWitchEntity.class, EntityDataSerializers.BOOLEAN);
    private int usingTime;
    
    public FriendlyWitchEntity(EntityType<? extends FriendlyWitchEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_USING_ITEM, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setAge(Math.max(0, this.getAge()));
    }

    public static AttributeSupplier.Builder getAttributeModifiers() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 26.0D).add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    public void setUsingItem(boolean use) {
        this.getEntityData().set(DATA_USING_ITEM, use);
    }

    public boolean isDrinkingPotion() {
        return this.getEntityData().get(DATA_USING_ITEM);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void rewardTradeXp(MerchantOffer offer) {
        if (offer.shouldRewardExp()) {
            int i = 3 + this.random.nextInt(4);
            this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY() + 0.5D, this.getZ(), i));
        }
    }

    @Override
    protected void updateTrades() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return null;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        // TODO Auto-generated method stub
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean removeWhenFarAway(double distSq) {
        return false;
    }
    
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITCH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.WITCH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WITCH_DEATH;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.WITCH_CELEBRATE;
    }

    @Override
    public void notifyTradeUpdated(ItemStack stack) {
        // Do nothing
    }

    @Override
    public void aiStep() {
        // TODO Auto-generated method stub
        super.aiStep();
    }

    @Override
    public void handleEntityEvent(byte val) {
        if (val == 15) {
            for (int i = 0; i < this.random.nextInt(35) + 10; ++i) {
                this.level.addParticle(ParticleTypes.WITCH, this.getX() + this.random.nextGaussian() * (double)0.13F, this.getBoundingBox().maxY + 0.5D + this.random.nextGaussian() * (double)0.13F, this.getZ() + this.random.nextGaussian() * (double)0.13F, 0.0D, 0.0D, 0.0D);
            }
        } else {
            super.handleEntityEvent(val);
        }
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource source, float damage) {
        damage = super.getDamageAfterMagicAbsorb(source, damage);
        if (source.getEntity() == this) {
            damage = 0F;
        }
        if (source.isMagic()) {
            damage *= 0.15F;
        }
        return damage;
    }
}

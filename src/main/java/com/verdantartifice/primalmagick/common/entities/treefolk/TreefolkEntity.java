package com.verdantartifice.primalmagick.common.entities.treefolk;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypesPM;
import com.verdantartifice.primalmagick.common.entities.ai.sensing.SensorTypesPM;
import com.verdantartifice.primalmagick.common.entities.projectiles.AppleEntity;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.util.EntityUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.Brain.Provider;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Definition of a treefolk entity, tree-like neutral bipeds that spawn in forest biomes.
 * 
 * @author Daedalus4096
 */
public class TreefolkEntity extends AgeableMob implements RangedAttackMob {
    public static final Logger LOGGER = LogManager.getLogger();
    protected static final String DREADED_NAME = "Verdus";
    protected static final ImmutableList<SensorType<? extends Sensor<? super TreefolkEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, 
            SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorTypesPM.TREEFOLK_SPECIFIC_SENSOR.get(), SensorTypesPM.NEAREST_VALID_FERTILIZABLE_BLOCKS.get());
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_LIVING_ENTITIES, 
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, 
            MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, 
            MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.ADMIRING_ITEM, 
            MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleType.CELEBRATE_LOCATION, MemoryModuleType.DANCING, 
            MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleTypesPM.NEARBY_ADULT_TREEFOLK.get(), MemoryModuleTypesPM.NEAREST_VISIBLE_ADULT_TREEFOLK.get(),
            MemoryModuleTypesPM.DANCED_RECENTLY.get(), MemoryModuleTypesPM.NEAREST_VALID_FERTILIZABLE_BLOCKS.get(), MemoryModuleTypesPM.FERTILIZE_LOCATION.get(), MemoryModuleTypesPM.FERTILIZED_RECENTLY.get(),
            MemoryModuleTypesPM.TIME_TRYING_TO_REACH_FERTILIZE_BLOCK.get(), MemoryModuleTypesPM.DISABLE_WALK_TO_FERTILIZE_BLOCK.get(), MemoryModuleTypesPM.NEARBY_TREEFOLK.get(),
            MemoryModuleType.AVOID_TARGET);
    private static final EntityDataAccessor<Boolean> DATA_IS_DANCING = SynchedEntityData.defineId(TreefolkEntity.class, EntityDataSerializers.BOOLEAN);

    public TreefolkEntity(EntityType<? extends TreefolkEntity> entityType, Level world) {
        super(entityType, world);
        this.setCanPickUpLoot(true);
        this.xpReward = 5;
    }
    
    public static AttributeSupplier.Builder getAttributeModifiers() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.ARMOR, 4.0D).add(Attributes.MOVEMENT_SPEED, 0.35D).add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    public static boolean canSpawnOn(EntityType<? extends Mob> typeIn, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return Mob.checkMobSpawnRules(typeIn, worldIn, reason, pos, randomIn);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_DANCING, false);
    }
    
    public boolean isAngry() {
        return this.isAggressive();
    }
    
    public boolean isAdult() {
        return !this.isBaby();
    }

    @Override
    protected Provider<TreefolkEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> pDynamic) {
        return TreefolkAi.makeBrain(this, this.brainProvider().makeBrain(pDynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Brain<TreefolkEntity> getBrain() {
        return (Brain<TreefolkEntity>)super.getBrain();
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
    protected void customServerAiStep() {
        Level level = this.level();
        level.getProfiler().push("treefolkBrain");
        this.getBrain().tick((ServerLevel)level, this);
        level.getProfiler().pop();
        TreefolkAi.updateActivity(this);
        super.customServerAiStep();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.hasCustomName() && DREADED_NAME.equals(this.getCustomName().getString())) {
            this.setSecondsOnFire(8);
        }
    }

    @Override
    public void setCustomName(Component name) {
        super.setCustomName(name);
        Level level = this.level();
        if (!level.isClientSide && DREADED_NAME.equals(name.getString())) {
            List<Player> nearby = EntityUtils.getEntitiesInRange(level, this.position(), null, Player.class, 6.0D);
            for (Player player : nearby) {
                StatsManager.incrementValue(player, StatsPM.TREANTS_NAMED);
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        Level level = this.level();
        AppleEntity missile = new AppleEntity(level, this);
        missile.setItem(new ItemStack(Items.APPLE));
        double d0 = target.getEyeY() - (double)1.1F;
        double d1 = target.getX() - this.getX();
        double d2 = d0 - missile.getY();
        double d3 = target.getZ() - this.getZ();
        float f = (float)Math.sqrt(d1 * d1 + d3 * d3) * 0.2F;
        missile.shoot(d1, d2 + (double)f, d3, 1.6F, (float)(14 - level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SNOWBALL_THROW, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        level.addFreshEntity(missile);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        Level level = this.level();
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.FLINT_AND_STEEL)) {
            level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!level.isClientSide) {
                this.setSecondsOnFire(10);
                this.setLastHurtByMob(player);
                stack.hurtAndBreak(1, player, p -> {
                    p.broadcastBreakEvent(hand);
                });
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (!level.isClientSide) {
            return TreefolkAi.mobInteract(this, player, hand);
        } else {
            boolean flag = TreefolkAi.canAdmire(this, stack) && this.getArmPose() != TreefolkArmPose.ADMIRING_ITEM;
            return flag ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }

    @Override
    public boolean wantsToPickUp(ItemStack pStack) {
        return ForgeEventFactory.getMobGriefingEvent(this.level(), this) && this.canPickUpLoot() && TreefolkAi.wantsToPickup(this, pStack);
    }

    @Override
    protected void pickUpItem(ItemEntity pItemEntity) {
        this.onItemPickup(pItemEntity);
        TreefolkAi.pickUpItem(this, pItemEntity);
    }

    public void holdInOffHand(ItemStack stack) {
        if (TreefolkAi.isLovedItem(stack)) {
            this.setItemSlot(EquipmentSlot.OFFHAND, stack);
            this.setGuaranteedDrop(EquipmentSlot.OFFHAND);
        } else {
            this.setItemSlotAndDropWhenKilled(EquipmentSlot.OFFHAND, stack);
        }
    }
    
    public TreefolkArmPose getArmPose() {
        if (this.isDancing()) {
            return TreefolkArmPose.DANCING;
        } else if (TreefolkAi.isLovedItem(this.getOffhandItem())) {
            return TreefolkArmPose.ADMIRING_ITEM;
        } else {
            return TreefolkArmPose.DEFAULT;
        }
    }

    public boolean isDancing() {
        return this.entityData.get(DATA_IS_DANCING);
    }

    public void setDancing(boolean pDancing) {
        this.entityData.set(DATA_IS_DANCING, pDancing);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        boolean flag = super.hurt(pSource, pAmount);
        if (this.level().isClientSide) {
            return false;
        } else {
            if (flag && pSource.getEntity() instanceof LivingEntity livingSource) {
                TreefolkAi.wasHurtBy(this, livingSource);
            }
            return flag;
        }
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    public LivingEntity getTarget() {
        return this.brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return EntityTypesPM.TREEFOLK.get().create(pLevel);
    }
}

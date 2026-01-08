package com.verdantartifice.primalmagick.common.entities.misc;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.pixies.PixieRank;
import com.verdantartifice.primalmagick.common.entities.pixies.guardians.AbstractGuardianPixieEntity;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.misc.IPixieItem;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.tags.DamageTypeTagsPM;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PixieHouseEntity extends Mob implements NeutralMob {
    public static final EntityDataAccessor<ItemStack> DATA_HOUSED_PIXIE = SynchedEntityData.defineId(PixieHouseEntity.class, EntityDataSerializers.ITEM_STACK);
    public static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_DEPLOYED_PIXIE = SynchedEntityData.defineId(PixieHouseEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    public static final EntityDataAccessor<Long> DATA_ANGER_END_TIME = SynchedEntityData.defineId(PixieHouseEntity.class, EntityDataSerializers.LONG);
    protected static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    public static final long WOBBLE_TIME = 5L;
    private static final byte HIT_EVENT = 32;

    @NotNull private ItemStack housedPixie = ItemStack.EMPTY;
    @Nullable private AbstractGuardianPixieEntity deployedPixieCache;
    @Nullable private LivingEntity target;
    @Nullable protected EntityReference<LivingEntity> angerTarget;
    protected int targetTimestamp;
    public long lastHit;

    public PixieHouseEntity(EntityType<? extends PixieHouseEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder getAttributeModifiers() {
        return createLivingAttributes().add(Attributes.STEP_HEIGHT, 0.0D).add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    private boolean hasPhysics() {
        return !this.isNoGravity();
    }

    public boolean isEffectiveAi() {
        return super.isEffectiveAi() && this.hasPhysics();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new DeployGuardianPixieGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Monster.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_HOUSED_PIXIE, ItemStack.EMPTY);
        pBuilder.define(DATA_DEPLOYED_PIXIE, Optional.empty());
        pBuilder.define(DATA_ANGER_END_TIME, 0L);
    }

    @NotNull
    public ItemStack getHousedPixie() {
        return this.housedPixie;
    }

    public void setHousedPixie(@NotNull ItemStack pixie) {
        this.housedPixie = pixie.copy();
        this.entityData.set(DATA_HOUSED_PIXIE, this.housedPixie);
    }

    @NotNull
    public Optional<EntityReference<LivingEntity>> getDeployedPixieReference() {
        return this.entityData.get(DATA_DEPLOYED_PIXIE);
    }

    @Nullable
    public AbstractGuardianPixieEntity getDeployedPixie() {
        if (this.deployedPixieCache == null) {
            this.deployedPixieCache = this.entityData.get(DATA_DEPLOYED_PIXIE)
                    .map(u -> EntityReference.getLivingEntity(u, this.level()) instanceof AbstractGuardianPixieEntity pixie ? pixie : null)
                    .orElse(null);
        }
        return this.deployedPixieCache;
    }

    public void setDeployedPixie(@NotNull AbstractGuardianPixieEntity pixie) {
        this.deployedPixieCache = pixie;
        this.entityData.set(DATA_DEPLOYED_PIXIE, Optional.of(EntityReference.of(pixie)));
    }

    public void removeDeployedPixie() {
        AbstractGuardianPixieEntity pixie = this.getDeployedPixie();
        if (pixie != null) {
            this.deployedPixieCache = null;
            this.entityData.set(DATA_DEPLOYED_PIXIE, Optional.empty());
            pixie.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();

        // Ensure the locally cached housed pixie is up to date
        ItemStack pixieStack = this.entityData.get(DATA_HOUSED_PIXIE);
        if (!this.housedPixie.equals(pixieStack)) {
            this.setHousedPixie(pixieStack);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level() instanceof ServerLevel serverLevel) {
            this.updatePersistentAnger(serverLevel, true);
        }
    }

    @Override
    @NotNull
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        Level level = this.level();
        this.readPersistentAngerSaveData(level, input);
        if (EntityReference.getLivingEntity(EntityReference.read(input, "DeployedPixie"), level) instanceof AbstractGuardianPixieEntity pixie) {
            this.setDeployedPixie(pixie);
        }
        input.read("HousedPixie", ItemStack.OPTIONAL_CODEC).ifPresent(this::setHousedPixie);
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        this.addPersistentAngerSaveData(output);
        output.store("HousedPixie", ItemStack.OPTIONAL_CODEC, this.housedPixie);
        this.getDeployedPixieReference().ifPresent(ref -> ref.store(output, "DeployedPixie"));
    }

    public boolean isPushable() {
        return false;
    }

    protected void doPush(@NotNull Entity pEntity) {
    }

    @Override
    @NotNull
    public InteractionResult interactAt(@NotNull Player pPlayer, @NotNull Vec3 pVec, @NotNull InteractionHand pHand) {
        ItemStack heldStack = pPlayer.getItemInHand(pHand);
        if (heldStack.getItem() instanceof IPixieItem) {
            ItemStack stack = heldStack.copyWithCount(1);
            this.setHousedPixie(stack);
            heldStack.shrink(1);
            return InteractionResult.SUCCESS;
        } else if (heldStack.isEmpty() && !this.housedPixie.isEmpty()) {
            ItemStack stack = this.housedPixie.copy();
            if (!pPlayer.getInventory().add(stack)) {
                pPlayer.drop(stack, false);
            }
            this.setHousedPixie(ItemStack.EMPTY);
            this.removeDeployedPixie();
            this.playSound(SoundEvents.BAT_HURT, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        } else {
            return super.interactAt(pPlayer, pVec, pHand);
        }
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource pSource, float pAmount) {
        if (this.isRemoved()) {
            return false;
        } else {
            if (pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                this.kill(serverLevel);
                return false;
            } else if (!this.isInvulnerableTo(serverLevel, pSource)) {
                if (pSource.is(DamageTypeTags.IS_EXPLOSION)) {
                    this.brokenByAnything(serverLevel, pSource);
                    this.kill(serverLevel);
                    return false;
                } else if (pSource.is(DamageTypeTagsPM.IGNITES_PIXIE_HOUSES)) {
                    if (this.isOnFire()) {
                        this.causeDamage(serverLevel, pSource, 0.15F);
                    } else {
                        this.igniteForSeconds(5.0F);
                    }

                    return false;
                } else if (pSource.is(DamageTypeTagsPM.BURNS_PIXIE_HOUSES) && this.getHealth() > 0.5F) {
                    this.causeDamage(serverLevel, pSource, 4.0F);
                    return false;
                } else {
                    boolean canBreak = pSource.is(DamageTypeTagsPM.CAN_BREAK_PIXIE_HOUSES);
                    boolean alwaysKill = pSource.is(DamageTypeTagsPM.ALWAYS_KILLS_PIXIE_HOUSES);
                    if (!canBreak && !alwaysKill) {
                        return false;
                    } else {
                        if (pSource.getEntity() instanceof Player player) {
                            if (!player.getAbilities().mayBuild) {
                                return false;
                            }
                        }

                        if (pSource.isCreativePlayer()) {
                            this.playBrokenSound();
                            this.showBreakingParticles();
                            this.kill(serverLevel);
                        } else {
                            long i = serverLevel.getGameTime();
                            if (i - this.lastHit > WOBBLE_TIME && !alwaysKill) {
                                serverLevel.broadcastEntityEvent(this, HIT_EVENT);
                                this.gameEvent(GameEvent.ENTITY_DAMAGE, pSource.getEntity());
                                this.lastHit = i;
                            } else {
                                this.brokenByPlayer(serverLevel, pSource);
                                this.showBreakingParticles();
                                this.kill(serverLevel);
                            }
                        }
                        return true;
                    }
                }
            } else {
                return false;
            }
        }
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == HIT_EVENT) {
            Level level = this.level();
            if (level.isClientSide()) {
                level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ARMOR_STAND_HIT, this.getSoundSource(), 0.3F, 1.0F, false);
                this.lastHit = level.getGameTime();
            }
        } else {
            super.handleEntityEvent(pId);
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        double d0 = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(d0) || d0 == 0.0D) {
            d0 = 4.0D;
        }

        d0 *= 64.0D;
        return pDistance < d0 * d0;
    }

    private void showBreakingParticles() {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_PLANKS.defaultBlockState()), this.getX(), this.getY(0.6666666666666666D), this.getZ(), 10, this.getBbWidth() / 4.0D, this.getBbHeight() / 4.0D, this.getBbWidth() / 4.0D, 0.05D);
        }
    }

    private void causeDamage(ServerLevel pLevel, DamageSource pDamageSource, float pDamageAmount) {
        float newHealth = this.getHealth() - pDamageAmount;
        if (newHealth <= 0.5F) {
            this.brokenByAnything(pLevel, pDamageSource);
            this.kill(pLevel);
        } else {
            this.setHealth(newHealth);
            this.gameEvent(GameEvent.ENTITY_DAMAGE, pDamageSource.getEntity());
        }
    }

    private void brokenByPlayer(ServerLevel pLevel, DamageSource pDamageSource) {
        ItemStack itemstack = new ItemStack(ItemsPM.PIXIE_HOUSE.get());
        itemstack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
        Block.popResource(this.level(), this.blockPosition(), itemstack);
        this.brokenByAnything(pLevel, pDamageSource);
    }

    private void brokenByAnything(ServerLevel pLevel, DamageSource pDamageSource) {
        this.playBrokenSound();
        this.dropAllDeathLoot(pLevel, pDamageSource);
        this.handlePixiesWhenBroken(pLevel);
    }

    private void handlePixiesWhenBroken(Level level) {
        // Discard any pixies that are deployed
        this.removeDeployedPixie();

        if (!this.housedPixie.isEmpty()) {
            Block.popResource(level, this.blockPosition().above(), this.housedPixie);
            this.housedPixie = ItemStack.EMPTY;
        }
    }

    private void playBrokenSound() {
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ARMOR_STAND_BREAK, this.getSoundSource(), 1.0F, 1.0F);
    }

    @Override
    protected void tickHeadTurn(float pYRot) {
        this.yBodyRotO = this.yRotO;
        this.yBodyRot = this.getYRot();
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        if (this.hasPhysics()) {
            super.travel(pTravelVector);
        }
    }

    @Override
    public void kill(@NotNull ServerLevel serverLevel) {
        this.handlePixiesWhenBroken(serverLevel);
        this.remove(RemovalReason.KILLED);
        this.gameEvent(GameEvent.ENTITY_DIE);
    }

    @Override
    public boolean skipAttackInteraction(@NotNull Entity pEntity) {
        return pEntity instanceof Player player && !this.level().mayInteract(player, this.blockPosition());
    }

    @Override
    @NotNull
    public LivingEntity.Fallsounds getFallSounds() {
        return new LivingEntity.Fallsounds(SoundEvents.ARMOR_STAND_FALL, SoundEvents.ARMOR_STAND_FALL);
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.ARMOR_STAND_HIT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ARMOR_STAND_BREAK;
    }

    @Override
    public void thunderHit(@NotNull ServerLevel pLevel, @NotNull LightningBolt pLightning) {
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean attackable() {
        return false;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ItemsPM.PIXIE_HOUSE.get());
    }

    @Override
    public long getPersistentAngerEndTime() {
        return this.entityData.get(DATA_ANGER_END_TIME);
    }

    @Override
    public void setPersistentAngerEndTime(long time) {
        this.entityData.set(DATA_ANGER_END_TIME, time);
    }

    @Override
    public @Nullable EntityReference<LivingEntity> getPersistentAngerTarget() {
        return this.angerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> target) {
        this.angerTarget = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setTimeToRemainAngry(ANGER_TIME_RANGE.sample(this.random));
    }

    @Override
    public void setTarget(@Nullable LivingEntity livingEntity) {
        this.target = livingEntity;
        this.targetTimestamp = this.tickCount;
    }

    @Override
    public @Nullable LivingEntity getTarget() {
        return this.target;
    }

    public int getTargetTimestamp() {
        return this.targetTimestamp;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Nullable
    private static EntityType<? extends AbstractGuardianPixieEntity> getGuardianEntityType(@NotNull ItemStack housedPixieStack) {
        if (!housedPixieStack.isEmpty() && housedPixieStack.getItem() instanceof IPixieItem pixieItem) {
            return switch (pixieItem.getPixieRank()) {
                case PixieRank.BASIC -> EntityTypesPM.BASIC_GUARDIAN_PIXIE.get();
                case PixieRank.GRAND -> EntityTypesPM.GRAND_GUARDIAN_PIXIE.get();
                case PixieRank.MAJESTIC -> EntityTypesPM.MAJESTIC_GUARDIAN_PIXIE.get();
            };
        } else {
            return null;
        }
    }

    @Nullable
    private static Source getGuardianSource(@NotNull ItemStack housedPixieStack) {
        if (!housedPixieStack.isEmpty() && housedPixieStack.getItem() instanceof IPixieItem pixieItem) {
            return pixieItem.getPixieSource();
        } else {
            return null;
        }
    }

    public void deployGuardian() {
        EntityType<? extends AbstractGuardianPixieEntity> entityType = getGuardianEntityType(this.housedPixie);
        Source entitySource = getGuardianSource(this.housedPixie);
        if (entityType != null && entitySource != null && this.level() instanceof ServerLevel serverLevel) {
            Vec3 pos = this.getPosition(0F).add(0D, 1.5D, 0D);
            AbstractGuardianPixieEntity pixie = AbstractGuardianPixieEntity.spawn(entityType, entitySource, this, serverLevel, BlockPos.containing(pos));
            this.setDeployedPixie(pixie);
            serverLevel.playSound(null, pos.x(), pos.y(), pos.z(), SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    public void undeployGuardian() {
        this.removeDeployedPixie();
        Vec3 pos = this.getPosition(0F).add(0D, 1.5D, 0D);
        this.level().playSound(null, pos.x(), pos.y(), pos.z(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    protected static class DeployGuardianPixieGoal extends Goal {
        private final PixieHouseEntity mob;

        public DeployGuardianPixieGoal(@NotNull PixieHouseEntity mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            return this.mob.isAlive() && !this.mob.getHousedPixie().isEmpty() && this.mob.getDeployedPixie() == null && this.mob.getTarget() != null && this.mob.getTarget().isAlive();
        }

        @Override
        public void start() {
            this.mob.deployGuardian();
        }
    }
}

package com.verdantartifice.primalmagick.common.entities.misc;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class PixieHouseEntity extends LivingEntity {
    public static final int MAX_OCCUPANCY = 1;

    private static final long WOBBLE_TIME = 5L;
    private static final byte HIT_EVENT = 32;

    private final NonNullList<ItemStack> housedPixies = NonNullList.withSize(MAX_OCCUPANCY, ItemStack.EMPTY);
    public long lastHit;

    public PixieHouseEntity(EntityType<? extends PixieHouseEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public PixieHouseEntity(Level pLevel, double pX, double pY, double pZ) {
        this(EntityTypesPM.PIXIE_HOUSE.get(), pLevel);
        this.setPos(pX, pY, pZ);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createLivingAttributes().add(Attributes.STEP_HEIGHT, 0.0D);
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
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        // TODO Sync type of pixie currently housed
        // TODO Sync source of pixie currently housed
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.contains("HousedPixies", Tag.TAG_LIST)) {
            ListTag pixieList = compoundTag.getList("HousedPixies", Tag.TAG_COMPOUND);
            for (int index = 0; index < this.housedPixies.size(); index++) {
                CompoundTag pixieTag = pixieList.getCompound(index);
                this.housedPixies.set(index, ItemStack.parseOptional(this.registryAccess(), pixieTag));
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        ListTag pixieList = new ListTag();
        for (ItemStack pixieStack : this.housedPixies) {
            pixieList.add(pixieStack.saveOptional(this.registryAccess()));
        }
        compoundTag.put("HousedPixies", pixieList);
    }

    public boolean isPushable() {
        return false;
    }

    protected void doPush(Entity pEntity) {
    }

    @Override
    public InteractionResult interactAt(Player pPlayer, Vec3 pVec, InteractionHand pHand) {
        // TODO Either open a GUI or add pixie from hand directly
        return super.interactAt(pPlayer, pVec, pHand);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        // FIXME Update tag uses away from armor stands
        if (this.isRemoved()) {
            return false;
        } else {
            if (this.level() instanceof ServerLevel serverLevel) {
                if (pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                    this.kill();
                    return false;
                } else if (!this.isInvulnerableTo(pSource)) {
                    if (pSource.is(DamageTypeTags.IS_EXPLOSION)) {
                        this.brokenByAnything(serverLevel, pSource);
                        this.kill();
                        return false;
                    } else if (pSource.is(DamageTypeTags.IGNITES_ARMOR_STANDS)) {
                        if (this.isOnFire()) {
                            this.causeDamage(serverLevel, pSource, 0.15F);
                        } else {
                            this.igniteForSeconds(5.0F);
                        }

                        return false;
                    } else if (pSource.is(DamageTypeTags.BURNS_ARMOR_STANDS) && this.getHealth() > 0.5F) {
                        this.causeDamage(serverLevel, pSource, 4.0F);
                        return false;
                    } else {
                        boolean canBreak = pSource.is(DamageTypeTags.CAN_BREAK_ARMOR_STAND);
                        boolean alwaysKill = pSource.is(DamageTypeTags.ALWAYS_KILLS_ARMOR_STANDS);
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
                                this.kill();
                                return true;
                            } else {
                                long i = serverLevel.getGameTime();
                                if (i - this.lastHit > WOBBLE_TIME && !alwaysKill) {
                                    serverLevel.broadcastEntityEvent(this, HIT_EVENT);
                                    this.gameEvent(GameEvent.ENTITY_DAMAGE, pSource.getEntity());
                                    this.lastHit = i;
                                } else {
                                    this.brokenByPlayer(serverLevel, pSource);
                                    this.showBreakingParticles();
                                    this.kill();
                                }

                                return true;
                            }
                        }
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == HIT_EVENT) {
            if (this.level().isClientSide) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ARMOR_STAND_HIT, this.getSoundSource(), 0.3F, 1.0F, false);
                this.lastHit = this.level().getGameTime();
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
            this.kill();
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

        // TODO Discard any pixies that are deployed

        for(int index = 0; index < this.housedPixies.size(); index++) {
            ItemStack pixieStack = this.housedPixies.get(index);
            if (!pixieStack.isEmpty()) {
                Block.popResource(this.level(), this.blockPosition().above(), pixieStack);
                this.housedPixies.set(index, ItemStack.EMPTY);
            }
        }
    }

    private void playBrokenSound() {
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ARMOR_STAND_BREAK, this.getSoundSource(), 1.0F, 1.0F);
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (this.hasPhysics()) {
            super.travel(pTravelVector);
        }
    }

    @Override
    public void kill() {
        this.remove(RemovalReason.KILLED);
        this.gameEvent(GameEvent.ENTITY_DIE);
    }

    @Override
    public boolean skipAttackInteraction(Entity pEntity) {
        return pEntity instanceof Player player && !this.level().mayInteract(player, this.blockPosition());
    }

    @Override
    public LivingEntity.Fallsounds getFallSounds() {
        return new LivingEntity.Fallsounds(SoundEvents.ARMOR_STAND_FALL, SoundEvents.ARMOR_STAND_FALL);
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.ARMOR_STAND_HIT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ARMOR_STAND_BREAK;
    }

    @Override
    public void thunderHit(ServerLevel pLevel, LightningBolt pLightning) {
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
}

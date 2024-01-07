package com.verdantartifice.primalmagick.common.entities.projectiles;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagick.common.concoctions.FuseType;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.PotionExplosionPacket;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

/**
 * Definition for a thrown alchemcial bomb entity.  Detonates after a short time and applies a loaded
 * potion effect to all living entities in range.
 * 
 * @author Daedalus4096
 */
public class AlchemicalBombEntity extends ThrowableItemProjectile implements ItemSupplier {
    public static final Predicate<LivingEntity> WATER_SENSITIVE = LivingEntity::isSensitiveToWater;

    public AlchemicalBombEntity(EntityType<? extends AlchemicalBombEntity> entityType, Level world) {
        super(entityType, world);
    }
    
    public AlchemicalBombEntity(Level world, LivingEntity entity) {
        super(EntityTypesPM.ALCHEMICAL_BOMB.get(), entity, world);
    }
    
    public AlchemicalBombEntity(Level world, double x, double y, double z) {
        super(EntityTypesPM.ALCHEMICAL_BOMB.get(), x, y, z, world);
    }
    
    @Override
    protected Item getDefaultItem() {
        return ItemsPM.ALCHEMICAL_BOMB.get();
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }

    @Override
    public void tick() {
        super.tick();
        FuseType fuse = ConcoctionUtils.getFuseType(this.getItem());
        if (fuse != null && fuse.hasTimer() && this.tickCount >= fuse.getFuseLength()) {
            this.detonate(null);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        FuseType fuse = ConcoctionUtils.getFuseType(this.getItem());
        if (fuse == FuseType.IMPACT) {
            this.detonate(null);
        } else if (fuse != null) {
            double mx = result.getDirection().getStepX() == 0 ? 1.0D : -1.0D;
            double my = 0.9D * (result.getDirection().getStepY() == 0 ? 1.0D : -1.0D);
            double mz = result.getDirection().getStepZ() == 0 ? 1.0D : -1.0D;
            this.setDeltaMovement(this.getDeltaMovement().scale(0.7D).multiply(mx, my, mz));
            Level level = this.level();
            if (!level.isClientSide) {
                float volume = Mth.clamp((float)this.getDeltaMovement().length(), 0.0F, 1.0F);
                this.playSound(SoundsPM.CLANK.get(), volume, 0.8F + (0.4F * level.random.nextFloat()));
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        this.detonate(result.getEntity());
    }
    
    private void detonate(@Nullable Entity struckEntity) {
        Level level = this.level();
        if (!level.isClientSide) {
            ItemStack itemStack = this.getItem();
            Potion potion = PotionUtils.getPotion(itemStack);
            List<MobEffectInstance> effects = PotionUtils.getMobEffects(itemStack);

            // Do explosion audio visual effects
            PacketHandler.sendToAllAround(new PotionExplosionPacket(this.position(), PotionUtils.getColor(itemStack), potion.hasInstantEffects()), level.dimension(), this.blockPosition(), 32.0D);

            // Apply potion effects
            if (potion == Potions.WATER && effects.isEmpty()) {
                this.applyWater();
            } else if (!effects.isEmpty()) {
                this.applyPotionEffects(effects, struckEntity);
            }

            this.discard();
        }
    }
    
    private void applyWater() {
        AABB aabb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, aabb, WATER_SENSITIVE);
        for (LivingEntity entity : entities) {
            double distanceSq = this.distanceToSqr(entity);
            if (distanceSq < 16.0D && entity.isSensitiveToWater()) {
                entity.hurt(this.level().damageSources().indirectMagic(entity, this.getOwner()), 1.0F);
            }
        }
        
        BlockPos pos = this.blockPosition();
        this.extinguishFire(pos);
        for (Direction dir : Direction.values()) {
            this.extinguishFire(pos.relative(dir));
        }
    }

    private void applyPotionEffects(List<MobEffectInstance> effects, @Nullable Entity struckEntity) {
        AABB aabb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
        List<LivingEntity> entityList = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
        for (LivingEntity entity : entityList) {
            if (entity.isAffectedByPotions()) {
                double distanceSq = this.distanceToSqr(entity);
                if (distanceSq < 16.0D) {
                    double multiplier = (entity == struckEntity) ? 1.0D : 1.0D - Math.sqrt(distanceSq) / 4.0D;
                    for (MobEffectInstance effectInstance : effects) {
                        MobEffect effect = effectInstance.getEffect();
                        if (effect.isInstantenous()) {
                            effect.applyInstantenousEffect(this, this.getOwner(), entity, effectInstance.getAmplifier(), multiplier);
                        } else {
                            int scaledDuration = (int)(multiplier * (double)effectInstance.getDuration() + 0.5D);
                            if (scaledDuration > 20) {
                                entity.addEffect(new MobEffectInstance(effect, scaledDuration, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible()));
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void extinguishFire(BlockPos pos) {
        Level level = this.level();
        BlockState state = level.getBlockState(pos);
        if (state.is(BlockTags.FIRE)) {
            level.removeBlock(pos, false);
        } else if (CampfireBlock.isLitCampfire(state)) {
            level.levelEvent(null, 1009, pos, 0);
            CampfireBlock.dowse(this.getOwner(), level, pos, state);
            level.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
        }
    }
}

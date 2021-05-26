package com.verdantartifice.primalmagic.common.entities.projectiles;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagic.common.concoctions.FuseType;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Definition for a thrown alchemcial bomb entity.  Detonates after a short time and applies a loaded
 * potion effect to all living entities in range.
 * 
 * @author Daedalus4096
 */
@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class AlchemicalBombEntity extends ProjectileItemEntity implements IRendersAsItem {
    public static final Predicate<LivingEntity> WATER_SENSITIVE = LivingEntity::isWaterSensitive;

    public AlchemicalBombEntity(EntityType<? extends AlchemicalBombEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public AlchemicalBombEntity(World world, LivingEntity entity) {
        super(EntityTypesPM.ALCHEMICAL_BOMB.get(), entity, world);
    }
    
    public AlchemicalBombEntity(World world, double x, double y, double z) {
        super(EntityTypesPM.ALCHEMICAL_BOMB.get(), x, y, z, world);
    }
    
    @Override
    protected Item getDefaultItem() {
        return ItemsPM.ALCHEMICAL_BOMB.get();
    }

    @Override
    protected float getGravityVelocity() {
        return 0.05F;
    }

    @Override
    public void tick() {
        super.tick();
        FuseType fuse = ConcoctionUtils.getFuseType(this.getItem());
        if (fuse != null && fuse.hasTimer() && this.ticksExisted >= fuse.getFuseLength()) {
            this.detonate(null);
        }
    }

    @Override
    protected void func_230299_a_(BlockRayTraceResult result) {
        if (!this.world.isRemote) {
            FuseType fuse = ConcoctionUtils.getFuseType(this.getItem());
            if (fuse == FuseType.IMPACT) {
                this.detonate(null);
            } else if (fuse != null) {
                double mx = result.getFace().getXOffset() == 0 ? 1.0D : -1.0D;
                double my = result.getFace().getYOffset() == 0 ? 1.0D : -1.0D;
                double mz = result.getFace().getZOffset() == 0 ? 1.0D : -1.0D;
                this.setMotion(this.getMotion().scale(0.6D).mul(mx, my, mz));
                // TODO Play impact noise
            }
        }
        super.func_230299_a_(result);
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        this.detonate(result.getEntity());
    }
    
    private void detonate(@Nullable Entity struckEntity) {
        // TODO Do explosion effects
        
        if (!this.world.isRemote) {
            ItemStack itemStack = this.getItem();
            Potion potion = PotionUtils.getPotionFromItem(itemStack);
            List<EffectInstance> effects = PotionUtils.getEffectsFromStack(itemStack);
            
            if (potion == Potions.WATER && effects.isEmpty()) {
                this.applyWater();
            } else if (!effects.isEmpty()) {
                this.applyPotionEffects(effects, struckEntity);
            }

            this.world.playEvent(potion.hasInstantEffect() ? 2007 : 2002, this.getPosition(), PotionUtils.getColor(itemStack)); // TODO Remove when explosion effects are added
            this.remove();
        }
    }
    
    private void applyWater() {
        AxisAlignedBB aabb = this.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
        List<LivingEntity> entities = this.world.getEntitiesWithinAABB(LivingEntity.class, aabb, WATER_SENSITIVE);
        for (LivingEntity entity : entities) {
            double distanceSq = this.getDistanceSq(entity);
            if (distanceSq < 16.0D && entity.isWaterSensitive()) {
                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(entity, this.getShooter()), 1.0F);
            }
        }
        
        BlockPos pos = this.getPosition();
        this.extinguishFire(pos);
        for (Direction dir : Direction.values()) {
            this.extinguishFire(pos.offset(dir));
        }
    }

    private void applyPotionEffects(List<EffectInstance> effects, @Nullable Entity struckEntity) {
        AxisAlignedBB aabb = this.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
        List<LivingEntity> entityList = this.world.getEntitiesWithinAABB(LivingEntity.class, aabb);
        for (LivingEntity entity : entityList) {
            if (entity.canBeHitWithPotion()) {
                double distanceSq = this.getDistanceSq(entity);
                if (distanceSq < 16.0D) {
                    double multiplier = (entity == struckEntity) ? 1.0D : 1.0D - Math.sqrt(distanceSq) / 4.0D;
                    for (EffectInstance effectInstance : effects) {
                        Effect effect = effectInstance.getPotion();
                        if (effect.isInstant()) {
                            effect.affectEntity(this, this.getShooter(), entity, effectInstance.getAmplifier(), multiplier);
                        } else {
                            int scaledDuration = (int)(multiplier * (double)effectInstance.getDuration() + 0.5D);
                            if (scaledDuration > 20) {
                                entity.addPotionEffect(new EffectInstance(effect, scaledDuration, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.doesShowParticles()));
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void extinguishFire(BlockPos pos) {
        BlockState state = this.world.getBlockState(pos);
        if (state.isIn(BlockTags.FIRE)) {
            this.world.removeBlock(pos, false);
        } else if (CampfireBlock.isLit(state)) {
            this.world.playEvent(null, 1009, pos, 0);
            CampfireBlock.extinguish(this.world, pos, state);
            this.world.setBlockState(pos, state.with(CampfireBlock.LIT, false));
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

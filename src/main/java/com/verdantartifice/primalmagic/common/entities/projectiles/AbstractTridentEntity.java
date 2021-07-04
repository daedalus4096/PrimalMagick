package com.verdantartifice.primalmagic.common.entities.projectiles;

import javax.annotation.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Base class definition for a thrown magical metal trident entity.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTridentEntity extends AbstractArrowEntity {
    protected static final DataParameter<Byte> LOYALTY_LEVEL = EntityDataManager.createKey(AbstractTridentEntity.class, DataSerializers.BYTE);
    protected static final DataParameter<Boolean> HAS_GLINT = EntityDataManager.createKey(AbstractTridentEntity.class, DataSerializers.BOOLEAN);
    protected ItemStack thrownStack;
    protected boolean dealtDamage;
    public int returningTicks;
    
    public AbstractTridentEntity(EntityType<? extends AbstractTridentEntity> type, World worldIn) {
        super(type, worldIn);
    }
    
    public AbstractTridentEntity(EntityType<? extends AbstractTridentEntity> type, World worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(type, thrower, worldIn);
        this.thrownStack = thrownStackIn.copy();
        this.dataManager.set(LOYALTY_LEVEL, (byte)EnchantmentHelper.getLoyaltyModifier(this.thrownStack));
        this.dataManager.set(HAS_GLINT, this.thrownStack.hasEffect());
    }
    
    @OnlyIn(Dist.CLIENT)
    public AbstractTridentEntity(EntityType<? extends AbstractTridentEntity> type, World worldIn, double x, double y, double z) {
        super(type, x, y, z, worldIn);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(LOYALTY_LEVEL, (byte)0);
        this.dataManager.register(HAS_GLINT, false);
    }

    @Override
    public void tick() {
        if (this.timeInGround > 4) {
            this.dealtDamage = true;
        }
        
        Entity shooter = this.getShooter();
        if ((this.dealtDamage || this.getNoClip()) && shooter != null) {
            int loyalty = this.dataManager.get(LOYALTY_LEVEL);
            if (loyalty > 0 && !this.shouldReturnToThrower()) {
                if (!this.world.isRemote && this.pickupStatus == AbstractArrowEntity.PickupStatus.ALLOWED) {
                    this.entityDropItem(this.getArrowStack(), 0.1F);
                }
                this.remove();
            } else if (loyalty > 0) {
                this.setNoClip(true);
                Vector3d vector3d = new Vector3d(shooter.getPosX() - this.getPosX(), shooter.getPosYEye() - this.getPosY(), shooter.getPosZ() - this.getPosZ());
                this.setRawPosition(this.getPosX(), this.getPosY() + vector3d.y * 0.015D * (double)loyalty, this.getPosZ());
                if (this.world.isRemote) {
                    this.lastTickPosY = this.getPosY();
                }
                
                double d0 = 0.05D * (double)loyalty;
                this.setMotion(this.getMotion().scale(0.95D).add(vector3d.normalize().scale(d0)));
                if (this.returningTicks == 0) {
                    this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                }
                
                this.returningTicks++;
            }
        }
        
        super.tick();
    }

    private boolean shouldReturnToThrower() {
        Entity shooter = this.getShooter();
        if (shooter != null && shooter.isAlive()) {
            return !(shooter instanceof ServerPlayerEntity) || !shooter.isSpectator();
        } else {
            return false;
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return this.thrownStack.copy();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean hasGlint() {
        return this.dataManager.get(HAS_GLINT);
    }

    @Override
    @Nullable
    protected EntityRayTraceResult rayTraceEntities(Vector3d startVec, Vector3d endVec) {
        return this.dealtDamage ? null : super.rayTraceEntities(startVec, endVec);
    }
    
    protected abstract float getBaseDamage();

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        Entity entity = result.getEntity();
        float damage = this.getBaseDamage();
        if (entity instanceof LivingEntity) {
            damage += EnchantmentHelper.getModifierForCreature(this.thrownStack, ((LivingEntity)entity).getCreatureAttribute());
        }
        
        Entity shooter = this.getShooter();
        DamageSource damageSource = DamageSource.causeTridentDamage(this, (Entity)(shooter == null ? this : shooter));
        this.dealtDamage = true;
        SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_HIT;
        if (entity.attackEntityFrom(damageSource, damage)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                if (shooter instanceof LivingEntity) {
                    EnchantmentHelper.applyThornEnchantments(livingEntity, shooter);
                    EnchantmentHelper.applyArthropodEnchantments((LivingEntity)shooter, livingEntity);
                }
                this.arrowHit(livingEntity);
            }
        }
        
        this.setMotion(this.getMotion().mul(-0.01D, -0.1D, -0.01D));
        float volume = 1.0F;
        if (this.world instanceof ServerWorld && this.world.isThundering() && EnchantmentHelper.hasChanneling(this.thrownStack)) {
            BlockPos pos = entity.getPosition();
            if (this.world.canSeeSky(pos)) {
                LightningBoltEntity lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(this.world);
                lightningBoltEntity.moveForced(Vector3d.copyCenteredHorizontally(pos));
                lightningBoltEntity.setCaster(shooter instanceof ServerPlayerEntity ? (ServerPlayerEntity)shooter : null);
                this.world.addEntity(lightningBoltEntity);
                soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
                volume = 5.0F;
            }
        }
        
        this.playSound(soundEvent, volume, 1.0F);
    }

    @Override
    protected SoundEvent getHitEntitySound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    @Override
    public void onCollideWithPlayer(PlayerEntity entityIn) {
        Entity shooter = this.getShooter();
        if (shooter == null || shooter.getUniqueID() == entityIn.getUniqueID()) {
            super.onCollideWithPlayer(entityIn);
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("Trident", Constants.NBT.TAG_COMPOUND)) {
            this.thrownStack = ItemStack.read(compound.getCompound("Trident"));
        }
        this.dealtDamage = compound.getBoolean("DealtDamage");
        this.dataManager.set(LOYALTY_LEVEL, (byte)EnchantmentHelper.getLoyaltyModifier(this.thrownStack));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("Trident", this.thrownStack.write(new CompoundNBT()));
        compound.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    protected void func_225516_i_() {
        if (this.pickupStatus != AbstractArrowEntity.PickupStatus.ALLOWED || this.dataManager.get(LOYALTY_LEVEL) <= 0) {
            super.func_225516_i_();
        }
    }

    @Override
    protected float getWaterDrag() {
        return 0.99F;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

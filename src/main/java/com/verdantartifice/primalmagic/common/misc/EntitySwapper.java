package com.verdantartifice.primalmagic.common.misc;

import java.awt.Color;
import java.util.Collection;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.capabilities.IWorldEntitySwappers;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.effects.EffectsPM;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.WandPoofPacket;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

public class EntitySwapper implements INBTSerializable<CompoundNBT> {
    protected UUID targetId = null;
    protected EntityType<?> entityType = null;
    protected CompoundNBT originalData = null;
    protected Optional<Integer> polymorphDuration = Optional.empty();
    protected int delay = 0;
    
    protected EntitySwapper() {}
    
    public EntitySwapper(@Nonnull UUID targetId, @Nonnull EntityType<?> entityType, @Nullable CompoundNBT originalData, @Nonnull Optional<Integer> polymorphDuration, int delay) {
        this.targetId = targetId;
        this.entityType = entityType;
        this.originalData = originalData;
        this.polymorphDuration = polymorphDuration;
        this.delay = delay;
    }
    
    public EntitySwapper(CompoundNBT tag) {
        this();
        this.deserializeNBT(tag);
    }
    
    public static boolean enqueue(@Nonnull World world, @Nullable EntitySwapper swapper) {
        if (swapper == null) {
            return false;
        } else {
            IWorldEntitySwappers swappers = PrimalMagicCapabilities.getEntitySwappers(world);
            if (swappers == null) {
                return false;
            } else {
                return swappers.enqueue(swapper);
            }
        }
    }
    
    @Nullable
    public static Queue<EntitySwapper> getWorldSwappers(@Nonnull World world) {
        IWorldEntitySwappers swappers = PrimalMagicCapabilities.getEntitySwappers(world);
        if (swappers == null) {
            return null;
        } else {
            return swappers.getQueue();
        }
    }
    
    public static boolean setWorldSwapperQueue(@Nonnull World world, @Nonnull Queue<EntitySwapper> swapperQueue) {
        IWorldEntitySwappers swappers = PrimalMagicCapabilities.getEntitySwappers(world);
        if (swappers == null) {
            return false;
        } else {
            return swappers.setQueue(swapperQueue);
        }
    }
    
    @Nullable
    public EntitySwapper execute(@Nonnull World world) {
        if (!world.isRemote && world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld)world;
            Entity target = serverWorld.getEntityByUuid(this.targetId);
            if (this.isValid() && target != null && this.isValidTarget(target)) {
                LivingEntity livingTarget = (LivingEntity)target;
                if (livingTarget.isPassenger()) {
                    livingTarget.stopRiding();
                }
                if (livingTarget.isBeingRidden()) {
                    livingTarget.removePassengers();
                }
                
                EntityType<?> oldType = livingTarget.getType();
                Vec3d targetPos = livingTarget.getPositionVec();
                Vec2f targetRots = livingTarget.getPitchYaw();
                ITextComponent customName = livingTarget.getCustomName();
                double healthPercentage = (double)livingTarget.getHealth() / (double)livingTarget.getMaxHealth();
                Collection<EffectInstance> activeEffects = livingTarget.getActivePotionEffects();
                
                CompoundNBT data = null;
                if (this.originalData != null) {
                    data = new CompoundNBT();
                    data.put("EntityTag", this.pruneData(this.originalData, this.polymorphDuration.isPresent()));
                }
                
                PacketHandler.sendToAllAround(new WandPoofPacket(targetPos.x, targetPos.y, targetPos.z, Color.WHITE.getRGB(), true, null), 
                        world.dimension.getType(), new BlockPos(targetPos), 32.0D);
                
                livingTarget.remove();
                Entity newEntity = this.entityType.create(world, data, customName, null, new BlockPos(targetPos), SpawnReason.MOB_SUMMONED, false, false);
                world.addEntity(newEntity);
                newEntity.setPositionAndRotation(targetPos.x, targetPos.y, targetPos.z, targetRots.y, targetRots.x);
                if (newEntity instanceof LivingEntity) {
                    LivingEntity newLivingEntity = (LivingEntity)newEntity;
                    newLivingEntity.setHealth((float)(healthPercentage * newLivingEntity.getMaxHealth()));
                    for (EffectInstance activeEffect : activeEffects) {
                        newLivingEntity.addPotionEffect(activeEffect);
                    }
                }
                
                if (this.polymorphDuration.isPresent()) {
                    int ticks = this.polymorphDuration.get().intValue();
                    if (newEntity instanceof LivingEntity) {
                        ((LivingEntity)newEntity).addPotionEffect(new EffectInstance(EffectsPM.POLYMORPH, ticks));
                    }
                    return new EntitySwapper(newEntity.getUniqueID(), oldType, this.originalData, Optional.empty(), ticks);
                } else {
                    return null;
                }
            }
        }
        return null;
    }
    
    protected boolean isValidTarget(Entity entity) {
        return (entity instanceof LivingEntity) && !(entity instanceof PlayerEntity);
    }
    
    public void decrementDelay() {
        this.delay--;
    }
    
    public boolean isReady() {
        return (delay <= 0);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        if (this.targetId != null) {
            nbt.putUniqueId("TargetId", this.targetId);
        }
        if (this.entityType != null) {
            nbt.putString("EntityType", EntityType.getKey(this.entityType).toString());
        }
        if (this.originalData != null) {
            nbt.put("OriginalData", this.originalData);
        }
        if (this.polymorphDuration != null && this.polymorphDuration.isPresent()) {
            nbt.putInt("PolymorphDuration", this.polymorphDuration.get().intValue());
        }
        nbt.putInt("Delay", this.delay);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.hasUniqueId("TargetId")) {
            this.targetId = nbt.getUniqueId("TargetId");
        }
        if (nbt.contains("EntityType")) {
            this.entityType = EntityType.byKey(nbt.getString("EntityType")).orElse(null);
        }
        if (nbt.contains("OriginalData")) {
            this.originalData = nbt.getCompound("OriginalData");
        }
        this.polymorphDuration = nbt.contains("PolymorphDuration") ? 
                Optional.of(Integer.valueOf(nbt.getInt("PolymorphDuration"))) : 
                Optional.empty();
        this.delay = nbt.getInt("Delay");
    }
    
    public boolean isValid() {
        return this.targetId != null && this.entityType != null;
    }
    
    @Nonnull
    protected CompoundNBT pruneData(CompoundNBT data, boolean initialSwap) {
        CompoundNBT prunedData = new CompoundNBT();
        prunedData.merge(data);
        
        // Always remove the entity's ID so there are no spawning conflicts
        prunedData.remove("id");
        prunedData.remove("UUIDMost");
        prunedData.remove("UUIDLeast");
        
        if (initialSwap) {
            // A freshly swapped entity should have the attributes of its new form, not its old one
            prunedData.remove("Health");
            prunedData.remove("Attributes");
            prunedData.remove("ActiveEffects");
        } else {
            // Swapping back should ignore time-sensitive data that may have changed while the entity was transformed
            prunedData.remove("Pos");
            prunedData.remove("Motion");
            prunedData.remove("Rotation");
            prunedData.remove("FallDistance");
            prunedData.remove("Fire");
            prunedData.remove("Air");
            prunedData.remove("OnGround");
            prunedData.remove("Glowing");
            prunedData.remove("Health");
            prunedData.remove("AbsorptionAmount");
            prunedData.remove("DeathTime");
            prunedData.remove("Attributes");
            prunedData.remove("ActiveEffects");
        }
        return prunedData;
    }
}

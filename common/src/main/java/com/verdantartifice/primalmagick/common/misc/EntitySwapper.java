package com.verdantartifice.primalmagick.common.misc;

import com.verdantartifice.primalmagick.common.capabilities.IWorldEntitySwappers;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.WandPoofPacket;
import com.verdantartifice.primalmagick.common.util.INBTSerializablePM;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.util.Collection;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

/**
 * Definition of an entity swapper data structure.  Processed during server ticks to replace a specific
 * entity with another of a given type.  Optionally schedules a second swapper to reverse the original
 * swap.  Rather than a static registry, entity swappers are stored in a capability attached to the
 * relevant world.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.capabilities.IWorldEntitySwappers}
 */
@SuppressWarnings("deprecation")
public class EntitySwapper implements INBTSerializablePM<CompoundTag> {
    protected UUID targetId = null;
    protected EntityType<?> entityType = null;
    protected CompoundTag originalData = null;
    protected Optional<Integer> polymorphDuration = Optional.empty();
    protected int delay = 0;
    
    protected EntitySwapper() {}
    
    public EntitySwapper(@Nonnull UUID targetId, @Nonnull EntityType<?> entityType, @Nullable CompoundTag originalData, @Nonnull Optional<Integer> polymorphDuration, int delay) {
        this.targetId = targetId;
        this.entityType = entityType;
        this.originalData = originalData;
        this.polymorphDuration = polymorphDuration;
        this.delay = delay;
    }
    
    public EntitySwapper(HolderLookup.Provider registries, CompoundTag tag) {
        // Attempt to deserialize an entity swapper from the given NBT data
        this();
        this.deserializeNBT(registries, tag);
    }
    
    public static boolean enqueue(@Nonnull Level world, @Nullable EntitySwapper swapper) {
        if (swapper == null) {
            // Don't allow empty swappers in the queue
            return false;
        } else {
            IWorldEntitySwappers swappers = PrimalMagickCapabilities.getEntitySwappers(world);
            if (swappers == null) {
                return false;
            } else {
                // Store the new swapper in the world capability
                return swappers.enqueue(swapper);
            }
        }
    }
    
    @Nullable
    public static Queue<EntitySwapper> getWorldSwappers(@Nonnull Level world) {
        IWorldEntitySwappers swappers = PrimalMagickCapabilities.getEntitySwappers(world);
        if (swappers == null) {
            return null;
        } else {
            return swappers.getQueue();
        }
    }
    
    public static boolean setWorldSwapperQueue(@Nonnull Level world, @Nonnull Queue<EntitySwapper> swapperQueue) {
        IWorldEntitySwappers swappers = PrimalMagickCapabilities.getEntitySwappers(world);
        if (swappers == null) {
            return false;
        } else {
            return swappers.setQueue(swapperQueue);
        }
    }
    
    @Nullable
    public EntitySwapper execute(@Nonnull Level world) {
        if (!world.isClientSide && world instanceof ServerLevel serverWorld) {
            Entity target = serverWorld.getEntity(this.targetId);
            
            // Only proceed if this is a valid swapper and the target is allowed to be swapped
            if (this.isValid() && target != null && this.isValidTarget(target)) {
                LivingEntity livingTarget = (LivingEntity)target;
                CompoundTag currentData = livingTarget.saveWithoutId(new CompoundTag());
                
                // Dismount the target from any mounts and dismount any other entities riding the target
                if (livingTarget.isPassenger()) {
                    livingTarget.stopRiding();
                }
                if (livingTarget.isVehicle()) {
                    livingTarget.ejectPassengers();
                }
                
                EntityType<?> oldType = livingTarget.getType();
                Vec3 targetPos = livingTarget.position();
                Vec2 targetRots = livingTarget.getRotationVector();
                Component customName = livingTarget.getCustomName();
                boolean customNameVisible = livingTarget.isCustomNameVisible();
                double healthPercentage = (double)livingTarget.getHealth() / (double)livingTarget.getMaxHealth();
                Collection<MobEffectInstance> activeEffects = livingTarget.getActiveEffects();
                
                // If the original, pre-swapped entity had it's NBT data preserved in this swapper, prepare it for loading into the new entity
                CompoundTag data = this.originalData == null ?
                        currentData :
                        currentData.merge(this.pruneData(this.originalData, this.polymorphDuration.isPresent()));
                
                // Send an FX packet to all nearby player clients
                PacketHandler.sendToAllAround(new WandPoofPacket(targetPos.x, targetPos.y, targetPos.z, Color.WHITE.getRGB(), true, Direction.UP), 
                        serverWorld, BlockPos.containing(targetPos), 32.0D);
                
                // Remove the target entity and spawn a new one of the target type into the world
                livingTarget.discard();
                Entity newEntity = this.entityType.spawn(serverWorld, e -> {
                    if (!data.isEmpty()) {
                        e.load(data);
                    }
                }, BlockPos.containing(targetPos), MobSpawnType.MOB_SUMMONED, false, false);
                if (newEntity != null) {
                    newEntity.setCustomName(customName);
                    newEntity.setCustomNameVisible(customNameVisible);
                    newEntity.absMoveTo(targetPos.x, targetPos.y, targetPos.z, targetRots.y, targetRots.x);
                    serverWorld.gameEvent(null, GameEvent.ENTITY_PLACE, targetPos);
                }
                
                // Carry over the previous entity's percentage health and active potion effects
                if (newEntity instanceof LivingEntity newLivingEntity) {
                    newLivingEntity.setHealth((float)(healthPercentage * newLivingEntity.getMaxHealth()));
                    for (MobEffectInstance activeEffect : activeEffects) {
                        newLivingEntity.addEffect(activeEffect);
                    }
                }
                
                if (this.polymorphDuration.isPresent()) {
                    // If this is a temporary swap, create a new entity swapper to swap back
                    int ticks = this.polymorphDuration.get().intValue();
                    if (newEntity instanceof LivingEntity newLivingEntity) {
                        newLivingEntity.addEffect(new MobEffectInstance(EffectsPM.POLYMORPH.getHolder(), ticks));
                    }
                    return new EntitySwapper(newEntity.getUUID(), oldType, this.originalData, Optional.empty(), ticks);
                } else {
                    return null;
                }
            }
        }
        return null;
    }
    
    protected boolean isValidTarget(Entity entity) {
        // Only living, non-player entities may be swapped (e.g. no boats, etc)
        return (entity instanceof LivingEntity) && !(entity instanceof Player);
    }
    
    public void decrementDelay() {
        this.delay--;
    }
    
    public boolean isReady() {
        return (delay <= 0);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();
        if (this.targetId != null) {
            nbt.putUUID("TargetId", this.targetId);
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
    public void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt) {
        if (nbt.hasUUID("TargetId")) {
            this.targetId = nbt.getUUID("TargetId");
        }
        if (nbt.contains("EntityType")) {
            this.entityType = EntityType.byString(nbt.getString("EntityType")).orElse(null);
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
    protected CompoundTag pruneData(CompoundTag data, boolean initialSwap) {
        CompoundTag prunedData = new CompoundTag();
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

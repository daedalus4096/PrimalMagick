package com.verdantartifice.primalmagick.common.misc;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.capabilities.IEntitySwappers;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.WandPoofPacket;
import com.verdantartifice.primalmagick.common.util.INBTSerializablePM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.Collection;
import java.util.Optional;
import java.util.Queue;

/**
 * Definition of an entity swapper data structure.  Processed during server ticks to replace a specific
 * entity with another of a given type.  Optionally schedules a second swapper to reverse the original
 * swap.  Rather than a static registry, entity swappers are stored in a capability attached to the
 * relevant entity.
 * 
 * @author Daedalus4096
 * @see IEntitySwappers
 */
public class EntitySwapper implements INBTSerializablePM<CompoundTag> {
    protected static final Logger LOGGER = LogUtils.getLogger();

    protected EntityType<?> entityType = null;
    protected CompoundTag originalData = new CompoundTag();
    protected Optional<Integer> polymorphDuration = Optional.empty();
    protected int delay = 0;
    
    protected EntitySwapper() {}
    
    public EntitySwapper(@NotNull EntityType<?> entityType, @Nullable CompoundTag originalData, @NotNull Optional<Integer> polymorphDuration, int delay) {
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
    
    public static void enqueue(@NotNull Entity entity, @Nullable EntitySwapper swapper) {
        // Don't allow empty swappers in the queue
        if (swapper != null) {
            // Store the new swapper in the entity capability
            Services.CAPABILITIES.swappers(entity).map(swappers -> swappers.enqueue(swapper));
        }
    }
    
    @Nullable
    public static Queue<EntitySwapper> getSwapperQueue(@NotNull Entity entity) {
        return Services.CAPABILITIES.swappers(entity).map(IEntitySwappers::getQueue).orElse(null);
    }
    
    public static void setSwapperQueue(@NotNull Entity entity, @NotNull Queue<EntitySwapper> swapperQueue) {
        Services.CAPABILITIES.swappers(entity).map(swappers -> swappers.setQueue(swapperQueue));
    }
    
    public void execute(@NotNull Entity entity) {
        Level world = entity.level();
        if (!world.isClientSide() && world instanceof ServerLevel serverWorld) {
            // Only proceed if this is a valid swapper and the target is allowed to be swapped
            if (this.isValid() && this.isValidTarget(entity)) {
                LivingEntity livingTarget = (LivingEntity)entity;
                CompoundTag currentData;
                try (ProblemReporter.ScopedCollector problems = new ProblemReporter.ScopedCollector(LOGGER)) {
                    TagValueOutput out = TagValueOutput.createWithContext(problems, world.registryAccess());
                    livingTarget.saveWithoutId(out);
                    currentData = out.buildResult();
                }

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
                        try (ProblemReporter.ScopedCollector problems = new ProblemReporter.ScopedCollector(LOGGER)) {
                            ValueInput in = TagValueInput.create(problems, serverWorld.registryAccess(), data);
                            e.load(in);
                        }
                    }
                }, BlockPos.containing(targetPos), EntitySpawnReason.MOB_SUMMONED, false, false);
                if (newEntity != null) {
                    newEntity.setCustomName(customName);
                    newEntity.setCustomNameVisible(customNameVisible);
                    newEntity.absSnapTo(targetPos.x, targetPos.y, targetPos.z, targetRots.y, targetRots.x);
                    serverWorld.gameEvent(null, GameEvent.ENTITY_PLACE, targetPos);
                }
                
                // Carry over the previous entity's percentage health and active potion effects
                if (newEntity instanceof LivingEntity newLivingEntity) {
                    newLivingEntity.setHealth((float)(healthPercentage * newLivingEntity.getMaxHealth()));
                    for (MobEffectInstance activeEffect : activeEffects) {
                        newLivingEntity.addEffect(activeEffect);
                    }
                }

                this.polymorphDuration.ifPresent(ticks -> {
                    // If this is a temporary swap, create a new entity swapper to swap back
                    if (newEntity instanceof LivingEntity newLivingEntity) {
                        newLivingEntity.addEffect(new MobEffectInstance(EffectsPM.POLYMORPH.getHolder(), ticks));
                        enqueue(newLivingEntity, new EntitySwapper(oldType, this.originalData, Optional.empty(), ticks));
                    }
                });
            }
        }
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
        nbt.store("EntityType", EntityType.CODEC, this.entityType);
        if (this.originalData != null) {
            nbt.put("OriginalData", this.originalData);
        }
        this.polymorphDuration.ifPresent(duration -> nbt.putInt("PolymorphDuration", duration));
        nbt.putInt("Delay", this.delay);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt) {
        this.entityType = nbt.read("EntityType", EntityType.CODEC).orElse(null);
        this.originalData = nbt.getCompoundOrEmpty("OriginalData");
        this.polymorphDuration = nbt.getInt("PolymorphDuration");
        this.delay = nbt.getIntOr("Delay", 0);
    }
    
    public boolean isValid() {
        return this.entityType != null;
    }
    
    @NotNull
    protected CompoundTag pruneData(CompoundTag data, boolean initialSwap) {
        CompoundTag prunedData = new CompoundTag();
        prunedData.merge(data);
        
        // Always remove the entity's ID so there are no spawning conflicts
        prunedData.remove("id");
        prunedData.remove("UUID");

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

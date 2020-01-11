package com.verdantartifice.primalmagic.common.misc;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.effects.EffectsPM;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.WandPoofPacket;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class EntitySwapper {
    protected static final Map<Integer, Queue<EntitySwapper>> REGISTRY = new HashMap<>();
    
    protected UUID targetId;
    protected EntityType<?> entityType;
    protected Optional<Integer> polymorphDuration;
    protected int delay;
    
    public EntitySwapper(@Nonnull UUID targetId, @Nonnull EntityType<?> entityType, @Nonnull Optional<Integer> polymorphDuration, int delay) {
        this.targetId = targetId;
        this.entityType = entityType;
        this.polymorphDuration = polymorphDuration;
        this.delay = delay;
    }
    
    public static boolean enqueue(@Nonnull World world, @Nullable EntitySwapper swapper) {
        if (swapper == null) {
            return false;
        } else {
            return getWorldSwappers(world).offer(swapper);
        }
    }
    
    @Nonnull
    public static Queue<EntitySwapper> getWorldSwappers(@Nonnull World world) {
        int dim = world.getDimension().getType().getId();
        Queue<EntitySwapper> swapperQueue = REGISTRY.get(Integer.valueOf(dim));
        if (swapperQueue == null) {
            swapperQueue = new LinkedBlockingQueue<>();
            REGISTRY.put(Integer.valueOf(dim), swapperQueue);
        }
        return swapperQueue;
    }
    
    public static void setWorldSwapperQueue(@Nonnull World world, @Nonnull Queue<EntitySwapper> swapperQueue) {
        int dim = world.getDimension().getType().getId();
        REGISTRY.put(Integer.valueOf(dim), swapperQueue);
    }
    
    @Nullable
    public EntitySwapper execute(@Nonnull World world) {
        PrimalMagic.LOGGER.info("Doing entity swap");
        if (!world.isRemote && world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld)world;
            Entity target = serverWorld.getEntityByUuid(this.targetId);
            if (target != null && this.isValidTarget(target)) {
                if (target.isPassenger()) {
                    target.stopRiding();
                }
                if (target.isBeingRidden()) {
                    target.removePassengers();
                }
                
                EntityType<?> oldType = target.getType();
                Vec3d targetPos = target.getPositionVec();
                Vec2f targetRots = target.getPitchYaw();
                ITextComponent customName = target.getCustomName();
                
                PacketHandler.sendToAllAround(new WandPoofPacket(targetPos.x, targetPos.y, targetPos.z, Color.WHITE.getRGB(), true, null), 
                        world.dimension.getType(), new BlockPos(targetPos), 32.0D);
                
                target.remove();
                Entity newEntity = this.entityType.create(world, null, customName, null, new BlockPos(targetPos), SpawnReason.MOB_SUMMONED, false, false);
                world.addEntity(newEntity);
                newEntity.setPositionAndRotation(targetPos.x, targetPos.y, targetPos.z, targetRots.y, targetRots.x);
                
                if (this.polymorphDuration.isPresent()) {
                    int ticks = this.polymorphDuration.get().intValue();
                    if (newEntity instanceof LivingEntity) {
                        ((LivingEntity)newEntity).addPotionEffect(new EffectInstance(EffectsPM.POLYMORPH, ticks));
                    }
                    return new EntitySwapper(newEntity.getUniqueID(), oldType, Optional.empty(), ticks);
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
}

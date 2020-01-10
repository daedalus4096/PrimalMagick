package com.verdantartifice.primalmagic.common.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

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
        // TODO Do swap
        PrimalMagic.LOGGER.info("Doing entity swap");
        return null;
    }
    
    public void decrementDelay() {
        this.delay--;
    }
    
    public boolean isReady() {
        return (delay <= 0);
    }
}

package com.verdantartifice.primalmagick.common.capabilities;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.misc.EntitySwapper;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Default implementation of the world entity swappers capability.
 * 
 * @author Daedalus4096
 */
public class WorldEntitySwappers implements IWorldEntitySwappers {
    private final Queue<EntitySwapper> swappers = new LinkedBlockingQueue<>();  // Queue of active entity swappers for the world

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag rootTag = new CompoundTag();
        ListTag swapperList = new ListTag();
        for (EntitySwapper swapper : this.swappers) {
            if (swapper != null) {
                swapperList.add(swapper.serializeNBT());
            }
        }
        rootTag.put("Swappers", swapperList);
        return rootTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt == null) {
            return;
        }
        this.swappers.clear();
        ListTag swapperList = nbt.getList("Swappers", Tag.TAG_COMPOUND);
        for (int index = 0; index < swapperList.size(); index++) {
            CompoundTag swapperTag = swapperList.getCompound(index);
            EntitySwapper swapper = new EntitySwapper(swapperTag);
            if (swapper.isValid()) {
                // Only accept valid swappers
                this.swappers.offer(swapper);
            }
        }
    }

    @Override
    public boolean enqueue(EntitySwapper swapper) {
        if (swapper == null) {
            return false;
        } else {
            return this.swappers.offer(swapper);
        }
    }

    @Override
    public Queue<EntitySwapper> getQueue() {
        return this.swappers;
    }

    @Override
    public boolean setQueue(Queue<EntitySwapper> queue) {
        if (queue == null) {
            return false;
        } else {
            // Make a shallow copy of the given queue
            this.swappers.clear();
            for (EntitySwapper swapper : queue) {
                this.enqueue(swapper);
            }
            return true;
        }
    }
    
    /**
     * Capability provider for the world entity swapper capability.  Used to attach capability data to the owner.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagick.common.events.CapabilityEvents}
     */
    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        public static final ResourceLocation NAME = PrimalMagick.resource("capability_world_entity_swappers");

        private final IWorldEntitySwappers instance = new WorldEntitySwappers();
        private final LazyOptional<IWorldEntitySwappers> holder = LazyOptional.of(() -> instance);  // Cache a lazy optional of the capability instance
        
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == PrimalMagickCapabilities.ENTITY_SWAPPERS) {
                return holder.cast();
            } else {
                return LazyOptional.empty();
            }
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.deserializeNBT(nbt);
        }
    }
}

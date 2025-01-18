package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.misc.EntitySwapper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Default implementation of the entity swappers capability.
 * 
 * @author Daedalus4096
 */
public class EntitySwappers implements IEntitySwappers {
    private final Queue<EntitySwapper> swappers = new LinkedBlockingQueue<>();  // Queue of active entity swappers for the world

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider registries) {
        CompoundTag rootTag = new CompoundTag();
        ListTag swapperList = new ListTag();
        for (EntitySwapper swapper : this.swappers) {
            if (swapper != null) {
                swapperList.add(swapper.serializeNBT(registries));
            }
        }
        rootTag.put("Swappers", swapperList);
        return rootTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt) {
        if (nbt == null) {
            return;
        }
        this.swappers.clear();
        ListTag swapperList = nbt.getList("Swappers", Tag.TAG_COMPOUND);
        for (int index = 0; index < swapperList.size(); index++) {
            CompoundTag swapperTag = swapperList.getCompound(index);
            EntitySwapper swapper = new EntitySwapper(registries, swapperTag);
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
}

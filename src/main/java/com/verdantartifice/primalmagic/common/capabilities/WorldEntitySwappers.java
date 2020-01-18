package com.verdantartifice.primalmagic.common.capabilities;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.misc.EntitySwapper;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
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
    public CompoundNBT serializeNBT() {
        CompoundNBT rootTag = new CompoundNBT();
        ListNBT swapperList = new ListNBT();
        for (EntitySwapper swapper : this.swappers) {
            if (swapper != null) {
                swapperList.add(swapper.serializeNBT());
            }
        }
        rootTag.put("Swappers", swapperList);
        return rootTag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt == null) {
            return;
        }
        this.swappers.clear();
        ListNBT swapperList = nbt.getList("Swappers", 10);
        for (int index = 0; index < swapperList.size(); index++) {
            CompoundNBT swapperTag = swapperList.getCompound(index);
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
     * @see {@link com.verdantartifice.primalmagic.common.events.CapabilityEvents}
     */
    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        public static final ResourceLocation NAME = new ResourceLocation(PrimalMagic.MODID, "capability_world_entity_swappers");

        private final IWorldEntitySwappers instance = PrimalMagicCapabilities.ENTITY_SWAPPERS.getDefaultInstance();
        private final LazyOptional<IWorldEntitySwappers> holder = LazyOptional.of(() -> instance);  // Cache a lazy optional of the capability instance
        
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == PrimalMagicCapabilities.ENTITY_SWAPPERS) {
                return holder.cast();
            } else {
                return LazyOptional.empty();
            }
        }

        @Override
        public CompoundNBT serializeNBT() {
            return instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            instance.deserializeNBT(nbt);
        }
    }
    
    /**
     * Storage manager for the world entity swapper capability.  Used to register the capability.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.init.InitCapabilities}
     */
    public static class Storage implements Capability.IStorage<IWorldEntitySwappers> {
        @Override
        public INBT writeNBT(Capability<IWorldEntitySwappers> capability, IWorldEntitySwappers instance, Direction side) {
            // Use the instance's pre-defined serialization
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<IWorldEntitySwappers> capability, IWorldEntitySwappers instance, Direction side, INBT nbt) {
            // Use the instance's pre-defined deserialization
            instance.deserializeNBT((CompoundNBT)nbt);
        }
    }
    
    /**
     * Factory for the world entity swapper capability.  Used to register the capability.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.init.InitCapabilities}
     */
    public static class Factory implements Callable<IWorldEntitySwappers> {
        @Override
        public IWorldEntitySwappers call() throws Exception {
            return new WorldEntitySwappers();
        }
    }
}

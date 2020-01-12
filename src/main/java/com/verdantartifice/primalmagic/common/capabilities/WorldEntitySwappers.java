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

public class WorldEntitySwappers implements IWorldEntitySwappers {
    private final Queue<EntitySwapper> swappers = new LinkedBlockingQueue<>();

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
            this.swappers.clear();
            for (EntitySwapper swapper : queue) {
                this.enqueue(swapper);
            }
            return true;
        }
    }
    
    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        public static final ResourceLocation NAME = new ResourceLocation(PrimalMagic.MODID, "capability_world_entity_swappers");

        private final IWorldEntitySwappers instance = PrimalMagicCapabilities.ENTITY_SWAPPERS.getDefaultInstance();
        private final LazyOptional<IWorldEntitySwappers> holder = LazyOptional.of(() -> instance);
        
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
    
    public static class Storage implements Capability.IStorage<IWorldEntitySwappers> {
        @Override
        public INBT writeNBT(Capability<IWorldEntitySwappers> capability, IWorldEntitySwappers instance, Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<IWorldEntitySwappers> capability, IWorldEntitySwappers instance, Direction side, INBT nbt) {
            instance.deserializeNBT((CompoundNBT)nbt);
        }
    }
    
    public static class Factory implements Callable<IWorldEntitySwappers> {
        @Override
        public IWorldEntitySwappers call() throws Exception {
            return new WorldEntitySwappers();
        }
    }
}

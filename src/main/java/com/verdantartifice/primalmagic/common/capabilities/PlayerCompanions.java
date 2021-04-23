package com.verdantartifice.primalmagic.common.capabilities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Default implementation of the player companion capability.
 * 
 * @author Daedalus4096
 */
public class PlayerCompanions implements IPlayerCompanions {
    private Map<CompanionType, LinkedList<UUID>> companions = new ConcurrentHashMap<>();

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT rootTag = new CompoundNBT();
        for (CompanionType type : CompanionType.values()) {
            ListNBT list = new ListNBT();
            List<UUID> companions = this.get(type);
            for (UUID id : companions) {
                CompoundNBT companionTag = new CompoundNBT();
                companionTag.putUniqueId("Id", id);
                list.add(companionTag);
            }
            rootTag.put(type.getString(), list);
        }
        return rootTag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.clear();
        for (CompanionType type : CompanionType.values()) {
            if (nbt.contains(type.getString(), Constants.NBT.TAG_LIST)) {
                ListNBT list = nbt.getList(type.getString(), Constants.NBT.TAG_COMPOUND);
                for (int index = 0; index < list.size(); index++) {
                    CompoundNBT companionTag = list.getCompound(index);
                    if (companionTag.hasUniqueId("Id")) {
                        this.companions.computeIfAbsent(type, (t) -> new LinkedList<>()).add(companionTag.getUniqueId("Id"));
                    }
                }
            }
        }
    }

    @Override
    public UUID add(CompanionType type, UUID id) {
        LinkedList<UUID> list = this.companions.computeIfAbsent(type, (t) -> new LinkedList<>());
        if (list.contains(id)) {
            return null;
        } else {
            list.add(id);
            if (list.size() > type.getLimit()) {
                return list.pollFirst();
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean contains(CompanionType type, UUID id) {
        return this.companions.getOrDefault(type, new LinkedList<>()).contains(id);
    }

    @Override
    public List<UUID> get(CompanionType type) {
        return Collections.unmodifiableList(this.companions.getOrDefault(type, new LinkedList<>()));
    }

    @Override
    public boolean remove(CompanionType type, UUID id) {
        return this.companions.getOrDefault(type, new LinkedList<>()).remove(id);
    }

    @Override
    public void clear() {
        this.companions.clear();
    }

    @Override
    public void sync(ServerPlayerEntity player) {
        if (player != null) {
            // TODO send sync packet
        }
    }

    /**
     * Capability provider for the player companions capability.  Used to attach capability data to the owner.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.events.CapabilityEvents}
     */
    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        public static final ResourceLocation NAME = new ResourceLocation(PrimalMagic.MODID, "capability_companions");
        
        private final IPlayerCompanions instance = PrimalMagicCapabilities.COMPANIONS.getDefaultInstance();
        private final LazyOptional<IPlayerCompanions> holder = LazyOptional.of(() -> instance);  // Cache a lazy optional of the capability instance

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == PrimalMagicCapabilities.COMPANIONS) {
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
     * Storage manager for the player companions capability.  Used to register the capability.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.init.InitCapabilities}
     */
    public static class Storage implements Capability.IStorage<IPlayerCompanions> {
        @Override
        public INBT writeNBT(Capability<IPlayerCompanions> capability, IPlayerCompanions instance, Direction side) {
            // Use the instance's pre-defined serialization
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<IPlayerCompanions> capability, IPlayerCompanions instance, Direction side, INBT nbt) {
            // Use the instance's pre-defined deserialization
            instance.deserializeNBT((CompoundNBT)nbt);
        }
    }
    
    /**
     * Factory for the player companions capability.  Used to register the capability.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.init.InitCapabilities}
     */
    public static class Factory implements Callable<IPlayerCompanions> {
        @Override
        public IPlayerCompanions call() throws Exception {
            return new PlayerCompanions();
        }
    }
}

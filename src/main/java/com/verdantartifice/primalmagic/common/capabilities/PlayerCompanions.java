package com.verdantartifice.primalmagic.common.capabilities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
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
    public CompoundTag serializeNBT() {
        CompoundTag rootTag = new CompoundTag();
        for (CompanionType type : CompanionType.values()) {
            ListTag list = new ListTag();
            List<UUID> companions = this.get(type);
            for (UUID id : companions) {
                CompoundTag companionTag = new CompoundTag();
                companionTag.putUUID("Id", id);
                list.add(companionTag);
            }
            rootTag.put(type.getSerializedName(), list);
        }
        return rootTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.clear();
        for (CompanionType type : CompanionType.values()) {
            if (nbt.contains(type.getSerializedName(), Constants.NBT.TAG_LIST)) {
                ListTag list = nbt.getList(type.getSerializedName(), Constants.NBT.TAG_COMPOUND);
                for (int index = 0; index < list.size(); index++) {
                    CompoundTag companionTag = list.getCompound(index);
                    if (companionTag.hasUUID("Id")) {
                        this.companions.computeIfAbsent(type, (t) -> new LinkedList<>()).add(companionTag.getUUID("Id"));
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
    public void sync(ServerPlayer player) {
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
    public static class Provider implements ICapabilitySerializable<CompoundTag> {
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
        public CompoundTag serializeNBT() {
            return instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
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
        public Tag writeNBT(Capability<IPlayerCompanions> capability, IPlayerCompanions instance, Direction side) {
            // Use the instance's pre-defined serialization
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<IPlayerCompanions> capability, IPlayerCompanions instance, Direction side, Tag nbt) {
            // Use the instance's pre-defined deserialization
            instance.deserializeNBT((CompoundTag)nbt);
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

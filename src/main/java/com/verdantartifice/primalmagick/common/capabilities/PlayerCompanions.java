package com.verdantartifice.primalmagick.common.capabilities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncCompanionsPacket;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Default implementation of the player companion capability.
 * 
 * @author Daedalus4096
 */
public class PlayerCompanions implements IPlayerCompanions {
    private final Map<CompanionType, LinkedList<UUID>> companions = new ConcurrentHashMap<>();
    private long syncTimestamp = 0L;    // Last timestamp at which this capability received a sync from the server

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
        rootTag.putLong("SyncTimestamp", System.currentTimeMillis());
        return rootTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt == null || nbt.getLong("SyncTimestamp") <= this.syncTimestamp) {
            return;
        }
        this.clear();
        for (CompanionType type : CompanionType.values()) {
            if (nbt.contains(type.getSerializedName(), Tag.TAG_LIST)) {
                ListTag list = nbt.getList(type.getSerializedName(), Tag.TAG_COMPOUND);
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
            PacketHandler.sendToPlayer(new SyncCompanionsPacket(player), player);
        }
    }

    /**
     * Capability provider for the player companions capability.  Used to attach capability data to the owner.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagick.common.events.CapabilityEvents}
     */
    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        public static final ResourceLocation NAME = PrimalMagick.resource("capability_companions");
        
        private final IPlayerCompanions instance = new PlayerCompanions();
        private final LazyOptional<IPlayerCompanions> holder = LazyOptional.of(() -> instance);  // Cache a lazy optional of the capability instance

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == PrimalMagickCapabilities.COMPANIONS) {
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

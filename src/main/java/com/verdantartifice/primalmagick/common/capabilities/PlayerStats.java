package com.verdantartifice.primalmagick.common.capabilities;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncStatsPacket;
import com.verdantartifice.primalmagick.common.stats.Stat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Default implementation of the player statistics capability.
 * 
 * @author Daedalus4096
 */
public class PlayerStats implements IPlayerStats {
    private final Map<ResourceLocation, Integer> stats = new ConcurrentHashMap<>(); // Map of stat locations to values
    private final Set<Long> discoveredShrines = ConcurrentHashMap.newKeySet();      // Set of long-encoded block positions of shrine locations
    private long syncTimestamp = 0L;    // Last timestamp at which this capability received a sync from the server

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag rootTag = new CompoundTag();
        
        // Serialize recorded stat values
        ListTag statList = new ListTag();
        for (Map.Entry<ResourceLocation, Integer> entry : stats.entrySet()) {
            if (entry != null && entry.getKey() != null && entry.getValue() != null) {
                CompoundTag tag = new CompoundTag();
                tag.putString("Key", entry.getKey().toString());
                tag.putInt("Value", entry.getValue().intValue());
                statList.add(tag);
            }
        }
        rootTag.put("Stats", statList);
        
        // Serialize discovered shrine locations
        long[] locs = new long[this.discoveredShrines.size()];
        int index = 0;
        for (Long loc : this.discoveredShrines) {
            locs[index++] = loc.longValue();
        }
        rootTag.put("ShrineLocations", new LongArrayTag(locs));
        
        rootTag.putLong("SyncTimestamp", System.currentTimeMillis());
        
        return rootTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt == null || nbt.getLong("SyncTimestamp") <= this.syncTimestamp) {
            return;
        }
        
        this.clear();
        
        // Deserialize recorded stat values
        ListTag statList = nbt.getList("Stats", Tag.TAG_COMPOUND);
        for (int index = 0; index < statList.size(); index++) {
            CompoundTag tag = statList.getCompound(index);
            ResourceLocation loc = new ResourceLocation(tag.getString("Key"));
            Integer value = Integer.valueOf(tag.getInt("Value"));
            this.stats.put(loc, value);
        }
        
        // Deserialize discovered shrine locations
        long[] locs = nbt.getLongArray("ShrineLocations");
        for (long loc : locs) {
            this.discoveredShrines.add(Long.valueOf(loc));
        }
    }

    @Override
    public void clear() {
        this.stats.clear();
        this.discoveredShrines.clear();
    }

    @Override
    public int getValue(Stat stat) {
        if (stat == null) {
            return 0;
        } else {
            return this.stats.getOrDefault(stat.getLocation(), Integer.valueOf(0)).intValue();
        }
    }

    @Override
    public void setValue(Stat stat, int value) {
        if (stat != null) {
            this.stats.put(stat.getLocation(), Integer.valueOf(value));
        }
    }

    @Override
    public boolean isLocationDiscovered(BlockPos pos) {
        if (pos == null) {
            return false;
        } else {
            return this.discoveredShrines.contains(Long.valueOf(pos.asLong()));
        }
    }

    @Override
    public void setLocationDiscovered(BlockPos pos) {
        if (pos != null) {
            this.discoveredShrines.add(Long.valueOf(pos.asLong()));
        }
    }

    @Override
    public void sync(ServerPlayer player) {
        if (player != null) {
            PacketHandler.sendToPlayer(new SyncStatsPacket(player), player);
        }
    }

    /**
     * Capability provider for the player statistics capability.  Used to attach capability data to the owner.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagick.common.events.CapabilityEvents}
     */
    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        public static final ResourceLocation NAME = PrimalMagick.resource("capability_stats");
        
        private final IPlayerStats instance = new PlayerStats();
        private final LazyOptional<IPlayerStats> holder = LazyOptional.of(() -> instance);  // Cache a lazy optional of the capability instance
        
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == PrimalMagickCapabilities.STATS) {
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

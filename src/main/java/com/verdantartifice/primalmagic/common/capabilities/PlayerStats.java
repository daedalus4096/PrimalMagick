package com.verdantartifice.primalmagic.common.capabilities;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.data.SyncStatsPacket;
import com.verdantartifice.primalmagic.common.stats.Stat;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongArrayNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Default implementation of the player statistics capability.
 * 
 * @author Daedalus4096
 */
public class PlayerStats implements IPlayerStats {
    private final Map<ResourceLocation, Integer> stats = new ConcurrentHashMap<>(); // Map of stat locations to values
    private final Set<Long> discoveredShrines = ConcurrentHashMap.newKeySet();      // Set of long-encoded block positions of shrine locations

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT rootTag = new CompoundNBT();
        
        // Serialize recorded stat values
        ListNBT statList = new ListNBT();
        for (Map.Entry<ResourceLocation, Integer> entry : stats.entrySet()) {
            if (entry != null && entry.getKey() != null && entry.getValue() != null) {
                CompoundNBT tag = new CompoundNBT();
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
        rootTag.put("ShrineLocations", new LongArrayNBT(locs));
        
        return rootTag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt == null) {
            return;
        }
        
        this.clear();
        
        // Deserialize recorded stat values
        ListNBT statList = nbt.getList("Stats", Constants.NBT.TAG_COMPOUND);
        for (int index = 0; index < statList.size(); index++) {
            CompoundNBT tag = statList.getCompound(index);
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
            return this.discoveredShrines.contains(Long.valueOf(pos.toLong()));
        }
    }

    @Override
    public void setLocationDiscovered(BlockPos pos) {
        if (pos != null) {
            this.discoveredShrines.add(Long.valueOf(pos.toLong()));
        }
    }

    @Override
    public void sync(ServerPlayerEntity player) {
        if (player != null) {
            PacketHandler.sendToPlayer(new SyncStatsPacket(player), player);
        }
    }

    /**
     * Capability provider for the player statistics capability.  Used to attach capability data to the owner.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.events.CapabilityEvents}
     */
    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        public static final ResourceLocation NAME = new ResourceLocation(PrimalMagic.MODID, "capability_stats");
        
        private final IPlayerStats instance = PrimalMagicCapabilities.STATS.getDefaultInstance();
        private final LazyOptional<IPlayerStats> holder = LazyOptional.of(() -> instance);  // Cache a lazy optional of the capability instance
        
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == PrimalMagicCapabilities.STATS) {
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
     * Storage manager for the player statistics capability.  Used to register the capability.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.init.InitCapabilities}
     */
    public static class Storage implements Capability.IStorage<IPlayerStats> {
        @Override
        public INBT writeNBT(Capability<IPlayerStats> capability, IPlayerStats instance, Direction side) {
            // Use the instance's pre-defined serialization
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<IPlayerStats> capability, IPlayerStats instance, Direction side, INBT nbt) {
            // Use the instance's pre-defined deserialization
            instance.deserializeNBT((CompoundNBT)nbt);
        }
    }
    
    /**
     * Factory for the player statistics capability.  Used to register the capability.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.init.InitCapabilities}
     */
    public static class Factory implements Callable<IPlayerStats> {
        @Override
        public IPlayerStats call() throws Exception {
            return new PlayerStats();
        }
    }
}

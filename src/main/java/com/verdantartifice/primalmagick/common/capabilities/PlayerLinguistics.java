package com.verdantartifice.primalmagick.common.capabilities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncLinguisticsPacket;

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
 * Default implementation of the player linguistics capability.
 * 
 * @author Daedalus4096
 */
public class PlayerLinguistics implements IPlayerLinguistics {
    // Map of language IDs to comprehension scores
    private final Map<ResourceLocation, Integer> comprehension = new ConcurrentHashMap<>();
    
    private long syncTimestamp = 0L;    // Last timestamp at which this capability received a sync from the server

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag rootTag = new CompoundTag();
        
        // Serialize recorded comprehension values
        ListTag comprehensionList = new ListTag();
        for (Map.Entry<ResourceLocation, Integer> entry : this.comprehension.entrySet()) {
            if (entry != null) {
                CompoundTag tag = new CompoundTag();
                tag.putString("Language", entry.getKey().toString());
                tag.putInt("Value", entry.getValue());
                comprehensionList.add(tag);
            }
        }
        rootTag.put("Comprehension", comprehensionList);
        
        rootTag.putLong("SyncTimestamp", System.currentTimeMillis());
        
        return rootTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt == null || nbt.getLong("SyncTimestamp") <= this.syncTimestamp) {
            return;
        }
        
        this.clear();
        
        // Deserialize comprehension values
        ListTag comprehensionList = nbt.getList("Comprehension", Tag.TAG_COMPOUND);
        for (int index = 0; index < comprehensionList.size(); index++) {
            CompoundTag tag = comprehensionList.getCompound(index);
            this.setComprehension(new ResourceLocation(tag.getString("Language")), tag.getInt("Value"));
        }
    }

    @Override
    public void clear() {
        this.comprehension.clear();
    }

    @Override
    public boolean isLanguageKnown(ResourceLocation languageId) {
        return this.comprehension.containsKey(languageId);
    }

    @Override
    public int getComprehension(ResourceLocation languageId) {
        return this.comprehension.getOrDefault(languageId, 0);
    }

    @Override
    public void setComprehension(ResourceLocation languageId, int value) {
        this.comprehension.put(languageId, value);
    }

    @Override
    public void sync(ServerPlayer player) {
        if (player != null) {
            PacketHandler.sendToPlayer(new SyncLinguisticsPacket(player), player);
        }
    }

    /**
     * Capability provider for the player linguistics capability.  Used to attach capability data to the owner.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagick.common.events.CapabilityEvents}
     */
    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        public static final ResourceLocation NAME = PrimalMagick.resource("capability_linguistics");
        
        private final IPlayerLinguistics instance = new PlayerLinguistics();
        private final LazyOptional<IPlayerLinguistics> holder = LazyOptional.of(() -> instance);  // Cache a lazy optional of the capability instance
        
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == PrimalMagickCapabilities.LINGUISTICS) {
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

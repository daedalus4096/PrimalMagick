package com.verdantartifice.primalmagick.common.capabilities;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncAttunementsPacket;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Default implementation of the player attunements capability.
 * 
 * @author Daedalus4096
 */
public class PlayerAttunements implements IPlayerAttunements {
    // Nested map of sources to attunement types to values
    private final Map<Source, Map<AttunementType, Integer>> attunements = new ConcurrentHashMap<>();
    
    // Set of sources currently having their attunements suppressed
    private final Set<Source> suppressions = ConcurrentHashMap.newKeySet();
    
    private long syncTimestamp = 0L;    // Last timestamp at which this capability received a sync from the server

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider registries) {
        CompoundTag rootTag = new CompoundTag();
        
        // Serialize recorded attunement values
        ListTag attunementList = new ListTag();
        for (Map.Entry<Source, Map<AttunementType, Integer>> sourceEntry : this.attunements.entrySet()) {
            if (sourceEntry != null) {
                for (Map.Entry<AttunementType, Integer> typeEntry : sourceEntry.getValue().entrySet()) {
                    if (typeEntry != null && sourceEntry.getKey() != null && typeEntry.getKey() != null && typeEntry.getValue() != null) {
                        CompoundTag tag = new CompoundTag();
                        tag.putString("Source", sourceEntry.getKey().getId().toString());
                        tag.putString("Type", typeEntry.getKey().name());
                        tag.putInt("Value", typeEntry.getValue().intValue());
                        attunementList.add(tag);
                    }
                }
            }
        }
        rootTag.put("Attunements", attunementList);
        
        // Serialize recorded suppression values
        ListTag suppressionList = new ListTag();
        for (Source source : this.suppressions) {
            if (source != null) {
                suppressionList.add(StringTag.valueOf(source.getId().toString()));
            }
        }
        rootTag.put("Suppressions", suppressionList);
        
        rootTag.putLong("SyncTimestamp", System.currentTimeMillis());
        
        return rootTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt) {
        if (nbt == null || nbt.getLong("SyncTimestamp") <= this.syncTimestamp) {
            return;
        }

        this.syncTimestamp = nbt.getLong("syncTimestamp");
        this.clear();
        
        // Deserialize attunement values
        ListTag attunementList = nbt.getList("Attunements", Tag.TAG_COMPOUND);
        for (int index = 0; index < attunementList.size(); index++) {
            CompoundTag tag = attunementList.getCompound(index);
            Source source = Sources.get(ResourceLocation.parse(tag.getString("Source")));
            AttunementType type = null;
            try {
                type = AttunementType.valueOf(tag.getString("Type"));
            } catch (Exception e) {}
            int value = tag.getInt("Value");
            this.setValue(source, type, value);
        }
        
        // Deserialize suppression values
        ListTag suppressionList = nbt.getList("Suppressions", Tag.TAG_STRING);
        for (int index = 0; index < suppressionList.size(); index++) {
            Source source = Sources.get(ResourceLocation.parse(suppressionList.getString(index)));
            this.setSuppressed(source, true);
        }
    }

    @Override
    public void clear() {
        this.attunements.clear();
        this.suppressions.clear();
    }

    @Override
    public int getValue(Source source, AttunementType type) {
        return this.attunements.getOrDefault(source, Collections.emptyMap()).getOrDefault(type, Integer.valueOf(0)).intValue();
    }

    @Override
    public void setValue(Source source, AttunementType type, int value) {
        // Don't allow null keys or values in the data
        if (source != null && type != null) {
            // Get the map of types to values for the source, creating an empty one if it doesn't exist
            Map<AttunementType, Integer> typeMap = this.attunements.computeIfAbsent(source, k -> new ConcurrentHashMap<>());
            
            // Determine if the value to be set must be capped
            int toSet = type.isCapped() ? Mth.clamp(value, 0, type.getMaximum()) : Math.max(0, value);
            
            // Add the given value to the type map
            typeMap.put(type, Integer.valueOf(toSet));
        }
    }

    @Override
    public boolean isSuppressed(Source source) {
        return this.suppressions.contains(source);
    }

    @Override
    public void setSuppressed(Source source, boolean value) {
        // Don't allow null keys in the set
        if (source != null) {
            boolean present = this.isSuppressed(source);
            if (!present && value) {
                this.suppressions.add(source);
            } else if (present && !value) {
                this.suppressions.remove(source);
            }
        }
    }

    @Override
    public void sync(ServerPlayer player) {
        if (player != null) {
            PacketHandler.sendToPlayer(new SyncAttunementsPacket(player), player);
        }
    }
}

package com.verdantartifice.primalmagick.common.capabilities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
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
    
    // Map of language IDs to vocabulary scores
    private final Map<ResourceLocation, Integer> vocabulary = new ConcurrentHashMap<>();
    
    // Table of book definition IDs to language IDs to study counts
    private final Table<ResourceLocation, ResourceLocation, Integer> studyCounts = HashBasedTable.create();
    
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
        
        // Serialize recorded vocabulary scores
        ListTag vocabularyList = new ListTag();
        for (Map.Entry<ResourceLocation, Integer> entry : this.vocabulary.entrySet()) {
            if (entry != null) {
                CompoundTag tag = new CompoundTag();
                tag.putString("Language", entry.getKey().toString());
                tag.putInt("Value", entry.getValue());
                vocabularyList.add(tag);
            }
        }
        rootTag.put("Vocabulary", vocabularyList);
        
        // Serialize recorded study counts
        ListTag studyCountList = new ListTag();
        for (Table.Cell<ResourceLocation, ResourceLocation, Integer> cell : this.studyCounts.cellSet()) {
            if (cell != null) {
                CompoundTag tag = new CompoundTag();
                tag.putString("Book", cell.getRowKey().toString());
                tag.putString("Language", cell.getColumnKey().toString());
                tag.putInt("Value", cell.getValue());
                studyCountList.add(tag);
            }
        }
        rootTag.put("StudyCounts", studyCountList);
        
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
        
        // Deserialize vocabulary values
        ListTag vocabularyList = nbt.getList("Vocabulary", Tag.TAG_COMPOUND);
        for (int index = 0; index < vocabularyList.size(); index++) {
            CompoundTag tag = vocabularyList.getCompound(index);
            this.setVocabulary(new ResourceLocation(tag.getString("Language")), tag.getInt("Value"));
        }
        
        // Deserialize study count values
        ListTag studyCountList = nbt.getList("StudyCounts", Tag.TAG_COMPOUND);
        for (int index = 0; index < studyCountList.size(); index++) {
            CompoundTag tag = studyCountList.getCompound(index);
            this.setTimesStudied(new ResourceLocation(tag.getString("Book")), new ResourceLocation(tag.getString("Language")), tag.getInt("Value"));
        }
    }

    @Override
    public void clear() {
        this.comprehension.clear();
        this.vocabulary.clear();
        this.studyCounts.clear();
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
    public int getVocabulary(ResourceLocation languageId) {
        return this.vocabulary.getOrDefault(languageId, 0);
    }

    @Override
    public void setVocabulary(ResourceLocation languageId, int value) {
        this.vocabulary.put(languageId, value);
    }

    @Override
    public int getTimesStudied(ResourceLocation bookDefinitionId, ResourceLocation languageId) {
        return this.studyCounts.contains(bookDefinitionId, languageId) ? this.studyCounts.get(bookDefinitionId, languageId) : 0;
    }

    @Override
    public void setTimesStudied(ResourceLocation bookDefinitionId, ResourceLocation languageId, int value) {
        this.studyCounts.put(bookDefinitionId, languageId, value);
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

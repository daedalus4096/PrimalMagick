package com.verdantartifice.primalmagick.common.capabilities;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.joml.Vector2i;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.ScribeTableMode;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncLinguisticsPacket;

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
 * Default implementation of the player linguistics capability.
 * 
 * @author Daedalus4096
 */
public class PlayerLinguistics implements IPlayerLinguistics {
    // Map of language IDs to comprehension scores
    private final Map<ResourceLocation, Integer> comprehension = new ConcurrentHashMap<>();
    
    // Map of language IDs to vocabulary scores
    private final Map<ResourceLocation, Integer> vocabulary = new ConcurrentHashMap<>();
    
    // Map of language IDs to sets of book definition IDs, marking what books the player has opened in what languages
    private final Map<ResourceLocation, Set<ResourceLocation>> booksRead = new ConcurrentHashMap<>();
    
    // Table of book definition IDs to language IDs to study counts
    private final Table<ResourceLocation, ResourceLocation, Integer> studyCounts = HashBasedTable.create();
    
    // Map of grid definition IDs to sets of unlocked node coordinates
    private final Map<ResourceLocation, Set<Vector2i>> unlocks = new ConcurrentHashMap<>();
    
    // Map of grid definition IDs to last modified times
    private final Map<ResourceLocation, Long> gridModificationTimes = new ConcurrentHashMap<>();
    
    // Current scribe table mode
    private ScribeTableMode scribeTableMode = ScribeTableMode.STUDY_VOCABULARY;
    
    private long syncTimestamp = 0L;    // Last timestamp at which this capability received a sync from the server

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider registries) {
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
        
        // Serialize books read
        ListTag booksReadLanguageList = new ListTag();
        for (Map.Entry<ResourceLocation, Set<ResourceLocation>> entry : this.booksRead.entrySet()) {
            if (entry != null) {
                CompoundTag langTag = new CompoundTag();
                langTag.putString("Language", entry.getKey().toString());
                ListTag bookList = new ListTag();
                for (ResourceLocation bookId : entry.getValue()) {
                    if (bookId != null) {
                        bookList.add(StringTag.valueOf(bookId.toString()));
                    }
                }
                langTag.put("Books", bookList);
                booksReadLanguageList.add(langTag);
            }
        }
        rootTag.put("BooksRead", booksReadLanguageList);
        
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
        
        // Serialize unlocked node coordinates
        ListTag unlockGridList = new ListTag();
        for (Map.Entry<ResourceLocation, Set<Vector2i>> gridEntry : this.unlocks.entrySet()) {
            if (gridEntry != null) {
                CompoundTag gridTag = new CompoundTag();
                gridTag.putString("GridDef", gridEntry.getKey().toString());
                ListTag unlockCoordsList = new ListTag();
                for (Vector2i coords : gridEntry.getValue()) {
                    if (coords != null) {
                        CompoundTag coordsTag = new CompoundTag();
                        coordsTag.putInt("X", coords.x());
                        coordsTag.putInt("Y", coords.y());
                        unlockCoordsList.add(coordsTag);
                    }
                }
                gridTag.put("Coords", unlockCoordsList);
                unlockGridList.add(gridTag);
            }
        }
        rootTag.put("Unlocks", unlockGridList);
        
        // Serialize grid last modification times
        ListTag modifiedList = new ListTag();
        for (Map.Entry<ResourceLocation, Long> entry : this.gridModificationTimes.entrySet()) {
            if (entry != null) {
                CompoundTag tag = new CompoundTag();
                tag.putString("GridDef", entry.getKey().toString());
                tag.putLong("LastModified", entry.getValue());
                modifiedList.add(tag);
            }
        }
        rootTag.put("GridModifiedTimes", modifiedList);
        
        rootTag.putString("ScribeTableMode", this.scribeTableMode.getSerializedName());
        rootTag.putLong("SyncTimestamp", System.currentTimeMillis());
        
        return rootTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt) {
        if (nbt == null || nbt.getLong("SyncTimestamp") <= this.syncTimestamp) {
            return;
        }
        
        this.clear();
        
        // Deserialize comprehension values
        ListTag comprehensionList = nbt.getList("Comprehension", Tag.TAG_COMPOUND);
        for (int index = 0; index < comprehensionList.size(); index++) {
            CompoundTag tag = comprehensionList.getCompound(index);
            this.setComprehension(ResourceLocation.parse(tag.getString("Language")), tag.getInt("Value"));
        }
        
        // Deserialize vocabulary values
        ListTag vocabularyList = nbt.getList("Vocabulary", Tag.TAG_COMPOUND);
        for (int index = 0; index < vocabularyList.size(); index++) {
            CompoundTag tag = vocabularyList.getCompound(index);
            this.setVocabulary(ResourceLocation.parse(tag.getString("Language")), tag.getInt("Value"));
        }
        
        // Deserialize books read values
        ListTag booksReadList = nbt.getList("BooksRead", Tag.TAG_COMPOUND);
        for (int langIndex = 0; langIndex < booksReadList.size(); langIndex++) {
            CompoundTag langTag = booksReadList.getCompound(langIndex);
            ResourceLocation langId = ResourceLocation.parse(langTag.getString("Language"));
            ListTag booksList = langTag.getList("Books", Tag.TAG_STRING);
            for (int bookIndex = 0; bookIndex < booksList.size(); bookIndex++) {
                ResourceLocation bookId = ResourceLocation.parse(booksList.getString(bookIndex));
                this.markRead(bookId, langId);
            }
        }
        
        // Deserialize study count values
        ListTag studyCountList = nbt.getList("StudyCounts", Tag.TAG_COMPOUND);
        for (int index = 0; index < studyCountList.size(); index++) {
            CompoundTag tag = studyCountList.getCompound(index);
            this.setTimesStudied(ResourceLocation.parse(tag.getString("Book")), ResourceLocation.parse(tag.getString("Language")), tag.getInt("Value"));
        }
        
        // Deserialize unlocked node coordinates
        ListTag unlockGridList = nbt.getList("Unlocks", Tag.TAG_COMPOUND);
        for (int gridIndex = 0; gridIndex < unlockGridList.size(); gridIndex++) {
            CompoundTag gridTag = unlockGridList.getCompound(gridIndex);
            ResourceLocation gridId = ResourceLocation.parse(gridTag.getString("GridDef"));
            ListTag coordsList = gridTag.getList("Coords", Tag.TAG_COMPOUND);
            for (int coordsIndex = 0; coordsIndex < coordsList.size(); coordsIndex++) {
                CompoundTag coordsTag = coordsList.getCompound(coordsIndex);
                this.unlockNode(gridId, new Vector2i(coordsTag.getInt("X"), coordsTag.getInt("Y")));
            }
        }
        
        // Deserialize grid last modification times
        ListTag modifiedList = nbt.getList("GridModifiedTimes", Tag.TAG_COMPOUND);
        for (int index = 0; index < modifiedList.size(); index++) {
            CompoundTag tag = modifiedList.getCompound(index);
            this.setLastModified(ResourceLocation.parse(tag.getString("GridDef")), tag.getLong("LastModified"));
        }
        
        ScribeTableMode mode = ScribeTableMode.fromName(nbt.getString("ScribeTableMode"));
        this.scribeTableMode = mode == null ? ScribeTableMode.STUDY_VOCABULARY : mode;
    }

    @Override
    public void clear() {
        this.comprehension.clear();
        this.vocabulary.clear();
        this.booksRead.clear();
        this.studyCounts.clear();
        this.unlocks.clear();
        this.scribeTableMode = ScribeTableMode.STUDY_VOCABULARY;
        this.gridModificationTimes.clear();
    }

    @Override
    public boolean isLanguageKnown(ResourceLocation languageId) {
        return (this.booksRead.getOrDefault(languageId, Collections.emptySet()).size() > 0) ||
                (this.getVocabulary(languageId) > 0) || (this.getComprehension(languageId) > 0);
    }

    @Override
    public boolean markRead(ResourceLocation bookDefinitionId, ResourceLocation languageId) {
        return this.booksRead.computeIfAbsent(languageId, key -> new HashSet<>()).add(bookDefinitionId);
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
        this.studyCounts.put(bookDefinitionId, languageId, Mth.clamp(value, 0, MAX_STUDY_COUNT));
    }

    @Override
    public ScribeTableMode getScribeTableMode() {
        return this.scribeTableMode;
    }

    @Override
    public void setScribeTableMode(ScribeTableMode mode) {
        this.scribeTableMode = Preconditions.checkNotNull(mode);
    }

    @Override
    public Set<Vector2i> getUnlockedNodes(ResourceLocation gridDefinitionId) {
        return Collections.unmodifiableSet(this.unlocks.getOrDefault(gridDefinitionId, Collections.emptySet()));
    }

    @Override
    public void clearUnlockedNodes(ResourceLocation gridDefinitionId) {
        this.setLastModified(gridDefinitionId, System.currentTimeMillis());
        this.unlocks.remove(gridDefinitionId);
    }

    @Override
    public boolean unlockNode(ResourceLocation gridDefinitionId, Vector2i nodePos) {
        this.setLastModified(gridDefinitionId, System.currentTimeMillis());
        return this.unlocks.computeIfAbsent(gridDefinitionId, k -> new HashSet<>()).add(nodePos);
    }

    @Override
    public long getGridLastModified(ResourceLocation gridDefinitionId) {
        return this.gridModificationTimes.getOrDefault(gridDefinitionId, 0L);
    }

    private void setLastModified(ResourceLocation gridDefinitionId, long lastModified) {
        this.gridModificationTimes.put(gridDefinitionId, lastModified);
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
        public static final ResourceLocation NAME = ResourceUtils.loc("capability_linguistics");
        
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

        @SuppressWarnings("deprecation")
        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider registries) {
            return instance.serializeNBT(registries);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt) {
            instance.deserializeNBT(registries, nbt);
        }
    }
}

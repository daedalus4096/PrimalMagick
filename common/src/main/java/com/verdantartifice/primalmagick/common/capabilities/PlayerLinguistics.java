package com.verdantartifice.primalmagick.common.capabilities;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.books.ScribeTableMode;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncLinguisticsPacket;
import com.verdantartifice.primalmagick.common.util.CodecUtils;
import com.verdantartifice.primalmagick.common.util.IdentifiedScoreEntry;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the player linguistics capability.
 * 
 * @author Daedalus4096
 */
public class PlayerLinguistics extends AbstractCapability<PlayerLinguistics> implements IPlayerLinguistics {
    public static final Codec<PlayerLinguistics> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            IdentifiedScoreEntry.CODEC.listOf().<Map<Identifier, Integer>>xmap(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(IdentifiedScoreEntry::id, IdentifiedScoreEntry::score)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new IdentifiedScoreEntry(e.getKey(), e.getValue())).toList()
            ).fieldOf("comprehension").forGetter(l -> l.comprehension),
            IdentifiedScoreEntry.CODEC.listOf().<Map<Identifier, Integer>>xmap(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(IdentifiedScoreEntry::id, IdentifiedScoreEntry::score)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new IdentifiedScoreEntry(e.getKey(), e.getValue())).toList()
            ).fieldOf("vocabulary").forGetter(l -> l.vocabulary),
            BooksReadEntry.CODEC.listOf().<Map<Identifier, Set<Identifier>>>xmap(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(BooksReadEntry::languageId, BooksReadEntry::bookIds)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new BooksReadEntry(e.getKey(), e.getValue())).toList()
            ).fieldOf("booksRead").forGetter(l -> l.booksRead),
            StudyCountEntry.CODEC.listOf().<Map<Identifier, Map<Identifier, Integer>>>xmap(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(StudyCountEntry::bookId, StudyCountEntry::languageScores)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new StudyCountEntry(e.getKey(), e.getValue())).toList()
            ).fieldOf("studyCounts").forGetter(l -> l.studyCounts),
            GridUnlockEntry.CODEC.listOf().<Map<Identifier, Set<Vector2i>>>xmap(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(GridUnlockEntry::gridId, GridUnlockEntry::positions)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new GridUnlockEntry(e.getKey(), e.getValue())).toList()
            ).fieldOf("unlocks").forGetter(l -> l.unlocks),
            GridModificationTimeEntry.CODEC.listOf().<Map<Identifier, Long>>xmap(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(GridModificationTimeEntry::gridId, GridModificationTimeEntry::modificationTime)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new GridModificationTimeEntry(e.getKey(), e.getValue())).toList()
            ).fieldOf("gridModificationTimes").forGetter(l -> l.gridModificationTimes),
            ScribeTableMode.CODEC.fieldOf("scribeTableMode").forGetter(l -> l.scribeTableMode),
            Codec.LONG.fieldOf("syncTimestamp").forGetter(AbstractCapability::getSyncTimestamp)
    ).apply(instance, PlayerLinguistics::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerLinguistics> STREAM_CODEC = StreamCodec.composite(
            IdentifiedScoreEntry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(IdentifiedScoreEntry::id, IdentifiedScoreEntry::score)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new IdentifiedScoreEntry(e.getKey(), e.getValue())).toList()
            ), l -> l.comprehension,
            IdentifiedScoreEntry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(IdentifiedScoreEntry::id, IdentifiedScoreEntry::score)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new IdentifiedScoreEntry(e.getKey(), e.getValue())).toList()
            ), l -> l.vocabulary,
            BooksReadEntry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(BooksReadEntry::languageId, BooksReadEntry::bookIds)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new BooksReadEntry(e.getKey(), e.getValue())).toList()
            ), l -> l.booksRead,
            StudyCountEntry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(StudyCountEntry::bookId, StudyCountEntry::languageScores)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new StudyCountEntry(e.getKey(), e.getValue())).toList()
            ), l -> l.studyCounts,
            GridUnlockEntry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(GridUnlockEntry::gridId, GridUnlockEntry::positions)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new GridUnlockEntry(e.getKey(), e.getValue())).toList()
            ), l -> l.unlocks,
            GridModificationTimeEntry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(GridModificationTimeEntry::gridId, GridModificationTimeEntry::modificationTime)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new GridModificationTimeEntry(e.getKey(), e.getValue())).toList()
            ), l -> l.gridModificationTimes,
            ScribeTableMode.STREAM_CODEC, l -> l.scribeTableMode,
            ByteBufCodecs.VAR_LONG, AbstractCapability::getSyncTimestamp,
            PlayerLinguistics::new);

    // Map of language IDs to comprehension scores
    private final Map<Identifier, Integer> comprehension = new ConcurrentHashMap<>();
    
    // Map of language IDs to vocabulary scores
    private final Map<Identifier, Integer> vocabulary = new ConcurrentHashMap<>();
    
    // Map of language IDs to sets of book definition IDs, marking what books the player has opened in what languages
    private final Map<Identifier, Set<Identifier>> booksRead = new ConcurrentHashMap<>();
    
    // Table of book definition IDs to language IDs to study counts
    private final Map<Identifier, Map<Identifier, Integer>> studyCounts = new ConcurrentHashMap<>();
    
    // Map of grid definition IDs to sets of unlocked node coordinates
    private final Map<Identifier, Set<Vector2i>> unlocks = new ConcurrentHashMap<>();
    
    // Map of grid definition IDs to last modified times
    private final Map<Identifier, Long> gridModificationTimes = new ConcurrentHashMap<>();
    
    // Current scribe table mode
    private ScribeTableMode scribeTableMode;
    
    public PlayerLinguistics() {
        this(Map.of(), Map.of(), Map.of(), Map.of(), Map.of(), Map.of(), ScribeTableMode.STUDY_VOCABULARY, 0L);
    }

    protected PlayerLinguistics(Map<Identifier, Integer> comprehension, Map<Identifier, Integer> vocabulary,
                                Map<Identifier, Set<Identifier>> booksRead, Map<Identifier, Map<Identifier, Integer>> studyCounts,
                                Map<Identifier, Set<Vector2i>> unlocks, Map<Identifier, Long> gridModificationTimes,
                                ScribeTableMode scribeTableMode, long syncTimestamp) {
        super(syncTimestamp);
        this.comprehension.putAll(comprehension);
        this.vocabulary.putAll(vocabulary);
        this.booksRead.putAll(booksRead);
        this.studyCounts.putAll(studyCounts);
        this.unlocks.putAll(unlocks);
        this.gridModificationTimes.putAll(gridModificationTimes);
        this.scribeTableMode = scribeTableMode;
    }

    @Override
    public Codec<PlayerLinguistics> codec() {
        return CODEC;
    }

    @Override
    protected void copyFromInner(@NotNull PlayerLinguistics other) {
        this.clear();
        this.comprehension.putAll(other.comprehension);
        this.vocabulary.putAll(other.vocabulary);
        this.booksRead.putAll(other.booksRead);
        this.studyCounts.putAll(other.studyCounts);
        this.unlocks.putAll(other.unlocks);
        this.gridModificationTimes.putAll(other.gridModificationTimes);
        this.scribeTableMode = other.scribeTableMode;
    }

    @Override
    public void clear() {
        this.comprehension.clear();
        this.vocabulary.clear();
        this.booksRead.clear();
        this.studyCounts.clear();
        this.unlocks.clear();
        this.gridModificationTimes.clear();
        this.scribeTableMode = ScribeTableMode.STUDY_VOCABULARY;
    }

    @Override
    public boolean isLanguageKnown(Identifier languageId) {
        return (!this.booksRead.getOrDefault(languageId, Collections.emptySet()).isEmpty()) ||
                (this.getVocabulary(languageId) > 0) || (this.getComprehension(languageId) > 0);
    }

    @Override
    public boolean markRead(Identifier bookDefinitionId, Identifier languageId) {
        return this.booksRead.computeIfAbsent(languageId, key -> new HashSet<>()).add(bookDefinitionId);
    }

    @Override
    public int getComprehension(Identifier languageId) {
        return this.comprehension.getOrDefault(languageId, 0);
    }

    @Override
    public void setComprehension(Identifier languageId, int value) {
        this.comprehension.put(languageId, value);
    }

    @Override
    public int getVocabulary(Identifier languageId) {
        return this.vocabulary.getOrDefault(languageId, 0);
    }

    @Override
    public void setVocabulary(Identifier languageId, int value) {
        this.vocabulary.put(languageId, value);
    }

    @Override
    public int getTimesStudied(Identifier bookDefinitionId, Identifier languageId) {
        return this.studyCounts.getOrDefault(bookDefinitionId, Map.of()).getOrDefault(languageId, 0);
    }

    @Override
    public void setTimesStudied(Identifier bookDefinitionId, Identifier languageId, int value) {
        this.studyCounts.computeIfAbsent(bookDefinitionId, l -> new ConcurrentHashMap<>()).put(languageId, Mth.clamp(value, 0, MAX_STUDY_COUNT));
    }

    @Override
    @NotNull
    public ScribeTableMode getScribeTableMode() {
        return this.scribeTableMode;
    }

    @Override
    public void setScribeTableMode(@NotNull ScribeTableMode mode) {
        this.scribeTableMode = Preconditions.checkNotNull(mode);
    }

    @Override
    public Set<Vector2i> getUnlockedNodes(Identifier gridDefinitionId) {
        return Collections.unmodifiableSet(this.unlocks.getOrDefault(gridDefinitionId, Collections.emptySet()));
    }

    @Override
    public void clearUnlockedNodes(Identifier gridDefinitionId) {
        this.setLastModified(gridDefinitionId, System.currentTimeMillis());
        this.unlocks.remove(gridDefinitionId);
    }

    @Override
    public boolean unlockNode(Identifier gridDefinitionId, Vector2i nodePos) {
        this.setLastModified(gridDefinitionId, System.currentTimeMillis());
        return this.unlocks.computeIfAbsent(gridDefinitionId, k -> new HashSet<>()).add(nodePos);
    }

    @Override
    public long getGridLastModified(Identifier gridDefinitionId) {
        return this.gridModificationTimes.getOrDefault(gridDefinitionId, 0L);
    }

    private void setLastModified(Identifier gridDefinitionId, long lastModified) {
        this.gridModificationTimes.put(gridDefinitionId, lastModified);
    }

    @Override
    public void sync(@NotNull ServerPlayer player) {
        this.sync(player, SyncLinguisticsPacket::new);
    }

    protected record BooksReadEntry(Identifier languageId, Set<Identifier> bookIds) {
        public static final Codec<BooksReadEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("languageId").forGetter(BooksReadEntry::languageId),
                Identifier.CODEC.listOf().<Set<Identifier>>xmap(HashSet::new, ImmutableList::copyOf).fieldOf("bookIds").forGetter(BooksReadEntry::bookIds)
        ).apply(instance, BooksReadEntry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, BooksReadEntry> STREAM_CODEC = StreamCodec.composite(
                Identifier.STREAM_CODEC, BooksReadEntry::languageId,
                Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()).map(HashSet::new, ImmutableList::copyOf), BooksReadEntry::bookIds,
                BooksReadEntry::new);
    }

    protected record StudyCountEntry(Identifier bookId, Map<Identifier, Integer> languageScores) {
        public static final Codec<StudyCountEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("bookId").forGetter(StudyCountEntry::bookId),
                IdentifiedScoreEntry.CODEC.listOf().<Map<Identifier, Integer>>xmap(
                        iseList -> iseList.stream().collect(ImmutableMap.toImmutableMap(IdentifiedScoreEntry::id, IdentifiedScoreEntry::score)),
                        iseMap -> iseMap.entrySet().stream().map(e -> new IdentifiedScoreEntry(e.getKey(), e.getValue())).toList()
                ).fieldOf("languageScores").forGetter(e -> e.languageScores)
        ).apply(instance, StudyCountEntry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, StudyCountEntry> STREAM_CODEC = StreamCodec.composite(
                Identifier.STREAM_CODEC, StudyCountEntry::bookId,
                IdentifiedScoreEntry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
                        iseList -> iseList.stream().collect(ImmutableMap.toImmutableMap(IdentifiedScoreEntry::id, IdentifiedScoreEntry::score)),
                        iseMap -> iseMap.entrySet().stream().map(e -> new IdentifiedScoreEntry(e.getKey(), e.getValue())).toList()
                ), StudyCountEntry::languageScores,
                StudyCountEntry::new);
    }

    protected record GridUnlockEntry(Identifier gridId, Set<Vector2i> positions) {
        public static final Codec<GridUnlockEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("gridId").forGetter(GridUnlockEntry::gridId),
                CodecUtils.VECTOR2I.listOf().<Set<Vector2i>>xmap(HashSet::new, ImmutableList::copyOf).fieldOf("positions").forGetter(GridUnlockEntry::positions)
        ).apply(instance, GridUnlockEntry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, GridUnlockEntry> STREAM_CODEC = StreamCodec.composite(
                Identifier.STREAM_CODEC, GridUnlockEntry::gridId,
                StreamCodecUtils.VECTOR2I.apply(ByteBufCodecs.list()).map(HashSet::new, ImmutableList::copyOf), GridUnlockEntry::positions,
                GridUnlockEntry::new);
    }

    protected record GridModificationTimeEntry(Identifier gridId, long modificationTime) {
        public static final Codec<GridModificationTimeEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("gridId").forGetter(GridModificationTimeEntry::gridId),
                Codec.LONG.fieldOf("modificationTime").forGetter(GridModificationTimeEntry::modificationTime)
        ).apply(instance, GridModificationTimeEntry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, GridModificationTimeEntry> STREAM_CODEC = StreamCodec.composite(
                Identifier.STREAM_CODEC, GridModificationTimeEntry::gridId,
                ByteBufCodecs.VAR_LONG, GridModificationTimeEntry::modificationTime,
                GridModificationTimeEntry::new);
    }
}

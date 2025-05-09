package com.verdantartifice.primalmagick.common.capabilities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncKnowledgePacket;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.topics.AbstractResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.MainIndexResearchTopic;
import com.verdantartifice.primalmagick.common.theorycrafting.Project;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Default implementation of the player knowledge capability.
 * 
 * @author Daedalus4096
 */
public class PlayerKnowledge implements IPlayerKnowledge {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final int LEGACY_VERSION = 0;
    public static final int UNREAD_FLAG_VERSION = 1;
    public static final int CURRENT_SCHEMA_VERSION = UNREAD_FLAG_VERSION;

    public static final Codec<PlayerKnowledge> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                AbstractResearchKey.dispatchCodec().listOf().<Set<AbstractResearchKey<?>>>xmap(ImmutableSet::copyOf, ImmutableList::copyOf).fieldOf("research").forGetter(k -> k.research),
                StageEntry.CODEC.listOf().<Map<AbstractResearchKey<?>, Integer>>xmap(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(StageEntry::key, StageEntry::stage)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new StageEntry(e.getKey(), e.getValue())).toList()
                ).fieldOf("stages").forGetter(k -> k.stages),
                FlagsEntry.CODEC.listOf().<Map<AbstractResearchKey<?>, Set<ResearchFlag>>>xmap(
                    entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(FlagsEntry::key, FlagsEntry::flagSet)),
                    entryMap -> entryMap.entrySet().stream().map(e -> new FlagsEntry(e.getKey(), e.getValue())).toList()
                ).fieldOf("flags").forGetter(k -> k.flags),
                Codec.simpleMap(KnowledgeType.CODEC, Codec.INT, StringRepresentable.keys(KnowledgeType.values())).fieldOf("knowledge").forGetter(k -> k.knowledge),
                AbstractResearchTopic.dispatchCodec().listOf().fieldOf("topicHistory").forGetter(k -> k.topicHistory),
                Project.codec().optionalFieldOf("project").forGetter(k -> k.project),
                AbstractResearchTopic.dispatchCodec().optionalFieldOf("topic").forGetter(k -> k.topic),
                Codec.INT.optionalFieldOf("schemaVersion", LEGACY_VERSION).forGetter(k -> k.schemaVersion),
                Codec.LONG.optionalFieldOf("syncTimestamp", 0L).forGetter(k -> k.syncTimestamp)
            ).apply(instance, PlayerKnowledge::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerKnowledge> STREAM_CODEC = StreamCodecUtils.composite(
            AbstractResearchKey.dispatchStreamCodec().apply(ByteBufCodecs.list()).<Set<AbstractResearchKey<?>>>map(ImmutableSet::copyOf, ImmutableList::copyOf), k -> k.research,
            StageEntry.STREAM_CODEC.apply(ByteBufCodecs.list()).<Map<AbstractResearchKey<?>, Integer>>map(
                entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(StageEntry::key, StageEntry::stage)),
                entryMap -> entryMap.entrySet().stream().map(e -> new StageEntry(e.getKey(), e.getValue())).toList()
            ), k -> k.stages,
            FlagsEntry.STREAM_CODEC.apply(ByteBufCodecs.list()).<Map<AbstractResearchKey<?>, Set<ResearchFlag>>>map(
                entryList -> entryList.stream().collect(ImmutableMap.toImmutableMap(FlagsEntry::key, FlagsEntry::flagSet)),
                entryMap -> entryMap.entrySet().stream().map(e -> new FlagsEntry(e.getKey(), e.getValue())).toList()
            ), k -> k.flags,
            ByteBufCodecs.map(Object2IntOpenHashMap::new, KnowledgeType.STREAM_CODEC, ByteBufCodecs.VAR_INT), k -> k.knowledge,
            AbstractResearchTopic.dispatchStreamCodec().apply(ByteBufCodecs.list()), k -> k.topicHistory,
            ByteBufCodecs.optional(Project.streamCodec()), k -> k.project,
            ByteBufCodecs.optional(AbstractResearchTopic.dispatchStreamCodec()), k -> k.topic,
            ByteBufCodecs.VAR_INT, k -> k.schemaVersion,
            ByteBufCodecs.VAR_LONG, k -> k.syncTimestamp,
            PlayerKnowledge::new);
    
    private final Set<AbstractResearchKey<?>> research = ConcurrentHashMap.newKeySet();                 // Set of known research
    private final Map<AbstractResearchKey<?>, Integer> stages = new ConcurrentHashMap<>();              // Map of research keys to current stage numbers
    private final Map<AbstractResearchKey<?>, Set<ResearchFlag>> flags = new ConcurrentHashMap<>();     // Map of research keys to attached flag sets
    private final Map<KnowledgeType, Integer> knowledge = new ConcurrentHashMap<>();                    // Map of knowledge types to accrued points
    private final LinkedList<AbstractResearchTopic<?>> topicHistory = new LinkedList<>();               // Grimoire research topic history
    
    private Optional<Project> project;                  // Currently active research project
    private Optional<AbstractResearchTopic<?>> topic;   // Last active grimoire research topic
    private int schemaVersion;                          // Version of this object's schema, used to know when certain legacy updates should be done
    private long syncTimestamp;                         // Last timestamp at which this capability received a sync from the server

    public PlayerKnowledge() {
        this(Set.of(), Map.of(), Map.of(), Map.of(), List.of(), Optional.empty(), Optional.empty(), CURRENT_SCHEMA_VERSION, 0L);
    }

    protected PlayerKnowledge(Set<AbstractResearchKey<?>> research, Map<AbstractResearchKey<?>, Integer> stages,
                              Map<AbstractResearchKey<?>, Set<ResearchFlag>> flags, Map<KnowledgeType, Integer> knowledge,
                              List<AbstractResearchTopic<?>> topicHistory, Optional<Project> project,
                              Optional<AbstractResearchTopic<?>> topic, int schemaVersion, long syncTimestamp) {
        this.research.addAll(research);
        this.stages.putAll(stages);
        this.flags.putAll(flags);
        this.knowledge.putAll(knowledge);
        this.topicHistory.addAll(topicHistory);
        this.project = project;
        this.topic = topic;
        this.schemaVersion = schemaVersion;
        this.syncTimestamp = syncTimestamp;
    }

    @VisibleForTesting
    public int getSchemaVersion() {
        return this.schemaVersion;
    }

    @Nullable
    @Override
    public Tag serializeNBT(@NotNull HolderLookup.Provider registryAccess) {
        RegistryOps<Tag> registryOps = registryAccess.createSerializationContext(NbtOps.INSTANCE);
        this.syncTimestamp = System.currentTimeMillis();
        return CODEC.encodeStart(registryOps, this)
                .resultOrPartial(msg -> LOGGER.error("Failed to serialize player knowledge: {}", msg))
                .orElse(null);
    }

    @Override
    public synchronized void deserializeNBT(@NotNull HolderLookup.Provider registryAccess, @NotNull Tag nbt) {
        RegistryOps<Tag> registryOps = registryAccess.createSerializationContext(NbtOps.INSTANCE);
        Mutable<PlayerKnowledge> parsedKnowledge = new MutableObject<>(null);
        CODEC.parse(registryOps, nbt)
                .ifSuccess(parsedKnowledge::setValue)
                .ifError(err -> {
                    // If the tag could not be parsed via codec, it might be in the legacy format
                    LOGGER.warn("Failed to deserialize player knowledge using codec, trying fallback");
                    if (nbt instanceof CompoundTag compoundTag) {
                        PlayerKnowledge legacyKnowledge = new PlayerKnowledge();
                        legacyKnowledge.deserializeLegacyNBT(registryAccess, compoundTag);
                        parsedKnowledge.setValue(legacyKnowledge);
                    }
                });

        // If parsing succeeds and the data is new, copy it into this object
        this.copyFrom(parsedKnowledge.getValue());

        // If the deserialized data is from before the read flag existed, set default flags on appropriate entries
        if (this.schemaVersion < UNREAD_FLAG_VERSION) {
            this.research.stream()
                    .map(ark -> ark instanceof ResearchEntryKey rek ? rek : null)
                    .filter(Objects::nonNull)
                    .filter(this::isReadByDefault)
                    .forEach(k -> this.addResearchFlag(k, ResearchFlag.READ));
        }

        // After post-processing, mark the new data as fully up-versioned
        this.schemaVersion = CURRENT_SCHEMA_VERSION;
    }

    protected boolean isReadByDefault(ResearchEntryKey key) {
        return this.research.contains(key) && !this.hasResearchFlag(key, ResearchFlag.NEW) && !this.hasResearchFlag(key, ResearchFlag.UPDATED);
    }

    public void copyFrom(@Nullable PlayerKnowledge other) {
        if (other == null || other.syncTimestamp <= this.syncTimestamp) {
            return;
        }

        this.syncTimestamp = other.syncTimestamp;
        this.clearResearch();
        this.clearKnowledge();

        this.research.addAll(other.research);
        this.stages.putAll(other.stages);
        this.flags.putAll(other.flags);
        this.knowledge.putAll(other.knowledge);
        this.topicHistory.addAll(other.topicHistory);
        this.project = other.project;
        this.topic = other.topic;
        this.schemaVersion = other.schemaVersion;
    }

    @Deprecated(forRemoval = true, since = "6.0.2-beta")
    @VisibleForTesting
    @Nonnull
    public CompoundTag serializeLegacyNBT(HolderLookup.Provider registries) {
        CompoundTag rootTag = new CompoundTag();
        
        RegistryOps<Tag> registryOps = registries.createSerializationContext(NbtOps.INSTANCE);
        
        // Serialize known research, including stage number and attached flags
        ListTag researchList = new ListTag();
        for (AbstractResearchKey<?> key : this.research) {
            CompoundTag tag = new CompoundTag();
            AbstractResearchKey.dispatchCodec().encodeStart(registryOps, key)
                .resultOrPartial(msg -> LOGGER.error("Failed to encode research entry in player knowledge capability: {}", msg))
                .ifPresent(encodedTag -> tag.put("key", encodedTag));
            if (this.stages.containsKey(key)) {
                tag.putInt("stage", this.stages.get(key));
            }
            Set<ResearchFlag> researchFlags = this.flags.get(key);
            if (researchFlags != null) {
                String str = Arrays.stream(researchFlags.toArray(ResearchFlag[]::new))
                                   .map(t -> t.name())
                                   .collect(Collectors.joining(","));
                if (str != null && !str.isEmpty()) {
                    tag.putString("flags", str);
                }
            }
            researchList.add(tag);
        }
        rootTag.put("research", researchList);
        
        // Serialize knowledge types, including accrued points
        ListTag knowledgeList = new ListTag();
        for (KnowledgeType knowledgeKey : this.knowledge.keySet()) {
            if (knowledgeKey != null) {
                Integer points = this.knowledge.get(knowledgeKey);
                if (points != null && points.intValue() > 0) {
                    CompoundTag tag = new CompoundTag();
                    tag.putString("key", knowledgeKey.name());
                    tag.putInt("value", points);
                    knowledgeList.add(tag);
                }
            }
        }
        rootTag.put("knowledge", knowledgeList);
        
        // Serialize active research project, if any
        this.project.ifPresent(value -> Project.codec().encodeStart(registryOps, value)
                .resultOrPartial(msg -> LOGGER.error("Failed to encode active research project in player knowledge capability: {}", msg))
                .ifPresent(encodedProject -> rootTag.put("project", encodedProject)));
        
        // Serialize last active grimoire topic, if any
        this.topic.ifPresent(topic -> AbstractResearchTopic.dispatchCodec().encodeStart(registryOps, topic)
                .resultOrPartial(msg -> LOGGER.error("Failed to encode current grimoire topic in player knowledge capability: {}", msg))
                .ifPresent(encodedTopic -> rootTag.put("topic", encodedTopic)));
        
        // Serialize grimoire topic history
        AbstractResearchTopic.dispatchCodec().listOf().encodeStart(registryOps, this.topicHistory)
            .resultOrPartial(msg -> LOGGER.error("Failed to encode grimoire topic history entry in player knowledge capability: {}", msg))
            .ifPresent(encodedHistory -> rootTag.put("topicHistory", encodedHistory));
        
        rootTag.putLong("syncTimestamp", System.currentTimeMillis());
        
        return rootTag;
    }

    @Deprecated(forRemoval = true, since = "6.0.2-beta")
    @VisibleForTesting
    public synchronized void deserializeLegacyNBT(HolderLookup.Provider registries, @Nullable CompoundTag nbt) {
        if (nbt == null || nbt.getLong("syncTimestamp") <= this.syncTimestamp) {
            return;
        }
        
        this.syncTimestamp = nbt.getLong("syncTimestamp");
        this.clearResearch();
        this.clearKnowledge();

        RegistryOps<Tag> registryOps = registries.createSerializationContext(NbtOps.INSTANCE);

        // Deserialize schema version
        this.schemaVersion = nbt.getInt("schemaVersion");
        
        // Deserialize known research, including stage number and attached flags
        ListTag researchList = nbt.getList("research", Tag.TAG_COMPOUND);
        for (int index = 0; index < researchList.size(); index++) {
            CompoundTag tag = researchList.getCompound(index);
            AbstractResearchKey.dispatchCodec().parse(registryOps, tag.get("key")).resultOrPartial(msg -> {
                LOGGER.error("Failed to decode research entry in player knowledge capability: {}", msg);
            }).ifPresent(parsedKey -> {
                if (!this.isResearchKnown(parsedKey)) {
                    this.research.add(parsedKey);
                    int stage = tag.getInt("stage");
                    if (stage > 0) {
                        this.stages.put(parsedKey, stage);
                    }
                    String flagStr = tag.getString("flags");
                    if (flagStr != null && !flagStr.isEmpty()) {
                        for (String flagName : flagStr.split(",")) {
                            try {
                                this.addResearchFlag(parsedKey, ResearchFlag.valueOf(flagName));
                            } catch (Exception e) {
                                LOGGER.warn("Invalid research flag name: " + flagName, e);
                            }
                        }
                    }
                }
            });
        }

        // Deserialize knowledge types, including accrued points
        ListTag knowledgeList = nbt.getList("knowledge", Tag.TAG_COMPOUND);
        for (int index = 0; index < knowledgeList.size(); index++) {
            CompoundTag tag = knowledgeList.getCompound(index);
            String keyStr = tag.getString("key");
            KnowledgeType key = null;
            try {
                key = KnowledgeType.valueOf(keyStr);
            } catch (Exception e) {}
            int points = tag.getInt("value");
            if (key != null) {
                this.knowledge.put(key, Integer.valueOf(points));
            }
        }
        
        // Deserialize active research project
        if (nbt.contains("project")) {
            Project.codec().parse(registryOps, nbt.getCompound("project"))
                .resultOrPartial(msg -> LOGGER.error("Failed to decode active research project in player knowledge capability: {}", msg))
                .ifPresent(project -> this.project = Optional.ofNullable(project));
        }
        
        // Deserialize last active grimoire topic
        if (nbt.contains("topic")) {
            AbstractResearchTopic.dispatchCodec().parse(registryOps, nbt.get("topic"))
                .resultOrPartial(msg -> LOGGER.error("Failed to decode current grimoire topic in player knowledge capability: {}", msg))
                .ifPresent(decodedTopic -> this.topic = Optional.ofNullable(decodedTopic));
        }
        
        // Deserialize grimoire topic history
        AbstractResearchTopic.dispatchCodec().listOf().parse(registryOps, nbt.get("topicHistory"))
            .resultOrPartial(msg -> LOGGER.error("Failed to decode grimoire topic history entry in player knowledge capability: {}", msg))
            .ifPresent(decodedHistory -> this.topicHistory.addAll(decodedHistory));
    }

    @Override
    public void clearResearch() {
        this.research.clear();
        this.stages.clear();
        this.flags.clear();
        this.project = Optional.empty();
        this.topic = Optional.empty();
        this.topicHistory.clear();
    }
    
    @Override
    public void clearKnowledge() {
        this.knowledge.clear();
    }
    
    @Override
    @Nonnull
    public Set<AbstractResearchKey<?>> getResearchSet() {
        return Collections.unmodifiableSet(this.research);
    }

    @Override
    @Nonnull
    public ResearchStatus getResearchStatus(@Nonnull RegistryAccess registryAccess, @Nullable AbstractResearchKey<?> research) {
        if (!this.isResearchKnown(research)) {
            return ResearchStatus.UNKNOWN;
        } else {
            // Research is complete if it is known and its current stage equals or exceeds the number of stages defined in its entry
            if (research instanceof ResearchEntryKey entryKey) {
                ResearchEntry entry = ResearchEntries.getEntry(registryAccess, entryKey);
                if (entry == null || entry.stages().isEmpty() || this.getResearchStage(research) >= entry.stages().size()) {
                    return ResearchStatus.COMPLETE;
                } else {
                    return ResearchStatus.IN_PROGRESS;
                }
            } else {
                // If the key isn't for a research entry (it's a StackCraftedKey, for example), then it's complete if it's in the known set
                return ResearchStatus.COMPLETE;
            }
        }
    }

    @Override
    public boolean isResearchComplete(@Nonnull RegistryAccess registryAccess, @Nullable AbstractResearchKey<?> research) {
        return (this.getResearchStatus(registryAccess, research) == ResearchStatus.COMPLETE);
    }

    @Override
    public boolean isResearchKnown(@Nullable AbstractResearchKey<?> research) {
        if (research == null) {
            return false;
        } else {
            return this.research.contains(research);
        }
    }

    @Override
    public int getResearchStage(@Nullable AbstractResearchKey<?> research) {
        if (research == null || !this.research.contains(research)) {
            return -1;
        } else {
            return this.stages.getOrDefault(research, 0);
        }
    }

    @Override
    public boolean addResearch(@Nullable AbstractResearchKey<?> research) {
        if (research != null && !this.isResearchKnown(research)) {
            this.research.add(research);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setResearchStage(@Nullable AbstractResearchKey<?> research, int newStage) {
        if (research == null || !this.research.contains(research) || newStage <= 0) {
            return false;
        } else {
            this.stages.put(research, newStage);
            return true;
        }
    }

    @Override
    public boolean removeResearch(@Nullable AbstractResearchKey<?> research) {
        if (research != null && this.isResearchKnown(research)) {
            this.research.remove(research);
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean addResearchFlag(@Nullable AbstractResearchKey<?> research, @Nullable ResearchFlag flag) {
        if (research == null || flag == null) {
            return false;
        }
        return this.flags.computeIfAbsent(research, $ -> EnumSet.noneOf(ResearchFlag.class)).add(flag);
    }
    
    @Override
    public boolean removeResearchFlag(@Nullable AbstractResearchKey<?> research, @Nullable ResearchFlag flag) {
        if (research == null || flag == null) {
            return false;
        } else {
            return this.removeResearchFlagInner(research, flag);
        }
    }
    
    protected boolean removeResearchFlagInner(@Nonnull AbstractResearchKey<?> research, @Nonnull ResearchFlag flag) {
        Set<ResearchFlag> researchFlags = this.flags.get(research);
        if (researchFlags != null) {
            boolean retVal = researchFlags.remove(flag);
            if (researchFlags.isEmpty()) {
                // Remove empty flag sets to prevent data bloat
                this.flags.remove(research);
            }
            return retVal;
        }
        return false;
    }
    
    @Override
    public boolean hasResearchFlag(@Nullable AbstractResearchKey<?> research, @Nullable ResearchFlag flag) {
        if (research == null || flag == null) {
            return false;
        } else {
            Set<ResearchFlag> researchFlags = this.flags.get(research);
            return researchFlags != null && researchFlags.contains(flag);
        }
    }
    
    @Override
    @Nonnull
    public Set<ResearchFlag> getResearchFlags(@Nullable AbstractResearchKey<?> research) {
        if (research == null) {
            return Collections.emptySet();
        } else {
            return Collections.unmodifiableSet(this.flags.getOrDefault(research, EnumSet.noneOf(ResearchFlag.class)));
        }
    }
    
    @Override
    public boolean addKnowledge(@Nullable KnowledgeType type, int amount) {
        if (type == null) {
            return false;
        }
        int points = this.getKnowledgeRaw(type) + amount;
        if (points < 0) {
            return false;
        } else {
            this.knowledge.put(type, Integer.valueOf(points));
            return true;
        }
    }
    
    @Override
    public int getKnowledge(@Nullable KnowledgeType type) {
        if (type == null) {
            return 0;
        } else {
            // Calculate knowledge levels based on the knowledge type's progression value
            return (int)Math.floor((double)this.getKnowledgeRaw(type) / (double)type.getProgression());
        }
    }
    
    @Override
    public int getKnowledgeRaw(@Nullable KnowledgeType type) {
        if (type == null) {
            return 0;
        } else {
            return this.knowledge.getOrDefault(type, Integer.valueOf(0)).intValue();
        }
    }
    
    @Override
    public Project getActiveResearchProject() {
        return this.project.orElse(null);
    }
    
    @Override
    public void setActiveResearchProject(Project project) {
        this.project = Optional.ofNullable(project);
    }

    @Override
    public AbstractResearchTopic<?> getLastResearchTopic() {
        return this.topic.orElse(MainIndexResearchTopic.INSTANCE);
    }

    @Override
    public void setLastResearchTopic(AbstractResearchTopic<?> topic) {
        this.topic = Optional.ofNullable(topic);
    }

    @Override
    public LinkedList<AbstractResearchTopic<?>> getResearchTopicHistory() {
        return this.topicHistory;
    }

    @Override
    public void setResearchTopicHistory(List<AbstractResearchTopic<?>> history) {
        this.topicHistory.clear();
        this.topicHistory.addAll(history);
    }

    @Override
    public void sync(@Nullable ServerPlayer player) {
        if (player != null) {
            this.syncTimestamp = System.currentTimeMillis();

            // Clone this data before passing it to the network
            RegistryOps<Tag> registryOps = player.level().registryAccess().createSerializationContext(NbtOps.INSTANCE);
            CODEC.encodeStart(registryOps, this)
                    .resultOrPartial(err -> LOGGER.error("Failed to encode knowledge data for syncing"))
                    .ifPresent(tag -> {
                        CODEC.parse(registryOps, tag)
                                .resultOrPartial(err -> LOGGER.error("Failed to parse knowledge data for syncing"))
                                .ifPresent(knowledge -> {
                                    PacketHandler.sendToPlayer(new SyncKnowledgePacket(knowledge), player);
                                });
                    });

            // Remove all popup flags after syncing to prevent spam
            this.flags.keySet().forEach(key -> this.removeResearchFlagInner(key, ResearchFlag.POPUP));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PlayerKnowledge that)) return false;
        return Objects.equals(research, that.research) &&
                Objects.equals(stages, that.stages) &&
                Objects.equals(flags, that.flags) &&
                Objects.equals(knowledge, that.knowledge) &&
                Objects.equals(topicHistory, that.topicHistory) &&
                Objects.equals(project, that.project) &&
                Objects.equals(topic, that.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(research, stages, flags, knowledge, topicHistory, project, topic);
    }

    protected record StageEntry(AbstractResearchKey<?> key, int stage) {
        public static final Codec<StageEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    AbstractResearchKey.dispatchCodec().fieldOf("key").forGetter(StageEntry::key),
                    Codec.INT.fieldOf("stage").forGetter(StageEntry::stage)
            ).apply(instance, StageEntry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, StageEntry> STREAM_CODEC = StreamCodec.composite(
                AbstractResearchKey.dispatchStreamCodec(), StageEntry::key,
                ByteBufCodecs.VAR_INT, StageEntry::stage,
                StageEntry::new);
    }

    protected record FlagsEntry(AbstractResearchKey<?> key, Set<ResearchFlag> flagSet) {
        public static final Codec<FlagsEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    AbstractResearchKey.dispatchCodec().fieldOf("key").forGetter(FlagsEntry::key),
                    ResearchFlag.CODEC.listOf().<Set<ResearchFlag>>xmap(HashSet::new, ImmutableList::copyOf).fieldOf("flagSet").forGetter(FlagsEntry::flagSet)
            ).apply(instance, FlagsEntry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, FlagsEntry> STREAM_CODEC = StreamCodec.composite(
                AbstractResearchKey.dispatchStreamCodec(), FlagsEntry::key,
                ResearchFlag.STREAM_CODEC.apply(ByteBufCodecs.list()).map(HashSet::new, ImmutableList::copyOf), FlagsEntry::flagSet,
                FlagsEntry::new);
    }
}

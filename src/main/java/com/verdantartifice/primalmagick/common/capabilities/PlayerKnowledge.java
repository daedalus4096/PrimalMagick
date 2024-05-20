package com.verdantartifice.primalmagick.common.capabilities;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncKnowledgePacket;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.topics.AbstractResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.MainIndexResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.ResearchTopicFactory;
import com.verdantartifice.primalmagick.common.theorycrafting.Project;
import com.verdantartifice.primalmagick.common.theorycrafting.ProjectFactory;

import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Default implementation of the player knowledge capability.
 * 
 * @author Daedalus4096
 */
public class PlayerKnowledge implements IPlayerKnowledge {
    private static final Logger LOGGER = LogManager.getLogger();
    
    private final Set<AbstractResearchKey<?>> research = ConcurrentHashMap.newKeySet();                 // Set of known research
    private final Map<AbstractResearchKey<?>, Integer> stages = new ConcurrentHashMap<>();              // Map of research keys to current stage numbers
    private final Map<AbstractResearchKey<?>, Set<ResearchFlag>> flags = new ConcurrentHashMap<>();     // Map of research keys to attached flag sets
    private final Map<KnowledgeType, Integer> knowledge = new ConcurrentHashMap<>();                    // Map of knowledge types to accrued points
    private final LinkedList<AbstractResearchTopic> topicHistory = new LinkedList<>();                  // Grimoire research topic history
    
    private Project project = null;     // Currently active research project
    private AbstractResearchTopic topic = null; // Last active grimoire research topic
    private long syncTimestamp = 0L;    // Last timestamp at which this capability received a sync from the server

    @Override
    @Nonnull
    public CompoundTag serializeNBT() {
        CompoundTag rootTag = new CompoundTag();
        
        // Serialize known research, including stage number and attached flags
        ListTag researchList = new ListTag();
        for (AbstractResearchKey<?> key : this.research) {
            CompoundTag tag = new CompoundTag();
            AbstractResearchKey.CODEC.encodeStart(NbtOps.INSTANCE, key).resultOrPartial(LOGGER::error).ifPresent(encodedTag -> tag.put("key", encodedTag));
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
        if (this.project != null) {
            Project.CODEC.encodeStart(NbtOps.INSTANCE, this.project)
                .resultOrPartial(LOGGER::error)
                .ifPresent(encodedProject -> rootTag.put("project", encodedProject));
        }
        
        // Serialize last active grimoire topic, if any
        if (this.topic != null) {
            rootTag.put("topic", this.topic.serializeNBT());
        }
        
        // Serialize grimoire topic history
        ListTag historyList = new ListTag();
        for (AbstractResearchTopic topic : this.topicHistory) {
            historyList.add(topic.serializeNBT());
        }
        rootTag.put("topicHistory", historyList);
        
        rootTag.putLong("syncTimestamp", System.currentTimeMillis());
        
        return rootTag;
    }

    @Override
    public synchronized void deserializeNBT(@Nullable CompoundTag nbt) {
        if (nbt == null || nbt.getLong("syncTimestamp") <= this.syncTimestamp) {
            return;
        }
        
        this.syncTimestamp = nbt.getLong("syncTimestamp");
        this.clearResearch();
        this.clearKnowledge();
        this.project = null;
        
        // Deserialize known research, including stage number and attached flags
        ListTag researchList = nbt.getList("research", Tag.TAG_COMPOUND);
        for (int index = 0; index < researchList.size(); index++) {
            CompoundTag tag = researchList.getCompound(index);
            AbstractResearchKey.CODEC.parse(NbtOps.INSTANCE, tag.get("key")).resultOrPartial(LOGGER::error).ifPresent(parsedKey -> {
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
            this.project = ProjectFactory.getProjectFromNBT(nbt.getCompound("project"));
        }
        
        // Deserialize last active grimoire topic
        if (nbt.contains("topic")) {
            this.topic = ResearchTopicFactory.deserializeNBT(nbt.getCompound("topic"));
        }
        
        // Deserialize grimoire topic history
        ListTag historyList = nbt.getList("topicHistory", Tag.TAG_COMPOUND);
        for (int index = 0; index < historyList.size(); index++) {
            AbstractResearchTopic topic = ResearchTopicFactory.deserializeNBT(historyList.getCompound(index));
            if (topic != null) {
                this.topicHistory.add(topic);
            }
        }
    }

    @Override
    public void clearResearch() {
        this.research.clear();
        this.stages.clear();
        this.flags.clear();
        this.project = null;
        this.topic = null;
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
                }
            }
            return ResearchStatus.IN_PROGRESS;
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
        return this.project;
    }
    
    @Override
    public void setActiveResearchProject(Project project) {
        this.project = project;
    }

    @Override
    public AbstractResearchTopic getLastResearchTopic() {
        return this.topic == null ? MainIndexResearchTopic.INSTANCE : this.topic;
    }

    @Override
    public void setLastResearchTopic(AbstractResearchTopic topic) {
        this.topic = topic;
    }

    @Override
    public LinkedList<AbstractResearchTopic> getResearchTopicHistory() {
        return this.topicHistory;
    }

    @Override
    public void setResearchTopicHistory(List<AbstractResearchTopic> history) {
        this.topicHistory.clear();
        this.topicHistory.addAll(history);
    }

    @Override
    public void sync(@Nullable ServerPlayer player) {
        if (player != null) {
            PacketHandler.sendToPlayer(new SyncKnowledgePacket(player), player);
            
            // Remove all popup flags after syncing to prevent spam
            this.flags.keySet().forEach(key -> this.removeResearchFlagInner(key, ResearchFlag.POPUP));
        }
    }
    
    /**
     * Capability provider for the player knowledge capability.  Used to attach capability data to the owner.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagick.common.events.CapabilityEvents}
     */
    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        public static final ResourceLocation NAME = PrimalMagick.resource("capability_knowledge");
        
        private final IPlayerKnowledge instance = new PlayerKnowledge();
        private final LazyOptional<IPlayerKnowledge> holder = LazyOptional.of(() -> instance);  // Cache a lazy optional of the capability instance
        
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == PrimalMagickCapabilities.KNOWLEDGE) {
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

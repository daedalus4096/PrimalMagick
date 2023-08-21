package com.verdantartifice.primalmagick.common.capabilities;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncKnowledgePacket;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.research.topics.AbstractResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.MainIndexResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.ResearchTopicFactory;
import com.verdantartifice.primalmagick.common.theorycrafting.Project;
import com.verdantartifice.primalmagick.common.theorycrafting.ProjectFactory;

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
 * Default implementation of the player knowledge capability.
 * 
 * @author Daedalus4096
 */
public class PlayerKnowledge implements IPlayerKnowledge {
    private final Set<String> research = ConcurrentHashMap.newKeySet();             // Set of known research
    private final Map<String, Integer> stages = new ConcurrentHashMap<>();          // Map of research keys to current stage numbers
    private final Map<String, Set<ResearchFlag>> flags = new ConcurrentHashMap<>(); // Map of research keys to attached flag sets
    private final Map<KnowledgeType, Integer> knowledge = new ConcurrentHashMap<>();   // Map of knowledge types to accrued points
    private final LinkedList<AbstractResearchTopic> topicHistory = new LinkedList<>();  // Grimoire research topic history
    
    private Project project = null;     // Currently active research project
    private AbstractResearchTopic topic = null; // Last active grimoire research topic
    private long syncTimestamp = 0L;    // Last timestamp at which this capability received a sync from the server

    @Override
    @Nonnull
    public CompoundTag serializeNBT() {
        CompoundTag rootTag = new CompoundTag();
        
        // Serialize known research, including stage number and attached flags
        ListTag researchList = new ListTag();
        for (String res : this.research) {
            CompoundTag tag = new CompoundTag();
            tag.putString("key", res);
            if (this.stages.containsKey(res)) {
                tag.putInt("stage", this.stages.get(res).intValue());
            }
            Set<ResearchFlag> researchFlags = this.flags.get(res);
            if (researchFlags != null) {
                String str = Arrays.stream(researchFlags.toArray(new ResearchFlag[researchFlags.size()]))
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
            rootTag.put("project", this.project.serializeNBT());
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
            SimpleResearchKey keyObj = SimpleResearchKey.parse(tag.getString("key"));
            if (keyObj != null && !this.isResearchKnown(keyObj)) {
                this.research.add(keyObj.getRootKey());
                int stage = tag.getInt("stage");
                if (stage > 0) {
                    this.stages.put(keyObj.getRootKey(), Integer.valueOf(stage));
                }
                String flagStr = tag.getString("flags");
                if (flagStr != null && !flagStr.isEmpty()) {
                    for (String flagName : flagStr.split(",")) {
                        ResearchFlag flag = null;
                        try {
                            flag = ResearchFlag.valueOf(flagName);
                        } catch (Exception e) {}
                        this.addResearchFlag(keyObj, flag);
                    }
                }
            }
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
    public Set<SimpleResearchKey> getResearchSet() {
        // Map the stored strings to parsed keys, filtering out failures
        return Collections.unmodifiableSet(this.research.stream()
                                            .map(s -> SimpleResearchKey.parse(s))
                                            .filter(Objects::nonNull)
                                            .collect(Collectors.toSet()));
    }

    @Override
    @Nonnull
    public ResearchStatus getResearchStatus(@Nullable SimpleResearchKey research) {
        if (!this.isResearchKnown(research)) {
            return ResearchStatus.UNKNOWN;
        } else {
            // Research is complete if it is known and its current stage exceeds the number of stages defined in its entry
            ResearchEntry entry = ResearchEntries.getEntry(research);
            if (entry == null || entry.getStages().isEmpty() || this.getResearchStage(research) >= entry.getStages().size()) {
                return ResearchStatus.COMPLETE;
            } else {
                return ResearchStatus.IN_PROGRESS;
            }
        }
    }

    @Override
    public boolean isResearchComplete(@Nullable SimpleResearchKey research) {
        return (this.getResearchStatus(research) == ResearchStatus.COMPLETE);
    }

    @Override
    public boolean isResearchKnown(@Nullable SimpleResearchKey research) {
        if (research == null) {
            return false;
        }
        if ("".equals(research.getRootKey())) {
            return true;
        }

        // If a specific stage is specified in the given key, check if the current stage meets it
        if (research.hasStage() && (this.getResearchStage(research) + 1) < research.getStage()) {
            return false;
        } else {
            return this.research.contains(research.getRootKey());
        }
    }

    @Override
    public int getResearchStage(@Nullable SimpleResearchKey research) {
        if (research == null || research.getRootKey().isEmpty() || !this.research.contains(research.getRootKey())) {
            return -1;
        } else {
            return this.stages.getOrDefault(research.getRootKey(), Integer.valueOf(0)).intValue();
        }
    }

    @Override
    public boolean addResearch(@Nullable SimpleResearchKey research) {
        if (research != null && !this.isResearchKnown(research)) {
            this.research.add(research.getRootKey());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setResearchStage(@Nullable SimpleResearchKey research, int newStage) {
        if (research == null || research.getRootKey().isEmpty() || !this.research.contains(research.getRootKey()) || newStage <= 0) {
            return false;
        } else {
            this.stages.put(research.getRootKey(), Integer.valueOf(newStage));
            return true;
        }
    }

    @Override
    public boolean removeResearch(@Nullable SimpleResearchKey research) {
        if (research != null && this.isResearchKnown(research)) {
            this.research.remove(research.getRootKey());
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean addResearchFlag(@Nullable SimpleResearchKey research, @Nullable ResearchFlag flag) {
        if (research == null || flag == null) {
            return false;
        }
        Set<ResearchFlag> researchFlags = this.flags.get(research.getRootKey());
        if (researchFlags == null) {
            // Create and store a set if no flags are currently attached to the research
            researchFlags = EnumSet.noneOf(ResearchFlag.class);
            this.flags.put(research.getRootKey(), researchFlags);
        }
        return researchFlags.add(flag);
    }
    
    @Override
    public boolean removeResearchFlag(@Nullable SimpleResearchKey research, @Nullable ResearchFlag flag) {
        if (research == null || flag == null) {
            return false;
        } else {
            return this.removeResearchFlagInner(research.getRootKey(), flag);
        }
    }
    
    protected boolean removeResearchFlagInner(@Nonnull String researchKeyStr, @Nonnull ResearchFlag flag) {
        Set<ResearchFlag> researchFlags = this.flags.get(researchKeyStr);
        if (researchFlags != null) {
            boolean retVal = researchFlags.remove(flag);
            if (researchFlags.isEmpty()) {
                // Remove empty flag sets to prevent data bloat
                this.flags.remove(researchKeyStr);
            }
            return retVal;
        }
        return false;
    }
    
    @Override
    public boolean hasResearchFlag(@Nullable SimpleResearchKey research, @Nullable ResearchFlag flag) {
        if (research == null || flag == null) {
            return false;
        } else {
            Set<ResearchFlag> researchFlags = this.flags.get(research.getRootKey());
            return researchFlags != null && researchFlags.contains(flag);
        }
    }
    
    @Override
    @Nonnull
    public Set<ResearchFlag> getResearchFlags(@Nullable SimpleResearchKey research) {
        if (research == null) {
            return Collections.emptySet();
        } else {
            return Collections.unmodifiableSet(this.flags.getOrDefault(research.getRootKey(), EnumSet.noneOf(ResearchFlag.class)));
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
            for (String keyStr : this.flags.keySet()) {
                this.removeResearchFlagInner(keyStr, ResearchFlag.POPUP);
            }
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

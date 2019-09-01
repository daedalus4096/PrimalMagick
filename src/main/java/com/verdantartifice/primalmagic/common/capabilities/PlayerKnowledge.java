package com.verdantartifice.primalmagic.common.capabilities;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.data.SyncKnowledgePacket;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagic.common.research.ResearchEntries;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerKnowledge implements IPlayerKnowledge {
    private final Set<String> research = new HashSet<>();
    private final Map<String, Integer> stages = new HashMap<>();
    private final Map<String, Set<ResearchFlag>> flags = new HashMap<>();
    private final Map<String, Integer> knowledge = new HashMap<>();

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT rootTag = new CompoundNBT();
        
        ListNBT researchList = new ListNBT();
        for (String res : this.research) {
            CompoundNBT tag = new CompoundNBT();
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
        
        ListNBT knowledgeList = new ListNBT();
        for (String knowledgeKey : this.knowledge.keySet()) {
            if (knowledgeKey != null && !knowledgeKey.isEmpty()) {
                Integer points = this.knowledge.get(knowledgeKey);
                if (points != null && points.intValue() > 0) {
                    CompoundNBT tag = new CompoundNBT();
                    tag.putString("key", knowledgeKey);
                    tag.putInt("value", points);
                    knowledgeList.add(tag);
                }
            }
        }
        rootTag.put("knowledge", knowledgeList);
        
        return rootTag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt == null) {
            return;
        }
        
        this.clear();
        ListNBT researchList = nbt.getList("research", 10);
        for (int index = 0; index < researchList.size(); index++) {
            CompoundNBT tag = researchList.getCompound(index);
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
        ListNBT knowledgeList = nbt.getList("knowledge", 10);
        for (int index = 0; index < knowledgeList.size(); index++) {
            CompoundNBT tag = knowledgeList.getCompound(index);
            String key = tag.getString("key");
            int points = tag.getInt("value");
            if (key != null && !key.isEmpty()) {
                this.knowledge.put(key, Integer.valueOf(points));
            }
        }
    }

    @Override
    public void clear() {
        this.research.clear();
        this.stages.clear();
        this.flags.clear();
        this.knowledge.clear();
    }
    
    @Override
    @Nonnull
    public Set<SimpleResearchKey> getResearchSet() {
        return Collections.unmodifiableSet(this.research.stream()
                                            .map(s -> SimpleResearchKey.parse(s))
                                            .filter(Objects::nonNull)
                                            .collect(Collectors.toSet()));
    }

    @Override
    public ResearchStatus getResearchStatus(SimpleResearchKey research) {
        if (!this.isResearchKnown(research)) {
            return ResearchStatus.UNKNOWN;
        } else {
            ResearchEntry entry = ResearchEntries.getEntry(research);
            if (entry == null || entry.getStages().isEmpty() || this.getResearchStage(research) > entry.getStages().size()) {
                return ResearchStatus.COMPLETE;
            } else {
                return ResearchStatus.IN_PROGRESS;
            }
        }
    }

    @Override
    public boolean isResearchComplete(SimpleResearchKey research) {
        return (this.getResearchStatus(research) == ResearchStatus.COMPLETE);
    }

    @Override
    public boolean isResearchKnown(SimpleResearchKey research) {
        if (research == null) {
            return false;
        }
        if ("".equals(research.getRootKey())) {
            return true;
        }
        if (research.hasStage() && this.getResearchStage(research) < research.getStage()) {
            return false;
        } else {
            return this.research.contains(research.getRootKey());
        }
    }

    @Override
    public int getResearchStage(SimpleResearchKey research) {
        if (research == null || research.getRootKey().isEmpty() || !this.research.contains(research.getRootKey())) {
            return -1;
        } else {
            return this.stages.getOrDefault(research.getRootKey(), Integer.valueOf(0)).intValue();
        }
    }

    @Override
    public boolean addResearch(SimpleResearchKey research) {
        if (!this.isResearchKnown(research)) {
            this.research.add(research.getRootKey());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setResearchStage(SimpleResearchKey research, int newStage) {
        if (research == null || research.getRootKey().isEmpty() || !this.research.contains(research.getRootKey()) || newStage <= 0) {
            return false;
        } else {
            this.stages.put(research.getRootKey(), Integer.valueOf(newStage));
            return true;
        }
    }

    @Override
    public boolean removeResearch(SimpleResearchKey research) {
        if (this.isResearchKnown(research)) {
            this.research.remove(research.getRootKey());
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean addResearchFlag(SimpleResearchKey research, ResearchFlag flag) {
        if (flag == null) {
            return false;
        }
        Set<ResearchFlag> researchFlags = this.flags.get(research.getRootKey());
        if (researchFlags == null) {
            researchFlags = EnumSet.noneOf(ResearchFlag.class);
            this.flags.put(research.getRootKey(), researchFlags);
        }
        return researchFlags.add(flag);
    }
    
    @Override
    public boolean removeResearchFlag(SimpleResearchKey research, ResearchFlag flag) {
        Set<ResearchFlag> researchFlags = this.flags.get(research.getRootKey());
        if (researchFlags != null) {
            boolean retVal = researchFlags.remove(flag);
            if (researchFlags.isEmpty()) {
                this.flags.remove(research.getRootKey());
            }
            return retVal;
        }
        return false;
    }
    
    @Override
    public boolean hasResearchFlag(SimpleResearchKey research, ResearchFlag flag) {
        Set<ResearchFlag> researchFlags = this.flags.get(research.getRootKey());
        return researchFlags != null && researchFlags.contains(flag);
    }
    
    @Override
    @Nonnull
    public Set<ResearchFlag> getResearchFlags(SimpleResearchKey research) {
        return Collections.unmodifiableSet(this.flags.getOrDefault(research.getRootKey(), EnumSet.noneOf(ResearchFlag.class)));
    }
    
    @Nonnull
    private String makeKnowledgeKey(@Nonnull KnowledgeType type, @Nonnull ResearchDiscipline discipline) {
        StringBuilder builder = new StringBuilder();
        builder.append(type.name());
        builder.append("_");
        builder.append(discipline.getKey());
        return builder.toString();
    }
    
    @Override
    public boolean addKnowledge(KnowledgeType type, ResearchDiscipline discipline, int amount) {
        if (type == null || discipline == null) {
            return false;
        }
        int points = this.getKnowledgeRaw(type, discipline) + amount;
        if (points < 0) {
            return false;
        } else {
            this.knowledge.put(this.makeKnowledgeKey(type, discipline), Integer.valueOf(points));
            return true;
        }
    }
    
    @Override
    public int getKnowledge(KnowledgeType type, ResearchDiscipline discipline) {
        return (int)Math.floor((double)this.getKnowledgeRaw(type, discipline) / (double)type.getProgression());
    }
    
    @Override
    public int getKnowledgeRaw(KnowledgeType type, ResearchDiscipline discipline) {
        return this.knowledge.getOrDefault(this.makeKnowledgeKey(type, discipline), Integer.valueOf(0)).intValue();
    }

    @Override
    public void sync(ServerPlayerEntity player) {
        PacketHandler.sendToPlayer(new SyncKnowledgePacket(player), player);
    }
    
    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        public static final ResourceLocation NAME = new ResourceLocation(PrimalMagic.MODID, "capability_knowledge");
        
        private final IPlayerKnowledge instance = PrimalMagicCapabilities.KNOWLEDGE.getDefaultInstance();
        private final LazyOptional<IPlayerKnowledge> holder = LazyOptional.of(() -> instance);
        
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            if (cap == PrimalMagicCapabilities.KNOWLEDGE) {
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

    public static class Storage implements Capability.IStorage<IPlayerKnowledge> {
        @Override
        public INBT writeNBT(Capability<IPlayerKnowledge> capability, IPlayerKnowledge instance, Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<IPlayerKnowledge> capability, IPlayerKnowledge instance, Direction side, INBT nbt) {
            instance.deserializeNBT((CompoundNBT)nbt);
        }
    }
    
    public static class Factory implements Callable<IPlayerKnowledge> {
        @Override
        public IPlayerKnowledge call() throws Exception {
            return new PlayerKnowledge();
        }
    }
}

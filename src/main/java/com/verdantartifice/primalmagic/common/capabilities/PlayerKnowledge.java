package com.verdantartifice.primalmagic.common.capabilities;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.data.SyncKnowledgePacket;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerKnowledge implements IPlayerKnowledge {
    private final Set<String> research = new HashSet<>();
    private final Map<String, Integer> stages = new HashMap<>();
    private final Map<String, Set<ResearchFlag>> flags = new HashMap<>();

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
            String key = tag.getString("key");
            if (key != null && !this.isResearchKnown(key)) {
                this.research.add(key);
                int stage = tag.getInt("stage");
                if (stage > 0) {
                    this.stages.put(key, Integer.valueOf(stage));
                }
                String flagStr = tag.getString("flags");
                if (flagStr != null && !flagStr.isEmpty()) {
                    for (String flagName : flagStr.split(",")) {
                        ResearchFlag flag = null;
                        try {
                            flag = ResearchFlag.valueOf(flagName);
                        } catch (Exception e) {}
                        this.addResearchFlag(key, flag);
                    }
                }
            }
        }
    }

    @Override
    public void clear() {
        this.research.clear();
        this.stages.clear();
        this.flags.clear();
    }
    
    @Override
    @Nonnull
    public Set<String> getResearchSet() {
        return Collections.unmodifiableSet(this.research);
    }

    @Override
    public ResearchStatus getResearchStatus(String research) {
        // TODO Auto-generated method stub
        return ResearchStatus.UNKNOWN;
    }

    @Override
    public boolean isResearchComplete(String research) {
        return (this.getResearchStatus(research) == ResearchStatus.COMPLETE);
    }

    @Override
    public boolean isResearchKnown(String research) {
        if (research == null) {
            return false;
        }
        if ("".equals(research)) {
            return true;
        }
        String[] tokens = research.split("@");
        if (tokens.length > 1 && this.getResearchStage(tokens[0]) < MathHelper.getInt(tokens[1], 0)) {
            return false;
        } else {
            return this.research.contains(tokens[0]);
        }
    }

    @Override
    public int getResearchStage(String research) {
        if (research == null || !this.research.contains(research)) {
            return -1;
        } else {
            return this.stages.getOrDefault(research, Integer.valueOf(0)).intValue();
        }
    }

    @Override
    public boolean addResearch(String research) {
        if (!this.isResearchKnown(research)) {
            this.research.add(research);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setResearchStage(String research, int newStage) {
        if (research == null || !this.research.contains(research) || newStage <= 0) {
            return false;
        } else {
            this.stages.put(research, Integer.valueOf(newStage));
            return true;
        }
    }

    @Override
    public boolean removeResearch(String research) {
        if (this.isResearchKnown(research)) {
            this.research.remove(research);
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean addResearchFlag(String research, ResearchFlag flag) {
        if (flag == null) {
            return false;
        }
        Set<ResearchFlag> researchFlags = this.flags.get(research);
        if (researchFlags == null) {
            researchFlags = EnumSet.noneOf(ResearchFlag.class);
            this.flags.put(research, researchFlags);
        }
        return researchFlags.add(flag);
    }
    
    @Override
    public boolean removeResearchFlag(String research, ResearchFlag flag) {
        Set<ResearchFlag> researchFlags = this.flags.get(research);
        if (researchFlags != null) {
            boolean retVal = researchFlags.remove(flag);
            if (researchFlags.isEmpty()) {
                this.flags.remove(research);
            }
            return retVal;
        }
        return false;
    }
    
    @Override
    public boolean hasResearchFlag(String research, ResearchFlag flag) {
        Set<ResearchFlag> researchFlags = this.flags.get(research);
        return researchFlags != null && researchFlags.contains(flag);
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

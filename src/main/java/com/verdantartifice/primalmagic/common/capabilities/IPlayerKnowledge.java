package com.verdantartifice.primalmagic.common.capabilities;

import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerKnowledge extends INBTSerializable<CompoundNBT> {
    public void clear();
    
    @Nonnull
    public Set<String> getResearchSet();
    
    public ResearchStatus getResearchStatus(String research);
    public boolean isResearchComplete(String research);
    public boolean isResearchKnown(String research);
    public int getResearchStage(String research);
    
    public boolean addResearch(String research);
    public boolean setResearchStage(String research, int newStage);
    public boolean removeResearch(String research);
    
    public void sync(ServerPlayerEntity player);
    
    public static enum ResearchStatus {
        UNKNOWN,
        IN_PROGRESS,
        COMPLETE
    }
}

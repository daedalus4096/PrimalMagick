package com.verdantartifice.primalmagic.common.capabilities;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerKnowledge extends INBTSerializable<CompoundNBT> {
    public void clearResearch();
    public void clearKnowledge();
    
    @Nonnull
    public Set<SimpleResearchKey> getResearchSet();
    
    @Nonnull
    public ResearchStatus getResearchStatus(@Nullable SimpleResearchKey research);
    public boolean isResearchComplete(@Nullable SimpleResearchKey research);
    public boolean isResearchKnown(@Nullable SimpleResearchKey research);
    public int getResearchStage(@Nullable SimpleResearchKey research);
    
    public boolean addResearch(@Nullable SimpleResearchKey research);
    public boolean setResearchStage(@Nullable SimpleResearchKey research, int newStage);
    public boolean removeResearch(@Nullable SimpleResearchKey research);
    
    public boolean addResearchFlag(@Nullable SimpleResearchKey research, @Nullable ResearchFlag flag);
    public boolean removeResearchFlag(@Nullable SimpleResearchKey research, @Nullable ResearchFlag flag);
    public boolean hasResearchFlag(@Nullable SimpleResearchKey research, @Nullable ResearchFlag flag);
    
    @Nonnull
    public Set<ResearchFlag> getResearchFlags(@Nullable SimpleResearchKey research);
    
    public boolean addKnowledge(@Nullable KnowledgeType type, @Nullable ResearchDiscipline discipline, int amount);
    public int getKnowledge(@Nullable KnowledgeType type, @Nullable ResearchDiscipline discipline);
    public int getKnowledgeRaw(@Nullable KnowledgeType type, @Nullable ResearchDiscipline discipline);
    
    public void sync(@Nullable ServerPlayerEntity player);
    
    public static enum ResearchStatus {
        UNKNOWN,
        IN_PROGRESS,
        COMPLETE
    }
    
    public static enum ResearchFlag {
        NEW,
        UPDATED,
        POPUP
    }
    
    public static enum KnowledgeType {
        OBSERVATION(16, new ResourceLocation(PrimalMagic.MODID, "textures/research/knowledge_observation.png")),
        THEORY(32, new ResourceLocation(PrimalMagic.MODID, "textures/research/knowledge_theory.png"));
        
        private short progression;
        private ResourceLocation iconLocation;
        
        private KnowledgeType(int progression, @Nonnull ResourceLocation iconLocation) {
            this.progression = (short)progression;
            this.iconLocation = iconLocation;
        }
        
        public int getProgression() {
            return this.progression;
        }
        
        @Nonnull
        public ResourceLocation getIconLocation() {
            return this.iconLocation;
        }
        
        @Nonnull
        public String getNameTranslationKey() {
            return "primalmagic.knowledge_type." + this.name();
        }
    }
}

package com.verdantartifice.primalmagic.common.capabilities;

import java.util.Set;

import javax.annotation.Nonnull;

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
    
    public ResearchStatus getResearchStatus(SimpleResearchKey research);
    public boolean isResearchComplete(SimpleResearchKey research);
    public boolean isResearchKnown(SimpleResearchKey research);
    public int getResearchStage(SimpleResearchKey research);
    
    public boolean addResearch(SimpleResearchKey research);
    public boolean setResearchStage(SimpleResearchKey research, int newStage);
    public boolean removeResearch(SimpleResearchKey research);
    
    public boolean addResearchFlag(SimpleResearchKey research, ResearchFlag flag);
    public boolean removeResearchFlag(SimpleResearchKey research, ResearchFlag flag);
    public boolean hasResearchFlag(SimpleResearchKey research, ResearchFlag flag);
    
    @Nonnull
    public Set<ResearchFlag> getResearchFlags(SimpleResearchKey research);
    
    public boolean addKnowledge(KnowledgeType type, ResearchDiscipline discipline, int amount);
    public int getKnowledge(KnowledgeType type, ResearchDiscipline discipline);
    public int getKnowledgeRaw(KnowledgeType type, ResearchDiscipline discipline);
    
    public void sync(ServerPlayerEntity player);
    
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

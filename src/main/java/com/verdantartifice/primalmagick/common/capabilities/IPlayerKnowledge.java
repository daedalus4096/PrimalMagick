package com.verdantartifice.primalmagick.common.capabilities;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.research.topics.AbstractResearchTopic;
import com.verdantartifice.primalmagick.common.theorycrafting.Project;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Capability interface for storing research and knowledge.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
public interface IPlayerKnowledge extends INBTSerializable<CompoundTag> {
    /**
     * Remove all research from the player.
     */
    public void clearResearch();
    
    /**
     * Remove all knowledge from the player.
     */
    public void clearKnowledge();
    
    /**
     * Get all research known by the player.
     * 
     * @return a set of keys for all research entries known by the player
     */
    @Nonnull
    public Set<SimpleResearchKey> getResearchSet();
    
    /**
     * Get the status of a given research for the player.
     * 
     * @param research a key for the desired research entry
     * @return the current status of the given research entry
     */
    @Nonnull
    public ResearchStatus getResearchStatus(@Nullable SimpleResearchKey research);
    
    /**
     * Determine if the given research has been completed by the player.
     * 
     * @param research a key for the desired research entry
     * @return true if the given research is complete, false otherwise
     */
    public boolean isResearchComplete(@Nullable SimpleResearchKey research);
    
    /**
     * Determine if the given research has been known by the player.  If a stage number is defined in the given research
     * key, then the current stage number for the research must equal or exceed that to count.  Otherwise, the player
     * having started the research is sufficient.
     * 
     * @param research a key for the desired research entry
     * @return true if the given research is known, false otherwise
     */
    public boolean isResearchKnown(@Nullable SimpleResearchKey research);
    
    /**
     * Get the current stage number of the given research for the player.
     * 
     * @param research a key for the desired research entry
     * @return if the given research has been started, the current stage number; -1 otherwise
     */
    public int getResearchStage(@Nullable SimpleResearchKey research);
    
    /**
     * Add the given research to the player's known set.
     * 
     * @param research a key for the desired research entry
     * @return true if the research was previously unknown and successfully added, false otherwise
     */
    public boolean addResearch(@Nullable SimpleResearchKey research);
    
    /**
     * Set the current stage number of the given known research for the player.
     * 
     * @param research a key for the desired research entry
     * @param newStage the new stage number for the research
     * @return true if the research was previously known and the new stage number valid, false otherwise
     */
    public boolean setResearchStage(@Nullable SimpleResearchKey research, int newStage);
    
    /**
     * Remove the given research from the player's known set.
     * 
     * @param research a key for the desired research entry
     * @return true if the research was known and successfully removed, false otherwise
     */
    public boolean removeResearch(@Nullable SimpleResearchKey research);
    
    /**
     * Attach the given flag to the given research for the player.
     * 
     * @param research a key for the desired research entry
     * @param flag the flag to be set
     * @return true if the flag was successfully set, false otherwise
     */
    public boolean addResearchFlag(@Nullable SimpleResearchKey research, @Nullable ResearchFlag flag);
    
    /**
     * Detach the given flag from the given research for the player.
     * 
     * @param research a key for the desired research entry
     * @param flag the flag to be removed
     * @return true if the flag was previously set and successfully removed, false otherwise
     */
    public boolean removeResearchFlag(@Nullable SimpleResearchKey research, @Nullable ResearchFlag flag);
    
    /**
     * Determine whether the given flag is attached to the given research for the player.
     * 
     * @param research a key for the desired research entry
     * @param flag the flag to be queried
     * @return true if the flag is attached to the research, false otherwise
     */
    public boolean hasResearchFlag(@Nullable SimpleResearchKey research, @Nullable ResearchFlag flag);
    
    /**
     * Get the set of flags attached to the given research for the player.
     * 
     * @param research a key for the desired research entry
     * @return the set of flags attached to the research
     */
    @Nonnull
    public Set<ResearchFlag> getResearchFlags(@Nullable SimpleResearchKey research);
    
    /**
     * Add the given number of points to the given knowledge type for the player.
     * 
     * @param type the knowledge type to be added to
     * @param amount the number of points to be added
     * @return true if the addition was successful, false otherwise
     */
    public boolean addKnowledge(@Nullable KnowledgeType type, int amount);
    
    /**
     * Get the number of complete levels (not points) of the given knowledge type that the player has.
     * 
     * @param type the knowledge type to be queried
     * @return the number of complete levels for the knowledge type
     */
    public int getKnowledge(@Nullable KnowledgeType type);
    
    /**
     * Get the number of points (not complete levels) of the given knowledge type that the player has.
     * 
     * @param type the knowledge type to be queried
     * @return the number of points for the knowledge type
     */
    public int getKnowledgeRaw(@Nullable KnowledgeType type);
    
    /**
     * Gets the player's currently active theorycrafting research project.
     * 
     * @return the player's currently active theorycrafting research project
     */
    public Project getActiveResearchProject();
    
    /**
     * Sets the player's currently active theorycrafting research project.
     * 
     * @param project the newly active theorycrafting research project
     */
    public void setActiveResearchProject(Project project);
    
    /**
     * Gets the player's last active grimoire research topic.
     * 
     * @return the player's last active grimoire research topic
     */
    public AbstractResearchTopic getLastResearchTopic();
    
    /**
     * Sets the player's last active grimoire research topic.
     * 
     * @param topic the player's last active grimoire research topic
     */
    public void setLastResearchTopic(AbstractResearchTopic topic);
    
    /**
     * Gets the player's grimoire research topic history.
     * 
     * @return the player's grimoire research topic history
     */
    public LinkedList<AbstractResearchTopic> getResearchTopicHistory();
    
    /**
     * Sets the player's grimoire research topic history.
     * 
     * @param history the player's grimoire research topic history
     */
    public void setResearchTopicHistory(List<AbstractResearchTopic> history);
    
    /**
     * Sync the given player's research and knowledge data to the their client.
     * 
     * @param player the player whose client should receive the data
     */
    public void sync(@Nullable ServerPlayer player);
    
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
}

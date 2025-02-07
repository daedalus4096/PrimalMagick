package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.topics.AbstractResearchTopic;
import com.verdantartifice.primalmagick.common.theorycrafting.Project;
import com.verdantartifice.primalmagick.common.util.INBTSerializablePM;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Capability interface for storing research and knowledge.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
public interface IPlayerKnowledge extends INBTSerializablePM<CompoundTag> {
    /**
     * Remove all research from the player.
     */
    void clearResearch();
    
    /**
     * Remove all knowledge from the player.
     */
    void clearKnowledge();
    
    /**
     * Get all research known by the player.
     * 
     * @return a set of keys for all research entries known by the player
     */
    @Nonnull
    Set<AbstractResearchKey<?>> getResearchSet();
    
    /**
     * Get the status of a given research for the player.
     * 
     * @param research a key for the desired research entry
     * @return the current status of the given research entry
     */
    @Nonnull
    ResearchStatus getResearchStatus(@Nonnull RegistryAccess registryAccess, @Nullable AbstractResearchKey<?> research);
    
    /**
     * Determine if the given research has been completed by the player.
     * 
     * @param research a key for the desired research entry
     * @return true if the given research is complete, false otherwise
     */
    boolean isResearchComplete(@Nonnull RegistryAccess registryAccess, @Nullable AbstractResearchKey<?> research);
    
    /**
     * Determine if the given research has been started by the player.
     * 
     * @param research a key for the desired research entry
     * @return true if the given research is started, false otherwise
     */
    boolean isResearchKnown(@Nullable AbstractResearchKey<?> research);
    
    /**
     * Get the current stage number of the given research for the player.
     * 
     * @param research a key for the desired research entry
     * @return if the given research has been started, the current stage number; -1 otherwise
     */
    int getResearchStage(@Nullable AbstractResearchKey<?> research);
    
    /**
     * Add the given research to the player's known set.
     * 
     * @param research a key for the desired research entry
     * @return true if the research was previously unknown and successfully added, false otherwise
     */
    boolean addResearch(@Nullable AbstractResearchKey<?> research);
    
    /**
     * Set the current stage number of the given known research for the player.
     * 
     * @param research a key for the desired research entry
     * @param newStage the new stage number for the research
     * @return true if the research was previously known and the new stage number valid, false otherwise
     */
    boolean setResearchStage(@Nullable AbstractResearchKey<?> research, int newStage);
    
    /**
     * Remove the given research from the player's known set.
     * 
     * @param research a key for the desired research entry
     * @return true if the research was known and successfully removed, false otherwise
     */
    boolean removeResearch(@Nullable AbstractResearchKey<?> research);
    
    /**
     * Attach the given flag to the given research for the player.
     * 
     * @param research a key for the desired research entry
     * @param flag the flag to be set
     * @return true if the flag was successfully set, false otherwise
     */
    boolean addResearchFlag(@Nullable AbstractResearchKey<?> research, @Nullable ResearchFlag flag);
    
    /**
     * Detach the given flag from the given research for the player.
     * 
     * @param research a key for the desired research entry
     * @param flag the flag to be removed
     * @return true if the flag was previously set and successfully removed, false otherwise
     */
    boolean removeResearchFlag(@Nullable AbstractResearchKey<?> research, @Nullable ResearchFlag flag);
    
    /**
     * Determine whether the given flag is attached to the given research for the player.
     * 
     * @param research a key for the desired research entry
     * @param flag the flag to be queried
     * @return true if the flag is attached to the research, false otherwise
     */
    boolean hasResearchFlag(@Nullable AbstractResearchKey<?> research, @Nullable ResearchFlag flag);
    
    /**
     * Get the set of flags attached to the given research for the player.
     * 
     * @param research a key for the desired research entry
     * @return the set of flags attached to the research
     */
    @Nonnull
    Set<ResearchFlag> getResearchFlags(@Nullable AbstractResearchKey<?> research);
    
    /**
     * Add the given number of points to the given knowledge type for the player.
     * 
     * @param type the knowledge type to be added to
     * @param amount the number of points to be added
     * @return true if the addition was successful, false otherwise
     */
    boolean addKnowledge(@Nullable KnowledgeType type, int amount);
    
    /**
     * Get the number of complete levels (not points) of the given knowledge type that the player has.
     * 
     * @param type the knowledge type to be queried
     * @return the number of complete levels for the knowledge type
     */
    int getKnowledge(@Nullable KnowledgeType type);
    
    /**
     * Get the number of points (not complete levels) of the given knowledge type that the player has.
     * 
     * @param type the knowledge type to be queried
     * @return the number of points for the knowledge type
     */
    int getKnowledgeRaw(@Nullable KnowledgeType type);
    
    /**
     * Gets the player's currently active theorycrafting research project.
     * 
     * @return the player's currently active theorycrafting research project
     */
    Project getActiveResearchProject();
    
    /**
     * Sets the player's currently active theorycrafting research project.
     * 
     * @param project the newly active theorycrafting research project
     */
    void setActiveResearchProject(Project project);
    
    /**
     * Gets the player's last active grimoire research topic.
     * 
     * @return the player's last active grimoire research topic
     */
    AbstractResearchTopic<?> getLastResearchTopic();
    
    /**
     * Sets the player's last active grimoire research topic.
     * 
     * @param topic the player's last active grimoire research topic
     */
    void setLastResearchTopic(AbstractResearchTopic<?> topic);
    
    /**
     * Gets the player's grimoire research topic history.
     * 
     * @return the player's grimoire research topic history
     */
    LinkedList<AbstractResearchTopic<?>> getResearchTopicHistory();
    
    /**
     * Sets the player's grimoire research topic history.
     * 
     * @param history the player's grimoire research topic history
     */
    void setResearchTopicHistory(List<AbstractResearchTopic<?>> history);
    
    /**
     * Sync the given player's research and knowledge data to the their client.
     * 
     * @param player the player whose client should receive the data
     */
    void sync(@Nullable ServerPlayer player);
    
    enum ResearchStatus {
        UNKNOWN,
        IN_PROGRESS,
        COMPLETE
    }
    
    enum ResearchFlag {
        NEW,
        UPDATED,
        POPUP
    }
}

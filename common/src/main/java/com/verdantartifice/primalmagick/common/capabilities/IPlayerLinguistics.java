package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.books.ScribeTableMode;
import com.verdantartifice.primalmagick.common.util.INBTSerializablePM;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.joml.Vector2i;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * Capability interface for storing linguistics data.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
public interface IPlayerLinguistics extends INBTSerializablePM<CompoundTag> {
    int MAX_STUDY_COUNT = 3;
    
    /**
     * Remove all linguistics data from the player.
     */
    void clear();
    
    /**
     * Get whether the player has started studying the given language.
     * 
     * @param languageId the language to be queried
     * @return whether the player has started studying the language
     */
    boolean isLanguageKnown(ResourceLocation languageId);
    
    /**
     * Marks a given book in a given language as having been read by the player.
     * 
     * @param bookDefinitionId the book definition to be updated
     * @param languageId the language to be updated
     * @return true if this combination of book and language is new to the player, false otherwise
     */
    boolean markRead(ResourceLocation bookDefinitionId, ResourceLocation languageId);
    
    /**
     * Get the player's comprehension score for the given language.
     * 
     * @param languageId the language to be queried
     * @return the player's comprehension score
     */
    int getComprehension(ResourceLocation languageId);
    
    /**
     * Sets the player's comprehension score for the given language.
     * 
     * @param languageId the language to be updated
     * @param value the new comprehension score value
     */
    void setComprehension(ResourceLocation languageId, int value);
    
    /**
     * Gets the player's vocabulary score for the given language.
     * 
     * @param languageId the language to be queried
     * @return the player's vocabulary score
     */
    int getVocabulary(ResourceLocation languageId);
    
    /**
     * Sets the player's vocabulary score for the given language.
     * 
     * @param languageId the language to be updated
     * @param value the new vocabulary score value
     */
    void setVocabulary(ResourceLocation languageId, int value);
    
    /**
     * Gets the number of times that a given book definition in a given language has been studied for vocabulary.
     * 
     * @param bookDefinitionId the book definition to be queried
     * @param languageId the language to be queried
     * @return the number of times that title has been studied
     */
    int getTimesStudied(ResourceLocation bookDefinitionId, ResourceLocation languageId);
    
    /**
     * Sets the number of times that a given book definition in a given language has been studied for vocabulary.
     * 
     * @param bookDefinitionId the book definition to be updated
     * @param languageId the language to be updated
     * @param value the new study count value
     */
    void setTimesStudied(ResourceLocation bookDefinitionId, ResourceLocation languageId, int value);
    
    /**
     * Gets the current mode being used by scribe tables for the player.
     * 
     * @return the current scribe table mode
     */
    @Nonnull
    ScribeTableMode getScribeTableMode();
    
    /**
     * Sets the current mode being used by scribe tables for the player.
     * 
     * @param mode the new scribe table mode
     */
    void setScribeTableMode(@Nonnull ScribeTableMode mode);
    
    /**
     * Gets an unmodifiable view of the currently unlocked node coordinates for the given grid.  To unlock
     * a new node, use {@link #unlockNode(ResourceLocation, Vector2i)}.
     * 
     * @param gridDefinitionId the grid definition to be queried
     * @return an unmodifiable view of the given grid's unlocked nodes
     */
    Set<Vector2i> getUnlockedNodes(ResourceLocation gridDefinitionId);
    
    /**
     * Clears all unlocked nodes for the given grid.
     * 
     * @param gridDefinitionId the grid definition to be cleared
     */
    void clearUnlockedNodes(ResourceLocation gridDefinitionId);
    
    /**
     * Unlocks a node at the given coordinates for the given grid.  Does *not* perform validity checking,
     * apart from duplicate checking.
     * 
     * @param gridDefinitionId the grid to be modified
     * @param nodePos the coordinates to be unlocked
     * @return true if the node was unlocked, false otherwise (i.e. the node was already unlocked)
     */
    boolean unlockNode(ResourceLocation gridDefinitionId, Vector2i nodePos);
    
    /**
     * Gets the system time at which the player last modified the unlock states of the given grid.
     * 
     * @param gridDefinitionId the grid definition to be queried
     * @return the system time at which the given grid was last modified
     */
    long getGridLastModified(ResourceLocation gridDefinitionId);
    
    /**
     * Sync the given player's linguistics data to the their client.
     * 
     * @param player the player whose client should receive the data
     */
    void sync(@Nullable ServerPlayer player);
}

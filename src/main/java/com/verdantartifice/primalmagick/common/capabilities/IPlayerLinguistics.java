package com.verdantartifice.primalmagick.common.capabilities;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joml.Vector2ic;

import com.verdantartifice.primalmagick.common.books.ScribeTableMode;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Capability interface for storing linguistics data.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
@SuppressWarnings("deprecation")
@AutoRegisterCapability
public interface IPlayerLinguistics extends INBTSerializable<CompoundTag> {
    public static final int MAX_STUDY_COUNT = 3;
    
    /**
     * Remove all linguistics data from the player.
     */
    public void clear();
    
    /**
     * Get whether the player has started studying the given language.
     * 
     * @param languageId the language to be queried
     * @return whether the player has started studying the language
     */
    public boolean isLanguageKnown(ResourceLocation languageId);
    
    /**
     * Marks a given book in a given language as having been read by the player.
     * 
     * @param bookDefinitionId the book definition to be updated
     * @param languageId the language to be updated
     * @return true if this combination of book and language is new to the player, false otherwise
     */
    public boolean markRead(ResourceLocation bookDefinitionId, ResourceLocation languageId);
    
    /**
     * Get the player's comprehension score for the given language.
     * 
     * @param languageId the language to be queried
     * @return the player's comprehension score
     */
    public int getComprehension(ResourceLocation languageId);
    
    /**
     * Sets the player's comprehension score for the given language.
     * 
     * @param languageId the language to be updated
     * @param value the new comprehension score value
     */
    public void setComprehension(ResourceLocation languageId, int value);
    
    /**
     * Gets the player's vocabulary score for the given language.
     * 
     * @param languageId the language to be queried
     * @return the player's vocabulary score
     */
    public int getVocabulary(ResourceLocation languageId);
    
    /**
     * Sets the player's vocabulary score for the given language.
     * 
     * @param languageId the language to be updated
     * @param value the new vocabulary score value
     */
    public void setVocabulary(ResourceLocation languageId, int value);
    
    /**
     * Gets the number of times that a given book definition in a given language has been studied for vocabulary.
     * 
     * @param bookDefinitionId the book definition to be queried
     * @param languageId the language to be queried
     * @return the number of times that title has been studied
     */
    public int getTimesStudied(ResourceLocation bookDefinitionId, ResourceLocation languageId);
    
    /**
     * Sets the number of times that a given book definition in a given language has been studied for vocabulary.
     * 
     * @param bookDefinitionId the book definition to be updated
     * @param languageId the language to be updated
     * @param value the new study count value
     */
    public void setTimesStudied(ResourceLocation bookDefinitionId, ResourceLocation languageId, int value);
    
    /**
     * Gets the current mode being used by scribe tables for the player.
     * 
     * @return the current scribe table mode
     */
    @Nonnull
    public ScribeTableMode getScribeTableMode();
    
    /**
     * Sets the current mode being used by scribe tables for the player.
     * 
     * @param mode the new scribe table mode
     */
    public void setScribeTableMode(@Nonnull ScribeTableMode mode);
    
    /**
     * Gets an unmodifiable view of the currently unlocked node coordinates for the given grid.  To unlock
     * a new node, use {@link #unlockNode(ResourceLocation, Vector2ic)}.
     * 
     * @param gridDefinitionId the grid definition to be queried
     * @return an unmodifiable view of the given grid's unlocked nodes
     */
    public Set<Vector2ic> getUnlockedNodes(ResourceLocation gridDefinitionId);
    
    /**
     * Clears all unlocked nodes for the given grid.
     * 
     * @param gridDefinitionId the grid definition to be cleared
     */
    public void clearUnlockedNodes(ResourceLocation gridDefinitionId);
    
    /**
     * Unlocks a node at the given coordinates for the given grid.  Does *not* perform validity checking,
     * apart from duplicate checking.
     * 
     * @param gridDefinitionId the grid to be modified
     * @param nodePos the coordinates to be unlocked
     * @return true if the node was unlocked, false otherwise (i.e. the node was already unlocked)
     */
    public boolean unlockNode(ResourceLocation gridDefinitionId, Vector2ic nodePos);
    
    /**
     * Gets the system time at which the player last modified the unlock states of the given grid.
     * 
     * @param gridDefinitionId the grid definition to be queried
     * @return the system time at which the given grid was last modified
     */
    public long getGridLastModified(ResourceLocation gridDefinitionId);
    
    /**
     * Sync the given player's linguistics data to the their client.
     * 
     * @param player the player whose client should receive the data
     */
    public void sync(@Nullable ServerPlayer player);
}

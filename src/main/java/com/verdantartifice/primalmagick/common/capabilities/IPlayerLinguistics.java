package com.verdantartifice.primalmagick.common.capabilities;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Capability interface for storing linguistics data.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
public interface IPlayerLinguistics extends INBTSerializable<CompoundTag> {
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
     * Sync the given player's linguistics data to the their client.
     * 
     * @param player the player whose client should receive the data
     */
    public void sync(@Nullable ServerPlayer player);
}

package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.util.INBTSerializablePM;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;

/**
 * Capability interface for storing attunement data.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
public interface IPlayerAttunements extends INBTSerializablePM<Tag> {
    /**
     * Remove all attunement data from the player.
     */
    void clear();
    
    /**
     * Get the stored value of the given attunement for the player.
     * 
     * @param source the source of the attunement to be retrieved
     * @param type the type of the attunement to be retrieved
     * @return the value of the attunement, or zero if not found
     */
    int getValue(@Nullable Source source, @Nullable AttunementType type);
    
    /**
     * Store the given value of the given attunement for the player.
     * 
     * @param source the source of the attunement to be stored
     * @param type the type of the attunement to be stored
     * @param value the value of the attunement to be stored
     */
    void setValue(@Nullable Source source, @Nullable AttunementType type, int value);
    
    /**
     * Get whether the given attunement is suppressed for the player.
     * 
     * @param source the source of the attunement to be queried
     * @return true if the attunement is suppressed for the player, false otherwise
     */
    boolean isSuppressed(@Nullable Source source);
    
    /**
     * Store whether the given attunement should be suppressed for the player.
     * 
     * @param source the source of the attunement to be stored
     * @param value the suppression status to be stored
     */
    void setSuppressed(@Nullable Source source, boolean value);
    
    /**
     * Sync the given player's attunement data to their client.
     * 
     * @param player the player whose client should receive the data
     */
    void sync(@Nullable ServerPlayer player);
}

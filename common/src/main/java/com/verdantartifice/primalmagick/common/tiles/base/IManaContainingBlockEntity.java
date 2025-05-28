package com.verdantartifice.primalmagick.common.tiles.base;

import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import javax.annotation.Nonnull;

/**
 * Base interface for a block entity that can store mana.  The mana is
 * stored internally as "centimana" (hundredths of a mana point).
 * <p>
 * This interface is meant for internal use only.  To query or modify mana levels from
 * another block entity, see the IManaStorage capability.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.capabilities.IManaStorage
 */
public interface IManaContainingBlockEntity {
    /**
     * Get the amount of centimana of the given source stored in this object.
     * 
     * @param source the desired source
     * @return the amount of centimana of the given source stored
     */
    int getMana(@Nonnull Source source);
    
    /**
     * Get all forms of centimana stored in this object.
     * 
     * @return a list of all sources of centimana stored
     */
    @Nonnull
    SourceList getAllMana();
    
    /**
     * Get the maximum amount of centimana storeable in this object.
     * 
     * @return the maximum amount of centimana storeable
     */
    int getMaxMana();
    
    /**
     * Set the current amount of centimana stored in this object of the given source.
     * 
     * @param source the desired source
     * @param amount the new amount of centimana to store
     */
    void setMana(@Nonnull Source source, int amount);
    
    /**
     * Set the current amount of centimana stored in this object for all sources.
     * 
     * @param mana the new amounts of centimana to store
     */
    void setMana(@Nonnull SourceList mana);
}

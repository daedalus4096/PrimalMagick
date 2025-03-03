package com.verdantartifice.primalmagick.common.sources;

import javax.annotation.Nonnull;

/**
 * Base interface for a block, item, or tile entity that can store mana.  The mana is
 * stored internally as "centimana" (hundredths of a mana point).
 * <p>
 * This interface is meant for internal use only.  To query or modify mana levels from
 * another block, item, or tile entity, see the IManaStorage capability.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.capabilities.IManaStorage
 */
public interface IManaContainer {
    /**
     * Get the amount of centimana of the given source stored in this object.
     * 
     * @param source the desired source
     * @return the amount of centimana of the given source stored
     */
    public int getMana(@Nonnull Source source);
    
    /**
     * Get all forms of centimana stored in this object.
     * 
     * @return a list of all sources of centimana stored
     */
    @Nonnull
    public SourceList getAllMana();
    
    /**
     * Get the maximum amount of centimana storeable in this object.
     * 
     * @return the maximum amount of centimana storeable
     */
    public int getMaxMana();
    
    /**
     * Set the current amount of centimana stored in this object of the given source.
     * 
     * @param source the desired source
     * @param amount the new amount of centimana to store
     */
    public void setMana(@Nonnull Source source, int amount);
    
    /**
     * Set the current amount of centimana stored in this object for all sources.
     * 
     * @param mana the new amounts of centimana to store
     */
    public void setMana(@Nonnull SourceList mana);
}

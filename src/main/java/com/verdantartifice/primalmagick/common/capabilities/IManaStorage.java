package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Capability interface for storing mana.  Methods take and return values in centimana.
 * 
 * @author Daedalus4096
 */
public interface IManaStorage extends INBTSerializable<CompoundTag> {
    /**
     * Adds mana of the given source to the storage.  Returns quantity of centimana that was accepted.
     * 
     * @param source source of mana to be inserted
     * @param maxReceive maximum amount of centimana to be inserted
     * @param simulate if {@code true}, the insertion will only be simulated
     * @return amount of centimana that was (or would have been, if simulated) accepted by the storage
     */
    public int receiveMana(Source source, int maxReceive, boolean simulate);
    
    /**
     * Removes mana of the given source from the storage.  Returns quantity of centimana that was removed.
     * 
     * @param source source of mana to be extracted
     * @param maxExtract maximum amount of centimana to be extracted
     * @param simulate if {@code true}, the extraction will only be simulated
     * @return amount of centimana that was (or would have been, if simulated) extracted from the storage
     */
    public int extractMana(Source source, int maxExtract, boolean simulate);
    
    /**
     * Returns the amount of centimana of the given source currently stored.
     * 
     * @param source source of mana to be queried
     * @return the amount of centimana currently stored
     */
    public int getManaStored(Source source);
    
    /**
     * Returns the maxiumum amount of centimana of the given source that can be stored.
     * 
     * @param source source of mana to be queried
     * @return the maximum amount of centimana currently stored
     */
    public int getMaxManaStored(Source source);
    
    /**
     * Returns whether this storage can contain mana of the given source.
     * 
     * @param source source of mana to be queried
     * @return whether this storage can contain the given type of mana
     */
    public boolean canStore(Source source);
    
    /**
     * Returns whether this storage can have mana of the given source extracted.
     * 
     * @param source source of mana to be queried
     * @return whether this storage can have mana extracted
     */
    public boolean canExtract(Source source);
    
    /**
     * Returns whether this storage can have mana of the given source inserted.
     * 
     * @param source source of mana to be queried
     * @return whether this storage can have mana inserted
     */
    public boolean canReceive(Source source);
}

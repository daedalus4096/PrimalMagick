package com.verdantartifice.primalmagick.common.capabilities;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Capability interface for storing mana.  Methods take and return values in centimana.
 * 
 * @author Daedalus4096
 */
public interface IManaStorage<T extends IManaStorage<T>> {
    Codec<T> codec();
    StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec();
    
    /**
     * Adds mana of the given source to the storage.  Returns quantity of centimana that was accepted.
     * 
     * @param source source of mana to be inserted
     * @param maxReceive maximum amount of centimana to be inserted
     * @param simulate if {@code true}, the insertion will only be simulated
     * @return amount of centimana that was (or would have been, if simulated) accepted by the storage
     */
    int receiveMana(Source source, int maxReceive, boolean simulate);
    
    /**
     * Removes mana of the given source from the storage.  Returns quantity of centimana that was removed.
     * 
     * @param source source of mana to be extracted
     * @param maxExtract maximum amount of centimana to be extracted
     * @param simulate if {@code true}, the extraction will only be simulated
     * @return amount of centimana that was (or would have been, if simulated) extracted from the storage
     */
    int extractMana(Source source, int maxExtract, boolean simulate);

    /**
     * Returns the centimana of all sources currently stored.
     *
     * @return the centimana of all sources currently stored
     */
    SourceList getAllManaStored();
    
    /**
     * Returns the amount of centimana of the given source currently stored.
     * 
     * @param source source of mana to be queried
     * @return the amount of centimana currently stored
     */
    int getManaStored(Source source);
    
    /**
     * Returns the maxiumum amount of centimana of the given source that can be stored.
     * 
     * @param source source of mana to be queried
     * @return the maximum amount of centimana currently stored
     */
    int getMaxManaStored(Source source);
    
    /**
     * Returns whether this storage can contain mana of the given source.
     * 
     * @param source source of mana to be queried
     * @return whether this storage can contain the given type of mana
     */
    boolean canStore(Source source);
    
    /**
     * Returns whether this storage can have mana of the given source extracted.
     * 
     * @param source source of mana to be queried
     * @return whether this storage can have mana extracted
     */
    boolean canExtract(Source source);
    
    /**
     * Returns whether this storage can have mana of the given source inserted.
     * 
     * @param source source of mana to be queried
     * @return whether this storage can have mana inserted
     */
    boolean canReceive(Source source);

    /**
     * Returns whether this storage currently contains no mana of any storable sources.
     *
     * @return whether this storage is empty
     */
    default boolean isEmpty() {
        return Sources.stream().filter(this::canStore).allMatch(s -> this.getManaStored(s) == 0);
    }

    /**
     * Returns whether this storage currently contains the maximum allowed amount of mana for all storable sources.
     * Also returns true if no sources are storable.
     *
     * @return whether this storage is full
     */
    default boolean isFull() {
        return Sources.stream().filter(this::canStore).noneMatch(s -> this.getManaStored(s) < this.getMaxManaStored(s));
    }
}

package com.verdantartifice.primalmagic.common.attunements;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

/**
 * Primary access point for attunement-related methods.  As players utilize magic, they gain or
 * (sometimes) lose attunement with that magic's source.  Reaching certain thresholds of attunement
 * value results in the acquisition of certain passive abilities for as long as the player maintains
 * the attunement.  Attunement values may be permanent, induced, or temporary, but the total value
 * is what determines any bonuses received.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.attunements.AttunementType}
 */
public class AttunementManager {
    // Set of unique IDs of players that need their research synced to their client
    private static final Set<UUID> SYNC_SET = ConcurrentHashMap.newKeySet();
    
    public static boolean isSyncScheduled(@Nullable PlayerEntity player) {
        if (player == null) {
            return false;
        } else {
            return SYNC_SET.remove(player.getUniqueID());
        }
    }
    
    public static void scheduleSync(@Nullable PlayerEntity player) {
        if (player != null) {
            SYNC_SET.add(player.getUniqueID());
        }
    }
    
    /**
     * Gets a partial attunement value for the given player.
     * 
     * @param player the player to be queried
     * @param source the source of attunement to be retrieved
     * @param type the type of attunement to be retrieved
     * @return the partial attunement value
     */
    public static int getAttunement(@Nullable PlayerEntity player, @Nullable Source source, @Nullable AttunementType type) {
        if (player != null && source != null && type != null) {
            IPlayerAttunements attunements = PrimalMagicCapabilities.getAttunements(player);
            if (attunements != null) {
                return attunements.getValue(source, type);
            }
        }
        return 0;
    }
    
    /**
     * Gets the total attunement value for the given player.
     * 
     * @param player the player to be queried
     * @param source the source of attunement to be retrieved
     * @return the total attunement value
     */
    public static int getTotalAttunement(@Nullable PlayerEntity player, @Nullable Source source) {
        if (player != null && source != null) {
            IPlayerAttunements attunements = PrimalMagicCapabilities.getAttunements(player);
            if (attunements != null) {
                // Sum up the partial attunement values for each attunement type
                int total = 0;
                for (AttunementType type : AttunementType.values()) {
                    total += attunements.getValue(source, type);
                }
                return total;
            }
        }
        return 0;
    }
    
    /**
     * Sets the partial attunement value for the given player.
     * 
     * @param player the player to be modified
     * @param source the source of attunement to be set
     * @param type the type of attunement to be set
     * @param value the new partial attunement value
     */
    public static void setAttunement(@Nullable PlayerEntity player, @Nullable Source source, @Nullable AttunementType type, int value) {
        if (player instanceof ServerPlayerEntity && source != null && type != null) {
            IPlayerAttunements attunements = PrimalMagicCapabilities.getAttunements(player);
            if (attunements != null) {
                // Set the new value into the player capability
                attunements.setValue(source, type, value);
                scheduleSync(player);
                
                // TODO Determine if any thresholds were passed, either up or down
            }
        }
    }
    
    /**
     * Sets the partial attunement values for the given player.
     * 
     * @param player the player to be modified
     * @param type the type of attunement to be set
     * @param values the new partial attunement values
     */
    public static void setAttunement(@Nullable PlayerEntity player, @Nullable AttunementType type, @Nullable SourceList values) {
        if (values != null && !values.isEmpty()) {
            for (Source source : values.getSources()) {
                setAttunement(player, source, type, values.getAmount(source));
            }
        }
    }
    
    /**
     * Increments the partial attunement value for the given player by the given amount.
     * 
     * @param player the player to be modified
     * @param source the source of attunement to be changed
     * @param type the type of attunement to be changed
     * @param delta the amount of change to apply, may be negative
     */
    public static void incrementAttunement(@Nullable PlayerEntity player, @Nullable Source source, @Nullable AttunementType type, int delta) {
        int oldValue = getAttunement(player, source, type);
        setAttunement(player, source, type, oldValue + delta);
    }
    
    /**
     * Increments the partial attunement value for the given player by one.
     * 
     * @param player the player to be modified
     * @param source the source of attunement to be changed
     * @param type the type of attunement to be changed
     */
    public static void incrementAttunement(@Nullable PlayerEntity player, @Nullable Source source, @Nullable AttunementType type) {
        incrementAttunement(player, source, type, 1);
    }
    
    /**
     * Increments the partial attunement values for the given player by the given amounts.
     * 
     * @param player the player to be modified
     * @param type the type of attunement to be changed
     * @param deltas the amounts of change to apply, may be negative
     */
    public static void incrementAttunement(@Nullable PlayerEntity player, @Nullable AttunementType type, @Nullable SourceList deltas) {
        SourceList newValues = new SourceList();
        for (Source source : deltas.getSources()) {
            int oldValue = getAttunement(player, source, type);
            newValues.add(source, oldValue + deltas.getAmount(source));
        }
        setAttunement(player, type, newValues);
    }
}

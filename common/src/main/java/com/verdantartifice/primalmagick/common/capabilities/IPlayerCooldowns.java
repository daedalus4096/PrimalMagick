package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.util.INBTSerializablePM;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;

/**
 * Capability interface for storing custom cooldowns.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
public interface IPlayerCooldowns extends INBTSerializablePM<CompoundTag> {
    /**
     * Determine if the given cooldown type is active, rendering that ability unusable.
     * 
     * @param type the type of cooldown
     * @return true if the ability is on cooldown and unusable, false otherwise
     */
    boolean isOnCooldown(@Nullable CooldownType type);
    
    /**
     * Determine how much time remains for the given cooldown type.
     * 
     * @param type the type of cooldown
     * @return the remaining cooldown time in milliseconds, or zero if not on cooldown
     */
    long getRemainingCooldown(@Nullable CooldownType type);
    
    /**
     * Set a cooldown of the given type, rendering an ability temporarily unusable.
     * 
     * @param type the type of cooldown
     * @param durationTicks the length of the cooldown to set, in ticks
     */
    void setCooldown(@Nullable CooldownType type, int durationTicks);
    
    /**
     * Reset all active cooldowns.
     */
    void clearCooldowns();
    
    /**
     * Sync the given player's cooldown data to their client.
     * 
     * @param player the player whose client should receive the data
     */
    void sync(@Nullable ServerPlayer player);
    
    enum CooldownType {
        SPELL,
        DEATH_SAVE
    }
}

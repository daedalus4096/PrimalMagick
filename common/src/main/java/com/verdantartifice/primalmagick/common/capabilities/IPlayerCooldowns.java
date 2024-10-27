package com.verdantartifice.primalmagick.common.capabilities;

import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;

/**
 * Capability interface for storing custom cooldowns.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
public interface IPlayerCooldowns {
    /**
     * Determine if the given cooldown type is active, rendering that ability unusable.
     * 
     * @param type the type of cooldown
     * @return true if the ability is on cooldown and unusable, false otherwise
     */
    public boolean isOnCooldown(@Nullable CooldownType type);
    
    /**
     * Determine how much time remains for the given cooldown type.
     * 
     * @param type the type of cooldown
     * @return the remaining cooldown time in milliseconds, or zero if not on cooldown
     */
    public long getRemainingCooldown(@Nullable CooldownType type);
    
    /**
     * Set a cooldown of the given type, rendering an ability temporarily unusable.
     * 
     * @param type the type of cooldown
     * @param durationTicks the length of the cooldown to set, in ticks
     */
    public void setCooldown(@Nullable CooldownType type, int durationTicks);
    
    /**
     * Reset all active cooldowns.
     */
    public void clearCooldowns();
    
    /**
     * Sync the given player's cooldown data to the their client.
     * 
     * @param player the player whose client should receive the data
     */
    public void sync(@Nullable ServerPlayer player);
    
    public enum CooldownType {
        SPELL,
        DEATH_SAVE
    }
}

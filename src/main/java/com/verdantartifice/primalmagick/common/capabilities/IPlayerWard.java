package com.verdantartifice.primalmagick.common.capabilities;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Capability interface for storing player ward data.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
public interface IPlayerWard extends INBTSerializable<CompoundTag> {
    /**
     * Gets the list of equipment slots which count toward player maximum ward.
     * 
     * @return the list of applicable equipment slots
     */
    List<EquipmentSlot> getApplicableSlots();

    /**
     * Gets the player's current ward level, in health points.
     * 
     * @return the player's current ward level
     */
    float getCurrentWard();
    
    /**
     * Gets the player's maximum ward level, in health points.
     * 
     * @return the player's maximum ward level
     */
    float getMaxWard();
    
    /**
     * Sets the player's current ward level, in health points.
     * 
     * @param ward the new current ward level
     */
    void setCurrentWard(float ward);
    
    /**
     * Increments the player's current ward level by the given amount, in health points.
     * 
     * @param delta the amount to increase the player's current ward level
     */
    default void incrementCurrentWard(float delta) {
        this.setCurrentWard(this.getCurrentWard() + delta);
    }
    
    /**
     * Increments the player's current ward level by one, in health points.
     */
    default void incrementCurrentWard() {
        this.incrementCurrentWard(1);
    }
    
    /**
     * Decrements the player's current ward level by the given amount, in health points.
     * 
     * @param delta the amount to decrease the player's current ward level
     */
    default void decrementCurrentWard(float delta) {
        this.incrementCurrentWard(-delta);
    }
    
    /**
     * Decrements the player's current ward level by one, in health points.
     */
    default void decrementCurrentWard() {
        this.decrementCurrentWard(1);
    }
    
    /**
     * Sets the player's maximum ward level, in health points.
     * 
     * @param ward the new maximum ward level
     */
    void setMaxWard(float ward);
    
    /**
     * Gets whether the player's current ward level is regenerating over time.
     * 
     * @return whether the player's current ward level is regenerating
     */
    boolean isRegenerating();
    
    /**
     * Trigger a pause in the regeneration of the player's current ward; will wear off over time.
     */
    void pauseRegeneration();
    
    /**
     * Removes all wards from this player.
     */
    void clear();
    
    /**
     * Sync the given player's ward data to the their client.
     * 
     * @param player the player whose client should receive the data
     */
    void sync(@Nullable ServerPlayer player);
}

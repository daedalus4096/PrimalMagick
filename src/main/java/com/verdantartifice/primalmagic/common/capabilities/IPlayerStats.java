package com.verdantartifice.primalmagic.common.capabilities;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.stats.Stat;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Capability interface for storing mod statistics data.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
public interface IPlayerStats extends INBTSerializable<CompoundNBT> {
    /**
     * Remove all statistics data from the player.
     */
    public void clear();
    
    /**
     * Get the stored value of the given statistic for the player.
     * 
     * @param stat the statistic to be retrieved
     * @return the value of the given statistic, or zero if not found
     */
    public int getValue(@Nullable Stat stat);
    
    /**
     * Store the given value of the given statistic for the player.
     * 
     * @param stat the statistic to be stored
     * @param value the value to be stored
     */
    public void setValue(@Nullable Stat stat, int value);
    
    /**
     * Determine whether the player has discovered the given shrine location.
     * 
     * @param pos the location of the shrine being queried
     * @return true if the shrine has previously been marked as discovered, false otherwise
     */
    public boolean isLocationDiscovered(@Nullable BlockPos pos);
    
    /**
     * Mark the given shrine location as having been discovered by the player.
     * 
     * @param pos the location of the shrine being updated
     */
    public void setLocationDiscovered(@Nullable BlockPos pos);
    
    /**
     * Sync the given player's statistics data to the their client.
     * 
     * @param player the player whose client should receive the data
     */
    public void sync(@Nullable ServerPlayerEntity player);
}

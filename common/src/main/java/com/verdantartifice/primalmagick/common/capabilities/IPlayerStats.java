package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.util.INBTSerializablePM;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;

/**
 * Capability interface for storing mod statistics data.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
public interface IPlayerStats extends INBTSerializablePM<CompoundTag> {
    /**
     * Remove all statistics data from the player.
     */
    void clear();
    
    /**
     * Get the stored value of the given statistic for the player.
     * 
     * @param stat the statistic to be retrieved
     * @return the value of the given statistic, or zero if not found
     */
    int getValue(@Nullable Stat stat);
    
    /**
     * Store the given value of the given statistic for the player.
     * 
     * @param stat the statistic to be stored
     * @param value the value to be stored
     */
    void setValue(@Nullable Stat stat, int value);
    
    /**
     * Determine whether the player has discovered the given shrine location.
     * 
     * @param pos the location of the shrine being queried
     * @return true if the shrine has previously been marked as discovered, false otherwise
     */
    boolean isLocationDiscovered(@Nullable BlockPos pos);
    
    /**
     * Mark the given shrine location as having been discovered by the player.
     * 
     * @param pos the location of the shrine being updated
     */
    void setLocationDiscovered(@Nullable BlockPos pos);
    
    /**
     * Determine whether the player has crafted the given recipe before, for the purposes of bonus expertise.
     * 
     * @param recipeId the ID of the recipe to be queried
     * @return whether the player has crafted the given recipe before
     */
    boolean isRecipeCrafted(@Nullable ResourceLocation recipeId);
    
    /**
     * Determine whether the player has crafted the given recipe group before, for the purposes of bonus expertise.
     * 
     * @param groupId the ID of the recipe group to be queried
     * @return whether the player has crafted any recipe in the given group before
     */
    boolean isRecipeGroupCrafted(@Nullable ResourceLocation groupId);
    
    /**
     * Determine whether the player has runescribed the given enchantment before, for the purposes of bonus expertise.
     * 
     * @param enchantmentId the ID of the enchantment to be queried
     * @return whether the player has runescribed the given enchantment before
     */
    boolean isRuneEnchantmentCrafted(@Nullable ResourceLocation enchantmentId);
    
    /**
     * Mark the given recipe as having been crafted, and thus ineligible for further bonus expertise.
     * 
     * @param recipeId the ID of the recipe to be updated
     */
    void setRecipeCrafted(@Nullable ResourceLocation recipeId);
    
    /**
     * Mark the given recipe group as having been crafted, and thus ineligible for further bonus expertise.
     * 
     * @param groupId the ID of the recipe group to be updated
     */
    void setRecipeGroupCrafted(@Nullable ResourceLocation groupId);
    
    /**
     * Mark the given enchantment as having been runescribed, and thus ineligible for further bonus expertise.
     * 
     * @param enchantmentId the ID of the enchantment to be updated
     */
    void setRuneEnchantmentCrafted(@Nullable ResourceLocation enchantmentId);
    
    /**
     * Sync the given player's statistics data to the their client.
     * 
     * @param player the player whose client should receive the data
     */
    void sync(@Nullable ServerPlayer player);
}

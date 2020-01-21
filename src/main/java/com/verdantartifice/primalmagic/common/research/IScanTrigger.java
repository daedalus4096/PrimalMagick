package com.verdantartifice.primalmagic.common.research;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.IItemProvider;

/**
 * Primary interface for behavior to be triggered upon the successful scan of a block/item in the
 * analysis table or with the arcanometer.
 * 
 * @author Daedalus4096
 */
public interface IScanTrigger {
    /**
     * Determine whether this trigger should be executed for the given block/item.
     * 
     * @param player the player performing the scan
     * @param itemProvider the block/item being scanned
     * @return true if the trigger should be executed, false otherwise
     */
    boolean matches(ServerPlayerEntity player, IItemProvider itemProvider);
    
    /**
     * Execute this trigger's behavior.
     * 
     * @param player the player performing the scan
     * @param itemProvider the block/item being scanned
     */
    void onMatch(ServerPlayerEntity player, IItemProvider itemProvider);
}

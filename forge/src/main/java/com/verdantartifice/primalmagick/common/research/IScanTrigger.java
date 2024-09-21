package com.verdantartifice.primalmagick.common.research;

import net.minecraft.server.level.ServerPlayer;

/**
 * Primary interface for behavior to be triggered upon the successful scan of an object in the
 * analysis table or with the arcanometer.
 * 
 * @author Daedalus4096
 */
public interface IScanTrigger {
    /**
     * Determine whether this trigger should be executed for the given object.
     * 
     * @param player the player performing the scan
     * @param obj the object being scanned
     * @return true if the trigger should be executed, false otherwise
     */
    boolean matches(ServerPlayer player, Object obj);
    
    /**
     * Execute this trigger's behavior.
     * 
     * @param player the player performing the scan
     * @param obj the object being scanned
     */
    void onMatch(ServerPlayer player, Object obj);
}

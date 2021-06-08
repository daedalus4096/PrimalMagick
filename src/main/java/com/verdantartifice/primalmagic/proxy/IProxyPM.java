package com.verdantartifice.primalmagic.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Control interface for the mod's sided proxy.  Available on both client and server sides, with a
 * separate implementation on each side to handle side-specific differences.
 * 
 * @author Daedalus4096
 */
public interface IProxyPM {
    /**
     * Initialize deferred object registries so that they can receive registrations.
     */
    public void initDeferredRegistries();
    
    /**
     * Handle client setup during mod initialization.  See event documentation for more details.
     * 
     * @param event the triggering event
     */
    public void clientSetup(FMLClientSetupEvent event);
    
    /**
     * Determine whether the user is holding down the SHIFT key.  Only ever returns true on the client side.
     * 
     * @return true if this is the client side and the user is holding down SHIFT, false otherwise
     */
    public boolean isShiftDown();
}

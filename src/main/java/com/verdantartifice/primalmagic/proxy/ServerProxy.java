package com.verdantartifice.primalmagic.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Server sided proxy.  Handles server setup issues and provides side-dependent utility methods.
 * 
 * @author Daedalus4096
 */
public class ServerProxy implements IProxyPM {
    @Override
    public void initDeferredRegistries() {}
    
    @Override
    public void clientSetup(FMLClientSetupEvent event) {}

    @Override
    public boolean isShiftDown() {
        return false;
    }
}

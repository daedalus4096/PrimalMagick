package com.verdantartifice.primalmagic.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public interface IProxyPM {
    public void commonSetup(FMLCommonSetupEvent event);
    public void clientSetup(FMLClientSetupEvent event);
    public void serverStarting(FMLServerStartingEvent event);
    
    public boolean isShiftDown();
}

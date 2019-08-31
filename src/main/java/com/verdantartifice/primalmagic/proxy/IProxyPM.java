package com.verdantartifice.primalmagic.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public interface IProxyPM {
    public void preInit(FMLCommonSetupEvent event);
    public void serverStarting(FMLServerStartingEvent event);
}

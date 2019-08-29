package com.verdantartifice.primalmagic.proxy;

import com.verdantartifice.primalmagic.common.init.InitCapabilities;
import com.verdantartifice.primalmagic.common.init.InitResearch;
import com.verdantartifice.primalmagic.common.network.PacketHandler;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonProxy implements IProxyPM {
    @Override
    public void preInit(FMLCommonSetupEvent event) {
        PacketHandler.registerMessages();
        InitCapabilities.initCapabilities();
        InitResearch.initResearch();
    }
}

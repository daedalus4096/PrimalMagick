package com.verdantartifice.primalmagic.proxy;

import com.verdantartifice.primalmagic.common.init.InitCapabilities;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonProxy implements IProxyPM {
    @Override
    public void preInit(FMLCommonSetupEvent event) {
        InitCapabilities.initCapabilities();
    }
}

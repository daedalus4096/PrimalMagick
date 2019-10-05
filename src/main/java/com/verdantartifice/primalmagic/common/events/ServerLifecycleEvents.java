package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class ServerLifecycleEvents {
    @SubscribeEvent
    public static void serverStarting(FMLServerStartingEvent event) {
        PrimalMagic.proxy.serverStarting(event);
    }
}

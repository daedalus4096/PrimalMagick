package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

/**
 * Handlers for server lifecycle related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class ServerLifecycleEvents {
    @SubscribeEvent
    public static void serverStarting(FMLServerStartingEvent event) {
        // Perform server-starting work on the sided proxy
        PrimalMagic.proxy.serverStarting(event);
    }
}

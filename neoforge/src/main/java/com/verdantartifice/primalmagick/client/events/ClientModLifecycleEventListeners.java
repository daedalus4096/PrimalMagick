package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Neoforge listeners for client-only mod lifecycle events for setup.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientModLifecycleEventListeners {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ClientModLifecycleEvents.clientSetup(event::enqueueWork);
    }
}

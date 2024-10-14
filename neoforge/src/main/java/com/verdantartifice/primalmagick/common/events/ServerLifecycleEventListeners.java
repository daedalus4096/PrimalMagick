package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Neoforge listeners for server lifecycle related events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public class ServerLifecycleEventListeners {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ServerLifecycleEvents.onRegisterCommands(event.getDispatcher(), event.getBuildContext());
    }
}

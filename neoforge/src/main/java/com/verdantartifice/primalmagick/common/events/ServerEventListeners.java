package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

/**
 * Neoforge listeners for server related events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public class ServerEventListeners {
    @SubscribeEvent
    public static void serverWorldTick(LevelTickEvent.Post event) {
        ServerEvents.serverLevelTick(event.getLevel());
    }
}

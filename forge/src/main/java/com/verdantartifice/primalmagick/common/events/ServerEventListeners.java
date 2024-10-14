package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge listeners for server related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ServerEventListeners {
    @SubscribeEvent
    public static void serverWorldTick(TickEvent.LevelTickEvent event) {
        if (event.side.isServer() && event.phase != TickEvent.Phase.START) {
            ServerEvents.serverLevelTick(event.level);
        }
    }
}

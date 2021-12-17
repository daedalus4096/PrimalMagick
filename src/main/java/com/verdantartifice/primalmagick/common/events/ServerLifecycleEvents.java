package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.commands.PrimalMagickCommand;

import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for server lifecycle related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID)
public class ServerLifecycleEvents {
    @SubscribeEvent
    public static void serverStarting(ServerStartingEvent event) {
        PrimalMagickCommand.register(event.getServer().getCommands().getDispatcher());
    }
}

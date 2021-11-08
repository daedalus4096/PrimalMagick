package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.PrimalMagic;
import com.verdantartifice.primalmagick.common.commands.PrimalMagicCommand;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;

/**
 * Handlers for server lifecycle related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class ServerLifecycleEvents {
    @SubscribeEvent
    public static void serverStarting(FMLServerStartingEvent event) {
        PrimalMagicCommand.register(event.getServer().getCommands().getDispatcher());
    }
}

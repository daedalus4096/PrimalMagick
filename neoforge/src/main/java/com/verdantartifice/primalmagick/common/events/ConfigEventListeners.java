package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.network.tasks.SyncDatapackDataTaskNeoforge;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;

/**
 * Neoforge listeners for mod configuration events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid= Constants.MOD_ID)
public class ConfigEventListeners {
    @SubscribeEvent
    public static void gatherConfigTasks(RegisterConfigurationTasksEvent event) {
        event.register(new SyncDatapackDataTaskNeoforge());
    }
}

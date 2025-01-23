package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.network.ConfigPacketHandlerNeoforge;
import com.verdantartifice.primalmagick.common.network.tasks.SyncAffinityDataTaskNeoforge;
import com.verdantartifice.primalmagick.common.network.tasks.SyncLinguisticsGridDataTaskNeoforge;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/**
 * Neoforge listeners for mod configuration events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ConfigEventListeners {
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        ConfigPacketHandlerNeoforge.registerPayloadHandlers(event.registrar(ConfigPacketHandlerNeoforge.REGISTRAR_VERSION));
    }

    @SubscribeEvent
    public static void gatherConfigTasks(RegisterConfigurationTasksEvent event) {
        event.register(new SyncAffinityDataTaskNeoforge());
        event.register(new SyncLinguisticsGridDataTaskNeoforge());
    }
}

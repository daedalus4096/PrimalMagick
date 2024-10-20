package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.network.tasks.SyncDatapackDataTaskForge;
import net.minecraftforge.event.network.GatherLoginConfigurationTasksEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge listeners for mod configuration events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid= Constants.MOD_ID)
public class ConfigEventListeners {
    @SubscribeEvent
    public static void gatherConfigTasks(GatherLoginConfigurationTasksEvent event) {
        event.addTask(new SyncDatapackDataTaskForge());
    }
}

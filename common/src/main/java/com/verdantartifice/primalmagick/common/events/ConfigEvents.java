package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.network.tasks.SyncDatapackDataTask;

import net.minecraftforge.event.network.GatherLoginConfigurationTasksEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for mod configuration events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=Constants.MOD_ID)
public class ConfigEvents {
    @SubscribeEvent
    public static void gatherConfigTasks(GatherLoginConfigurationTasksEvent event) {
        event.addTask(new SyncDatapackDataTask());
    }
}

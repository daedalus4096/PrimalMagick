package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.books.grids.GridDefinitionLoader;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class ResourceEventListeners {
    @SubscribeEvent
    public static void onResourceReload(AddServerReloadListenersEvent event) {
        event.addListener(AffinityManager.getOrCreateInstance());
        event.addListener(GridDefinitionLoader.getOrCreateInstance());
    }
}

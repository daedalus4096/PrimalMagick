package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class DatapackEventListeners {
    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        DatapackEvents.onDatapackSync(event::sendRecipes);
    }
}

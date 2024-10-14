package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

/**
 * Neoforge listeners for client-side Forge entity events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientEntityEventListeners {
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        ClientEntityEvents.onEntityJoinLevel(event.getEntity(), event.getLevel());
    }
}

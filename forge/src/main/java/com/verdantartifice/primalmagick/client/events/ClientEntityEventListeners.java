package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge listeners for client-side Forge entity events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientEntityEventListeners {
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        ClientEntityEvents.onEntityJoinLevel(event.getEntity(), event.getLevel());
    }
}

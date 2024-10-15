package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Neoforge listeners for client-only entity renderer registration events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class EntityRendererEventListeners {
    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        EntityRendererEvents.onRegisterEntityRenderers(event::registerEntityRenderer);
    }
    
    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        EntityRendererEvents.onRegisterLayerDefinitions(event::registerLayerDefinition);
    }
}

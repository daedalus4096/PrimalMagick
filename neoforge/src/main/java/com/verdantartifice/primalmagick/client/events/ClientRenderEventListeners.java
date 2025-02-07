package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/**
 * Neoforge listeners for client-only rendering events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientRenderEventListeners {
    @SubscribeEvent
    public static void renderTooltip(ItemTooltipEvent event) {
        ClientRenderEvents.renderTooltip(event.getItemStack(), event.getEntity(), event.getToolTip());
    }
    
    @SubscribeEvent
    public static void onRenderTooltipGatherComponents(RenderTooltipEvent.GatherComponents event) {
        ClientRenderEvents.onRenderTooltipGatherComponents(event.getItemStack(), event.getTooltipElements());
    }
    
    @SubscribeEvent
    public static void onHighlightEntity(RenderHighlightEvent.Entity event) {
        ClientRenderEvents.onHighlightEntity(event.getTarget(), event.getPoseStack(), event.getMultiBufferSource(), event.getDeltaTracker().getGameTimeDeltaPartialTick(true));
    }
}

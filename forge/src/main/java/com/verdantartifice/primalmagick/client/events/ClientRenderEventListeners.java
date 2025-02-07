package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge listeners for client-only rendering events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
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
        ClientRenderEvents.onHighlightEntity(event.getTarget(), event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
    }
}

package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Neoforge listeners for item related events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public class ItemEventListeners {
    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        InteractionResult result = ItemEvents.onItemRightClick(event.getItemStack(), event.getEntity(), event.getLevel());
        if (result.consumesAction()) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }
}

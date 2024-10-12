package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge listeners for item related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
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

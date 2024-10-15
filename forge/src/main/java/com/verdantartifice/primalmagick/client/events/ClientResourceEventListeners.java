package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge listeners for client-only Forge registration events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientResourceEventListeners {
    @SubscribeEvent
    public static void onRecipesUpdated(RecipesUpdatedEvent event) {
        ClientResourceEvents.onRecipesUpdated(event.getRecipeManager());
    }
}

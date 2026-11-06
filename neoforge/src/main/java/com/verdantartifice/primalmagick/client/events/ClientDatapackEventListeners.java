package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientDatapackEventListeners {
    @SubscribeEvent
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        ClientDatapackEvents.onRecipesReceived(event.getRecipeTypes(), event.getRecipeMap());
    }
}

package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.recipe_book.ArcaneSearchRegistry;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-only Forge registration events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID, value=Dist.CLIENT)
public class ClientResourceEvents {
    @SubscribeEvent
    public static void onRecipesUpdated(RecipesUpdatedEvent event) {
        ArcaneSearchRegistry.populate();
    }
}

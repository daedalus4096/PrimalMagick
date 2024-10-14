package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

/**
 * Neoforge listeners for villager related events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid= Constants.MOD_ID)
public class VillagerEventListeners {
    @SubscribeEvent
    public static void onVillagerTradeSetup(VillagerTradesEvent event) {
        VillagerEvents.onVillagerTradeSetup(event.getType(), event.getTrades());
    }
    
    @SubscribeEvent
    public static void onWandererTradeSetup(WandererTradesEvent event) {
        VillagerEvents.onWandererTradeSetup(event.getGenericTrades(), event.getRareTrades());
    }
}

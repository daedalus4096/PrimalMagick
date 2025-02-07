package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge listeners for villager related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
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

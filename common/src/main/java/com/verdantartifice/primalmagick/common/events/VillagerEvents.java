package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.EmeraldForItems;
import net.minecraft.world.entity.npc.VillagerTrades.ItemsForEmeralds;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for villager related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=Constants.MOD_ID)
public class VillagerEvents {
    @SubscribeEvent
    public static void onVillagerTradeSetup(VillagerTradesEvent event) {
        // Allow librarians to trade certain knowledge granting items
        if (VillagerProfession.LIBRARIAN.equals(event.getType())) {
            event.getTrades().get(1).add(new EmeraldForItems(ItemsPM.MYSTICAL_RELIC_FRAGMENT.get(), 3, 12, 2, 1));
            event.getTrades().get(2).add(new ItemsForEmeralds(ItemsPM.OBSERVATION_NOTES.get(), 8, 1, 5));
            event.getTrades().get(3).add(new EmeraldForItems(ItemsPM.MYSTICAL_RELIC.get(), 1, 12, 20, 5));
            event.getTrades().get(4).add(new ItemsForEmeralds(ItemsPM.THEORY_NOTES.get(), 16, 1, 15));
        }
    }
    
    @SubscribeEvent
    public static void onWandererTradeSetup(WandererTradesEvent event) {
        // Allow wandering traders to sell certain knowledge granting items
        event.getGenericTrades().add(new ItemsForEmeralds(ItemsPM.OBSERVATION_NOTES.get(), 8, 1, 5));
        event.getRareTrades().add(new ItemsForEmeralds(ItemsPM.THEORY_NOTES.get(), 16, 1, 15));
    }
}

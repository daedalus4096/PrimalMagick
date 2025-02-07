package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerTrades.EmeraldForItems;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.entity.npc.VillagerTrades.ItemsForEmeralds;

import java.util.List;

/**
 * Handlers for villager related events.
 * 
 * @author Daedalus4096
 */
public class VillagerEvents {
    public static void onVillagerTradeSetup(VillagerProfession type, Int2ObjectMap<List<ItemListing>> trades) {
        // Allow librarians to trade certain knowledge granting items
        if (VillagerProfession.LIBRARIAN.equals(type)) {
            trades.get(1).add(new EmeraldForItems(ItemsPM.MYSTICAL_RELIC_FRAGMENT.get(), 3, 12, 2, 1));
            trades.get(2).add(new ItemsForEmeralds(ItemsPM.OBSERVATION_NOTES.get(), 8, 1, 5));
            trades.get(3).add(new EmeraldForItems(ItemsPM.MYSTICAL_RELIC.get(), 1, 12, 20, 5));
            trades.get(4).add(new ItemsForEmeralds(ItemsPM.THEORY_NOTES.get(), 16, 1, 15));
        }
    }
    
    public static void onWandererTradeSetup(List<VillagerTrades.ItemListing> genericTrades, List<ItemListing> rareTrades) {
        // Allow wandering traders to sell certain knowledge granting items
        genericTrades.add(new ItemsForEmeralds(ItemsPM.OBSERVATION_NOTES.get(), 8, 1, 5));
        rareTrades.add(new ItemsForEmeralds(ItemsPM.THEORY_NOTES.get(), 16, 1, 15));
    }
}

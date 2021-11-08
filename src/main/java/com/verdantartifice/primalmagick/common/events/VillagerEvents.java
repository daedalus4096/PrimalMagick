package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.PrimalMagic;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for villager related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class VillagerEvents {
    @SubscribeEvent
    public static void onVillagerTradeSetup(VillagerTradesEvent event) {
        // Allow librarians to trade certain knowledge granting items
        if (VillagerProfession.LIBRARIAN.equals(event.getType())) {
            event.getTrades().get(1).add(new BasicTrade(new ItemStack(ItemsPM.MYSTICAL_RELIC_FRAGMENT.get(), 3), new ItemStack(Items.EMERALD, 1), 12, 2, 0.05F));
            event.getTrades().get(2).add(new BasicTrade(8, new ItemStack(ItemsPM.OBSERVATION_NOTES.get()), 12, 5, 0.05F));
            event.getTrades().get(3).add(new BasicTrade(new ItemStack(ItemsPM.MYSTICAL_RELIC.get(), 1), new ItemStack(Items.EMERALD, 5), 12, 20, 0.05F));
            event.getTrades().get(4).add(new BasicTrade(16, new ItemStack(ItemsPM.THEORY_NOTES.get()), 12, 15, 0.05F));
        }
    }
    
    @SubscribeEvent
    public static void onWandererTradeSetup(WandererTradesEvent event) {
        // Allow wandering traders to sell certain knowledge granting items
        event.getGenericTrades().add(new BasicTrade(8, new ItemStack(ItemsPM.OBSERVATION_NOTES.get()), 12, 5, 0.05F));
        event.getRareTrades().add(new BasicTrade(16, new ItemStack(ItemsPM.THEORY_NOTES.get()), 12, 15, 0.05F));
    }
}

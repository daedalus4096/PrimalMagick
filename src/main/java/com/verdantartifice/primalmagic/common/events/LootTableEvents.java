package com.verdantartifice.primalmagic.common.events;

import java.util.Arrays;
import java.util.List;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for loot table related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class LootTableEvents {
    protected static final List<String> BLOODY_FLESH_SOURCES = Arrays.asList("minecraft:entities/evoker", "minecraft:entities/illusioner", "minecraft:entities/pillager", "minecraft:entities/player", "minecraft:entities/villager", "minecraft:entities/vindicator", "minecraft:entities/wandering_trader", "minecraft:entities/witch");
    
    @SubscribeEvent
    public static void lootLoad(LootTableLoadEvent event) {
        if (BLOODY_FLESH_SOURCES.contains(event.getName().toString())) {
            // If the fired event is for one of the defined entity types, add a new loot pool to its table.  This pool allows
            // the entity to drop Bloody Flesh, at the same rate that ender pearls drop from Endermen.
            event.getTable().addPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(ItemsPM.BLOODY_FLESH).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(0.0F, 1.0F))).acceptFunction(LootingEnchantBonus.func_215915_a(RandomValueRange.func_215837_a(0.0F, 1.0F)))).build());
        }
    }
}

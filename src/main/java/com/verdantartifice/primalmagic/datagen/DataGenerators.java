package com.verdantartifice.primalmagic.datagen;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

/**
 * Handlers for events related to data file generation.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        // Add all of the mod's data providers to the generator for processing
        DataGenerator generator = event.getGenerator();
        generator.addProvider(new Recipes(generator));
        generator.addProvider(new BlockLootTables(generator));
        generator.addProvider(new BlockTagsProvider(generator));
        generator.addProvider(new ItemTagsProvider(generator));
        generator.addProvider(new AffinityProvider(generator));
    }
}

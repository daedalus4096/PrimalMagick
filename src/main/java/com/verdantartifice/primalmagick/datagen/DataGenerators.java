package com.verdantartifice.primalmagick.datagen;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.datagen.affinities.AffinityProvider;
import com.verdantartifice.primalmagick.datagen.loot_modifiers.LootModifierProvider;
import com.verdantartifice.primalmagick.datagen.loot_tables.BlockLootTables;
import com.verdantartifice.primalmagick.datagen.loot_tables.EntityLootTables;
import com.verdantartifice.primalmagick.datagen.recipes.Recipes;
import com.verdantartifice.primalmagick.datagen.research.ResearchProvider;
import com.verdantartifice.primalmagick.datagen.tags.BiomeTagsProvider;
import com.verdantartifice.primalmagick.datagen.tags.BlockTagsProvider;
import com.verdantartifice.primalmagick.datagen.tags.EntityTypeTagsProvider;
import com.verdantartifice.primalmagick.datagen.tags.ItemTagsProvider;
import com.verdantartifice.primalmagick.datagen.theorycrafting.ProjectProvider;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

/**
 * Handlers for events related to data file generation.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        // Add all of the mod's data providers to the generator for processing
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeServer(), new Recipes(generator));
        generator.addProvider(event.includeServer(), new BlockLootTables(generator));
        generator.addProvider(event.includeServer(), new EntityLootTables(generator));
        generator.addProvider(event.includeServer(), new BlockTagsProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ItemTagsProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new EntityTypeTagsProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new BiomeTagsProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new AffinityProvider(generator));
        generator.addProvider(event.includeServer(), new ResearchProvider(generator));
        generator.addProvider(event.includeServer(), new ProjectProvider(generator));
        generator.addProvider(event.includeServer(), new LootModifierProvider(generator));
    }
}

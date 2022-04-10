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
        generator.addProvider(new Recipes(generator));
        generator.addProvider(new BlockLootTables(generator));
        generator.addProvider(new EntityLootTables(generator));
        generator.addProvider(new BlockTagsProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(new ItemTagsProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(new EntityTypeTagsProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(new BiomeTagsProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(new AffinityProvider(generator));
        generator.addProvider(new ResearchProvider(generator));
        generator.addProvider(new ProjectProvider(generator));
        generator.addProvider(new LootModifierProvider(generator));
    }
}

package com.verdantartifice.primalmagick.datagen;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.datagen.advancements.StoryAdvancementsPM;
import com.verdantartifice.primalmagick.datagen.affinities.AffinityProvider;
import com.verdantartifice.primalmagick.datagen.atlas.SpriteSourceProviderPM;
import com.verdantartifice.primalmagick.datagen.blocks.BlockStateProviderPM;
import com.verdantartifice.primalmagick.datagen.blocks.WandComponentBlockStateProvider;
import com.verdantartifice.primalmagick.datagen.books.StyleGuideProvider;
import com.verdantartifice.primalmagick.datagen.items.ItemModelProviderPM;
import com.verdantartifice.primalmagick.datagen.lang.LanguageProviderEnUs;
import com.verdantartifice.primalmagick.datagen.linguistics.GridDefinitionProvider;
import com.verdantartifice.primalmagick.datagen.loot_modifiers.LootModifierProvider;
import com.verdantartifice.primalmagick.datagen.loot_tables.BlockLootTables;
import com.verdantartifice.primalmagick.datagen.loot_tables.EntityLootTables;
import com.verdantartifice.primalmagick.datagen.loot_tables.LibraryLootTables;
import com.verdantartifice.primalmagick.datagen.loot_tables.TheorycraftingRewardLootTables;
import com.verdantartifice.primalmagick.datagen.loot_tables.TreefolkBarteringLootTables;
import com.verdantartifice.primalmagick.datagen.recipes.Recipes;
import com.verdantartifice.primalmagick.datagen.sounds.SoundDefinitionsProviderPM;
import com.verdantartifice.primalmagick.datagen.tags.BiomeTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.tags.BlockTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.tags.EntityTypeTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.tags.ItemTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.tags.RecipeSerializerTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.tags.SpellPropertyTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.tips.TipDefinitionProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
        generator.addProvider(event.includeClient(), new SpriteSourceProviderPM(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new BlockStateProviderPM(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new WandComponentBlockStateProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new ItemModelProviderPM(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new SoundDefinitionsProviderPM(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new StyleGuideProvider(generator.getPackOutput()));
        generator.addProvider(event.includeClient(), new TipDefinitionProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new Recipes(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), (DataProvider.Factory<LootTableProvider>)(output -> new LootTableProvider(output, Collections.emptySet(), List.of(
                BlockLootTables.getSubProviderEntry(), 
                EntityLootTables.getSubProviderEntry(), 
                TreefolkBarteringLootTables.getSubProviderEntry(), 
                TheorycraftingRewardLootTables.getSubProviderEntry(), 
                LibraryLootTables.getSubProviderEntry()))));
        BlockTagsProviderPM blockTagsProvider = new BlockTagsProviderPM(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new ItemTagsProviderPM(generator.getPackOutput(), event.getLookupProvider(), blockTagsProvider.contentsGetter(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new EntityTypeTagsProviderPM(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new BiomeTagsProviderPM(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new SpellPropertyTagsProviderPM(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new RecipeSerializerTagsProviderPM(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new AffinityProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new LootModifierProvider(generator.getPackOutput()));
        CompletableFuture<HolderLookup.Provider> intermediate = DualRegistryDataGenerator.addProviders(event.includeServer(), generator, generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), new ForgeAdvancementProvider(generator.getPackOutput(), intermediate, event.getExistingFileHelper(), List.of(
                new StoryAdvancementsPM())));
        CompletableFuture<HolderLookup.Provider> registryLookupFuture = RegistryDataGenerator.addProviders(event.includeServer(), generator, generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), new GridDefinitionProvider(generator.getPackOutput(), registryLookupFuture));
        generator.addProvider(event.includeClient(), new LanguageProviderEnUs(generator.getPackOutput(), registryLookupFuture));
    }
}

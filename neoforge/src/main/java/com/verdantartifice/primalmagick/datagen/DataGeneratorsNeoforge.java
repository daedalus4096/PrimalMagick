package com.verdantartifice.primalmagick.datagen;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.datagen.advancements.StoryAdvancementsProviderNeoforge;
import com.verdantartifice.primalmagick.datagen.affinities.AffinityProvider;
import com.verdantartifice.primalmagick.datagen.atlas.SpriteSourceProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.blocks.BlockStateProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.books.StyleGuideProvider;
import com.verdantartifice.primalmagick.datagen.items.ItemModelProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.lang.LanguageProviderEnUs;
import com.verdantartifice.primalmagick.datagen.linguistics.GridDefinitionProvider;
import com.verdantartifice.primalmagick.datagen.loot_modifiers.LootModifierProviderNeoforge;
import com.verdantartifice.primalmagick.datagen.loot_tables.BlockLootTables;
import com.verdantartifice.primalmagick.datagen.loot_tables.EntityLootTables;
import com.verdantartifice.primalmagick.datagen.loot_tables.LibraryLootTables;
import com.verdantartifice.primalmagick.datagen.loot_tables.TheorycraftingRewardLootTables;
import com.verdantartifice.primalmagick.datagen.loot_tables.TreefolkBarteringLootTables;
import com.verdantartifice.primalmagick.datagen.recipes.RecipesNeoforge;
import com.verdantartifice.primalmagick.datagen.sounds.SoundDefinitionsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.BiomeTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.BlockTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.EnchantmentTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.tags.EntityTypeTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.ItemTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.RecipeSerializerTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.SpellPropertyTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tips.TipDefinitionProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Handlers for events related to data file generation.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGeneratorsNeoforge {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        // Add all of the mod's data providers to the generator for processing
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> intermediate = DualRegistryDataGeneratorNeoforge.addProviders(event.includeServer(), generator, generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
        CompletableFuture<HolderLookup.Provider> registryLookupFuture = RegistryDataGeneratorNeoforge.addProviders(event.includeServer(), generator, generator.getPackOutput(), intermediate, event.getExistingFileHelper());
        generator.addProvider(event.includeClient(), new SpriteSourceProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new BlockStateProviderPMNeoforge(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new ItemModelProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new SoundDefinitionsProviderPMNeoforge(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new StyleGuideProvider(generator.getPackOutput()));
        generator.addProvider(event.includeClient(), new TipDefinitionProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new RecipesNeoforge(generator.getPackOutput(), registryLookupFuture));
        BlockTagsProviderPMNeoforge blockTagsProvider = new BlockTagsProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new ItemTagsProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture, blockTagsProvider.contentsGetter(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new EntityTypeTagsProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new BiomeTagsProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new SpellPropertyTagsProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new RecipeSerializerTagsProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new EnchantmentTagsProviderPM(generator.getPackOutput(), registryLookupFuture));
        generator.addProvider(event.includeServer(), new AffinityProvider(generator.getPackOutput(), registryLookupFuture));
        generator.addProvider(event.includeServer(), new LootModifierProviderNeoforge(generator.getPackOutput(), registryLookupFuture));
        generator.addProvider(event.includeServer(), new GridDefinitionProvider(generator.getPackOutput(), registryLookupFuture));
        generator.addProvider(event.includeServer(), new StoryAdvancementsProviderNeoforge(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), (DataProvider.Factory<LootTableProvider>)(output -> new LootTableProvider(output, Collections.emptySet(), List.of(
                BlockLootTables.getSubProviderEntry(), 
                EntityLootTables.getSubProviderEntry(), 
                TreefolkBarteringLootTables.getSubProviderEntry(), 
                TheorycraftingRewardLootTables.getSubProviderEntry(), 
                LibraryLootTables.getSubProviderEntry()),
            registryLookupFuture)));
        generator.addProvider(event.includeClient(), new LanguageProviderEnUs(generator.getPackOutput(), registryLookupFuture));
    }
}

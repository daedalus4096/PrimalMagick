package com.verdantartifice.primalmagick.datagen;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.datagen.advancements.StoryAdvancementsPMForge;
import com.verdantartifice.primalmagick.datagen.affinities.AffinityProvider;
import com.verdantartifice.primalmagick.datagen.atlas.SpriteSourceProviderPMForge;
import com.verdantartifice.primalmagick.datagen.blocks.BlockStateProviderPMForge;
import com.verdantartifice.primalmagick.datagen.books.StyleGuideProvider;
import com.verdantartifice.primalmagick.datagen.items.ItemModelProviderPMForge;
import com.verdantartifice.primalmagick.datagen.lang.LanguageProviderEnUs;
import com.verdantartifice.primalmagick.datagen.linguistics.GridDefinitionProvider;
import com.verdantartifice.primalmagick.datagen.loot_modifiers.LootModifierProvider;
import com.verdantartifice.primalmagick.datagen.loot_tables.BlockLootTables;
import com.verdantartifice.primalmagick.datagen.loot_tables.EntityLootTables;
import com.verdantartifice.primalmagick.datagen.loot_tables.LibraryLootTables;
import com.verdantartifice.primalmagick.datagen.loot_tables.TheorycraftingRewardLootTables;
import com.verdantartifice.primalmagick.datagen.loot_tables.TreefolkBarteringLootTables;
import com.verdantartifice.primalmagick.datagen.recipes.Recipes;
import com.verdantartifice.primalmagick.datagen.sounds.SoundDefinitionsProviderPMForge;
import com.verdantartifice.primalmagick.datagen.tags.BiomeTagsProviderPMForge;
import com.verdantartifice.primalmagick.datagen.tags.BlockTagsProviderPMForge;
import com.verdantartifice.primalmagick.datagen.tags.EnchantmentTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.tags.EntityTypeTagsProviderPMForge;
import com.verdantartifice.primalmagick.datagen.tags.ItemTagsProviderPMForge;
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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Handlers for events related to data file generation.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneratorsForge {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        // Add all of the mod's data providers to the generator for processing
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> intermediate = DualRegistryDataGenerator.addProviders(event.includeServer(), generator, generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
        CompletableFuture<HolderLookup.Provider> registryLookupFuture = RegistryDataGenerator.addProviders(event.includeServer(), generator, generator.getPackOutput(), intermediate, event.getExistingFileHelper());
        generator.addProvider(event.includeClient(), new SpriteSourceProviderPMForge(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new BlockStateProviderPMForge(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new ItemModelProviderPMForge(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new SoundDefinitionsProviderPMForge(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new StyleGuideProvider(generator.getPackOutput()));
        generator.addProvider(event.includeClient(), new TipDefinitionProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new Recipes(generator.getPackOutput(), registryLookupFuture));
        BlockTagsProviderPMForge blockTagsProvider = new BlockTagsProviderPMForge(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new ItemTagsProviderPMForge(generator.getPackOutput(), registryLookupFuture, blockTagsProvider.contentsGetter(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new EntityTypeTagsProviderPMForge(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new BiomeTagsProviderPMForge(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new SpellPropertyTagsProviderPM(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new RecipeSerializerTagsProviderPM(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new EnchantmentTagsProviderPM(generator.getPackOutput(), registryLookupFuture));
        generator.addProvider(event.includeServer(), new AffinityProvider(generator.getPackOutput(), registryLookupFuture));
        generator.addProvider(event.includeServer(), new LootModifierProvider(generator.getPackOutput(), registryLookupFuture));
        generator.addProvider(event.includeServer(), new GridDefinitionProvider(generator.getPackOutput(), registryLookupFuture));
        generator.addProvider(event.includeServer(), new ForgeAdvancementProvider(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper(), List.of(
                new StoryAdvancementsPMForge())));
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

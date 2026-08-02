package com.verdantartifice.primalmagick.datagen;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.datagen.advancements.StoryAdvancementsProviderNeoforge;
import com.verdantartifice.primalmagick.datagen.affinities.AffinityProvider;
import com.verdantartifice.primalmagick.datagen.atlas.SpriteSourceProviderPMNeoforge;
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
import com.verdantartifice.primalmagick.datagen.models.EquipmentAssetProviderPM;
import com.verdantartifice.primalmagick.datagen.models.ModelProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.recipes.RecipesNeoforge;
import com.verdantartifice.primalmagick.datagen.sounds.SoundDefinitionsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.BiomeTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.BlockTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.EnchantmentTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.tags.EnchantmentTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.EntityTypeTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.ItemTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.MobEffectTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.RecipeSerializerTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.SpellPropertyTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.VillagerTradesTagsProviderPM;
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
@EventBusSubscriber(modid = Constants.MOD_ID)
public class DataGeneratorsNeoforge {
    @SubscribeEvent
    public static void onGatherClientData(GatherDataEvent.Client event) {
        // Add all the mod's client data providers to the generator for processing
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> intermediate = DualRegistryDataGeneratorNeoforge.addProviders(false, generator, generator.getPackOutput(), event.getLookupProvider());
        CompletableFuture<HolderLookup.Provider> registryLookupFuture = RegistryDataGeneratorNeoforge.addProviders(false, generator, generator.getPackOutput(), intermediate);
        event.addProvider(new SpriteSourceProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture));
        event.addProvider(new ItemModelProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture, event.getExistingFileHelper()));
        event.addProvider(new SoundDefinitionsProviderPMNeoforge(generator.getPackOutput()));
        event.addProvider(new StyleGuideProvider(generator.getPackOutput()));
        event.addProvider(new ModelProviderPMNeoforge(generator.getPackOutput(), Constants.MOD_ID));
        event.addProvider(new EquipmentAssetProviderPM(generator.getPackOutput()));
        event.addProvider(new LanguageProviderEnUs(generator.getPackOutput(), registryLookupFuture));
    }
    
    @SubscribeEvent
    public static void onGatherServerData(GatherDataEvent.Server event) {
        // Add all the mod's server data providers to the generator for processing
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> intermediate = DualRegistryDataGeneratorNeoforge.addProviders(true, generator, generator.getPackOutput(), event.getLookupProvider());
        CompletableFuture<HolderLookup.Provider> registryLookupFuture = RegistryDataGeneratorNeoforge.addProviders(true, generator, generator.getPackOutput(), intermediate);
        event.addProvider(new RecipesNeoforge.Runner(generator.getPackOutput(), registryLookupFuture));
        event.addProvider(new BlockTagsProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture));
        event.addProvider(new ItemTagsProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture));
        event.addProvider(new EntityTypeTagsProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture));
        event.addProvider(new BiomeTagsProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture));
        event.addProvider(new SpellPropertyTagsProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture));
        event.addProvider(new RecipeSerializerTagsProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture));
        event.addProvider(new MobEffectTagsProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture));
        event.addProvider(new EnchantmentTagsProviderPM(generator.getPackOutput(), registryLookupFuture));
        event.addProvider(new VillagerTradesTagsProviderPM(generator.getPackOutput(), registryLookupFuture));
        event.addProvider(new EnchantmentTagsProviderPMNeoforge(generator.getPackOutput(), registryLookupFuture));
        event.addProvider(new AffinityProvider(generator.getPackOutput(), registryLookupFuture));
        event.addProvider(new LootModifierProviderNeoforge(generator.getPackOutput(), registryLookupFuture));
        event.addProvider(new GridDefinitionProvider(generator.getPackOutput(), registryLookupFuture));
        event.addProvider(new StoryAdvancementsProviderNeoforge(generator.getPackOutput(), registryLookupFuture));
        generator.addProvider(true, (DataProvider.Factory<LootTableProvider>)(output -> new LootTableProvider(output, Collections.emptySet(), List.of(
                BlockLootTables.getSubProviderEntry(),
                EntityLootTables.getSubProviderEntry(),
                TreefolkBarteringLootTables.getSubProviderEntry(),
                TheorycraftingRewardLootTables.getSubProviderEntry(),
                LibraryLootTables.getSubProviderEntry()),
                registryLookupFuture)));
    }
}

package com.verdantartifice.primalmagick.datagen;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.armortrim.TrimMaterialsPM;
import com.verdantartifice.primalmagick.common.armortrim.TrimPatternsPM;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.books.CulturesPM;
import com.verdantartifice.primalmagick.common.damagesource.DamageTypesPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.theorycrafting.ProjectTemplates;
import com.verdantartifice.primalmagick.common.worldgen.biome_modifiers.BiomeModifiersPMNeoforge;
import com.verdantartifice.primalmagick.common.worldgen.features.ConfiguredFeaturesPM;
import com.verdantartifice.primalmagick.common.worldgen.features.PlacedFeaturesPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructureSetsPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructuresPM;
import com.verdantartifice.primalmagick.datagen.tags.BookLanguageTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.DamageTypeTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.ResearchEntryTagsProviderPMNeoforge;
import com.verdantartifice.primalmagick.datagen.tags.StructureTagsProviderPMNeoforge;
import net.minecraft.core.Cloner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryDataLoader;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Generates datapack JSON for the Primal Magick mod.
 * 
 * @author Daedalus4096
 */
public class RegistryDataGeneratorNeoforge extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, context -> { ConfiguredFeaturesPM.bootstrap(context); })    // FIXME Compile error when just using ConfiguredFeaturesPM::bootstrap for some reason
            .add(Registries.PLACED_FEATURE, PlacedFeaturesPM::bootstrap)
            .add(Registries.STRUCTURE, StructuresPM::bootstrap)
            .add(Registries.STRUCTURE_SET, StructureSetsPM::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifiersPMNeoforge::bootstrap)
            .add(Registries.TRIM_MATERIAL, TrimMaterialsPM::bootstrap)
            .add(Registries.TRIM_PATTERN, TrimPatternsPM::bootstrap)
            .add(Registries.DAMAGE_TYPE, DamageTypesPM::bootstrap)
            .add(Registries.ENCHANTMENT, EnchantmentsPM::bootstrap)
            .add(RegistryKeysPM.RESEARCH_DISCIPLINES, ResearchDisciplines::bootstrap)
            .add(RegistryKeysPM.RESEARCH_ENTRIES, ResearchEntries::bootstrap)
            .add(RegistryKeysPM.PROJECT_TEMPLATES, ProjectTemplates::bootstrap)
            .add(RegistryKeysPM.BOOKS, BooksPM::bootstrap)
            .add(RegistryKeysPM.BOOK_LANGUAGES, BookLanguagesPM::bootstrap)
            .add(RegistryKeysPM.CULTURES, CulturesPM::bootstrap);

    // Use addProviders() instead
    private RegistryDataGeneratorNeoforge(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of(Constants.MOD_ID));
    }
    
    public static CompletableFuture<HolderLookup.Provider> addProviders(boolean isServer, DataGenerator generator, PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        RegistryDataGeneratorNeoforge registryDataGenerator = generator.addProvider(isServer, new RegistryDataGeneratorNeoforge(output, provider));
        // TODO Move to DataGenerators once Forge allows tagging datapack registries
        generator.addProvider(isServer, new DamageTypeTagsProviderPMNeoforge(output, provider.thenApply(r -> append(r, BUILDER)), helper));
        generator.addProvider(isServer, new StructureTagsProviderPMNeoforge(output, provider.thenApply(r -> append(r, BUILDER)), helper));
        generator.addProvider(isServer, new BookLanguageTagsProviderPMNeoforge(output, provider.thenApply(r -> append(r, BUILDER)), helper));
        generator.addProvider(isServer, new ResearchEntryTagsProviderPMNeoforge(output, provider.thenApply(r -> append(r, BUILDER)), helper));
        return registryDataGenerator.getRegistryProvider();
    }
    
    private static HolderLookup.Provider append(HolderLookup.Provider original, RegistrySetBuilder builder) {
        Cloner.Factory clonerFactory = new Cloner.Factory();
        Stream<RegistryDataLoader.RegistryData<?>> worldgenAndDimensionStream = Stream.concat(RegistryDataLoader.WORLDGEN_REGISTRIES.stream(), RegistryDataLoader.DIMENSION_REGISTRIES.stream());
        worldgenAndDimensionStream.forEach(registryData -> registryData.runWithArguments(clonerFactory::addCodec));
        return builder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original, clonerFactory).full();
    }

    @Override
    public String getName() {
        return "Mod-specific Datapack Registries";
    }
}

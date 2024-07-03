package com.verdantartifice.primalmagick.datagen;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.PrimalMagick;
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
import com.verdantartifice.primalmagick.common.worldgen.biome_modifiers.BiomeModifiersPM;
import com.verdantartifice.primalmagick.common.worldgen.features.ConfiguredFeaturesPM;
import com.verdantartifice.primalmagick.common.worldgen.features.PlacedFeaturesPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructureSetsPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructuresPM;
import com.verdantartifice.primalmagick.datagen.tags.BookLanguageTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.tags.DamageTypeTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.tags.ResearchEntryTagsProviderPM;
import com.verdantartifice.primalmagick.datagen.tags.StructureTagsProviderPM;

import net.minecraft.core.Cloner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Generates datapack JSON for the Primal Magick mod.
 * 
 * @author Daedalus4096
 */
public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, context -> { ConfiguredFeaturesPM.bootstrap(context); })    // FIXME Compile error when just using ConfiguredFeaturesPM::bootstrap for some reason
            .add(Registries.PLACED_FEATURE, PlacedFeaturesPM::bootstrap)
            .add(Registries.STRUCTURE, StructuresPM::bootstrap)
            .add(Registries.STRUCTURE_SET, StructureSetsPM::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifiersPM::bootstrap)
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
    private RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of(PrimalMagick.MODID));
    }
    
    public static CompletableFuture<HolderLookup.Provider> addProviders(boolean isServer, DataGenerator generator, PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        RegistryDataGenerator registryDataGenerator = generator.addProvider(isServer, new RegistryDataGenerator(output, provider));
        // TODO Move to DataGenerators once Forge allows tagging datapack registries
        generator.addProvider(isServer, new DamageTypeTagsProviderPM(output, provider.thenApply(r -> append(r, BUILDER)), helper));
        generator.addProvider(isServer, new StructureTagsProviderPM(output, provider.thenApply(r -> append(r, BUILDER)), helper));
        generator.addProvider(isServer, new BookLanguageTagsProviderPM(output, provider.thenApply(r -> append(r, BUILDER)), helper));
        generator.addProvider(isServer, new ResearchEntryTagsProviderPM(output, provider.thenApply(r -> append(r, BUILDER)), helper));
        return registryDataGenerator.getRegistryProvider();
    }
    
    private static HolderLookup.Provider append(HolderLookup.Provider original, RegistrySetBuilder builder) {
        Cloner.Factory clonerFactory = new Cloner.Factory();
        RegistryDataLoader.getWorldGenAndDimensionStream().forEach(registryData -> registryData.runWithArguments(clonerFactory::addCodec));
        return builder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original, clonerFactory).full();
    }

    @Override
    public String getName() {
        return "Mod-specific Datapack Registries";
    }
}

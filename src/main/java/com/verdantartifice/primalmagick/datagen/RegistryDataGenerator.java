package com.verdantartifice.primalmagick.datagen;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.armortrim.TrimMaterialsPM;
import com.verdantartifice.primalmagick.common.armortrim.TrimPatternsPM;
import com.verdantartifice.primalmagick.common.damagesource.DamageTypesPM;
import com.verdantartifice.primalmagick.common.worldgen.biome_modifiers.BiomeModifiersPM;
import com.verdantartifice.primalmagick.common.worldgen.features.ConfiguredFeaturesPM;
import com.verdantartifice.primalmagick.common.worldgen.features.PlacedFeaturesPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructureSetsPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructuresPM;
import com.verdantartifice.primalmagick.datagen.tags.DamageTypeTagsProviderPM;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, context -> { ConfiguredFeaturesPM.bootstrap(context); })    // FIXME Compile error when just using ConfiguredFeaturesPM::bootstrap for some reason
            .add(Registries.PLACED_FEATURE, PlacedFeaturesPM::bootstrap)
            .add(Registries.STRUCTURE, StructuresPM::bootstrap)
            .add(Registries.STRUCTURE_SET, StructureSetsPM::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifiersPM::bootstrap)
            .add(Registries.TRIM_MATERIAL, TrimMaterialsPM::bootstrap)
            .add(Registries.TRIM_PATTERN, TrimPatternsPM::bootstrap)
            .add(Registries.DAMAGE_TYPE, DamageTypesPM::bootstrap);
    
    // Use addProviders() instead
    private RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of(PrimalMagick.MODID));
    }
    
    public static void addProviders(boolean isServer, DataGenerator generator, PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        generator.addProvider(isServer, new RegistryDataGenerator(output, provider));
        // TODO Move to DataGenerators once Forge allows tagging custom registries
        generator.addProvider(isServer, new DamageTypeTagsProviderPM(output, provider.thenApply(r -> append(r, BUILDER)), helper));
    }
    
    private static HolderLookup.Provider append(HolderLookup.Provider original, RegistrySetBuilder builder) {
        return builder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original);
    }
}

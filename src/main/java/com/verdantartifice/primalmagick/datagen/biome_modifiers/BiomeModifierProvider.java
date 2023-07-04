package com.verdantartifice.primalmagick.datagen.biome_modifiers;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.worldgen.features.PlacedFeaturesPM;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeModifierProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final PackOutput packOutput;
    protected final CompletableFuture<HolderLookup.Provider> registries;

    public BiomeModifierProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        this.packOutput = packOutput;
        this.registries = registries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return this.registries.thenCompose((registryLookup) -> {
            final RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, registryLookup);
            Path path = this.packOutput.getOutputFolder();
            ResourceLocation registryLoc = ForgeRegistries.Keys.BIOME_MODIFIERS.location();
            Map<ResourceLocation, BiomeModifier> map = new HashMap<>();
            this.registerModifiers(ops, (loc, modifier) -> {
                if (map.put(loc, modifier) != null) {
                    LOGGER.debug("Duplicate biome modifier in data generation: {}", loc.toString());
                }
            });
            for (Map.Entry<ResourceLocation, BiomeModifier> entry : map.entrySet()) {
                Path modPath = path.resolve(String.join("/", PackType.SERVER_DATA.getDirectory(), entry.getKey().getNamespace(), registryLoc.getNamespace(), registryLoc.getPath(), entry.getKey().getPath() + ".json"));
                this.saveModifier(cache, ops, entry.getKey(), entry.getValue(), modPath);
            }
            return CompletableFuture.completedFuture(null);
        });
    }
    
    private void saveModifier(CachedOutput cache, RegistryOps<JsonElement> ops, ResourceLocation loc, BiomeModifier modifier, Path path) {
        BiomeModifier.DIRECT_CODEC.encodeStart(ops, modifier)
            .resultOrPartial(msg -> LOGGER.error("Failed to encode {}: {}", path, msg))
            .ifPresent(json -> {
                DataProvider.saveStable(cache, json, path);
            });
    }
    
    protected void registerModifiers(RegistryOps<JsonElement> ops, BiConsumer<ResourceLocation, BiomeModifier> consumer) {
        // Register ore placement biome modifiers
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_ore_marble_raw_upper"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                ops.getter(Registries.BIOME).get().getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(ops.getter(Registries.PLACED_FEATURE).get().getOrThrow(PlacedFeaturesPM.ORE_MARBLE_RAW_UPPER)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_ore_marble_raw_lower"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                ops.getter(Registries.BIOME).get().getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(ops.getter(Registries.PLACED_FEATURE).get().getOrThrow(PlacedFeaturesPM.ORE_MARBLE_RAW_LOWER)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_ore_rock_salt_upper"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                ops.getter(Registries.BIOME).get().getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(ops.getter(Registries.PLACED_FEATURE).get().getOrThrow(PlacedFeaturesPM.ORE_ROCK_SALT_UPPER)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_ore_rock_salt_lower"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                ops.getter(Registries.BIOME).get().getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(ops.getter(Registries.PLACED_FEATURE).get().getOrThrow(PlacedFeaturesPM.ORE_ROCK_SALT_LOWER)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_ore_quartz_upper"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                ops.getter(Registries.BIOME).get().getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(ops.getter(Registries.PLACED_FEATURE).get().getOrThrow(PlacedFeaturesPM.ORE_QUARTZ_UPPER)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_ore_quartz_lower"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                ops.getter(Registries.BIOME).get().getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(ops.getter(Registries.PLACED_FEATURE).get().getOrThrow(PlacedFeaturesPM.ORE_QUARTZ_LOWER)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
        
        // Register vegetation placement biome modifiers
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_trees_wild_sunwood"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                ops.getter(Registries.BIOME).get().getOrThrow(BiomeTags.IS_FOREST),
                HolderSet.direct(ops.getter(Registries.PLACED_FEATURE).get().getOrThrow(PlacedFeaturesPM.TREE_WILD_SUNWOOD)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_trees_wild_moonwood"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                ops.getter(Registries.BIOME).get().getOrThrow(BiomeTags.IS_FOREST),
                HolderSet.direct(ops.getter(Registries.PLACED_FEATURE).get().getOrThrow(PlacedFeaturesPM.TREE_WILD_MOONWOOD)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
        
        // Register mob spawn biome modifiers
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_spawn_treefolk"), ForgeBiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
                ops.getter(Registries.BIOME).get().getOrThrow(BiomeTags.IS_FOREST),
                new MobSpawnSettings.SpawnerData(EntityTypesPM.TREEFOLK.get(), 100, 1, 3)
        ));
    }

    @Override
    public String getName() {
        return "Primal Magick Biome Modifiers";
    }
}

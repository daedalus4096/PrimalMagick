package com.verdantartifice.primalmagick.datagen.biome_modifiers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
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
    protected final DataGenerator generator;

    public BiomeModifierProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void run(CachedOutput cache) throws IOException {
        final RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.BUILTIN.get());
        Path path = this.generator.getOutputFolder();
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
    }
    
    private void saveModifier(CachedOutput cache, RegistryOps<JsonElement> ops, ResourceLocation loc, BiomeModifier modifier, Path path) {
        BiomeModifier.DIRECT_CODEC.encodeStart(ops, modifier)
            .resultOrPartial(msg -> LOGGER.error("Failed to encode {}: {}", path, msg))
            .ifPresent(json -> {
                try {
                    DataProvider.saveStable(cache, json, path);
                } catch (IOException e) {
                    LOGGER.error("Failed to save {}", path, e);
                }
            });
    }
    
    protected void registerModifiers(RegistryOps<JsonElement> ops, BiConsumer<ResourceLocation, BiomeModifier> consumer) {
        // Register ore placement biome modifiers
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_ore_marble_raw_upper"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_OVERWORLD),
                HolderSet.direct(ops.registry(Registry.PLACED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(PrimalMagick.MODID, "ore_marble_raw_upper")))),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_ore_marble_raw_lower"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_OVERWORLD),
                HolderSet.direct(ops.registry(Registry.PLACED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(PrimalMagick.MODID, "ore_marble_raw_lower")))),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_ore_rock_salt_upper"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_OVERWORLD),
                HolderSet.direct(ops.registry(Registry.PLACED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(PrimalMagick.MODID, "ore_rock_salt_upper")))),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_ore_rock_salt_lower"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_OVERWORLD),
                HolderSet.direct(ops.registry(Registry.PLACED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(PrimalMagick.MODID, "ore_rock_salt_lower")))),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_ore_quartz"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_OVERWORLD),
                HolderSet.direct(ops.registry(Registry.PLACED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(PrimalMagick.MODID, "ore_quartz")))),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_ore_quartz_lower"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_OVERWORLD),
                HolderSet.direct(ops.registry(Registry.PLACED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(PrimalMagick.MODID, "ore_quartz_lower")))),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
        
        // Register vegetation placement biome modifiers
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_trees_wild_sunwood"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_FOREST),
                HolderSet.direct(ops.registry(Registry.PLACED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(PrimalMagick.MODID, "trees_wild_sunwood")))),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_trees_wild_moonwood"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_FOREST),
                HolderSet.direct(ops.registry(Registry.PLACED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(PrimalMagick.MODID, "trees_wild_moonwood")))),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
        
        // Register mob spawn biome modifiers
        consumer.accept(new ResourceLocation(PrimalMagick.MODID, "add_spawn_treefolk"), ForgeBiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
                new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_FOREST),
                new MobSpawnSettings.SpawnerData(EntityTypesPM.TREEFOLK.get(), 100, 1, 3)
        ));
    }

    @Override
    public String getName() {
        return "Primal Magick Biome Modifiers";
    }
}

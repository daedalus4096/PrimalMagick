package com.verdantartifice.primalmagick.common.worldgen.biome_modifiers;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.worldgen.features.PlacedFeaturesPM;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddSpawnsBiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Registration for mod biome modifier data.
 * 
 * @author Daedalus4096
 */
public class BiomeModifiersPM {
    public static final ResourceKey<BiomeModifier> ADD_ORE_MARBLE_RAW_UPPER = registerKey("add_ore_marble_raw_upper");
    public static final ResourceKey<BiomeModifier> ADD_ORE_MARBLE_RAW_LOWER = registerKey("add_ore_marble_raw_lower");
    public static final ResourceKey<BiomeModifier> ADD_ORE_ROCK_SALT_UPPER = registerKey("add_ore_rock_salt_upper");
    public static final ResourceKey<BiomeModifier> ADD_ORE_ROCK_SALT_LOWER = registerKey("add_ore_rock_salt_lower");
    public static final ResourceKey<BiomeModifier> ADD_ORE_QUARTZ_UPPER = registerKey("add_ore_quartz_upper");
    public static final ResourceKey<BiomeModifier> ADD_ORE_QUARTZ_LOWER = registerKey("add_ore_quartz_lower");
    
    public static final ResourceKey<BiomeModifier> ADD_TREE_WILD_SUNWOOD = registerKey("add_tree_wild_sunwood");
    public static final ResourceKey<BiomeModifier> ADD_TREE_WILD_MOONWOOD = registerKey("add_tree_wild_moonwood");
    
    public static final ResourceKey<BiomeModifier> ADD_SPAWN_TREEFOLK = registerKey("add_spawn_treefolk");
    
    public static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(PrimalMagick.MODID, name));
    }
    
    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        
        // Register ore placement biome modifiers
        HolderSet.Named<Biome> overworldBiomes = context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_OVERWORLD);
        context.register(ADD_ORE_MARBLE_RAW_UPPER, new AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(PlacedFeaturesPM.ORE_MARBLE_RAW_UPPER)), GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_ORE_MARBLE_RAW_LOWER, new AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(PlacedFeaturesPM.ORE_MARBLE_RAW_LOWER)), GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_ORE_ROCK_SALT_UPPER, new AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(PlacedFeaturesPM.ORE_ROCK_SALT_UPPER)), GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_ORE_ROCK_SALT_LOWER, new AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(PlacedFeaturesPM.ORE_ROCK_SALT_LOWER)), GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_ORE_QUARTZ_UPPER, new AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(PlacedFeaturesPM.ORE_QUARTZ_UPPER)), GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_ORE_QUARTZ_LOWER, new AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placedFeatures.getOrThrow(PlacedFeaturesPM.ORE_QUARTZ_LOWER)), GenerationStep.Decoration.UNDERGROUND_ORES));
        
        // Register vegetation placement biome modifiers
        HolderSet.Named<Biome> forestBiomes = context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_FOREST);
        context.register(ADD_TREE_WILD_SUNWOOD, new AddFeaturesBiomeModifier(forestBiomes, HolderSet.direct(placedFeatures.getOrThrow(PlacedFeaturesPM.TREE_WILD_SUNWOOD)), GenerationStep.Decoration.VEGETAL_DECORATION));
        context.register(ADD_TREE_WILD_MOONWOOD, new AddFeaturesBiomeModifier(forestBiomes, HolderSet.direct(placedFeatures.getOrThrow(PlacedFeaturesPM.TREE_WILD_MOONWOOD)), GenerationStep.Decoration.VEGETAL_DECORATION));
        
        // Register mob spawn biome modifiers
        context.register(ADD_SPAWN_TREEFOLK, AddSpawnsBiomeModifier.singleSpawn(forestBiomes, new MobSpawnSettings.SpawnerData(EntityTypesPM.TREEFOLK.get(), 100, 1, 3)));
    }
}

package com.verdantartifice.primalmagic.common.worldgen.features;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod worldgen features.
 * 
 * @author Daedalus4096
 */
public class FeaturesPM {
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, PrimalMagic.MODID);
    private static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, PrimalMagic.MODID);
    
    public static void init() {
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
        STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    // TODO Reorganize into normal/configured structure separation
    public static final ConfiguredFeature<?, ?> ORE_MARBLE_RAW = registerFeature(new ResourceLocation(PrimalMagic.MODID, "ore_marble_raw"), Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlocksPM.MARBLE_RAW.get().getDefaultState(), 33)).range(80).square().func_242731_b(10));
    public static final ConfiguredFeature<?, ?> ORE_ROCK_SALT = registerFeature(new ResourceLocation(PrimalMagic.MODID, "ore_rock_salt"), Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlocksPM.ROCK_SALT_ORE.get().getDefaultState(), 33)).range(128).square().func_242731_b(20));
    public static final ConfiguredFeature<?, ?> ORE_QUARTZ = registerFeature(new ResourceLocation(PrimalMagic.MODID, "ore_quartz"), Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlocksPM.QUARTZ_ORE.get().getDefaultState(), 33)).range(128).square().func_242731_b(20));
    
    public static final RegistryObject<Feature<TreeFeatureConfig>> PHASING_TREE = FEATURES.register("phasing_tree", () -> new PhasingTreeFeature(TreeFeatureConfig::func_227338_a_));
    
    public static final RegistryObject<Structure<ShrineConfig>> SHRINE = STRUCTURES.register("shrine", () -> new ShrineStructure(ShrineConfig.CODEC));
    
    public static void setupStructures() {
    	setupSpacingAndLand(SHRINE.get(), new StructureSeparationSettings(10, 5, 11893192), true);
    }
    
    private static <S extends Structure<?>> void setupSpacingAndLand(S structure, StructureSeparationSettings settings, boolean transformSurroundingLand) {
        Structure.NAME_STRUCTURE_BIMAP.put(structure.getRegistryName().toString(), structure);
        if (transformSurroundingLand) {
        	Structure.field_236384_t_ = ImmutableList.<Structure<?>>builder()
        			.addAll(Structure.field_236384_t_)
        			.add(structure)
        			.build();
        }
        DimensionStructuresSettings.field_236191_b_ = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
    			.putAll(DimensionStructuresSettings.field_236191_b_)
    			.put(structure, settings)
    			.build();
    }
    
    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> registerFeature(ResourceLocation key, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, configuredFeature);
    }
}

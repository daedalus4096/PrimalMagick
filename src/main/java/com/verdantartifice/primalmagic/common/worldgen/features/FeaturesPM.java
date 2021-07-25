package com.verdantartifice.primalmagic.common.worldgen.features;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodLeavesBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodLogBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodLeavesBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodLogBlock;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.data.worldgen.Features;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.placement.FrequencyWithExtraChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod worldgen features.
 * 
 * @author Daedalus4096
 */
public class FeaturesPM {
    private static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, PrimalMagic.MODID);
    
    public static void init() {
        STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    // TODO Reorganize into normal/configured structure separation
    public static ConfiguredFeature<?, ?> ORE_MARBLE_RAW;
    public static ConfiguredFeature<?, ?> ORE_ROCK_SALT;
    public static ConfiguredFeature<?, ?> ORE_QUARTZ;
    
    public static ConfiguredFeature<TreeConfiguration, ?> TREE_SUNWOOD_FULL;
    public static ConfiguredFeature<TreeConfiguration, ?> TREE_SUNWOOD_WAXING;
    public static ConfiguredFeature<TreeConfiguration, ?> TREE_SUNWOOD_WANING;
    public static ConfiguredFeature<TreeConfiguration, ?> TREE_SUNWOOD_FADED;

    public static ConfiguredFeature<TreeConfiguration, ?> TREE_MOONWOOD_FULL;
    public static ConfiguredFeature<TreeConfiguration, ?> TREE_MOONWOOD_WAXING;
    public static ConfiguredFeature<TreeConfiguration, ?> TREE_MOONWOOD_WANING;
    public static ConfiguredFeature<TreeConfiguration, ?> TREE_MOONWOOD_FADED;
    
    public static ConfiguredFeature<TreeConfiguration, ?> TREE_HALLOWOOD;
    
    public static ConfiguredFeature<?, ?> TREE_SUNWOOD_FULL_SPACED;
    public static ConfiguredFeature<?, ?> TREE_MOONWOOD_FULL_SPACED;

    public static final RegistryObject<StructureFeature<ShrineConfig>> SHRINE = STRUCTURES.register("shrine", () -> new ShrineStructure(ShrineConfig.CODEC));
    
    public static void setupFeatures() {
        ORE_MARBLE_RAW = registerFeature("ore_marble_raw", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, BlocksPM.MARBLE_RAW.get().defaultBlockState(), 33)).range(80).squared().count(10));
        ORE_ROCK_SALT = registerFeature("ore_rock_salt", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, BlocksPM.ROCK_SALT_ORE.get().defaultBlockState(), 10)).range(128).squared().count(20));
        ORE_QUARTZ = registerFeature("ore_quartz", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, BlocksPM.QUARTZ_ORE.get().defaultBlockState(), 3)).range(128).squared().count(20));
        
        TREE_SUNWOOD_FULL = registerFeature("tree_sunwood_full", Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(FeaturesPM.States.SUNWOOD_LOG_FULL), new SimpleStateProvider(FeaturesPM.States.SUNWOOD_LEAVES_FULL), new BlobFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        TREE_SUNWOOD_WAXING = registerFeature("tree_sunwood_waxing", Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(FeaturesPM.States.SUNWOOD_LOG_WAXING), new SimpleStateProvider(FeaturesPM.States.SUNWOOD_LEAVES_WAXING), new BlobFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        TREE_SUNWOOD_WANING = registerFeature("tree_sunwood_waning", Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(FeaturesPM.States.SUNWOOD_LOG_WANING), new SimpleStateProvider(FeaturesPM.States.SUNWOOD_LEAVES_WANING), new BlobFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        TREE_SUNWOOD_FADED = registerFeature("tree_sunwood_faded", Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(FeaturesPM.States.SUNWOOD_LOG_FADED), new SimpleStateProvider(FeaturesPM.States.SUNWOOD_LEAVES_FADED), new BlobFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        
        TREE_MOONWOOD_FULL = registerFeature("tree_moonwood_full", Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(FeaturesPM.States.MOONWOOD_LOG_FULL), new SimpleStateProvider(FeaturesPM.States.MOONWOOD_LEAVES_FULL), new BlobFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        TREE_MOONWOOD_WAXING = registerFeature("tree_moonwood_waxing", Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(FeaturesPM.States.MOONWOOD_LOG_WAXING), new SimpleStateProvider(FeaturesPM.States.MOONWOOD_LEAVES_WAXING), new BlobFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        TREE_MOONWOOD_WANING = registerFeature("tree_moonwood_waning", Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(FeaturesPM.States.MOONWOOD_LOG_WANING), new SimpleStateProvider(FeaturesPM.States.MOONWOOD_LEAVES_WANING), new BlobFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        TREE_MOONWOOD_FADED = registerFeature("tree_moonwood_faded", Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(FeaturesPM.States.MOONWOOD_LOG_FADED), new SimpleStateProvider(FeaturesPM.States.MOONWOOD_LEAVES_FADED), new BlobFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        
        TREE_HALLOWOOD = registerFeature("tree_hallowood", Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(FeaturesPM.States.HALLOWOOD_LOG), new SimpleStateProvider(FeaturesPM.States.HALLOWOOD_LEAVES), new BlobFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        
        TREE_SUNWOOD_FULL_SPACED = registerFeature("tree_sunwood_full_spaced", TREE_SUNWOOD_FULL.decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.05F, 1))));
        TREE_MOONWOOD_FULL_SPACED = registerFeature("tree_moonwood_full_spaced", TREE_MOONWOOD_FULL.decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.05F, 1))));
    }
    
    public static void setupStructures() {
    	setupSpacingAndLand(SHRINE.get(), new StructureFeatureConfiguration(20, 10, 11893192), true);
    }
    
    private static <S extends StructureFeature<?>> void setupSpacingAndLand(S structure, StructureFeatureConfiguration settings, boolean transformSurroundingLand) {
        StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);
        if (transformSurroundingLand) {
        	StructureFeature.NOISE_AFFECTING_FEATURES = ImmutableList.<StructureFeature<?>>builder()
        			.addAll(StructureFeature.NOISE_AFFECTING_FEATURES)
        			.add(structure)
        			.build();
        }
        StructureSettings.DEFAULTS = ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
    			.putAll(StructureSettings.DEFAULTS)
    			.put(structure, settings)
    			.build();
    }
    
    private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> registerFeature(String key, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(PrimalMagic.MODID, key), configuredFeature);
    }
    
    protected static final class States {
        protected static final BlockState SUNWOOD_LOG_FULL = BlocksPM.SUNWOOD_LOG.get().defaultBlockState().setValue(SunwoodLogBlock.PHASE, TimePhase.FULL);
        protected static final BlockState SUNWOOD_LOG_WAXING = BlocksPM.SUNWOOD_LOG.get().defaultBlockState().setValue(SunwoodLogBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState SUNWOOD_LOG_WANING = BlocksPM.SUNWOOD_LOG.get().defaultBlockState().setValue(SunwoodLogBlock.PHASE, TimePhase.WANING);
        protected static final BlockState SUNWOOD_LOG_FADED = BlocksPM.SUNWOOD_LOG.get().defaultBlockState().setValue(SunwoodLogBlock.PHASE, TimePhase.FADED);
        protected static final BlockState SUNWOOD_LEAVES_FULL = BlocksPM.SUNWOOD_LEAVES.get().defaultBlockState().setValue(SunwoodLeavesBlock.PHASE, TimePhase.FULL);
        protected static final BlockState SUNWOOD_LEAVES_WAXING = BlocksPM.SUNWOOD_LEAVES.get().defaultBlockState().setValue(SunwoodLeavesBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState SUNWOOD_LEAVES_WANING = BlocksPM.SUNWOOD_LEAVES.get().defaultBlockState().setValue(SunwoodLeavesBlock.PHASE, TimePhase.WANING);
        protected static final BlockState SUNWOOD_LEAVES_FADED = BlocksPM.SUNWOOD_LEAVES.get().defaultBlockState().setValue(SunwoodLeavesBlock.PHASE, TimePhase.FADED);
        protected static final BlockState MOONWOOD_LOG_FULL = BlocksPM.MOONWOOD_LOG.get().defaultBlockState().setValue(MoonwoodLogBlock.PHASE, TimePhase.FULL);
        protected static final BlockState MOONWOOD_LOG_WAXING = BlocksPM.MOONWOOD_LOG.get().defaultBlockState().setValue(MoonwoodLogBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState MOONWOOD_LOG_WANING = BlocksPM.MOONWOOD_LOG.get().defaultBlockState().setValue(MoonwoodLogBlock.PHASE, TimePhase.WANING);
        protected static final BlockState MOONWOOD_LOG_FADED = BlocksPM.MOONWOOD_LOG.get().defaultBlockState().setValue(MoonwoodLogBlock.PHASE, TimePhase.FADED);
        protected static final BlockState MOONWOOD_LEAVES_FULL = BlocksPM.MOONWOOD_LEAVES.get().defaultBlockState().setValue(MoonwoodLeavesBlock.PHASE, TimePhase.FULL);
        protected static final BlockState MOONWOOD_LEAVES_WAXING = BlocksPM.MOONWOOD_LEAVES.get().defaultBlockState().setValue(MoonwoodLeavesBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState MOONWOOD_LEAVES_WANING = BlocksPM.MOONWOOD_LEAVES.get().defaultBlockState().setValue(MoonwoodLeavesBlock.PHASE, TimePhase.WANING);
        protected static final BlockState MOONWOOD_LEAVES_FADED = BlocksPM.MOONWOOD_LEAVES.get().defaultBlockState().setValue(MoonwoodLeavesBlock.PHASE, TimePhase.FADED);
        protected static final BlockState HALLOWOOD_LOG = BlocksPM.HALLOWOOD_LOG.get().defaultBlockState();
        protected static final BlockState HALLOWOOD_LEAVES = BlocksPM.HALLOWOOD_LEAVES.get().defaultBlockState();
    }
}

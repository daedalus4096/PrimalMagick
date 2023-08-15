package com.verdantartifice.primalmagick.common.worldgen.features;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodLeavesBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodLogBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodLeavesBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodLogBlock;
import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

/**
 * Registration for configured versions of mod features (e.g. ores, vegetation, etc).
 * 
 * @author Daedalus4096
 */
public class ConfiguredFeaturesPM {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_MARBLE_RAW = registerKey("ore_marble_raw");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ROCK_SALT = registerKey("ore_rock_salt");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_QUARTZ = registerKey("ore_quartz");
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREE_SUNWOOD_FULL = registerKey("tree_sunwood_full");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREE_SUNWOOD_WAXING = registerKey("tree_sunwood_waxing");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREE_SUNWOOD_WANING = registerKey("tree_sunwood_waning");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREE_SUNWOOD_FADED = registerKey("tree_sunwood_faded");
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREE_MOONWOOD_FULL = registerKey("tree_moonwood_full");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREE_MOONWOOD_WAXING = registerKey("tree_moonwood_waxing");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREE_MOONWOOD_WANING = registerKey("tree_moonwood_waning");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREE_MOONWOOD_FADED = registerKey("tree_moonwood_faded");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREE_HALLOWOOD = registerKey("tree_hallowood");

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, PrimalMagick.resource(name));
    }
    
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        // Register configured ore features
        context.register(ORE_MARBLE_RAW, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), BlocksPM.MARBLE_RAW.get().defaultBlockState(), 33)));
        context.register(ORE_ROCK_SALT, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), BlocksPM.ROCK_SALT_ORE.get().defaultBlockState(), 10)));
        context.register(ORE_QUARTZ, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), BlocksPM.QUARTZ_ORE.get().defaultBlockState(), 3)));

        // Register configured tree features
        context.register(TREE_SUNWOOD_FULL, new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(ConfiguredFeaturesPM.States.SUNWOOD_LOG_FULL, 39).add(ConfiguredFeaturesPM.States.PULSING_SUNWOOD_LOG_FULL, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(ConfiguredFeaturesPM.States.SUNWOOD_LEAVES_FULL), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        context.register(TREE_SUNWOOD_WAXING, new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(ConfiguredFeaturesPM.States.SUNWOOD_LOG_WAXING, 39).add(ConfiguredFeaturesPM.States.PULSING_SUNWOOD_LOG_WAXING, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(ConfiguredFeaturesPM.States.SUNWOOD_LEAVES_WAXING), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        context.register(TREE_SUNWOOD_WANING, new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(ConfiguredFeaturesPM.States.SUNWOOD_LOG_WANING, 39).add(ConfiguredFeaturesPM.States.PULSING_SUNWOOD_LOG_WANING, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(ConfiguredFeaturesPM.States.SUNWOOD_LEAVES_WANING), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        context.register(TREE_SUNWOOD_FADED, new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(ConfiguredFeaturesPM.States.SUNWOOD_LOG_FADED, 39).add(ConfiguredFeaturesPM.States.PULSING_SUNWOOD_LOG_FADED, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(ConfiguredFeaturesPM.States.SUNWOOD_LEAVES_FADED), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        
        context.register(TREE_MOONWOOD_FULL, new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(ConfiguredFeaturesPM.States.MOONWOOD_LOG_FULL, 39).add(ConfiguredFeaturesPM.States.PULSING_MOONWOOD_LOG_FULL, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(ConfiguredFeaturesPM.States.MOONWOOD_LEAVES_FULL), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        context.register(TREE_MOONWOOD_WAXING, new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(ConfiguredFeaturesPM.States.MOONWOOD_LOG_WAXING, 39).add(ConfiguredFeaturesPM.States.PULSING_MOONWOOD_LOG_WAXING, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(ConfiguredFeaturesPM.States.MOONWOOD_LEAVES_WAXING), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        context.register(TREE_MOONWOOD_WANING, new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(ConfiguredFeaturesPM.States.MOONWOOD_LOG_WANING, 39).add(ConfiguredFeaturesPM.States.PULSING_MOONWOOD_LOG_WANING, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(ConfiguredFeaturesPM.States.MOONWOOD_LEAVES_WANING), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        context.register(TREE_MOONWOOD_FADED, new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(ConfiguredFeaturesPM.States.MOONWOOD_LOG_FADED, 39).add(ConfiguredFeaturesPM.States.PULSING_MOONWOOD_LOG_FADED, 1)), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(ConfiguredFeaturesPM.States.MOONWOOD_LEAVES_FADED), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        
        context.register(TREE_HALLOWOOD, new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(ConfiguredFeaturesPM.States.HALLOWOOD_LOG), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(ConfiguredFeaturesPM.States.HALLOWOOD_LEAVES), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
    }

    /**
     * Helper class pre-defining complex block states.
     * 
     * @author Daedalus4096
     */
    protected static final class States {
        protected static final BlockState SUNWOOD_LOG_FULL = BlocksPM.SUNWOOD_LOG.get().defaultBlockState().setValue(SunwoodLogBlock.PHASE, TimePhase.FULL);
        protected static final BlockState SUNWOOD_LOG_WAXING = BlocksPM.SUNWOOD_LOG.get().defaultBlockState().setValue(SunwoodLogBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState SUNWOOD_LOG_WANING = BlocksPM.SUNWOOD_LOG.get().defaultBlockState().setValue(SunwoodLogBlock.PHASE, TimePhase.WANING);
        protected static final BlockState SUNWOOD_LOG_FADED = BlocksPM.SUNWOOD_LOG.get().defaultBlockState().setValue(SunwoodLogBlock.PHASE, TimePhase.FADED);
        protected static final BlockState PULSING_SUNWOOD_LOG_FULL = SUNWOOD_LOG_FULL.setValue(SunwoodLogBlock.PULSING, true);
        protected static final BlockState PULSING_SUNWOOD_LOG_WAXING = SUNWOOD_LOG_WAXING.setValue(SunwoodLogBlock.PULSING, true);
        protected static final BlockState PULSING_SUNWOOD_LOG_WANING = SUNWOOD_LOG_WANING.setValue(SunwoodLogBlock.PULSING, true);
        protected static final BlockState PULSING_SUNWOOD_LOG_FADED = SUNWOOD_LOG_FADED.setValue(SunwoodLogBlock.PULSING, true);
        protected static final BlockState SUNWOOD_LEAVES_FULL = BlocksPM.SUNWOOD_LEAVES.get().defaultBlockState().setValue(SunwoodLeavesBlock.PHASE, TimePhase.FULL);
        protected static final BlockState SUNWOOD_LEAVES_WAXING = BlocksPM.SUNWOOD_LEAVES.get().defaultBlockState().setValue(SunwoodLeavesBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState SUNWOOD_LEAVES_WANING = BlocksPM.SUNWOOD_LEAVES.get().defaultBlockState().setValue(SunwoodLeavesBlock.PHASE, TimePhase.WANING);
        protected static final BlockState SUNWOOD_LEAVES_FADED = BlocksPM.SUNWOOD_LEAVES.get().defaultBlockState().setValue(SunwoodLeavesBlock.PHASE, TimePhase.FADED);
        protected static final BlockState SUNWOOD_SAPLING = BlocksPM.SUNWOOD_SAPLING.get().defaultBlockState();
        protected static final BlockState MOONWOOD_LOG_FULL = BlocksPM.MOONWOOD_LOG.get().defaultBlockState().setValue(MoonwoodLogBlock.PHASE, TimePhase.FULL);
        protected static final BlockState MOONWOOD_LOG_WAXING = BlocksPM.MOONWOOD_LOG.get().defaultBlockState().setValue(MoonwoodLogBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState MOONWOOD_LOG_WANING = BlocksPM.MOONWOOD_LOG.get().defaultBlockState().setValue(MoonwoodLogBlock.PHASE, TimePhase.WANING);
        protected static final BlockState MOONWOOD_LOG_FADED = BlocksPM.MOONWOOD_LOG.get().defaultBlockState().setValue(MoonwoodLogBlock.PHASE, TimePhase.FADED);
        protected static final BlockState PULSING_MOONWOOD_LOG_FULL = MOONWOOD_LOG_FULL.setValue(MoonwoodLogBlock.PULSING, true);
        protected static final BlockState PULSING_MOONWOOD_LOG_WAXING = MOONWOOD_LOG_WAXING.setValue(MoonwoodLogBlock.PULSING, true);
        protected static final BlockState PULSING_MOONWOOD_LOG_WANING = MOONWOOD_LOG_WANING.setValue(MoonwoodLogBlock.PULSING, true);
        protected static final BlockState PULSING_MOONWOOD_LOG_FADED = MOONWOOD_LOG_FADED.setValue(MoonwoodLogBlock.PULSING, true);
        protected static final BlockState MOONWOOD_LEAVES_FULL = BlocksPM.MOONWOOD_LEAVES.get().defaultBlockState().setValue(MoonwoodLeavesBlock.PHASE, TimePhase.FULL);
        protected static final BlockState MOONWOOD_LEAVES_WAXING = BlocksPM.MOONWOOD_LEAVES.get().defaultBlockState().setValue(MoonwoodLeavesBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState MOONWOOD_LEAVES_WANING = BlocksPM.MOONWOOD_LEAVES.get().defaultBlockState().setValue(MoonwoodLeavesBlock.PHASE, TimePhase.WANING);
        protected static final BlockState MOONWOOD_LEAVES_FADED = BlocksPM.MOONWOOD_LEAVES.get().defaultBlockState().setValue(MoonwoodLeavesBlock.PHASE, TimePhase.FADED);
        protected static final BlockState MOONWOOD_SAPLING = BlocksPM.MOONWOOD_SAPLING.get().defaultBlockState();
        protected static final BlockState HALLOWOOD_LOG = BlocksPM.HALLOWOOD_LOG.get().defaultBlockState();
        protected static final BlockState HALLOWOOD_LEAVES = BlocksPM.HALLOWOOD_LEAVES.get().defaultBlockState();
        protected static final BlockState HALLOWOOD_SAPLING = BlocksPM.HALLOWOOD_SAPLING.get().defaultBlockState();
    }
}

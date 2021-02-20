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

import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.TwoLayerFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
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
    private static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, PrimalMagic.MODID);
    
    public static void init() {
        STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    // TODO Reorganize into normal/configured structure separation
    public static ConfiguredFeature<?, ?> ORE_MARBLE_RAW;
    public static ConfiguredFeature<?, ?> ORE_ROCK_SALT;
    public static ConfiguredFeature<?, ?> ORE_QUARTZ;
    
    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TREE_SUNWOOD_FULL;
    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TREE_SUNWOOD_WAXING;
    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TREE_SUNWOOD_WANING;
    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TREE_SUNWOOD_FADED;

    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TREE_MOONWOOD_FULL;
    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TREE_MOONWOOD_WAXING;
    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TREE_MOONWOOD_WANING;
    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TREE_MOONWOOD_FADED;

    public static final RegistryObject<Structure<ShrineConfig>> SHRINE = STRUCTURES.register("shrine", () -> new ShrineStructure(ShrineConfig.CODEC));
    
    public static void setupFeatures() {
        ORE_MARBLE_RAW = registerFeature("ore_marble_raw", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlocksPM.MARBLE_RAW.get().getDefaultState(), 33)).range(80).square().func_242731_b(10));
        ORE_ROCK_SALT = registerFeature("ore_rock_salt", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlocksPM.ROCK_SALT_ORE.get().getDefaultState(), 33)).range(128).square().func_242731_b(20));
        ORE_QUARTZ = registerFeature("ore_quartz", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlocksPM.QUARTZ_ORE.get().getDefaultState(), 33)).range(128).square().func_242731_b(20));
        
        TREE_SUNWOOD_FULL = registerFeature("tree_sunwood_full", Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(FeaturesPM.States.SUNWOOD_LOG_FULL), new SimpleBlockStateProvider(FeaturesPM.States.SUNWOOD_LEAVES_FULL), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));
        TREE_SUNWOOD_WAXING = registerFeature("tree_sunwood_waxing", Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(FeaturesPM.States.SUNWOOD_LOG_WAXING), new SimpleBlockStateProvider(FeaturesPM.States.SUNWOOD_LEAVES_WAXING), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));
        TREE_SUNWOOD_WANING = registerFeature("tree_sunwood_waning", Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(FeaturesPM.States.SUNWOOD_LOG_WANING), new SimpleBlockStateProvider(FeaturesPM.States.SUNWOOD_LEAVES_WANING), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));
        TREE_SUNWOOD_FADED = registerFeature("tree_sunwood_faded", Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(FeaturesPM.States.SUNWOOD_LOG_FADED), new SimpleBlockStateProvider(FeaturesPM.States.SUNWOOD_LEAVES_FADED), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));
        
        TREE_MOONWOOD_FULL = registerFeature("tree_moonwood_full", Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(FeaturesPM.States.MOONWOOD_LOG_FULL), new SimpleBlockStateProvider(FeaturesPM.States.MOONWOOD_LEAVES_FULL), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));
        TREE_MOONWOOD_WAXING = registerFeature("tree_moonwood_waxing", Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(FeaturesPM.States.MOONWOOD_LOG_WAXING), new SimpleBlockStateProvider(FeaturesPM.States.MOONWOOD_LEAVES_WAXING), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));
        TREE_MOONWOOD_WANING = registerFeature("tree_moonwood_waning", Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(FeaturesPM.States.MOONWOOD_LOG_WANING), new SimpleBlockStateProvider(FeaturesPM.States.MOONWOOD_LEAVES_WANING), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));
        TREE_MOONWOOD_FADED = registerFeature("tree_moonwood_faded", Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(FeaturesPM.States.MOONWOOD_LOG_FADED), new SimpleBlockStateProvider(FeaturesPM.States.MOONWOOD_LEAVES_FADED), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));
    }
    
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
    
    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> registerFeature(String key, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(PrimalMagic.MODID, key), configuredFeature);
    }
    
    protected static final class States {
        protected static final BlockState SUNWOOD_LOG_FULL = BlocksPM.SUNWOOD_LOG.get().getDefaultState().with(SunwoodLogBlock.PHASE, TimePhase.FULL);
        protected static final BlockState SUNWOOD_LOG_WAXING = BlocksPM.SUNWOOD_LOG.get().getDefaultState().with(SunwoodLogBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState SUNWOOD_LOG_WANING = BlocksPM.SUNWOOD_LOG.get().getDefaultState().with(SunwoodLogBlock.PHASE, TimePhase.WANING);
        protected static final BlockState SUNWOOD_LOG_FADED = BlocksPM.SUNWOOD_LOG.get().getDefaultState().with(SunwoodLogBlock.PHASE, TimePhase.FADED);
        protected static final BlockState SUNWOOD_LEAVES_FULL = BlocksPM.SUNWOOD_LEAVES.get().getDefaultState().with(SunwoodLeavesBlock.PHASE, TimePhase.FULL);
        protected static final BlockState SUNWOOD_LEAVES_WAXING = BlocksPM.SUNWOOD_LEAVES.get().getDefaultState().with(SunwoodLeavesBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState SUNWOOD_LEAVES_WANING = BlocksPM.SUNWOOD_LEAVES.get().getDefaultState().with(SunwoodLeavesBlock.PHASE, TimePhase.WANING);
        protected static final BlockState SUNWOOD_LEAVES_FADED = BlocksPM.SUNWOOD_LEAVES.get().getDefaultState().with(SunwoodLeavesBlock.PHASE, TimePhase.FADED);
        protected static final BlockState MOONWOOD_LOG_FULL = BlocksPM.MOONWOOD_LOG.get().getDefaultState().with(MoonwoodLogBlock.PHASE, TimePhase.FULL);
        protected static final BlockState MOONWOOD_LOG_WAXING = BlocksPM.MOONWOOD_LOG.get().getDefaultState().with(MoonwoodLogBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState MOONWOOD_LOG_WANING = BlocksPM.MOONWOOD_LOG.get().getDefaultState().with(MoonwoodLogBlock.PHASE, TimePhase.WANING);
        protected static final BlockState MOONWOOD_LOG_FADED = BlocksPM.MOONWOOD_LOG.get().getDefaultState().with(MoonwoodLogBlock.PHASE, TimePhase.FADED);
        protected static final BlockState MOONWOOD_LEAVES_FULL = BlocksPM.MOONWOOD_LEAVES.get().getDefaultState().with(MoonwoodLeavesBlock.PHASE, TimePhase.FULL);
        protected static final BlockState MOONWOOD_LEAVES_WAXING = BlocksPM.MOONWOOD_LEAVES.get().getDefaultState().with(MoonwoodLeavesBlock.PHASE, TimePhase.WAXING);
        protected static final BlockState MOONWOOD_LEAVES_WANING = BlocksPM.MOONWOOD_LEAVES.get().getDefaultState().with(MoonwoodLeavesBlock.PHASE, TimePhase.WANING);
        protected static final BlockState MOONWOOD_LEAVES_FADED = BlocksPM.MOONWOOD_LEAVES.get().getDefaultState().with(MoonwoodLeavesBlock.PHASE, TimePhase.FADED);
    }
}

package com.verdantartifice.primalmagick.common.blocks;

import com.verdantartifice.primalmagick.common.blocks.base.AttachedStemBlockPM;
import com.verdantartifice.primalmagick.common.blocks.base.SaplingBlockPM;
import com.verdantartifice.primalmagick.common.blocks.base.StairBlockPM;
import com.verdantartifice.primalmagick.common.blocks.base.StemBlockPM;
import com.verdantartifice.primalmagick.common.blocks.crafting.ArcaneWorkbenchBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.CalcinatorBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.ConcocterBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.EssenceFurnaceBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.RunecarvingTableBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.RunescribingAltarBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.RunicGrindstoneBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.SpellcraftingAltarBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.WandAssemblyTableBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.WandGlamourTableBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.WandInscriptionTableBlock;
import com.verdantartifice.primalmagick.common.blocks.crops.HydromelonBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.AnalysisTableBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.DesalinatorBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.DissolutionChamberBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.EssenceCaskBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.EssenceTransmuterBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.HoneyExtractorBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.InfernalFurnaceBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.ResearchTableBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.SanguineCrucibleBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.ScribeTableBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.SunlampBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.VoidTurbineBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.ZephyrEngineBlock;
import com.verdantartifice.primalmagick.common.blocks.flowers.BloodRoseBlock;
import com.verdantartifice.primalmagick.common.blocks.flowers.EmberflowerBlock;
import com.verdantartifice.primalmagick.common.blocks.golems.HallowsteelGolemControllerBlock;
import com.verdantartifice.primalmagick.common.blocks.golems.HexiumGolemControllerBlock;
import com.verdantartifice.primalmagick.common.blocks.golems.PrimaliteGolemControllerBlock;
import com.verdantartifice.primalmagick.common.blocks.mana.AncientManaFontBlock;
import com.verdantartifice.primalmagick.common.blocks.mana.ArtificialManaFontBlock;
import com.verdantartifice.primalmagick.common.blocks.mana.AutoChargerBlock;
import com.verdantartifice.primalmagick.common.blocks.mana.ManaBatteryBlock;
import com.verdantartifice.primalmagick.common.blocks.mana.ManaInjectorBlock;
import com.verdantartifice.primalmagick.common.blocks.mana.ManaRelayBlock;
import com.verdantartifice.primalmagick.common.blocks.mana.WandChargerBlock;
import com.verdantartifice.primalmagick.common.blocks.minerals.BuddingGemClusterBlock;
import com.verdantartifice.primalmagick.common.blocks.minerals.BuddingGemSourceBlock;
import com.verdantartifice.primalmagick.common.blocks.minerals.GemBudType;
import com.verdantartifice.primalmagick.common.blocks.misc.CarvedBookshelfBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.ConsecrationFieldBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.EnderwardBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.GlowFieldBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.PillarBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.SkyglassBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.SkyglassPaneBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.StainedSkyglassBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.StainedSkyglassPaneBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.WoodTableBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.BloodletterBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.CelestialHarpBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.EntropySinkBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.IncenseBrazierBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.OfferingPedestalBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.RitualAltarBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.RitualBellBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.RitualCandleBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.RitualLecternBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.SaltTrailBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.SoulAnvilBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodLeavesBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodLogBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodPillarBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodPlanksBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodSlabBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodStairsBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.StrippableLogBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodLeavesBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodLogBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodPillarBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodPlanksBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodSlabBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodStairsBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.TreeGrowersPM;
import com.verdantartifice.primalmagick.common.blocks.trees.TreefolkSproutBlock;
import com.verdantartifice.primalmagick.common.items.ItemReferencesPM;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tags.BlockTagsPM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Common repository for block registry objects.
 *
 * @author Daedalus4096
 */
public class BlocksPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.BLOCKS_REGISTRY.init();
    }

    private static <T extends Block> IRegistryItem<Block, T> register(String name, Supplier<T> blockSupplier) {
        return Services.BLOCKS_REGISTRY.register(name, blockSupplier);
    }

    // Register raw marble blocks
    public static final IRegistryItem<Block, Block> MARBLE_RAW = register("marble_raw", () -> new Block(Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, SlabBlock> MARBLE_SLAB = register("marble_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(MARBLE_RAW.get())));
    public static final IRegistryItem<Block, StairBlock> MARBLE_STAIRS = register("marble_stairs", () -> new StairBlockPM(MARBLE_RAW.get().defaultBlockState(), Block.Properties.ofFullCopy(MARBLE_RAW.get())));
    public static final IRegistryItem<Block, WallBlock> MARBLE_WALL = register("marble_wall", () -> new WallBlock(Block.Properties.ofFullCopy(MARBLE_RAW.get())));
    public static final IRegistryItem<Block, Block> MARBLE_BRICKS = register("marble_bricks", () -> new Block(Block.Properties.ofFullCopy(MARBLE_RAW.get())));
    public static final IRegistryItem<Block, SlabBlock> MARBLE_BRICK_SLAB = register("marble_brick_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(MARBLE_BRICKS.get())));
    public static final IRegistryItem<Block, StairBlock> MARBLE_BRICK_STAIRS = register("marble_brick_stairs", () -> new StairBlockPM(MARBLE_BRICKS.get().defaultBlockState(), Block.Properties.ofFullCopy(MARBLE_BRICKS.get())));
    public static final IRegistryItem<Block, WallBlock> MARBLE_BRICK_WALL = register("marble_brick_wall", () -> new WallBlock(Block.Properties.ofFullCopy(MARBLE_BRICKS.get())));
    public static final IRegistryItem<Block, PillarBlock> MARBLE_PILLAR = register("marble_pillar", () -> new PillarBlock(Block.Properties.ofFullCopy(MARBLE_RAW.get())));
    public static final IRegistryItem<Block, Block> MARBLE_CHISELED = register("marble_chiseled", () -> new Block(Block.Properties.ofFullCopy(MARBLE_RAW.get())));
    public static final IRegistryItem<Block, Block> MARBLE_RUNED = register("marble_runed", () -> new Block(Block.Properties.ofFullCopy(MARBLE_RAW.get())));
    public static final IRegistryItem<Block, Block> MARBLE_TILES = register("marble_tiles", () -> new Block(Block.Properties.ofFullCopy(MARBLE_RAW.get())));
    public static final IRegistryItem<Block, CarvedBookshelfBlock> MARBLE_BOOKSHELF = register("marble_bookshelf", () -> new CarvedBookshelfBlock(Block.Properties.ofFullCopy(MARBLE_RAW.get())));

    // Register enchanted marble blocks
    public static final IRegistryItem<Block, Block> MARBLE_ENCHANTED = register("marble_enchanted", () -> new Block(Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, SlabBlock> MARBLE_ENCHANTED_SLAB = register("marble_enchanted_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(MARBLE_ENCHANTED.get())));
    public static final IRegistryItem<Block, StairBlock> MARBLE_ENCHANTED_STAIRS = register("marble_enchanted_stairs", () -> new StairBlockPM(MARBLE_ENCHANTED.get().defaultBlockState(), Block.Properties.ofFullCopy(MARBLE_ENCHANTED.get())));
    public static final IRegistryItem<Block, WallBlock> MARBLE_ENCHANTED_WALL = register("marble_enchanted_wall", () -> new WallBlock(Block.Properties.ofFullCopy(MARBLE_ENCHANTED.get())));
    public static final IRegistryItem<Block, Block> MARBLE_ENCHANTED_BRICKS = register("marble_enchanted_bricks", () -> new Block(Block.Properties.ofFullCopy(MARBLE_ENCHANTED.get())));
    public static final IRegistryItem<Block, SlabBlock> MARBLE_ENCHANTED_BRICK_SLAB = register("marble_enchanted_brick_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(MARBLE_ENCHANTED_BRICKS.get())));
    public static final IRegistryItem<Block, StairBlock> MARBLE_ENCHANTED_BRICK_STAIRS = register("marble_enchanted_brick_stairs", () -> new StairBlockPM(MARBLE_ENCHANTED_BRICKS.get().defaultBlockState(), Block.Properties.ofFullCopy(MARBLE_ENCHANTED_BRICKS.get())));
    public static final IRegistryItem<Block, WallBlock> MARBLE_ENCHANTED_BRICK_WALL = register("marble_enchanted_brick_wall", () -> new WallBlock(Block.Properties.ofFullCopy(MARBLE_ENCHANTED_BRICKS.get())));
    public static final IRegistryItem<Block, PillarBlock> MARBLE_ENCHANTED_PILLAR = register("marble_enchanted_pillar", () -> new PillarBlock(Block.Properties.ofFullCopy(MARBLE_ENCHANTED.get())));
    public static final IRegistryItem<Block, Block> MARBLE_ENCHANTED_CHISELED = register("marble_enchanted_chiseled", () -> new Block(Block.Properties.ofFullCopy(MARBLE_ENCHANTED.get())));
    public static final IRegistryItem<Block, Block> MARBLE_ENCHANTED_RUNED = register("marble_enchanted_runed", () -> new Block(Block.Properties.ofFullCopy(MARBLE_ENCHANTED.get())));
    public static final IRegistryItem<Block, CarvedBookshelfBlock> MARBLE_ENCHANTED_BOOKSHELF = register("marble_enchanted_bookshelf", () -> new CarvedBookshelfBlock(Block.Properties.ofFullCopy(MARBLE_ENCHANTED.get())));

    // Register smoked marble blocks
    public static final IRegistryItem<Block, Block> MARBLE_SMOKED = register("marble_smoked", () -> new Block(Block.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, SlabBlock> MARBLE_SMOKED_SLAB = register("marble_smoked_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(MARBLE_SMOKED.get())));
    public static final IRegistryItem<Block, StairBlock> MARBLE_SMOKED_STAIRS = register("marble_smoked_stairs", () -> new StairBlockPM(MARBLE_SMOKED.get().defaultBlockState(), Block.Properties.ofFullCopy(MARBLE_SMOKED.get())));
    public static final IRegistryItem<Block, WallBlock> MARBLE_SMOKED_WALL = register("marble_smoked_wall", () -> new WallBlock(Block.Properties.ofFullCopy(MARBLE_SMOKED.get())));
    public static final IRegistryItem<Block, Block> MARBLE_SMOKED_BRICKS = register("marble_smoked_bricks", () -> new Block(Block.Properties.ofFullCopy(MARBLE_SMOKED.get())));
    public static final IRegistryItem<Block, SlabBlock> MARBLE_SMOKED_BRICK_SLAB = register("marble_smoked_brick_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(MARBLE_SMOKED_BRICKS.get())));
    public static final IRegistryItem<Block, StairBlock> MARBLE_SMOKED_BRICK_STAIRS = register("marble_smoked_brick_stairs", () -> new StairBlockPM(MARBLE_SMOKED_BRICKS.get().defaultBlockState(), Block.Properties.ofFullCopy(MARBLE_SMOKED_BRICKS.get())));
    public static final IRegistryItem<Block, WallBlock> MARBLE_SMOKED_BRICK_WALL = register("marble_smoked_brick_wall", () -> new WallBlock(Block.Properties.ofFullCopy(MARBLE_SMOKED_BRICKS.get())));
    public static final IRegistryItem<Block, PillarBlock> MARBLE_SMOKED_PILLAR = register("marble_smoked_pillar", () -> new PillarBlock(Block.Properties.ofFullCopy(MARBLE_SMOKED.get())));
    public static final IRegistryItem<Block, Block> MARBLE_SMOKED_CHISELED = register("marble_smoked_chiseled", () -> new Block(Block.Properties.ofFullCopy(MARBLE_SMOKED.get())));
    public static final IRegistryItem<Block, Block> MARBLE_SMOKED_RUNED = register("marble_smoked_runed", () -> new Block(Block.Properties.ofFullCopy(MARBLE_SMOKED.get())));
    public static final IRegistryItem<Block, CarvedBookshelfBlock> MARBLE_SMOKED_BOOKSHELF = register("marble_smoked_bookshelf", () -> new CarvedBookshelfBlock(Block.Properties.ofFullCopy(MARBLE_SMOKED.get())));

    // Register hallowed marble blocks
    public static final IRegistryItem<Block, Block> MARBLE_HALLOWED = register("marble_hallowed", () -> new Block(Block.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, SlabBlock> MARBLE_HALLOWED_SLAB  = register("marble_hallowed_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(MARBLE_HALLOWED.get())));
    public static final IRegistryItem<Block, StairBlock> MARBLE_HALLOWED_STAIRS = register("marble_hallowed_stairs", () -> new StairBlockPM(MARBLE_HALLOWED.get().defaultBlockState(), Block.Properties.ofFullCopy(MARBLE_HALLOWED.get())));
    public static final IRegistryItem<Block, WallBlock> MARBLE_HALLOWED_WALL = register("marble_hallowed_wall", () -> new WallBlock(Block.Properties.ofFullCopy(MARBLE_HALLOWED.get())));
    public static final IRegistryItem<Block, Block> MARBLE_HALLOWED_BRICKS = register("marble_hallowed_bricks", () -> new Block(Block.Properties.ofFullCopy(MARBLE_HALLOWED.get())));
    public static final IRegistryItem<Block, SlabBlock> MARBLE_HALLOWED_BRICK_SLAB = register("marble_hallowed_brick_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(MARBLE_HALLOWED_BRICKS.get())));
    public static final IRegistryItem<Block, StairBlock> MARBLE_HALLOWED_BRICK_STAIRS = register("marble_hallowed_brick_stairs", () -> new StairBlockPM(MARBLE_HALLOWED_BRICKS.get().defaultBlockState(), Block.Properties.ofFullCopy(MARBLE_HALLOWED_BRICKS.get())));
    public static final IRegistryItem<Block, WallBlock> MARBLE_HALLOWED_BRICK_WALL = register("marble_hallowed_brick_wall", () -> new WallBlock(Block.Properties.ofFullCopy(MARBLE_HALLOWED_BRICKS.get())));
    public static final IRegistryItem<Block, PillarBlock> MARBLE_HALLOWED_PILLAR = register("marble_hallowed_pillar", () -> new PillarBlock(Block.Properties.ofFullCopy(MARBLE_HALLOWED.get())));
    public static final IRegistryItem<Block, Block> MARBLE_HALLOWED_CHISELED = register("marble_hallowed_chiseled", () -> new Block(Block.Properties.ofFullCopy(MARBLE_HALLOWED.get())));
    public static final IRegistryItem<Block, Block> MARBLE_HALLOWED_RUNED = register("marble_hallowed_runed", () -> new Block(Block.Properties.ofFullCopy(MARBLE_HALLOWED.get())));
    public static final IRegistryItem<Block, CarvedBookshelfBlock> MARBLE_HALLOWED_BOOKSHELF = register("marble_hallowed_bookshelf", () -> new CarvedBookshelfBlock(Block.Properties.ofFullCopy(MARBLE_HALLOWED.get())));

    // Register sunwood blocks
    public static final IRegistryItem<Block, SunwoodLogBlock> STRIPPED_SUNWOOD_LOG = register("stripped_sunwood_log", () -> new SunwoodLogBlock(null));
    public static final IRegistryItem<Block, SunwoodLogBlock> SUNWOOD_LOG = register("sunwood_log", () -> new SunwoodLogBlock(STRIPPED_SUNWOOD_LOG.get()));
    public static final IRegistryItem<Block, SunwoodLogBlock> STRIPPED_SUNWOOD_WOOD = register("stripped_sunwood_wood", () -> new SunwoodLogBlock(null));
    public static final IRegistryItem<Block, SunwoodLogBlock> SUNWOOD_WOOD = register("sunwood_wood", () -> new SunwoodLogBlock(STRIPPED_SUNWOOD_WOOD.get()));
    public static final IRegistryItem<Block, SunwoodLeavesBlock> SUNWOOD_LEAVES = register("sunwood_leaves", SunwoodLeavesBlock::new);
    public static final IRegistryItem<Block, SaplingBlockPM> SUNWOOD_SAPLING = register("sunwood_sapling", () -> new SaplingBlockPM(TreeGrowersPM.SUNWOOD, BlockTagsPM.MAY_PLACE_SUNWOOD_SAPLINGS, Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).isRedstoneConductor((state, getter, pos) -> false).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final IRegistryItem<Block, SunwoodPlanksBlock> SUNWOOD_PLANKS = register("sunwood_planks", SunwoodPlanksBlock::new);
    public static final IRegistryItem<Block, SunwoodSlabBlock> SUNWOOD_SLAB = register("sunwood_slab", () -> new SunwoodSlabBlock(Block.Properties.ofFullCopy(SUNWOOD_PLANKS.get())));
    public static final IRegistryItem<Block, SunwoodStairsBlock> SUNWOOD_STAIRS = register("sunwood_stairs", () -> new SunwoodStairsBlock(SUNWOOD_PLANKS.get().defaultBlockState(), Block.Properties.ofFullCopy(SUNWOOD_PLANKS.get())));
    public static final IRegistryItem<Block, SunwoodPillarBlock> SUNWOOD_PILLAR = register("sunwood_pillar", SunwoodPillarBlock::new);
    public static final IRegistryItem<Block, FlowerPotBlock> POTTED_SUNWOOD_SAPLING = register("potted_sunwood_sapling", Services.BLOCK_PROTOTYPES.flowerPot(() -> (FlowerPotBlock)Blocks.FLOWER_POT, SUNWOOD_SAPLING, Block.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)));

    // Register moonwood blocks
    public static final IRegistryItem<Block, MoonwoodLogBlock> STRIPPED_MOONWOOD_LOG = register("stripped_moonwood_log", () -> new MoonwoodLogBlock(null));
    public static final IRegistryItem<Block, MoonwoodLogBlock> MOONWOOD_LOG = register("moonwood_log", () -> new MoonwoodLogBlock(STRIPPED_MOONWOOD_LOG.get()));
    public static final IRegistryItem<Block, MoonwoodLogBlock> STRIPPED_MOONWOOD_WOOD = register("stripped_moonwood_wood", () -> new MoonwoodLogBlock(null));
    public static final IRegistryItem<Block, MoonwoodLogBlock> MOONWOOD_WOOD = register("moonwood_wood", () -> new MoonwoodLogBlock(STRIPPED_MOONWOOD_WOOD.get()));
    public static final IRegistryItem<Block, MoonwoodLeavesBlock> MOONWOOD_LEAVES = register("moonwood_leaves", MoonwoodLeavesBlock::new);
    public static final IRegistryItem<Block, SaplingBlockPM> MOONWOOD_SAPLING = register("moonwood_sapling", () -> new SaplingBlockPM(TreeGrowersPM.MOONWOOD, BlockTagsPM.MAY_PLACE_MOONWOOD_SAPLINGS, Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).isRedstoneConductor((state, getter, pos) -> false).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final IRegistryItem<Block, MoonwoodPlanksBlock> MOONWOOD_PLANKS = register("moonwood_planks", MoonwoodPlanksBlock::new);
    public static final IRegistryItem<Block, MoonwoodSlabBlock> MOONWOOD_SLAB = register("moonwood_slab", () -> new MoonwoodSlabBlock(Block.Properties.ofFullCopy(MOONWOOD_PLANKS.get())));
    public static final IRegistryItem<Block, MoonwoodStairsBlock> MOONWOOD_STAIRS = register("moonwood_stairs", () -> new MoonwoodStairsBlock(MOONWOOD_PLANKS.get().defaultBlockState(), Block.Properties.ofFullCopy(MOONWOOD_PLANKS.get())));
    public static final IRegistryItem<Block, MoonwoodPillarBlock> MOONWOOD_PILLAR = register("moonwood_pillar", MoonwoodPillarBlock::new);
    public static final IRegistryItem<Block, FlowerPotBlock> POTTED_MOONWOOD_SAPLING = register("potted_moonwood_sapling", Services.BLOCK_PROTOTYPES.flowerPot(() -> (FlowerPotBlock)Blocks.FLOWER_POT, MOONWOOD_SAPLING, Block.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)));

    // Register hallowood blocks
    public static final IRegistryItem<Block, RotatedPillarBlock> STRIPPED_HALLOWOOD_LOG = register("stripped_hallowood_log", () -> new RotatedPillarBlock(Block.Properties.of().mapColor(MapColor.GOLD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
    public static final IRegistryItem<Block, StrippableLogBlock> HALLOWOOD_LOG = register("hallowood_log", () -> new StrippableLogBlock(STRIPPED_HALLOWOOD_LOG.get(), Block.Properties.of().mapColor(MapColor.GOLD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
    public static final IRegistryItem<Block, RotatedPillarBlock> STRIPPED_HALLOWOOD_WOOD = register("stripped_hallowood_wood", () -> new RotatedPillarBlock(Block.Properties.of().mapColor(MapColor.GOLD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
    public static final IRegistryItem<Block, StrippableLogBlock> HALLOWOOD_WOOD = register("hallowood_wood", () -> new StrippableLogBlock(STRIPPED_HALLOWOOD_WOOD.get(), Block.Properties.of().mapColor(MapColor.GOLD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
    public static final IRegistryItem<Block, LeavesBlock> HALLOWOOD_LEAVES = register("hallowood_leaves", () -> new LeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor((state, getter, pos) -> false).strength(0.2F).noOcclusion().sound(SoundType.GRASS).lightLevel(state -> 10).isSuffocating(BlocksPM::never).isViewBlocking(BlocksPM::never).isValidSpawn(BlocksPM::allowsSpawnOnLeaves)));
    public static final IRegistryItem<Block, SaplingBlockPM> HALLOWOOD_SAPLING = register("hallowood_sapling", () -> new SaplingBlockPM(TreeGrowersPM.HALLOWOOD, BlockTagsPM.MAY_PLACE_HALLOWOOD_SAPLINGS, Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).isRedstoneConductor((state, getter, pos) -> false).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final IRegistryItem<Block, Block> HALLOWOOD_PLANKS = register("hallowood_planks", () -> new Block(Block.Properties.of().mapColor(MapColor.GOLD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final IRegistryItem<Block, SlabBlock> HALLOWOOD_SLAB = register("hallowood_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(HALLOWOOD_PLANKS.get())));
    public static final IRegistryItem<Block, StairBlock> HALLOWOOD_STAIRS = register("hallowood_stairs", () -> new StairBlockPM(HALLOWOOD_PLANKS.get().defaultBlockState(), Block.Properties.ofFullCopy(HALLOWOOD_PLANKS.get())));
    public static final IRegistryItem<Block, PillarBlock> HALLOWOOD_PILLAR = register("hallowood_pillar", () -> new PillarBlock(Block.Properties.of().mapColor(MapColor.GOLD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
    public static final IRegistryItem<Block, FlowerPotBlock> POTTED_HALLOWOOD_SAPLING = register("potted_hallowood_sapling", Services.BLOCK_PROTOTYPES.flowerPot(() -> (FlowerPotBlock)Blocks.FLOWER_POT, HALLOWOOD_SAPLING, Block.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)));

    // Register crop blocks
    public static final IRegistryItem<Block, HydromelonBlock> HYDROMELON = register("hydromelon", () -> new HydromelonBlock(Block.Properties.of().mapColor(MapColor.COLOR_BLUE).strength(1.0F).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, AttachedStemBlock> ATTACHED_HYDROMELON_STEM = register("attached_hydromelon_stem", () -> new AttachedStemBlockPM(BlockReferencesPM.HYDROMELON_STEM, BlockReferencesPM.HYDROMELON, ItemReferencesPM.HYDROMELON_SEEDS, Block.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, StemBlock> HYDROMELON_STEM = register("hydromelon_stem", () -> new StemBlockPM(BlockReferencesPM.HYDROMELON, BlockReferencesPM.ATTACHED_HYDROMELON_STEM, ItemReferencesPM.HYDROMELON_SEEDS, Block.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.HARD_CROP).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BloodRoseBlock> BLOOD_ROSE = register("blood_rose", () -> new BloodRoseBlock(Block.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).ignitedByLava().pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, EmberflowerBlock> EMBERFLOWER = register("emberflower", () -> new EmberflowerBlock(Block.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY).lightLevel(state -> 10)));

    // Register infused stone
    public static final IRegistryItem<Block, Block> INFUSED_STONE_EARTH = register("infused_stone_earth", () -> new Block(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, Block> INFUSED_STONE_SEA = register("infused_stone_sea", () -> new Block(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, Block> INFUSED_STONE_SKY = register("infused_stone_sky", () -> new Block(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, Block> INFUSED_STONE_SUN = register("infused_stone_sun", () -> new Block(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, Block> INFUSED_STONE_MOON = register("infused_stone_moon", () -> new Block(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));

    // Register budding gem blocks
    public static final IRegistryItem<Block, BuddingGemClusterBlock> SYNTHETIC_AMETHYST_CLUSTER = register("synthetic_amethyst_cluster", () -> new BuddingGemClusterBlock(7, 3, GemBudType.AMETHYST, Optional.empty(), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.AMETHYST_CLUSTER).strength(1.5F).lightLevel(state -> 5).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemClusterBlock> LARGE_SYNTHETIC_AMETHYST_BUD = register("large_synthetic_amethyst_bud", () -> new BuddingGemClusterBlock(5, 3, GemBudType.AMETHYST, Optional.of(SYNTHETIC_AMETHYST_CLUSTER), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.LARGE_AMETHYST_BUD).strength(1.5F).lightLevel(state -> 4).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemClusterBlock> MEDIUM_SYNTHETIC_AMETHYST_BUD = register("medium_synthetic_amethyst_bud", () -> new BuddingGemClusterBlock(4, 3, GemBudType.AMETHYST, Optional.of(LARGE_SYNTHETIC_AMETHYST_BUD), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.MEDIUM_AMETHYST_BUD).strength(1.5F).lightLevel(state -> 2).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemClusterBlock> SMALL_SYNTHETIC_AMETHYST_BUD = register("small_synthetic_amethyst_bud", () -> new BuddingGemClusterBlock(3, 4, GemBudType.AMETHYST, Optional.of(MEDIUM_SYNTHETIC_AMETHYST_BUD), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.SMALL_AMETHYST_BUD).strength(1.5F).lightLevel(state -> 1).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemSourceBlock> DAMAGED_BUDDING_AMETHYST_BLOCK = register("damaged_budding_amethyst_block", () -> new BuddingGemSourceBlock(GemBudType.AMETHYST, SMALL_SYNTHETIC_AMETHYST_BUD::get, () -> Blocks.AMETHYST_BLOCK, 0.08F, Block.Properties.of().mapColor(MapColor.COLOR_PURPLE).randomTicks().strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final IRegistryItem<Block, BuddingGemSourceBlock> CHIPPED_BUDDING_AMETHYST_BLOCK = register("chipped_budding_amethyst_block", () -> new BuddingGemSourceBlock(GemBudType.AMETHYST, SMALL_SYNTHETIC_AMETHYST_BUD::get, DAMAGED_BUDDING_AMETHYST_BLOCK::get, 0.08F, Block.Properties.of().mapColor(MapColor.COLOR_PURPLE).randomTicks().strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final IRegistryItem<Block, BuddingGemSourceBlock> FLAWED_BUDDING_AMETHYST_BLOCK = register("flawed_budding_amethyst_block", () -> new BuddingGemSourceBlock(GemBudType.AMETHYST, SMALL_SYNTHETIC_AMETHYST_BUD::get, CHIPPED_BUDDING_AMETHYST_BLOCK::get, 0.08F, Block.Properties.of().mapColor(MapColor.COLOR_PURPLE).randomTicks().strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final IRegistryItem<Block, BuddingGemClusterBlock> SYNTHETIC_DIAMOND_CLUSTER = register("synthetic_diamond_cluster", () -> new BuddingGemClusterBlock(7, 3, GemBudType.DIAMOND, Optional.empty(), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.AMETHYST_CLUSTER).strength(1.5F).lightLevel(state -> 5).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemClusterBlock> LARGE_SYNTHETIC_DIAMOND_BUD = register("large_synthetic_diamond_bud", () -> new BuddingGemClusterBlock(5, 3, GemBudType.DIAMOND, Optional.of(SYNTHETIC_DIAMOND_CLUSTER), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.LARGE_AMETHYST_BUD).strength(1.5F).lightLevel(state -> 4).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemClusterBlock> MEDIUM_SYNTHETIC_DIAMOND_BUD = register("medium_synthetic_diamond_bud", () -> new BuddingGemClusterBlock(4, 3, GemBudType.DIAMOND, Optional.of(LARGE_SYNTHETIC_DIAMOND_BUD), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.MEDIUM_AMETHYST_BUD).strength(1.5F).lightLevel(state -> 2).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemClusterBlock> SMALL_SYNTHETIC_DIAMOND_BUD = register("small_synthetic_diamond_bud", () -> new BuddingGemClusterBlock(3, 4, GemBudType.DIAMOND, Optional.of(MEDIUM_SYNTHETIC_DIAMOND_BUD), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.SMALL_AMETHYST_BUD).strength(1.5F).lightLevel(state -> 1).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemSourceBlock> DAMAGED_BUDDING_DIAMOND_BLOCK = register("damaged_budding_diamond_block", () -> new BuddingGemSourceBlock(GemBudType.DIAMOND, SMALL_SYNTHETIC_DIAMOND_BUD::get, () -> Blocks.DIAMOND_BLOCK, 0.08F, Block.Properties.of().mapColor(MapColor.DIAMOND).randomTicks().strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final IRegistryItem<Block, BuddingGemSourceBlock> CHIPPED_BUDDING_DIAMOND_BLOCK = register("chipped_budding_diamond_block", () -> new BuddingGemSourceBlock(GemBudType.DIAMOND, SMALL_SYNTHETIC_DIAMOND_BUD::get, DAMAGED_BUDDING_DIAMOND_BLOCK::get, 0.08F, Block.Properties.of().mapColor(MapColor.DIAMOND).randomTicks().strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final IRegistryItem<Block, BuddingGemSourceBlock> FLAWED_BUDDING_DIAMOND_BLOCK = register("flawed_budding_diamond_block", () -> new BuddingGemSourceBlock(GemBudType.DIAMOND, SMALL_SYNTHETIC_DIAMOND_BUD::get, CHIPPED_BUDDING_DIAMOND_BLOCK::get, 0.08F, Block.Properties.of().mapColor(MapColor.DIAMOND).randomTicks().strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final IRegistryItem<Block, BuddingGemClusterBlock> SYNTHETIC_EMERALD_CLUSTER = register("synthetic_emerald_cluster", () -> new BuddingGemClusterBlock(7, 3, GemBudType.EMERALD, Optional.empty(), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.AMETHYST_CLUSTER).strength(1.5F).lightLevel(state -> 5).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemClusterBlock> LARGE_SYNTHETIC_EMERALD_BUD = register("large_synthetic_emerald_bud", () -> new BuddingGemClusterBlock(5, 3, GemBudType.EMERALD, Optional.of(SYNTHETIC_EMERALD_CLUSTER), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.LARGE_AMETHYST_BUD).strength(1.5F).lightLevel(state -> 4).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemClusterBlock> MEDIUM_SYNTHETIC_EMERALD_BUD = register("medium_synthetic_emerald_bud", () -> new BuddingGemClusterBlock(4, 3, GemBudType.EMERALD, Optional.of(LARGE_SYNTHETIC_EMERALD_BUD), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.MEDIUM_AMETHYST_BUD).strength(1.5F).lightLevel(state -> 2).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemClusterBlock> SMALL_SYNTHETIC_EMERALD_BUD = register("small_synthetic_emerald_bud", () -> new BuddingGemClusterBlock(3, 4, GemBudType.EMERALD, Optional.of(MEDIUM_SYNTHETIC_EMERALD_BUD), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.SMALL_AMETHYST_BUD).strength(1.5F).lightLevel(state -> 1).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemSourceBlock> DAMAGED_BUDDING_EMERALD_BLOCK = register("damaged_budding_emerald_block", () -> new BuddingGemSourceBlock(GemBudType.EMERALD, SMALL_SYNTHETIC_EMERALD_BUD::get, () -> Blocks.EMERALD_BLOCK, 0.08F, Block.Properties.of().mapColor(MapColor.EMERALD).randomTicks().strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final IRegistryItem<Block, BuddingGemSourceBlock> CHIPPED_BUDDING_EMERALD_BLOCK = register("chipped_budding_emerald_block", () -> new BuddingGemSourceBlock(GemBudType.EMERALD, SMALL_SYNTHETIC_EMERALD_BUD::get, DAMAGED_BUDDING_EMERALD_BLOCK::get, 0.08F, Block.Properties.of().mapColor(MapColor.EMERALD).randomTicks().strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final IRegistryItem<Block, BuddingGemSourceBlock> FLAWED_BUDDING_EMERALD_BLOCK = register("flawed_budding_emerald_block", () -> new BuddingGemSourceBlock(GemBudType.EMERALD, SMALL_SYNTHETIC_EMERALD_BUD::get, CHIPPED_BUDDING_EMERALD_BLOCK::get, 0.08F, Block.Properties.of().mapColor(MapColor.EMERALD).randomTicks().strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final IRegistryItem<Block, BuddingGemClusterBlock> SYNTHETIC_QUARTZ_CLUSTER = register("synthetic_quartz_cluster", () -> new BuddingGemClusterBlock(7, 3, GemBudType.QUARTZ, Optional.empty(), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.AMETHYST_CLUSTER).strength(1.5F).lightLevel(state -> 5).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemClusterBlock> LARGE_SYNTHETIC_QUARTZ_BUD = register("large_synthetic_quartz_bud", () -> new BuddingGemClusterBlock(5, 3, GemBudType.QUARTZ, Optional.of(SYNTHETIC_QUARTZ_CLUSTER), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.LARGE_AMETHYST_BUD).strength(1.5F).lightLevel(state -> 4).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemClusterBlock> MEDIUM_SYNTHETIC_QUARTZ_BUD = register("medium_synthetic_quartz_bud", () -> new BuddingGemClusterBlock(4, 3, GemBudType.QUARTZ, Optional.of(LARGE_SYNTHETIC_QUARTZ_BUD), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.MEDIUM_AMETHYST_BUD).strength(1.5F).lightLevel(state -> 2).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemClusterBlock> SMALL_SYNTHETIC_QUARTZ_BUD = register("small_synthetic_quartz_bud", () -> new BuddingGemClusterBlock(3, 4, GemBudType.QUARTZ, Optional.of(MEDIUM_SYNTHETIC_QUARTZ_BUD), Block.Properties.of().forceSolidOn().noOcclusion().randomTicks().sound(SoundType.SMALL_AMETHYST_BUD).strength(1.5F).lightLevel(state -> 1).pushReaction(PushReaction.DESTROY)));
    public static final IRegistryItem<Block, BuddingGemSourceBlock> DAMAGED_BUDDING_QUARTZ_BLOCK = register("damaged_budding_quartz_block", () -> new BuddingGemSourceBlock(GemBudType.QUARTZ, SMALL_SYNTHETIC_QUARTZ_BUD::get, () -> Blocks.QUARTZ_BLOCK, 0.08F, Block.Properties.of().mapColor(MapColor.QUARTZ).randomTicks().strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final IRegistryItem<Block, BuddingGemSourceBlock> CHIPPED_BUDDING_QUARTZ_BLOCK = register("chipped_budding_quartz_block", () -> new BuddingGemSourceBlock(GemBudType.QUARTZ, SMALL_SYNTHETIC_QUARTZ_BUD::get, DAMAGED_BUDDING_QUARTZ_BLOCK::get, 0.08F, Block.Properties.of().mapColor(MapColor.QUARTZ).randomTicks().strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final IRegistryItem<Block, BuddingGemSourceBlock> FLAWED_BUDDING_QUARTZ_BLOCK = register("flawed_budding_quartz_block", () -> new BuddingGemSourceBlock(GemBudType.QUARTZ, SMALL_SYNTHETIC_QUARTZ_BUD::get, CHIPPED_BUDDING_QUARTZ_BLOCK::get, 0.08F, Block.Properties.of().mapColor(MapColor.QUARTZ).randomTicks().strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));

    // Register skyglass
    public static final IRegistryItem<Block, SkyglassBlock> SKYGLASS = register("skyglass", () -> new SkyglassBlock(Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_BLACK = register("stained_skyglass_black", () -> new StainedSkyglassBlock(DyeColor.BLACK, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_BLUE = register("stained_skyglass_blue", () -> new StainedSkyglassBlock(DyeColor.BLUE, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_BROWN = register("stained_skyglass_brown", () -> new StainedSkyglassBlock(DyeColor.BROWN, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_CYAN = register("stained_skyglass_cyan", () -> new StainedSkyglassBlock(DyeColor.CYAN, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_GRAY = register("stained_skyglass_gray", () -> new StainedSkyglassBlock(DyeColor.GRAY, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_GREEN = register("stained_skyglass_green", () -> new StainedSkyglassBlock(DyeColor.GREEN, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_LIGHT_BLUE = register("stained_skyglass_light_blue", () -> new StainedSkyglassBlock(DyeColor.LIGHT_BLUE, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_LIGHT_GRAY = register("stained_skyglass_light_gray", () -> new StainedSkyglassBlock(DyeColor.LIGHT_GRAY, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_LIME = register("stained_skyglass_lime", () -> new StainedSkyglassBlock(DyeColor.LIME, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_MAGENTA = register("stained_skyglass_magenta", () -> new StainedSkyglassBlock(DyeColor.MAGENTA, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_ORANGE = register("stained_skyglass_orange", () -> new StainedSkyglassBlock(DyeColor.ORANGE, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_PINK = register("stained_skyglass_pink", () -> new StainedSkyglassBlock(DyeColor.PINK, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_PURPLE = register("stained_skyglass_purple", () -> new StainedSkyglassBlock(DyeColor.PURPLE, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_RED = register("stained_skyglass_red", () -> new StainedSkyglassBlock(DyeColor.RED, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_WHITE = register("stained_skyglass_white", () -> new StainedSkyglassBlock(DyeColor.WHITE, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassBlock> STAINED_SKYGLASS_YELLOW = register("stained_skyglass_yellow", () -> new StainedSkyglassBlock(DyeColor.YELLOW, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));

    // Register skyglass panes
    public static final IRegistryItem<Block, SkyglassPaneBlock> SKYGLASS_PANE = register("skyglass_pane", () -> new SkyglassPaneBlock(Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_BLACK = register("stained_skyglass_pane_black", () -> new StainedSkyglassPaneBlock(DyeColor.BLACK, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_BLUE = register("stained_skyglass_pane_blue", () -> new StainedSkyglassPaneBlock(DyeColor.BLUE, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_BROWN = register("stained_skyglass_pane_brown", () -> new StainedSkyglassPaneBlock(DyeColor.BROWN, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_CYAN = register("stained_skyglass_pane_cyan", () -> new StainedSkyglassPaneBlock(DyeColor.CYAN, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_GRAY = register("stained_skyglass_pane_gray", () -> new StainedSkyglassPaneBlock(DyeColor.GRAY, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_GREEN = register("stained_skyglass_pane_green", () -> new StainedSkyglassPaneBlock(DyeColor.GREEN, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_LIGHT_BLUE = register("stained_skyglass_pane_light_blue", () -> new StainedSkyglassPaneBlock(DyeColor.LIGHT_BLUE, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_LIGHT_GRAY = register("stained_skyglass_pane_light_gray", () -> new StainedSkyglassPaneBlock(DyeColor.LIGHT_GRAY, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_LIME = register("stained_skyglass_pane_lime", () -> new StainedSkyglassPaneBlock(DyeColor.LIME, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_MAGENTA = register("stained_skyglass_pane_magenta", () -> new StainedSkyglassPaneBlock(DyeColor.MAGENTA, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_ORANGE = register("stained_skyglass_pane_orange", () -> new StainedSkyglassPaneBlock(DyeColor.ORANGE, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_PINK = register("stained_skyglass_pane_pink", () -> new StainedSkyglassPaneBlock(DyeColor.PINK, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_PURPLE = register("stained_skyglass_pane_purple", () -> new StainedSkyglassPaneBlock(DyeColor.PURPLE, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_RED = register("stained_skyglass_pane_red", () -> new StainedSkyglassPaneBlock(DyeColor.RED, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_WHITE = register("stained_skyglass_pane_white", () -> new StainedSkyglassPaneBlock(DyeColor.WHITE, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final IRegistryItem<Block, StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_YELLOW = register("stained_skyglass_pane_yellow", () -> new StainedSkyglassPaneBlock(DyeColor.YELLOW, Block.Properties.of().instrument(NoteBlockInstrument.HAT).isRedstoneConductor((state, getter, pos) -> false).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));

    // Register ritual candles
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_BLACK = register("ritual_candle_black", () -> new RitualCandleBlock(DyeColor.BLACK, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_BLUE = register("ritual_candle_blue", () -> new RitualCandleBlock(DyeColor.BLUE, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_BROWN = register("ritual_candle_brown", () -> new RitualCandleBlock(DyeColor.BROWN, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_CYAN = register("ritual_candle_cyan", () -> new RitualCandleBlock(DyeColor.CYAN, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_GRAY = register("ritual_candle_gray", () -> new RitualCandleBlock(DyeColor.GRAY, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_GREEN = register("ritual_candle_green", () -> new RitualCandleBlock(DyeColor.GREEN, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_LIGHT_BLUE = register("ritual_candle_light_blue", () -> new RitualCandleBlock(DyeColor.LIGHT_BLUE, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_LIGHT_GRAY = register("ritual_candle_light_gray", () -> new RitualCandleBlock(DyeColor.LIGHT_GRAY, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_LIME = register("ritual_candle_lime", () -> new RitualCandleBlock(DyeColor.LIME, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_MAGENTA = register("ritual_candle_magenta", () -> new RitualCandleBlock(DyeColor.MAGENTA, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_ORANGE = register("ritual_candle_orange", () -> new RitualCandleBlock(DyeColor.ORANGE, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_PINK = register("ritual_candle_pink", () -> new RitualCandleBlock(DyeColor.PINK, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_PURPLE = register("ritual_candle_purple", () -> new RitualCandleBlock(DyeColor.PURPLE, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_RED = register("ritual_candle_red", () -> new RitualCandleBlock(DyeColor.RED, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_WHITE = register("ritual_candle_white", () -> new RitualCandleBlock(DyeColor.WHITE, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));
    public static final IRegistryItem<Block, RitualCandleBlock> RITUAL_CANDLE_YELLOW = register("ritual_candle_yellow", () -> new RitualCandleBlock(DyeColor.YELLOW, Block.Properties.of().mapColor(MapColor.GRASS).pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 7).sound(SoundType.WOOL).noOcclusion()));

    // Register mana fonts
    public static final IRegistryItem<Block, AncientManaFontBlock> ANCIENT_FONT_EARTH = register("ancient_font_earth", () -> new AncientManaFontBlock(Sources.EARTH, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(-1.0F, 3600000.0F).sound(SoundType.STONE).lightLevel(state -> 15).noLootTable()));
    public static final IRegistryItem<Block, AncientManaFontBlock> ANCIENT_FONT_SEA = register("ancient_font_sea", () -> new AncientManaFontBlock(Sources.SEA, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(-1.0F, 3600000.0F).sound(SoundType.STONE).lightLevel(state -> 15).noLootTable()));
    public static final IRegistryItem<Block, AncientManaFontBlock> ANCIENT_FONT_SKY = register("ancient_font_sky", () -> new AncientManaFontBlock(Sources.SKY, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(-1.0F, 3600000.0F).sound(SoundType.STONE).lightLevel(state -> 15).noLootTable()));
    public static final IRegistryItem<Block, AncientManaFontBlock> ANCIENT_FONT_SUN = register("ancient_font_sun", () -> new AncientManaFontBlock(Sources.SUN, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(-1.0F, 3600000.0F).sound(SoundType.STONE).lightLevel(state -> 15).noLootTable()));
    public static final IRegistryItem<Block, AncientManaFontBlock> ANCIENT_FONT_MOON = register("ancient_font_moon", () -> new AncientManaFontBlock(Sources.MOON, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(-1.0F, 3600000.0F).sound(SoundType.STONE).lightLevel(state -> 15).noLootTable()));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> ARTIFICIAL_FONT_EARTH = register("artificial_font_earth", () -> new ArtificialManaFontBlock(Sources.EARTH, DeviceTier.ENCHANTED, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> ARTIFICIAL_FONT_SEA = register("artificial_font_sea", () -> new ArtificialManaFontBlock(Sources.SEA, DeviceTier.ENCHANTED, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> ARTIFICIAL_FONT_SKY = register("artificial_font_sky", () -> new ArtificialManaFontBlock(Sources.SKY, DeviceTier.ENCHANTED, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> ARTIFICIAL_FONT_SUN = register("artificial_font_sun", () -> new ArtificialManaFontBlock(Sources.SUN, DeviceTier.ENCHANTED, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> ARTIFICIAL_FONT_MOON = register("artificial_font_moon", () -> new ArtificialManaFontBlock(Sources.MOON, DeviceTier.ENCHANTED, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> ARTIFICIAL_FONT_BLOOD = register("artificial_font_blood", () -> new ArtificialManaFontBlock(Sources.BLOOD, DeviceTier.ENCHANTED, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> ARTIFICIAL_FONT_INFERNAL = register("artificial_font_infernal", () -> new ArtificialManaFontBlock(Sources.INFERNAL, DeviceTier.ENCHANTED, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> ARTIFICIAL_FONT_VOID = register("artificial_font_void", () -> new ArtificialManaFontBlock(Sources.VOID, DeviceTier.ENCHANTED, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> ARTIFICIAL_FONT_HALLOWED = register("artificial_font_hallowed", () -> new ArtificialManaFontBlock(Sources.HALLOWED, DeviceTier.ENCHANTED, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> FORBIDDEN_FONT_EARTH = register("forbidden_font_earth", () -> new ArtificialManaFontBlock(Sources.EARTH, DeviceTier.FORBIDDEN, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> FORBIDDEN_FONT_SEA = register("forbidden_font_sea", () -> new ArtificialManaFontBlock(Sources.SEA, DeviceTier.FORBIDDEN, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> FORBIDDEN_FONT_SKY = register("forbidden_font_sky", () -> new ArtificialManaFontBlock(Sources.SKY, DeviceTier.FORBIDDEN, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> FORBIDDEN_FONT_SUN = register("forbidden_font_sun", () -> new ArtificialManaFontBlock(Sources.SUN, DeviceTier.FORBIDDEN, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> FORBIDDEN_FONT_MOON = register("forbidden_font_moon", () -> new ArtificialManaFontBlock(Sources.MOON, DeviceTier.FORBIDDEN, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> FORBIDDEN_FONT_BLOOD = register("forbidden_font_blood", () -> new ArtificialManaFontBlock(Sources.BLOOD, DeviceTier.FORBIDDEN, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> FORBIDDEN_FONT_INFERNAL = register("forbidden_font_infernal", () -> new ArtificialManaFontBlock(Sources.INFERNAL, DeviceTier.FORBIDDEN, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> FORBIDDEN_FONT_VOID = register("forbidden_font_void", () -> new ArtificialManaFontBlock(Sources.VOID, DeviceTier.FORBIDDEN, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> FORBIDDEN_FONT_HALLOWED = register("forbidden_font_hallowed", () -> new ArtificialManaFontBlock(Sources.HALLOWED, DeviceTier.FORBIDDEN, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> HEAVENLY_FONT_EARTH = register("heavenly_font_earth", () -> new ArtificialManaFontBlock(Sources.EARTH, DeviceTier.HEAVENLY, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> HEAVENLY_FONT_SEA = register("heavenly_font_sea", () -> new ArtificialManaFontBlock(Sources.SEA, DeviceTier.HEAVENLY, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> HEAVENLY_FONT_SKY = register("heavenly_font_sky", () -> new ArtificialManaFontBlock(Sources.SKY, DeviceTier.HEAVENLY, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> HEAVENLY_FONT_SUN = register("heavenly_font_sun", () -> new ArtificialManaFontBlock(Sources.SUN, DeviceTier.HEAVENLY, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> HEAVENLY_FONT_MOON = register("heavenly_font_moon", () -> new ArtificialManaFontBlock(Sources.MOON, DeviceTier.HEAVENLY, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> HEAVENLY_FONT_BLOOD = register("heavenly_font_blood", () -> new ArtificialManaFontBlock(Sources.BLOOD, DeviceTier.HEAVENLY, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> HEAVENLY_FONT_INFERNAL = register("heavenly_font_infernal", () -> new ArtificialManaFontBlock(Sources.INFERNAL, DeviceTier.HEAVENLY, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> HEAVENLY_FONT_VOID = register("heavenly_font_void", () -> new ArtificialManaFontBlock(Sources.VOID, DeviceTier.HEAVENLY, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));
    public static final IRegistryItem<Block, ArtificialManaFontBlock> HEAVENLY_FONT_HALLOWED = register("heavenly_font_hallowed", () -> new ArtificialManaFontBlock(Sources.HALLOWED, DeviceTier.HEAVENLY, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel(state -> 15)));

    // Register devices
    public static final IRegistryItem<Block, ArcaneWorkbenchBlock> ARCANE_WORKBENCH = register("arcane_workbench", ArcaneWorkbenchBlock::new);
    public static final IRegistryItem<Block, WandAssemblyTableBlock> WAND_ASSEMBLY_TABLE = register("wand_assembly_table", WandAssemblyTableBlock::new);
    public static final IRegistryItem<Block, WoodTableBlock> WOOD_TABLE = register("wood_table", WoodTableBlock::new);
    public static final IRegistryItem<Block, AnalysisTableBlock> ANALYSIS_TABLE = register("analysis_table", AnalysisTableBlock::new);
    public static final IRegistryItem<Block, EssenceFurnaceBlock> ESSENCE_FURNACE = register("essence_furnace", () -> new EssenceFurnaceBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.5F).lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, CalcinatorBlock> CALCINATOR_BASIC = register("calcinator_basic", () -> new CalcinatorBlock(DeviceTier.BASIC, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.5F).lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, CalcinatorBlock> CALCINATOR_ENCHANTED = register("calcinator_enchanted", () -> new CalcinatorBlock(DeviceTier.ENCHANTED, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.5F).lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, CalcinatorBlock> CALCINATOR_FORBIDDEN = register("calcinator_forbidden", () -> new CalcinatorBlock(DeviceTier.FORBIDDEN, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.5F).lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, CalcinatorBlock> CALCINATOR_HEAVENLY = register("calcinator_heavenly", () -> new CalcinatorBlock(DeviceTier.HEAVENLY, Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.5F).lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, WandInscriptionTableBlock> WAND_INSCRIPTION_TABLE = register("wand_inscription_table", WandInscriptionTableBlock::new);
    public static final IRegistryItem<Block, SpellcraftingAltarBlock> SPELLCRAFTING_ALTAR = register("spellcrafting_altar", () -> new SpellcraftingAltarBlock(Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE).noOcclusion()));
    public static final IRegistryItem<Block, WandChargerBlock> WAND_CHARGER = register("wand_charger", () -> new WandChargerBlock(Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE).noOcclusion()));
    public static final IRegistryItem<Block, ResearchTableBlock> RESEARCH_TABLE = register("research_table", () -> new ResearchTableBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(1.5F, 6.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final IRegistryItem<Block, SunlampBlock> SUNLAMP = register("sunlamp", () -> new SunlampBlock(BlockReferencesPM.GLOW_FIELD, Block.Properties.of().mapColor(MapColor.METAL).pushReaction(PushReaction.DESTROY).strength(1.5F, 6.0F).sound(SoundType.LANTERN).lightLevel(state -> 15).noOcclusion()));
    public static final IRegistryItem<Block, SunlampBlock> SPIRIT_LANTERN = register("spirit_lantern", () -> new SunlampBlock(BlockReferencesPM.SOUL_GLOW_FIELD, Block.Properties.of().mapColor(MapColor.METAL).pushReaction(PushReaction.DESTROY).strength(1.5F, 6.0F).sound(SoundType.LANTERN).lightLevel(state -> 15).noOcclusion()));
    public static final IRegistryItem<Block, RitualAltarBlock> RITUAL_ALTAR = register("ritual_altar", () -> new RitualAltarBlock(Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE).noOcclusion()));
    public static final IRegistryItem<Block, OfferingPedestalBlock> OFFERING_PEDESTAL = register("offering_pedestal", () -> new OfferingPedestalBlock(Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE).noOcclusion()));
    public static final IRegistryItem<Block, IncenseBrazierBlock> INCENSE_BRAZIER = register("incense_brazier", () -> new IncenseBrazierBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(1.5F, 6.0F).sound(SoundType.METAL).lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 7 : 0)));
    public static final IRegistryItem<Block, RitualLecternBlock> RITUAL_LECTERN = register("ritual_lectern", () -> new RitualLecternBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(1.5F, 6.0F).sound(SoundType.WOOD)));
    public static final IRegistryItem<Block, RitualBellBlock> RITUAL_BELL = register("ritual_bell", () -> new RitualBellBlock(Block.Properties.of().mapColor(MapColor.COLOR_CYAN).pushReaction(PushReaction.DESTROY).strength(1.5F, 6.0F).sound(SoundType.ANVIL)));
    public static final IRegistryItem<Block, BloodletterBlock> BLOODLETTER = register("bloodletter", () -> new BloodletterBlock(Block.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, SoulAnvilBlock> SOUL_ANVIL = register("soul_anvil", () -> new SoulAnvilBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).pushReaction(PushReaction.BLOCK).strength(1.5F, 6.0F).sound(SoundType.ANVIL)));
    public static final IRegistryItem<Block, RunescribingAltarBlock> RUNESCRIBING_ALTAR_BASIC = register("runescribing_altar_basic", () -> new RunescribingAltarBlock(DeviceTier.BASIC, Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, RunescribingAltarBlock> RUNESCRIBING_ALTAR_ENCHANTED = register("runescribing_altar_enchanted", () -> new RunescribingAltarBlock(DeviceTier.ENCHANTED, Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, RunescribingAltarBlock> RUNESCRIBING_ALTAR_FORBIDDEN = register("runescribing_altar_forbidden", () -> new RunescribingAltarBlock(DeviceTier.FORBIDDEN, Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, RunescribingAltarBlock> RUNESCRIBING_ALTAR_HEAVENLY = register("runescribing_altar_heavenly", () -> new RunescribingAltarBlock(DeviceTier.HEAVENLY, Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, RunecarvingTableBlock> RUNECARVING_TABLE = register("runecarving_table", () -> new RunecarvingTableBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(1.5F, 6.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final IRegistryItem<Block, RunicGrindstoneBlock> RUNIC_GRINDSTONE = register("runic_grindstone", RunicGrindstoneBlock::new);
    public static final IRegistryItem<Block, HoneyExtractorBlock> HONEY_EXTRACTOR = register("honey_extractor", () -> new HoneyExtractorBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(1.5F, 6.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final IRegistryItem<Block, PrimaliteGolemControllerBlock> PRIMALITE_GOLEM_CONTROLLER = register("primalite_golem_controller", () -> new PrimaliteGolemControllerBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(1.5F, 6.0F).sound(SoundType.METAL)));
    public static final IRegistryItem<Block, HexiumGolemControllerBlock> HEXIUM_GOLEM_CONTROLLER = register("hexium_golem_controller", () -> new HexiumGolemControllerBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(1.5F, 6.0F).sound(SoundType.METAL)));
    public static final IRegistryItem<Block, HallowsteelGolemControllerBlock> HALLOWSTEEL_GOLEM_CONTROLLER = register("hallowsteel_golem_controller", () -> new HallowsteelGolemControllerBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(1.5F, 6.0F).sound(SoundType.METAL)));
    public static final IRegistryItem<Block, SanguineCrucibleBlock> SANGUINE_CRUCIBLE = register("sanguine_crucible", () -> new SanguineCrucibleBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(1.5F, 6.0F).lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0).sound(SoundType.METAL)));
    public static final IRegistryItem<Block, ConcocterBlock> CONCOCTER = register("concocter", () -> new ConcocterBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(1.5F, 6.0F).lightLevel(state -> 1).noOcclusion()));
    public static final IRegistryItem<Block, CelestialHarpBlock> CELESTIAL_HARP = register("celestial_harp", () -> new CelestialHarpBlock(Block.Properties.of().mapColor(MapColor.GOLD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(1.5F, 6.0F).sound(SoundType.WOOD)));
    public static final IRegistryItem<Block, EntropySinkBlock> ENTROPY_SINK = register("entropy_sink", () -> new EntropySinkBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F).lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0).sound(SoundType.STONE).noOcclusion()));
    public static final IRegistryItem<Block, AutoChargerBlock> AUTO_CHARGER = register("auto_charger", () -> new AutoChargerBlock(Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 12.0F).sound(SoundType.STONE).noOcclusion()));
    public static final IRegistryItem<Block, EssenceTransmuterBlock> ESSENCE_TRANSMUTER = register("essence_transmuter", () -> new EssenceTransmuterBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(1.5F, 6.0F).sound(SoundType.METAL).noOcclusion()));
    public static final IRegistryItem<Block, DissolutionChamberBlock> DISSOLUTION_CHAMBER = register("dissolution_chamber", () -> new DissolutionChamberBlock(Block.Properties.of().mapColor(MapColor.DIAMOND).strength(1.5F, 6.0F).sound(SoundType.METAL).noOcclusion()));
    public static final IRegistryItem<Block, ZephyrEngineBlock> ZEPHYR_ENGINE = register("zephyr_engine", () -> new ZephyrEngineBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(1.5F, 6.0F).sound(SoundType.WOOD)));
    public static final IRegistryItem<Block, VoidTurbineBlock> VOID_TURBINE = register("void_turbine", () -> new VoidTurbineBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(1.5F, 6.0F).sound(SoundType.WOOD)));
    public static final IRegistryItem<Block, EssenceCaskBlock> ESSENCE_CASK_ENCHANTED = register("essence_cask_enchanted", () -> new EssenceCaskBlock(DeviceTier.ENCHANTED, Block.Properties.of().mapColor(MapColor.COLOR_BROWN).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD)));
    public static final IRegistryItem<Block, EssenceCaskBlock> ESSENCE_CASK_FORBIDDEN = register("essence_cask_forbidden", () -> new EssenceCaskBlock(DeviceTier.FORBIDDEN, Block.Properties.of().mapColor(MapColor.COLOR_BROWN).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD)));
    public static final IRegistryItem<Block, EssenceCaskBlock> ESSENCE_CASK_HEAVENLY = register("essence_cask_heavenly", () -> new EssenceCaskBlock(DeviceTier.HEAVENLY, Block.Properties.of().mapColor(MapColor.COLOR_BROWN).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD)));
    public static final IRegistryItem<Block, WandGlamourTableBlock> WAND_GLAMOUR_TABLE = register("wand_glamour_table", WandGlamourTableBlock::new);
    public static final IRegistryItem<Block, InfernalFurnaceBlock> INFERNAL_FURNACE = register("infernal_furnace", () -> new InfernalFurnaceBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.5F).lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0).sound(SoundType.STONE)));
    public static final IRegistryItem<Block, ManaBatteryBlock> MANA_NEXUS = register("mana_nexus", () -> new ManaBatteryBlock(DeviceTier.FORBIDDEN, Block.Properties.of().mapColor(MapColor.DIAMOND).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE).noOcclusion()));
    public static final IRegistryItem<Block, ManaBatteryBlock> MANA_SINGULARITY = register("mana_singularity", () -> new ManaBatteryBlock(DeviceTier.HEAVENLY, Block.Properties.of().mapColor(MapColor.DIAMOND).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE).noOcclusion()));
    public static final IRegistryItem<Block, ManaBatteryBlock> MANA_SINGULARITY_CREATIVE = register("mana_singularity_creative", () -> new ManaBatteryBlock(DeviceTier.CREATIVE, Block.Properties.of().mapColor(MapColor.DIAMOND).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).sound(SoundType.STONE).noOcclusion()));
    public static final IRegistryItem<Block, ScribeTableBlock> SCRIBE_TABLE = register("scribe_table", () -> new ScribeTableBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(1.5F, 6.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final IRegistryItem<Block, ManaRelayBlock> MANA_RELAY_BASIC = register("mana_relay_basic", () -> new ManaRelayBlock(DeviceTier.BASIC, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.5F, 6.0F).sound(SoundType.COPPER).noOcclusion().lightLevel(state -> 13)));
    public static final IRegistryItem<Block, ManaRelayBlock> MANA_RELAY_ENCHANTED = register("mana_relay_enchanted", () -> new ManaRelayBlock(DeviceTier.ENCHANTED, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.5F, 6.0F).sound(SoundType.COPPER).noOcclusion().lightLevel(state -> 13)));
    public static final IRegistryItem<Block, ManaRelayBlock> MANA_RELAY_FORBIDDEN = register("mana_relay_forbidden", () -> new ManaRelayBlock(DeviceTier.FORBIDDEN, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.5F, 6.0F).sound(SoundType.COPPER).noOcclusion().lightLevel(state -> 13)));
    public static final IRegistryItem<Block, ManaRelayBlock> MANA_RELAY_HEAVENLY = register("mana_relay_heavenly", () -> new ManaRelayBlock(DeviceTier.HEAVENLY, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.5F, 6.0F).sound(SoundType.COPPER).noOcclusion().lightLevel(state -> 13)));
    public static final IRegistryItem<Block, ManaInjectorBlock> MANA_INJECTOR_BASIC = register("mana_injector_basic", () -> new ManaInjectorBlock(DeviceTier.BASIC, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.5F, 6.0F).sound(SoundType.COPPER).noOcclusion().lightLevel(state -> 13)));
    public static final IRegistryItem<Block, ManaInjectorBlock> MANA_INJECTOR_ENCHANTED = register("mana_injector_enchanted", () -> new ManaInjectorBlock(DeviceTier.ENCHANTED, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.5F, 6.0F).sound(SoundType.COPPER).noOcclusion().lightLevel(state -> 13)));
    public static final IRegistryItem<Block, ManaInjectorBlock> MANA_INJECTOR_FORBIDDEN = register("mana_injector_forbidden", () -> new ManaInjectorBlock(DeviceTier.FORBIDDEN, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.5F, 6.0F).sound(SoundType.COPPER).noOcclusion().lightLevel(state -> 13)));
    public static final IRegistryItem<Block, ManaInjectorBlock> MANA_INJECTOR_HEAVENLY = register("mana_injector_heavenly", () -> new ManaInjectorBlock(DeviceTier.HEAVENLY, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.5F, 6.0F).sound(SoundType.COPPER).noOcclusion().lightLevel(state -> 13)));
    public static final IRegistryItem<Block, DesalinatorBlock> DESALINATOR = register("desalinator", () -> new DesalinatorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.5F, 6.0F).sound(SoundType.COPPER).noOcclusion()));

    // Register misc blocks
    public static final IRegistryItem<Block, ConsecrationFieldBlock> CONSECRATION_FIELD = register("consecration_field", ConsecrationFieldBlock::new);
    public static final IRegistryItem<Block, GlowFieldBlock> GLOW_FIELD = register("glow_field", GlowFieldBlock::new);
    public static final IRegistryItem<Block, GlowFieldBlock> SOUL_GLOW_FIELD = register("soul_glow_field", GlowFieldBlock::new);
    public static final IRegistryItem<Block, SaltTrailBlock> SALT_TRAIL = register("salt_trail", SaltTrailBlock::new);
    public static final IRegistryItem<Block, Block> ROCK_SALT_ORE = register("rock_salt_ore", () -> new Block(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 3.0F)));
    public static final IRegistryItem<Block, DropExperienceBlock> QUARTZ_ORE = register("quartz_ore", () -> new DropExperienceBlock(UniformInt.of(2, 5), Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 3.0F)));
    public static final IRegistryItem<Block, Block> PRIMALITE_BLOCK = register("primalite_block", () -> new Block(Block.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final IRegistryItem<Block, Block> HEXIUM_BLOCK = register("hexium_block", () -> new Block(Block.Properties.of().mapColor(MapColor.METAL).strength(7.0F, 6.0F).sound(SoundType.METAL)));
    public static final IRegistryItem<Block, Block> HALLOWSTEEL_BLOCK = register("hallowsteel_block", () -> new Block(Block.Properties.of().mapColor(MapColor.METAL).strength(9.0F, 6.0F).sound(SoundType.METAL)));
    public static final IRegistryItem<Block, Block> IGNYX_BLOCK = register("ignyx_block", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).strength(5.0F, 6.0F)));
    public static final IRegistryItem<Block, Block> SALT_BLOCK = register("salt_block", () -> new Block(Block.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 3.0F)));
    public static final IRegistryItem<Block, TreefolkSproutBlock> TREEFOLK_SPROUT = register("treefolk_sprout", () -> new TreefolkSproutBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).isRedstoneConductor((state, getter, pos) -> false).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final IRegistryItem<Block, EnderwardBlock> ENDERWARD = register("enderward", EnderwardBlock::new);

    // Helper functions for block properties
    protected static boolean never(BlockState state, BlockGetter reader, BlockPos pos) {
        return false;
    }

    protected static Boolean allowsSpawnOnLeaves(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
        return entity == EntityType.OCELOT || entity == EntityType.PARROT;
    }
}

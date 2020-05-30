package com.verdantartifice.primalmagic.common.blocks;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.base.SaplingBlockPM;
import com.verdantartifice.primalmagic.common.blocks.crafting.ArcaneWorkbenchBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.CalcinatorBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.SpellcraftingAltarBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.WandAssemblyTableBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.WandInscriptionTableBlock;
import com.verdantartifice.primalmagic.common.blocks.devices.AnalysisTableBlock;
import com.verdantartifice.primalmagic.common.blocks.devices.ResearchTableBlock;
import com.verdantartifice.primalmagic.common.blocks.devices.SunlampBlock;
import com.verdantartifice.primalmagic.common.blocks.mana.AncientManaFontBlock;
import com.verdantartifice.primalmagic.common.blocks.mana.WandChargerBlock;
import com.verdantartifice.primalmagic.common.blocks.minerals.MagicalMetalBlock;
import com.verdantartifice.primalmagic.common.blocks.minerals.QuartzOreBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.ConsecrationFieldBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.GlowFieldBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.PillarBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.PyramidBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.SkyglassBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.SkyglassPaneBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.StainedSkyglassBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.StainedSkyglassPaneBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.WoodTableBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.IncenseBrazierBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.OfferingPedestalBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.RitualAltarBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.RitualBellBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.RitualCandleBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.RitualLecternBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.SaltTrailBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodLeavesBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodLogBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodPillarBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodPlanksBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodSlabBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodStairsBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodTree;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodLeavesBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodLogBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodPillarBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodPlanksBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodSlabBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodStairsBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodTree;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod blocks.
 * 
 * @author Daedalus4096
 */
public class BlocksPM {
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, PrimalMagic.MODID);
    
    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    // Register raw marble blocks
    public static final RegistryObject<Block> MARBLE_RAW = BLOCKS.register("marble_raw", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final RegistryObject<SlabBlock> MARBLE_SLAB = BLOCKS.register("marble_slab", () -> new SlabBlock(Block.Properties.from(MARBLE_RAW.get())));
    public static final RegistryObject<StairsBlock> MARBLE_STAIRS = BLOCKS.register("marble_stairs", () -> new StairsBlock(MARBLE_RAW.get()::getDefaultState, Block.Properties.from(MARBLE_RAW.get())));
    public static final RegistryObject<WallBlock> MARBLE_WALL = BLOCKS.register("marble_wall", () -> new WallBlock(Block.Properties.from(MARBLE_RAW.get())));
    public static final RegistryObject<Block> MARBLE_BRICKS = BLOCKS.register("marble_bricks", () -> new Block(Block.Properties.from(MARBLE_RAW.get())));
    public static final RegistryObject<SlabBlock> MARBLE_BRICK_SLAB = BLOCKS.register("marble_brick_slab", () -> new SlabBlock(Block.Properties.from(MARBLE_BRICKS.get())));
    public static final RegistryObject<StairsBlock> MARBLE_BRICK_STAIRS = BLOCKS.register("marble_brick_stairs", () -> new StairsBlock(MARBLE_BRICKS.get()::getDefaultState, Block.Properties.from(MARBLE_BRICKS.get())));
    public static final RegistryObject<WallBlock> MARBLE_BRICK_WALL = BLOCKS.register("marble_brick_wall", () -> new WallBlock(Block.Properties.from(MARBLE_BRICKS.get())));
    public static final RegistryObject<PillarBlock> MARBLE_PILLAR = BLOCKS.register("marble_pillar", () -> new PillarBlock(Block.Properties.from(MARBLE_RAW.get())));
    public static final RegistryObject<Block> MARBLE_CHISELED = BLOCKS.register("marble_chiseled", () -> new Block(Block.Properties.from(MARBLE_RAW.get())));
    public static final RegistryObject<Block> MARBLE_RUNED = BLOCKS.register("marble_runed", () -> new Block(Block.Properties.from(MARBLE_RAW.get())));
    
    // Register enchanted marble blocks
    public static final RegistryObject<Block> MARBLE_ENCHANTED = BLOCKS.register("marble_enchanted", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(3.0F, 12.0F).sound(SoundType.STONE)));
    public static final RegistryObject<SlabBlock> MARBLE_ENCHANTED_SLAB = BLOCKS.register("marble_enchanted_slab", () -> new SlabBlock(Block.Properties.from(MARBLE_ENCHANTED.get())));
    public static final RegistryObject<StairsBlock> MARBLE_ENCHANTED_STAIRS = BLOCKS.register("marble_enchanted_stairs", () -> new StairsBlock(MARBLE_ENCHANTED.get()::getDefaultState, Block.Properties.from(MARBLE_ENCHANTED.get())));
    public static final RegistryObject<WallBlock> MARBLE_ENCHANTED_WALL = BLOCKS.register("marble_enchanted_wall", () -> new WallBlock(Block.Properties.from(MARBLE_ENCHANTED.get())));
    public static final RegistryObject<Block> MARBLE_ENCHANTED_BRICKS = BLOCKS.register("marble_enchanted_bricks", () -> new Block(Block.Properties.from(MARBLE_ENCHANTED.get())));
    public static final RegistryObject<SlabBlock> MARBLE_ENCHANTED_BRICK_SLAB = BLOCKS.register("marble_enchanted_brick_slab", () -> new SlabBlock(Block.Properties.from(MARBLE_ENCHANTED_BRICKS.get())));
    public static final RegistryObject<StairsBlock> MARBLE_ENCHANTED_BRICK_STAIRS = BLOCKS.register("marble_enchanted_brick_stairs", () -> new StairsBlock(MARBLE_ENCHANTED_BRICKS.get()::getDefaultState, Block.Properties.from(MARBLE_ENCHANTED_BRICKS.get())));
    public static final RegistryObject<WallBlock> MARBLE_ENCHANTED_BRICK_WALL = BLOCKS.register("marble_enchanted_brick_wall", () -> new WallBlock(Block.Properties.from(MARBLE_ENCHANTED_BRICKS.get())));
    public static final RegistryObject<PillarBlock> MARBLE_ENCHANTED_PILLAR = BLOCKS.register("marble_enchanted_pillar", () -> new PillarBlock(Block.Properties.from(MARBLE_ENCHANTED.get())));
    public static final RegistryObject<Block> MARBLE_ENCHANTED_CHISELED = BLOCKS.register("marble_enchanted_chiseled", () -> new Block(Block.Properties.from(MARBLE_ENCHANTED.get())));
    public static final RegistryObject<Block> MARBLE_ENCHANTED_RUNED = BLOCKS.register("marble_enchanted_runed", () -> new Block(Block.Properties.from(MARBLE_ENCHANTED.get())));
    
    // Register smoked marble blocks
    public static final RegistryObject<Block> MARBLE_SMOKED = BLOCKS.register("marble_smoked", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.OBSIDIAN).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final RegistryObject<SlabBlock> MARBLE_SMOKED_SLAB = BLOCKS.register("marble_smoked_slab", () -> new SlabBlock(Block.Properties.from(MARBLE_SMOKED.get())));
    public static final RegistryObject<StairsBlock> MARBLE_SMOKED_STAIRS = BLOCKS.register("marble_smoked_stairs", () -> new StairsBlock(MARBLE_SMOKED.get()::getDefaultState, Block.Properties.from(MARBLE_SMOKED.get())));
    public static final RegistryObject<WallBlock> MARBLE_SMOKED_WALL = BLOCKS.register("marble_smoked_wall", () -> new WallBlock(Block.Properties.from(MARBLE_SMOKED.get())));
    public static final RegistryObject<Block> MARBLE_SMOKED_BRICKS = BLOCKS.register("marble_smoked_bricks", () -> new Block(Block.Properties.from(MARBLE_SMOKED.get())));
    public static final RegistryObject<SlabBlock> MARBLE_SMOKED_BRICK_SLAB = BLOCKS.register("marble_smoked_brick_slab", () -> new SlabBlock(Block.Properties.from(MARBLE_SMOKED_BRICKS.get())));
    public static final RegistryObject<StairsBlock> MARBLE_SMOKED_BRICK_STAIRS = BLOCKS.register("marble_smoked_brick_stairs", () -> new StairsBlock(MARBLE_SMOKED_BRICKS.get()::getDefaultState, Block.Properties.from(MARBLE_SMOKED_BRICKS.get())));
    public static final RegistryObject<WallBlock> MARBLE_SMOKED_BRICK_WALL = BLOCKS.register("marble_smoked_brick_wall", () -> new WallBlock(Block.Properties.from(MARBLE_SMOKED_BRICKS.get())));
    public static final RegistryObject<PillarBlock> MARBLE_SMOKED_PILLAR = BLOCKS.register("marble_smoked_pillar", () -> new PillarBlock(Block.Properties.from(MARBLE_SMOKED.get())));
    public static final RegistryObject<Block> MARBLE_SMOKED_CHISELED = BLOCKS.register("marble_smoked_chiseled", () -> new Block(Block.Properties.from(MARBLE_SMOKED.get())));
    public static final RegistryObject<Block> MARBLE_SMOKED_RUNED = BLOCKS.register("marble_smoked_runed", () -> new Block(Block.Properties.from(MARBLE_SMOKED.get())));
    
    // Register sunwood blocks
    public static final RegistryObject<SunwoodLogBlock> STRIPPED_SUNWOOD_LOG = BLOCKS.register("stripped_sunwood_log", () -> new SunwoodLogBlock(null));
    public static final RegistryObject<SunwoodLogBlock> SUNWOOD_LOG = BLOCKS.register("sunwood_log", () -> new SunwoodLogBlock(STRIPPED_SUNWOOD_LOG.get()));
    public static final RegistryObject<SunwoodLogBlock> STRIPPED_SUNWOOD_WOOD = BLOCKS.register("stripped_sunwood_wood", () -> new SunwoodLogBlock(null));
    public static final RegistryObject<SunwoodLogBlock> SUNWOOD_WOOD = BLOCKS.register("sunwood_wood", () -> new SunwoodLogBlock(STRIPPED_SUNWOOD_WOOD.get()));
    public static final RegistryObject<SunwoodLeavesBlock> SUNWOOD_LEAVES = BLOCKS.register("sunwood_leaves", SunwoodLeavesBlock::new);
    public static final RegistryObject<SaplingBlockPM> SUNWOOD_SAPLING = BLOCKS.register("sunwood_sapling", () -> new SaplingBlockPM(new SunwoodTree(), Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.PLANT)));
    public static final RegistryObject<SunwoodPlanksBlock> SUNWOOD_PLANKS = BLOCKS.register("sunwood_planks", SunwoodPlanksBlock::new);
    public static final RegistryObject<SunwoodSlabBlock> SUNWOOD_SLAB = BLOCKS.register("sunwood_slab", () -> new SunwoodSlabBlock(Block.Properties.from(SUNWOOD_PLANKS.get())));
    public static final RegistryObject<SunwoodStairsBlock> SUNWOOD_STAIRS = BLOCKS.register("sunwood_stairs", () -> new SunwoodStairsBlock(SUNWOOD_PLANKS.get()::getDefaultState, Block.Properties.from(SUNWOOD_PLANKS.get())));
    public static final RegistryObject<SunwoodPillarBlock> SUNWOOD_PILLAR = BLOCKS.register("sunwood_pillar", SunwoodPillarBlock::new);
    
    // Register moonwood blocks
    public static final RegistryObject<MoonwoodLogBlock> STRIPPED_MOONWOOD_LOG = BLOCKS.register("stripped_moonwood_log", () -> new MoonwoodLogBlock(null));
    public static final RegistryObject<MoonwoodLogBlock> MOONWOOD_LOG = BLOCKS.register("moonwood_log", () -> new MoonwoodLogBlock(STRIPPED_MOONWOOD_LOG.get()));
    public static final RegistryObject<MoonwoodLogBlock> STRIPPED_MOONWOOD_WOOD = BLOCKS.register("stripped_moonwood_wood", () -> new MoonwoodLogBlock(null));
    public static final RegistryObject<MoonwoodLogBlock> MOONWOOD_WOOD = BLOCKS.register("moonwood_wood", () -> new MoonwoodLogBlock(STRIPPED_MOONWOOD_WOOD.get()));
    public static final RegistryObject<MoonwoodLeavesBlock> MOONWOOD_LEAVES = BLOCKS.register("moonwood_leaves", MoonwoodLeavesBlock::new);
    public static final RegistryObject<SaplingBlockPM> MOONWOOD_SAPLING = BLOCKS.register("moonwood_sapling", () -> new SaplingBlockPM(new MoonwoodTree(), Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.PLANT)));
    public static final RegistryObject<MoonwoodPlanksBlock> MOONWOOD_PLANKS = BLOCKS.register("moonwood_planks", MoonwoodPlanksBlock::new);
    public static final RegistryObject<MoonwoodSlabBlock> MOONWOOD_SLAB = BLOCKS.register("moonwood_slab", () -> new MoonwoodSlabBlock(Block.Properties.from(MOONWOOD_PLANKS.get())));
    public static final RegistryObject<MoonwoodStairsBlock> MOONWOOD_STAIRS = BLOCKS.register("moonwood_stairs", () -> new MoonwoodStairsBlock(MOONWOOD_PLANKS.get()::getDefaultState, Block.Properties.from(MOONWOOD_PLANKS.get())));
    public static final RegistryObject<MoonwoodPillarBlock> MOONWOOD_PILLAR = BLOCKS.register("moonwood_pillar", MoonwoodPillarBlock::new);
    
    // Register infused stone
    public static final RegistryObject<Block> INFUSED_STONE_EARTH = BLOCKS.register("infused_stone_earth", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> INFUSED_STONE_SEA = BLOCKS.register("infused_stone_sea", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> INFUSED_STONE_SKY = BLOCKS.register("infused_stone_sky", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> INFUSED_STONE_SUN = BLOCKS.register("infused_stone_sun", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> INFUSED_STONE_MOON = BLOCKS.register("infused_stone_moon", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE)));
    
    // Register skyglass
    public static final RegistryObject<SkyglassBlock> SKYGLASS = BLOCKS.register("skyglass", () -> new SkyglassBlock(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_BLACK = BLOCKS.register("stained_skyglass_black", () -> new StainedSkyglassBlock(DyeColor.BLACK, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_BLUE = BLOCKS.register("stained_skyglass_blue", () -> new StainedSkyglassBlock(DyeColor.BLUE, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_BROWN = BLOCKS.register("stained_skyglass_brown", () -> new StainedSkyglassBlock(DyeColor.BROWN, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_CYAN = BLOCKS.register("stained_skyglass_cyan", () -> new StainedSkyglassBlock(DyeColor.CYAN, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_GRAY = BLOCKS.register("stained_skyglass_gray", () -> new StainedSkyglassBlock(DyeColor.GRAY, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_GREEN = BLOCKS.register("stained_skyglass_green", () -> new StainedSkyglassBlock(DyeColor.GREEN, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_LIGHT_BLUE = BLOCKS.register("stained_skyglass_light_blue", () -> new StainedSkyglassBlock(DyeColor.LIGHT_BLUE, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_LIGHT_GRAY = BLOCKS.register("stained_skyglass_light_gray", () -> new StainedSkyglassBlock(DyeColor.LIGHT_GRAY, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_LIME = BLOCKS.register("stained_skyglass_lime", () -> new StainedSkyglassBlock(DyeColor.LIME, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_MAGENTA = BLOCKS.register("stained_skyglass_magenta", () -> new StainedSkyglassBlock(DyeColor.MAGENTA, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_ORANGE = BLOCKS.register("stained_skyglass_orange", () -> new StainedSkyglassBlock(DyeColor.ORANGE, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_PINK = BLOCKS.register("stained_skyglass_pink", () -> new StainedSkyglassBlock(DyeColor.PINK, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_PURPLE = BLOCKS.register("stained_skyglass_purple", () -> new StainedSkyglassBlock(DyeColor.PURPLE, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_RED = BLOCKS.register("stained_skyglass_red", () -> new StainedSkyglassBlock(DyeColor.RED, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_WHITE = BLOCKS.register("stained_skyglass_white", () -> new StainedSkyglassBlock(DyeColor.WHITE, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_YELLOW = BLOCKS.register("stained_skyglass_yellow", () -> new StainedSkyglassBlock(DyeColor.YELLOW, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    
    // Register skyglass panes
    public static final RegistryObject<SkyglassPaneBlock> SKYGLASS_PANE = BLOCKS.register("skyglass_pane", () -> new SkyglassPaneBlock(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_BLACK = BLOCKS.register("stained_skyglass_pane_black", () -> new StainedSkyglassPaneBlock(DyeColor.BLACK, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_BLUE = BLOCKS.register("stained_skyglass_pane_blue", () -> new StainedSkyglassPaneBlock(DyeColor.BLUE, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_BROWN = BLOCKS.register("stained_skyglass_pane_brown", () -> new StainedSkyglassPaneBlock(DyeColor.BROWN, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_CYAN = BLOCKS.register("stained_skyglass_pane_cyan", () -> new StainedSkyglassPaneBlock(DyeColor.CYAN, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_GRAY = BLOCKS.register("stained_skyglass_pane_gray", () -> new StainedSkyglassPaneBlock(DyeColor.GRAY, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_GREEN = BLOCKS.register("stained_skyglass_pane_green", () -> new StainedSkyglassPaneBlock(DyeColor.GREEN, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_LIGHT_BLUE = BLOCKS.register("stained_skyglass_pane_light_blue", () -> new StainedSkyglassPaneBlock(DyeColor.LIGHT_BLUE, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_LIGHT_GRAY = BLOCKS.register("stained_skyglass_pane_light_gray", () -> new StainedSkyglassPaneBlock(DyeColor.LIGHT_GRAY, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_LIME = BLOCKS.register("stained_skyglass_pane_lime", () -> new StainedSkyglassPaneBlock(DyeColor.LIME, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_MAGENTA = BLOCKS.register("stained_skyglass_pane_magenta", () -> new StainedSkyglassPaneBlock(DyeColor.MAGENTA, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_ORANGE = BLOCKS.register("stained_skyglass_pane_orange", () -> new StainedSkyglassPaneBlock(DyeColor.ORANGE, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_PINK = BLOCKS.register("stained_skyglass_pane_pink", () -> new StainedSkyglassPaneBlock(DyeColor.PINK, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_PURPLE = BLOCKS.register("stained_skyglass_pane_purple", () -> new StainedSkyglassPaneBlock(DyeColor.PURPLE, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_RED = BLOCKS.register("stained_skyglass_pane_red", () -> new StainedSkyglassPaneBlock(DyeColor.RED, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_WHITE = BLOCKS.register("stained_skyglass_pane_white", () -> new StainedSkyglassPaneBlock(DyeColor.WHITE, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_YELLOW = BLOCKS.register("stained_skyglass_pane_yellow", () -> new StainedSkyglassPaneBlock(DyeColor.YELLOW, Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()));

    // Register ritual candles
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_BLACK = BLOCKS.register("ritual_candle_black", () -> new RitualCandleBlock(DyeColor.BLACK, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_BLUE = BLOCKS.register("ritual_candle_blue", () -> new RitualCandleBlock(DyeColor.BLUE, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_BROWN = BLOCKS.register("ritual_candle_brown", () -> new RitualCandleBlock(DyeColor.BROWN, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_CYAN = BLOCKS.register("ritual_candle_cyan", () -> new RitualCandleBlock(DyeColor.CYAN, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_GRAY = BLOCKS.register("ritual_candle_gray", () -> new RitualCandleBlock(DyeColor.GRAY, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_GREEN = BLOCKS.register("ritual_candle_green", () -> new RitualCandleBlock(DyeColor.GREEN, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_LIGHT_BLUE = BLOCKS.register("ritual_candle_light_blue", () -> new RitualCandleBlock(DyeColor.LIGHT_BLUE, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_LIGHT_GRAY = BLOCKS.register("ritual_candle_light_gray", () -> new RitualCandleBlock(DyeColor.LIGHT_GRAY, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_LIME = BLOCKS.register("ritual_candle_lime", () -> new RitualCandleBlock(DyeColor.LIME, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_MAGENTA = BLOCKS.register("ritual_candle_magenta", () -> new RitualCandleBlock(DyeColor.MAGENTA, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_ORANGE = BLOCKS.register("ritual_candle_orange", () -> new RitualCandleBlock(DyeColor.ORANGE, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_PINK = BLOCKS.register("ritual_candle_pink", () -> new RitualCandleBlock(DyeColor.PINK, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_PURPLE = BLOCKS.register("ritual_candle_purple", () -> new RitualCandleBlock(DyeColor.PURPLE, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_RED = BLOCKS.register("ritual_candle_red", () -> new RitualCandleBlock(DyeColor.RED, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_WHITE = BLOCKS.register("ritual_candle_white", () -> new RitualCandleBlock(DyeColor.WHITE, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_YELLOW = BLOCKS.register("ritual_candle_yellow", () -> new RitualCandleBlock(DyeColor.YELLOW, Block.Properties.create(Material.ORGANIC).hardnessAndResistance(0.0F).lightValue(7).sound(SoundType.CLOTH).notSolid()));

    // Register mana fonts
    public static final RegistryObject<AncientManaFontBlock> ANCIENT_FONT_EARTH = BLOCKS.register("ancient_font_earth", () -> new AncientManaFontBlock(Source.EARTH));
    public static final RegistryObject<AncientManaFontBlock> ANCIENT_FONT_SEA = BLOCKS.register("ancient_font_sea", () -> new AncientManaFontBlock(Source.SEA));
    public static final RegistryObject<AncientManaFontBlock> ANCIENT_FONT_SKY = BLOCKS.register("ancient_font_sky", () -> new AncientManaFontBlock(Source.SKY));
    public static final RegistryObject<AncientManaFontBlock> ANCIENT_FONT_SUN = BLOCKS.register("ancient_font_sun", () -> new AncientManaFontBlock(Source.SUN));
    public static final RegistryObject<AncientManaFontBlock> ANCIENT_FONT_MOON = BLOCKS.register("ancient_font_moon", () -> new AncientManaFontBlock(Source.MOON));

    // Register devices
    public static final RegistryObject<ArcaneWorkbenchBlock> ARCANE_WORKBENCH = BLOCKS.register("arcane_workbench", ArcaneWorkbenchBlock::new);
    public static final RegistryObject<WandAssemblyTableBlock> WAND_ASSEMBLY_TABLE = BLOCKS.register("wand_assembly_table", WandAssemblyTableBlock::new);
    public static final RegistryObject<WoodTableBlock> WOOD_TABLE = BLOCKS.register("wood_table", WoodTableBlock::new);
    public static final RegistryObject<AnalysisTableBlock> ANALYSIS_TABLE = BLOCKS.register("analysis_table", AnalysisTableBlock::new);
    public static final RegistryObject<CalcinatorBlock> CALCINATOR = BLOCKS.register("calcinator", CalcinatorBlock::new);
    public static final RegistryObject<WandInscriptionTableBlock> WAND_INSCRIPTION_TABLE = BLOCKS.register("wand_inscription_table", WandInscriptionTableBlock::new);
    public static final RegistryObject<SpellcraftingAltarBlock> SPELLCRAFTING_ALTAR = BLOCKS.register("spellcrafting_altar", SpellcraftingAltarBlock::new);
    public static final RegistryObject<WandChargerBlock> WAND_CHARGER = BLOCKS.register("wand_charger", WandChargerBlock::new);
    public static final RegistryObject<ResearchTableBlock> RESEARCH_TABLE = BLOCKS.register("research_table", ResearchTableBlock::new);
    public static final RegistryObject<SunlampBlock> SUNLAMP = BLOCKS.register("sunlamp", SunlampBlock::new);
    public static final RegistryObject<RitualAltarBlock> RITUAL_ALTAR = BLOCKS.register("ritual_altar", RitualAltarBlock::new);
    public static final RegistryObject<OfferingPedestalBlock> OFFERING_PEDESTAL = BLOCKS.register("offering_pedestal", OfferingPedestalBlock::new);
    public static final RegistryObject<IncenseBrazierBlock> INCENSE_BRAZIER = BLOCKS.register("incense_brazier", IncenseBrazierBlock::new);
    public static final RegistryObject<RitualLecternBlock> RITUAL_LECTERN = BLOCKS.register("ritual_lectern", RitualLecternBlock::new);
    public static final RegistryObject<RitualBellBlock> RITUAL_BELL = BLOCKS.register("ritual_bell", RitualBellBlock::new);
    
    // Register misc blocks
    public static final RegistryObject<ConsecrationFieldBlock> CONSECRATION_FIELD = BLOCKS.register("consecration_field", ConsecrationFieldBlock::new);
    public static final RegistryObject<GlowFieldBlock> GLOW_FIELD = BLOCKS.register("glow_field", GlowFieldBlock::new);
    public static final RegistryObject<SaltTrailBlock> SALT_TRAIL = BLOCKS.register("salt_trail", SaltTrailBlock::new);
    public static final RegistryObject<Block> ROCK_SALT_ORE = BLOCKS.register("rock_salt_ore", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)));
    public static final RegistryObject<QuartzOreBlock> QUARTZ_ORE = BLOCKS.register("quartz_ore", () -> new QuartzOreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)));
    public static final RegistryObject<MagicalMetalBlock> PRIMALITE_BLOCK = BLOCKS.register("primalite_block", () -> new MagicalMetalBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1)));
    public static final RegistryObject<MagicalMetalBlock> HEXIUM_BLOCK = BLOCKS.register("hexium_block", () -> new MagicalMetalBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(7.0F, 6.0F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2)));
    public static final RegistryObject<MagicalMetalBlock> HALLOWSTEEL_BLOCK = BLOCKS.register("hallowsteel_block", () -> new MagicalMetalBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(9.0F, 6.0F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<PyramidBlock> PYRAMID = BLOCKS.register("pyramid", PyramidBlock::new);
}

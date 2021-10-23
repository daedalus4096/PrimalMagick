package com.verdantartifice.primalmagic.common.blocks;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.base.SaplingBlockPM;
import com.verdantartifice.primalmagic.common.blocks.crafting.ArcaneWorkbenchBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.CalcinatorBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.ConcocterBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.EssenceFurnaceBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.RunecarvingTableBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.RunescribingAltarBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.RunicGrindstoneBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.SpellcraftingAltarBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.WandAssemblyTableBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.WandInscriptionTableBlock;
import com.verdantartifice.primalmagic.common.blocks.devices.AnalysisTableBlock;
import com.verdantartifice.primalmagic.common.blocks.devices.EssenceTransmuterBlock;
import com.verdantartifice.primalmagic.common.blocks.devices.HoneyExtractorBlock;
import com.verdantartifice.primalmagic.common.blocks.devices.ResearchTableBlock;
import com.verdantartifice.primalmagic.common.blocks.devices.SanguineCrucibleBlock;
import com.verdantartifice.primalmagic.common.blocks.devices.SunlampBlock;
import com.verdantartifice.primalmagic.common.blocks.golems.HallowsteelGolemControllerBlock;
import com.verdantartifice.primalmagic.common.blocks.golems.HexiumGolemControllerBlock;
import com.verdantartifice.primalmagic.common.blocks.golems.PrimaliteGolemControllerBlock;
import com.verdantartifice.primalmagic.common.blocks.mana.AncientManaFontBlock;
import com.verdantartifice.primalmagic.common.blocks.mana.ArtificialManaFontBlock;
import com.verdantartifice.primalmagic.common.blocks.mana.AutoChargerBlock;
import com.verdantartifice.primalmagic.common.blocks.mana.WandChargerBlock;
import com.verdantartifice.primalmagic.common.blocks.minerals.QuartzOreBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.ConsecrationFieldBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.GlowFieldBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.PillarBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.SkyglassBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.SkyglassPaneBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.StainedSkyglassBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.StainedSkyglassPaneBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.WoodTableBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.BloodletterBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.CelestialHarpBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.EntropySinkBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.IncenseBrazierBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.OfferingPedestalBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.RitualAltarBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.RitualBellBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.RitualCandleBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.RitualLecternBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.SaltTrailBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.SoulAnvilBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.HallowoodTree;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodLeavesBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodLogBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodPillarBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodPlanksBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodSlabBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodStairsBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodTree;
import com.verdantartifice.primalmagic.common.blocks.trees.StrippableLogBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodLeavesBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodLogBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodPillarBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodPlanksBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodSlabBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodStairsBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodTree;
import com.verdantartifice.primalmagic.common.misc.DeviceTier;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod blocks.
 * 
 * @author Daedalus4096
 */
public class BlocksPM {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PrimalMagic.MODID);
    
    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    // Register raw marble blocks
    public static final RegistryObject<Block> MARBLE_RAW = BLOCKS.register("marble_raw", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.QUARTZ).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final RegistryObject<SlabBlock> MARBLE_SLAB = BLOCKS.register("marble_slab", () -> new SlabBlock(Block.Properties.copy(MARBLE_RAW.get())));
    public static final RegistryObject<StairBlock> MARBLE_STAIRS = BLOCKS.register("marble_stairs", () -> new StairBlock(MARBLE_RAW.get()::defaultBlockState, Block.Properties.copy(MARBLE_RAW.get())));
    public static final RegistryObject<WallBlock> MARBLE_WALL = BLOCKS.register("marble_wall", () -> new WallBlock(Block.Properties.copy(MARBLE_RAW.get())));
    public static final RegistryObject<Block> MARBLE_BRICKS = BLOCKS.register("marble_bricks", () -> new Block(Block.Properties.copy(MARBLE_RAW.get())));
    public static final RegistryObject<SlabBlock> MARBLE_BRICK_SLAB = BLOCKS.register("marble_brick_slab", () -> new SlabBlock(Block.Properties.copy(MARBLE_BRICKS.get())));
    public static final RegistryObject<StairBlock> MARBLE_BRICK_STAIRS = BLOCKS.register("marble_brick_stairs", () -> new StairBlock(MARBLE_BRICKS.get()::defaultBlockState, Block.Properties.copy(MARBLE_BRICKS.get())));
    public static final RegistryObject<WallBlock> MARBLE_BRICK_WALL = BLOCKS.register("marble_brick_wall", () -> new WallBlock(Block.Properties.copy(MARBLE_BRICKS.get())));
    public static final RegistryObject<PillarBlock> MARBLE_PILLAR = BLOCKS.register("marble_pillar", () -> new PillarBlock(Block.Properties.copy(MARBLE_RAW.get())));
    public static final RegistryObject<Block> MARBLE_CHISELED = BLOCKS.register("marble_chiseled", () -> new Block(Block.Properties.copy(MARBLE_RAW.get())));
    public static final RegistryObject<Block> MARBLE_RUNED = BLOCKS.register("marble_runed", () -> new Block(Block.Properties.copy(MARBLE_RAW.get())));
    
    // Register enchanted marble blocks
    public static final RegistryObject<Block> MARBLE_ENCHANTED = BLOCKS.register("marble_enchanted", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.QUARTZ).strength(3.0F, 12.0F).sound(SoundType.STONE)));
    public static final RegistryObject<SlabBlock> MARBLE_ENCHANTED_SLAB = BLOCKS.register("marble_enchanted_slab", () -> new SlabBlock(Block.Properties.copy(MARBLE_ENCHANTED.get())));
    public static final RegistryObject<StairBlock> MARBLE_ENCHANTED_STAIRS = BLOCKS.register("marble_enchanted_stairs", () -> new StairBlock(MARBLE_ENCHANTED.get()::defaultBlockState, Block.Properties.copy(MARBLE_ENCHANTED.get())));
    public static final RegistryObject<WallBlock> MARBLE_ENCHANTED_WALL = BLOCKS.register("marble_enchanted_wall", () -> new WallBlock(Block.Properties.copy(MARBLE_ENCHANTED.get())));
    public static final RegistryObject<Block> MARBLE_ENCHANTED_BRICKS = BLOCKS.register("marble_enchanted_bricks", () -> new Block(Block.Properties.copy(MARBLE_ENCHANTED.get())));
    public static final RegistryObject<SlabBlock> MARBLE_ENCHANTED_BRICK_SLAB = BLOCKS.register("marble_enchanted_brick_slab", () -> new SlabBlock(Block.Properties.copy(MARBLE_ENCHANTED_BRICKS.get())));
    public static final RegistryObject<StairBlock> MARBLE_ENCHANTED_BRICK_STAIRS = BLOCKS.register("marble_enchanted_brick_stairs", () -> new StairBlock(MARBLE_ENCHANTED_BRICKS.get()::defaultBlockState, Block.Properties.copy(MARBLE_ENCHANTED_BRICKS.get())));
    public static final RegistryObject<WallBlock> MARBLE_ENCHANTED_BRICK_WALL = BLOCKS.register("marble_enchanted_brick_wall", () -> new WallBlock(Block.Properties.copy(MARBLE_ENCHANTED_BRICKS.get())));
    public static final RegistryObject<PillarBlock> MARBLE_ENCHANTED_PILLAR = BLOCKS.register("marble_enchanted_pillar", () -> new PillarBlock(Block.Properties.copy(MARBLE_ENCHANTED.get())));
    public static final RegistryObject<Block> MARBLE_ENCHANTED_CHISELED = BLOCKS.register("marble_enchanted_chiseled", () -> new Block(Block.Properties.copy(MARBLE_ENCHANTED.get())));
    public static final RegistryObject<Block> MARBLE_ENCHANTED_RUNED = BLOCKS.register("marble_enchanted_runed", () -> new Block(Block.Properties.copy(MARBLE_ENCHANTED.get())));
    
    // Register smoked marble blocks
    public static final RegistryObject<Block> MARBLE_SMOKED = BLOCKS.register("marble_smoked", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.PODZOL).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final RegistryObject<SlabBlock> MARBLE_SMOKED_SLAB = BLOCKS.register("marble_smoked_slab", () -> new SlabBlock(Block.Properties.copy(MARBLE_SMOKED.get())));
    public static final RegistryObject<StairBlock> MARBLE_SMOKED_STAIRS = BLOCKS.register("marble_smoked_stairs", () -> new StairBlock(MARBLE_SMOKED.get()::defaultBlockState, Block.Properties.copy(MARBLE_SMOKED.get())));
    public static final RegistryObject<WallBlock> MARBLE_SMOKED_WALL = BLOCKS.register("marble_smoked_wall", () -> new WallBlock(Block.Properties.copy(MARBLE_SMOKED.get())));
    public static final RegistryObject<Block> MARBLE_SMOKED_BRICKS = BLOCKS.register("marble_smoked_bricks", () -> new Block(Block.Properties.copy(MARBLE_SMOKED.get())));
    public static final RegistryObject<SlabBlock> MARBLE_SMOKED_BRICK_SLAB = BLOCKS.register("marble_smoked_brick_slab", () -> new SlabBlock(Block.Properties.copy(MARBLE_SMOKED_BRICKS.get())));
    public static final RegistryObject<StairBlock> MARBLE_SMOKED_BRICK_STAIRS = BLOCKS.register("marble_smoked_brick_stairs", () -> new StairBlock(MARBLE_SMOKED_BRICKS.get()::defaultBlockState, Block.Properties.copy(MARBLE_SMOKED_BRICKS.get())));
    public static final RegistryObject<WallBlock> MARBLE_SMOKED_BRICK_WALL = BLOCKS.register("marble_smoked_brick_wall", () -> new WallBlock(Block.Properties.copy(MARBLE_SMOKED_BRICKS.get())));
    public static final RegistryObject<PillarBlock> MARBLE_SMOKED_PILLAR = BLOCKS.register("marble_smoked_pillar", () -> new PillarBlock(Block.Properties.copy(MARBLE_SMOKED.get())));
    public static final RegistryObject<Block> MARBLE_SMOKED_CHISELED = BLOCKS.register("marble_smoked_chiseled", () -> new Block(Block.Properties.copy(MARBLE_SMOKED.get())));
    public static final RegistryObject<Block> MARBLE_SMOKED_RUNED = BLOCKS.register("marble_smoked_runed", () -> new Block(Block.Properties.copy(MARBLE_SMOKED.get())));
    
    // Register hallowed marble blocks
    public static final RegistryObject<Block> MARBLE_HALLOWED = BLOCKS.register("marble_hallowed", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.GOLD).strength(3.0F, 12.0F).sound(SoundType.STONE)));
    public static final RegistryObject<SlabBlock> MARBLE_HALLOWED_SLAB  = BLOCKS.register("marble_hallowed_slab", () -> new SlabBlock(Block.Properties.copy(MARBLE_HALLOWED.get())));
    public static final RegistryObject<StairBlock> MARBLE_HALLOWED_STAIRS = BLOCKS.register("marble_hallowed_stairs", () -> new StairBlock(MARBLE_HALLOWED.get()::defaultBlockState, Block.Properties.copy(MARBLE_HALLOWED.get())));
    public static final RegistryObject<WallBlock> MARBLE_HALLOWED_WALL = BLOCKS.register("marble_hallowed_wall", () -> new WallBlock(Block.Properties.copy(MARBLE_HALLOWED.get())));
    public static final RegistryObject<Block> MARBLE_HALLOWED_BRICKS = BLOCKS.register("marble_hallowed_bricks", () -> new Block(Block.Properties.copy(MARBLE_HALLOWED.get())));
    public static final RegistryObject<SlabBlock> MARBLE_HALLOWED_BRICK_SLAB = BLOCKS.register("marble_hallowed_brick_slab", () -> new SlabBlock(Block.Properties.copy(MARBLE_HALLOWED_BRICKS.get())));
    public static final RegistryObject<StairBlock> MARBLE_HALLOWED_BRICK_STAIRS = BLOCKS.register("marble_hallowed_brick_stairs", () -> new StairBlock(MARBLE_HALLOWED_BRICKS.get()::defaultBlockState, Block.Properties.copy(MARBLE_HALLOWED_BRICKS.get())));
    public static final RegistryObject<WallBlock> MARBLE_HALLOWED_BRICK_WALL = BLOCKS.register("marble_hallowed_brick_wall", () -> new WallBlock(Block.Properties.copy(MARBLE_HALLOWED_BRICKS.get())));
    public static final RegistryObject<PillarBlock> MARBLE_HALLOWED_PILLAR = BLOCKS.register("marble_hallowed_pillar", () -> new PillarBlock(Block.Properties.copy(MARBLE_HALLOWED.get())));
    public static final RegistryObject<Block> MARBLE_HALLOWED_CHISELED = BLOCKS.register("marble_hallowed_chiseled", () -> new Block(Block.Properties.copy(MARBLE_HALLOWED.get())));
    public static final RegistryObject<Block> MARBLE_HALLOWED_RUNED = BLOCKS.register("marble_hallowed_runed", () -> new Block(Block.Properties.copy(MARBLE_HALLOWED.get())));
    
    // Register sunwood blocks
    public static final RegistryObject<SunwoodLogBlock> STRIPPED_SUNWOOD_LOG = BLOCKS.register("stripped_sunwood_log", () -> new SunwoodLogBlock(null));
    public static final RegistryObject<SunwoodLogBlock> SUNWOOD_LOG = BLOCKS.register("sunwood_log", () -> new SunwoodLogBlock(STRIPPED_SUNWOOD_LOG.get()));
    public static final RegistryObject<SunwoodLogBlock> STRIPPED_SUNWOOD_WOOD = BLOCKS.register("stripped_sunwood_wood", () -> new SunwoodLogBlock(null));
    public static final RegistryObject<SunwoodLogBlock> SUNWOOD_WOOD = BLOCKS.register("sunwood_wood", () -> new SunwoodLogBlock(STRIPPED_SUNWOOD_WOOD.get()));
    public static final RegistryObject<SunwoodLeavesBlock> SUNWOOD_LEAVES = BLOCKS.register("sunwood_leaves", SunwoodLeavesBlock::new);
    public static final RegistryObject<SaplingBlockPM> SUNWOOD_SAPLING = BLOCKS.register("sunwood_sapling", () -> new SaplingBlockPM(new SunwoodTree(), Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0.0F).sound(SoundType.GRASS)));
    public static final RegistryObject<SunwoodPlanksBlock> SUNWOOD_PLANKS = BLOCKS.register("sunwood_planks", SunwoodPlanksBlock::new);
    public static final RegistryObject<SunwoodSlabBlock> SUNWOOD_SLAB = BLOCKS.register("sunwood_slab", () -> new SunwoodSlabBlock(Block.Properties.copy(SUNWOOD_PLANKS.get())));
    public static final RegistryObject<SunwoodStairsBlock> SUNWOOD_STAIRS = BLOCKS.register("sunwood_stairs", () -> new SunwoodStairsBlock(SUNWOOD_PLANKS.get()::defaultBlockState, Block.Properties.copy(SUNWOOD_PLANKS.get())));
    public static final RegistryObject<SunwoodPillarBlock> SUNWOOD_PILLAR = BLOCKS.register("sunwood_pillar", SunwoodPillarBlock::new);
    
    // Register moonwood blocks
    public static final RegistryObject<MoonwoodLogBlock> STRIPPED_MOONWOOD_LOG = BLOCKS.register("stripped_moonwood_log", () -> new MoonwoodLogBlock(null));
    public static final RegistryObject<MoonwoodLogBlock> MOONWOOD_LOG = BLOCKS.register("moonwood_log", () -> new MoonwoodLogBlock(STRIPPED_MOONWOOD_LOG.get()));
    public static final RegistryObject<MoonwoodLogBlock> STRIPPED_MOONWOOD_WOOD = BLOCKS.register("stripped_moonwood_wood", () -> new MoonwoodLogBlock(null));
    public static final RegistryObject<MoonwoodLogBlock> MOONWOOD_WOOD = BLOCKS.register("moonwood_wood", () -> new MoonwoodLogBlock(STRIPPED_MOONWOOD_WOOD.get()));
    public static final RegistryObject<MoonwoodLeavesBlock> MOONWOOD_LEAVES = BLOCKS.register("moonwood_leaves", MoonwoodLeavesBlock::new);
    public static final RegistryObject<SaplingBlockPM> MOONWOOD_SAPLING = BLOCKS.register("moonwood_sapling", () -> new SaplingBlockPM(new MoonwoodTree(), Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0.0F).sound(SoundType.GRASS)));
    public static final RegistryObject<MoonwoodPlanksBlock> MOONWOOD_PLANKS = BLOCKS.register("moonwood_planks", MoonwoodPlanksBlock::new);
    public static final RegistryObject<MoonwoodSlabBlock> MOONWOOD_SLAB = BLOCKS.register("moonwood_slab", () -> new MoonwoodSlabBlock(Block.Properties.copy(MOONWOOD_PLANKS.get())));
    public static final RegistryObject<MoonwoodStairsBlock> MOONWOOD_STAIRS = BLOCKS.register("moonwood_stairs", () -> new MoonwoodStairsBlock(MOONWOOD_PLANKS.get()::defaultBlockState, Block.Properties.copy(MOONWOOD_PLANKS.get())));
    public static final RegistryObject<MoonwoodPillarBlock> MOONWOOD_PILLAR = BLOCKS.register("moonwood_pillar", MoonwoodPillarBlock::new);
    
    // Register hallowood blocks
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_HALLOWOOD_LOG = BLOCKS.register("stripped_hallowood_log", () -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, MaterialColor.GOLD).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<StrippableLogBlock> HALLOWOOD_LOG = BLOCKS.register("hallowood_log", () -> new StrippableLogBlock(STRIPPED_HALLOWOOD_LOG.get(), Block.Properties.of(Material.WOOD, MaterialColor.GOLD).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_HALLOWOOD_WOOD = BLOCKS.register("stripped_hallowood_wood", () -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, MaterialColor.GOLD).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<StrippableLogBlock> HALLOWOOD_WOOD = BLOCKS.register("hallowood_wood", () -> new StrippableLogBlock(STRIPPED_HALLOWOOD_WOOD.get(), Block.Properties.of(Material.WOOD, MaterialColor.GOLD).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<LeavesBlock> HALLOWOOD_LEAVES = BLOCKS.register("hallowood_leaves", () -> new LeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).noOcclusion().sound(SoundType.GRASS).lightLevel((state) -> { return 10; }).isSuffocating(BlocksPM::isntSolid).isViewBlocking(BlocksPM::isntSolid).isValidSpawn(BlocksPM::allowsSpawnOnLeaves)));
    public static final RegistryObject<SaplingBlockPM> HALLOWOOD_SAPLING = BLOCKS.register("hallowood_sapling", () -> new SaplingBlockPM(new HallowoodTree(), Block.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> HALLOWOOD_PLANKS = BLOCKS.register("hallowood_planks", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.GOLD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<SlabBlock> HALLOWOOD_SLAB = BLOCKS.register("hallowood_slab", () -> new SlabBlock(Block.Properties.copy(HALLOWOOD_PLANKS.get())));
    public static final RegistryObject<StairBlock> HALLOWOOD_STAIRS = BLOCKS.register("hallowood_stairs", () -> new StairBlock(HALLOWOOD_PLANKS.get()::defaultBlockState, Block.Properties.copy(HALLOWOOD_PLANKS.get())));
    public static final RegistryObject<PillarBlock> HALLOWOOD_PILLAR = BLOCKS.register("hallowood_pillar", () -> new PillarBlock(Block.Properties.of(Material.WOOD, MaterialColor.GOLD).strength(2.0F).sound(SoundType.WOOD)));
    
    // Register infused stone
    public static final RegistryObject<Block> INFUSED_STONE_EARTH = BLOCKS.register("infused_stone_earth", () -> new Block(Block.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> INFUSED_STONE_SEA = BLOCKS.register("infused_stone_sea", () -> new Block(Block.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> INFUSED_STONE_SKY = BLOCKS.register("infused_stone_sky", () -> new Block(Block.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> INFUSED_STONE_SUN = BLOCKS.register("infused_stone_sun", () -> new Block(Block.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> INFUSED_STONE_MOON = BLOCKS.register("infused_stone_moon", () -> new Block(Block.Properties.of(Material.STONE).strength(1.5F, 6.0F).sound(SoundType.STONE)));
    
    // Register skyglass
    public static final RegistryObject<SkyglassBlock> SKYGLASS = BLOCKS.register("skyglass", () -> new SkyglassBlock(Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_BLACK = BLOCKS.register("stained_skyglass_black", () -> new StainedSkyglassBlock(DyeColor.BLACK, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_BLUE = BLOCKS.register("stained_skyglass_blue", () -> new StainedSkyglassBlock(DyeColor.BLUE, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_BROWN = BLOCKS.register("stained_skyglass_brown", () -> new StainedSkyglassBlock(DyeColor.BROWN, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_CYAN = BLOCKS.register("stained_skyglass_cyan", () -> new StainedSkyglassBlock(DyeColor.CYAN, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_GRAY = BLOCKS.register("stained_skyglass_gray", () -> new StainedSkyglassBlock(DyeColor.GRAY, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_GREEN = BLOCKS.register("stained_skyglass_green", () -> new StainedSkyglassBlock(DyeColor.GREEN, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_LIGHT_BLUE = BLOCKS.register("stained_skyglass_light_blue", () -> new StainedSkyglassBlock(DyeColor.LIGHT_BLUE, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_LIGHT_GRAY = BLOCKS.register("stained_skyglass_light_gray", () -> new StainedSkyglassBlock(DyeColor.LIGHT_GRAY, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_LIME = BLOCKS.register("stained_skyglass_lime", () -> new StainedSkyglassBlock(DyeColor.LIME, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_MAGENTA = BLOCKS.register("stained_skyglass_magenta", () -> new StainedSkyglassBlock(DyeColor.MAGENTA, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_ORANGE = BLOCKS.register("stained_skyglass_orange", () -> new StainedSkyglassBlock(DyeColor.ORANGE, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_PINK = BLOCKS.register("stained_skyglass_pink", () -> new StainedSkyglassBlock(DyeColor.PINK, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_PURPLE = BLOCKS.register("stained_skyglass_purple", () -> new StainedSkyglassBlock(DyeColor.PURPLE, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_RED = BLOCKS.register("stained_skyglass_red", () -> new StainedSkyglassBlock(DyeColor.RED, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_WHITE = BLOCKS.register("stained_skyglass_white", () -> new StainedSkyglassBlock(DyeColor.WHITE, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassBlock> STAINED_SKYGLASS_YELLOW = BLOCKS.register("stained_skyglass_yellow", () -> new StainedSkyglassBlock(DyeColor.YELLOW, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    
    // Register skyglass panes
    public static final RegistryObject<SkyglassPaneBlock> SKYGLASS_PANE = BLOCKS.register("skyglass_pane", () -> new SkyglassPaneBlock(Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_BLACK = BLOCKS.register("stained_skyglass_pane_black", () -> new StainedSkyglassPaneBlock(DyeColor.BLACK, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_BLUE = BLOCKS.register("stained_skyglass_pane_blue", () -> new StainedSkyglassPaneBlock(DyeColor.BLUE, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_BROWN = BLOCKS.register("stained_skyglass_pane_brown", () -> new StainedSkyglassPaneBlock(DyeColor.BROWN, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_CYAN = BLOCKS.register("stained_skyglass_pane_cyan", () -> new StainedSkyglassPaneBlock(DyeColor.CYAN, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_GRAY = BLOCKS.register("stained_skyglass_pane_gray", () -> new StainedSkyglassPaneBlock(DyeColor.GRAY, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_GREEN = BLOCKS.register("stained_skyglass_pane_green", () -> new StainedSkyglassPaneBlock(DyeColor.GREEN, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_LIGHT_BLUE = BLOCKS.register("stained_skyglass_pane_light_blue", () -> new StainedSkyglassPaneBlock(DyeColor.LIGHT_BLUE, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_LIGHT_GRAY = BLOCKS.register("stained_skyglass_pane_light_gray", () -> new StainedSkyglassPaneBlock(DyeColor.LIGHT_GRAY, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_LIME = BLOCKS.register("stained_skyglass_pane_lime", () -> new StainedSkyglassPaneBlock(DyeColor.LIME, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_MAGENTA = BLOCKS.register("stained_skyglass_pane_magenta", () -> new StainedSkyglassPaneBlock(DyeColor.MAGENTA, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_ORANGE = BLOCKS.register("stained_skyglass_pane_orange", () -> new StainedSkyglassPaneBlock(DyeColor.ORANGE, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_PINK = BLOCKS.register("stained_skyglass_pane_pink", () -> new StainedSkyglassPaneBlock(DyeColor.PINK, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_PURPLE = BLOCKS.register("stained_skyglass_pane_purple", () -> new StainedSkyglassPaneBlock(DyeColor.PURPLE, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_RED = BLOCKS.register("stained_skyglass_pane_red", () -> new StainedSkyglassPaneBlock(DyeColor.RED, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_WHITE = BLOCKS.register("stained_skyglass_pane_white", () -> new StainedSkyglassPaneBlock(DyeColor.WHITE, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<StainedSkyglassPaneBlock> STAINED_SKYGLASS_PANE_YELLOW = BLOCKS.register("stained_skyglass_pane_yellow", () -> new StainedSkyglassPaneBlock(DyeColor.YELLOW, Block.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));

    // Register ritual candles
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_BLACK = BLOCKS.register("ritual_candle_black", () -> new RitualCandleBlock(DyeColor.BLACK, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_BLUE = BLOCKS.register("ritual_candle_blue", () -> new RitualCandleBlock(DyeColor.BLUE, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_BROWN = BLOCKS.register("ritual_candle_brown", () -> new RitualCandleBlock(DyeColor.BROWN, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_CYAN = BLOCKS.register("ritual_candle_cyan", () -> new RitualCandleBlock(DyeColor.CYAN, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_GRAY = BLOCKS.register("ritual_candle_gray", () -> new RitualCandleBlock(DyeColor.GRAY, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_GREEN = BLOCKS.register("ritual_candle_green", () -> new RitualCandleBlock(DyeColor.GREEN, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_LIGHT_BLUE = BLOCKS.register("ritual_candle_light_blue", () -> new RitualCandleBlock(DyeColor.LIGHT_BLUE, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_LIGHT_GRAY = BLOCKS.register("ritual_candle_light_gray", () -> new RitualCandleBlock(DyeColor.LIGHT_GRAY, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_LIME = BLOCKS.register("ritual_candle_lime", () -> new RitualCandleBlock(DyeColor.LIME, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_MAGENTA = BLOCKS.register("ritual_candle_magenta", () -> new RitualCandleBlock(DyeColor.MAGENTA, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_ORANGE = BLOCKS.register("ritual_candle_orange", () -> new RitualCandleBlock(DyeColor.ORANGE, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_PINK = BLOCKS.register("ritual_candle_pink", () -> new RitualCandleBlock(DyeColor.PINK, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_PURPLE = BLOCKS.register("ritual_candle_purple", () -> new RitualCandleBlock(DyeColor.PURPLE, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_RED = BLOCKS.register("ritual_candle_red", () -> new RitualCandleBlock(DyeColor.RED, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_WHITE = BLOCKS.register("ritual_candle_white", () -> new RitualCandleBlock(DyeColor.WHITE, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<RitualCandleBlock> RITUAL_CANDLE_YELLOW = BLOCKS.register("ritual_candle_yellow", () -> new RitualCandleBlock(DyeColor.YELLOW, Block.Properties.of(Material.GRASS).strength(0.0F).lightLevel((state) -> { return 7; }).sound(SoundType.WOOL).noOcclusion()));

    // Register mana fonts
    public static final RegistryObject<AncientManaFontBlock> ANCIENT_FONT_EARTH = BLOCKS.register("ancient_font_earth", () -> new AncientManaFontBlock(Source.EARTH, Block.Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; }).noDrops()));
    public static final RegistryObject<AncientManaFontBlock> ANCIENT_FONT_SEA = BLOCKS.register("ancient_font_sea", () -> new AncientManaFontBlock(Source.SEA, Block.Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; }).noDrops()));
    public static final RegistryObject<AncientManaFontBlock> ANCIENT_FONT_SKY = BLOCKS.register("ancient_font_sky", () -> new AncientManaFontBlock(Source.SKY, Block.Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; }).noDrops()));
    public static final RegistryObject<AncientManaFontBlock> ANCIENT_FONT_SUN = BLOCKS.register("ancient_font_sun", () -> new AncientManaFontBlock(Source.SUN, Block.Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; }).noDrops()));
    public static final RegistryObject<AncientManaFontBlock> ANCIENT_FONT_MOON = BLOCKS.register("ancient_font_moon", () -> new AncientManaFontBlock(Source.MOON, Block.Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; }).noDrops()));
    public static final RegistryObject<ArtificialManaFontBlock> ARTIFICIAL_FONT_EARTH = BLOCKS.register("artificial_font_earth", () -> new ArtificialManaFontBlock(Source.EARTH, DeviceTier.ENCHANTED, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> ARTIFICIAL_FONT_SEA = BLOCKS.register("artificial_font_sea", () -> new ArtificialManaFontBlock(Source.SEA, DeviceTier.ENCHANTED, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> ARTIFICIAL_FONT_SKY = BLOCKS.register("artificial_font_sky", () -> new ArtificialManaFontBlock(Source.SKY, DeviceTier.ENCHANTED, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> ARTIFICIAL_FONT_SUN = BLOCKS.register("artificial_font_sun", () -> new ArtificialManaFontBlock(Source.SUN, DeviceTier.ENCHANTED, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> ARTIFICIAL_FONT_MOON = BLOCKS.register("artificial_font_moon", () -> new ArtificialManaFontBlock(Source.MOON, DeviceTier.ENCHANTED, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> ARTIFICIAL_FONT_BLOOD = BLOCKS.register("artificial_font_blood", () -> new ArtificialManaFontBlock(Source.BLOOD, DeviceTier.ENCHANTED, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> ARTIFICIAL_FONT_INFERNAL = BLOCKS.register("artificial_font_infernal", () -> new ArtificialManaFontBlock(Source.INFERNAL, DeviceTier.ENCHANTED, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> ARTIFICIAL_FONT_VOID = BLOCKS.register("artificial_font_void", () -> new ArtificialManaFontBlock(Source.VOID, DeviceTier.ENCHANTED, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> ARTIFICIAL_FONT_HALLOWED = BLOCKS.register("artificial_font_hallowed", () -> new ArtificialManaFontBlock(Source.HALLOWED, DeviceTier.ENCHANTED, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> FORBIDDEN_FONT_EARTH = BLOCKS.register("forbidden_font_earth", () -> new ArtificialManaFontBlock(Source.EARTH, DeviceTier.FORBIDDEN, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> FORBIDDEN_FONT_SEA = BLOCKS.register("forbidden_font_sea", () -> new ArtificialManaFontBlock(Source.SEA, DeviceTier.FORBIDDEN, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> FORBIDDEN_FONT_SKY = BLOCKS.register("forbidden_font_sky", () -> new ArtificialManaFontBlock(Source.SKY, DeviceTier.FORBIDDEN, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> FORBIDDEN_FONT_SUN = BLOCKS.register("forbidden_font_sun", () -> new ArtificialManaFontBlock(Source.SUN, DeviceTier.FORBIDDEN, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> FORBIDDEN_FONT_MOON = BLOCKS.register("forbidden_font_moon", () -> new ArtificialManaFontBlock(Source.MOON, DeviceTier.FORBIDDEN, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> FORBIDDEN_FONT_BLOOD = BLOCKS.register("forbidden_font_blood", () -> new ArtificialManaFontBlock(Source.BLOOD, DeviceTier.FORBIDDEN, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> FORBIDDEN_FONT_INFERNAL = BLOCKS.register("forbidden_font_infernal", () -> new ArtificialManaFontBlock(Source.INFERNAL, DeviceTier.FORBIDDEN, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> FORBIDDEN_FONT_VOID = BLOCKS.register("forbidden_font_void", () -> new ArtificialManaFontBlock(Source.VOID, DeviceTier.FORBIDDEN, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> FORBIDDEN_FONT_HALLOWED = BLOCKS.register("forbidden_font_hallowed", () -> new ArtificialManaFontBlock(Source.HALLOWED, DeviceTier.FORBIDDEN, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> HEAVENLY_FONT_EARTH = BLOCKS.register("heavenly_font_earth", () -> new ArtificialManaFontBlock(Source.EARTH, DeviceTier.HEAVENLY, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> HEAVENLY_FONT_SEA = BLOCKS.register("heavenly_font_sea", () -> new ArtificialManaFontBlock(Source.SEA, DeviceTier.HEAVENLY, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> HEAVENLY_FONT_SKY = BLOCKS.register("heavenly_font_sky", () -> new ArtificialManaFontBlock(Source.SKY, DeviceTier.HEAVENLY, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> HEAVENLY_FONT_SUN = BLOCKS.register("heavenly_font_sun", () -> new ArtificialManaFontBlock(Source.SUN, DeviceTier.HEAVENLY, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> HEAVENLY_FONT_MOON = BLOCKS.register("heavenly_font_moon", () -> new ArtificialManaFontBlock(Source.MOON, DeviceTier.HEAVENLY, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> HEAVENLY_FONT_BLOOD = BLOCKS.register("heavenly_font_blood", () -> new ArtificialManaFontBlock(Source.BLOOD, DeviceTier.HEAVENLY, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> HEAVENLY_FONT_INFERNAL = BLOCKS.register("heavenly_font_infernal", () -> new ArtificialManaFontBlock(Source.INFERNAL, DeviceTier.HEAVENLY, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> HEAVENLY_FONT_VOID = BLOCKS.register("heavenly_font_void", () -> new ArtificialManaFontBlock(Source.VOID, DeviceTier.HEAVENLY, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));
    public static final RegistryObject<ArtificialManaFontBlock> HEAVENLY_FONT_HALLOWED = BLOCKS.register("heavenly_font_hallowed", () -> new ArtificialManaFontBlock(Source.HALLOWED, DeviceTier.HEAVENLY, Block.Properties.of(Material.STONE).strength(3.0F, 12.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; })));

    // Register devices
    public static final RegistryObject<ArcaneWorkbenchBlock> ARCANE_WORKBENCH = BLOCKS.register("arcane_workbench", ArcaneWorkbenchBlock::new);
    public static final RegistryObject<WandAssemblyTableBlock> WAND_ASSEMBLY_TABLE = BLOCKS.register("wand_assembly_table", WandAssemblyTableBlock::new);
    public static final RegistryObject<WoodTableBlock> WOOD_TABLE = BLOCKS.register("wood_table", WoodTableBlock::new);
    public static final RegistryObject<AnalysisTableBlock> ANALYSIS_TABLE = BLOCKS.register("analysis_table", AnalysisTableBlock::new);
    public static final RegistryObject<EssenceFurnaceBlock> ESSENCE_FURNACE = BLOCKS.register("essence_furnace", EssenceFurnaceBlock::new);
    public static final RegistryObject<CalcinatorBlock> CALCINATOR_BASIC = BLOCKS.register("calcinator_basic", () -> new CalcinatorBlock(DeviceTier.BASIC));
    public static final RegistryObject<CalcinatorBlock> CALCINATOR_ENCHANTED = BLOCKS.register("calcinator_enchanted", () -> new CalcinatorBlock(DeviceTier.ENCHANTED));
    public static final RegistryObject<CalcinatorBlock> CALCINATOR_FORBIDDEN = BLOCKS.register("calcinator_forbidden", () -> new CalcinatorBlock(DeviceTier.FORBIDDEN));
    public static final RegistryObject<CalcinatorBlock> CALCINATOR_HEAVENLY = BLOCKS.register("calcinator_heavenly", () -> new CalcinatorBlock(DeviceTier.HEAVENLY));
    public static final RegistryObject<WandInscriptionTableBlock> WAND_INSCRIPTION_TABLE = BLOCKS.register("wand_inscription_table", WandInscriptionTableBlock::new);
    public static final RegistryObject<SpellcraftingAltarBlock> SPELLCRAFTING_ALTAR = BLOCKS.register("spellcrafting_altar", SpellcraftingAltarBlock::new);
    public static final RegistryObject<WandChargerBlock> WAND_CHARGER = BLOCKS.register("wand_charger", WandChargerBlock::new);
    public static final RegistryObject<ResearchTableBlock> RESEARCH_TABLE = BLOCKS.register("research_table", ResearchTableBlock::new);
    public static final RegistryObject<SunlampBlock> SUNLAMP = BLOCKS.register("sunlamp", () -> new SunlampBlock(() -> BlocksPM.GLOW_FIELD.get()));
    public static final RegistryObject<SunlampBlock> SPIRIT_LANTERN = BLOCKS.register("spirit_lantern", () -> new SunlampBlock(() -> BlocksPM.SOUL_GLOW_FIELD.get()));
    public static final RegistryObject<RitualAltarBlock> RITUAL_ALTAR = BLOCKS.register("ritual_altar", RitualAltarBlock::new);
    public static final RegistryObject<OfferingPedestalBlock> OFFERING_PEDESTAL = BLOCKS.register("offering_pedestal", OfferingPedestalBlock::new);
    public static final RegistryObject<IncenseBrazierBlock> INCENSE_BRAZIER = BLOCKS.register("incense_brazier", IncenseBrazierBlock::new);
    public static final RegistryObject<RitualLecternBlock> RITUAL_LECTERN = BLOCKS.register("ritual_lectern", RitualLecternBlock::new);
    public static final RegistryObject<RitualBellBlock> RITUAL_BELL = BLOCKS.register("ritual_bell", RitualBellBlock::new);
    public static final RegistryObject<BloodletterBlock> BLOODLETTER = BLOCKS.register("bloodletter", BloodletterBlock::new);
    public static final RegistryObject<SoulAnvilBlock> SOUL_ANVIL = BLOCKS.register("soul_anvil", SoulAnvilBlock::new);
    public static final RegistryObject<RunescribingAltarBlock> RUNESCRIBING_ALTAR_BASIC = BLOCKS.register("runescribing_altar_basic", () -> new RunescribingAltarBlock(DeviceTier.BASIC));
    public static final RegistryObject<RunescribingAltarBlock> RUNESCRIBING_ALTAR_ENCHANTED = BLOCKS.register("runescribing_altar_enchanted", () -> new RunescribingAltarBlock(DeviceTier.ENCHANTED));
    public static final RegistryObject<RunescribingAltarBlock> RUNESCRIBING_ALTAR_FORBIDDEN = BLOCKS.register("runescribing_altar_forbidden", () -> new RunescribingAltarBlock(DeviceTier.FORBIDDEN));
    public static final RegistryObject<RunescribingAltarBlock> RUNESCRIBING_ALTAR_HEAVENLY = BLOCKS.register("runescribing_altar_heavenly", () -> new RunescribingAltarBlock(DeviceTier.HEAVENLY));
    public static final RegistryObject<RunecarvingTableBlock> RUNECARVING_TABLE = BLOCKS.register("runecarving_table", RunecarvingTableBlock::new);
    public static final RegistryObject<RunicGrindstoneBlock> RUNIC_GRINDSTONE = BLOCKS.register("runic_grindstone", RunicGrindstoneBlock::new);
    public static final RegistryObject<HoneyExtractorBlock> HONEY_EXTRACTOR = BLOCKS.register("honey_extractor", HoneyExtractorBlock::new);
    public static final RegistryObject<PrimaliteGolemControllerBlock> PRIMALITE_GOLEM_CONTROLLER = BLOCKS.register("primalite_golem_controller", () -> new PrimaliteGolemControllerBlock(Block.Properties.of(Material.METAL).strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<HexiumGolemControllerBlock> HEXIUM_GOLEM_CONTROLLER = BLOCKS.register("hexium_golem_controller", () -> new HexiumGolemControllerBlock(Block.Properties.of(Material.METAL).strength(7.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<HallowsteelGolemControllerBlock> HALLOWSTEEL_GOLEM_CONTROLLER = BLOCKS.register("hallowsteel_golem_controller", () -> new HallowsteelGolemControllerBlock(Block.Properties.of(Material.METAL).strength(9.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<SanguineCrucibleBlock> SANGUINE_CRUCIBLE = BLOCKS.register("sanguine_crucible", SanguineCrucibleBlock::new);
    public static final RegistryObject<ConcocterBlock> CONCOCTER = BLOCKS.register("concocter", ConcocterBlock::new);
    public static final RegistryObject<CelestialHarpBlock> CELESTIAL_HARP = BLOCKS.register("celestial_harp", CelestialHarpBlock::new);
    public static final RegistryObject<EntropySinkBlock> ENTROPY_SINK = BLOCKS.register("entropy_sink", EntropySinkBlock::new);
    public static final RegistryObject<AutoChargerBlock> AUTO_CHARGER = BLOCKS.register("auto_charger", AutoChargerBlock::new);
    public static final RegistryObject<EssenceTransmuterBlock> ESSENCE_TRANSMUTER = BLOCKS.register("essence_transmuter", EssenceTransmuterBlock::new);
    
    // Register misc blocks
    public static final RegistryObject<ConsecrationFieldBlock> CONSECRATION_FIELD = BLOCKS.register("consecration_field", ConsecrationFieldBlock::new);
    public static final RegistryObject<GlowFieldBlock> GLOW_FIELD = BLOCKS.register("glow_field", GlowFieldBlock::new);
    public static final RegistryObject<GlowFieldBlock> SOUL_GLOW_FIELD = BLOCKS.register("soul_glow_field", GlowFieldBlock::new);
    public static final RegistryObject<SaltTrailBlock> SALT_TRAIL = BLOCKS.register("salt_trail", SaltTrailBlock::new);
    public static final RegistryObject<Block> ROCK_SALT_ORE = BLOCKS.register("rock_salt_ore", () -> new Block(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F)));
    public static final RegistryObject<QuartzOreBlock> QUARTZ_ORE = BLOCKS.register("quartz_ore", () -> new QuartzOreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F)));
    public static final RegistryObject<Block> PRIMALITE_BLOCK = BLOCKS.register("primalite_block", () -> new Block(Block.Properties.of(Material.METAL).strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> HEXIUM_BLOCK = BLOCKS.register("hexium_block", () -> new Block(Block.Properties.of(Material.METAL).strength(7.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> HALLOWSTEEL_BLOCK = BLOCKS.register("hallowsteel_block", () -> new Block(Block.Properties.of(Material.METAL).strength(9.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> IGNYX_BLOCK = BLOCKS.register("ignyx_block", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).strength(5.0F, 6.0F)));
    public static final RegistryObject<Block> SALT_BLOCK = BLOCKS.register("salt_block", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.QUARTZ).strength(3.0F, 3.0F)));

    // Helper functions for block properties
    protected static boolean isntSolid(BlockState state, BlockGetter reader, BlockPos pos) {
        return false;
    }

    protected static Boolean allowsSpawnOnLeaves(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
        return entity == EntityType.OCELOT || entity == EntityType.PARROT;
    }
}

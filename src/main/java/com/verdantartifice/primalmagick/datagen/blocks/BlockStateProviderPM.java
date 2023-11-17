package com.verdantartifice.primalmagick.datagen.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.crafting.AbstractCalcinatorBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.ConcocterBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.RunescribingAltarBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.EssenceCaskBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.SanguineCrucibleBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.SunlampBlock;
import com.verdantartifice.primalmagick.common.blocks.golems.AbstractEnchantedGolemControllerBlock;
import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.PillarBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.BloodletterBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.IncenseBrazierBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.RitualCandleBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.SoulAnvilBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.AbstractPhasingBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.AbstractPhasingLeavesBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.AbstractPhasingLogBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.AbstractPhasingPillarBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.AbstractPhasingSlabBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.AbstractPhasingStairsBlock;
import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Data provider for mod block states and associated blocks and items.
 * 
 * @author Daedalus4096
 */
public class BlockStateProviderPM extends BlockStateProvider {
    protected static final ResourceLocation SOLID = new ResourceLocation("solid");
    protected static final ResourceLocation CUTOUT = new ResourceLocation("cutout");
    protected static final ResourceLocation CUTOUT_MIPPED = new ResourceLocation("cutout_mipped");
    protected static final ResourceLocation TRANSLUCENT = new ResourceLocation("translucent");

    public BlockStateProviderPM(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PrimalMagick.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Generate marble blocks
        this.simpleCubeBlockWithItem(BlocksPM.MARBLE_RAW.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_SLAB.get(), BlocksPM.MARBLE_RAW.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_RAW.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_WALL.get(), this.blockTexture(BlocksPM.MARBLE_RAW.get()));
        this.simpleCubeBlockWithItem(BlocksPM.MARBLE_BRICKS.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_BRICK_SLAB.get(), BlocksPM.MARBLE_BRICKS.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_BRICK_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_BRICKS.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_BRICK_WALL.get(), this.blockTexture(BlocksPM.MARBLE_BRICKS.get()));
        this.pillarBlockWithItem(BlocksPM.MARBLE_PILLAR.get());
        this.simpleCubeBlockWithItem(BlocksPM.MARBLE_CHISELED.get());
        this.cubeColumnBlockWithItem(BlocksPM.MARBLE_RUNED.get(), this.blockTexture(BlocksPM.MARBLE_RAW.get()));
        this.simpleCubeBlockWithItem(BlocksPM.MARBLE_TILES.get());
        
        // Generate enchanted marble blocks
        this.simpleCubeBlockWithItem(BlocksPM.MARBLE_ENCHANTED.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_ENCHANTED_SLAB.get(), BlocksPM.MARBLE_ENCHANTED.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_ENCHANTED_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_ENCHANTED.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_ENCHANTED_WALL.get(), this.blockTexture(BlocksPM.MARBLE_ENCHANTED.get()));
        this.simpleCubeBlockWithItem(BlocksPM.MARBLE_ENCHANTED_BRICKS.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB.get(), BlocksPM.MARBLE_ENCHANTED_BRICKS.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_ENCHANTED_BRICKS.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.get(), this.blockTexture(BlocksPM.MARBLE_ENCHANTED_BRICKS.get()));
        this.pillarBlockWithItem(BlocksPM.MARBLE_ENCHANTED_PILLAR.get());
        this.simpleCubeBlockWithItem(BlocksPM.MARBLE_ENCHANTED_CHISELED.get());
        this.cubeColumnBlockWithItem(BlocksPM.MARBLE_ENCHANTED_RUNED.get(), this.blockTexture(BlocksPM.MARBLE_ENCHANTED.get()));
        
        // Generate smoked marble blocks
        this.simpleCubeBlockWithItem(BlocksPM.MARBLE_SMOKED.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_SMOKED_SLAB.get(), BlocksPM.MARBLE_SMOKED.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_SMOKED_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_SMOKED.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_SMOKED_WALL.get(), this.blockTexture(BlocksPM.MARBLE_SMOKED.get()));
        this.simpleCubeBlockWithItem(BlocksPM.MARBLE_SMOKED_BRICKS.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_SMOKED_BRICK_SLAB.get(), BlocksPM.MARBLE_SMOKED_BRICKS.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_SMOKED_BRICKS.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_SMOKED_BRICK_WALL.get(), this.blockTexture(BlocksPM.MARBLE_SMOKED_BRICKS.get()));
        this.pillarBlockWithItem(BlocksPM.MARBLE_SMOKED_PILLAR.get());
        this.simpleCubeBlockWithItem(BlocksPM.MARBLE_SMOKED_CHISELED.get());
        this.cubeColumnBlockWithItem(BlocksPM.MARBLE_SMOKED_RUNED.get(), this.blockTexture(BlocksPM.MARBLE_SMOKED.get()));
        
        // Generate hallowed marble blocks
        this.simpleCubeBlockWithItem(BlocksPM.MARBLE_HALLOWED.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_HALLOWED_SLAB.get(), BlocksPM.MARBLE_HALLOWED.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_HALLOWED_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_HALLOWED.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_HALLOWED_WALL.get(), this.blockTexture(BlocksPM.MARBLE_HALLOWED.get()));
        this.simpleCubeBlockWithItem(BlocksPM.MARBLE_HALLOWED_BRICKS.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_HALLOWED_BRICK_SLAB.get(), BlocksPM.MARBLE_HALLOWED_BRICKS.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_HALLOWED_BRICK_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_HALLOWED_BRICKS.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_HALLOWED_BRICK_WALL.get(), this.blockTexture(BlocksPM.MARBLE_HALLOWED_BRICKS.get()));
        this.pillarBlockWithItem(BlocksPM.MARBLE_HALLOWED_PILLAR.get());
        this.simpleCubeBlockWithItem(BlocksPM.MARBLE_HALLOWED_CHISELED.get());
        this.cubeColumnBlockWithItem(BlocksPM.MARBLE_HALLOWED_RUNED.get(), this.blockTexture(BlocksPM.MARBLE_HALLOWED.get()));
        
        // Generate sunwood blocks
        this.phasingLogBlockWithItem(BlocksPM.SUNWOOD_LOG.get());
        this.phasingLogBlockWithItem(BlocksPM.STRIPPED_SUNWOOD_LOG.get());
        this.phasingWoodBlockWithItem(BlocksPM.SUNWOOD_WOOD.get(), this.blockTexture(BlocksPM.SUNWOOD_LOG.get()));
        this.phasingWoodBlockWithItem(BlocksPM.STRIPPED_SUNWOOD_WOOD.get(), this.blockTexture(BlocksPM.STRIPPED_SUNWOOD_LOG.get()));
        this.phasingLeavesBlockWithItem(BlocksPM.SUNWOOD_LEAVES.get());
        this.saplingBlockWithItem(BlocksPM.SUNWOOD_SAPLING.get());
        this.phasingCubeBlockWithItem(BlocksPM.SUNWOOD_PLANKS.get());
        this.phasingSlabBlockWithItem(BlocksPM.SUNWOOD_SLAB.get(), BlocksPM.SUNWOOD_PLANKS.get());
        this.phasingStairsBlockWithItem(BlocksPM.SUNWOOD_STAIRS.get(), this.blockTexture(BlocksPM.SUNWOOD_PLANKS.get()));
        this.phasingPillarBlockWithItem(BlocksPM.SUNWOOD_PILLAR.get());
        
        // Generate moonwood blocks
        this.phasingLogBlockWithItem(BlocksPM.MOONWOOD_LOG.get());
        this.phasingLogBlockWithItem(BlocksPM.STRIPPED_MOONWOOD_LOG.get());
        this.phasingWoodBlockWithItem(BlocksPM.MOONWOOD_WOOD.get(), this.blockTexture(BlocksPM.MOONWOOD_LOG.get()));
        this.phasingWoodBlockWithItem(BlocksPM.STRIPPED_MOONWOOD_WOOD.get(), this.blockTexture(BlocksPM.STRIPPED_MOONWOOD_LOG.get()));
        this.phasingLeavesBlockWithItem(BlocksPM.MOONWOOD_LEAVES.get());
        this.saplingBlockWithItem(BlocksPM.MOONWOOD_SAPLING.get());
        this.phasingCubeBlockWithItem(BlocksPM.MOONWOOD_PLANKS.get());
        this.phasingSlabBlockWithItem(BlocksPM.MOONWOOD_SLAB.get(), BlocksPM.MOONWOOD_PLANKS.get());
        this.phasingStairsBlockWithItem(BlocksPM.MOONWOOD_STAIRS.get(), this.blockTexture(BlocksPM.MOONWOOD_PLANKS.get()));
        this.phasingPillarBlockWithItem(BlocksPM.MOONWOOD_PILLAR.get());
        
        // Generate hallowood blocks
        this.logBlockWithItem(BlocksPM.HALLOWOOD_LOG.get());
        this.logBlockWithItem(BlocksPM.STRIPPED_HALLOWOOD_LOG.get());
        this.woodBlockWithItem(BlocksPM.HALLOWOOD_WOOD.get(), this.blockTexture(BlocksPM.HALLOWOOD_LOG.get()));
        this.woodBlockWithItem(BlocksPM.STRIPPED_HALLOWOOD_WOOD.get(), this.blockTexture(BlocksPM.STRIPPED_HALLOWOOD_LOG.get()));
        this.leavesBlockWithItem(BlocksPM.HALLOWOOD_LEAVES.get());
        this.saplingBlockWithItem(BlocksPM.HALLOWOOD_SAPLING.get());
        this.simpleCubeBlockWithItem(BlocksPM.HALLOWOOD_PLANKS.get());
        this.slabBlockWithItem(BlocksPM.HALLOWOOD_SLAB.get(), BlocksPM.HALLOWOOD_PLANKS.get());
        this.stairsBlockWithItem(BlocksPM.HALLOWOOD_STAIRS.get(), this.blockTexture(BlocksPM.HALLOWOOD_PLANKS.get()));
        this.pillarBlockWithItem(BlocksPM.HALLOWOOD_PILLAR.get());
        
        // Generate crop blocks
        this.cubeColumnBlockWithItem(BlocksPM.HYDROMELON.get());
        this.stemBlock(BlocksPM.HYRDOMELON_STEM.get());
        this.attachedStemBlock(BlocksPM.ATTACHED_HYDROMELON_STEM.get(), this.blockTexture(BlocksPM.HYRDOMELON_STEM.get()));
        this.tallCrossBlockWithItem(BlocksPM.BLOOD_ROSE.get());
        this.tallExistingBlockWithItem(BlocksPM.EMBERFLOWER.get(), this.blockTexture(BlocksPM.EMBERFLOWER.get()).withSuffix("_front"));
        
        // Generate infused stone blocks
        this.simpleCubeBlockWithItem(BlocksPM.INFUSED_STONE_EARTH.get());
        this.simpleCubeBlockWithItem(BlocksPM.INFUSED_STONE_SEA.get());
        this.simpleCubeBlockWithItem(BlocksPM.INFUSED_STONE_SKY.get());
        this.simpleCubeBlockWithItem(BlocksPM.INFUSED_STONE_SUN.get());
        this.simpleCubeBlockWithItem(BlocksPM.INFUSED_STONE_MOON.get());
        
        // Generate budding gem blocks
        this.directionalCrossBlockWithItem(BlocksPM.SYNTHETIC_AMETHYST_CLUSTER.get());
        this.directionalCrossBlockWithItem(BlocksPM.LARGE_SYNTHETIC_AMETHYST_BUD.get());
        this.directionalCrossBlockWithItem(BlocksPM.MEDIUM_SYNTHETIC_AMETHYST_BUD.get());
        this.directionalCrossBlockWithItem(BlocksPM.SMALL_SYNTHETIC_AMETHYST_BUD.get());
        this.simpleCubeBlockWithItem(BlocksPM.DAMAGED_BUDDING_AMETHYST_BLOCK.get());
        this.simpleCubeBlockWithItem(BlocksPM.CHIPPED_BUDDING_AMETHYST_BLOCK.get());
        this.simpleCubeBlockWithItem(BlocksPM.FLAWED_BUDDING_AMETHYST_BLOCK.get());
        this.directionalCrossBlockWithItem(BlocksPM.SYNTHETIC_DIAMOND_CLUSTER.get());
        this.directionalCrossBlockWithItem(BlocksPM.LARGE_SYNTHETIC_DIAMOND_BUD.get());
        this.directionalCrossBlockWithItem(BlocksPM.MEDIUM_SYNTHETIC_DIAMOND_BUD.get());
        this.directionalCrossBlockWithItem(BlocksPM.SMALL_SYNTHETIC_DIAMOND_BUD.get());
        this.simpleCubeBlockWithItem(BlocksPM.DAMAGED_BUDDING_DIAMOND_BLOCK.get());
        this.simpleCubeBlockWithItem(BlocksPM.CHIPPED_BUDDING_DIAMOND_BLOCK.get());
        this.simpleCubeBlockWithItem(BlocksPM.FLAWED_BUDDING_DIAMOND_BLOCK.get());
        this.directionalCrossBlockWithItem(BlocksPM.SYNTHETIC_EMERALD_CLUSTER.get());
        this.directionalCrossBlockWithItem(BlocksPM.LARGE_SYNTHETIC_EMERALD_BUD.get());
        this.directionalCrossBlockWithItem(BlocksPM.MEDIUM_SYNTHETIC_EMERALD_BUD.get());
        this.directionalCrossBlockWithItem(BlocksPM.SMALL_SYNTHETIC_EMERALD_BUD.get());
        this.simpleCubeBlockWithItem(BlocksPM.DAMAGED_BUDDING_EMERALD_BLOCK.get());
        this.simpleCubeBlockWithItem(BlocksPM.CHIPPED_BUDDING_EMERALD_BLOCK.get());
        this.simpleCubeBlockWithItem(BlocksPM.FLAWED_BUDDING_EMERALD_BLOCK.get());
        this.directionalCrossBlockWithItem(BlocksPM.SYNTHETIC_QUARTZ_CLUSTER.get());
        this.directionalCrossBlockWithItem(BlocksPM.LARGE_SYNTHETIC_QUARTZ_BUD.get());
        this.directionalCrossBlockWithItem(BlocksPM.MEDIUM_SYNTHETIC_QUARTZ_BUD.get());
        this.directionalCrossBlockWithItem(BlocksPM.SMALL_SYNTHETIC_QUARTZ_BUD.get());
        this.cubeColumnBlockWithItem(BlocksPM.DAMAGED_BUDDING_QUARTZ_BLOCK.get());
        this.cubeColumnBlockWithItem(BlocksPM.CHIPPED_BUDDING_QUARTZ_BLOCK.get());
        this.cubeColumnBlockWithItem(BlocksPM.FLAWED_BUDDING_QUARTZ_BLOCK.get());
        
        // TODO Generate skyglass blocks
        // TODO Generate skyglass pane blocks
        
        // Generate ritual candle blocks
        RitualCandleBlock.getAllCandles().forEach(this::ritualCandleBlockWithItem);
        
        // Generate mana font blocks
        AbstractManaFontBlock.getAllManaFontsForTier(DeviceTier.BASIC).forEach(block -> this.manaFontBlockWithItem(block, this.blockTexture(BlocksPM.MARBLE_RAW.get())));
        AbstractManaFontBlock.getAllManaFontsForTier(DeviceTier.ENCHANTED).forEach(block -> this.manaFontBlockWithItem(block, this.blockTexture(BlocksPM.MARBLE_ENCHANTED.get())));
        AbstractManaFontBlock.getAllManaFontsForTier(DeviceTier.FORBIDDEN).forEach(block -> this.manaFontBlockWithItem(block, this.blockTexture(BlocksPM.MARBLE_SMOKED.get())));
        AbstractManaFontBlock.getAllManaFontsForTier(DeviceTier.HEAVENLY).forEach(block -> this.manaFontBlockWithItem(block, this.blockTexture(BlocksPM.MARBLE_HALLOWED.get())));
        
        // Generate device blocks
        this.simpleExistingBlockWithItem(BlocksPM.ARCANE_WORKBENCH.get());
        this.horizontalExistingBlockWithItem(BlocksPM.WAND_ASSEMBLY_TABLE.get());
        this.simpleExistingBlockWithItem(BlocksPM.WOOD_TABLE.get());
        this.horizontalExistingBlockWithRightHandAdjustmentsAndItem(BlocksPM.ANALYSIS_TABLE.get());
        this.calcinatorBlockWithItem(BlocksPM.ESSENCE_FURNACE.get(), state -> this.models()
                .getExistingFile(PrimalMagick.resource("block/essence_furnace").withSuffix(state.getValue(AbstractCalcinatorBlock.LIT) ? "_on" : "")));
        this.calcinatorBlockWithItem(BlocksPM.CALCINATOR_BASIC.get());
        this.calcinatorBlockWithItem(BlocksPM.CALCINATOR_ENCHANTED.get());
        this.calcinatorBlockWithItem(BlocksPM.CALCINATOR_FORBIDDEN.get());
        this.calcinatorBlockWithItem(BlocksPM.CALCINATOR_HEAVENLY.get());
        this.horizontalExistingBlockWithRightHandAdjustmentsAndItem(BlocksPM.WAND_INSCRIPTION_TABLE.get());
        this.spellcraftingAltarBlockWithItem();
        this.simpleExistingBlockWithItem(BlocksPM.WAND_CHARGER.get());
        this.horizontalExistingBlockWithRightHandAdjustmentsAndItem(BlocksPM.RESEARCH_TABLE.get());
        this.sunlampBlockWithItem(BlocksPM.SUNLAMP.get());
        this.sunlampBlockWithItem(BlocksPM.SPIRIT_LANTERN.get());
        this.simpleExistingBlockWithItem(BlocksPM.RITUAL_ALTAR.get());
        this.simpleExistingBlockWithItem(BlocksPM.OFFERING_PEDESTAL.get());
        this.incenseBrazierBlockWithItem();
        this.horizontalExistingBlockWithItem(BlocksPM.RITUAL_LECTERN.get());
        this.ritualBellBlockWithItem();
        this.bloodletterBlockWithItem();
        this.horizontalBlockWithItem(BlocksPM.SOUL_ANVIL.get(), state -> this.models()
                .getExistingFile(this.defaultModel(BlocksPM.SOUL_ANVIL.get()).withSuffix(state.getValue(SoulAnvilBlock.DIRTY) ? "_dirty" : "")));
        this.runescribingAltarBlockWithItem(BlocksPM.RUNESCRIBING_ALTAR_BASIC.get(), this.blockTexture(BlocksPM.MARBLE_RAW.get()));
        this.runescribingAltarBlockWithItem(BlocksPM.RUNESCRIBING_ALTAR_ENCHANTED.get(), this.blockTexture(BlocksPM.MARBLE_ENCHANTED.get()));
        this.runescribingAltarBlockWithItem(BlocksPM.RUNESCRIBING_ALTAR_FORBIDDEN.get(), this.blockTexture(BlocksPM.MARBLE_SMOKED.get()));
        this.runescribingAltarBlockWithItem(BlocksPM.RUNESCRIBING_ALTAR_HEAVENLY.get(), this.blockTexture(BlocksPM.MARBLE_HALLOWED.get()));
        this.horizontalExistingBlockWithRightHandAdjustmentsAndItem(BlocksPM.RUNECARVING_TABLE.get());
        this.horizontalFaceExistingBlockWithItem(BlocksPM.RUNIC_GRINDSTONE.get());
        this.horizontalExistingBlockWithItem(BlocksPM.HONEY_EXTRACTOR.get());
        this.golemControllerBlockWithItem(BlocksPM.PRIMALITE_GOLEM_CONTROLLER.get(), this.blockTexture(BlocksPM.PRIMALITE_BLOCK.get()));
        this.golemControllerBlockWithItem(BlocksPM.HEXIUM_GOLEM_CONTROLLER.get(), this.blockTexture(BlocksPM.HEXIUM_BLOCK.get()));
        this.golemControllerBlockWithItem(BlocksPM.HALLOWSTEEL_GOLEM_CONTROLLER.get(), this.blockTexture(BlocksPM.HALLOWSTEEL_BLOCK.get()));
        this.horizontalBlockWithItem(BlocksPM.SANGUINE_CRUCIBLE.get(), state -> this.models()
                .getExistingFile(this.defaultModel(BlocksPM.SANGUINE_CRUCIBLE.get()).withSuffix(state.getValue(SanguineCrucibleBlock.LIT) ? "_lit" : "")));
        this.horizontalBlockWithItem(BlocksPM.CONCOCTER.get(), state -> this.models()
                .getExistingFile(this.defaultModel(BlocksPM.CONCOCTER.get()).withSuffix(state.getValue(ConcocterBlock.HAS_BOTTLE) ? "_bottle" : "")));
        this.horizontalExistingBlockWithItem(BlocksPM.CELESTIAL_HARP.get());
        this.horizontalExistingBlockWithItem(BlocksPM.ENTROPY_SINK.get());
        this.simpleExistingBlockWithItem(BlocksPM.AUTO_CHARGER.get());
        this.horizontalExistingBlockWithItem(BlocksPM.ESSENCE_TRANSMUTER.get());
        this.horizontalExistingBlockWithItem(BlocksPM.DISSOLUTION_CHAMBER.get());
        this.directionalExistingBlockWithItem(BlocksPM.ZEPHYR_ENGINE.get());
        this.directionalExistingBlockWithItem(BlocksPM.VOID_TURBINE.get());
        this.essenceCaskBlockWithItem(BlocksPM.ESSENCE_CASK_ENCHANTED.get());
        this.essenceCaskBlockWithItem(BlocksPM.ESSENCE_CASK_FORBIDDEN.get());
        this.essenceCaskBlockWithItem(BlocksPM.ESSENCE_CASK_HEAVENLY.get());
        this.horizontalExistingBlockWithItem(BlocksPM.WAND_GLAMOUR_TABLE.get());
        this.infernalFurnaceBlockWithItem();
        this.simpleExistingBlockWithItem(BlocksPM.MANA_NEXUS.get());
        this.simpleExistingBlockWithItem(BlocksPM.MANA_SINGULARITY.get());
        this.simpleExistingBlockWithItem(BlocksPM.MANA_SINGULARITY_CREATIVE.get());
        
        // Generate misc blocks
        this.emptyBlock(BlocksPM.CONSECRATION_FIELD.get()); // Do not generate an item
        this.emptyBlock(BlocksPM.GLOW_FIELD.get());         // Do not generate an item
        this.emptyBlock(BlocksPM.SOUL_GLOW_FIELD.get());    // Do not generate an item
        // TODO Generate salt trail block
        this.simpleCubeBlockWithItem(BlocksPM.ROCK_SALT_ORE.get());
        this.simpleCubeBlockWithItem(BlocksPM.QUARTZ_ORE.get());
        this.simpleCubeBlockWithItem(BlocksPM.PRIMALITE_BLOCK.get());
        this.simpleCubeBlockWithItem(BlocksPM.HEXIUM_BLOCK.get());
        this.simpleCubeBlockWithItem(BlocksPM.HALLOWSTEEL_BLOCK.get());
        this.simpleCubeBlockWithItem(BlocksPM.IGNYX_BLOCK.get());
        this.simpleCubeBlockWithItem(BlocksPM.SALT_BLOCK.get());
        this.crossBlockWithItem(BlocksPM.TREEFOLK_SPROUT.get(), this.key(ItemsPM.TREEFOLK_SEED.get()));
        this.horizontalExistingBlockWithBasicItem(BlocksPM.ENDERWARD.get());

        // TODO Genreate arcanometer blockstates
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }
    
    private ResourceLocation key(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }

    private String name(Block block) {
        return key(block).getPath();
    }
    
    private ResourceLocation defaultModel(Block block) {
        return this.key(block).withPrefix(ModelProvider.BLOCK_FOLDER + "/");
    }
    
    private void emptyBlock(Block block) {
        this.simpleBlock(block, this.models().getExistingFile(PrimalMagick.resource("block/empty")));
    }
    
    private void simpleCubeBlockWithItem(Block block) {
        this.simpleBlockWithItem(block, this.cubeAll(block));
    }
    
    private void simpleExistingBlockWithItem(Block block) {
        this.simpleExistingBlockWithItem(block, this.defaultModel(block));
    }
    
    private void simpleExistingBlockWithItem(Block block, ResourceLocation modelFile) {
        this.simpleBlockWithItem(block, this.models().getExistingFile(modelFile));
    }

    private void slabBlockWithItem(SlabBlock block, Block doubleSlabBlock) {
        this.slabBlockWithItem(block, doubleSlabBlock, this.blockTexture(doubleSlabBlock));
    }
    
    private void slabBlockWithItem(SlabBlock block, Block doubleSlabBlock, ResourceLocation texture) {
        String blockName = this.name(block);
        ModelFile bottomModel = this.models().slab(blockName, texture, texture, texture);
        ModelFile topModel = this.models().slabTop(blockName + "_top", texture, texture, texture);
        this.slabBlock(block, bottomModel, topModel, this.models().getExistingFile(this.key(doubleSlabBlock)));
        this.simpleBlockItem(block, bottomModel);
    }
    
    private void stairsBlockWithItem(StairBlock block, ResourceLocation texture) {
        String baseName = this.name(block);
        ModelFile stairs = this.models().stairs(baseName, texture, texture, texture);
        ModelFile stairsInner = this.models().stairsInner(baseName + "_inner", texture, texture, texture);
        ModelFile stairsOuter = this.models().stairsOuter(baseName + "_outer", texture, texture, texture);
        this.stairsBlock(block, stairs, stairsInner, stairsOuter);
        this.simpleBlockItem(block, stairs);
    }
    
    private void wallBlockWithItem(WallBlock block, ResourceLocation texture) {
        this.wallBlock(block, texture);
        ModelFile wallInv = this.models().wallInventory(this.name(block) + "_inventory", texture);
        this.simpleBlockItem(block, wallInv);
    }
    
    private void cubeColumnBlockWithItem(Block block) {
        this.cubeColumnBlockWithItem(block, this.blockTexture(block).withSuffix("_side"), this.blockTexture(block).withSuffix("_end"));
    }
    
    private void cubeColumnBlockWithItem(Block block, ResourceLocation endTexture) {
        this.cubeColumnBlockWithItem(block, this.blockTexture(block), endTexture);
    }
    
    private void cubeColumnBlockWithItem(Block block, ResourceLocation sideTexture, ResourceLocation endTexture) {
        this.simpleBlockWithItem(block, this.models().cubeColumn(this.name(block), sideTexture, endTexture));
    }
    
    private void pillarBlockWithItem(PillarBlock block) {
        this.pillarBlockWithItem(block, this.blockTexture(block));
    }
    
    private void pillarBlockWithItem(PillarBlock block, ResourceLocation texture) {
        this.pillarBlockWithItem(block, texture, texture.withSuffix("_inner"), texture.withSuffix("_top"), texture.withSuffix("_bottom"), texture.withSuffix("_base"));
    }
    
    private void pillarBlockWithItem(PillarBlock block, ResourceLocation sideTexture, ResourceLocation innerTexture, ResourceLocation topTexture, ResourceLocation bottomTexture, ResourceLocation baseTexture) {
        ModelFile baseModel = this.models().withExistingParent(this.name(block), PrimalMagick.resource("block/pillar"))
                .texture("side", sideTexture)
                .texture("inner", innerTexture);
        ModelFile topModel = this.models().withExistingParent(this.name(block) + "_top", PrimalMagick.resource("block/pillar_top"))
                .texture("side", topTexture)
                .texture("inner", innerTexture)
                .texture("top", baseTexture);
        ModelFile bottomModel = this.models().withExistingParent(this.name(block) + "_bottom", PrimalMagick.resource("block/pillar_bottom"))
                .texture("side", bottomTexture)
                .texture("inner", innerTexture)
                .texture("bottom", baseTexture);
        this.getVariantBuilder(block)
                .partialState().with(PillarBlock.PROPERTY_TYPE, PillarBlock.Type.BASE).addModels(new ConfiguredModel(baseModel))
                .partialState().with(PillarBlock.PROPERTY_TYPE, PillarBlock.Type.TOP).addModels(new ConfiguredModel(topModel))
                .partialState().with(PillarBlock.PROPERTY_TYPE, PillarBlock.Type.BOTTOM).addModels(new ConfiguredModel(bottomModel));
        this.simpleBlockItem(block, baseModel);
    }
    
    private void phasingLogBlockWithItem(AbstractPhasingLogBlock block) {
        Stream.of(TimePhase.values()).forEach(phase -> {
            String phaseName = phase.getSerializedName();
            ResourceLocation sideTexture = this.blockTexture(block).withSuffix("_" + phaseName);
            ResourceLocation endTexture = this.blockTexture(block).withSuffix("_top_" + phaseName);
            ModelFile model = this.models().cubeColumn(this.name(block) + "_" + phaseName, sideTexture, endTexture).renderType(TimePhase.FULL.equals(phase) ? SOLID : TRANSLUCENT);
            this.axisBlockPhase(block, model, model, phase);
        });

        String phaseName = TimePhase.FULL.getSerializedName();
        ResourceLocation sideTexture = this.blockTexture(block).withSuffix("_" + phaseName);
        ResourceLocation endTexture = this.blockTexture(block).withSuffix("_top_" + phaseName);
        this.simpleBlockItem(block, this.models().cubeColumn(this.name(block) + "_" + phaseName, sideTexture, endTexture));
    }
    
    private void phasingWoodBlockWithItem(AbstractPhasingLogBlock block, ResourceLocation texture) {
        Stream.of(TimePhase.values()).forEach(phase -> {
            String phaseName = phase.getSerializedName();
            ResourceLocation phaseTexture = texture.withSuffix("_" + phaseName);
            ModelFile model = this.models().cubeColumn(this.name(block) + "_" + phaseName, phaseTexture, phaseTexture).renderType(TimePhase.FULL.equals(phase) ? SOLID : TRANSLUCENT);
            this.axisBlockPhase(block, model, model, phase);
        });

        String phaseName = TimePhase.FULL.getSerializedName();
        ResourceLocation phaseTexture = texture.withSuffix("_" + phaseName);
        this.simpleBlockItem(block, this.models().cubeColumn(this.name(block) + "_" + phaseName, phaseTexture, phaseTexture));
    }
    
    private void axisBlockPhase(AbstractPhasingLogBlock block, ModelFile vertical, ModelFile horizontal, TimePhase phase) {
        this.getVariantBuilder(block)
            .partialState().with(AbstractPhasingLogBlock.PHASE, phase).with(AbstractPhasingLogBlock.AXIS, Axis.Y).modelForState().modelFile(vertical).addModel()
            .partialState().with(AbstractPhasingLogBlock.PHASE, phase).with(AbstractPhasingLogBlock.AXIS, Axis.Z).modelForState().modelFile(horizontal).rotationX(90).addModel()
            .partialState().with(AbstractPhasingLogBlock.PHASE, phase).with(AbstractPhasingLogBlock.AXIS, Axis.X).modelForState().modelFile(horizontal).rotationX(90).rotationY(90).addModel();
    }
    
    private void phasingLeavesBlockWithItem(AbstractPhasingLeavesBlock block) {
        Stream.of(TimePhase.values()).forEach(phase -> {
            String phaseName = phase.getSerializedName();
            ResourceLocation phaseTexture = this.blockTexture(block).withSuffix("_" + phaseName);
            ModelFile model = this.models().withExistingParent(this.name(block) + "_" + phaseName, new ResourceLocation("block/leaves"))
                    .texture("all", phaseTexture).renderType(TimePhase.FULL.equals(phase) ? CUTOUT : TRANSLUCENT);
            this.getVariantBuilder(block).partialState().with(AbstractPhasingLeavesBlock.PHASE, phase).modelForState().modelFile(model).addModel();
        });

        String phaseName = TimePhase.FULL.getSerializedName();
        ResourceLocation phaseTexture = this.blockTexture(block).withSuffix("_" + phaseName);
        this.simpleBlockItem(block, this.models().withExistingParent(this.name(block) + "_" + phaseName, new ResourceLocation("block/leaves")).texture("all", phaseTexture));
    }
    
    private void saplingBlockWithItem(Block block) {
        this.simpleBlock(block, this.models().cross(this.name(block), this.blockTexture(block)).renderType(CUTOUT));
        this.itemModels().getBuilder(this.key(block).toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", this.blockTexture(block));
    }
    
    private void phasingCubeBlockWithItem(AbstractPhasingBlock block) {
        Stream.of(TimePhase.values()).forEach(phase -> {
            String phaseName = phase.getSerializedName();
            ResourceLocation phaseTexture = this.blockTexture(block).withSuffix("_" + phaseName);
            ModelFile model = this.models().cubeAll(this.name(block) + "_" + phaseName, phaseTexture).renderType(TimePhase.FULL.equals(phase) ? SOLID : TRANSLUCENT);
            this.getVariantBuilder(block).partialState().with(AbstractPhasingBlock.PHASE, phase).modelForState().modelFile(model).addModel();
        });
        
        String phaseName = TimePhase.FULL.getSerializedName();
        ResourceLocation phaseTexture = this.blockTexture(block).withSuffix("_" + phaseName);
        this.simpleBlockItem(block, this.models().cubeAll(this.name(block) + "_" + phaseName, phaseTexture).renderType(SOLID));
    }
    
    private void phasingSlabBlockWithItem(AbstractPhasingSlabBlock block, AbstractPhasingBlock doubleSlabBlock) {
        String blockName = this.name(block);
        ResourceLocation texture = this.blockTexture(doubleSlabBlock);
        Stream.of(TimePhase.values()).forEach(phase -> {
            String phaseName = phase.getSerializedName();
            ResourceLocation phaseTexture = texture.withSuffix("_" + phaseName);
            ResourceLocation renderType = TimePhase.FULL.equals(phase) ? SOLID : TRANSLUCENT;
            ModelFile bottomModel = this.models().slab(blockName + "_" + phaseName, phaseTexture, phaseTexture, phaseTexture).renderType(renderType);
            ModelFile topModel = this.models().slabTop(blockName + "_top_" + phaseName, phaseTexture, phaseTexture, phaseTexture).renderType(renderType);
            ModelFile doubleModel = this.models().getExistingFile(this.key(doubleSlabBlock).withSuffix("_" + phaseName));
            this.getVariantBuilder(block)
                .partialState().with(AbstractPhasingSlabBlock.PHASE, phase).with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(new ConfiguredModel(bottomModel))
                .partialState().with(AbstractPhasingSlabBlock.PHASE, phase).with(SlabBlock.TYPE, SlabType.TOP).addModels(new ConfiguredModel(topModel))
                .partialState().with(AbstractPhasingSlabBlock.PHASE, phase).with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(new ConfiguredModel(doubleModel));
        });
        
        String phaseName = TimePhase.FULL.getSerializedName();
        ResourceLocation phaseTexture = texture.withSuffix("_" + phaseName);
        ModelFile bottomModel = this.models().slab(blockName + "_" + phaseName, phaseTexture, phaseTexture, phaseTexture);
        this.simpleBlockItem(block, bottomModel);
    }
    
    private void phasingStairsBlockWithItem(AbstractPhasingStairsBlock block, ResourceLocation texture) {
        String baseName = this.name(block);
        Map<TimePhase, ModelFile> baseModels = new HashMap<>();
        Map<TimePhase, ModelFile> innerModels = new HashMap<>();
        Map<TimePhase, ModelFile> outerModels = new HashMap<>();
        Stream.of(TimePhase.values()).forEach(phase -> {
            String phaseName = phase.getSerializedName();
            ResourceLocation phaseTexture = texture.withSuffix("_" + phaseName);
            ResourceLocation renderType = TimePhase.FULL.equals(phase) ? SOLID : TRANSLUCENT;
            baseModels.put(phase, this.models().stairs(baseName + "_" + phaseName, phaseTexture, phaseTexture, phaseTexture).renderType(renderType));
            innerModels.put(phase, this.models().stairsInner(baseName + "_inner_" + phaseName, phaseTexture, phaseTexture, phaseTexture).renderType(renderType));
            outerModels.put(phase, this.models().stairsOuter(baseName + "_outer_" + phaseName, phaseTexture, phaseTexture, phaseTexture).renderType(renderType));
        });
        this.getVariantBuilder(block).forAllStatesExcept(state -> {
            Direction facing = state.getValue(StairBlock.FACING);
            Half half = state.getValue(StairBlock.HALF);
            StairsShape shape = state.getValue(StairBlock.SHAPE);
            TimePhase phase = state.getValue(AbstractPhasingStairsBlock.PHASE);
            int yRot = (int) facing.getClockWise().toYRot(); // Stairs model is rotated 90 degrees clockwise for some reason
            if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
                yRot += 270; // Left facing stairs are rotated 90 degrees clockwise
            }
            if (shape != StairsShape.STRAIGHT && half == Half.TOP) {
                yRot += 90; // Top stairs are rotated 90 degrees clockwise
            }
            yRot %= 360;
            boolean uvlock = yRot != 0 || half == Half.TOP; // Don't set uvlock for states that have no rotation
            return ConfiguredModel.builder()
                    .modelFile(shape == StairsShape.STRAIGHT ? baseModels.get(phase) : shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT ? innerModels.get(phase) : outerModels.get(phase))
                    .rotationX(half == Half.BOTTOM ? 0 : 180)
                    .rotationY(yRot)
                    .uvLock(uvlock)
                    .build();
        }, StairBlock.WATERLOGGED);
        this.simpleBlockItem(block, baseModels.get(TimePhase.FULL));
    }
    
    private void phasingPillarBlockWithItem(AbstractPhasingPillarBlock block) {
        this.phasingPillarBlockWithItem(block, this.blockTexture(block));
    }
    
    private void phasingPillarBlockWithItem(AbstractPhasingPillarBlock block, ResourceLocation texture) {
        this.phasingPillarBlockWithItem(block, texture, texture.withSuffix("_inner"), texture.withSuffix("_top"), texture.withSuffix("_bottom"), texture.withSuffix("_base"));
    }
    
    private void phasingPillarBlockWithItem(AbstractPhasingPillarBlock block, ResourceLocation sideTexture, ResourceLocation innerTexture, ResourceLocation topTexture, 
            ResourceLocation bottomTexture, ResourceLocation baseTexture) {
        String baseName = this.name(block);
        Table<PillarBlock.Type, TimePhase, ModelFile> models = HashBasedTable.create();
        Stream.of(TimePhase.values()).forEach(phase -> {
            String phaseName = phase.getSerializedName();
            ResourceLocation renderType = TimePhase.FULL.equals(phase) ? SOLID : TRANSLUCENT;
            models.put(PillarBlock.Type.BASE, phase, this.models().withExistingParent(baseName + "_" + phaseName, PrimalMagick.resource("block/pillar"))
                    .texture("side", sideTexture.withSuffix("_" + phaseName))
                    .texture("inner", innerTexture.withSuffix("_" + phaseName))
                    .renderType(renderType));
            models.put(PillarBlock.Type.TOP, phase, this.models().withExistingParent(baseName + "_top_" + phaseName, PrimalMagick.resource("block/pillar_top"))
                    .texture("side", topTexture.withSuffix("_" + phaseName))
                    .texture("inner", innerTexture.withSuffix("_" + phaseName))
                    .texture("top", baseTexture.withSuffix("_" + phaseName))
                    .renderType(renderType));
            models.put(PillarBlock.Type.BOTTOM, phase, this.models().withExistingParent(baseName + "_bottom_" + phaseName, PrimalMagick.resource("block/pillar_bottom"))
                    .texture("side", bottomTexture.withSuffix("_" + phaseName))
                    .texture("inner", innerTexture.withSuffix("_" + phaseName))
                    .texture("bottom", baseTexture.withSuffix("_" + phaseName))
                    .renderType(renderType));
        });
        this.getVariantBuilder(block).forAllStates(state -> {
            PillarBlock.Type type = state.getValue(AbstractPhasingPillarBlock.PROPERTY_TYPE);
            TimePhase phase = state.getValue(AbstractPhasingPillarBlock.PHASE);
            return ConfiguredModel.builder().modelFile(models.get(type, phase)).build();
        });
        this.simpleBlockItem(block, models.get(PillarBlock.Type.BASE, TimePhase.FULL));
    }
    
    private void logBlockWithItem(RotatedPillarBlock block) {
        ResourceLocation texture = this.blockTexture(block);
        ModelFile model = this.models().cubeColumn(this.name(block), texture, texture.withSuffix("_top"));
        this.getVariantBuilder(block)
            .partialState().with(RotatedPillarBlock.AXIS, Axis.Y).modelForState().modelFile(model).addModel()
            .partialState().with(RotatedPillarBlock.AXIS, Axis.Z).modelForState().modelFile(model).rotationX(90).addModel()
            .partialState().with(RotatedPillarBlock.AXIS, Axis.X).modelForState().modelFile(model).rotationX(90).rotationY(90).addModel();
        this.simpleBlockItem(block, model);
    }
    
    private void woodBlockWithItem(RotatedPillarBlock block, ResourceLocation texture) {
        ModelFile model = this.models().cubeColumn(this.name(block), texture, texture);
        this.axisBlock(block, model, model);
        this.simpleBlockItem(block, model);
    }
    
    private void leavesBlockWithItem(LeavesBlock block) {
        ModelFile model = this.models().withExistingParent(this.name(block), new ResourceLocation("block/leaves")).texture("all", this.blockTexture(block));
        this.simpleBlockWithItem(block, model);
    }
    
    private void ritualCandleBlockWithItem(RitualCandleBlock block) {
        this.simpleExistingBlockWithItem(block, PrimalMagick.resource("block/ritual_candle"));
    }
    
    private void manaFontBlockWithItem(AbstractManaFontBlock block, ResourceLocation baseTexture) {
        this.simpleBlock(block, this.models().withExistingParent(this.name(block), PrimalMagick.resource("block/template_mana_font")).texture("base", baseTexture));
        this.itemModels().withExistingParent(this.name(block), PrimalMagick.resource("item/template_mana_font"));
    }
    
    private void horizontalExistingBlockWithItem(Block block) {
        this.horizontalExistingBlockWithItem(block, this.defaultModel(block));
    }
    
    private void horizontalExistingBlockWithItem(Block block, ResourceLocation modelFile) {
        this.horizontalBlockWithItem(block, this.models().getExistingFile(modelFile));
    }
    
    private void horizontalBlockWithItem(Block block, ModelFile model) {
        this.horizontalBlock(block, model);
        this.simpleBlockItem(block, model);
    }
    
    private void horizontalBlockWithItem(Block block, Function<BlockState, ModelFile> modelFunc) {
        this.horizontalBlock(block, modelFunc);
        this.simpleBlockItem(block, modelFunc.apply(block.defaultBlockState()));
    }
    
    private void horizontalExistingBlockWithBasicItem(Block block) {
        this.horizontalExistingBlockWithBasicItem(block, this.defaultModel(block));
    }
    
    private void horizontalExistingBlockWithBasicItem(Block block, ResourceLocation modelFile) {
        this.horizontalBlockWithBasicItem(block, this.models().getExistingFile(modelFile));
    }
    
    private void horizontalBlockWithBasicItem(Block block, ModelFile model) {
        this.horizontalBlock(block, model);
        this.itemModels().basicItem(block.asItem());
    }
    
    private void horizontalExistingBlockWithRightHandAdjustmentsAndItem(Block block) {
        this.horizontalExistingBlockWithRightHandAdjustmentsAndItem(block, this.defaultModel(block));
    }
    
    private void horizontalExistingBlockWithRightHandAdjustmentsAndItem(Block block, ResourceLocation modelFile) {
        this.horizontalBlockWithRightHandAdjustmentsAndItem(block, this.models().getExistingFile(modelFile));
    }
    
    private void horizontalBlockWithRightHandAdjustmentsAndItem(Block block, ModelFile model) {
        this.horizontalBlock(block, model);
        this.itemModels().getBuilder(this.key(block).toString()).parent(model).transforms()
            .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(75, 135, 0).translation(0, 2.5F, 0).scale(0.375F).end()
            .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, 135, 0).translation(0, 0, 0).scale(0.40F).end();
    }

    private void horizontalFaceExistingBlockWithItem(Block block) {
        this.horizontalFaceBlockWithItem(block, this.models().getExistingFile(this.defaultModel(block)));
    }
    
    private void horizontalFaceBlockWithItem(Block block, ModelFile model) {
        this.horizontalFaceBlock(block, model);
        this.simpleBlockItem(block, model);
    }
    
    private void directionalExistingBlockWithItem(Block block) {
        this.directionalExistingBlockWithItem(block, this.defaultModel(block));
    }
    
    private void directionalExistingBlockWithItem(Block block, ResourceLocation modelFile) {
        this.directionalBlockWithItem(block, this.models().getExistingFile(modelFile));
    }
    
    private void directionalBlockWithItem(Block block, ModelFile model) {
        this.directionalBlock(block, model);
        this.simpleBlockItem(block, model);
    }
    
    private void directionalBlockWithItem(Block block, Function<BlockState, ModelFile> modelFunc) {
        this.directionalBlock(block, modelFunc);
        this.simpleBlockItem(block, modelFunc.apply(block.defaultBlockState()));
    }

    private void calcinatorBlockWithItem(AbstractCalcinatorBlock block) {
        ResourceLocation texture = this.blockTexture(block);
        this.calcinatorBlockWithItem(block, state -> this.models().orientableWithBottom(
                this.name(block) + (state.getValue(AbstractCalcinatorBlock.LIT) ? "_on" : ""), 
                texture.withSuffix("_side"), 
                texture.withSuffix("_front" + (state.getValue(AbstractCalcinatorBlock.LIT) ? "_on" : "")), 
                this.mcLoc("block/furnace_top"), 
                texture.withSuffix("_top")));
    }
    
    private void calcinatorBlockWithItem(AbstractCalcinatorBlock block, Function<BlockState, ModelFile> modelFunc) {
        this.horizontalBlock(block, modelFunc);
        this.simpleBlockItem(block, modelFunc.apply(block.defaultBlockState()));
    }
    
    private void infernalFurnaceBlockWithItem() {
        Block block = BlocksPM.INFERNAL_FURNACE.get();
        ResourceLocation texture = this.blockTexture(block);
        Function<BlockState, ModelFile> modelFunc = state -> this.models().orientableWithBottom(
                this.name(block) + (state.getValue(BlockStateProperties.LIT) ? "_on" : ""), 
                texture.withSuffix("_side"), 
                texture.withSuffix("_front" + (state.getValue(BlockStateProperties.LIT) ? "_on" : "")), 
                texture.withSuffix("_top"), 
                texture.withSuffix("_top"));
        this.horizontalBlock(block, modelFunc);
        this.simpleBlockItem(block, modelFunc.apply(block.defaultBlockState()));
    }
    
    private void spellcraftingAltarBlockWithItem() {
        Block block = BlocksPM.SPELLCRAFTING_ALTAR.get();
        ModelFile model = this.models().getExistingFile(PrimalMagick.resource("block/spellcrafting_altar"));
        this.horizontalBlock(block, model);
        this.itemModels().getBuilder(this.key(block).toString()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).transforms()
            .transform(ItemDisplayContext.GUI).rotation(30, 225, 0).translation(0, 0, 0).scale(0.625F).end()
            .transform(ItemDisplayContext.GROUND).rotation(0, 0, 0).translation(0, 3F, 0).scale(0.25F).end()
            .transform(ItemDisplayContext.FIXED).rotation(0, 0, 0).translation(0, 0, 0).scale(0.5F).end()
            .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(75, 45, 0).translation(0, 2.5F, 0).scale(0.375F).end()
            .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, 45, 0).translation(0, 0, 0).scale(0.40F).end()
            .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 225, 0).translation(0, 0, 0).scale(0.40F).end();
    }
    
    private void sunlampBlockWithItem(SunlampBlock block) {
        ResourceLocation modelLoc = this.defaultModel(block);
        DirectionProperty prop = SunlampBlock.ATTACHMENT;
        this.getMultipartBuilder(block)
            .part().modelFile(this.models().getExistingFile(modelLoc.withSuffix("_ground_base"))).addModel().condition(prop, Direction.DOWN).end()
            .part().modelFile(this.models().getExistingFile(modelLoc.withSuffix("_ground_chain_stub"))).addModel().condition(prop, Direction.DOWN).end()
            .part().modelFile(this.models().getExistingFile(modelLoc.withSuffix("_hanging_base"))).addModel().condition(prop, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP).end()
            .part().modelFile(this.models().getExistingFile(modelLoc.withSuffix("_hanging_chain_stub"))).addModel().condition(prop, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST).end()
            .part().modelFile(this.models().getExistingFile(modelLoc.withSuffix("_hanging_chain_full"))).addModel().condition(prop, Direction.UP).end()
            .part().modelFile(this.models().getExistingFile(modelLoc.withSuffix("_hanging_arm"))).addModel().condition(prop, Direction.NORTH).end()
            .part().modelFile(this.models().getExistingFile(modelLoc.withSuffix("_hanging_arm"))).rotationY(90).addModel().condition(prop, Direction.EAST).end()
            .part().modelFile(this.models().getExistingFile(modelLoc.withSuffix("_hanging_arm"))).rotationY(180).addModel().condition(prop, Direction.SOUTH).end()
            .part().modelFile(this.models().getExistingFile(modelLoc.withSuffix("_hanging_arm"))).rotationY(270).addModel().condition(prop, Direction.WEST).end();
        this.itemModels().basicItem(block.asItem());
    }
    
    private void incenseBrazierBlockWithItem() {
        Block block = BlocksPM.INCENSE_BRAZIER.get();
        ResourceLocation modelLoc = this.defaultModel(block);
        this.getVariantBuilder(block)
            .partialState().with(IncenseBrazierBlock.LIT, false).modelForState().modelFile(this.models().getExistingFile(modelLoc)).addModel()
            .partialState().with(IncenseBrazierBlock.LIT, true).modelForState().modelFile(this.models().getExistingFile(modelLoc.withSuffix("_lit"))).addModel();
        this.simpleBlockItem(block, this.models().getExistingFile(modelLoc));
    }
    
    private void ritualBellBlockWithItem() {
        Block block = BlocksPM.RITUAL_BELL.get();
        this.getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(this.getRitualBellModel(state))
                .rotationY(this.getRitualBellRotationY(state))
                .build());
        this.itemModels().basicItem(block.asItem());
    }

    private ModelFile getRitualBellModel(BlockState state) {
        String suffix = switch (state.getValue(BlockStateProperties.BELL_ATTACHMENT)) {
            case FLOOR -> "_floor";
            case CEILING -> "_ceiling";
            case SINGLE_WALL -> "_wall";
            case DOUBLE_WALL -> "_between_walls";
            default -> throw new IllegalArgumentException("Unknown bell attachment type");
        };
        return this.models().getExistingFile(this.defaultModel(state.getBlock()).withSuffix(suffix));
    }
    
    private int getRitualBellRotationY(BlockState state) {
        int angleOffset = switch (state.getValue(BlockStateProperties.BELL_ATTACHMENT)) {
            case FLOOR, CEILING -> 180;
            case SINGLE_WALL, DOUBLE_WALL -> 90;
        };
        return ((int)state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset) % 360;
    }
    
    private void bloodletterBlockWithItem() {
        Block block = BlocksPM.BLOODLETTER.get();
        ResourceLocation modelLoc = this.defaultModel(block);
        this.getVariantBuilder(block)
            .partialState().with(BloodletterBlock.FILLED, false).modelForState().modelFile(this.models().getExistingFile(modelLoc)).addModel()
            .partialState().with(BloodletterBlock.FILLED, true).modelForState().modelFile(this.models().getExistingFile(modelLoc.withSuffix("_full"))).addModel();
        this.simpleBlockItem(block, this.models().getExistingFile(modelLoc));
    }
    
    private void runescribingAltarBlockWithItem(RunescribingAltarBlock block, ResourceLocation texture) {
        ModelFile model = this.models().withExistingParent(this.name(block), PrimalMagick.resource("block/runescribing_altar"))
                .texture("altar_bottom", texture)
                .texture("altar_side", this.blockTexture(block).withSuffix("_side"));
        this.simpleBlockWithItem(block, model);
    }
    
    private void golemControllerBlockWithItem(AbstractEnchantedGolemControllerBlock<?> block, ResourceLocation topTexture) {
        ResourceLocation baseTexture = this.blockTexture(block);
        ModelFile model = this.models().orientable(this.name(block), baseTexture.withSuffix("_side"), baseTexture.withSuffix("_front"), topTexture);
        this.horizontalBlockWithItem(block, model);
    }
    
    private void essenceCaskBlockWithItem(EssenceCaskBlock block) {
        ResourceLocation texture = this.blockTexture(block);
        this.essenceCaskBlockWithItem(block, state -> this.models().cubeBottomTop(
                this.name(block) + (state.getValue(EssenceCaskBlock.OPEN) ? "_open" : ""), 
                texture.withSuffix("_side"), 
                texture.withSuffix("_bottom"), 
                texture.withSuffix("_top" + (state.getValue(EssenceCaskBlock.OPEN) ? "_open" : ""))));
    }
    
    private void essenceCaskBlockWithItem(EssenceCaskBlock block, Function<BlockState, ModelFile> modelFunc) {
        this.directionalBlockWithItem(block, modelFunc);
        this.simpleBlockItem(block, modelFunc.apply(block.defaultBlockState()));
    }
    
    private ModelFile getCrossModel(Block block) {
        return this.models().cross(this.name(block), this.blockTexture(block)).renderType(CUTOUT);
    }
    
    private void crossBlockWithItem(Block block, ResourceLocation itemTexture) {
        this.simpleBlock(block, this.getCrossModel(block));
        this.itemModels().basicItem(itemTexture);
    }
    
    private void stemBlock(Block block) {
        VariantBlockStateBuilder builder = this.getVariantBuilder(block);
        StemBlock.AGE.getPossibleValues().forEach(stage -> {
            builder.partialState().with(StemBlock.AGE, stage).modelForState().modelFile(
                    this.models().withExistingParent(this.name(block) + "_stage" + stage, new ResourceLocation("block/stem_growth" + stage))
                        .texture("stem", this.blockTexture(block)).renderType(CUTOUT)).addModel();
        });
    }
    
    private void attachedStemBlock(Block block, ResourceLocation lowerStemTexture) {
        ModelFile model = this.models().withExistingParent(this.name(block), new ResourceLocation("block/stem_fruit"))
                .texture("stem", lowerStemTexture)
                .texture("upperstem", this.blockTexture(block))
                .renderType(CUTOUT);
        this.getVariantBuilder(block)
            .partialState().with(AttachedStemBlock.FACING, Direction.NORTH).modelForState().modelFile(model).rotationY(90).addModel()
            .partialState().with(AttachedStemBlock.FACING, Direction.SOUTH).modelForState().modelFile(model).rotationY(270).addModel()
            .partialState().with(AttachedStemBlock.FACING, Direction.WEST).modelForState().modelFile(model).addModel()
            .partialState().with(AttachedStemBlock.FACING, Direction.EAST).modelForState().modelFile(model).rotationY(180).addModel();
    }
    
    private void tallCrossBlockWithItem(Block block) {
        this.tallCrossBlockWithItem(block, this.blockTexture(block).withSuffix("_top"), this.blockTexture(block).withSuffix("_bottom"));
    }
    
    private void tallCrossBlockWithItem(Block block, ResourceLocation topTexture, ResourceLocation bottomTexture) {
        ModelFile topModel = this.models().cross(this.name(block) + "_top", topTexture).renderType(CUTOUT);
        ModelFile bottomModel = this.models().cross(this.name(block) + "_bottom", bottomTexture).renderType(CUTOUT);
        this.getVariantBuilder(block)
            .partialState().with(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).modelForState().modelFile(topModel).addModel()
            .partialState().with(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).modelForState().modelFile(bottomModel).addModel();
        this.itemModels().getBuilder(this.key(block).toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", this.blockTexture(block).withSuffix("_top"));
    }
    
    private void tallExistingBlockWithItem(Block block, ResourceLocation itemTexture) {
        this.tallExistingBlockWithItem(block, this.defaultModel(block).withSuffix("_top"), this.defaultModel(block).withSuffix("_bottom"), itemTexture);
    }
    
    private void tallExistingBlockWithItem(Block block, ResourceLocation topModel, ResourceLocation bottomModel, ResourceLocation itemTexture) {
        this.tallExistingBlockWithItem(block, this.models().getExistingFile(topModel), this.models().getExistingFile(bottomModel), itemTexture);
    }
    
    private void tallExistingBlockWithItem(Block block, ModelFile topModel, ModelFile bottomModel, ResourceLocation itemTexture) {
        this.getVariantBuilder(block)
            .partialState().with(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).modelForState().modelFile(topModel).addModel()
            .partialState().with(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).modelForState().modelFile(bottomModel).addModel();
        this.itemModels().getBuilder(this.key(block).toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", itemTexture);
    }
    
    private void directionalCrossBlockWithItem(Block block) {
        this.directionalCrossBlockWithItem(block, this.blockTexture(block));
    }
    
    private void directionalCrossBlockWithItem(Block block, ResourceLocation itemTexture) {
        this.directionalBlock(block, this.getCrossModel(block));
        this.itemModels().getBuilder(this.key(block).toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", itemTexture);
    }
}

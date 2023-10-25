package com.verdantartifice.primalmagick.datagen.tags;

import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.tags.BlockTagsForgeExt;
import com.verdantartifice.primalmagick.common.tags.BlockTagsPM;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Data provider for all of the mod's block tags, both original tags and modifications to vanilla tags.
 * 
 * @author Daedalus4096
 */
public class BlockTagsProviderPM extends BlockTagsProvider {
    public BlockTagsProviderPM(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper) {
        super(packOutput, lookupProvider, PrimalMagick.MODID, helper);
    }

    @Override
    public String getName() {
        return "Primal Magick Block Tags";
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        // Add entries to vanilla tags
        this.tag(BlockTags.BEACON_BASE_BLOCKS).addTag(BlockTagsPM.STORAGE_BLOCKS_PRIMALITE).addTag(BlockTagsPM.STORAGE_BLOCKS_HEXIUM).addTag(BlockTagsPM.STORAGE_BLOCKS_HALLOWSTEEL);
        this.tag(BlockTags.CROPS).add(BlocksPM.HYRDOMELON_STEM.get());
        this.tag(BlockTags.CRYSTAL_SOUND_BLOCKS).add(BlocksPM.DAMAGED_BUDDING_AMETHYST_BLOCK.get(), BlocksPM.CHIPPED_BUDDING_AMETHYST_BLOCK.get(), BlocksPM.FLAWED_BUDDING_AMETHYST_BLOCK.get(), BlocksPM.DAMAGED_BUDDING_DIAMOND_BLOCK.get(), BlocksPM.CHIPPED_BUDDING_DIAMOND_BLOCK.get(), BlocksPM.FLAWED_BUDDING_DIAMOND_BLOCK.get(), BlocksPM.DAMAGED_BUDDING_EMERALD_BLOCK.get(), BlocksPM.CHIPPED_BUDDING_EMERALD_BLOCK.get(), BlocksPM.FLAWED_BUDDING_EMERALD_BLOCK.get(), BlocksPM.DAMAGED_BUDDING_QUARTZ_BLOCK.get(), BlocksPM.CHIPPED_BUDDING_QUARTZ_BLOCK.get(), BlocksPM.FLAWED_BUDDING_QUARTZ_BLOCK.get());
        this.tag(BlockTags.LOGS_THAT_BURN).addTag(BlockTagsPM.MOONWOOD_LOGS).addTag(BlockTagsPM.SUNWOOD_LOGS).addTag(BlockTagsPM.HALLOWOOD_LOGS);
        this.tag(BlockTags.LEAVES).add(BlocksPM.MOONWOOD_LEAVES.get(), BlocksPM.SUNWOOD_LEAVES.get(), BlocksPM.HALLOWOOD_LEAVES.get());
        this.tag(BlockTags.PIGLIN_REPELLENTS).add(BlocksPM.SPIRIT_LANTERN.get(), BlocksPM.SOUL_GLOW_FIELD.get());
        this.tag(BlockTags.PLANKS).add(BlocksPM.MOONWOOD_PLANKS.get(), BlocksPM.SUNWOOD_PLANKS.get(), BlocksPM.HALLOWOOD_PLANKS.get());
        this.tag(BlockTags.SAPLINGS).add(BlocksPM.MOONWOOD_SAPLING.get(), BlocksPM.SUNWOOD_SAPLING.get(), BlocksPM.HALLOWOOD_SAPLING.get());
        this.tag(BlockTags.TALL_FLOWERS).add(BlocksPM.BLOOD_ROSE.get(), BlocksPM.EMBERFLOWER.get());
        this.tag(BlockTags.WALLS).add(BlocksPM.MARBLE_WALL.get(), BlocksPM.MARBLE_BRICK_WALL.get(), BlocksPM.MARBLE_ENCHANTED_WALL.get(), BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.get(), BlocksPM.MARBLE_SMOKED_WALL.get(), BlocksPM.MARBLE_SMOKED_BRICK_WALL.get(), BlocksPM.MARBLE_HALLOWED_WALL.get(), BlocksPM.MARBLE_HALLOWED_BRICK_WALL.get());
        this.tag(BlockTags.WOODEN_SLABS).add(BlocksPM.MOONWOOD_SLAB.get(), BlocksPM.SUNWOOD_SLAB.get(), BlocksPM.HALLOWOOD_SLAB.get());
        this.tag(BlockTags.WOODEN_STAIRS).add(BlocksPM.MOONWOOD_STAIRS.get(), BlocksPM.SUNWOOD_STAIRS.get(), BlocksPM.HALLOWOOD_STAIRS.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(BlocksPM.SUNWOOD_PILLAR.get(), BlocksPM.MOONWOOD_PILLAR.get(), BlocksPM.HALLOWOOD_PILLAR.get(), BlocksPM.ARCANE_WORKBENCH.get(), BlocksPM.WOOD_TABLE.get(), BlocksPM.ANALYSIS_TABLE.get(), BlocksPM.WAND_INSCRIPTION_TABLE.get(), BlocksPM.RESEARCH_TABLE.get(), BlocksPM.RITUAL_LECTERN.get(), BlocksPM.RUNECARVING_TABLE.get(), BlocksPM.CELESTIAL_HARP.get(), BlocksPM.HYDROMELON.get(), BlocksPM.ATTACHED_HYDROMELON_STEM.get(), BlocksPM.HYRDOMELON_STEM.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BlocksPM.MARBLE_RAW.get(), BlocksPM.MARBLE_SLAB.get(), BlocksPM.MARBLE_STAIRS.get(), BlocksPM.MARBLE_WALL.get(), BlocksPM.MARBLE_BRICKS.get(), BlocksPM.MARBLE_BRICK_SLAB.get(), BlocksPM.MARBLE_BRICK_STAIRS.get(), BlocksPM.MARBLE_BRICK_WALL.get(), BlocksPM.MARBLE_PILLAR.get(), BlocksPM.MARBLE_CHISELED.get(), BlocksPM.MARBLE_RUNED.get(), BlocksPM.MARBLE_TILES.get(), BlocksPM.MARBLE_ENCHANTED.get(), BlocksPM.MARBLE_ENCHANTED_SLAB.get(), BlocksPM.MARBLE_ENCHANTED_STAIRS.get(), BlocksPM.MARBLE_ENCHANTED_WALL.get(), BlocksPM.MARBLE_ENCHANTED_BRICKS.get(), BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB.get(), BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS.get(), BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.get(), BlocksPM.MARBLE_ENCHANTED_PILLAR.get(), BlocksPM.MARBLE_ENCHANTED_CHISELED.get(), BlocksPM.MARBLE_ENCHANTED_RUNED.get(), BlocksPM.MARBLE_SMOKED.get(), BlocksPM.MARBLE_SMOKED_SLAB.get(), BlocksPM.MARBLE_SMOKED_STAIRS.get(), BlocksPM.MARBLE_SMOKED_WALL.get(), BlocksPM.MARBLE_SMOKED_BRICKS.get(), BlocksPM.MARBLE_SMOKED_BRICK_SLAB.get(), BlocksPM.MARBLE_SMOKED_BRICK_STAIRS.get(), BlocksPM.MARBLE_SMOKED_BRICK_WALL.get(), BlocksPM.MARBLE_SMOKED_PILLAR.get(), BlocksPM.MARBLE_SMOKED_CHISELED.get(), BlocksPM.MARBLE_SMOKED_RUNED.get(), BlocksPM.MARBLE_HALLOWED.get(), BlocksPM.MARBLE_HALLOWED_SLAB.get(), BlocksPM.MARBLE_HALLOWED_STAIRS.get(), BlocksPM.MARBLE_HALLOWED_WALL.get(), BlocksPM.MARBLE_HALLOWED_BRICKS.get(), BlocksPM.MARBLE_HALLOWED_BRICK_SLAB.get(), BlocksPM.MARBLE_HALLOWED_BRICK_STAIRS.get(), BlocksPM.MARBLE_HALLOWED_BRICK_WALL.get(), BlocksPM.MARBLE_HALLOWED_PILLAR.get(), BlocksPM.MARBLE_HALLOWED_CHISELED.get(), BlocksPM.MARBLE_HALLOWED_RUNED.get(), BlocksPM.INFUSED_STONE_EARTH.get(), BlocksPM.INFUSED_STONE_SEA.get(), BlocksPM.INFUSED_STONE_SKY.get(), BlocksPM.INFUSED_STONE_SUN.get(), BlocksPM.INFUSED_STONE_MOON.get(), BlocksPM.ARTIFICIAL_FONT_EARTH.get(), BlocksPM.ARTIFICIAL_FONT_SEA.get(), BlocksPM.ARTIFICIAL_FONT_SKY.get(), BlocksPM.ARTIFICIAL_FONT_SUN.get(), BlocksPM.ARTIFICIAL_FONT_MOON.get(), BlocksPM.ARTIFICIAL_FONT_BLOOD.get(), BlocksPM.ARTIFICIAL_FONT_INFERNAL.get(), BlocksPM.ARTIFICIAL_FONT_VOID.get(), BlocksPM.ARTIFICIAL_FONT_HALLOWED.get(), BlocksPM.FORBIDDEN_FONT_EARTH.get(), BlocksPM.FORBIDDEN_FONT_SEA.get(), BlocksPM.FORBIDDEN_FONT_SKY.get(), BlocksPM.FORBIDDEN_FONT_SUN.get(), BlocksPM.FORBIDDEN_FONT_MOON.get(), BlocksPM.FORBIDDEN_FONT_BLOOD.get(), BlocksPM.FORBIDDEN_FONT_INFERNAL.get(), BlocksPM.FORBIDDEN_FONT_VOID.get(), BlocksPM.FORBIDDEN_FONT_HALLOWED.get(), BlocksPM.HEAVENLY_FONT_EARTH.get(), BlocksPM.HEAVENLY_FONT_SEA.get(), BlocksPM.HEAVENLY_FONT_SKY.get(), BlocksPM.HEAVENLY_FONT_SUN.get(), BlocksPM.HEAVENLY_FONT_MOON.get(), BlocksPM.HEAVENLY_FONT_BLOOD.get(), BlocksPM.HEAVENLY_FONT_INFERNAL.get(), BlocksPM.HEAVENLY_FONT_VOID.get(), BlocksPM.HEAVENLY_FONT_HALLOWED.get(), BlocksPM.WAND_ASSEMBLY_TABLE.get(), BlocksPM.ESSENCE_FURNACE.get(), BlocksPM.CALCINATOR_BASIC.get(), BlocksPM.CALCINATOR_ENCHANTED.get(), BlocksPM.CALCINATOR_FORBIDDEN.get(), BlocksPM.CALCINATOR_HEAVENLY.get(), BlocksPM.SPELLCRAFTING_ALTAR.get(), BlocksPM.WAND_CHARGER.get(), BlocksPM.RITUAL_ALTAR.get(), BlocksPM.OFFERING_PEDESTAL.get(), BlocksPM.INCENSE_BRAZIER.get(), BlocksPM.BLOODLETTER.get(), BlocksPM.SOUL_ANVIL.get(), BlocksPM.RUNESCRIBING_ALTAR_BASIC.get(), BlocksPM.RUNESCRIBING_ALTAR_ENCHANTED.get(), BlocksPM.RUNESCRIBING_ALTAR_FORBIDDEN.get(), BlocksPM.RUNESCRIBING_ALTAR_HEAVENLY.get(), BlocksPM.RUNIC_GRINDSTONE.get(), BlocksPM.HONEY_EXTRACTOR.get(), BlocksPM.PRIMALITE_GOLEM_CONTROLLER.get(), BlocksPM.HEXIUM_GOLEM_CONTROLLER.get(), BlocksPM.HALLOWSTEEL_GOLEM_CONTROLLER.get(), BlocksPM.SANGUINE_CRUCIBLE.get(), BlocksPM.CONCOCTER.get(), BlocksPM.ENTROPY_SINK.get(), BlocksPM.ROCK_SALT_ORE.get(), BlocksPM.QUARTZ_ORE.get(), BlocksPM.PRIMALITE_BLOCK.get(), BlocksPM.HEXIUM_BLOCK.get(), BlocksPM.HALLOWSTEEL_BLOCK.get(), BlocksPM.IGNYX_BLOCK.get(), BlocksPM.SALT_BLOCK.get(), BlocksPM.ESSENCE_TRANSMUTER.get(), BlocksPM.INFERNAL_FURNACE.get(), BlocksPM.MANA_NEXUS.get(), BlocksPM.MANA_SINGULARITY.get(), BlocksPM.MANA_SINGULARITY_CREATIVE.get());
        this.tag(BlockTags.SWORD_EFFICIENT).add(BlocksPM.HYDROMELON.get(), BlocksPM.ATTACHED_HYDROMELON_STEM.get(), BlocksPM.BLOOD_ROSE.get(), BlocksPM.EMBERFLOWER.get());
        this.tag(BlockTags.NEEDS_STONE_TOOL).add(BlocksPM.ROCK_SALT_ORE.get(), BlocksPM.QUARTZ_ORE.get(), BlocksPM.PRIMALITE_BLOCK.get(), BlocksPM.SALT_BLOCK.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(BlocksPM.HEXIUM_BLOCK.get(), BlocksPM.SANGUINE_CRUCIBLE.get());
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(BlocksPM.HALLOWSTEEL_BLOCK.get());
        this.tag(BlockTags.MAINTAINS_FARMLAND).add(BlocksPM.ATTACHED_HYDROMELON_STEM.get(), BlocksPM.HYRDOMELON_STEM.get());
        
        // Add entries to Forge tags
        this.tag(Tags.Blocks.ORE_RATES_DENSE).add(BlocksPM.ROCK_SALT_ORE.get());
        this.tag(Tags.Blocks.ORE_RATES_SINGULAR).add(BlocksPM.QUARTZ_ORE.get());
        this.tag(Tags.Blocks.ORES).addTag(BlockTagsForgeExt.ORES_ROCK_SALT);
        this.tag(Tags.Blocks.ORES_QUARTZ).add(BlocksPM.QUARTZ_ORE.get());
        this.tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(BlocksPM.QUARTZ_ORE.get(), BlocksPM.ROCK_SALT_ORE.get());
        this.tag(Tags.Blocks.STORAGE_BLOCKS).add(BlocksPM.IGNYX_BLOCK.get(), BlocksPM.SALT_BLOCK.get()).addTag(BlockTagsPM.STORAGE_BLOCKS_PRIMALITE).addTag(BlockTagsPM.STORAGE_BLOCKS_HEXIUM).addTag(BlockTagsPM.STORAGE_BLOCKS_HALLOWSTEEL);
        
        this.tag(Tags.Blocks.GLASS_COLORLESS).add(BlocksPM.SKYGLASS.get());
        this.tag(Tags.Blocks.GLASS_BLACK).add(BlocksPM.STAINED_SKYGLASS_BLACK.get());
        this.tag(Tags.Blocks.GLASS_BLUE).add(BlocksPM.STAINED_SKYGLASS_BLUE.get());
        this.tag(Tags.Blocks.GLASS_BROWN).add(BlocksPM.STAINED_SKYGLASS_BROWN.get());
        this.tag(Tags.Blocks.GLASS_CYAN).add(BlocksPM.STAINED_SKYGLASS_CYAN.get());
        this.tag(Tags.Blocks.GLASS_GRAY).add(BlocksPM.STAINED_SKYGLASS_GRAY.get());
        this.tag(Tags.Blocks.GLASS_GREEN).add(BlocksPM.STAINED_SKYGLASS_GREEN.get());
        this.tag(Tags.Blocks.GLASS_LIGHT_BLUE).add(BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE.get());
        this.tag(Tags.Blocks.GLASS_LIGHT_GRAY).add(BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY.get());
        this.tag(Tags.Blocks.GLASS_LIME).add(BlocksPM.STAINED_SKYGLASS_LIME.get());
        this.tag(Tags.Blocks.GLASS_MAGENTA).add(BlocksPM.STAINED_SKYGLASS_MAGENTA.get());
        this.tag(Tags.Blocks.GLASS_ORANGE).add(BlocksPM.STAINED_SKYGLASS_ORANGE.get());
        this.tag(Tags.Blocks.GLASS_PINK).add(BlocksPM.STAINED_SKYGLASS_PINK.get());
        this.tag(Tags.Blocks.GLASS_PURPLE).add(BlocksPM.STAINED_SKYGLASS_PURPLE.get());
        this.tag(Tags.Blocks.GLASS_RED).add(BlocksPM.STAINED_SKYGLASS_RED.get());
        this.tag(Tags.Blocks.GLASS_WHITE).add(BlocksPM.STAINED_SKYGLASS_WHITE.get());
        this.tag(Tags.Blocks.GLASS_YELLOW).add(BlocksPM.STAINED_SKYGLASS_YELLOW.get());
        this.tag(Tags.Blocks.STAINED_GLASS).addTag(BlockTagsPM.STAINED_SKYGLASS);
        
        this.tag(Tags.Blocks.GLASS_PANES_COLORLESS).add(BlocksPM.SKYGLASS_PANE.get());
        this.tag(Tags.Blocks.GLASS_PANES_BLACK).add(BlocksPM.STAINED_SKYGLASS_PANE_BLACK.get());
        this.tag(Tags.Blocks.GLASS_PANES_BLUE).add(BlocksPM.STAINED_SKYGLASS_PANE_BLUE.get());
        this.tag(Tags.Blocks.GLASS_PANES_BROWN).add(BlocksPM.STAINED_SKYGLASS_PANE_BROWN.get());
        this.tag(Tags.Blocks.GLASS_PANES_CYAN).add(BlocksPM.STAINED_SKYGLASS_PANE_CYAN.get());
        this.tag(Tags.Blocks.GLASS_PANES_GRAY).add(BlocksPM.STAINED_SKYGLASS_PANE_GRAY.get());
        this.tag(Tags.Blocks.GLASS_PANES_GREEN).add(BlocksPM.STAINED_SKYGLASS_PANE_GREEN.get());
        this.tag(Tags.Blocks.GLASS_PANES_LIGHT_BLUE).add(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get());
        this.tag(Tags.Blocks.GLASS_PANES_LIGHT_GRAY).add(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get());
        this.tag(Tags.Blocks.GLASS_PANES_LIME).add(BlocksPM.STAINED_SKYGLASS_PANE_LIME.get());
        this.tag(Tags.Blocks.GLASS_PANES_MAGENTA).add(BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA.get());
        this.tag(Tags.Blocks.GLASS_PANES_ORANGE).add(BlocksPM.STAINED_SKYGLASS_PANE_ORANGE.get());
        this.tag(Tags.Blocks.GLASS_PANES_PINK).add(BlocksPM.STAINED_SKYGLASS_PANE_PINK.get());
        this.tag(Tags.Blocks.GLASS_PANES_PURPLE).add(BlocksPM.STAINED_SKYGLASS_PANE_PURPLE.get());
        this.tag(Tags.Blocks.GLASS_PANES_RED).add(BlocksPM.STAINED_SKYGLASS_PANE_RED.get());
        this.tag(Tags.Blocks.GLASS_PANES_WHITE).add(BlocksPM.STAINED_SKYGLASS_PANE_WHITE.get());
        this.tag(Tags.Blocks.GLASS_PANES_YELLOW).add(BlocksPM.STAINED_SKYGLASS_PANE_YELLOW.get());
        this.tag(Tags.Blocks.STAINED_GLASS_PANES).addTag(BlockTagsPM.STAINED_SKYGLASS_PANES);
        
        // Add entries to Forge extension tags
        this.tag(BlockTagsForgeExt.ORES_ROCK_SALT).add(BlocksPM.ROCK_SALT_ORE.get());
        this.tag(BlockTagsForgeExt.BUDDING).add(BlocksPM.DAMAGED_BUDDING_AMETHYST_BLOCK.get(), BlocksPM.CHIPPED_BUDDING_AMETHYST_BLOCK.get(), BlocksPM.FLAWED_BUDDING_AMETHYST_BLOCK.get(), BlocksPM.DAMAGED_BUDDING_DIAMOND_BLOCK.get(), BlocksPM.CHIPPED_BUDDING_DIAMOND_BLOCK.get(), BlocksPM.FLAWED_BUDDING_DIAMOND_BLOCK.get(), BlocksPM.DAMAGED_BUDDING_EMERALD_BLOCK.get(), BlocksPM.CHIPPED_BUDDING_EMERALD_BLOCK.get(), BlocksPM.FLAWED_BUDDING_EMERALD_BLOCK.get(), BlocksPM.DAMAGED_BUDDING_QUARTZ_BLOCK.get(), BlocksPM.CHIPPED_BUDDING_QUARTZ_BLOCK.get(), BlocksPM.FLAWED_BUDDING_QUARTZ_BLOCK.get());
        this.tag(BlockTagsForgeExt.MINEABLE_WITH_SHEARS).addTag(BlockTags.LEAVES).addTag(BlockTags.WOOL).add(Blocks.COBWEB, Blocks.GRASS, Blocks.FERN, Blocks.DEAD_BUSH, Blocks.HANGING_ROOTS, Blocks.VINE, Blocks.TRIPWIRE);
        
        // Create custom tags
        this.tag(BlockTagsPM.BOUNTY_CROPS).addTag(BlockTags.CROPS).add(Blocks.NETHER_WART);
        this.tag(BlockTagsPM.COLORED_SHULKER_BOXES).add(Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX);
        this.tag(BlockTagsPM.CONCRETE).add(Blocks.BLACK_CONCRETE, Blocks.BLUE_CONCRETE, Blocks.BROWN_CONCRETE, Blocks.CYAN_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.GREEN_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE, Blocks.LIME_CONCRETE, Blocks.MAGENTA_CONCRETE, Blocks.ORANGE_CONCRETE, Blocks.PINK_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.RED_CONCRETE, Blocks.WHITE_CONCRETE, Blocks.YELLOW_CONCRETE);
        this.tag(BlockTagsPM.DEAD_CORAL_BLOCKS).add(Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.DEAD_TUBE_CORAL_BLOCK);
        this.tag(BlockTagsPM.DEAD_CORAL_PLANTS).add(Blocks.DEAD_BRAIN_CORAL, Blocks.DEAD_BUBBLE_CORAL, Blocks.DEAD_FIRE_CORAL, Blocks.DEAD_HORN_CORAL, Blocks.DEAD_TUBE_CORAL);
        this.tag(BlockTagsPM.DEAD_CORALS).addTag(BlockTagsPM.DEAD_CORAL_PLANTS).add(Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.DEAD_HORN_CORAL_FAN, Blocks.DEAD_TUBE_CORAL_FAN);
        this.tag(BlockTagsPM.ENCHANTING_TABLES).add(Blocks.ENCHANTING_TABLE).addOptional(new ResourceLocation("quark", "matrix_enchanter"));
        this.tag(BlockTagsPM.HALLOWOOD_LOGS).add(BlocksPM.HALLOWOOD_LOG.get(), BlocksPM.STRIPPED_HALLOWOOD_LOG.get(), BlocksPM.HALLOWOOD_WOOD.get(), BlocksPM.STRIPPED_HALLOWOOD_WOOD.get());
        this.tag(BlockTagsPM.MOONWOOD_LOGS).add(BlocksPM.MOONWOOD_LOG.get(), BlocksPM.STRIPPED_MOONWOOD_LOG.get(), BlocksPM.MOONWOOD_WOOD.get(), BlocksPM.STRIPPED_MOONWOOD_WOOD.get());
        this.tag(BlockTagsPM.RITUAL_CANDLES).add(BlocksPM.RITUAL_CANDLE_BLACK.get(), BlocksPM.RITUAL_CANDLE_BLUE.get(), BlocksPM.RITUAL_CANDLE_BROWN.get(), BlocksPM.RITUAL_CANDLE_CYAN.get(), BlocksPM.RITUAL_CANDLE_GRAY.get(), BlocksPM.RITUAL_CANDLE_GREEN.get(), BlocksPM.RITUAL_CANDLE_LIGHT_BLUE.get(), BlocksPM.RITUAL_CANDLE_LIGHT_GRAY.get(), BlocksPM.RITUAL_CANDLE_LIME.get(), BlocksPM.RITUAL_CANDLE_MAGENTA.get(), BlocksPM.RITUAL_CANDLE_ORANGE.get(), BlocksPM.RITUAL_CANDLE_PINK.get(), BlocksPM.RITUAL_CANDLE_PURPLE.get(), BlocksPM.RITUAL_CANDLE_RED.get(), BlocksPM.RITUAL_CANDLE_WHITE.get(), BlocksPM.RITUAL_CANDLE_YELLOW.get());
        this.tag(BlockTagsPM.SHULKER_BOXES).addTag(BlockTagsPM.COLORED_SHULKER_BOXES).add(Blocks.SHULKER_BOX);
        this.tag(BlockTagsPM.SKYGLASS).add(BlocksPM.SKYGLASS.get()).addTag(BlockTagsPM.STAINED_SKYGLASS);
        this.tag(BlockTagsPM.SKYGLASS_PANES).add(BlocksPM.SKYGLASS_PANE.get()).addTag(BlockTagsPM.STAINED_SKYGLASS_PANES);
        this.tag(BlockTagsPM.STAINED_SKYGLASS).add(BlocksPM.STAINED_SKYGLASS_BLACK.get(), BlocksPM.STAINED_SKYGLASS_BLUE.get(), BlocksPM.STAINED_SKYGLASS_BROWN.get(), BlocksPM.STAINED_SKYGLASS_CYAN.get(), BlocksPM.STAINED_SKYGLASS_GRAY.get(), BlocksPM.STAINED_SKYGLASS_GREEN.get(), BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), BlocksPM.STAINED_SKYGLASS_LIME.get(), BlocksPM.STAINED_SKYGLASS_MAGENTA.get(), BlocksPM.STAINED_SKYGLASS_ORANGE.get(), BlocksPM.STAINED_SKYGLASS_PINK.get(), BlocksPM.STAINED_SKYGLASS_PURPLE.get(), BlocksPM.STAINED_SKYGLASS_RED.get(), BlocksPM.STAINED_SKYGLASS_WHITE.get(), BlocksPM.STAINED_SKYGLASS_YELLOW.get());
        this.tag(BlockTagsPM.STAINED_SKYGLASS_PANES).add(BlocksPM.STAINED_SKYGLASS_PANE_BLACK.get(), BlocksPM.STAINED_SKYGLASS_PANE_BLUE.get(), BlocksPM.STAINED_SKYGLASS_PANE_BROWN.get(), BlocksPM.STAINED_SKYGLASS_PANE_CYAN.get(), BlocksPM.STAINED_SKYGLASS_PANE_GRAY.get(), BlocksPM.STAINED_SKYGLASS_PANE_GREEN.get(), BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), BlocksPM.STAINED_SKYGLASS_PANE_LIME.get(), BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), BlocksPM.STAINED_SKYGLASS_PANE_ORANGE.get(), BlocksPM.STAINED_SKYGLASS_PANE_PINK.get(), BlocksPM.STAINED_SKYGLASS_PANE_PURPLE.get(), BlocksPM.STAINED_SKYGLASS_PANE_RED.get(), BlocksPM.STAINED_SKYGLASS_PANE_WHITE.get(), BlocksPM.STAINED_SKYGLASS_PANE_YELLOW.get());
        this.tag(BlockTagsPM.STORAGE_BLOCKS_HALLOWSTEEL).add(BlocksPM.HALLOWSTEEL_BLOCK.get());
        this.tag(BlockTagsPM.STORAGE_BLOCKS_HEXIUM).add(BlocksPM.HEXIUM_BLOCK.get());
        this.tag(BlockTagsPM.STORAGE_BLOCKS_PRIMALITE).add(BlocksPM.PRIMALITE_BLOCK.get());
        this.tag(BlockTagsPM.SUNWOOD_LOGS).add(BlocksPM.SUNWOOD_LOG.get(), BlocksPM.STRIPPED_SUNWOOD_LOG.get(), BlocksPM.SUNWOOD_WOOD.get(), BlocksPM.STRIPPED_SUNWOOD_WOOD.get());
        this.tag(BlockTagsPM.TREEFOLK_FERTILIZE_EXEMPT).add(Blocks.GRASS_BLOCK, Blocks.ROOTED_DIRT, Blocks.GRASS, Blocks.FERN);
        
        this.tag(BlockTagsPM.MAY_PLACE_SUNWOOD_SAPLINGS).addTag(BlockTags.DIRT).add(Blocks.FARMLAND);
        this.tag(BlockTagsPM.MAY_PLACE_MOONWOOD_SAPLINGS).addTag(BlockTags.DIRT).add(Blocks.FARMLAND);
        this.tag(BlockTagsPM.MAY_PLACE_HALLOWOOD_SAPLINGS).addTag(BlockTags.DIRT).add(Blocks.FARMLAND);
    }
}

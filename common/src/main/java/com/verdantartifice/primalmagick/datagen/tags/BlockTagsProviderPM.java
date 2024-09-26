package com.verdantartifice.primalmagick.datagen.tags;

import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;
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
        super(packOutput, lookupProvider, Constants.MOD_ID, helper);
    }

    @Override
    public String getName() {
        return "Primal Magick Block Tags";
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        // Add entries to vanilla tags
        this.tag(BlockTags.BEACON_BASE_BLOCKS).addTag(BlockTagsPM.STORAGE_BLOCKS_PRIMALITE).addTag(BlockTagsPM.STORAGE_BLOCKS_HEXIUM).addTag(BlockTagsPM.STORAGE_BLOCKS_HALLOWSTEEL);
        this.tag(BlockTags.CROPS).add(BlockRegistration.HYRDOMELON_STEM.get());
        this.tag(BlockTags.CRYSTAL_SOUND_BLOCKS).add(BlockRegistration.DAMAGED_BUDDING_AMETHYST_BLOCK.get(), BlockRegistration.CHIPPED_BUDDING_AMETHYST_BLOCK.get(), BlockRegistration.FLAWED_BUDDING_AMETHYST_BLOCK.get(), BlockRegistration.DAMAGED_BUDDING_DIAMOND_BLOCK.get(), BlockRegistration.CHIPPED_BUDDING_DIAMOND_BLOCK.get(), BlockRegistration.FLAWED_BUDDING_DIAMOND_BLOCK.get(), BlockRegistration.DAMAGED_BUDDING_EMERALD_BLOCK.get(), BlockRegistration.CHIPPED_BUDDING_EMERALD_BLOCK.get(), BlockRegistration.FLAWED_BUDDING_EMERALD_BLOCK.get(), BlockRegistration.DAMAGED_BUDDING_QUARTZ_BLOCK.get(), BlockRegistration.CHIPPED_BUDDING_QUARTZ_BLOCK.get(), BlockRegistration.FLAWED_BUDDING_QUARTZ_BLOCK.get());
        this.tag(BlockTags.LOGS_THAT_BURN).addTag(BlockTagsPM.MOONWOOD_LOGS).addTag(BlockTagsPM.SUNWOOD_LOGS).addTag(BlockTagsPM.HALLOWOOD_LOGS);
        this.tag(BlockTags.LEAVES).add(BlockRegistration.MOONWOOD_LEAVES.get(), BlockRegistration.SUNWOOD_LEAVES.get(), BlockRegistration.HALLOWOOD_LEAVES.get());
        this.tag(BlockTags.PIGLIN_REPELLENTS).add(BlockRegistration.SPIRIT_LANTERN.get(), BlockRegistration.SOUL_GLOW_FIELD.get());
        this.tag(BlockTags.PLANKS).add(BlockRegistration.MOONWOOD_PLANKS.get(), BlockRegistration.SUNWOOD_PLANKS.get(), BlockRegistration.HALLOWOOD_PLANKS.get());
        this.tag(BlockTags.SAPLINGS).add(BlockRegistration.MOONWOOD_SAPLING.get(), BlockRegistration.SUNWOOD_SAPLING.get(), BlockRegistration.HALLOWOOD_SAPLING.get());
        this.tag(BlockTags.TALL_FLOWERS).add(BlockRegistration.BLOOD_ROSE.get(), BlockRegistration.EMBERFLOWER.get());
        this.tag(BlockTags.WALLS).add(BlockRegistration.MARBLE_WALL.get(), BlockRegistration.MARBLE_BRICK_WALL.get(), BlockRegistration.MARBLE_ENCHANTED_WALL.get(), BlockRegistration.MARBLE_ENCHANTED_BRICK_WALL.get(), BlockRegistration.MARBLE_SMOKED_WALL.get(), BlockRegistration.MARBLE_SMOKED_BRICK_WALL.get(), BlockRegistration.MARBLE_HALLOWED_WALL.get(), BlockRegistration.MARBLE_HALLOWED_BRICK_WALL.get());
        this.tag(BlockTags.WOODEN_SLABS).add(BlockRegistration.MOONWOOD_SLAB.get(), BlockRegistration.SUNWOOD_SLAB.get(), BlockRegistration.HALLOWOOD_SLAB.get());
        this.tag(BlockTags.WOODEN_STAIRS).add(BlockRegistration.MOONWOOD_STAIRS.get(), BlockRegistration.SUNWOOD_STAIRS.get(), BlockRegistration.HALLOWOOD_STAIRS.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(BlockRegistration.SUNWOOD_PILLAR.get(), BlockRegistration.MOONWOOD_PILLAR.get(), BlockRegistration.HALLOWOOD_PILLAR.get(), BlockRegistration.ARCANE_WORKBENCH.get(), BlockRegistration.WOOD_TABLE.get(), BlockRegistration.ANALYSIS_TABLE.get(), BlockRegistration.WAND_INSCRIPTION_TABLE.get(), BlockRegistration.RESEARCH_TABLE.get(), BlockRegistration.RITUAL_LECTERN.get(), BlockRegistration.RUNECARVING_TABLE.get(), BlockRegistration.CELESTIAL_HARP.get(), BlockRegistration.HYDROMELON.get(), BlockRegistration.ATTACHED_HYDROMELON_STEM.get(), BlockRegistration.HYRDOMELON_STEM.get(), BlockRegistration.SCRIBE_TABLE.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BlockRegistration.MARBLE_RAW.get(), BlockRegistration.MARBLE_SLAB.get(), BlockRegistration.MARBLE_STAIRS.get(), BlockRegistration.MARBLE_WALL.get(), BlockRegistration.MARBLE_BRICKS.get(), BlockRegistration.MARBLE_BRICK_SLAB.get(), BlockRegistration.MARBLE_BRICK_STAIRS.get(), BlockRegistration.MARBLE_BRICK_WALL.get(), BlockRegistration.MARBLE_PILLAR.get(), BlockRegistration.MARBLE_CHISELED.get(), BlockRegistration.MARBLE_RUNED.get(), BlockRegistration.MARBLE_TILES.get(), BlockRegistration.MARBLE_ENCHANTED.get(), BlockRegistration.MARBLE_ENCHANTED_SLAB.get(), BlockRegistration.MARBLE_ENCHANTED_STAIRS.get(), BlockRegistration.MARBLE_ENCHANTED_WALL.get(), BlockRegistration.MARBLE_ENCHANTED_BRICKS.get(), BlockRegistration.MARBLE_ENCHANTED_BRICK_SLAB.get(), BlockRegistration.MARBLE_ENCHANTED_BRICK_STAIRS.get(), BlockRegistration.MARBLE_ENCHANTED_BRICK_WALL.get(), BlockRegistration.MARBLE_ENCHANTED_PILLAR.get(), BlockRegistration.MARBLE_ENCHANTED_CHISELED.get(), BlockRegistration.MARBLE_ENCHANTED_RUNED.get(), BlockRegistration.MARBLE_SMOKED.get(), BlockRegistration.MARBLE_SMOKED_SLAB.get(), BlockRegistration.MARBLE_SMOKED_STAIRS.get(), BlockRegistration.MARBLE_SMOKED_WALL.get(), BlockRegistration.MARBLE_SMOKED_BRICKS.get(), BlockRegistration.MARBLE_SMOKED_BRICK_SLAB.get(), BlockRegistration.MARBLE_SMOKED_BRICK_STAIRS.get(), BlockRegistration.MARBLE_SMOKED_BRICK_WALL.get(), BlockRegistration.MARBLE_SMOKED_PILLAR.get(), BlockRegistration.MARBLE_SMOKED_CHISELED.get(), BlockRegistration.MARBLE_SMOKED_RUNED.get(), BlockRegistration.MARBLE_HALLOWED.get(), BlockRegistration.MARBLE_HALLOWED_SLAB.get(), BlockRegistration.MARBLE_HALLOWED_STAIRS.get(), BlockRegistration.MARBLE_HALLOWED_WALL.get(), BlockRegistration.MARBLE_HALLOWED_BRICKS.get(), BlockRegistration.MARBLE_HALLOWED_BRICK_SLAB.get(), BlockRegistration.MARBLE_HALLOWED_BRICK_STAIRS.get(), BlockRegistration.MARBLE_HALLOWED_BRICK_WALL.get(), BlockRegistration.MARBLE_HALLOWED_PILLAR.get(), BlockRegistration.MARBLE_HALLOWED_CHISELED.get(), BlockRegistration.MARBLE_HALLOWED_RUNED.get(), BlockRegistration.INFUSED_STONE_EARTH.get(), BlockRegistration.INFUSED_STONE_SEA.get(), BlockRegistration.INFUSED_STONE_SKY.get(), BlockRegistration.INFUSED_STONE_SUN.get(), BlockRegistration.INFUSED_STONE_MOON.get(), BlockRegistration.ARTIFICIAL_FONT_EARTH.get(), BlockRegistration.ARTIFICIAL_FONT_SEA.get(), BlockRegistration.ARTIFICIAL_FONT_SKY.get(), BlockRegistration.ARTIFICIAL_FONT_SUN.get(), BlockRegistration.ARTIFICIAL_FONT_MOON.get(), BlockRegistration.ARTIFICIAL_FONT_BLOOD.get(), BlockRegistration.ARTIFICIAL_FONT_INFERNAL.get(), BlockRegistration.ARTIFICIAL_FONT_VOID.get(), BlockRegistration.ARTIFICIAL_FONT_HALLOWED.get(), BlockRegistration.FORBIDDEN_FONT_EARTH.get(), BlockRegistration.FORBIDDEN_FONT_SEA.get(), BlockRegistration.FORBIDDEN_FONT_SKY.get(), BlockRegistration.FORBIDDEN_FONT_SUN.get(), BlockRegistration.FORBIDDEN_FONT_MOON.get(), BlockRegistration.FORBIDDEN_FONT_BLOOD.get(), BlockRegistration.FORBIDDEN_FONT_INFERNAL.get(), BlockRegistration.FORBIDDEN_FONT_VOID.get(), BlockRegistration.FORBIDDEN_FONT_HALLOWED.get(), BlockRegistration.HEAVENLY_FONT_EARTH.get(), BlockRegistration.HEAVENLY_FONT_SEA.get(), BlockRegistration.HEAVENLY_FONT_SKY.get(), BlockRegistration.HEAVENLY_FONT_SUN.get(), BlockRegistration.HEAVENLY_FONT_MOON.get(), BlockRegistration.HEAVENLY_FONT_BLOOD.get(), BlockRegistration.HEAVENLY_FONT_INFERNAL.get(), BlockRegistration.HEAVENLY_FONT_VOID.get(), BlockRegistration.HEAVENLY_FONT_HALLOWED.get(), BlockRegistration.WAND_ASSEMBLY_TABLE.get(), BlockRegistration.ESSENCE_FURNACE.get(), BlockRegistration.CALCINATOR_BASIC.get(), BlockRegistration.CALCINATOR_ENCHANTED.get(), BlockRegistration.CALCINATOR_FORBIDDEN.get(), BlockRegistration.CALCINATOR_HEAVENLY.get(), BlockRegistration.SPELLCRAFTING_ALTAR.get(), BlockRegistration.WAND_CHARGER.get(), BlockRegistration.RITUAL_ALTAR.get(), BlockRegistration.OFFERING_PEDESTAL.get(), BlockRegistration.INCENSE_BRAZIER.get(), BlockRegistration.BLOODLETTER.get(), BlockRegistration.SOUL_ANVIL.get(), BlockRegistration.RUNESCRIBING_ALTAR_BASIC.get(), BlockRegistration.RUNESCRIBING_ALTAR_ENCHANTED.get(), BlockRegistration.RUNESCRIBING_ALTAR_FORBIDDEN.get(), BlockRegistration.RUNESCRIBING_ALTAR_HEAVENLY.get(), BlockRegistration.RUNIC_GRINDSTONE.get(), BlockRegistration.HONEY_EXTRACTOR.get(), BlockRegistration.PRIMALITE_GOLEM_CONTROLLER.get(), BlockRegistration.HEXIUM_GOLEM_CONTROLLER.get(), BlockRegistration.HALLOWSTEEL_GOLEM_CONTROLLER.get(), BlockRegistration.SANGUINE_CRUCIBLE.get(), BlockRegistration.CONCOCTER.get(), BlockRegistration.ENTROPY_SINK.get(), BlockRegistration.ROCK_SALT_ORE.get(), BlockRegistration.QUARTZ_ORE.get(), BlockRegistration.PRIMALITE_BLOCK.get(), BlockRegistration.HEXIUM_BLOCK.get(), BlockRegistration.HALLOWSTEEL_BLOCK.get(), BlockRegistration.IGNYX_BLOCK.get(), BlockRegistration.SALT_BLOCK.get(), BlockRegistration.ESSENCE_TRANSMUTER.get(), BlockRegistration.INFERNAL_FURNACE.get(), BlockRegistration.MANA_NEXUS.get(), BlockRegistration.MANA_SINGULARITY.get(), BlockRegistration.MANA_SINGULARITY_CREATIVE.get());
        this.tag(BlockTags.SWORD_EFFICIENT).add(BlockRegistration.HYDROMELON.get(), BlockRegistration.ATTACHED_HYDROMELON_STEM.get(), BlockRegistration.BLOOD_ROSE.get(), BlockRegistration.EMBERFLOWER.get());
        this.tag(BlockTags.NEEDS_STONE_TOOL).add(BlockRegistration.ROCK_SALT_ORE.get(), BlockRegistration.QUARTZ_ORE.get(), BlockRegistration.PRIMALITE_BLOCK.get(), BlockRegistration.SALT_BLOCK.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(BlockRegistration.HEXIUM_BLOCK.get(), BlockRegistration.SANGUINE_CRUCIBLE.get());
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(BlockRegistration.HALLOWSTEEL_BLOCK.get());
        this.tag(BlockTags.MAINTAINS_FARMLAND).add(BlockRegistration.ATTACHED_HYDROMELON_STEM.get(), BlockRegistration.HYRDOMELON_STEM.get());
        
        // Add entries to Forge tags
        this.tag(Tags.Blocks.ORE_RATES_DENSE).add(BlockRegistration.ROCK_SALT_ORE.get());
        this.tag(Tags.Blocks.ORE_RATES_SINGULAR).add(BlockRegistration.QUARTZ_ORE.get());
        this.tag(Tags.Blocks.ORES).addTag(BlockTagsForgeExt.ORES_ROCK_SALT);
        this.tag(Tags.Blocks.ORES_QUARTZ).add(BlockRegistration.QUARTZ_ORE.get());
        this.tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(BlockRegistration.QUARTZ_ORE.get(), BlockRegistration.ROCK_SALT_ORE.get());
        this.tag(Tags.Blocks.STORAGE_BLOCKS).add(BlockRegistration.IGNYX_BLOCK.get(), BlockRegistration.SALT_BLOCK.get()).addTag(BlockTagsPM.STORAGE_BLOCKS_PRIMALITE).addTag(BlockTagsPM.STORAGE_BLOCKS_HEXIUM).addTag(BlockTagsPM.STORAGE_BLOCKS_HALLOWSTEEL);
        
        this.tag(Tags.Blocks.GLASS_COLORLESS).add(BlockRegistration.SKYGLASS.get());
        this.tag(Tags.Blocks.GLASS_BLACK).add(BlockRegistration.STAINED_SKYGLASS_BLACK.get());
        this.tag(Tags.Blocks.GLASS_BLUE).add(BlockRegistration.STAINED_SKYGLASS_BLUE.get());
        this.tag(Tags.Blocks.GLASS_BROWN).add(BlockRegistration.STAINED_SKYGLASS_BROWN.get());
        this.tag(Tags.Blocks.GLASS_CYAN).add(BlockRegistration.STAINED_SKYGLASS_CYAN.get());
        this.tag(Tags.Blocks.GLASS_GRAY).add(BlockRegistration.STAINED_SKYGLASS_GRAY.get());
        this.tag(Tags.Blocks.GLASS_GREEN).add(BlockRegistration.STAINED_SKYGLASS_GREEN.get());
        this.tag(Tags.Blocks.GLASS_LIGHT_BLUE).add(BlockRegistration.STAINED_SKYGLASS_LIGHT_BLUE.get());
        this.tag(Tags.Blocks.GLASS_LIGHT_GRAY).add(BlockRegistration.STAINED_SKYGLASS_LIGHT_GRAY.get());
        this.tag(Tags.Blocks.GLASS_LIME).add(BlockRegistration.STAINED_SKYGLASS_LIME.get());
        this.tag(Tags.Blocks.GLASS_MAGENTA).add(BlockRegistration.STAINED_SKYGLASS_MAGENTA.get());
        this.tag(Tags.Blocks.GLASS_ORANGE).add(BlockRegistration.STAINED_SKYGLASS_ORANGE.get());
        this.tag(Tags.Blocks.GLASS_PINK).add(BlockRegistration.STAINED_SKYGLASS_PINK.get());
        this.tag(Tags.Blocks.GLASS_PURPLE).add(BlockRegistration.STAINED_SKYGLASS_PURPLE.get());
        this.tag(Tags.Blocks.GLASS_RED).add(BlockRegistration.STAINED_SKYGLASS_RED.get());
        this.tag(Tags.Blocks.GLASS_WHITE).add(BlockRegistration.STAINED_SKYGLASS_WHITE.get());
        this.tag(Tags.Blocks.GLASS_YELLOW).add(BlockRegistration.STAINED_SKYGLASS_YELLOW.get());
        this.tag(Tags.Blocks.STAINED_GLASS).addTag(BlockTagsPM.STAINED_SKYGLASS);
        
        this.tag(Tags.Blocks.GLASS_PANES_COLORLESS).add(BlockRegistration.SKYGLASS_PANE.get());
        this.tag(Tags.Blocks.GLASS_PANES_BLACK).add(BlockRegistration.STAINED_SKYGLASS_PANE_BLACK.get());
        this.tag(Tags.Blocks.GLASS_PANES_BLUE).add(BlockRegistration.STAINED_SKYGLASS_PANE_BLUE.get());
        this.tag(Tags.Blocks.GLASS_PANES_BROWN).add(BlockRegistration.STAINED_SKYGLASS_PANE_BROWN.get());
        this.tag(Tags.Blocks.GLASS_PANES_CYAN).add(BlockRegistration.STAINED_SKYGLASS_PANE_CYAN.get());
        this.tag(Tags.Blocks.GLASS_PANES_GRAY).add(BlockRegistration.STAINED_SKYGLASS_PANE_GRAY.get());
        this.tag(Tags.Blocks.GLASS_PANES_GREEN).add(BlockRegistration.STAINED_SKYGLASS_PANE_GREEN.get());
        this.tag(Tags.Blocks.GLASS_PANES_LIGHT_BLUE).add(BlockRegistration.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get());
        this.tag(Tags.Blocks.GLASS_PANES_LIGHT_GRAY).add(BlockRegistration.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get());
        this.tag(Tags.Blocks.GLASS_PANES_LIME).add(BlockRegistration.STAINED_SKYGLASS_PANE_LIME.get());
        this.tag(Tags.Blocks.GLASS_PANES_MAGENTA).add(BlockRegistration.STAINED_SKYGLASS_PANE_MAGENTA.get());
        this.tag(Tags.Blocks.GLASS_PANES_ORANGE).add(BlockRegistration.STAINED_SKYGLASS_PANE_ORANGE.get());
        this.tag(Tags.Blocks.GLASS_PANES_PINK).add(BlockRegistration.STAINED_SKYGLASS_PANE_PINK.get());
        this.tag(Tags.Blocks.GLASS_PANES_PURPLE).add(BlockRegistration.STAINED_SKYGLASS_PANE_PURPLE.get());
        this.tag(Tags.Blocks.GLASS_PANES_RED).add(BlockRegistration.STAINED_SKYGLASS_PANE_RED.get());
        this.tag(Tags.Blocks.GLASS_PANES_WHITE).add(BlockRegistration.STAINED_SKYGLASS_PANE_WHITE.get());
        this.tag(Tags.Blocks.GLASS_PANES_YELLOW).add(BlockRegistration.STAINED_SKYGLASS_PANE_YELLOW.get());
        this.tag(Tags.Blocks.STAINED_GLASS_PANES).addTag(BlockTagsPM.STAINED_SKYGLASS_PANES);
        
        // Add entries to Forge extension tags
        this.tag(BlockTagsForgeExt.ORES_ROCK_SALT).add(BlockRegistration.ROCK_SALT_ORE.get());
        this.tag(BlockTagsForgeExt.BUDDING).add(BlockRegistration.DAMAGED_BUDDING_AMETHYST_BLOCK.get(), BlockRegistration.CHIPPED_BUDDING_AMETHYST_BLOCK.get(), BlockRegistration.FLAWED_BUDDING_AMETHYST_BLOCK.get(), BlockRegistration.DAMAGED_BUDDING_DIAMOND_BLOCK.get(), BlockRegistration.CHIPPED_BUDDING_DIAMOND_BLOCK.get(), BlockRegistration.FLAWED_BUDDING_DIAMOND_BLOCK.get(), BlockRegistration.DAMAGED_BUDDING_EMERALD_BLOCK.get(), BlockRegistration.CHIPPED_BUDDING_EMERALD_BLOCK.get(), BlockRegistration.FLAWED_BUDDING_EMERALD_BLOCK.get(), BlockRegistration.DAMAGED_BUDDING_QUARTZ_BLOCK.get(), BlockRegistration.CHIPPED_BUDDING_QUARTZ_BLOCK.get(), BlockRegistration.FLAWED_BUDDING_QUARTZ_BLOCK.get());
        this.tag(BlockTagsForgeExt.FURNACES).add(Blocks.FURNACE);
        this.tag(BlockTagsForgeExt.MINEABLE_WITH_SHEARS).addTag(BlockTags.LEAVES).addTag(BlockTags.WOOL).add(Blocks.COBWEB, Blocks.SHORT_GRASS, Blocks.FERN, Blocks.DEAD_BUSH, Blocks.HANGING_ROOTS, Blocks.VINE, Blocks.TRIPWIRE);
        
        // Create custom tags
        this.tag(BlockTagsPM.BOUNTY_CROPS).addTag(BlockTags.CROPS).add(Blocks.NETHER_WART);
        this.tag(BlockTagsPM.COLORED_SHULKER_BOXES).add(Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX);
        this.tag(BlockTagsPM.CONCRETE).add(Blocks.BLACK_CONCRETE, Blocks.BLUE_CONCRETE, Blocks.BROWN_CONCRETE, Blocks.CYAN_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.GREEN_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE, Blocks.LIME_CONCRETE, Blocks.MAGENTA_CONCRETE, Blocks.ORANGE_CONCRETE, Blocks.PINK_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.RED_CONCRETE, Blocks.WHITE_CONCRETE, Blocks.YELLOW_CONCRETE);
        this.tag(BlockTagsPM.DEAD_CORAL_BLOCKS).add(Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.DEAD_TUBE_CORAL_BLOCK);
        this.tag(BlockTagsPM.DEAD_CORAL_PLANTS).add(Blocks.DEAD_BRAIN_CORAL, Blocks.DEAD_BUBBLE_CORAL, Blocks.DEAD_FIRE_CORAL, Blocks.DEAD_HORN_CORAL, Blocks.DEAD_TUBE_CORAL);
        this.tag(BlockTagsPM.DEAD_CORALS).addTag(BlockTagsPM.DEAD_CORAL_PLANTS).add(Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.DEAD_HORN_CORAL_FAN, Blocks.DEAD_TUBE_CORAL_FAN);
        this.tag(BlockTagsPM.ENCHANTING_TABLES).add(Blocks.ENCHANTING_TABLE).addOptional(ResourceLocation.fromNamespaceAndPath("quark", "matrix_enchanter"));
        this.tag(BlockTagsPM.HALLOWOOD_LOGS).add(BlockRegistration.HALLOWOOD_LOG.get(), BlockRegistration.STRIPPED_HALLOWOOD_LOG.get(), BlockRegistration.HALLOWOOD_WOOD.get(), BlockRegistration.STRIPPED_HALLOWOOD_WOOD.get());
        this.tag(BlockTagsPM.MOONWOOD_LOGS).add(BlockRegistration.MOONWOOD_LOG.get(), BlockRegistration.STRIPPED_MOONWOOD_LOG.get(), BlockRegistration.MOONWOOD_WOOD.get(), BlockRegistration.STRIPPED_MOONWOOD_WOOD.get());
        this.tag(BlockTagsPM.RITUAL_CANDLES).add(BlockRegistration.RITUAL_CANDLE_BLACK.get(), BlockRegistration.RITUAL_CANDLE_BLUE.get(), BlockRegistration.RITUAL_CANDLE_BROWN.get(), BlockRegistration.RITUAL_CANDLE_CYAN.get(), BlockRegistration.RITUAL_CANDLE_GRAY.get(), BlockRegistration.RITUAL_CANDLE_GREEN.get(), BlockRegistration.RITUAL_CANDLE_LIGHT_BLUE.get(), BlockRegistration.RITUAL_CANDLE_LIGHT_GRAY.get(), BlockRegistration.RITUAL_CANDLE_LIME.get(), BlockRegistration.RITUAL_CANDLE_MAGENTA.get(), BlockRegistration.RITUAL_CANDLE_ORANGE.get(), BlockRegistration.RITUAL_CANDLE_PINK.get(), BlockRegistration.RITUAL_CANDLE_PURPLE.get(), BlockRegistration.RITUAL_CANDLE_RED.get(), BlockRegistration.RITUAL_CANDLE_WHITE.get(), BlockRegistration.RITUAL_CANDLE_YELLOW.get());
        this.tag(BlockTagsPM.SHULKER_BOXES).addTag(BlockTagsPM.COLORED_SHULKER_BOXES).add(Blocks.SHULKER_BOX);
        this.tag(BlockTagsPM.SKYGLASS).add(BlockRegistration.SKYGLASS.get()).addTag(BlockTagsPM.STAINED_SKYGLASS);
        this.tag(BlockTagsPM.SKYGLASS_PANES).add(BlockRegistration.SKYGLASS_PANE.get()).addTag(BlockTagsPM.STAINED_SKYGLASS_PANES);
        this.tag(BlockTagsPM.STAINED_SKYGLASS).add(BlockRegistration.STAINED_SKYGLASS_BLACK.get(), BlockRegistration.STAINED_SKYGLASS_BLUE.get(), BlockRegistration.STAINED_SKYGLASS_BROWN.get(), BlockRegistration.STAINED_SKYGLASS_CYAN.get(), BlockRegistration.STAINED_SKYGLASS_GRAY.get(), BlockRegistration.STAINED_SKYGLASS_GREEN.get(), BlockRegistration.STAINED_SKYGLASS_LIGHT_BLUE.get(), BlockRegistration.STAINED_SKYGLASS_LIGHT_GRAY.get(), BlockRegistration.STAINED_SKYGLASS_LIME.get(), BlockRegistration.STAINED_SKYGLASS_MAGENTA.get(), BlockRegistration.STAINED_SKYGLASS_ORANGE.get(), BlockRegistration.STAINED_SKYGLASS_PINK.get(), BlockRegistration.STAINED_SKYGLASS_PURPLE.get(), BlockRegistration.STAINED_SKYGLASS_RED.get(), BlockRegistration.STAINED_SKYGLASS_WHITE.get(), BlockRegistration.STAINED_SKYGLASS_YELLOW.get());
        this.tag(BlockTagsPM.STAINED_SKYGLASS_PANES).add(BlockRegistration.STAINED_SKYGLASS_PANE_BLACK.get(), BlockRegistration.STAINED_SKYGLASS_PANE_BLUE.get(), BlockRegistration.STAINED_SKYGLASS_PANE_BROWN.get(), BlockRegistration.STAINED_SKYGLASS_PANE_CYAN.get(), BlockRegistration.STAINED_SKYGLASS_PANE_GRAY.get(), BlockRegistration.STAINED_SKYGLASS_PANE_GREEN.get(), BlockRegistration.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), BlockRegistration.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), BlockRegistration.STAINED_SKYGLASS_PANE_LIME.get(), BlockRegistration.STAINED_SKYGLASS_PANE_MAGENTA.get(), BlockRegistration.STAINED_SKYGLASS_PANE_ORANGE.get(), BlockRegistration.STAINED_SKYGLASS_PANE_PINK.get(), BlockRegistration.STAINED_SKYGLASS_PANE_PURPLE.get(), BlockRegistration.STAINED_SKYGLASS_PANE_RED.get(), BlockRegistration.STAINED_SKYGLASS_PANE_WHITE.get(), BlockRegistration.STAINED_SKYGLASS_PANE_YELLOW.get());
        this.tag(BlockTagsPM.STORAGE_BLOCKS_HALLOWSTEEL).add(BlockRegistration.HALLOWSTEEL_BLOCK.get());
        this.tag(BlockTagsPM.STORAGE_BLOCKS_HEXIUM).add(BlockRegistration.HEXIUM_BLOCK.get());
        this.tag(BlockTagsPM.STORAGE_BLOCKS_PRIMALITE).add(BlockRegistration.PRIMALITE_BLOCK.get());
        this.tag(BlockTagsPM.SUNWOOD_LOGS).add(BlockRegistration.SUNWOOD_LOG.get(), BlockRegistration.STRIPPED_SUNWOOD_LOG.get(), BlockRegistration.SUNWOOD_WOOD.get(), BlockRegistration.STRIPPED_SUNWOOD_WOOD.get());
        this.tag(BlockTagsPM.TREEFOLK_FERTILIZE_EXEMPT).add(Blocks.GRASS_BLOCK, Blocks.ROOTED_DIRT, Blocks.SHORT_GRASS, Blocks.FERN).addOptional(ResourceLocation.fromNamespaceAndPath("regions_unexplored", "alpha_grass_block"));
        
        this.tag(BlockTagsPM.MAY_PLACE_SUNWOOD_SAPLINGS).addTag(BlockTags.DIRT).add(Blocks.FARMLAND);
        this.tag(BlockTagsPM.MAY_PLACE_MOONWOOD_SAPLINGS).addTag(BlockTags.DIRT).add(Blocks.FARMLAND);
        this.tag(BlockTagsPM.MAY_PLACE_HALLOWOOD_SAPLINGS).addTag(BlockTags.DIRT).add(Blocks.FARMLAND);
    }
}

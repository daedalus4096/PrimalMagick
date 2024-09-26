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
        this.tag(BlockTags.CROPS).add(BlocksPM.get(BlocksPM.HYRDOMELON_STEM));
        this.tag(BlockTags.CRYSTAL_SOUND_BLOCKS).add(BlocksPM.get(BlocksPM.DAMAGED_BUDDING_AMETHYST_BLOCK), BlocksPM.get(BlocksPM.CHIPPED_BUDDING_AMETHYST_BLOCK), BlocksPM.get(BlocksPM.FLAWED_BUDDING_AMETHYST_BLOCK), BlocksPM.get(BlocksPM.DAMAGED_BUDDING_DIAMOND_BLOCK), BlocksPM.get(BlocksPM.CHIPPED_BUDDING_DIAMOND_BLOCK), BlocksPM.get(BlocksPM.FLAWED_BUDDING_DIAMOND_BLOCK), BlocksPM.get(BlocksPM.DAMAGED_BUDDING_EMERALD_BLOCK), BlocksPM.get(BlocksPM.CHIPPED_BUDDING_EMERALD_BLOCK), BlocksPM.get(BlocksPM.FLAWED_BUDDING_EMERALD_BLOCK), BlocksPM.get(BlocksPM.DAMAGED_BUDDING_QUARTZ_BLOCK), BlocksPM.get(BlocksPM.CHIPPED_BUDDING_QUARTZ_BLOCK), BlocksPM.get(BlocksPM.FLAWED_BUDDING_QUARTZ_BLOCK));
        this.tag(BlockTags.LOGS_THAT_BURN).addTag(BlockTagsPM.MOONWOOD_LOGS).addTag(BlockTagsPM.SUNWOOD_LOGS).addTag(BlockTagsPM.HALLOWOOD_LOGS);
        this.tag(BlockTags.LEAVES).add(BlocksPM.get(BlocksPM.MOONWOOD_LEAVES), BlocksPM.get(BlocksPM.SUNWOOD_LEAVES), BlocksPM.get(BlocksPM.HALLOWOOD_LEAVES));
        this.tag(BlockTags.PIGLIN_REPELLENTS).add(BlocksPM.get(BlocksPM.SPIRIT_LANTERN), BlocksPM.get(BlocksPM.SOUL_GLOW_FIELD));
        this.tag(BlockTags.PLANKS).add(BlocksPM.get(BlocksPM.MOONWOOD_PLANKS), BlocksPM.get(BlocksPM.SUNWOOD_PLANKS), BlocksPM.get(BlocksPM.HALLOWOOD_PLANKS));
        this.tag(BlockTags.SAPLINGS).add(BlocksPM.get(BlocksPM.MOONWOOD_SAPLING), BlocksPM.get(BlocksPM.SUNWOOD_SAPLING), BlocksPM.get(BlocksPM.HALLOWOOD_SAPLING));
        this.tag(BlockTags.TALL_FLOWERS).add(BlocksPM.get(BlocksPM.BLOOD_ROSE), BlocksPM.get(BlocksPM.EMBERFLOWER));
        this.tag(BlockTags.WALLS).add(BlocksPM.get(BlocksPM.MARBLE_WALL), BlocksPM.get(BlocksPM.MARBLE_BRICK_WALL), BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_WALL), BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL), BlocksPM.get(BlocksPM.MARBLE_SMOKED_WALL), BlocksPM.get(BlocksPM.MARBLE_SMOKED_BRICK_WALL), BlocksPM.get(BlocksPM.MARBLE_HALLOWED_WALL), BlocksPM.get(BlocksPM.MARBLE_HALLOWED_BRICK_WALL));
        this.tag(BlockTags.WOODEN_SLABS).add(BlocksPM.get(BlocksPM.MOONWOOD_SLAB), BlocksPM.get(BlocksPM.SUNWOOD_SLAB), BlocksPM.get(BlocksPM.HALLOWOOD_SLAB));
        this.tag(BlockTags.WOODEN_STAIRS).add(BlocksPM.get(BlocksPM.MOONWOOD_STAIRS), BlocksPM.get(BlocksPM.SUNWOOD_STAIRS), BlocksPM.get(BlocksPM.HALLOWOOD_STAIRS));
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(BlocksPM.get(BlocksPM.SUNWOOD_PILLAR), BlocksPM.get(BlocksPM.MOONWOOD_PILLAR), BlocksPM.get(BlocksPM.HALLOWOOD_PILLAR), BlocksPM.get(BlocksPM.ARCANE_WORKBENCH), BlocksPM.get(BlocksPM.WOOD_TABLE), BlocksPM.get(BlocksPM.ANALYSIS_TABLE), BlocksPM.get(BlocksPM.WAND_INSCRIPTION_TABLE), BlocksPM.get(BlocksPM.RESEARCH_TABLE), BlocksPM.get(BlocksPM.RITUAL_LECTERN), BlocksPM.get(BlocksPM.RUNECARVING_TABLE), BlocksPM.get(BlocksPM.CELESTIAL_HARP), BlocksPM.get(BlocksPM.HYDROMELON), BlocksPM.get(BlocksPM.ATTACHED_HYDROMELON_STEM), BlocksPM.get(BlocksPM.HYRDOMELON_STEM), BlocksPM.get(BlocksPM.SCRIBE_TABLE));
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BlocksPM.get(BlocksPM.MARBLE_RAW), BlocksPM.get(BlocksPM.MARBLE_SLAB), BlocksPM.get(BlocksPM.MARBLE_STAIRS), BlocksPM.get(BlocksPM.MARBLE_WALL), BlocksPM.get(BlocksPM.MARBLE_BRICKS), BlocksPM.get(BlocksPM.MARBLE_BRICK_SLAB), BlocksPM.get(BlocksPM.MARBLE_BRICK_STAIRS), BlocksPM.get(BlocksPM.MARBLE_BRICK_WALL), BlocksPM.get(BlocksPM.MARBLE_PILLAR), BlocksPM.get(BlocksPM.MARBLE_CHISELED), BlocksPM.get(BlocksPM.MARBLE_RUNED), BlocksPM.get(BlocksPM.MARBLE_TILES), BlocksPM.get(BlocksPM.MARBLE_ENCHANTED), BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_SLAB), BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_STAIRS), BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_WALL), BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_BRICKS), BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB), BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS), BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL), BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_PILLAR), BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_CHISELED), BlocksPM.get(BlocksPM.MARBLE_ENCHANTED_RUNED), BlocksPM.get(BlocksPM.MARBLE_SMOKED), BlocksPM.get(BlocksPM.MARBLE_SMOKED_SLAB), BlocksPM.get(BlocksPM.MARBLE_SMOKED_STAIRS), BlocksPM.get(BlocksPM.MARBLE_SMOKED_WALL), BlocksPM.get(BlocksPM.MARBLE_SMOKED_BRICKS), BlocksPM.get(BlocksPM.MARBLE_SMOKED_BRICK_SLAB), BlocksPM.get(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS), BlocksPM.get(BlocksPM.MARBLE_SMOKED_BRICK_WALL), BlocksPM.get(BlocksPM.MARBLE_SMOKED_PILLAR), BlocksPM.get(BlocksPM.MARBLE_SMOKED_CHISELED), BlocksPM.get(BlocksPM.MARBLE_SMOKED_RUNED), BlocksPM.get(BlocksPM.MARBLE_HALLOWED), BlocksPM.get(BlocksPM.MARBLE_HALLOWED_SLAB), BlocksPM.get(BlocksPM.MARBLE_HALLOWED_STAIRS), BlocksPM.get(BlocksPM.MARBLE_HALLOWED_WALL), BlocksPM.get(BlocksPM.MARBLE_HALLOWED_BRICKS), BlocksPM.get(BlocksPM.MARBLE_HALLOWED_BRICK_SLAB), BlocksPM.get(BlocksPM.MARBLE_HALLOWED_BRICK_STAIRS), BlocksPM.get(BlocksPM.MARBLE_HALLOWED_BRICK_WALL), BlocksPM.get(BlocksPM.MARBLE_HALLOWED_PILLAR), BlocksPM.get(BlocksPM.MARBLE_HALLOWED_CHISELED), BlocksPM.get(BlocksPM.MARBLE_HALLOWED_RUNED), BlocksPM.get(BlocksPM.INFUSED_STONE_EARTH), BlocksPM.get(BlocksPM.INFUSED_STONE_SEA), BlocksPM.get(BlocksPM.INFUSED_STONE_SKY), BlocksPM.get(BlocksPM.INFUSED_STONE_SUN), BlocksPM.get(BlocksPM.INFUSED_STONE_MOON), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_EARTH), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_SEA), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_SKY), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_SUN), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_MOON), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_BLOOD), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_INFERNAL), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_VOID), BlocksPM.get(BlocksPM.ARTIFICIAL_FONT_HALLOWED), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_EARTH), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_SEA), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_SKY), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_SUN), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_MOON), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_BLOOD), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_INFERNAL), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_VOID), BlocksPM.get(BlocksPM.FORBIDDEN_FONT_HALLOWED), BlocksPM.get(BlocksPM.HEAVENLY_FONT_EARTH), BlocksPM.get(BlocksPM.HEAVENLY_FONT_SEA), BlocksPM.get(BlocksPM.HEAVENLY_FONT_SKY), BlocksPM.get(BlocksPM.HEAVENLY_FONT_SUN), BlocksPM.get(BlocksPM.HEAVENLY_FONT_MOON), BlocksPM.get(BlocksPM.HEAVENLY_FONT_BLOOD), BlocksPM.get(BlocksPM.HEAVENLY_FONT_INFERNAL), BlocksPM.get(BlocksPM.HEAVENLY_FONT_VOID), BlocksPM.get(BlocksPM.HEAVENLY_FONT_HALLOWED), BlocksPM.get(BlocksPM.WAND_ASSEMBLY_TABLE), BlocksPM.get(BlocksPM.ESSENCE_FURNACE), BlocksPM.get(BlocksPM.CALCINATOR_BASIC), BlocksPM.get(BlocksPM.CALCINATOR_ENCHANTED), BlocksPM.get(BlocksPM.CALCINATOR_FORBIDDEN), BlocksPM.get(BlocksPM.CALCINATOR_HEAVENLY), BlocksPM.get(BlocksPM.SPELLCRAFTING_ALTAR), BlocksPM.get(BlocksPM.WAND_CHARGER), BlocksPM.get(BlocksPM.RITUAL_ALTAR), BlocksPM.get(BlocksPM.OFFERING_PEDESTAL), BlocksPM.get(BlocksPM.INCENSE_BRAZIER), BlocksPM.get(BlocksPM.BLOODLETTER), BlocksPM.get(BlocksPM.SOUL_ANVIL), BlocksPM.get(BlocksPM.RUNESCRIBING_ALTAR_BASIC), BlocksPM.get(BlocksPM.RUNESCRIBING_ALTAR_ENCHANTED), BlocksPM.get(BlocksPM.RUNESCRIBING_ALTAR_FORBIDDEN), BlocksPM.get(BlocksPM.RUNESCRIBING_ALTAR_HEAVENLY), BlocksPM.get(BlocksPM.RUNIC_GRINDSTONE), BlocksPM.get(BlocksPM.HONEY_EXTRACTOR), BlocksPM.get(BlocksPM.PRIMALITE_GOLEM_CONTROLLER), BlocksPM.get(BlocksPM.HEXIUM_GOLEM_CONTROLLER), BlocksPM.get(BlocksPM.HALLOWSTEEL_GOLEM_CONTROLLER), BlocksPM.get(BlocksPM.SANGUINE_CRUCIBLE), BlocksPM.get(BlocksPM.CONCOCTER), BlocksPM.get(BlocksPM.ENTROPY_SINK), BlocksPM.get(BlocksPM.ROCK_SALT_ORE), BlocksPM.get(BlocksPM.QUARTZ_ORE), BlocksPM.get(BlocksPM.PRIMALITE_BLOCK), BlocksPM.get(BlocksPM.HEXIUM_BLOCK), BlocksPM.get(BlocksPM.HALLOWSTEEL_BLOCK), BlocksPM.get(BlocksPM.IGNYX_BLOCK), BlocksPM.get(BlocksPM.SALT_BLOCK), BlocksPM.get(BlocksPM.ESSENCE_TRANSMUTER), BlocksPM.get(BlocksPM.INFERNAL_FURNACE), BlocksPM.get(BlocksPM.MANA_NEXUS), BlocksPM.get(BlocksPM.MANA_SINGULARITY), BlocksPM.get(BlocksPM.MANA_SINGULARITY_CREATIVE));
        this.tag(BlockTags.SWORD_EFFICIENT).add(BlocksPM.get(BlocksPM.HYDROMELON), BlocksPM.get(BlocksPM.ATTACHED_HYDROMELON_STEM), BlocksPM.get(BlocksPM.BLOOD_ROSE), BlocksPM.get(BlocksPM.EMBERFLOWER));
        this.tag(BlockTags.NEEDS_STONE_TOOL).add(BlocksPM.get(BlocksPM.ROCK_SALT_ORE), BlocksPM.get(BlocksPM.QUARTZ_ORE), BlocksPM.get(BlocksPM.PRIMALITE_BLOCK), BlocksPM.get(BlocksPM.SALT_BLOCK));
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(BlocksPM.get(BlocksPM.HEXIUM_BLOCK), BlocksPM.get(BlocksPM.SANGUINE_CRUCIBLE));
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(BlocksPM.get(BlocksPM.HALLOWSTEEL_BLOCK));
        this.tag(BlockTags.MAINTAINS_FARMLAND).add(BlocksPM.get(BlocksPM.ATTACHED_HYDROMELON_STEM), BlocksPM.get(BlocksPM.HYRDOMELON_STEM));
        
        // Add entries to Forge tags
        this.tag(Tags.Blocks.ORE_RATES_DENSE).add(BlocksPM.get(BlocksPM.ROCK_SALT_ORE));
        this.tag(Tags.Blocks.ORE_RATES_SINGULAR).add(BlocksPM.get(BlocksPM.QUARTZ_ORE));
        this.tag(Tags.Blocks.ORES).addTag(BlockTagsForgeExt.ORES_ROCK_SALT);
        this.tag(Tags.Blocks.ORES_QUARTZ).add(BlocksPM.get(BlocksPM.QUARTZ_ORE));
        this.tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(BlocksPM.get(BlocksPM.QUARTZ_ORE), BlocksPM.get(BlocksPM.ROCK_SALT_ORE));
        this.tag(Tags.Blocks.STORAGE_BLOCKS).add(BlocksPM.get(BlocksPM.IGNYX_BLOCK), BlocksPM.get(BlocksPM.SALT_BLOCK)).addTag(BlockTagsPM.STORAGE_BLOCKS_PRIMALITE).addTag(BlockTagsPM.STORAGE_BLOCKS_HEXIUM).addTag(BlockTagsPM.STORAGE_BLOCKS_HALLOWSTEEL);
        
        this.tag(Tags.Blocks.GLASS_COLORLESS).add(BlocksPM.get(BlocksPM.SKYGLASS));
        this.tag(Tags.Blocks.GLASS_BLACK).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_BLACK));
        this.tag(Tags.Blocks.GLASS_BLUE).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_BLUE));
        this.tag(Tags.Blocks.GLASS_BROWN).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_BROWN));
        this.tag(Tags.Blocks.GLASS_CYAN).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_CYAN));
        this.tag(Tags.Blocks.GLASS_GRAY).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_GRAY));
        this.tag(Tags.Blocks.GLASS_GREEN).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_GREEN));
        this.tag(Tags.Blocks.GLASS_LIGHT_BLUE).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE));
        this.tag(Tags.Blocks.GLASS_LIGHT_GRAY).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY));
        this.tag(Tags.Blocks.GLASS_LIME).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_LIME));
        this.tag(Tags.Blocks.GLASS_MAGENTA).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_MAGENTA));
        this.tag(Tags.Blocks.GLASS_ORANGE).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_ORANGE));
        this.tag(Tags.Blocks.GLASS_PINK).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PINK));
        this.tag(Tags.Blocks.GLASS_PURPLE).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PURPLE));
        this.tag(Tags.Blocks.GLASS_RED).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_RED));
        this.tag(Tags.Blocks.GLASS_WHITE).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_WHITE));
        this.tag(Tags.Blocks.GLASS_YELLOW).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_YELLOW));
        this.tag(Tags.Blocks.STAINED_GLASS).addTag(BlockTagsPM.STAINED_SKYGLASS);
        
        this.tag(Tags.Blocks.GLASS_PANES_COLORLESS).add(BlocksPM.get(BlocksPM.SKYGLASS_PANE));
        this.tag(Tags.Blocks.GLASS_PANES_BLACK).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_BLACK));
        this.tag(Tags.Blocks.GLASS_PANES_BLUE).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_BLUE));
        this.tag(Tags.Blocks.GLASS_PANES_BROWN).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_BROWN));
        this.tag(Tags.Blocks.GLASS_PANES_CYAN).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_CYAN));
        this.tag(Tags.Blocks.GLASS_PANES_GRAY).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_GRAY));
        this.tag(Tags.Blocks.GLASS_PANES_GREEN).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_GREEN));
        this.tag(Tags.Blocks.GLASS_PANES_LIGHT_BLUE).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE));
        this.tag(Tags.Blocks.GLASS_PANES_LIGHT_GRAY).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY));
        this.tag(Tags.Blocks.GLASS_PANES_LIME).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_LIME));
        this.tag(Tags.Blocks.GLASS_PANES_MAGENTA).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA));
        this.tag(Tags.Blocks.GLASS_PANES_ORANGE).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_ORANGE));
        this.tag(Tags.Blocks.GLASS_PANES_PINK).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_PINK));
        this.tag(Tags.Blocks.GLASS_PANES_PURPLE).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_PURPLE));
        this.tag(Tags.Blocks.GLASS_PANES_RED).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_RED));
        this.tag(Tags.Blocks.GLASS_PANES_WHITE).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_WHITE));
        this.tag(Tags.Blocks.GLASS_PANES_YELLOW).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_YELLOW));
        this.tag(Tags.Blocks.STAINED_GLASS_PANES).addTag(BlockTagsPM.STAINED_SKYGLASS_PANES);
        
        // Add entries to Forge extension tags
        this.tag(BlockTagsForgeExt.ORES_ROCK_SALT).add(BlocksPM.get(BlocksPM.ROCK_SALT_ORE));
        this.tag(BlockTagsForgeExt.BUDDING).add(BlocksPM.get(BlocksPM.DAMAGED_BUDDING_AMETHYST_BLOCK), BlocksPM.get(BlocksPM.CHIPPED_BUDDING_AMETHYST_BLOCK), BlocksPM.get(BlocksPM.FLAWED_BUDDING_AMETHYST_BLOCK), BlocksPM.get(BlocksPM.DAMAGED_BUDDING_DIAMOND_BLOCK), BlocksPM.get(BlocksPM.CHIPPED_BUDDING_DIAMOND_BLOCK), BlocksPM.get(BlocksPM.FLAWED_BUDDING_DIAMOND_BLOCK), BlocksPM.get(BlocksPM.DAMAGED_BUDDING_EMERALD_BLOCK), BlocksPM.get(BlocksPM.CHIPPED_BUDDING_EMERALD_BLOCK), BlocksPM.get(BlocksPM.FLAWED_BUDDING_EMERALD_BLOCK), BlocksPM.get(BlocksPM.DAMAGED_BUDDING_QUARTZ_BLOCK), BlocksPM.get(BlocksPM.CHIPPED_BUDDING_QUARTZ_BLOCK), BlocksPM.get(BlocksPM.FLAWED_BUDDING_QUARTZ_BLOCK));
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
        this.tag(BlockTagsPM.HALLOWOOD_LOGS).add(BlocksPM.get(BlocksPM.HALLOWOOD_LOG), BlocksPM.get(BlocksPM.STRIPPED_HALLOWOOD_LOG), BlocksPM.get(BlocksPM.HALLOWOOD_WOOD), BlocksPM.get(BlocksPM.STRIPPED_HALLOWOOD_WOOD));
        this.tag(BlockTagsPM.MOONWOOD_LOGS).add(BlocksPM.get(BlocksPM.MOONWOOD_LOG), BlocksPM.get(BlocksPM.STRIPPED_MOONWOOD_LOG), BlocksPM.get(BlocksPM.MOONWOOD_WOOD), BlocksPM.get(BlocksPM.STRIPPED_MOONWOOD_WOOD));
        this.tag(BlockTagsPM.RITUAL_CANDLES).add(BlocksPM.get(BlocksPM.RITUAL_CANDLE_BLACK), BlocksPM.get(BlocksPM.RITUAL_CANDLE_BLUE), BlocksPM.get(BlocksPM.RITUAL_CANDLE_BROWN), BlocksPM.get(BlocksPM.RITUAL_CANDLE_CYAN), BlocksPM.get(BlocksPM.RITUAL_CANDLE_GRAY), BlocksPM.get(BlocksPM.RITUAL_CANDLE_GREEN), BlocksPM.get(BlocksPM.RITUAL_CANDLE_LIGHT_BLUE), BlocksPM.get(BlocksPM.RITUAL_CANDLE_LIGHT_GRAY), BlocksPM.get(BlocksPM.RITUAL_CANDLE_LIME), BlocksPM.get(BlocksPM.RITUAL_CANDLE_MAGENTA), BlocksPM.get(BlocksPM.RITUAL_CANDLE_ORANGE), BlocksPM.get(BlocksPM.RITUAL_CANDLE_PINK), BlocksPM.get(BlocksPM.RITUAL_CANDLE_PURPLE), BlocksPM.get(BlocksPM.RITUAL_CANDLE_RED), BlocksPM.get(BlocksPM.RITUAL_CANDLE_WHITE), BlocksPM.get(BlocksPM.RITUAL_CANDLE_YELLOW));
        this.tag(BlockTagsPM.SHULKER_BOXES).addTag(BlockTagsPM.COLORED_SHULKER_BOXES).add(Blocks.SHULKER_BOX);
        this.tag(BlockTagsPM.SKYGLASS).add(BlocksPM.get(BlocksPM.SKYGLASS)).addTag(BlockTagsPM.STAINED_SKYGLASS);
        this.tag(BlockTagsPM.SKYGLASS_PANES).add(BlocksPM.get(BlocksPM.SKYGLASS_PANE)).addTag(BlockTagsPM.STAINED_SKYGLASS_PANES);
        this.tag(BlockTagsPM.STAINED_SKYGLASS).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_BLACK), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_BLUE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_BROWN), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_CYAN), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_GRAY), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_GREEN), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_LIME), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_MAGENTA), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_ORANGE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PINK), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PURPLE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_RED), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_WHITE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_YELLOW));
        this.tag(BlockTagsPM.STAINED_SKYGLASS_PANES).add(BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_BLACK), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_BLUE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_BROWN), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_CYAN), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_GRAY), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_GREEN), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_LIME), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_ORANGE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_PINK), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_PURPLE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_RED), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_WHITE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_YELLOW));
        this.tag(BlockTagsPM.STORAGE_BLOCKS_HALLOWSTEEL).add(BlocksPM.get(BlocksPM.HALLOWSTEEL_BLOCK));
        this.tag(BlockTagsPM.STORAGE_BLOCKS_HEXIUM).add(BlocksPM.get(BlocksPM.HEXIUM_BLOCK));
        this.tag(BlockTagsPM.STORAGE_BLOCKS_PRIMALITE).add(BlocksPM.get(BlocksPM.PRIMALITE_BLOCK));
        this.tag(BlockTagsPM.SUNWOOD_LOGS).add(BlocksPM.get(BlocksPM.SUNWOOD_LOG), BlocksPM.get(BlocksPM.STRIPPED_SUNWOOD_LOG), BlocksPM.get(BlocksPM.SUNWOOD_WOOD), BlocksPM.get(BlocksPM.STRIPPED_SUNWOOD_WOOD));
        this.tag(BlockTagsPM.TREEFOLK_FERTILIZE_EXEMPT).add(Blocks.GRASS_BLOCK, Blocks.ROOTED_DIRT, Blocks.SHORT_GRASS, Blocks.FERN).addOptional(ResourceLocation.fromNamespaceAndPath("regions_unexplored", "alpha_grass_block"));
        
        this.tag(BlockTagsPM.MAY_PLACE_SUNWOOD_SAPLINGS).addTag(BlockTags.DIRT).add(Blocks.FARMLAND);
        this.tag(BlockTagsPM.MAY_PLACE_MOONWOOD_SAPLINGS).addTag(BlockTags.DIRT).add(Blocks.FARMLAND);
        this.tag(BlockTagsPM.MAY_PLACE_HALLOWOOD_SAPLINGS).addTag(BlockTags.DIRT).add(Blocks.FARMLAND);
    }
}

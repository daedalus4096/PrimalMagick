package com.verdantartifice.primalmagic.datagen;

import java.nio.file.Path;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.tags.BlockTagsForgeExt;
import com.verdantartifice.primalmagic.common.tags.BlockTagsPM;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;

/**
 * Data provider for all of the mod's block tags, both original tags and modifications to vanilla tags.
 * 
 * @author Daedalus4096
 */
public class BlockTagsProvider extends TagsProvider<Block> {
    @SuppressWarnings("deprecation")
    public BlockTagsProvider(DataGenerator generator) {
        super(generator, Registry.BLOCK);
    }

    @Override
    public String getName() {
        return "Primal Magic Block Tags";
    }

    @Override
    protected void registerTags() {
        // Add entries to vanilla tags
        this.getBuilder(BlockTags.LOGS).add(BlockTagsPM.MOONWOOD_LOGS, BlockTagsPM.SUNWOOD_LOGS);
        this.getBuilder(BlockTags.LEAVES).add(BlocksPM.MOONWOOD_LEAVES.get(), BlocksPM.SUNWOOD_LEAVES.get());
        this.getBuilder(BlockTags.PLANKS).add(BlocksPM.MOONWOOD_PLANKS.get(), BlocksPM.SUNWOOD_PLANKS.get());
        this.getBuilder(BlockTags.SAPLINGS).add(BlocksPM.MOONWOOD_SAPLING.get(), BlocksPM.SUNWOOD_SAPLING.get());
        this.getBuilder(BlockTags.WALLS).add(BlocksPM.MARBLE_WALL.get(), BlocksPM.MARBLE_BRICK_WALL.get(), BlocksPM.MARBLE_ENCHANTED_WALL.get(), BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.get(), BlocksPM.MARBLE_SMOKED_WALL.get(), BlocksPM.MARBLE_SMOKED_BRICK_WALL.get());
        this.getBuilder(BlockTags.WOODEN_SLABS).add(BlocksPM.MOONWOOD_SLAB.get(), BlocksPM.SUNWOOD_SLAB.get());
        this.getBuilder(BlockTags.WOODEN_STAIRS).add(BlocksPM.MOONWOOD_STAIRS.get(), BlocksPM.SUNWOOD_STAIRS.get());
        
        // Add entries to Forge tags
        this.getBuilder(Tags.Blocks.STORAGE_BLOCKS).add(BlockTagsForgeExt.STORAGE_BLOCKS_PRIMALITE, BlockTagsForgeExt.STORAGE_BLOCKS_HEXIUM, BlockTagsForgeExt.STORAGE_BLOCKS_HALLOWSTEEL);
        
        this.getBuilder(Tags.Blocks.GLASS_COLORLESS).add(BlocksPM.SKYGLASS.get());
        this.getBuilder(Tags.Blocks.GLASS_BLACK).add(BlocksPM.STAINED_SKYGLASS_BLACK.get());
        this.getBuilder(Tags.Blocks.GLASS_BLUE).add(BlocksPM.STAINED_SKYGLASS_BLUE.get());
        this.getBuilder(Tags.Blocks.GLASS_BROWN).add(BlocksPM.STAINED_SKYGLASS_BROWN.get());
        this.getBuilder(Tags.Blocks.GLASS_CYAN).add(BlocksPM.STAINED_SKYGLASS_CYAN.get());
        this.getBuilder(Tags.Blocks.GLASS_GRAY).add(BlocksPM.STAINED_SKYGLASS_GRAY.get());
        this.getBuilder(Tags.Blocks.GLASS_GREEN).add(BlocksPM.STAINED_SKYGLASS_GREEN.get());
        this.getBuilder(Tags.Blocks.GLASS_LIGHT_BLUE).add(BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE.get());
        this.getBuilder(Tags.Blocks.GLASS_LIGHT_GRAY).add(BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY.get());
        this.getBuilder(Tags.Blocks.GLASS_LIME).add(BlocksPM.STAINED_SKYGLASS_LIME.get());
        this.getBuilder(Tags.Blocks.GLASS_MAGENTA).add(BlocksPM.STAINED_SKYGLASS_MAGENTA.get());
        this.getBuilder(Tags.Blocks.GLASS_ORANGE).add(BlocksPM.STAINED_SKYGLASS_ORANGE.get());
        this.getBuilder(Tags.Blocks.GLASS_PINK).add(BlocksPM.STAINED_SKYGLASS_PINK.get());
        this.getBuilder(Tags.Blocks.GLASS_PURPLE).add(BlocksPM.STAINED_SKYGLASS_PURPLE.get());
        this.getBuilder(Tags.Blocks.GLASS_RED).add(BlocksPM.STAINED_SKYGLASS_RED.get());
        this.getBuilder(Tags.Blocks.GLASS_WHITE).add(BlocksPM.STAINED_SKYGLASS_WHITE.get());
        this.getBuilder(Tags.Blocks.GLASS_YELLOW).add(BlocksPM.STAINED_SKYGLASS_YELLOW.get());
        this.getBuilder(Tags.Blocks.STAINED_GLASS).add(BlockTagsPM.STAINED_SKYGLASS);
        
        this.getBuilder(Tags.Blocks.GLASS_PANES_COLORLESS).add(BlocksPM.SKYGLASS_PANE.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_BLACK).add(BlocksPM.STAINED_SKYGLASS_PANE_BLACK.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_BLUE).add(BlocksPM.STAINED_SKYGLASS_PANE_BLUE.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_BROWN).add(BlocksPM.STAINED_SKYGLASS_PANE_BROWN.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_CYAN).add(BlocksPM.STAINED_SKYGLASS_PANE_CYAN.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_GRAY).add(BlocksPM.STAINED_SKYGLASS_PANE_GRAY.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_GREEN).add(BlocksPM.STAINED_SKYGLASS_PANE_GREEN.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_LIGHT_BLUE).add(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_LIGHT_GRAY).add(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_LIME).add(BlocksPM.STAINED_SKYGLASS_PANE_LIME.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_MAGENTA).add(BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_ORANGE).add(BlocksPM.STAINED_SKYGLASS_PANE_ORANGE.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_PINK).add(BlocksPM.STAINED_SKYGLASS_PANE_PINK.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_PURPLE).add(BlocksPM.STAINED_SKYGLASS_PANE_PURPLE.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_RED).add(BlocksPM.STAINED_SKYGLASS_PANE_RED.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_WHITE).add(BlocksPM.STAINED_SKYGLASS_PANE_WHITE.get());
        this.getBuilder(Tags.Blocks.GLASS_PANES_YELLOW).add(BlocksPM.STAINED_SKYGLASS_PANE_YELLOW.get());
        this.getBuilder(Tags.Blocks.STAINED_GLASS_PANES).add(BlockTagsPM.STAINED_SKYGLASS_PANES);
        
        // Add entries to Forge extension tags
        this.getBuilder(BlockTagsForgeExt.STORAGE_BLOCKS_HALLOWSTEEL).add(BlocksPM.HALLOWSTEEL_BLOCK.get());
        this.getBuilder(BlockTagsForgeExt.STORAGE_BLOCKS_HEXIUM).add(BlocksPM.HEXIUM_BLOCK.get());
        this.getBuilder(BlockTagsForgeExt.STORAGE_BLOCKS_PRIMALITE).add(BlocksPM.PRIMALITE_BLOCK.get());
        
        // Create custom tags
        this.getBuilder(BlockTagsPM.COLORED_SHULKER_BOXES).add(Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX);
        this.getBuilder(BlockTagsPM.CONCRETE).add(Blocks.BLACK_CONCRETE, Blocks.BLUE_CONCRETE, Blocks.BROWN_CONCRETE, Blocks.CYAN_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.GREEN_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE, Blocks.LIME_CONCRETE, Blocks.MAGENTA_CONCRETE, Blocks.ORANGE_CONCRETE, Blocks.PINK_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.RED_CONCRETE, Blocks.WHITE_CONCRETE, Blocks.YELLOW_CONCRETE);
        this.getBuilder(BlockTagsPM.DEAD_CORAL_BLOCKS).add(Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.DEAD_TUBE_CORAL_BLOCK);
        this.getBuilder(BlockTagsPM.DEAD_CORAL_PLANTS).add(Blocks.DEAD_BRAIN_CORAL, Blocks.DEAD_BUBBLE_CORAL, Blocks.DEAD_FIRE_CORAL, Blocks.DEAD_HORN_CORAL, Blocks.DEAD_TUBE_CORAL);
        this.getBuilder(BlockTagsPM.DEAD_CORALS).add(BlockTagsPM.DEAD_CORAL_PLANTS).add(Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.DEAD_HORN_CORAL_FAN, Blocks.DEAD_TUBE_CORAL_FAN);
        this.getBuilder(BlockTagsPM.MOONWOOD_LOGS).add(BlocksPM.MOONWOOD_LOG.get(), BlocksPM.STRIPPED_MOONWOOD_LOG.get(), BlocksPM.MOONWOOD_WOOD.get(), BlocksPM.STRIPPED_MOONWOOD_WOOD.get());
        this.getBuilder(BlockTagsPM.SHULKER_BOXES).add(BlockTagsPM.COLORED_SHULKER_BOXES).add(Blocks.SHULKER_BOX);
        this.getBuilder(BlockTagsPM.SKYGLASS).add(BlocksPM.SKYGLASS.get()).add(BlockTagsPM.STAINED_SKYGLASS);
        this.getBuilder(BlockTagsPM.SKYGLASS_PANES).add(BlocksPM.SKYGLASS_PANE.get()).add(BlockTagsPM.STAINED_SKYGLASS_PANES);
        this.getBuilder(BlockTagsPM.STAINED_SKYGLASS).add(BlocksPM.STAINED_SKYGLASS_BLACK.get(), BlocksPM.STAINED_SKYGLASS_BLUE.get(), BlocksPM.STAINED_SKYGLASS_BROWN.get(), BlocksPM.STAINED_SKYGLASS_CYAN.get(), BlocksPM.STAINED_SKYGLASS_GRAY.get(), BlocksPM.STAINED_SKYGLASS_GREEN.get(), BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), BlocksPM.STAINED_SKYGLASS_LIME.get(), BlocksPM.STAINED_SKYGLASS_MAGENTA.get(), BlocksPM.STAINED_SKYGLASS_ORANGE.get(), BlocksPM.STAINED_SKYGLASS_PINK.get(), BlocksPM.STAINED_SKYGLASS_PURPLE.get(), BlocksPM.STAINED_SKYGLASS_RED.get(), BlocksPM.STAINED_SKYGLASS_WHITE.get(), BlocksPM.STAINED_SKYGLASS_YELLOW.get());
        this.getBuilder(BlockTagsPM.STAINED_SKYGLASS_PANES).add(BlocksPM.STAINED_SKYGLASS_PANE_BLACK.get(), BlocksPM.STAINED_SKYGLASS_PANE_BLUE.get(), BlocksPM.STAINED_SKYGLASS_PANE_BROWN.get(), BlocksPM.STAINED_SKYGLASS_PANE_CYAN.get(), BlocksPM.STAINED_SKYGLASS_PANE_GRAY.get(), BlocksPM.STAINED_SKYGLASS_PANE_GREEN.get(), BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), BlocksPM.STAINED_SKYGLASS_PANE_LIME.get(), BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), BlocksPM.STAINED_SKYGLASS_PANE_ORANGE.get(), BlocksPM.STAINED_SKYGLASS_PANE_PINK.get(), BlocksPM.STAINED_SKYGLASS_PANE_PURPLE.get(), BlocksPM.STAINED_SKYGLASS_PANE_RED.get(), BlocksPM.STAINED_SKYGLASS_PANE_WHITE.get(), BlocksPM.STAINED_SKYGLASS_PANE_YELLOW.get());
        this.getBuilder(BlockTagsPM.SUNWOOD_LOGS).add(BlocksPM.SUNWOOD_LOG.get(), BlocksPM.STRIPPED_SUNWOOD_LOG.get(), BlocksPM.SUNWOOD_WOOD.get(), BlocksPM.STRIPPED_SUNWOOD_WOOD.get());
    }

    @Override
    protected void setCollection(TagCollection<Block> colectionIn) {
        // Do nothing
    }

    @Override
    protected Path makePath(ResourceLocation id) {
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/blocks/" + id.getPath() + ".json");
    }
}

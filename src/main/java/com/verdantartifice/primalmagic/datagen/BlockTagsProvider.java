package com.verdantartifice.primalmagic.datagen;

import java.nio.file.Path;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.tags.BlockTagsPM;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

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
        this.getBuilder(BlockTags.LOGS).add(BlockTagsPM.SUNWOOD_LOGS);
        this.getBuilder(BlockTags.LEAVES).add(BlocksPM.SUNWOOD_LEAVES);
        this.getBuilder(BlockTags.SAPLINGS).add(BlocksPM.SUNWOOD_SAPLING);
        this.getBuilder(BlockTags.WALLS).add(BlocksPM.MARBLE_WALL, BlocksPM.MARBLE_BRICK_WALL, BlocksPM.MARBLE_ENCHANTED_WALL, BlocksPM.MARBLE_ENCHANTED_BRICK_WALL, BlocksPM.MARBLE_SMOKED_WALL, BlocksPM.MARBLE_SMOKED_BRICK_WALL);
        
        this.getBuilder(BlockTagsPM.COLORED_SHULKER_BOXES).add(Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX);
        this.getBuilder(BlockTagsPM.CONCRETE).add(Blocks.BLACK_CONCRETE, Blocks.BLUE_CONCRETE, Blocks.BROWN_CONCRETE, Blocks.CYAN_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.GREEN_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE, Blocks.LIME_CONCRETE, Blocks.MAGENTA_CONCRETE, Blocks.ORANGE_CONCRETE, Blocks.PINK_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.RED_CONCRETE, Blocks.WHITE_CONCRETE, Blocks.YELLOW_CONCRETE);
        this.getBuilder(BlockTagsPM.DEAD_CORAL_BLOCKS).add(Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.DEAD_TUBE_CORAL_BLOCK);
        this.getBuilder(BlockTagsPM.DEAD_CORAL_PLANTS).add(Blocks.DEAD_BRAIN_CORAL, Blocks.DEAD_BUBBLE_CORAL, Blocks.DEAD_FIRE_CORAL, Blocks.DEAD_HORN_CORAL, Blocks.DEAD_TUBE_CORAL);
        this.getBuilder(BlockTagsPM.DEAD_CORALS).add(BlockTagsPM.DEAD_CORAL_PLANTS).add(Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.DEAD_HORN_CORAL_FAN, Blocks.DEAD_TUBE_CORAL_FAN);
        this.getBuilder(BlockTagsPM.SHULKER_BOXES).add(BlockTagsPM.COLORED_SHULKER_BOXES).add(Blocks.SHULKER_BOX);
        this.getBuilder(BlockTagsPM.SUNWOOD_LOGS).add(BlocksPM.SUNWOOD_LOG, BlocksPM.STRIPPED_SUNWOOD_LOG, BlocksPM.SUNWOOD_WOOD, BlocksPM.STRIPPED_SUNWOOD_WOOD);
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

package com.verdantartifice.primalmagick.datagen.models;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class BlockFamiliesPM {
    private static final Map<Block, BlockFamily> MAP = new HashMap<>();

    // Specify block families
    public static final BlockFamily MARBLE_ENCHANTED = familyBuilder(BlocksPM.MARBLE_ENCHANTED.get())
            .slab(BlocksPM.MARBLE_ENCHANTED_SLAB.get())
            .stairs(BlocksPM.MARBLE_ENCHANTED_STAIRS.get())
            .wall(BlocksPM.MARBLE_ENCHANTED_WALL.get())
            .chiseled(BlocksPM.MARBLE_ENCHANTED_CHISELED.get())
            .getFamily();
    public static final BlockFamily MARBLE_ENCHANTED_BRICKS = familyBuilder(BlocksPM.MARBLE_ENCHANTED_BRICKS.get())
            .slab(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB.get())
            .stairs(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS.get())
            .wall(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.get())
            .getFamily();
    public static final BlockFamily MARBLE_SMOKED = familyBuilder(BlocksPM.MARBLE_SMOKED.get())
            .slab(BlocksPM.MARBLE_SMOKED_SLAB.get())
            .stairs(BlocksPM.MARBLE_SMOKED_STAIRS.get())
            .wall(BlocksPM.MARBLE_SMOKED_WALL.get())
            .chiseled(BlocksPM.MARBLE_SMOKED_CHISELED.get())
            .getFamily();
    public static final BlockFamily MARBLE_SMOKED_BRICKS = familyBuilder(BlocksPM.MARBLE_SMOKED_BRICKS.get())
            .slab(BlocksPM.MARBLE_SMOKED_BRICK_SLAB.get())
            .stairs(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS.get())
            .wall(BlocksPM.MARBLE_SMOKED_BRICK_WALL.get())
            .getFamily();
    public static final BlockFamily MARBLE_HALLOWED = familyBuilder(BlocksPM.MARBLE_HALLOWED.get())
            .slab(BlocksPM.MARBLE_HALLOWED_SLAB.get())
            .stairs(BlocksPM.MARBLE_HALLOWED_STAIRS.get())
            .wall(BlocksPM.MARBLE_HALLOWED_WALL.get())
            .chiseled(BlocksPM.MARBLE_HALLOWED_CHISELED.get())
            .getFamily();
    public static final BlockFamily MARBLE_HALLOWED_BRICKS = familyBuilder(BlocksPM.MARBLE_HALLOWED_BRICKS.get())
            .slab(BlocksPM.MARBLE_HALLOWED_BRICK_SLAB.get())
            .stairs(BlocksPM.MARBLE_HALLOWED_BRICK_STAIRS.get())
            .wall(BlocksPM.MARBLE_HALLOWED_BRICK_WALL.get())
            .getFamily();

    private static BlockFamily.Builder familyBuilder(Block pBaseBlock) {
        BlockFamily.Builder builder = new BlockFamily.Builder(pBaseBlock);
        BlockFamily blockFamily = MAP.put(pBaseBlock, builder.getFamily());
        if (blockFamily != null) {
            throw new IllegalStateException("Duplicate family definition for " + BuiltInRegistries.BLOCK.getKey(pBaseBlock));
        } else {
            return builder;
        }
    }

    public static Stream<BlockFamily> getAllFamilies() {
        return MAP.values().stream();
    }
}

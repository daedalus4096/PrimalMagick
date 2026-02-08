package com.verdantartifice.primalmagick.datagen.models;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class BlockFamiliesPM {
    private static final Map<Block, BlockFamily> STANDARD = new HashMap<>();
    private static final Map<Block, BlockFamily> PHASING = new HashMap<>();

    // Specify block families
    public static final BlockFamily MARBLE = standardFamily(BlocksPM.MARBLE.get())
            .slab(BlocksPM.MARBLE_SLAB.get())
            .stairs(BlocksPM.MARBLE_STAIRS.get())
            .wall(BlocksPM.MARBLE_WALL.get())
            .chiseled(BlocksPM.MARBLE_CHISELED.get())
            .getFamily();
    public static final BlockFamily MARBLE_BRICKS = standardFamily(BlocksPM.MARBLE_BRICKS.get())
            .slab(BlocksPM.MARBLE_BRICK_SLAB.get())
            .stairs(BlocksPM.MARBLE_BRICK_STAIRS.get())
            .wall(BlocksPM.MARBLE_BRICK_WALL.get())
            .getFamily();
    public static final BlockFamily MARBLE_ENCHANTED = standardFamily(BlocksPM.MARBLE_ENCHANTED.get())
            .slab(BlocksPM.MARBLE_ENCHANTED_SLAB.get())
            .stairs(BlocksPM.MARBLE_ENCHANTED_STAIRS.get())
            .wall(BlocksPM.MARBLE_ENCHANTED_WALL.get())
            .chiseled(BlocksPM.MARBLE_ENCHANTED_CHISELED.get())
            .getFamily();
    public static final BlockFamily MARBLE_ENCHANTED_BRICKS = standardFamily(BlocksPM.MARBLE_ENCHANTED_BRICKS.get())
            .slab(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB.get())
            .stairs(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS.get())
            .wall(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.get())
            .getFamily();
    public static final BlockFamily MARBLE_SMOKED = standardFamily(BlocksPM.MARBLE_SMOKED.get())
            .slab(BlocksPM.MARBLE_SMOKED_SLAB.get())
            .stairs(BlocksPM.MARBLE_SMOKED_STAIRS.get())
            .wall(BlocksPM.MARBLE_SMOKED_WALL.get())
            .chiseled(BlocksPM.MARBLE_SMOKED_CHISELED.get())
            .getFamily();
    public static final BlockFamily MARBLE_SMOKED_BRICKS = standardFamily(BlocksPM.MARBLE_SMOKED_BRICKS.get())
            .slab(BlocksPM.MARBLE_SMOKED_BRICK_SLAB.get())
            .stairs(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS.get())
            .wall(BlocksPM.MARBLE_SMOKED_BRICK_WALL.get())
            .getFamily();
    public static final BlockFamily MARBLE_HALLOWED = standardFamily(BlocksPM.MARBLE_HALLOWED.get())
            .slab(BlocksPM.MARBLE_HALLOWED_SLAB.get())
            .stairs(BlocksPM.MARBLE_HALLOWED_STAIRS.get())
            .wall(BlocksPM.MARBLE_HALLOWED_WALL.get())
            .chiseled(BlocksPM.MARBLE_HALLOWED_CHISELED.get())
            .getFamily();
    public static final BlockFamily MARBLE_HALLOWED_BRICKS = standardFamily(BlocksPM.MARBLE_HALLOWED_BRICKS.get())
            .slab(BlocksPM.MARBLE_HALLOWED_BRICK_SLAB.get())
            .stairs(BlocksPM.MARBLE_HALLOWED_BRICK_STAIRS.get())
            .wall(BlocksPM.MARBLE_HALLOWED_BRICK_WALL.get())
            .getFamily();
    public static final BlockFamily SUNWOOD_PLANKS = phasingFamily(BlocksPM.SUNWOOD_PLANKS.get())
            .slab(BlocksPM.SUNWOOD_SLAB.get())
            .stairs(BlocksPM.SUNWOOD_STAIRS.get())
            .getFamily();
    public static final BlockFamily MOONWOOD_PLANKS = phasingFamily(BlocksPM.MOONWOOD_PLANKS.get())
            .slab(BlocksPM.MOONWOOD_SLAB.get())
            .stairs(BlocksPM.MOONWOOD_STAIRS.get())
            .getFamily();
    public static final BlockFamily HALLOWOOD_PLANKS = standardFamily(BlocksPM.HALLOWOOD_PLANKS.get())
            .slab(BlocksPM.HALLOWOOD_SLAB.get())
            .stairs(BlocksPM.HALLOWOOD_STAIRS.get())
            .getFamily();

    private static BlockFamily.Builder standardFamily(Block pBaseBlock) {
        return register(pBaseBlock, STANDARD);
    }

    private static BlockFamily.Builder phasingFamily(Block pBaseBlock) {
        return register(pBaseBlock, PHASING);
    }

    private static BlockFamily.Builder register(Block baseBlock, Map<Block, BlockFamily> map) {
        BlockFamily.Builder builder = new BlockFamily.Builder(baseBlock);
        BlockFamily blockFamily = map.put(baseBlock, builder.getFamily());
        if (blockFamily != null) {
            throw new IllegalStateException("Duplicate family definition for " + BuiltInRegistries.BLOCK.getKey(baseBlock));
        } else {
            return builder;
        }
    }

    public static Stream<BlockFamily> getStandardFamilies() {
        return STANDARD.values().stream();
    }

    public static Stream<BlockFamily> getPhasingFamilies() {
        return PHASING.values().stream();
    }
}

package com.verdantartifice.primalmagick.datagen.models;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

public class ModelConnectionSets {
    public static final ModelConnectionSet CUBE = new ModelConnectionSet("cube",
            List.of(ModelConnections.ZERO, ModelConnections.ONE, ModelConnections.TWO_ANGLE, ModelConnections.TWO_LINE,
                    ModelConnections.THREE_ANGLE, ModelConnections.THREE_T1, ModelConnections.THREE_T2,
                    ModelConnections.FOUR_ANGLE, ModelConnections.FOUR_CROSS),
            (block, variants) -> MultiVariantGenerator.dispatch(block).with(
                    PropertyDispatchPM.initial(BlockStateProperties.DOWN, BlockStateProperties.EAST, BlockStateProperties.NORTH, BlockStateProperties.SOUTH, BlockStateProperties.UP, BlockStateProperties.WEST)
                            .select(false, false, false, false, false, false, variants.get(ModelConnections.ZERO))
                            .select(false, false, false, false, true, false, variants.get(ModelConnections.ONE))
                            .select(true, false, false, false, false, false, variants.get(ModelConnections.ONE).with(BlockModelGenerators.X_ROT_180))
                            .select(false, false, true, false, false, false, variants.get(ModelConnections.ONE).with(BlockModelGenerators.X_ROT_90))
                            .select(false, false, false, true, false, false, variants.get(ModelConnections.ONE).with(BlockModelGenerators.X_ROT_270))
                            .select(false, false, false, false, false, true, variants.get(ModelConnections.ONE).with(BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_270)))
                            .select(false, true, false, false, false, false, variants.get(ModelConnections.ONE).with(BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90)))
            ));
}

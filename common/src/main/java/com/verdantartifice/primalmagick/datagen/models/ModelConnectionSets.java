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
                            .select(false, true, false, false, true, false, variants.get(ModelConnections.TWO_ANGLE))
                            .select(false, false, false, false, true, true, variants.get(ModelConnections.TWO_ANGLE).with(BlockModelGenerators.Y_ROT_180))
                            .select(true, true, false, false, false, false, variants.get(ModelConnections.TWO_ANGLE).with(BlockModelGenerators.X_ROT_180))
                            .select(true, false, false, false, false, true, variants.get(ModelConnections.TWO_ANGLE).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_180)))
                            .select(false, false, true, false, true, false, variants.get(ModelConnections.TWO_ANGLE).with(BlockModelGenerators.Y_ROT_270))
                            .select(false, false, false, true, true, false, variants.get(ModelConnections.TWO_ANGLE).with(BlockModelGenerators.Y_ROT_90))
                            .select(true, false, true, false, false, false, variants.get(ModelConnections.TWO_ANGLE).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_270)))
                            .select(true, false, false, true, false, false, variants.get(ModelConnections.TWO_ANGLE).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_90)))
                            .select(false, true, true, false, false, false, variants.get(ModelConnections.TWO_ANGLE).with(BlockModelGenerators.X_ROT_90))
                            .select(false, true, false, true, false, false, variants.get(ModelConnections.TWO_ANGLE).with(BlockModelGenerators.X_ROT_270))
                            .select(false, false, true, false, false, true, variants.get(ModelConnections.TWO_ANGLE).with(BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_270)))
                            .select(false, false, false, true, false, true, variants.get(ModelConnections.TWO_ANGLE).with(BlockModelGenerators.X_ROT_270.then(BlockModelGenerators.Y_ROT_90)))
                            .select(true, false, false, false, true, false, variants.get(ModelConnections.TWO_LINE))
                            .select(false, true, false, false, false, true, variants.get(ModelConnections.TWO_LINE).with(BlockModelGenerators.X_ROT_270.then(BlockModelGenerators.Y_ROT_90)))
                            .select(false, false, true, true, false, false, variants.get(ModelConnections.TWO_LINE).with(BlockModelGenerators.X_ROT_270))
                            .select(true, false, false, false, true, true, variants.get(ModelConnections.THREE_T1))
                            .select(true, false, false, true, true, false, variants.get(ModelConnections.THREE_T1).with(BlockModelGenerators.Y_ROT_270))
                            .select(true, true, false, false, true, false, variants.get(ModelConnections.THREE_T1).with(BlockModelGenerators.Y_ROT_180))
                            .select(true, false, true, false, true, false, variants.get(ModelConnections.THREE_T1).with(BlockModelGenerators.Y_ROT_90))
                            .select(false, false, true, true, false, true, variants.get(ModelConnections.THREE_T1).with(BlockModelGenerators.X_ROT_270))
                            .select(false, true, true, true, false, false, variants.get(ModelConnections.THREE_T1).with(BlockModelGenerators.X_ROT_270.then(BlockModelGenerators.Y_ROT_180)))
                            .select(false, true, false, false, true, true, variants.get(ModelConnections.THREE_T2))
                            .select(false, true, false, true, false, true, variants.get(ModelConnections.THREE_T2).with(BlockModelGenerators.X_ROT_270))
                            .select(true, true, false, false, false, true, variants.get(ModelConnections.THREE_T2).with(BlockModelGenerators.X_ROT_180))
                            .select(false, true, true, false, false, true, variants.get(ModelConnections.THREE_T2).with(BlockModelGenerators.X_ROT_90))
                            .select(false, false, true, true, true, false, variants.get(ModelConnections.THREE_T2).with(BlockModelGenerators.Y_ROT_90))
                            .select(true, false, true, true, false, false, variants.get(ModelConnections.THREE_T2).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_90)))
                            .select(false, true, false, true, true, false, variants.get(ModelConnections.THREE_ANGLE))
                            .select(false, false, false, true, true, true, variants.get(ModelConnections.THREE_ANGLE).with(BlockModelGenerators.Y_ROT_90))
                            .select(false, true, true, false, true, false, variants.get(ModelConnections.THREE_ANGLE).with(BlockModelGenerators.X_ROT_90))
                            .select(true, true, true, false, false, false, variants.get(ModelConnections.THREE_ANGLE).with(BlockModelGenerators.X_ROT_180))
                            .select(true, true, false, true, false, false, variants.get(ModelConnections.THREE_ANGLE).with(BlockModelGenerators.X_ROT_270))
                            .select(true, false, false, true, false, true, variants.get(ModelConnections.THREE_ANGLE).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_180)))
                            .select(true, false, true, false, false, true, variants.get(ModelConnections.THREE_ANGLE).with(BlockModelGenerators.X_ROT_270.then(BlockModelGenerators.Y_ROT_180)))
                            .select(false, false, true, false, true, true, variants.get(ModelConnections.THREE_ANGLE).with(BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_270)))
                            .select(true, true, false, false, true, true, variants.get(ModelConnections.FOUR_CROSS))
                            .select(false, true, true, true, false, true, variants.get(ModelConnections.FOUR_CROSS).with(BlockModelGenerators.X_ROT_90))
                            .select(true, false, true, true, true, false, variants.get(ModelConnections.FOUR_CROSS).with(BlockModelGenerators.Y_ROT_90))
                            .select(true, false, true, false, true, true, variants.get(ModelConnections.FOUR_ANGLE))
                            .select(true, false, true, true, false, true, variants.get(ModelConnections.FOUR_ANGLE).with(BlockModelGenerators.X_ROT_90))
                            .select(false, false, true, true, true, true, variants.get(ModelConnections.FOUR_ANGLE).with(BlockModelGenerators.X_ROT_270))
                            .select(true, false, false, true, true, true, variants.get(ModelConnections.FOUR_ANGLE).with(BlockModelGenerators.Y_ROT_270))
                            .select(true, true, true, false, true, false, variants.get(ModelConnections.FOUR_ANGLE).with(BlockModelGenerators.Y_ROT_90))
                            .select(true, true, false, true, true, false, variants.get(ModelConnections.FOUR_ANGLE).with(BlockModelGenerators.Y_ROT_180))
                            .select(false, true, true, true, true, false, variants.get(ModelConnections.FOUR_ANGLE).with(BlockModelGenerators.X_ROT_270.then(BlockModelGenerators.Y_ROT_180)))
                            .select(true, true, true, true, false, false, variants.get(ModelConnections.FOUR_ANGLE).with(BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_180)))
                            .select(false, true, false, true, true, true, variants.get(ModelConnections.FOUR_ANGLE).with(BlockModelGenerators.X_ROT_270.then(BlockModelGenerators.Y_ROT_270)))
                            .select(true, true, false, true, false, true, variants.get(ModelConnections.FOUR_ANGLE).with(BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_270)))
                            .select(false, true, true, false, true, true, variants.get(ModelConnections.FOUR_ANGLE).with(BlockModelGenerators.X_ROT_270.then(BlockModelGenerators.Y_ROT_90)))
                            .select(true, true, true, false, false, true, variants.get(ModelConnections.FOUR_ANGLE).with(BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90)))
                            .select(true, true, true, true, false, true, variants.get(ModelConnections.FOUR_CROSS))
                            .select(true, true, false, true, true, true, variants.get(ModelConnections.FOUR_CROSS))
                            .select(true, false, true, true, true, true, variants.get(ModelConnections.FOUR_CROSS))
                            .select(true, true, true, true, true, false, variants.get(ModelConnections.FOUR_CROSS))
                            .select(true, true, true, false, true, true, variants.get(ModelConnections.FOUR_CROSS))
                            .select(false, true, true, true, true, true, variants.get(ModelConnections.FOUR_CROSS))
                            .select(true, true, true, true, true, true, variants.get(ModelConnections.FOUR_CROSS))
            ));
}

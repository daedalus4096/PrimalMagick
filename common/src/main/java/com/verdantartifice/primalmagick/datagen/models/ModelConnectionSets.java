package com.verdantartifice.primalmagick.datagen.models;

import com.verdantartifice.primalmagick.common.blocks.misc.SkyglassPaneBlock;
import com.verdantartifice.primalmagick.common.blockstates.properties.SkyglassPaneSide;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

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

    public static final ModelConnectionSet PANE = new ModelConnectionSet("pane",
            List.of(ModelConnections.PANE_NOSIDE_ALT_U, ModelConnections.PANE_NOSIDE_ALT_UD, ModelConnections.PANE_NOSIDE_ALT_UNCONNECTED,
                    ModelConnections.PANE_NOSIDE_U, ModelConnections.PANE_NOSIDE_UD, ModelConnections.PANE_NOSIDE_UNCONNECTED,
                    ModelConnections.PANE_SIDE_ALT_LR, ModelConnections.PANE_SIDE_ALT_U, ModelConnections.PANE_SIDE_ALT_UD,
                    ModelConnections.PANE_SIDE_ALT_UDLR, ModelConnections.PANE_SIDE_ALT_ULR, ModelConnections.PANE_SIDE_ALT_UNCONNECTED,
                    ModelConnections.PANE_SIDE_LR, ModelConnections.PANE_SIDE_U, ModelConnections.PANE_SIDE_UD,
                    ModelConnections.PANE_SIDE_UDLR, ModelConnections.PANE_SIDE_ULR, ModelConnections.PANE_SIDE_UNCONNECTED,
                    ModelConnections.PANE_POST),
            (block, variants) -> MultiPartGenerator.multiPart(block)
                    // central, always-on post
                    .with(variants.get(ModelConnections.PANE_POST))
                    // northern connections
                    .with(paneCondition(SkyglassPaneBlock.NORTH, SkyglassPaneSide.OTHER, false, false), variants.get(ModelConnections.PANE_SIDE_UNCONNECTED))
                    .with(paneCondition(SkyglassPaneBlock.NORTH, SkyglassPaneSide.OTHER, false, true), variants.get(ModelConnections.PANE_SIDE_U).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_180)))
                    .with(paneCondition(SkyglassPaneBlock.NORTH, SkyglassPaneSide.OTHER, true, false), variants.get(ModelConnections.PANE_SIDE_U))
                    .with(paneCondition(SkyglassPaneBlock.NORTH, SkyglassPaneSide.OTHER, true, true), variants.get(ModelConnections.PANE_SIDE_UD))
                    .with(paneCondition(SkyglassPaneBlock.NORTH, SkyglassPaneSide.GLASS, false, false), variants.get(ModelConnections.PANE_SIDE_LR))
                    .with(paneCondition(SkyglassPaneBlock.NORTH, SkyglassPaneSide.GLASS, false, true), variants.get(ModelConnections.PANE_SIDE_ULR).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_180)))
                    .with(paneCondition(SkyglassPaneBlock.NORTH, SkyglassPaneSide.GLASS, true, false), variants.get(ModelConnections.PANE_SIDE_ULR))
                    .with(paneCondition(SkyglassPaneBlock.NORTH, SkyglassPaneSide.GLASS, true, true), variants.get(ModelConnections.PANE_SIDE_UDLR))
                    .with(paneCondition(SkyglassPaneBlock.NORTH, SkyglassPaneSide.NONE, false, false), variants.get(ModelConnections.PANE_NOSIDE_UNCONNECTED))
                    .with(paneCondition(SkyglassPaneBlock.NORTH, SkyglassPaneSide.NONE, false, true), variants.get(ModelConnections.PANE_NOSIDE_U).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_180)))
                    .with(paneCondition(SkyglassPaneBlock.NORTH, SkyglassPaneSide.NONE, true, false), variants.get(ModelConnections.PANE_NOSIDE_U))
                    .with(paneCondition(SkyglassPaneBlock.NORTH, SkyglassPaneSide.NONE, true, true), variants.get(ModelConnections.PANE_NOSIDE_UD))
                    // eastern connections
                    .with(paneCondition(SkyglassPaneBlock.EAST, SkyglassPaneSide.OTHER, false, false), variants.get(ModelConnections.PANE_SIDE_UNCONNECTED).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.EAST, SkyglassPaneSide.OTHER, false, true), variants.get(ModelConnections.PANE_SIDE_U).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_270)))
                    .with(paneCondition(SkyglassPaneBlock.EAST, SkyglassPaneSide.OTHER, true, false), variants.get(ModelConnections.PANE_SIDE_U).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.EAST, SkyglassPaneSide.OTHER, true, true), variants.get(ModelConnections.PANE_SIDE_UD).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.EAST, SkyglassPaneSide.GLASS, false, false), variants.get(ModelConnections.PANE_SIDE_LR).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.EAST, SkyglassPaneSide.GLASS, false, true), variants.get(ModelConnections.PANE_SIDE_ULR).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_270)))
                    .with(paneCondition(SkyglassPaneBlock.EAST, SkyglassPaneSide.GLASS, true, false), variants.get(ModelConnections.PANE_SIDE_ULR).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.EAST, SkyglassPaneSide.GLASS, true, true), variants.get(ModelConnections.PANE_SIDE_UDLR).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.EAST, SkyglassPaneSide.NONE, false, false), variants.get(ModelConnections.PANE_NOSIDE_UNCONNECTED).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.EAST, SkyglassPaneSide.NONE, false, true), variants.get(ModelConnections.PANE_NOSIDE_U).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_270)))
                    .with(paneCondition(SkyglassPaneBlock.EAST, SkyglassPaneSide.NONE, true, false), variants.get(ModelConnections.PANE_NOSIDE_U).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.EAST, SkyglassPaneSide.NONE, true, true), variants.get(ModelConnections.PANE_NOSIDE_UD).with(BlockModelGenerators.Y_ROT_90))
                    // southern connections
                    .with(paneCondition(SkyglassPaneBlock.SOUTH, SkyglassPaneSide.OTHER, false, false), variants.get(ModelConnections.PANE_SIDE_ALT_UNCONNECTED))
                    .with(paneCondition(SkyglassPaneBlock.SOUTH, SkyglassPaneSide.OTHER, false, true), variants.get(ModelConnections.PANE_SIDE_ALT_U).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_180)))
                    .with(paneCondition(SkyglassPaneBlock.SOUTH, SkyglassPaneSide.OTHER, true, false), variants.get(ModelConnections.PANE_SIDE_ALT_U))
                    .with(paneCondition(SkyglassPaneBlock.SOUTH, SkyglassPaneSide.OTHER, true, true), variants.get(ModelConnections.PANE_SIDE_ALT_UD))
                    .with(paneCondition(SkyglassPaneBlock.SOUTH, SkyglassPaneSide.GLASS, false, false), variants.get(ModelConnections.PANE_SIDE_ALT_LR))
                    .with(paneCondition(SkyglassPaneBlock.SOUTH, SkyglassPaneSide.GLASS, false, true), variants.get(ModelConnections.PANE_SIDE_ALT_ULR).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_180)))
                    .with(paneCondition(SkyglassPaneBlock.SOUTH, SkyglassPaneSide.GLASS, true, false), variants.get(ModelConnections.PANE_SIDE_ALT_ULR))
                    .with(paneCondition(SkyglassPaneBlock.SOUTH, SkyglassPaneSide.GLASS, true, true), variants.get(ModelConnections.PANE_SIDE_ALT_UDLR))
                    .with(paneCondition(SkyglassPaneBlock.SOUTH, SkyglassPaneSide.NONE, false, false), variants.get(ModelConnections.PANE_NOSIDE_ALT_UNCONNECTED))
                    .with(paneCondition(SkyglassPaneBlock.SOUTH, SkyglassPaneSide.NONE, false, true), variants.get(ModelConnections.PANE_NOSIDE_ALT_U).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_180)))
                    .with(paneCondition(SkyglassPaneBlock.SOUTH, SkyglassPaneSide.NONE, true, false), variants.get(ModelConnections.PANE_NOSIDE_ALT_U))
                    .with(paneCondition(SkyglassPaneBlock.SOUTH, SkyglassPaneSide.NONE, true, true), variants.get(ModelConnections.PANE_NOSIDE_ALT_UD))
                    // western connections
                    .with(paneCondition(SkyglassPaneBlock.WEST, SkyglassPaneSide.OTHER, false, false), variants.get(ModelConnections.PANE_SIDE_ALT_UNCONNECTED).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.WEST, SkyglassPaneSide.OTHER, false, true), variants.get(ModelConnections.PANE_SIDE_ALT_U).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_270)))
                    .with(paneCondition(SkyglassPaneBlock.WEST, SkyglassPaneSide.OTHER, true, false), variants.get(ModelConnections.PANE_SIDE_ALT_U).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.WEST, SkyglassPaneSide.OTHER, true, true), variants.get(ModelConnections.PANE_SIDE_ALT_UD).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.WEST, SkyglassPaneSide.GLASS, false, false), variants.get(ModelConnections.PANE_SIDE_ALT_LR).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.WEST, SkyglassPaneSide.GLASS, false, true), variants.get(ModelConnections.PANE_SIDE_ALT_ULR).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_270)))
                    .with(paneCondition(SkyglassPaneBlock.WEST, SkyglassPaneSide.GLASS, true, false), variants.get(ModelConnections.PANE_SIDE_ALT_ULR).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.WEST, SkyglassPaneSide.GLASS, true, true), variants.get(ModelConnections.PANE_SIDE_ALT_UDLR).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.WEST, SkyglassPaneSide.NONE, false, false), variants.get(ModelConnections.PANE_NOSIDE_ALT_UNCONNECTED).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.WEST, SkyglassPaneSide.NONE, false, true), variants.get(ModelConnections.PANE_NOSIDE_ALT_U).with(BlockModelGenerators.X_ROT_180.then(BlockModelGenerators.Y_ROT_270)))
                    .with(paneCondition(SkyglassPaneBlock.WEST, SkyglassPaneSide.NONE, true, false), variants.get(ModelConnections.PANE_NOSIDE_ALT_U).with(BlockModelGenerators.Y_ROT_90))
                    .with(paneCondition(SkyglassPaneBlock.WEST, SkyglassPaneSide.NONE, true, true), variants.get(ModelConnections.PANE_NOSIDE_ALT_UD).with(BlockModelGenerators.Y_ROT_90))
    );

    private static ConditionBuilder paneCondition(EnumProperty<SkyglassPaneSide> faceProperty, SkyglassPaneSide faceValue, boolean upGlass, boolean downGlass) {
        ConditionBuilder retVal = BlockModelGenerators.condition();

        // Add primary facing term
        retVal.term(faceProperty, faceValue);

        // Add up facing term
        if (upGlass) {
            retVal.term(SkyglassPaneBlock.UP, SkyglassPaneSide.GLASS);
        } else {
            retVal.negatedTerm(SkyglassPaneBlock.UP, SkyglassPaneSide.GLASS);
        }

        // Add down facing term
        if (downGlass) {
            retVal.term(SkyglassPaneBlock.DOWN, SkyglassPaneSide.GLASS);
        } else {
            retVal.negatedTerm(SkyglassPaneBlock.DOWN, SkyglassPaneSide.GLASS);
        }

        return retVal;
    }
}

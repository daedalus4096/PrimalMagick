package com.verdantartifice.primalmagick.datagen.models;

import com.verdantartifice.primalmagick.common.blocks.trees.IPhasingBlock;
import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Arrays;

public class PhasingWoodProvider {
    private final PhasingTextureMapping logMapping;
    private final BlockModelGenerators blockModelGenerators;

    public PhasingWoodProvider(PhasingTextureMapping logMapping, BlockModelGenerators blockModelGenerators) {
        this.logMapping = logMapping;
        this.blockModelGenerators = blockModelGenerators;
    }

    public PhasingWoodProvider wood(Block logBlock) {
        var propertyDispatch = PropertyDispatch.initial(BlockStateProperties.AXIS, IPhasingBlock.PHASE);
        PhasingTextureMapping woodMapping = this.logMapping.copyAndUpdate(TextureSlot.SIDE, TextureSlot.END);
        Arrays.stream(TimePhase.values()).forEach(phase -> {
            Identifier modelId = Services.MODEL_TEMPLATES.extend(ModelTemplates.CUBE_COLUMN)
                    .createWithSuffix(logBlock, "_" + phase, woodMapping.resolve(phase), this.blockModelGenerators.modelOutput);
            MultiVariant variant = BlockModelGenerators.plainVariant(modelId);
            propertyDispatch
                    .select(Direction.Axis.Y, phase, variant)
                    .select(Direction.Axis.Z, phase, variant.with(BlockModelGenerators.X_ROT_90))
                    .select(Direction.Axis.X, phase, variant.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_90));
            if (phase == TimePhase.FULL) {
                this.blockModelGenerators.registerSimpleItemModel(logBlock, modelId);
            }
        });
        this.blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(logBlock).with(propertyDispatch));
        return this;
    }

    public PhasingWoodProvider logWithHorizontal(Block logBlock) {
        var propertyDispatch = PropertyDispatch.initial(BlockStateProperties.AXIS, IPhasingBlock.PHASE);
        Arrays.stream(TimePhase.values()).forEach(phase -> {
            Identifier verticalModelId = Services.MODEL_TEMPLATES.extend(ModelTemplates.CUBE_COLUMN)
                    .createWithSuffix(logBlock, "_" + phase, this.logMapping.resolve(phase), this.blockModelGenerators.modelOutput);
            Identifier horizontalModelId = Services.MODEL_TEMPLATES.extend(ModelTemplates.CUBE_COLUMN_HORIZONTAL)
                    .createWithSuffix(logBlock, "_" + phase, this.logMapping.resolve(phase), this.blockModelGenerators.modelOutput);
            MultiVariant verticalVariant = BlockModelGenerators.plainVariant(verticalModelId);
            MultiVariant horizontalVariant = BlockModelGenerators.plainVariant(horizontalModelId);
            propertyDispatch
                    .select(Direction.Axis.Y, phase, verticalVariant)
                    .select(Direction.Axis.Z, phase, horizontalVariant.with(BlockModelGenerators.X_ROT_90))
                    .select(Direction.Axis.X, phase, horizontalVariant.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_90));
            if (phase == TimePhase.FULL) {
                this.blockModelGenerators.registerSimpleItemModel(logBlock, verticalModelId);
            }
        });
        this.blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(logBlock).with(propertyDispatch));
        return this;
    }
}

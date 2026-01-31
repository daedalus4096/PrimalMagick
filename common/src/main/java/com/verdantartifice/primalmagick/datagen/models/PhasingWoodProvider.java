package com.verdantartifice.primalmagick.datagen.models;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;

public class PhasingWoodProvider {
    private static final Identifier SOLID_RENDER_TYPE = Identifier.withDefaultNamespace("solid");
    private static final Identifier TRANSLUCENT_RENDER_TYPE = Identifier.withDefaultNamespace("translucent");

    private final PhasingTextureMapping logMapping;
    private final BlockModelGenerators blockModelGenerators;

    public PhasingWoodProvider(PhasingTextureMapping logMapping, BlockModelGenerators blockModelGenerators) {
        this.logMapping = logMapping;
        this.blockModelGenerators = blockModelGenerators;
    }

    public PhasingWoodProvider wood(Block logBlock) {
        PhasingTextureMapping woodMapping = this.logMapping.copyAndUpdate(TextureSlot.SIDE, TextureSlot.END);
        Arrays.stream(TimePhase.values()).forEach(phase -> {
            Identifier modelId = Services.MODEL_TEMPLATES.extend(ModelTemplates.CUBE_COLUMN)
                    .withRenderType(phase == TimePhase.FULL ? SOLID_RENDER_TYPE : TRANSLUCENT_RENDER_TYPE)
                    .createWithSuffix(logBlock, "_" + phase, woodMapping.resolve(phase), this.blockModelGenerators.modelOutput);
            this.blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createAxisAlignedPillarBlock(logBlock, BlockModelGenerators.plainVariant(modelId)));
            if (phase == TimePhase.FULL) {
                this.blockModelGenerators.registerSimpleItemModel(logBlock, modelId);
            }
        });
        return this;
    }

    public PhasingWoodProvider logWithHorizontal(Block logBlock) {
        Arrays.stream(TimePhase.values()).forEach(phase -> {
            Identifier verticalModelId = Services.MODEL_TEMPLATES.extend(ModelTemplates.CUBE_COLUMN)
                    .withRenderType(phase == TimePhase.FULL ? SOLID_RENDER_TYPE : TRANSLUCENT_RENDER_TYPE)
                    .createWithSuffix(logBlock, "_" + phase, this.logMapping.resolve(phase), this.blockModelGenerators.modelOutput);
            Identifier horizontalModelId = Services.MODEL_TEMPLATES.extend(ModelTemplates.CUBE_COLUMN_HORIZONTAL)
                    .withRenderType(phase == TimePhase.FULL ? SOLID_RENDER_TYPE : TRANSLUCENT_RENDER_TYPE)
                    .createWithSuffix(logBlock, "_" + phase, this.logMapping.resolve(phase), this.blockModelGenerators.modelOutput);
            this.blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createRotatedPillarWithHorizontalVariant(logBlock, BlockModelGenerators.plainVariant(verticalModelId), BlockModelGenerators.plainVariant(horizontalModelId)));
            if (phase == TimePhase.FULL) {
                this.blockModelGenerators.registerSimpleItemModel(logBlock, verticalModelId);
            }
        });
        return this;
    }
}

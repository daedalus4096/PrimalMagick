package com.verdantartifice.primalmagick.datagen.models;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class PhasingBlockFamilyProvider {
    private static final Identifier SOLID_RENDER_TYPE = AbstractModelProviderPM.SOLID_RENDER_TYPE;
    private static final Identifier TRANSLUCENT_RENDER_TYPE = AbstractModelProviderPM.TRANSLUCENT_RENDER_TYPE;

    private static final Map<BlockFamily.Variant, BiConsumer<PhasingBlockFamilyProvider, Block>> SHAPE_CONSUMERS;

    private final PhasingTextureMapping mapping;
    private final BlockModelGenerators blockModelGenerators;
    private final Map<ModelTemplate, Identifier> models = new HashMap<>();
    private final Map<TimePhase, MultiVariant> fullBlockVariants = new HashMap<>();
    private @Nullable BlockFamily family;

    public PhasingBlockFamilyProvider(PhasingTextureMapping mapping, BlockModelGenerators blockModelGenerators) {
        this.mapping = mapping;
        this.blockModelGenerators = blockModelGenerators;
    }

    public PhasingBlockFamilyProvider fullBlock(Block block, ModelTemplate modelTemplate) {
        Arrays.stream(TimePhase.values()).forEach(phase -> {
            Identifier modelId = this.createExtendedModel(block, ModelTemplates.CUBE_ALL, phase);
            MultiVariant multiVariant = BlockModelGenerators.plainVariant(modelId);
            this.fullBlockVariants.put(phase, multiVariant);
            this.blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, multiVariant));
            if (phase == TimePhase.FULL) {
                this.blockModelGenerators.registerSimpleItemModel(block, modelId);
            }
        });
        return this;
    }

    public PhasingBlockFamilyProvider slab(Block block) {
        Arrays.stream(TimePhase.values()).forEach(phase -> {
            if (!this.fullBlockVariants.containsKey(phase)) {
                throw new IllegalStateException("Full block not generated for phase: " + phase);
            } else {
                Identifier bottomModelId = this.createExtendedModel(block, ModelTemplates.SLAB_BOTTOM, phase);
                Identifier topModelId = this.createExtendedModel(block, ModelTemplates.SLAB_TOP, phase);
                this.blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createSlab(block, BlockModelGenerators.plainVariant(bottomModelId), BlockModelGenerators.plainVariant(topModelId), this.fullBlock));
                if (phase == TimePhase.FULL) {
                    this.blockModelGenerators.registerSimpleItemModel(block, bottomModelId);
                }
            }
        });
        return this;
    }

    public PhasingBlockFamilyProvider stairs(Block block) {
        Arrays.stream(TimePhase.values()).forEach(phase -> {
            Identifier innerModelId = this.createExtendedModel(block, ModelTemplates.STAIRS_INNER, phase);
            Identifier straightModelId = this.createExtendedModel(block, ModelTemplates.STAIRS_STRAIGHT, phase);
            Identifier outerModelId = this.createExtendedModel(block, ModelTemplates.STAIRS_OUTER, phase);
            this.blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createStairs(block, BlockModelGenerators.plainVariant(innerModelId), BlockModelGenerators.plainVariant(straightModelId), BlockModelGenerators.plainVariant(outerModelId)));
            if (phase == TimePhase.FULL) {
                this.blockModelGenerators.registerSimpleItemModel(block, straightModelId);
            }
        });
        return this;
    }

    private Identifier createExtendedModel(Block block, ModelTemplate modelTemplate, TimePhase phase) {
        return Services.MODEL_TEMPLATES.extend(modelTemplate)
                .withRenderType(phase == TimePhase.FULL ? SOLID_RENDER_TYPE : TRANSLUCENT_RENDER_TYPE)
                .createWithSuffix(block, "_" + phase, this.mapping.resolve(phase), this.blockModelGenerators.modelOutput);
    }

    public PhasingBlockFamilyProvider generateFor(BlockFamily family) {
        this.family = family;
        family.getVariants().forEach((variant, block) -> {

        });
        return this;
    }
}

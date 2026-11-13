package com.verdantartifice.primalmagick.datagen.models;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.blocks.trees.IPhasingBlock;
import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Direction;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class PhasingBlockFamilyProvider {
    private static final Map<BlockFamily.Variant, BiConsumer<PhasingBlockFamilyProvider, Block>> SHAPE_CONSUMERS = ImmutableMap.<BlockFamily.Variant, BiConsumer<PhasingBlockFamilyProvider, Block>>builder()
            .put(BlockFamily.Variant.SLAB, PhasingBlockFamilyProvider::slab)
            .put(BlockFamily.Variant.STAIRS, PhasingBlockFamilyProvider::stairs)
            .build();

    private final PhasingTextureMapping mapping;
    private final BlockModelGenerators blockModelGenerators;
    private final Map<TimePhase, MultiVariant> fullBlockVariants = new HashMap<>();

    public PhasingBlockFamilyProvider(PhasingTextureMapping mapping, BlockModelGenerators blockModelGenerators) {
        this.mapping = mapping;
        this.blockModelGenerators = blockModelGenerators;
    }

    public PhasingBlockFamilyProvider fullBlock(Block block, ModelTemplate modelTemplate) {
        var propertyDispatch = PropertyDispatch.initial(IPhasingBlock.PHASE);
        Arrays.stream(TimePhase.values()).forEach(phase -> {
            Identifier modelId = this.createExtendedModel(block, modelTemplate, phase);
            MultiVariant multiVariant = BlockModelGenerators.plainVariant(modelId);
            this.fullBlockVariants.put(phase, multiVariant);
            propertyDispatch.select(phase, multiVariant);
            if (phase == TimePhase.FULL) {
                this.blockModelGenerators.registerSimpleItemModel(block, modelId);
            }
        });
        this.blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(propertyDispatch));
        return this;
    }

    public PhasingBlockFamilyProvider slab(Block block) {
        var propertyDispatch = PropertyDispatch.initial(BlockStateProperties.SLAB_TYPE, IPhasingBlock.PHASE);
        Arrays.stream(TimePhase.values()).forEach(phase -> {
            if (!this.fullBlockVariants.containsKey(phase)) {
                throw new IllegalStateException("Full block not generated for phase: " + phase);
            } else {
                Identifier bottomModelId = this.createExtendedModel(block, ModelTemplates.SLAB_BOTTOM, phase);
                Identifier topModelId = this.createExtendedModel(block, ModelTemplates.SLAB_TOP, phase);
                propertyDispatch
                        .select(SlabType.BOTTOM, phase, BlockModelGenerators.plainVariant(bottomModelId))
                        .select(SlabType.TOP, phase, BlockModelGenerators.plainVariant(topModelId))
                        .select(SlabType.DOUBLE, phase, this.fullBlockVariants.get(phase));
                if (phase == TimePhase.FULL) {
                    this.blockModelGenerators.registerSimpleItemModel(block, bottomModelId);
                }
            }
        });
        this.blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(propertyDispatch));
        return this;
    }

    public PhasingBlockFamilyProvider stairs(Block block) {
        var propertyDispatch = PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF, BlockStateProperties.STAIRS_SHAPE, IPhasingBlock.PHASE);
        Arrays.stream(TimePhase.values()).forEach(phase -> {
            Identifier innerModelId = this.createExtendedModel(block, ModelTemplates.STAIRS_INNER, phase);
            Identifier straightModelId = this.createExtendedModel(block, ModelTemplates.STAIRS_STRAIGHT, phase);
            Identifier outerModelId = this.createExtendedModel(block, ModelTemplates.STAIRS_OUTER, phase);
            MultiVariant inner = BlockModelGenerators.plainVariant(innerModelId);
            MultiVariant straight = BlockModelGenerators.plainVariant(straightModelId);
            MultiVariant outer = BlockModelGenerators.plainVariant(outerModelId);
            propertyDispatch
                    .select(Direction.EAST, Half.BOTTOM, StairsShape.STRAIGHT, phase, straight)
                    .select(Direction.WEST, Half.BOTTOM, StairsShape.STRAIGHT, phase, straight.with(BlockModelGenerators.Y_ROT_180).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.SOUTH, Half.BOTTOM, StairsShape.STRAIGHT, phase, straight.with(BlockModelGenerators.Y_ROT_90).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.NORTH, Half.BOTTOM, StairsShape.STRAIGHT, phase, straight.with(BlockModelGenerators.Y_ROT_270).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.EAST, Half.BOTTOM, StairsShape.OUTER_RIGHT, phase, outer)
                    .select(Direction.WEST, Half.BOTTOM, StairsShape.OUTER_RIGHT, phase, outer.with(BlockModelGenerators.Y_ROT_180).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_RIGHT, phase, outer.with(BlockModelGenerators.Y_ROT_90).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_RIGHT, phase, outer.with(BlockModelGenerators.Y_ROT_270).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.EAST, Half.BOTTOM, StairsShape.OUTER_LEFT, phase, outer.with(BlockModelGenerators.Y_ROT_270).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.WEST, Half.BOTTOM, StairsShape.OUTER_LEFT, phase, outer.with(BlockModelGenerators.Y_ROT_90).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_LEFT, phase, outer)
                    .select(Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_LEFT, phase, outer.with(BlockModelGenerators.Y_ROT_180).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.EAST, Half.BOTTOM, StairsShape.INNER_RIGHT, phase, inner)
                    .select(Direction.WEST, Half.BOTTOM, StairsShape.INNER_RIGHT, phase, inner.with(BlockModelGenerators.Y_ROT_180).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_RIGHT, phase, inner.with(BlockModelGenerators.Y_ROT_90).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.NORTH, Half.BOTTOM, StairsShape.INNER_RIGHT, phase, inner.with(BlockModelGenerators.Y_ROT_270).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.EAST, Half.BOTTOM, StairsShape.INNER_LEFT, phase, inner.with(BlockModelGenerators.Y_ROT_270).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.WEST, Half.BOTTOM, StairsShape.INNER_LEFT, phase, inner.with(BlockModelGenerators.Y_ROT_90).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_LEFT, phase, inner)
                    .select(Direction.NORTH, Half.BOTTOM, StairsShape.INNER_LEFT, phase, inner.with(BlockModelGenerators.Y_ROT_180).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.EAST, Half.TOP, StairsShape.STRAIGHT, phase, straight.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.WEST, Half.TOP, StairsShape.STRAIGHT, phase, straight.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_180).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.SOUTH, Half.TOP, StairsShape.STRAIGHT, phase, straight.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_90).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.NORTH, Half.TOP, StairsShape.STRAIGHT, phase, straight.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_270).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.EAST, Half.TOP, StairsShape.OUTER_RIGHT, phase, outer.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_90).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.WEST, Half.TOP, StairsShape.OUTER_RIGHT, phase, outer.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_270).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.SOUTH, Half.TOP, StairsShape.OUTER_RIGHT, phase, outer.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_180).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.NORTH, Half.TOP, StairsShape.OUTER_RIGHT, phase, outer.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.EAST, Half.TOP, StairsShape.OUTER_LEFT, phase, outer.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.WEST, Half.TOP, StairsShape.OUTER_LEFT, phase, outer.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_180).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.SOUTH, Half.TOP, StairsShape.OUTER_LEFT, phase, outer.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_90).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.NORTH, Half.TOP, StairsShape.OUTER_LEFT, phase, outer.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_270).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.EAST, Half.TOP, StairsShape.INNER_RIGHT, phase, inner.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_90).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.WEST, Half.TOP, StairsShape.INNER_RIGHT, phase, inner.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_270).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.SOUTH, Half.TOP, StairsShape.INNER_RIGHT, phase, inner.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_180).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.NORTH, Half.TOP, StairsShape.INNER_RIGHT, phase, inner.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.EAST, Half.TOP, StairsShape.INNER_LEFT, phase, inner.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.WEST, Half.TOP, StairsShape.INNER_LEFT, phase, inner.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_180).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.SOUTH, Half.TOP, StairsShape.INNER_LEFT, phase, inner.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_90).with(BlockModelGenerators.UV_LOCK))
                    .select(Direction.NORTH, Half.TOP, StairsShape.INNER_LEFT, phase, inner.with(BlockModelGenerators.X_ROT_180).with(BlockModelGenerators.Y_ROT_270).with(BlockModelGenerators.UV_LOCK));
            if (phase == TimePhase.FULL) {
                this.blockModelGenerators.registerSimpleItemModel(block, straightModelId);
            }
        });
        this.blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(propertyDispatch));
        return this;
    }

    private Identifier createExtendedModel(Block block, ModelTemplate modelTemplate, TimePhase phase) {
        return Services.MODEL_TEMPLATES.extend(modelTemplate)
                .createWithSuffix(block, "_" + phase, this.mapping.resolve(phase), this.blockModelGenerators.modelOutput);
    }

    public PhasingBlockFamilyProvider generateFor(BlockFamily family) {
        family.getVariants().forEach((variant, block) -> {
            BiConsumer<PhasingBlockFamilyProvider, Block> consumer = SHAPE_CONSUMERS.get(variant);
            if (consumer != null) {
                consumer.accept(this, block);
            }
        });
        return this;
    }
}

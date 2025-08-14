package com.verdantartifice.primalmagick.datagen.models;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModelProviderPMForge extends AbstractModelProviderPM {
    public ModelProviderPMForge(PackOutput output) {
        super(output);
    }

    @Override
    protected BlockModelGenerators getBlockModelGenerators(BlockStateGeneratorCollector blocks, ItemInfoCollector items, SimpleModelCollector models) {
        return new BlockModelGeneratorsPMForge(blocks, items, models, this);
    }

    @Override
    protected ItemModelGenerators getItemModelGenerators(ItemInfoCollector items, SimpleModelCollector models) {
        return super.getItemModelGenerators(items, models);
    }

    private static class BlockModelGeneratorsPMForge extends BlockModelGenerators {
        private final AbstractModelProviderPM parentProvider;

        public BlockModelGeneratorsPMForge(Consumer<BlockModelDefinitionGenerator> pBlockStateOutput,
                                           ItemModelOutput pItemModelOutput,
                                           BiConsumer<ResourceLocation, ModelInstance> pModelOutput,
                                           AbstractModelProviderPM pParentProvider) {
            super(pBlockStateOutput, pItemModelOutput, pModelOutput);
            this.parentProvider = pParentProvider;
        }

        @Override
        public void run() {
            this.parentProvider.executeBlockModelGenerators(this);
        }
    }

    private static class ItemModelGeneratorsPMForge extends ItemModelGenerators {
        private final AbstractModelProviderPM parentProvider;

        public ItemModelGeneratorsPMForge(ItemModelOutput pItemModelOutput, BiConsumer<ResourceLocation, ModelInstance> pModelOutput, AbstractModelProviderPM pParentProvider) {
            super(pItemModelOutput, pModelOutput);
            this.parentProvider = pParentProvider;
        }

        @Override
        public void run() {
            this.parentProvider.executeItemModelGenerators(this);
        }
    }
}

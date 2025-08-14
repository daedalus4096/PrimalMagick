package com.verdantartifice.primalmagick.datagen.models;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;

public abstract class AbstractModelProviderPM extends ModelProvider {
    public AbstractModelProviderPM(PackOutput output) {
        super(output);
    }

    protected void executeBlockModelGenerators(BlockModelGenerators blockModels) {
        // TODO Actually create models here
        blockModels.createTrivialCube(BlocksPM.MARBLE_RAW.get());
    }

    protected void executeItemModelGenerators(ItemModelGenerators itemModels) {
        // TODO Actually create models and client items here
    }
}

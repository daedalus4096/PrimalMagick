package com.verdantartifice.primalmagick.datagen.models;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
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
        itemModels.generateFlatItem(ItemsPM.HYDROMELON_SEEDS.get(), ModelTemplates.FLAT_ITEM);
    }
}

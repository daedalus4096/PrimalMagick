package com.verdantartifice.primalmagick.datagen.models;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

public class ModelProviderPMNeoforge extends AbstractModelProviderPM {
    // The parent class is in common code, which means the two-parameter constructor added by NF isn't
    // usable, and its modId field is final and so can't be set in this class's constructor. So, ignore
    // it and save our own instead, overriding all the methods that would look for it to use this field
    // instead.
    private final String modIdentifier;

    public ModelProviderPMNeoforge(PackOutput output, String modId) {
        super(output);
        this.modIdentifier = modId;
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.executeBlockModelGenerators(blockModels);
        this.executeItemModelGenerators(itemModels);
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.listElements().filter(holder -> holder.getKey().identifier().getNamespace().equals(this.modIdentifier));
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return BuiltInRegistries.ITEM.listElements().filter(holder -> holder.getKey().identifier().getNamespace().equals(this.modIdentifier));
    }

    @Override
    public String getName() {
        return "Model Definitions - " + this.modIdentifier;
    }

    @Override
    public Identifier modLocation(String modelPath) {
        return Identifier.fromNamespaceAndPath(this.modIdentifier, modelPath);
    }
}

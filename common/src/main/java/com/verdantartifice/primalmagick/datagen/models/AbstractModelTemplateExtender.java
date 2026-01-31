package com.verdantartifice.primalmagick.datagen.models;

import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;

public abstract class AbstractModelTemplateExtender implements IModelTemplateExtender {
    protected final ModelTemplate modelTemplate;
    protected Optional<Identifier> renderTypeOpt = Optional.empty();

    protected AbstractModelTemplateExtender(ModelTemplate modelTemplate) {
        this.modelTemplate = modelTemplate;
    }

    @Override
    public IModelTemplateExtender withRenderType(@NotNull Identifier renderType) {
        this.renderTypeOpt = Optional.of(renderType);
        return this;
    }

    @Override
    public Identifier create(@NotNull Block pBlock, @NotNull TextureMapping pTextureMapping, @NotNull BiConsumer<Identifier, ModelInstance> pOutput) {
        return this.createInner(ModelLocationUtils.getModelLocation(pBlock, this.modelTemplate.suffix.orElse("")), pTextureMapping, pOutput);
    }

    @Override
    public Identifier createWithSuffix(@NotNull Block pBlock, @NotNull String pSuffix, @NotNull TextureMapping pTextureMapping, @NotNull BiConsumer<Identifier, ModelInstance> pOutput) {
        return this.createInner(ModelLocationUtils.getModelLocation(pBlock, pSuffix + this.modelTemplate.suffix.orElse("")), pTextureMapping, pOutput);
    }

    @Override
    public Identifier createWithOverride(@NotNull Block pBlock, @NotNull String pSuffix, @NotNull TextureMapping pTextureMapping, @NotNull BiConsumer<Identifier, ModelInstance> pOutput) {
        return this.createInner(ModelLocationUtils.getModelLocation(pBlock, pSuffix), pTextureMapping, pOutput);
    }

    @Override
    public Identifier create(@NotNull Item pItem, @NotNull TextureMapping pTextureMapping, @NotNull BiConsumer<Identifier, ModelInstance> pOutput) {
        return this.createInner(ModelLocationUtils.getModelLocation(pItem, this.modelTemplate.suffix.orElse("")), pTextureMapping, pOutput);
    }

    protected abstract Identifier createInner(@NotNull Identifier modelLocation, @NotNull TextureMapping textureMapping, @NotNull BiConsumer<Identifier, ModelInstance> output);
}

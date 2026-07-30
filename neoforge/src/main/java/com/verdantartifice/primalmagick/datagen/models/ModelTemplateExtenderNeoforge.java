package com.verdantartifice.primalmagick.datagen.models;

import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;

public class ModelTemplateExtenderNeoforge extends AbstractModelTemplateExtender {
    private Optional<Identifier> parentOpt = Optional.empty();

    public ModelTemplateExtenderNeoforge(ModelTemplate modelTemplate) {
        super(modelTemplate);
    }

    @Override
    protected Identifier createInner(@NotNull Identifier modelLocation, @NotNull TextureMapping textureMapping, @NotNull BiConsumer<Identifier, ModelInstance> output) {
        ExtendedModelTemplateBuilder builder = this.modelTemplate.extend();
        this.parentOpt.ifPresent(builder::parent);
        // TODO Apply other extensions from the Neoforge-specific model extender
        return builder.build().create(modelLocation, textureMapping, output);
    }

    @Override
    public IModelTemplateExtender parent(@NotNull Identifier parentId) {
        this.parentOpt = Optional.of(parentId);
        return this;
    }
}

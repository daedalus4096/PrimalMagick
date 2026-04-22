package com.verdantartifice.primalmagick.datagen.models;

import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class ModelTemplateExtenderNeoforge extends AbstractModelTemplateExtender {
    public ModelTemplateExtenderNeoforge(ModelTemplate modelTemplate) {
        super(modelTemplate);
    }

    @Override
    protected Identifier createInner(@NotNull Identifier modelLocation, @NotNull TextureMapping textureMapping, @NotNull BiConsumer<Identifier, ModelInstance> output) {
        ExtendedModelTemplateBuilder builder = this.modelTemplate.extend();
        this.renderTypeOpt.ifPresent(builder::renderType);
        return builder.build().create(modelLocation, textureMapping, output);
    }
}

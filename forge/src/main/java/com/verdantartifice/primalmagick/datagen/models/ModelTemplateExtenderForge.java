package com.verdantartifice.primalmagick.datagen.models;

import com.google.gson.JsonObject;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class ModelTemplateExtenderForge extends AbstractModelTemplateExtender {
    private static final String RENDER_TYPE_PROPERTY = "render_type";

    public ModelTemplateExtenderForge(ModelTemplate modelTemplate) {
        super(modelTemplate);
    }

    @Override
    protected Identifier createInner(@NotNull Identifier modelLocation, @NotNull TextureMapping textureMapping, @NotNull BiConsumer<Identifier, ModelInstance> output) {
        return this.modelTemplate.create(modelLocation, textureMapping, (name, modelInstance) -> {
            JsonObject json = modelInstance.get().getAsJsonObject();
            this.renderTypeOpt.ifPresent(renderType -> json.addProperty(RENDER_TYPE_PROPERTY, renderType.toString()));
            output.accept(name, () -> json);
        });
    }
}

package com.verdantartifice.primalmagick.datagen.models;

import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * Specifies the model connection type of a block with connected textures, such as skyglass. Typically refers to the
 * number and configuration of connected block faces.
 *
 * @author Daedalus4096
 */
public record ModelConnection(String name, Optional<String> parentSuffixOpt, Map<TextureSlot, TextureConnection> textureConnections) {
    public ModelConnection(String name, Map<TextureSlot, TextureConnection> textureConnections) {
        this(name, Optional.of("base"), textureConnections);
    }

    @NotNull
    public String suffix() {
        return "_" + this.name;
    }

    public IModelTemplateExtender extendModel(ModelTemplate modelTemplate, Identifier parentPrefix) {
        IModelTemplateExtender extender = Services.MODEL_TEMPLATES.extend(modelTemplate);
        parentSuffixOpt.ifPresent(suffix -> {
            extender.parent(parentPrefix.withSuffix("_" + suffix));
        });
        return extender;
    }
}

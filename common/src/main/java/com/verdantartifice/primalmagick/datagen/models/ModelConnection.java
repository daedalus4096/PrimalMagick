package com.verdantartifice.primalmagick.datagen.models;

import net.minecraft.client.data.models.model.TextureSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Specifies the model connection type of a block with connected textures, such as skyglass. Typically refers to the
 * number and configuration of connected block faces.
 *
 * @author Daedalus4096
 */
public record ModelConnection(String name, Map<TextureSlot, TextureConnection> textureConnections) {
    @NotNull
    public String suffix() {
        return "_" + this.name;
    }
}

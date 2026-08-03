package com.verdantartifice.primalmagick.datagen.models;

import org.jetbrains.annotations.NotNull;

/**
 * Specifies the model connection type of a block with connected textures, such as skyglass. Typically refers to the
 * number and configuration of connected block faces.
 *
 * @author Daedalus4096
 */
public record ModelConnection(String name) {
    // TODO Add map of texture slots to texture connection enums

    @NotNull
    public String suffix() {
        return "_" + this.name;
    }
}

package com.verdantartifice.primalmagick.datagen.models;

import org.jetbrains.annotations.NotNull;

/**
 * Specifies the texture connection type of a face of a block with connected textures, such as skyglass. Typically
 * refers to which faces have connections attached.
 *
 * @author Daedalus4096
 */
public record TextureConnection(String name) {
    @NotNull
    public String suffix() {
        return "_" + this.name;
    }
}

package com.verdantartifice.primalmagick.datagen.models;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum TextureConnection implements StringRepresentable {
    UNCONNECTED("unconnected"),
    U("u"),
    R("r"),
    UD("ud"),
    UL("ul"),
    UR("ur"),
    LR("lr"),
    UDL("udl"),
    UDR("udr"),
    ULR("ulr"),
    UDLR("udlr");

    private final String name;

    TextureConnection(String name) {
        this.name = name;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name;
    }
}

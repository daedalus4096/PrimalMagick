package com.verdantartifice.primalmagick.datagen.models;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

/**
 * Specifies the model connection type of a block with connected textures, such as skyglass. Typically refers to the
 * number and configuration of connected block faces.
 *
 * @author Daedalus4096
 */
public enum ModelConnection implements StringRepresentable {
    ZERO("0"),
    ONE("1"),
    TWO_ANGLE("2_angle"),
    TWO_LINE("2_line"),
    THREE_ANGLE("3_angle"),
    THREE_T1("3_t1"),
    THREE_T2("3_t2"),
    FOUR_ANGLE("4_angle"),
    FOUR_CROSS("4_cross");

    private final String name;
    // TODO Add map of texture slots to texture connection enums

    ModelConnection(String name) {
        this.name = name;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name;
    }
}

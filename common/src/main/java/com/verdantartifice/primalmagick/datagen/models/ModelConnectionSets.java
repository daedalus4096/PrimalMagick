package com.verdantartifice.primalmagick.datagen.models;

import java.util.List;

public class ModelConnectionSets {
    public static final ModelConnectionSet CUBE = new ModelConnectionSet("cube",
            List.of(ModelConnections.ZERO, ModelConnections.ONE, ModelConnections.TWO_ANGLE, ModelConnections.TWO_LINE,
                    ModelConnections.THREE_ANGLE, ModelConnections.THREE_T1, ModelConnections.THREE_T2,
                    ModelConnections.FOUR_ANGLE, ModelConnections.FOUR_CROSS));
}

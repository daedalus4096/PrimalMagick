package com.verdantartifice.primalmagick.datagen.models;

import net.minecraft.client.data.models.model.TexturedModel;

public class TexturedModelsPM {
    public static final TexturedModel.Provider PILLAR = TexturedModel.createDefault(TextureMappingsPM::pillar, ModelTemplatesPM.PILLAR);
    public static final TexturedModel.Provider PILLAR_BOTTOM = TexturedModel.createDefault(TextureMappingsPM::pillarBottom, ModelTemplatesPM.PILLAR_BOTTOM);
    public static final TexturedModel.Provider PILLAR_TOP = TexturedModel.createDefault(TextureMappingsPM::pillarTop, ModelTemplatesPM.PILLAR_TOP);
}

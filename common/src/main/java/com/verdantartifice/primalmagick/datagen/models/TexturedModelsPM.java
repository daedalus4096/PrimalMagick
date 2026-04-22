package com.verdantartifice.primalmagick.datagen.models;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;

public class TexturedModelsPM {
    public static final TexturedModel.Provider PILLAR = TexturedModel.createDefault(block -> new TextureMapping()
            .put(TextureSlotsPM.INNER, TextureMapping.getBlockTexture(block, "_inner"))
            .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block)),
        ModelTemplatesPM.PILLAR);
    public static final TexturedModel.Provider PILLAR_BOTTOM = TexturedModel.createDefault(block -> new TextureMapping()
            .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_base"))
            .put(TextureSlotsPM.INNER, TextureMapping.getBlockTexture(block, "_inner"))
            .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_bottom")),
        ModelTemplatesPM.PILLAR_BOTTOM);
    public static final TexturedModel.Provider PILLAR_TOP = TexturedModel.createDefault(block -> new TextureMapping()
            .put(TextureSlotsPM.INNER, TextureMapping.getBlockTexture(block, "_inner"))
            .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_top"))
            .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_base")),
        ModelTemplatesPM.PILLAR_TOP);
}

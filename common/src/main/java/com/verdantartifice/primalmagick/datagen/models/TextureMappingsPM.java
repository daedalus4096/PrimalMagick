package com.verdantartifice.primalmagick.datagen.models;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.world.level.block.Block;

public class TextureMappingsPM {
    public static TextureMapping empty(Block block) {
        return new TextureMapping();
    }

    public static TextureMapping manaFont(Block block) {
        return new TextureMapping()
                .put(TextureSlotsPM.BASE, TextureMapping.getBlockTexture(block));
    }

    public static TextureMapping pillar(Block block) {
        return new TextureMapping()
                .put(TextureSlotsPM.INNER, TextureMapping.getBlockTexture(block, "_inner"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block));
    }

    public static TextureMapping pillarBottom(Block block) {
        return new TextureMapping()
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_base"))
                .put(TextureSlotsPM.INNER, TextureMapping.getBlockTexture(block, "_inner"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_bottom"));
    }

    public static TextureMapping pillarTop(Block block) {
        return new TextureMapping()
                .put(TextureSlotsPM.INNER, TextureMapping.getBlockTexture(block, "_inner"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_top"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_base"));
    }

    public static TextureMapping runescribingAltar(Block block) {
        return new TextureMapping()
                .put(TextureSlotsPM.ALTAR_BOTTOM, TextureMapping.getBlockTexture(block))
                .put(TextureSlotsPM.ALTAR_SIDE, TextureMapping.getBlockTexture(block, "_side"));
    }
}

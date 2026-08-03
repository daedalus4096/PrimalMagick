package com.verdantartifice.primalmagick.datagen.models;

import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.Objects;

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

    public static TextureMapping skyglass0(Block block) {
        return connected(
                block,
                TextureConnections.UNCONNECTED,
                TextureConnections.UNCONNECTED,
                TextureConnections.UNCONNECTED,
                TextureConnections.UNCONNECTED,
                TextureConnections.UNCONNECTED,
                TextureConnections.UNCONNECTED);
    }

    public static TextureMapping skyglass1(Block block) {
        return connected(
                block,
                TextureConnections.UDLR,
                TextureConnections.UNCONNECTED,
                TextureConnections.U,
                TextureConnections.U,
                TextureConnections.U,
                TextureConnections.U);
    }

    private static TextureMapping connected(Block block, TextureConnection up, TextureConnection down, TextureConnection north,
                                            TextureConnection south, TextureConnection west, TextureConnection east) {
        Identifier id = Services.BLOCKS_REGISTRY.getKey(block);
        return connected(Objects.requireNonNull(id), up, down, north, south, west, east);
    }

    private static TextureMapping connected(Block block, TextureConnection up, TextureConnection down, TextureConnection north,
                                            TextureConnection south, TextureConnection west, TextureConnection east,
                                            TextureConnection particle) {
        Identifier id = Services.BLOCKS_REGISTRY.getKey(block);
        return connected(Objects.requireNonNull(id), up, down, north, south, west, east, particle);
    }

    private static TextureMapping connected(Identifier id, TextureConnection up, TextureConnection down, TextureConnection north,
                                            TextureConnection south, TextureConnection west, TextureConnection east) {
        return connected(id, up, down, north, south, west, east, TextureConnections.UNCONNECTED);
    }

    private static TextureMapping connected(Identifier id, TextureConnection up, TextureConnection down, TextureConnection north,
                                            TextureConnection south, TextureConnection west, TextureConnection east,
                                            TextureConnection particle) {
        return new TextureMapping()
                .put(TextureSlot.UP, getConnectedBlockTexture(id, up))
                .put(TextureSlot.DOWN, getConnectedBlockTexture(id, down))
                .put(TextureSlot.NORTH, getConnectedBlockTexture(id, north))
                .put(TextureSlot.SOUTH, getConnectedBlockTexture(id, south))
                .put(TextureSlot.WEST, getConnectedBlockTexture(id, west))
                .put(TextureSlot.EAST, getConnectedBlockTexture(id, east))
                .put(TextureSlot.PARTICLE, getConnectedBlockTexture(id, particle));
    }

    private static Material getConnectedBlockTexture(Block block, TextureConnection connection) {
        return TextureMapping.getBlockTexture(block, "_" + connection);
    }

    private static Material getConnectedBlockTexture(Identifier baseId, TextureConnection connection) {
        return new Material(baseId.withPath(path -> "block/" + path + "_" + connection));
    }
}

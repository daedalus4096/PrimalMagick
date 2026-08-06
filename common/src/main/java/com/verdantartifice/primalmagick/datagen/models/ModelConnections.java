package com.verdantartifice.primalmagick.datagen.models;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.data.models.model.TextureSlot;

public class ModelConnections {
    public static final ModelConnection ZERO = new ModelConnection("0", ImmutableMap.<TextureSlot, TextureConnection>builder()
            .put(TextureSlot.UP, TextureConnections.UNCONNECTED)
            .put(TextureSlot.DOWN, TextureConnections.UNCONNECTED)
            .put(TextureSlot.NORTH, TextureConnections.UNCONNECTED)
            .put(TextureSlot.SOUTH, TextureConnections.UNCONNECTED)
            .put(TextureSlot.WEST, TextureConnections.UNCONNECTED)
            .put(TextureSlot.EAST, TextureConnections.UNCONNECTED)
            .put(TextureSlot.PARTICLE, TextureConnections.UNCONNECTED)
            .build());
    public static final ModelConnection ONE = new ModelConnection("1", ImmutableMap.<TextureSlot, TextureConnection>builder()
            .put(TextureSlot.UP, TextureConnections.UDLR)
            .put(TextureSlot.DOWN, TextureConnections.UNCONNECTED)
            .put(TextureSlot.NORTH, TextureConnections.U)
            .put(TextureSlot.SOUTH, TextureConnections.U)
            .put(TextureSlot.WEST, TextureConnections.U)
            .put(TextureSlot.EAST, TextureConnections.U)
            .put(TextureSlot.PARTICLE, TextureConnections.UNCONNECTED)
            .build());
    public static final ModelConnection TWO_ANGLE = new ModelConnection("2_angle", ImmutableMap.<TextureSlot, TextureConnection>builder()
            .put(TextureSlot.UP, TextureConnections.UDLR)
            .put(TextureSlot.DOWN, TextureConnections.R)
            .put(TextureSlot.NORTH, TextureConnections.UL)
            .put(TextureSlot.SOUTH, TextureConnections.UR)
            .put(TextureSlot.WEST, TextureConnections.U)
            .put(TextureSlot.EAST, TextureConnections.UDLR)
            .put(TextureSlot.PARTICLE, TextureConnections.UNCONNECTED)
            .build());
    public static final ModelConnection TWO_LINE = new ModelConnection("2_line", ImmutableMap.<TextureSlot, TextureConnection>builder()
            .put(TextureSlot.UP, TextureConnections.UDLR)
            .put(TextureSlot.DOWN, TextureConnections.UDLR)
            .put(TextureSlot.NORTH, TextureConnections.UD)
            .put(TextureSlot.SOUTH, TextureConnections.UD)
            .put(TextureSlot.WEST, TextureConnections.UD)
            .put(TextureSlot.EAST, TextureConnections.UD)
            .put(TextureSlot.PARTICLE, TextureConnections.UNCONNECTED)
            .build());
    public static final ModelConnection THREE_ANGLE = new ModelConnection("3_angle", ImmutableMap.<TextureSlot, TextureConnection>builder()
            .put(TextureSlot.UP, TextureConnections.UDLR)
            .put(TextureSlot.DOWN, TextureConnections.UR)
            .put(TextureSlot.NORTH, TextureConnections.UL)
            .put(TextureSlot.SOUTH, TextureConnections.UDLR)
            .put(TextureSlot.WEST, TextureConnections.UR)
            .put(TextureSlot.EAST, TextureConnections.UDLR)
            .put(TextureSlot.PARTICLE, TextureConnections.UNCONNECTED)
            .build());
    public static final ModelConnection THREE_T1 = new ModelConnection("3_t1", ImmutableMap.<TextureSlot, TextureConnection>builder()
            .put(TextureSlot.UP, TextureConnections.UDLR)
            .put(TextureSlot.DOWN, TextureConnections.UDLR)
            .put(TextureSlot.NORTH, TextureConnections.UDR)
            .put(TextureSlot.SOUTH, TextureConnections.UDL)
            .put(TextureSlot.WEST, TextureConnections.UDLR)
            .put(TextureSlot.EAST, TextureConnections.UD)
            .put(TextureSlot.PARTICLE, TextureConnections.UNCONNECTED)
            .build());
    public static final ModelConnection THREE_T2 = new ModelConnection("3_t2", ImmutableMap.<TextureSlot, TextureConnection>builder()
            .put(TextureSlot.UP, TextureConnections.UDLR)
            .put(TextureSlot.DOWN, TextureConnections.LR)
            .put(TextureSlot.NORTH, TextureConnections.ULR)
            .put(TextureSlot.SOUTH, TextureConnections.ULR)
            .put(TextureSlot.WEST, TextureConnections.UDLR)
            .put(TextureSlot.EAST, TextureConnections.UDLR)
            .put(TextureSlot.PARTICLE, TextureConnections.UNCONNECTED)
            .build());
    public static final ModelConnection FOUR_ANGLE = new ModelConnection("4_angle", ImmutableMap.<TextureSlot, TextureConnection>builder()
            .put(TextureSlot.UP, TextureConnections.UDLR)
            .put(TextureSlot.DOWN, TextureConnections.UDLR)
            .put(TextureSlot.NORTH, TextureConnections.UDLR)
            .put(TextureSlot.SOUTH, TextureConnections.UDL)
            .put(TextureSlot.WEST, TextureConnections.UDLR)
            .put(TextureSlot.EAST, TextureConnections.UDR)
            .put(TextureSlot.PARTICLE, TextureConnections.UNCONNECTED)
            .build());
    public static final ModelConnection FOUR_CROSS = new ModelConnection("4_cross", ImmutableMap.<TextureSlot, TextureConnection>builder()
            .put(TextureSlot.UP, TextureConnections.UDLR)
            .put(TextureSlot.DOWN, TextureConnections.UDLR)
            .put(TextureSlot.NORTH, TextureConnections.UDLR)
            .put(TextureSlot.SOUTH, TextureConnections.UDLR)
            .put(TextureSlot.WEST, TextureConnections.UDLR)
            .put(TextureSlot.EAST, TextureConnections.UDLR)
            .put(TextureSlot.PARTICLE, TextureConnections.UNCONNECTED)
            .build());

    public static final ModelConnection PANE_NOSIDE_ALT_U = createPaneConnection(false, true, TextureConnections.U);
    public static final ModelConnection PANE_NOSIDE_ALT_UD = createPaneConnection(false, true, TextureConnections.UD);
    public static final ModelConnection PANE_NOSIDE_ALT_UNCONNECTED = createPaneConnection(false, true, TextureConnections.UNCONNECTED);
    public static final ModelConnection PANE_NOSIDE_U = createPaneConnection(false, false, TextureConnections.U);
    public static final ModelConnection PANE_NOSIDE_UD = createPaneConnection(false, false, TextureConnections.UD);
    public static final ModelConnection PANE_NOSIDE_UNCONNECTED = createPaneConnection(false, false, TextureConnections.UNCONNECTED);
    public static final ModelConnection PANE_SIDE_ALT_LR = createPaneConnection(true, true, TextureConnections.LR);
    public static final ModelConnection PANE_SIDE_ALT_U = createPaneConnection(true, true, TextureConnections.U);
    public static final ModelConnection PANE_SIDE_ALT_UD = createPaneConnection(true, true, TextureConnections.UD);
    public static final ModelConnection PANE_SIDE_ALT_UDLR = createPaneConnection(true, true, TextureConnections.UDLR);
    public static final ModelConnection PANE_SIDE_ALT_ULR = createPaneConnection(true, true, TextureConnections.ULR);
    public static final ModelConnection PANE_SIDE_ALT_UNCONNECTED = createPaneConnection(true, true, TextureConnections.UNCONNECTED);
    public static final ModelConnection PANE_SIDE_LR = createPaneConnection(true, false, TextureConnections.LR);
    public static final ModelConnection PANE_SIDE_U = createPaneConnection(true, false, TextureConnections.U);
    public static final ModelConnection PANE_SIDE_UD = createPaneConnection(true, false, TextureConnections.UD);
    public static final ModelConnection PANE_SIDE_UDLR = createPaneConnection(true, false, TextureConnections.UDLR);
    public static final ModelConnection PANE_SIDE_ULR = createPaneConnection(true, false, TextureConnections.ULR);
    public static final ModelConnection PANE_SIDE_UNCONNECTED = createPaneConnection(true, false, TextureConnections.UNCONNECTED);
    public static final ModelConnection PANE_POST = new ModelConnection("post", ImmutableMap.of());

    protected static ModelConnection createPaneConnection(boolean side, boolean alt, TextureConnection textureConnection) {
        return new ModelConnection((side ? "side_" : "noside_") + (alt ? "alt_" : "") + textureConnection.name(), ImmutableMap.of(TextureSlot.PANE, textureConnection));
    }
}

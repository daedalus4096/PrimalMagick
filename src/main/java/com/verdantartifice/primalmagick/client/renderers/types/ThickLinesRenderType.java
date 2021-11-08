package com.verdantartifice.primalmagick.client.renderers.types;

import java.util.OptionalDouble;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.RenderType;

public class ThickLinesRenderType extends RenderType {
    private static final LineStateShard THICK_LINE_STATE = new LineStateShard(OptionalDouble.of(6.0D));
    
    public static final RenderType THICK_LINES = create("thick_lines", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.LINES, 256, false, false,
            RenderType.CompositeState.builder().setShaderState(RENDERTYPE_LINES_SHADER).setLineState(THICK_LINE_STATE).setLayeringState(VIEW_OFFSET_Z_LAYERING).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).createCompositeState(false));

    protected ThickLinesRenderType(String p_i225992_1_, VertexFormat p_i225992_2_, VertexFormat.Mode p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable p_i225992_7_, Runnable p_i225992_8_) {
        // Dummy constructor, never instantiated
        super(p_i225992_1_, p_i225992_2_, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, p_i225992_7_, p_i225992_8_);
    }
}

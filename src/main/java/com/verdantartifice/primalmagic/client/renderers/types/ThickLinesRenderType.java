package com.verdantartifice.primalmagic.client.renderers.types;

import java.util.OptionalDouble;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ThickLinesRenderType extends RenderType {
    private static final LineState THICK_LINE_STATE = new LineState(OptionalDouble.of(6.0D));
    
    public static final RenderType THICK_LINES = get("thick_lines", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256,
            RenderType.State.getBuilder().line(THICK_LINE_STATE).layer(PROJECTION_LAYERING).transparency(TRANSLUCENT_TRANSPARENCY).writeMask(COLOR_WRITE).build(false));

    protected ThickLinesRenderType(String p_i225992_1_, VertexFormat p_i225992_2_, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable p_i225992_7_, Runnable p_i225992_8_) {
        // Dummy constructor, never instantiated
        super(p_i225992_1_, p_i225992_2_, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, p_i225992_7_, p_i225992_8_);
    }
}

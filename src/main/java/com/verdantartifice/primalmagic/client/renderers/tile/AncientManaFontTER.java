package com.verdantartifice.primalmagic.client.renderers.tile;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.common.blocks.mana.AncientManaFontBlock;
import com.verdantartifice.primalmagic.common.tiles.mana.AncientManaFontTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AncientManaFontTER extends TileEntityRenderer<AncientManaFontTileEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/beacon_beam.png");
    
    @Override
    public void render(AncientManaFontTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
        super.render(tileEntityIn, x, y, z, partialTicks, destroyStage);
        Block block = tileEntityIn.getBlockState().getBlock();
        if (tileEntityIn != null && block instanceof AncientManaFontBlock) {
            Color sourceColor = new Color(((AncientManaFontBlock)block).getSource().getColor());
            float r = sourceColor.getRed() / 255.0F;
            float g = sourceColor.getGreen() / 255.0F;
            float b = sourceColor.getBlue() / 255.0F;
            double ds = 0.1875D;
            
            this.bindTexture(TEXTURE);
            
            GlStateManager.pushMatrix();
            GlStateManager.translated(x + 0.5D, y + 0.5D, z + 0.5D);
            GlStateManager.color4f(r, g, b, 1.0F);

            Tessellator tess = Tessellator.getInstance();
            BufferBuilder bb = tess.getBuffer();
            bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            
            bb.pos(-ds, ds, ds).tex(0.0D, 1.0D).endVertex();
            bb.pos(-ds, -ds, ds).tex(0.0D, 0.0D).endVertex();
            bb.pos(ds, -ds, ds).tex(1.0D, 0.0D).endVertex();
            bb.pos(ds, ds, ds).tex(1.0D, 1.0D).endVertex();
            
            bb.pos(-ds, ds, -ds).tex(0.0D, 1.0D).endVertex();
            bb.pos(-ds, -ds, -ds).tex(0.0D, 0.0D).endVertex();
            bb.pos(ds, -ds, -ds).tex(1.0D, 0.0D).endVertex();
            bb.pos(ds, ds, -ds).tex(1.0D, 1.0D).endVertex();
            
            bb.pos(ds, ds, -ds).tex(1.0D, 0.0D).endVertex();
            bb.pos(ds, -ds, -ds).tex(0.0D, 0.0D).endVertex();
            bb.pos(ds, -ds, ds).tex(0.0D, 1.0D).endVertex();
            bb.pos(ds, ds, ds).tex(1.0D, 1.0D).endVertex();
            
            bb.pos(-ds, -ds, ds).tex(0.0D, 1.0D).endVertex();
            bb.pos(-ds, -ds, -ds).tex(0.0D, 0.0D).endVertex();
            bb.pos(-ds, ds, -ds).tex(1.0D, 0.0D).endVertex();
            bb.pos(-ds, ds, ds).tex(1.0D, 1.0D).endVertex();
            
            bb.pos(ds, ds, -ds).tex(1.0D, 0.0D).endVertex();
            bb.pos(ds, ds, ds).tex(1.0D, 1.0D).endVertex();
            bb.pos(-ds, ds, ds).tex(0.0D, 1.0D).endVertex();
            bb.pos(-ds, ds, -ds).tex(0.0D, 0.0D).endVertex();
            
            bb.pos(ds, -ds, -ds).tex(1.0D, 0.0D).endVertex();
            bb.pos(ds, -ds, ds).tex(1.0D, 1.0D).endVertex();
            bb.pos(-ds, -ds, ds).tex(0.0D, 1.0D).endVertex();
            bb.pos(-ds, -ds, -ds).tex(0.0D, 0.0D).endVertex();
            
            tess.draw();
            
            GlStateManager.popMatrix();
        }
    }
}

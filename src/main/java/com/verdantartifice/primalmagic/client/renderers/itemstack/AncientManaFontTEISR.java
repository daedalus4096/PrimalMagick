package com.verdantartifice.primalmagic.client.renderers.itemstack;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.mana.AncientManaFontBlock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AncientManaFontTEISR extends ItemStackTileEntityRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/mana_font_core.png");

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        PrimalMagic.LOGGER.debug("Calling renderByItem for ancient mana font");
        Item item = itemStackIn.getItem();
        if (item instanceof BlockItem && ((BlockItem)item).getBlock() instanceof AncientManaFontBlock) {
            Color sourceColor = new Color(((AncientManaFontBlock)((BlockItem)item).getBlock()).getSource().getColor());
            float r = sourceColor.getRed() / 255.0F;
            float g = sourceColor.getGreen() / 255.0F;
            float b = sourceColor.getBlue() / 255.0F;
            double ds = 0.1875D;

            Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
            
            GlStateManager.pushLightingAttributes();
            GlStateManager.disableLighting();
            
            GlStateManager.pushMatrix();
            GlStateManager.translated(0.5D, 0.5D, 0.5D);
            GlStateManager.rotated(45.0D, 0.0D, 0.0D, 1.0D);
            GlStateManager.rotated(45.0D, 1.0D, 0.0D, 0.0D);
            GlStateManager.color4f(r, g, b, 1.0F);

            Tessellator tess = Tessellator.getInstance();
            BufferBuilder bb = tess.getBuffer();
            bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            
            bb.pos(-ds, ds, ds).tex(0.0D, 1.0D).endVertex();
            bb.pos(-ds, -ds, ds).tex(0.0D, 0.0D).endVertex();
            bb.pos(ds, -ds, ds).tex(1.0D, 0.0D).endVertex();
            bb.pos(ds, ds, ds).tex(1.0D, 1.0D).endVertex();
            
            bb.pos(-ds, ds, -ds).tex(0.0D, 1.0D).endVertex();
            bb.pos(ds, ds, -ds).tex(1.0D, 1.0D).endVertex();
            bb.pos(ds, -ds, -ds).tex(1.0D, 0.0D).endVertex();
            bb.pos(-ds, -ds, -ds).tex(0.0D, 0.0D).endVertex();
            
            bb.pos(ds, ds, -ds).tex(0.0D, 1.0D).endVertex();
            bb.pos(ds, ds, ds).tex(1.0D, 1.0D).endVertex();
            bb.pos(ds, -ds, ds).tex(1.0D, 0.0D).endVertex();
            bb.pos(ds, -ds, -ds).tex(0.0D, 0.0D).endVertex();
            
            bb.pos(-ds, -ds, ds).tex(1.0D, 0.0D).endVertex();
            bb.pos(-ds, ds, ds).tex(1.0D, 1.0D).endVertex();
            bb.pos(-ds, ds, -ds).tex(0.0D, 1.0D).endVertex();
            bb.pos(-ds, -ds, -ds).tex(0.0D, 0.0D).endVertex();
            
            bb.pos(ds, ds, -ds).tex(1.0D, 0.0D).endVertex();
            bb.pos(-ds, ds, -ds).tex(0.0D, 0.0D).endVertex();
            bb.pos(-ds, ds, ds).tex(0.0D, 1.0D).endVertex();
            bb.pos(ds, ds, ds).tex(1.0D, 1.0D).endVertex();
            
            bb.pos(ds, -ds, -ds).tex(1.0D, 0.0D).endVertex();
            bb.pos(ds, -ds, ds).tex(1.0D, 1.0D).endVertex();
            bb.pos(-ds, -ds, ds).tex(0.0D, 1.0D).endVertex();
            bb.pos(-ds, -ds, -ds).tex(0.0D, 0.0D).endVertex();
            
            tess.draw();
            
            GlStateManager.popMatrix();
            GlStateManager.popAttributes();
        }
    }
}

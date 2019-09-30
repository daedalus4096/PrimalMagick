package com.verdantartifice.primalmagic.client.renderers.itemstack;

import java.awt.Color;
import java.lang.reflect.Method;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.mana.AncientManaFontBlock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;

@OnlyIn(Dist.CLIENT)
public class AncientManaFontTEISR extends ItemStackTileEntityRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/mana_font_core.png");
    private static final ModelResourceLocation MRL = new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, "ancient_font_earth"), "");
    private static Method RENDER_MODEL_METHOD;
    
    static {
        try {
            RENDER_MODEL_METHOD = ItemRenderer.class.getDeclaredMethod("renderModel", IBakedModel.class, int.class, ItemStack.class);
            RENDER_MODEL_METHOD.setAccessible(true);
        } catch (Exception e) {
            RENDER_MODEL_METHOD = null;
            PrimalMagic.LOGGER.catching(e);
        }
    }

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        Item item = itemStackIn.getItem();
        if (item instanceof BlockItem && ((BlockItem)item).getBlock() instanceof AncientManaFontBlock) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            
            Color sourceColor = new Color(((AncientManaFontBlock)((BlockItem)item).getBlock()).getSource().getColor());
            float r = sourceColor.getRed() / 255.0F;
            float g = sourceColor.getGreen() / 255.0F;
            float b = sourceColor.getBlue() / 255.0F;
            double ds = 0.1875D;

            Tessellator tess = Tessellator.getInstance();
            BufferBuilder bb = tess.getBuffer();
            
            // Draw the font base
            IBakedModel model = mc.getModelManager().getModel(MRL);
            model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GUI, false);
            try {
                RENDER_MODEL_METHOD.invoke(itemRenderer, model, Integer.valueOf(-1), itemStackIn);
            } catch (Exception e) {
                PrimalMagic.LOGGER.catching(e);
            }
            
            // Draw the font core
            mc.getTextureManager().bindTexture(TEXTURE);
            
            GlStateManager.pushLightingAttributes();
            GlStateManager.disableLighting();
            
            GlStateManager.pushMatrix();
            GlStateManager.translated(0.5D, 0.5D, 0.5D);
            GlStateManager.rotated(45.0D, 0.0D, 0.0D, 1.0D);
            GlStateManager.rotated(45.0D, 1.0D, 0.0D, 0.0D);
            GlStateManager.color4f(r, g, b, 1.0F);

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
            GlStateManager.enableLighting();
            GlStateManager.popAttributes();
        }
    }
}

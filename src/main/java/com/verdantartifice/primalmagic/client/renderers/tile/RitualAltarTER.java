package com.verdantartifice.primalmagic.client.renderers.tile;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualAltarTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Custom tile entity renderer for ritual altar tile entities.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.tiles.rituals.RitualAltarTileEntity}
 */
@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class RitualAltarTER extends TileEntityRenderer<RitualAltarTileEntity> {
    public RitualAltarTER(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }
    
    protected void addVertex(IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float r, float g, float b, float a, float u, float v) {
        renderer.pos(stack.getLast().getPositionMatrix(), x, y, z)
                .color(r, g, b, a)
                .tex(u, v)
                .lightmap(0, 240)
                .normal(1, 0, 0)
                .endVertex();
    }
    
    protected void renderCube(IVertexBuilder builder, MatrixStack matrixStack, float ds, float r, float g, float b, float a, TextureAtlasSprite sprite) {
        // Draw the south face of the cube
        this.addVertex(builder, matrixStack, -ds, ds, ds, r, g, b, a, sprite.getMinU(), sprite.getMaxV());
        this.addVertex(builder, matrixStack, -ds, -ds, ds, r, g, b, a, sprite.getMinU(), sprite.getMinV());
        this.addVertex(builder, matrixStack, ds, -ds, ds, r, g, b, a, sprite.getMaxU(), sprite.getMinV());
        this.addVertex(builder, matrixStack, ds, ds, ds, r, g, b, a, sprite.getMaxU(), sprite.getMaxV());
        
        // Draw the north face of the cube
        this.addVertex(builder, matrixStack, -ds, ds, -ds, r, g, b, a, sprite.getMinU(), sprite.getMaxV());
        this.addVertex(builder, matrixStack, ds, ds, -ds, r, g, b, a, sprite.getMaxU(), sprite.getMaxV());
        this.addVertex(builder, matrixStack, ds, -ds, -ds, r, g, b, a, sprite.getMaxU(), sprite.getMinV());
        this.addVertex(builder, matrixStack, -ds, -ds, -ds, r, g, b, a, sprite.getMinU(), sprite.getMinV());
        
        // Draw the east face of the cube
        this.addVertex(builder, matrixStack, ds, ds, -ds, r, g, b, a, sprite.getMinU(), sprite.getMaxV());
        this.addVertex(builder, matrixStack, ds, ds, ds, r, g, b, a, sprite.getMaxU(), sprite.getMaxV());
        this.addVertex(builder, matrixStack, ds, -ds, ds, r, g, b, a, sprite.getMaxU(), sprite.getMinV());
        this.addVertex(builder, matrixStack, ds, -ds, -ds, r, g, b, a, sprite.getMinU(), sprite.getMinV());
        
        // Draw the west face of the cube
        this.addVertex(builder, matrixStack, -ds, -ds, ds, r, g, b, a, sprite.getMaxU(), sprite.getMinV());
        this.addVertex(builder, matrixStack, -ds, ds, ds, r, g, b, a, sprite.getMaxU(), sprite.getMaxV());
        this.addVertex(builder, matrixStack, -ds, ds, -ds, r, g, b, a, sprite.getMinU(), sprite.getMaxV());
        this.addVertex(builder, matrixStack, -ds, -ds, -ds, r, g, b, a, sprite.getMinU(), sprite.getMinV());
        
        // Draw the top face of the cube
        this.addVertex(builder, matrixStack, ds, ds, -ds, r, g, b, a, sprite.getMaxU(), sprite.getMinV());
        this.addVertex(builder, matrixStack, -ds, ds, -ds, r, g, b, a, sprite.getMinU(), sprite.getMinV());
        this.addVertex(builder, matrixStack, -ds, ds, ds, r, g, b, a, sprite.getMinU(), sprite.getMaxV());
        this.addVertex(builder, matrixStack, ds, ds, ds, r, g, b, a, sprite.getMaxU(), sprite.getMaxV());
        
        // Draw the bottom face of the cube
        this.addVertex(builder, matrixStack, ds, -ds, -ds, r, g, b, a, sprite.getMaxU(), sprite.getMinV());
        this.addVertex(builder, matrixStack, ds, -ds, ds, r, g, b, a, sprite.getMaxU(), sprite.getMaxV());
        this.addVertex(builder, matrixStack, -ds, -ds, ds, r, g, b, a, sprite.getMinU(), sprite.getMaxV());
        this.addVertex(builder, matrixStack, -ds, -ds, -ds, r, g, b, a, sprite.getMinU(), sprite.getMinV());
    }
    
    @Override
    public void render(RitualAltarTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn == null) {
            return;
        }

        // Render the held item stack above the altar
        ItemStack stack = tileEntityIn.getSyncedStackInSlot(0).copy();
        if (!stack.isEmpty()) {
            int rot = (int)(this.renderDispatcher.world.getWorldInfo().getGameTime() % 360);
            matrixStack.push();
            matrixStack.translate(0.5D, 1.5D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(rot));   // Spin the stack around its Y-axis
            matrixStack.scale(0.75F, 0.75F, 0.75F);
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GUI, combinedLightIn, combinedOverlayIn, matrixStack, buffer);
            matrixStack.pop();
        }

        // Render the ritual orb above the altar if active
        if (tileEntityIn.isActive()) {
            float hue = 120.0F / 360.0F;    // Green
            float saturation = tileEntityIn.getActiveCount() / 100.0F;   // TODO Replace with real saturation calc from tile entity
            Color color = Color.getHSBColor(hue, saturation, 1.0F);
            float r = color.getRed() / 255.0F;
            float g = color.getGreen() / 255.0F;
            float b = color.getBlue() / 255.0F;
            float ds = 0.1875F;
            float ticks = (float)tileEntityIn.getActiveCount() + partialTicks;

            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(AncientManaFontTER.TEXTURE);
            IVertexBuilder builder = buffer.getBuffer(RenderType.translucent());
            
            matrixStack.push();
            matrixStack.translate(0.5D, 2.5D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(MathHelper.sin(ticks * 0.1F) * 180.0F)); // Spin the indicator like a shulker bullet
            matrixStack.rotate(Vector3f.XP.rotationDegrees(MathHelper.cos(ticks * 0.1F) * 180.0F));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(MathHelper.sin(ticks * 0.15F) * 360.0F));
            this.renderCube(builder, matrixStack, ds, r, g, b, 1.0F, sprite);
            
            matrixStack.scale(1.5F, 1.5F, 1.5F);
            this.renderCube(builder, matrixStack, ds, r, g, b, 0.5F, sprite);
            
            matrixStack.pop();
        }
    }
}

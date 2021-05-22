package com.verdantartifice.primalmagic.client.renderers.tile;

import java.awt.Color;
import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.tiles.devices.SanguineCrucibleTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Custom tile entity renderer for sanguine crucible blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.devices.SanguineCrucibleBlock}
 */
@OnlyIn(Dist.CLIENT)
public class SanguineCrucibleTER extends TileEntityRenderer<SanguineCrucibleTileEntity> {
    protected static final ResourceLocation WATER_TEXTURE = new ResourceLocation("block/water_still");
    protected static final Color COLOR = new Color(Source.BLOOD.getColor()).brighter().brighter();
    protected static final float R = COLOR.getRed() / 255.0F;
    protected static final float G = COLOR.getGreen() / 255.0F;
    protected static final float B = COLOR.getBlue() / 255.0F;
    
    public SanguineCrucibleTER(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(SanguineCrucibleTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        
        matrixStackIn.push();
        matrixStackIn.translate(0.0D, tileEntityIn.getFluidHeight(), 0.0D);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
        
        @SuppressWarnings("deprecation")
        TextureAtlasSprite sprite = mc.getModelManager().getAtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).getSprite(WATER_TEXTURE);
        IVertexBuilder builder = bufferIn.getBuffer(RenderType.getTranslucent());
        Matrix4f matrix = matrixStackIn.getLast().getMatrix();
        builder.pos(matrix, 0.0F, 1.0F, 0.0F).color(R, G, B, 1.0F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(0, 240).normal(1, 0, 0).endVertex();
        builder.pos(matrix, 1.0F, 1.0F, 0.0F).color(R, G, B, 1.0F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(0, 240).normal(1, 0, 0).endVertex();
        builder.pos(matrix, 1.0F, 0.0F, 0.0F).color(R, G, B, 1.0F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(0, 240).normal(1, 0, 0).endVertex();
        builder.pos(matrix, 0.0F, 0.0F, 0.0F).color(R, G, B, 1.0F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(0, 240).normal(1, 0, 0).endVertex();
        
        matrixStackIn.pop();
        
        World world = tileEntityIn.getWorld();
        Random rand = world.rand;
        BlockPos pos = tileEntityIn.getPos();
        if (tileEntityIn.showBubble(rand)) {
            double x = (double)pos.getX() + 0.2D + (rand.nextDouble() * 0.6D);
            double y = (double)pos.getY() + (double)tileEntityIn.getFluidHeight();
            double z = (double)pos.getZ() + 0.2D + (rand.nextDouble() * 0.6D);
            FxDispatcher.INSTANCE.crucibleBubble(x, y, z, R, G, B);
        }
        if (rand.nextDouble() < tileEntityIn.getSmokeChance()) {
            double x = (double)pos.getX() + 0.1D + (rand.nextDouble() * 0.8D);
            double y = (double)pos.getY() + 1.0D;
            double z = (double)pos.getZ() + 0.1D + (rand.nextDouble() * 0.8D);
            world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.1D, 0.0D);
        }
    }
}

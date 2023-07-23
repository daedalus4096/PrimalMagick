package com.verdantartifice.primalmagick.client.renderers.tile;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.common.blocks.devices.AbstractWindGeneratorBlock;
import com.verdantartifice.primalmagick.common.tiles.devices.WindGeneratorTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Custom tile entity renderer for zephyr engine and void turbine blocks.
 * 
 * @author Daedalus4096
 */
public class WindGeneratorTER implements BlockEntityRenderer<WindGeneratorTileEntity> {
    public WindGeneratorTER(BlockEntityRendererProvider.Context context) {
    }

    protected void addVertex(VertexConsumer renderer, PoseStack stack, float x, float y, float z, float r, float g, float b, float u, float v) {
        renderer.vertex(stack.last().pose(), x, y, z)
                .color(r, g, b, 1.0F)
                .uv(u, v)
                .uv2(240, 240)
                .normal(1, 0, 0)
                .endVertex();
    }
    
    @Override
    public void render(WindGeneratorTileEntity tileEntityIn, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        BlockState state = tileEntityIn.getBlockState();
        Direction facing = state.getValue(AbstractWindGeneratorBlock.FACING);
        boolean powered = state.getValue(AbstractWindGeneratorBlock.POWERED);
        long time = tileEntityIn.getLevel().getLevelData().getGameTime();

        // Color the tile entity core according to the block's source
        Color sourceColor = (state.getBlock() instanceof AbstractWindGeneratorBlock windBlock) ? new Color(windBlock.getCoreColor()) : Color.WHITE;
        float r = sourceColor.getRed() / 255.0F;
        float g = sourceColor.getGreen() / 255.0F;
        float b = sourceColor.getBlue() / 255.0F;
        float ds = 0.1875F;
        float rot = ((int)(time % 360) + partialTicks) * (powered ? 30.0F : 1.0F);    // Spin the core faster if the block is powered
        float scale = 0.9F;
        
        TextureAtlasSprite sprite = mc.getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(ManaFontTER.TEXTURE);
        VertexConsumer builder = buffer.getBuffer(RenderType.solid());
        
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        if (!facing.getAxis().equals(Direction.Axis.Y)) {
            matrixStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            if (facing.getAxis().equals(Direction.Axis.X)) {
                matrixStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }
        }
        matrixStack.mulPose(Axis.YP.rotationDegrees(rot));   // Spin the core around its Y-axis
        matrixStack.mulPose(Axis.ZP.rotationDegrees(45.0F)); // Tilt the core onto its diagonal
        matrixStack.mulPose(Axis.XP.rotationDegrees(45.0F)); // Tilt the core onto its diagonal
        matrixStack.scale(scale, scale, scale);
        
        // TODO Abstract into a model instead of plotting individual vertices
        // Draw the south face of the core
        this.addVertex(builder, matrixStack, -ds, ds, ds, r, g, b, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, matrixStack, -ds, -ds, ds, r, g, b, sprite.getU0(), sprite.getV0());
        this.addVertex(builder, matrixStack, ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, matrixStack, ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
        
        // Draw the north face of the core
        this.addVertex(builder, matrixStack, -ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, matrixStack, ds, ds, -ds, r, g, b, sprite.getU1(), sprite.getV1());
        this.addVertex(builder, matrixStack, ds, -ds, -ds, r, g, b, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, matrixStack, -ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
        
        // Draw the east face of the core
        this.addVertex(builder, matrixStack, ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, matrixStack, ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
        this.addVertex(builder, matrixStack, ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, matrixStack, ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
        
        // Draw the west face of the core
        this.addVertex(builder, matrixStack, -ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, matrixStack, -ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
        this.addVertex(builder, matrixStack, -ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, matrixStack, -ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
        
        // Draw the top face of the core
        this.addVertex(builder, matrixStack, ds, ds, -ds, r, g, b, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, matrixStack, -ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
        this.addVertex(builder, matrixStack, -ds, ds, ds, r, g, b, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, matrixStack, ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
        
        // Draw the bottom face of the core
        this.addVertex(builder, matrixStack, ds, -ds, -ds, r, g, b, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, matrixStack, ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
        this.addVertex(builder, matrixStack, -ds, -ds, ds, r, g, b, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, matrixStack, -ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
        
        matrixStack.popPose();
    }
}

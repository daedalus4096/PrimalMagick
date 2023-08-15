package com.verdantartifice.primalmagick.client.renderers.tile;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.SpellcraftingAltarRingModel;
import com.verdantartifice.primalmagick.common.blocks.crafting.SpellcraftingAltarBlock;
import com.verdantartifice.primalmagick.common.tiles.crafting.SpellcraftingAltarTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Custom tile entity renderer for spellcrafting altar blocks.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingAltarTER implements BlockEntityRenderer<SpellcraftingAltarTileEntity> {
    public static final ResourceLocation RING_TEXTURE = PrimalMagick.resource("entity/spellcrafting_altar/spellcrafting_altar_ring");
    public static final Material RING_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, RING_TEXTURE);

    protected final SpellcraftingAltarRingModel ringModel;
    
    public SpellcraftingAltarTER(BlockEntityRendererProvider.Context context) {
        this.ringModel = new SpellcraftingAltarRingModel(context.bakeLayer(ModelLayersPM.SPELLCRAFTING_ALTAR_RING));
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
    public void render(SpellcraftingAltarTileEntity tileEntityIn, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        BlockState state = tileEntityIn.getBlockState();
        long time = tileEntityIn.getLevel().getLevelData().getGameTime();
        
        // Render the altar's ring
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0D, 0.5D);
        matrixStack.translate(0D, 2.5D, 0D);    // Model position correction
        double bobDelta = 0.125D * Math.sin((time + (double)partialTicks) * (2D * Math.PI / (double)SpellcraftingAltarTileEntity.BOB_CYCLE_TIME_TICKS));
        matrixStack.translate(0D, bobDelta, 0D);    // Bob the ring up and down
        float facingAngle = state.getValue(SpellcraftingAltarBlock.FACING).getClockWise().toYRot();
        matrixStack.mulPose(Axis.ZP.rotationDegrees(180F));
        matrixStack.mulPose(Axis.YP.rotationDegrees(-facingAngle));
        matrixStack.mulPose(Axis.YP.rotationDegrees(90F));  // Model rotation correction
        matrixStack.mulPose(Axis.YP.rotationDegrees(tileEntityIn.getCurrentRotation(partialTicks)));    // Spin the ring according to tile control
        VertexConsumer ringBuilder = RING_MATERIAL.buffer(buffer, RenderType::entitySolid);
        this.ringModel.renderToBuffer(matrixStack, ringBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();

        // Color the tile entity core according to the block's source
        Color sourceColor = tileEntityIn.getCurrentColor(partialTicks);
        float r = sourceColor.getRed() / 255.0F;
        float g = sourceColor.getGreen() / 255.0F;
        float b = sourceColor.getBlue() / 255.0F;
        float ds = 0.1875F;
        int rot = (int)(time % 360);
        float scale = 0.5F;
        
        TextureAtlasSprite sprite = mc.getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(ManaFontTER.TEXTURE);
        VertexConsumer builder = buffer.getBuffer(RenderType.solid());
        
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 1.125D, 0.5D);
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
        
        // Draw a particle stream rising from the core
        FxDispatcher.INSTANCE.spellcraftingGlow(tileEntityIn.getBlockPos(), r, g, b);
    }
}

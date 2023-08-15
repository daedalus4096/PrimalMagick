package com.verdantartifice.primalmagick.client.renderers.entity;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.renderers.entity.model.SpellProjectileModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.projectiles.SinCrashEntity;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Renderer for a sin crash projectile.  Looks just like a void spell projectile.
 * 
 * @author Daedalus4096
 */
public class SinCrashRenderer extends EntityRenderer<SinCrashEntity> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/entity/spell_projectile.png");
    protected static final RenderType TRANSLUCENT_TYPE = RenderType.entityTranslucent(TEXTURE);

    protected final SpellProjectileModel model;

    public SinCrashRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SpellProjectileModel(context.bakeLayer(ModelLayersPM.SPELL_PROJECTILE));
    }

    @Override
    protected int getBlockLightLevel(SinCrashEntity entityIn, BlockPos blockPos) {
        return 15;
    }
    
    @Override
    public void render(SinCrashEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        float yaw = Mth.rotLerp(entity.yRotO, entity.getYRot(), partialTicks);
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        float ticks = (float)entity.tickCount + partialTicks;
        Color c = new Color(Source.VOID.getColor());
        float r = (float)c.getRed() / 255.0F;
        float g = (float)c.getGreen() / 255.0F;
        float b = (float)c.getBlue() / 255.0F;
        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.15D, 0.0D);
        matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(ticks * 0.1F) * 180.0F)); // Spin the projectile like a shulker bullet
        matrixStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(ticks * 0.1F) * 180.0F));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(ticks * 0.15F) * 360.0F));
        matrixStack.scale(-0.5F, -0.5F, 0.5F);
        this.model.setupAnim(null, 0.0F, 0.0F, 0.0F, yaw, pitch);
        VertexConsumer coreVertexBuilder = buffer.getBuffer(this.model.renderType(TEXTURE));
        this.model.renderToBuffer(matrixStack, coreVertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);    // Render the core of the projectile
        matrixStack.scale(1.5F, 1.5F, 1.5F);
        VertexConsumer glowVertexBuilder = buffer.getBuffer(TRANSLUCENT_TYPE);
        this.model.renderToBuffer(matrixStack, glowVertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, r, g, b, 0.5F);    // Render the transparent glow of the projectile
        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SinCrashEntity entity) {
        return TEXTURE;
    }
}

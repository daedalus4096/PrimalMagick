package com.verdantartifice.primalmagic.client.renderers.entity;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.models.SpellMineModel;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellMineEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Entity renderer for a spell mine.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class SpellMineRenderer extends EntityRenderer<SpellMineEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/spell_projectile.png");
    protected static final RenderType TRANSLUCENT_TYPE = RenderType.entityTranslucent(TEXTURE);

    protected final SpellMineModel model = new SpellMineModel();

    public SpellMineRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager);
    }
    
    @Override
    public void render(SpellMineEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        @SuppressWarnings("deprecation")
        float yaw = Mth.rotlerp(entity.yRotO, entity.yRot, partialTicks);
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.xRot);
        float ticks = (float)entity.tickCount + partialTicks;
        Color c = new Color(entity.getColor());
        float r = (float)c.getRed() / 255.0F;
        float g = (float)c.getGreen() / 255.0F;
        float b = (float)c.getBlue() / 255.0F;
        float alphaFactor = entity.isArmed() ? 0.25F : 1.0F;    // Fade out the mine if it's armed
        double bob = 0.25D * Mth.sin(ticks * 0.1F);      // Calculate a vertical bobbing displacement
        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.5D + bob, 0.0D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(Mth.sin(ticks * 0.1F) * 180.0F)); // Spin the mine like a shulker bullet
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(Mth.cos(ticks * 0.1F) * 180.0F));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.sin(ticks * 0.15F) * 360.0F));
        matrixStack.scale(-0.5F, -0.5F, 0.5F);
        this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, yaw, pitch);
        VertexConsumer coreVertexBuilder = buffer.getBuffer(this.model.renderType(TEXTURE));
        this.model.renderToBuffer(matrixStack, coreVertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F * alphaFactor);  // Render the core of the mine
        matrixStack.scale(1.5F, 1.5F, 1.5F);
        VertexConsumer glowVertexBuilder = buffer.getBuffer(TRANSLUCENT_TYPE);
        this.model.renderToBuffer(matrixStack, glowVertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, r, g, b, 0.5F * alphaFactor);  // Render the transparent glow of the mine
        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SpellMineEntity entity) {
        return TEXTURE;
    }
}

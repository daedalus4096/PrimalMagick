package com.verdantartifice.primalmagick.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.renderers.entity.model.SpellMineModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.projectiles.SpellMineEntity;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

/**
 * Entity renderer for a spell mine.
 * 
 * @author Daedalus4096
 */
public class SpellMineRenderer extends EntityRenderer<SpellMineEntity> {
    protected static final ResourceLocation TEXTURE = ResourceUtils.loc("textures/entity/spell_projectile.png");
    protected static final RenderType TRANSLUCENT_TYPE = RenderType.entityTranslucent(TEXTURE);

    protected final SpellMineModel model;

    public SpellMineRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SpellMineModel(context.bakeLayer(ModelLayersPM.SPELL_MINE));
    }
    
    @Override
    public void render(SpellMineEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        float yaw = Mth.rotLerp(entity.yRotO, entity.getYRot(), partialTicks);
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        float ticks = (float)entity.tickCount + partialTicks;
        float alphaFactor = entity.isArmed() ? 0.25F : 1.0F;    // Fade out the mine if it's armed
        int coreColor = FastColor.ARGB32.color(FastColor.as8BitChannel(1.0F * alphaFactor), entity.getColor());
        int glowColor = FastColor.ARGB32.color(FastColor.as8BitChannel(0.5F * alphaFactor), entity.getColor());
        double bob = 0.25D * Mth.sin(ticks * 0.1F);      // Calculate a vertical bobbing displacement
        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.5D + bob, 0.0D);
        matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(ticks * 0.1F) * 180.0F)); // Spin the mine like a shulker bullet
        matrixStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(ticks * 0.1F) * 180.0F));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(ticks * 0.15F) * 360.0F));
        matrixStack.scale(-0.5F, -0.5F, 0.5F);
        this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, yaw, pitch);
        VertexConsumer coreVertexBuilder = buffer.getBuffer(this.model.renderType(TEXTURE));
        this.model.renderToBuffer(matrixStack, coreVertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, coreColor);  // Render the core of the mine
        matrixStack.scale(1.5F, 1.5F, 1.5F);
        VertexConsumer glowVertexBuilder = buffer.getBuffer(TRANSLUCENT_TYPE);
        this.model.renderToBuffer(matrixStack, glowVertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, glowColor);  // Render the transparent glow of the mine
        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SpellMineEntity entity) {
        return TEXTURE;
    }
}

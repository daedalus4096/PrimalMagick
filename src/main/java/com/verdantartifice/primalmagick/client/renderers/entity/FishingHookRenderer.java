package com.verdantartifice.primalmagick.client.renderers.entity;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.common.entities.projectiles.FishingHookEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/**
 * Entity renderer for a mod fishing hook.  Works with any fishing rod derived from FishingRodItem,
 * instead of just Items.FISHING_ROD.
 * 
 * @author Daedalus4096
 */
public class FishingHookRenderer extends EntityRenderer<FishingHookEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/fishing_hook.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutout(TEXTURE_LOCATION);

    public FishingHookRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(FishingHookEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        Player player = entity.getPlayerOwner();
        if (player != null) {
            matrixStack.pushPose();
            matrixStack.pushPose();
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            PoseStack.Pose posestack$pose = matrixStack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            VertexConsumer vertexconsumer = buffer.getBuffer(RENDER_TYPE);
            vertex(vertexconsumer, matrix4f, matrix3f, packedLight, 0.0F, 0.0F, 0, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, packedLight, 1.0F, 0.0F, 1, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, packedLight, 1.0F, 1.0F, 1, 0);
            vertex(vertexconsumer, matrix4f, matrix3f, packedLight, 0.0F, 1.0F, 0, 0);
            matrixStack.popPose();
            int i = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
            ItemStack itemstack = player.getMainHandItem();
            if (!(itemstack.getItem() instanceof FishingRodItem)) {
                i = -i;
            }
            
            float f = player.getAttackAnim(partialTicks);
            float f1 = Mth.sin(Mth.sqrt(f) * (float)Math.PI);
            float f2 = Mth.lerp(partialTicks, player.yBodyRotO, player.yBodyRot) * ((float)Math.PI / 180F);
            double d0 = (double)Mth.sin(f2);
            double d1 = (double)Mth.cos(f2);
            double d2 = (double)i * 0.35D;
            double d4;
            double d5;
            double d6;
            float f3;
            if ((this.entityRenderDispatcher.options == null || this.entityRenderDispatcher.options.getCameraType().isFirstPerson()) && player == Minecraft.getInstance().player) {
                double d7 = 960.0D / this.entityRenderDispatcher.options.fov().get().intValue();
                Vec3 vec3 = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float)i * 0.525F, -0.1F);
                vec3 = vec3.scale(d7);
                vec3 = vec3.yRot(f1 * 0.5F);
                vec3 = vec3.xRot(-f1 * 0.7F);
                d4 = Mth.lerp((double)partialTicks, player.xo, player.getX()) + vec3.x;
                d5 = Mth.lerp((double)partialTicks, player.yo, player.getY()) + vec3.y;
                d6 = Mth.lerp((double)partialTicks, player.zo, player.getZ()) + vec3.z;
                f3 = player.getEyeHeight();
            } else {
                d4 = Mth.lerp((double)partialTicks, player.xo, player.getX()) - d1 * d2 - d0 * 0.8D;
                d5 = player.yo + (double)player.getEyeHeight() + (player.getY() - player.yo) * (double)partialTicks - 0.45D;
                d6 = Mth.lerp((double)partialTicks, player.zo, player.getZ()) - d0 * d2 + d1 * 0.8D;
                f3 = player.isCrouching() ? -0.1875F : 0.0F;
            }
            
            double d9 = Mth.lerp((double)partialTicks, entity.xo, entity.getX());
            double d10 = Mth.lerp((double)partialTicks, entity.yo, entity.getY()) + 0.25D;
            double d8 = Mth.lerp((double)partialTicks, entity.zo, entity.getZ());
            float f4 = (float)(d4 - d9);
            float f5 = (float)(d5 - d10) + f3;
            float f6 = (float)(d6 - d8);
            VertexConsumer vertexconsumer1 = buffer.getBuffer(RenderType.lineStrip());
            PoseStack.Pose posestack$pose1 = matrixStack.last();
            
            for (int k = 0; k <= 16; ++k) {
                stringVertex(f4, f5, f6, vertexconsumer1, posestack$pose1, fraction(k, 16), fraction(k + 1, 16));
            }

            matrixStack.popPose();
            super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        }
    }
    
    private static float fraction(int numerator, int denominator) {
        return (float)numerator / (float)denominator;
    }
    
    private static void vertex(VertexConsumer vertexConsumer, Matrix4f poseMatrix, Matrix3f normalMatrix, int packedLight, float x, float y, int u, int v) {
        vertexConsumer.vertex(poseMatrix, x - 0.5F, y - 0.5F, 0.0F).color(255, 255, 255, 255).uv((float)u, (float)v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
    }
    
    private static void stringVertex(float x, float y, float z, VertexConsumer vertexConsumer, PoseStack.Pose pose, float curPos, float nextPos) {
        float f = x * curPos;
        float f1 = y * (curPos * curPos + curPos) * 0.5F + 0.25F;
        float f2 = z * curPos;
        float f3 = x * nextPos - f;
        float f4 = y * (nextPos * nextPos + nextPos) * 0.5F + 0.25F - f1;
        float f5 = z * nextPos - f2;
        float f6 = Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
        f3 = f3 / f6;
        f4 = f4 / f6;
        f5 = f5 / f6;
        vertexConsumer.vertex(pose.pose(), f, f1, f2).color(0, 0, 0, 255).normal(pose.normal(), f3, f4, f5).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(FishingHookEntity entity) {
        return TEXTURE_LOCATION;
    }
}

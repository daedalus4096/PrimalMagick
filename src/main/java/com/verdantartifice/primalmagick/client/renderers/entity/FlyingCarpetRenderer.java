package com.verdantartifice.primalmagick.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.renderers.entity.model.FlyingCarpetModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.misc.FlyingCarpetEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

/**
 * Entity renderer for a flying carpet.
 * 
 * @author Daedalus4096
 */
public class FlyingCarpetRenderer extends EntityRenderer<FlyingCarpetEntity> {
    private static final ResourceLocation TEXTURE_WHITE = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_white.png");
    private static final ResourceLocation TEXTURE_ORANGE = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_orange.png");
    private static final ResourceLocation TEXTURE_MAGENTA = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_magenta.png");
    private static final ResourceLocation TEXTURE_LIGHT_BLUE = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_light_blue.png");
    private static final ResourceLocation TEXTURE_YELLOW = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_yellow.png");
    private static final ResourceLocation TEXTURE_LIME = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_lime.png");
    private static final ResourceLocation TEXTURE_PINK = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_pink.png");
    private static final ResourceLocation TEXTURE_GRAY = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_gray.png");
    private static final ResourceLocation TEXTURE_LIGHT_GRAY = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_light_gray.png");
    private static final ResourceLocation TEXTURE_CYAN = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_cyan.png");
    private static final ResourceLocation TEXTURE_PURPLE = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_purple.png");
    private static final ResourceLocation TEXTURE_BLUE = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_blue.png");
    private static final ResourceLocation TEXTURE_BROWN = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_brown.png");
    private static final ResourceLocation TEXTURE_GREEN = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_green.png");
    private static final ResourceLocation TEXTURE_RED = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_red.png");
    private static final ResourceLocation TEXTURE_BLACK = PrimalMagick.resource("textures/entity/flying_carpet/flying_carpet_black.png");
    
    protected final FlyingCarpetModel model;

    public FlyingCarpetRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.model = new FlyingCarpetModel(context.bakeLayer(ModelLayersPM.FLYING_CARPET));
    }

    @Override
    public ResourceLocation getTextureLocation(FlyingCarpetEntity entity) {
        DyeColor color = entity.getDyeColor();
        if (color == null) {
            return TEXTURE_WHITE;
        } else {
            switch (color) {
            case WHITE:
            default:
                return TEXTURE_WHITE;
            case ORANGE:
                return TEXTURE_ORANGE;
            case MAGENTA:
                return TEXTURE_MAGENTA;
            case LIGHT_BLUE:
                return TEXTURE_LIGHT_BLUE;
            case YELLOW:
                return TEXTURE_YELLOW;
            case LIME:
                return TEXTURE_LIME;
            case PINK:
                return TEXTURE_PINK;
            case GRAY:
                return TEXTURE_GRAY;
            case LIGHT_GRAY:
                return TEXTURE_LIGHT_GRAY;
            case CYAN:
                return TEXTURE_CYAN;
            case PURPLE:
                return TEXTURE_PURPLE;
            case BLUE:
                return TEXTURE_BLUE;
            case BROWN:
                return TEXTURE_BROWN;
            case GREEN:
                return TEXTURE_GREEN;
            case RED:
                return TEXTURE_RED;
            case BLACK:
                return TEXTURE_BLACK;
            }
        }
    }

    @Override
    public void render(FlyingCarpetEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0D, 0.375D, 0.0D);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
        
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(entityIn)));
        this.model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}

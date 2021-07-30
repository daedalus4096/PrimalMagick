package com.verdantartifice.primalmagic.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.entity.model.FlyingCarpetModel;
import com.verdantartifice.primalmagic.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagic.common.entities.misc.FlyingCarpetEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Entity renderer for a flying carpet.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class FlyingCarpetRenderer extends EntityRenderer<FlyingCarpetEntity> {
    private static final ResourceLocation TEXTURE_WHITE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_white.png");
    private static final ResourceLocation TEXTURE_ORANGE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_orange.png");
    private static final ResourceLocation TEXTURE_MAGENTA = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_magenta.png");
    private static final ResourceLocation TEXTURE_LIGHT_BLUE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_light_blue.png");
    private static final ResourceLocation TEXTURE_YELLOW = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_yellow.png");
    private static final ResourceLocation TEXTURE_LIME = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_lime.png");
    private static final ResourceLocation TEXTURE_PINK = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_pink.png");
    private static final ResourceLocation TEXTURE_GRAY = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_gray.png");
    private static final ResourceLocation TEXTURE_LIGHT_GRAY = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_light_gray.png");
    private static final ResourceLocation TEXTURE_CYAN = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_cyan.png");
    private static final ResourceLocation TEXTURE_PURPLE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_purple.png");
    private static final ResourceLocation TEXTURE_BLUE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_blue.png");
    private static final ResourceLocation TEXTURE_BROWN = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_brown.png");
    private static final ResourceLocation TEXTURE_GREEN = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_green.png");
    private static final ResourceLocation TEXTURE_RED = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_red.png");
    private static final ResourceLocation TEXTURE_BLACK = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_black.png");
    
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
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(entityIn)));
        this.model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}

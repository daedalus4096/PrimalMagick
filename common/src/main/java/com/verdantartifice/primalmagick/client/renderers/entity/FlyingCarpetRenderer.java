package com.verdantartifice.primalmagick.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.renderers.entity.model.FlyingCarpetModel;
import com.verdantartifice.primalmagick.client.renderers.entity.state.FlyingCarpetRenderState;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.misc.FlyingCarpetEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Entity renderer for a flying carpet.
 * 
 * @author Daedalus4096
 */
public class FlyingCarpetRenderer extends EntityRenderer<FlyingCarpetEntity, FlyingCarpetRenderState> {
    private static final Identifier TEXTURE_WHITE = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_white.png");
    private static final Identifier TEXTURE_ORANGE = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_orange.png");
    private static final Identifier TEXTURE_MAGENTA = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_magenta.png");
    private static final Identifier TEXTURE_LIGHT_BLUE = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_light_blue.png");
    private static final Identifier TEXTURE_YELLOW = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_yellow.png");
    private static final Identifier TEXTURE_LIME = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_lime.png");
    private static final Identifier TEXTURE_PINK = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_pink.png");
    private static final Identifier TEXTURE_GRAY = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_gray.png");
    private static final Identifier TEXTURE_LIGHT_GRAY = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_light_gray.png");
    private static final Identifier TEXTURE_CYAN = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_cyan.png");
    private static final Identifier TEXTURE_PURPLE = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_purple.png");
    private static final Identifier TEXTURE_BLUE = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_blue.png");
    private static final Identifier TEXTURE_BROWN = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_brown.png");
    private static final Identifier TEXTURE_GREEN = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_green.png");
    private static final Identifier TEXTURE_RED = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_red.png");
    private static final Identifier TEXTURE_BLACK = ResourceUtils.loc("textures/entity/flying_carpet/flying_carpet_black.png");
    
    protected final FlyingCarpetModel model;

    public FlyingCarpetRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.model = new FlyingCarpetModel(context.bakeLayer(ModelLayersPM.FLYING_CARPET));
    }

    @Override
    public FlyingCarpetRenderState createRenderState() {
        return new FlyingCarpetRenderState();
    }

    @Override
    public void extractRenderState(FlyingCarpetEntity entity, FlyingCarpetRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        renderState.yRot = entity.getYRot(partialTick);
        renderState.color = entity.getDyeColor();
    }

    public Identifier getTextureLocation(FlyingCarpetRenderState renderState) {
        if (renderState.color == null) {
            return TEXTURE_WHITE;
        } else {
            return switch (renderState.color) {
                case ORANGE -> TEXTURE_ORANGE;
                case MAGENTA -> TEXTURE_MAGENTA;
                case LIGHT_BLUE -> TEXTURE_LIGHT_BLUE;
                case YELLOW -> TEXTURE_YELLOW;
                case LIME -> TEXTURE_LIME;
                case PINK -> TEXTURE_PINK;
                case GRAY -> TEXTURE_GRAY;
                case LIGHT_GRAY -> TEXTURE_LIGHT_GRAY;
                case CYAN -> TEXTURE_CYAN;
                case PURPLE -> TEXTURE_PURPLE;
                case BLUE -> TEXTURE_BLUE;
                case BROWN -> TEXTURE_BROWN;
                case GREEN -> TEXTURE_GREEN;
                case RED -> TEXTURE_RED;
                case BLACK -> TEXTURE_BLACK;
                default -> TEXTURE_WHITE;
            };
        }
    }

    @Override
    public void submit(@NotNull FlyingCarpetRenderState renderState, @NotNull PoseStack matrixStackIn, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState cameraRenderState) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0D, 0.375D, 0.0D);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - renderState.yRot));
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));

        collector.submitModel(this.model, renderState, matrixStackIn, this.model.renderType(this.getTextureLocation(renderState)), renderState.lightCoords, OverlayTexture.NO_OVERLAY, renderState.outlineColor, null);

        matrixStackIn.popPose();
        super.submit(renderState, matrixStackIn, collector, cameraRenderState);
    }
}

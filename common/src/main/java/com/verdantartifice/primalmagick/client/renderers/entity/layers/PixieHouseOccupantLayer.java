package com.verdantartifice.primalmagick.client.renderers.entity.layers;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.client.renderers.entity.BasicPixieRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.GrandPixieRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.MajesticPixieRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieHouseModel;
import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieModel;
import com.verdantartifice.primalmagick.client.renderers.entity.state.PixieHouseRenderState;
import com.verdantartifice.primalmagick.client.renderers.entity.state.PixieRenderState;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.pixies.PixieRank;
import com.verdantartifice.primalmagick.common.items.misc.DrainedPixieItem;
import com.verdantartifice.primalmagick.common.items.misc.IPixieItem;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PixieHouseOccupantLayer extends RenderLayer<PixieHouseRenderState, PixieHouseModel> {
    private static final Map<PixieRank, Identifier> TEXTURES = ImmutableMap.of(
            PixieRank.BASIC, BasicPixieRenderer.TEXTURE,
            PixieRank.GRAND, GrandPixieRenderer.TEXTURE,
            PixieRank.MAJESTIC, MajesticPixieRenderer.TEXTURE);
    private static final PixieRenderState PIXIE_RENDER_STATE = new PixieRenderState();

    private final PixieModel basePixieModel;
    private final PixieModel royalPixieModel;
    private final PixieModel baseDrainedPixieModel;
    private final PixieModel royalDrainedPixieModel;

    public PixieHouseOccupantLayer(RenderLayerParent<PixieHouseRenderState, PixieHouseModel> pRenderer, EntityRendererProvider.Context pContext) {
        super(pRenderer);
        this.basePixieModel = new PixieModel(pContext.getModelSet().bakeLayer(ModelLayersPM.PIXIE_BASIC));
        this.royalPixieModel = new PixieModel(pContext.getModelSet().bakeLayer(ModelLayersPM.PIXIE_ROYAL));
        this.baseDrainedPixieModel = new PixieModel(pContext.getModelSet().bakeLayer(ModelLayersPM.PIXIE_BASIC));
        this.royalDrainedPixieModel = new PixieModel(pContext.getModelSet().bakeLayer(ModelLayersPM.PIXIE_ROYAL));
    }

    @Override
    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, int lightCoords,
                       @NotNull PixieHouseRenderState pixieHouseRenderState, float yRot, float xRot) {
        ItemStack pixieStack = pixieHouseRenderState.occupantStack;
        if (pixieStack.getItem() instanceof IPixieItem pixieItem && pixieHouseRenderState.isOccupantPresent) {
            // Render pixie house occupant if present and not deployed
            PixieRank rank = pixieItem.getPixieRank();
            PixieModel model = rank == PixieRank.MAJESTIC ? this.royalPixieModel : this.basePixieModel;
            double yBob = -0.125D * Mth.sin(pixieHouseRenderState.ageInTicks / 6F);
            poseStack.pushPose();
            poseStack.translate(0D, -0.25D + yBob, 0D);
            poseStack.scale(0.25F, 0.25F, 0.25F);
            model.setupAnim(PIXIE_RENDER_STATE);
            submitNodeCollector.submitModel(model, PIXIE_RENDER_STATE, poseStack, TEXTURES.get(rank), lightCoords, OverlayTexture.NO_OVERLAY, 0, null);
            poseStack.popPose();

            // Render falling pixie dust
            Source source = pixieItem.getPixieSource();
            double px = pixieHouseRenderState.x + pixieHouseRenderState.occupantDx;
            double py = pixieHouseRenderState.y + 1.5D - yBob;
            double pz = pixieHouseRenderState.z + pixieHouseRenderState.occupantDz;
            FxDispatcher.INSTANCE.pixieDust(px, py, pz, source.getColor());
        } else if (pixieStack.getItem() instanceof DrainedPixieItem drainedPixieItem) {
            // Render pixie house occupant convalescing if drained
            PixieRank rank = drainedPixieItem.getPixieRank();
            PixieModel model = rank == PixieRank.MAJESTIC ? this.royalDrainedPixieModel : this.baseDrainedPixieModel;
            poseStack.pushPose();
            poseStack.translate(0D, 0.27D, -0.25D);
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            poseStack.scale(0.25F, 0.25F, 0.25F);
            submitNodeCollector.submitModel(model, PIXIE_RENDER_STATE, poseStack, TEXTURES.get(rank), lightCoords, OverlayTexture.NO_OVERLAY, 0, null);
            poseStack.popPose();
        }
    }
}

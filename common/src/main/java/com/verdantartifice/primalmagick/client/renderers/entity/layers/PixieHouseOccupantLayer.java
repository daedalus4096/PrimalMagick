package com.verdantartifice.primalmagick.client.renderers.entity.layers;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieHouseModel;
import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.companions.pixies.PixieRank;
import com.verdantartifice.primalmagick.common.entities.misc.PixieHouseEntity;
import com.verdantartifice.primalmagick.common.items.misc.IPixieItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class PixieHouseOccupantLayer extends RenderLayer<PixieHouseEntity, PixieHouseModel> {
    private static final Map<PixieRank, ResourceLocation> TEXTURES = ImmutableMap.of(
            PixieRank.BASIC, ResourceUtils.loc("textures/entity/pixie/basic_pixie.png"),
            PixieRank.GRAND, ResourceUtils.loc("textures/entity/pixie/grand_pixie.png"),
            PixieRank.MAJESTIC, ResourceUtils.loc("textures/entity/pixie/majestic_pixie.png"));

    private final PixieModel basePixieModel;
    private final PixieModel royalPixieModel;

    public PixieHouseOccupantLayer(RenderLayerParent<PixieHouseEntity, PixieHouseModel> pRenderer, EntityModelSet pModelSet) {
        super(pRenderer);
        this.basePixieModel = new PixieModel(pModelSet.bakeLayer(ModelLayersPM.PIXIE_BASIC));
        this.royalPixieModel = new PixieModel(pModelSet.bakeLayer(ModelLayersPM.PIXIE_ROYAL));
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, PixieHouseEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        ItemStack pixieStack = pLivingEntity.getHousedPixie();
        if (pixieStack.getItem() instanceof IPixieItem pixieItem) {
            PixieRank rank = pixieItem.getPixieRank();
            PixieModel model = rank == PixieRank.MAJESTIC ? this.royalPixieModel : this.basePixieModel;
            double yBob = -0.125D * Mth.sin(pAgeInTicks / 6F);
            pPoseStack.pushPose();
            pPoseStack.translate(0D, -0.25D + yBob, 0D);
            pPoseStack.scale(0.25F, 0.25F, 0.25F);
            model.setupAnim(null, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            VertexConsumer vertexConsumer = pBuffer.getBuffer(model.renderType(TEXTURES.get(rank)));
            model.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY);
            pPoseStack.popPose();
        }
    }
}

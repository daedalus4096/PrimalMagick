package com.verdantartifice.primalmagick.client.renderers.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieHouseModel;
import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.misc.PixieHouseEntity;
import com.verdantartifice.primalmagick.common.items.misc.IPixieItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PixieHouseOccupantLayer extends RenderLayer<PixieHouseEntity, PixieHouseModel> {
    private static final ResourceLocation TEXTURE = ResourceUtils.loc("textures/entity/pixie/basic_pixie.png");

    private final PixieModel pixieModel;

    public PixieHouseOccupantLayer(RenderLayerParent<PixieHouseEntity, PixieHouseModel> pRenderer, EntityModelSet pModelSet) {
        super(pRenderer);
        // TODO Decide when to use the royal pixie layer
        this.pixieModel = new PixieModel(pModelSet.bakeLayer(ModelLayersPM.PIXIE_BASIC));
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, PixieHouseEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        ItemStack pixieStack = pLivingEntity.getHousedPixie();
        if (pixieStack.getItem() instanceof IPixieItem) {
            pPoseStack.pushPose();
            pPoseStack.translate(0D, -0.125D, 0D);
            pPoseStack.scale(0.25F, 0.25F, 0.25F);
//            this.pixieModel.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            VertexConsumer vertexConsumer = pBuffer.getBuffer(this.pixieModel.renderType(TEXTURE));
            this.pixieModel.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY);
            pPoseStack.popPose();
        }
    }
}

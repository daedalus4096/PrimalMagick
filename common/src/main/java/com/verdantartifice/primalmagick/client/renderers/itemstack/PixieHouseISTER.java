package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieHouseModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.items.entities.PixieHouseItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PixieHouseISTER extends BlockEntityWithoutLevelRenderer {
    public static final ResourceLocation TEXTURE = ResourceUtils.loc("entity/pixie_house");
    @SuppressWarnings("deprecation")
    protected static final Material MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, TEXTURE);

    protected PixieHouseModel model;

    public PixieHouseISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {
        this.model = new PixieHouseModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayersPM.PIXIE_HOUSE));
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (pStack.getItem() instanceof PixieHouseItem) {
            pPoseStack.pushPose();
            pPoseStack.scale(1.0F, -1.0F, -1.0F);
            Material renderMaterial = MATERIAL;
            VertexConsumer vertexBuilder = renderMaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(renderMaterial.atlasLocation()), true, pStack.hasFoil()));
            this.model.renderToBuffer(pPoseStack, vertexBuilder, pPackedLight, pPackedOverlay);
            pPoseStack.popPose();
        }
    }
}

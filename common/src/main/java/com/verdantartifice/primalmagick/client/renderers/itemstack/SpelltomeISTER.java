package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagick.common.items.tools.SpelltomeItem;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SpelltomeISTER extends BlockEntityWithoutLevelRenderer {
    protected static final ResourceLocation TEXTURE_APPRENTICE = ResourceUtils.loc("textures/entity/spelltome/apprentice.png");
    protected static final ResourceLocation TEXTURE_ADEPT = ResourceUtils.loc("textures/entity/spelltome/adept.png");
    protected static final ResourceLocation TEXTURE_WIZARD = ResourceUtils.loc("textures/entity/spelltome/wizard.png");
    protected static final ResourceLocation TEXTURE_ARCHMAGE = ResourceUtils.loc("textures/entity/spelltome/archmage.png");

    protected BookModel model;

    public SpelltomeISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {
        this.model = new BookModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.BOOK));
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (pStack.getItem() instanceof SpelltomeItem spelltomeItem) {
            float open = (pDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || pDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) ? 1.0F : 0.0F;
            pPoseStack.pushPose();
            pPoseStack.scale(1.0F, -1.0F, -1.0F);
            this.model.setupAnim(0F, 0.1F, 0.9F, open);
            VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(getTextureLocation(spelltomeItem.getDeviceTier())), false, pStack.hasFoil());
            this.model.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, pPackedOverlay, -1);
            pPoseStack.popPose();
        }
    }

    private static ResourceLocation getTextureLocation(DeviceTier tier) {
        return switch (tier) {
            case BASIC -> TEXTURE_APPRENTICE;
            case ENCHANTED -> TEXTURE_ADEPT;
            case FORBIDDEN -> TEXTURE_WIZARD;
            case HEAVENLY, CREATIVE -> TEXTURE_ARCHMAGE;
        };
    }
}

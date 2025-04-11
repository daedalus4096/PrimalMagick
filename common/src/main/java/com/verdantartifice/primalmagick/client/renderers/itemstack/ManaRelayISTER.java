package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaCubeModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaRelayFrameModel;
import com.verdantartifice.primalmagick.common.blocks.mana.ManaRelayBlock;
import com.verdantartifice.primalmagick.common.items.misc.ManaRelayBlockItem;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ManaRelayISTER extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation CORE_TEXTURE = ResourceUtils.loc("entity/mana_cube");
    private static final Material CORE_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, CORE_TEXTURE);

    private static final ResourceLocation BASIC_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_relay/basic_frame");
    private static final Material BASIC_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, BASIC_FRAME_TEXTURE);

    protected ManaRelayFrameModel frameModel;
    protected ManaCubeModel cubeModel;

    public ManaRelayISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {
        Minecraft mc = Minecraft.getInstance();
        this.frameModel = new ManaRelayFrameModel(mc.getEntityModels().bakeLayer(ModelLayersPM.MANA_RELAY_FRAME));
        this.cubeModel = new ManaCubeModel(mc.getEntityModels().bakeLayer(ModelLayersPM.MANA_CUBE));
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (pStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ManaRelayBlock relayBlock) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            VertexConsumer builder = pBuffer.getBuffer(RenderType.solid());
            final float baseScale = 0.5F;
            final float tilt = 45.0F;

            // TODO Draw the relay frame
            pPoseStack.pushPose();
            pPoseStack.translate(0.5D, 0.5D, 0.5D);
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(tilt));   // Tilt the frame onto its diagonal
            pPoseStack.mulPose(Axis.XP.rotationDegrees(tilt));   // Tilt the frame onto its diagonal
            pPoseStack.scale(baseScale, baseScale, baseScale);
            VertexConsumer frameBuilder = this.getFrameMaterial(relayBlock.getDeviceTier()).buffer(pBuffer, RenderType::entitySolid);
            this.frameModel.renderToBuffer(pPoseStack, frameBuilder, pPackedLight, pPackedOverlay, -1);
            pPoseStack.popPose();

            // TODO Draw the relay core
            final float coreScale = 0.375F;
            pPoseStack.pushPose();
            pPoseStack.translate(0.5D, 0.5D, 0.5D);
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(tilt));   // Tilt the frame onto its diagonal
            pPoseStack.mulPose(Axis.XP.rotationDegrees(tilt));   // Tilt the frame onto its diagonal
            pPoseStack.scale(baseScale, baseScale, baseScale);
            pPoseStack.scale(coreScale, coreScale, coreScale);
            VertexConsumer ringBuilder = CORE_MATERIAL.buffer(pBuffer, RenderType::entitySolid);
            this.cubeModel.renderToBuffer(pPoseStack, ringBuilder, pPackedLight, pPackedOverlay, -1);  // TODO Cycle through colors
            pPoseStack.popPose();
        }
    }

    private Material getFrameMaterial(DeviceTier tier) {
        return switch (tier) {
            default -> BASIC_FRAME_MATERIAL;
        };
    }
}

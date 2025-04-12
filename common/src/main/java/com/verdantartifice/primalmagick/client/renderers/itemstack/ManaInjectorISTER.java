package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaCubeModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingBottomMiddleModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingBottomModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingTopMiddleModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingTopModel;
import com.verdantartifice.primalmagick.common.blocks.mana.ManaInjectorBlock;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ManaInjectorISTER extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation CORE_TEXTURE = ResourceUtils.loc("entity/mana_cube");
    private static final Material CORE_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, CORE_TEXTURE);

    private static final ResourceLocation BASIC_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_injector/basic_frame_top");
    private static final ResourceLocation ENCHANTED_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_injector/enchanted_frame_top");
    private static final ResourceLocation FORBIDDEN_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_injector/forbidden_frame_top");
    private static final ResourceLocation HEAVENLY_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_injector/heavenly_frame_top");
    private static final ResourceLocation BOTTOM_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_injector/frame_bottom");

    private static final Material BASIC_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, BASIC_FRAME_TEXTURE);
    private static final Material ENCHANTED_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, ENCHANTED_FRAME_TEXTURE);
    private static final Material FORBIDDEN_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, FORBIDDEN_FRAME_TEXTURE);
    private static final Material HEAVENLY_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, HEAVENLY_FRAME_TEXTURE);
    private static final Material BOTTOM_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, BOTTOM_FRAME_TEXTURE);

    protected ManaInjectorFrameRingTopModel ringTopModel;
    protected ManaInjectorFrameRingTopMiddleModel ringTopMiddleModel;
    protected ManaInjectorFrameRingBottomMiddleModel ringBottomMiddleModel;
    protected ManaInjectorFrameRingBottomModel ringBottomModel;
    protected ManaCubeModel cubeModel;

    public ManaInjectorISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {
        Minecraft mc = Minecraft.getInstance();
        this.ringTopModel = new ManaInjectorFrameRingTopModel(mc.getEntityModels().bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_TOP));
        this.ringTopMiddleModel = new ManaInjectorFrameRingTopMiddleModel(mc.getEntityModels().bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_TOP_MIDDLE));
        this.ringBottomMiddleModel = new ManaInjectorFrameRingBottomMiddleModel(mc.getEntityModels().bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_BOTTOM_MIDDLE));
        this.ringBottomModel = new ManaInjectorFrameRingBottomModel(mc.getEntityModels().bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_BOTTOM));
        this.cubeModel = new ManaCubeModel(mc.getEntityModels().bakeLayer(ModelLayersPM.MANA_CUBE));
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (pStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ManaInjectorBlock injectorBlock) {
            final float tilt = 45.0F;

            // Render the injector's frame rings
            pPoseStack.pushPose();
            pPoseStack.translate(0.5D, 0D, 0.5D);
            VertexConsumer topFrameBuilder = this.getTopFrameMaterial(injectorBlock.getDeviceTier()).buffer(pBuffer, RenderType::entitySolid);
            VertexConsumer bottomFrameBuilder = BOTTOM_FRAME_MATERIAL.buffer(pBuffer, RenderType::entitySolid);

            pPoseStack.pushPose();
            pPoseStack.translate(0D, 1.875D, 0D);
            this.ringTopModel.renderToBuffer(pPoseStack, topFrameBuilder, pPackedLight, pPackedOverlay, -1);
            pPoseStack.popPose();

            pPoseStack.pushPose();
            pPoseStack.translate(0D, 1.375D, 0D);
            this.ringTopMiddleModel.renderToBuffer(pPoseStack, bottomFrameBuilder, pPackedLight, pPackedOverlay, -1);
            pPoseStack.popPose();

            pPoseStack.pushPose();
            pPoseStack.translate(0D, 0.875D, 0D);
            this.ringBottomMiddleModel.renderToBuffer(pPoseStack, bottomFrameBuilder, pPackedLight, pPackedOverlay, -1);
            pPoseStack.popPose();

            pPoseStack.pushPose();
            pPoseStack.translate(0D, 0.375D, 0D);
            this.ringBottomModel.renderToBuffer(pPoseStack, bottomFrameBuilder, pPackedLight, pPackedOverlay, -1);
            pPoseStack.popPose();

            pPoseStack.popPose();

            // Draw the injector core
            final float coreScale = 0.1875F;
            pPoseStack.pushPose();
            pPoseStack.translate(0.5D, 0.75D, 0.5D);
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(tilt));   // Tilt the frame onto its diagonal
            pPoseStack.mulPose(Axis.XP.rotationDegrees(tilt));   // Tilt the frame onto its diagonal
            pPoseStack.scale(coreScale, coreScale, coreScale);
            VertexConsumer ringBuilder = CORE_MATERIAL.buffer(pBuffer, RenderType::entitySolid);
            this.cubeModel.renderToBuffer(pPoseStack, ringBuilder, pPackedLight, pPackedOverlay, Sources.SKY.getColor());
            pPoseStack.popPose();
        }
    }

    private Material getTopFrameMaterial(DeviceTier tier) {
        return switch (tier) {
            case BASIC -> BASIC_FRAME_MATERIAL;
            case ENCHANTED -> ENCHANTED_FRAME_MATERIAL;
            case FORBIDDEN -> FORBIDDEN_FRAME_MATERIAL;
            case HEAVENLY, CREATIVE -> HEAVENLY_FRAME_MATERIAL;
        };
    }
}

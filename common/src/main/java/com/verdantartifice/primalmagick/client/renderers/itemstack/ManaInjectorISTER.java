package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaCubeModel;
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

    private static final ResourceLocation BASIC_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_injector/basic_frame");
    private static final ResourceLocation ENCHANTED_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_injector/enchanted_frame");
    private static final ResourceLocation FORBIDDEN_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_injector/forbidden_frame");
    private static final ResourceLocation HEAVENLY_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_injector/heavenly_frame");

    private static final Material BASIC_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, BASIC_FRAME_TEXTURE);
    private static final Material ENCHANTED_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, ENCHANTED_FRAME_TEXTURE);
    private static final Material FORBIDDEN_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, FORBIDDEN_FRAME_TEXTURE);
    private static final Material HEAVENLY_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, HEAVENLY_FRAME_TEXTURE);

    protected ManaCubeModel cubeModel;

    public ManaInjectorISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {
        Minecraft mc = Minecraft.getInstance();
        // TODO Load frame model
        this.cubeModel = new ManaCubeModel(mc.getEntityModels().bakeLayer(ModelLayersPM.MANA_CUBE));
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (pStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ManaInjectorBlock inejctorBlock) {
            final float tilt = 45.0F;

            // TODO Draw the injector frame

            // Draw the injector core
            final float coreScale = 0.1875F;
            pPoseStack.pushPose();
            pPoseStack.translate(0.5D, 1.125D, 0.5D);
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(tilt));   // Tilt the frame onto its diagonal
            pPoseStack.mulPose(Axis.XP.rotationDegrees(tilt));   // Tilt the frame onto its diagonal
            pPoseStack.scale(coreScale, coreScale, coreScale);
            VertexConsumer ringBuilder = CORE_MATERIAL.buffer(pBuffer, RenderType::entitySolid);
            this.cubeModel.renderToBuffer(pPoseStack, ringBuilder, pPackedLight, pPackedOverlay, Sources.SKY.getColor());
            pPoseStack.popPose();
        }
    }

    private Material getFrameMaterial(DeviceTier tier) {
        return switch (tier) {
            case BASIC -> BASIC_FRAME_MATERIAL;
            case ENCHANTED -> ENCHANTED_FRAME_MATERIAL;
            case FORBIDDEN -> FORBIDDEN_FRAME_MATERIAL;
            case HEAVENLY, CREATIVE -> HEAVENLY_FRAME_MATERIAL;
        };
    }
}

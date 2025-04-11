package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaCubeModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaRelayFrameModel;
import com.verdantartifice.primalmagick.common.blocks.mana.ManaRelayBlock;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaRelayTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ManaRelayTER implements BlockEntityRenderer<ManaRelayTileEntity> {
    public static final ResourceLocation CORE_TEXTURE = ResourceUtils.loc("entity/mana_cube");
    private static final Material CORE_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, CORE_TEXTURE);

    public static final ResourceLocation BASIC_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_relay/basic_frame");
    private static final Material BASIC_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, BASIC_FRAME_TEXTURE);

    protected ManaRelayFrameModel frameModel;
    protected final ManaCubeModel manaCubeModel;

    public ManaRelayTER(BlockEntityRendererProvider.Context context) {
        this.frameModel = new ManaRelayFrameModel(context.bakeLayer(ModelLayersPM.MANA_RELAY_FRAME));
        this.manaCubeModel = new ManaCubeModel(context.bakeLayer(ModelLayersPM.MANA_CUBE));
    }

    @Override
    public void render(ManaRelayTileEntity manaRelayTileEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        BlockState state = manaRelayTileEntity.getBlockState();
        DeviceTier tier = state.getBlock() instanceof ManaRelayBlock relayBlock ? relayBlock.getDeviceTier() : DeviceTier.BASIC;
        long time = manaRelayTileEntity.getLevel().getLevelData().getGameTime();
        int rot = (int)(time % 360);
        final float baseScale = 0.5F;

        // TODO Render the relay's frame
        poseStack.pushPose();
        poseStack.translate(0.5D, 0D, 0.5D);
//        poseStack.mulPose(Axis.YP.rotationDegrees(-rot));            // Spin the frame around its Y-axis
//        poseStack.mulPose(Axis.ZP.rotationDegrees(45.0F));  // Tilt the frame onto its diagonal
//        poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));  // Tilt the frame onto its diagonal
        poseStack.scale(baseScale, baseScale, baseScale);
        VertexConsumer frameBuilder = this.getFrameMaterial(tier).buffer(multiBufferSource, RenderType::entitySolid);
        this.frameModel.renderToBuffer(poseStack, frameBuilder, combinedLight, combinedOverlay, -1);
        poseStack.popPose();

        // TODO Render the relay's core
        final float coreScale = 0.375F;
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.3125D, 0.5D);
//        poseStack.mulPose(Axis.YP.rotationDegrees(rot));             // Spin the core around its Y-axis
//        poseStack.mulPose(Axis.ZP.rotationDegrees(45.0F));  // Tilt the core onto its diagonal
//        poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));  // Tilt the core onto its diagonal
        poseStack.scale(baseScale, baseScale, baseScale);
        poseStack.scale(coreScale, coreScale, coreScale);
        VertexConsumer ringBuilder = CORE_MATERIAL.buffer(multiBufferSource, RenderType::entitySolid);
        this.manaCubeModel.renderToBuffer(poseStack, ringBuilder, combinedLight, combinedOverlay, -1);  // TODO Cycle through colors
        poseStack.popPose();
    }

    protected Material getFrameMaterial(DeviceTier tier) {
        return switch (tier) {
            default -> BASIC_FRAME_MATERIAL;
        };
    }
}

package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaCubeModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingBottomMiddleModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingBottomModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingTopMiddleModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingTopModel;
import com.verdantartifice.primalmagick.common.blocks.mana.ManaInjectorBlock;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaInjectorTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Math;

public class ManaInjectorTER implements BlockEntityRenderer<ManaInjectorTileEntity> {
    public static final ResourceLocation CORE_TEXTURE = ResourceUtils.loc("entity/mana_cube");
    private static final Material CORE_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, CORE_TEXTURE);

    public static final ResourceLocation BASIC_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_injector/basic_frame_top");
    public static final ResourceLocation ENCHANTED_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_injector/enchanted_frame_top");
    public static final ResourceLocation FORBIDDEN_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_injector/forbidden_frame_top");
    public static final ResourceLocation HEAVENLY_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_injector/heavenly_frame_top");
    public static final ResourceLocation BOTTOM_FRAME_TEXTURE = ResourceUtils.loc("entity/mana_injector/frame_bottom");

    private static final Material BASIC_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, BASIC_FRAME_TEXTURE);
    private static final Material ENCHANTED_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, ENCHANTED_FRAME_TEXTURE);
    private static final Material FORBIDDEN_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, FORBIDDEN_FRAME_TEXTURE);
    private static final Material HEAVENLY_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, HEAVENLY_FRAME_TEXTURE);
    private static final Material BOTTOM_FRAME_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, BOTTOM_FRAME_TEXTURE);

    protected final ManaInjectorFrameRingTopModel ringTopModel;
    protected final ManaInjectorFrameRingTopMiddleModel ringTopMiddleModel;
    protected final ManaInjectorFrameRingBottomMiddleModel ringBottomMiddleModel;
    protected final ManaInjectorFrameRingBottomModel ringBottomModel;
    protected final ManaCubeModel manaCubeModel;

    public ManaInjectorTER(BlockEntityRendererProvider.Context context) {
        this.ringTopModel = new ManaInjectorFrameRingTopModel(context.bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_TOP));
        this.ringTopMiddleModel = new ManaInjectorFrameRingTopMiddleModel(context.bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_TOP_MIDDLE));
        this.ringBottomMiddleModel = new ManaInjectorFrameRingBottomMiddleModel(context.bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_BOTTOM_MIDDLE));
        this.ringBottomModel = new ManaInjectorFrameRingBottomModel(context.bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_BOTTOM));
        this.manaCubeModel = new ManaCubeModel(context.bakeLayer(ModelLayersPM.MANA_CUBE));
    }

    protected static final int DIP_DURATION = 8;
    protected static final int CYCLE_DURATION = 40;

    @Override
    public void render(ManaInjectorTileEntity manaInjectorTileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        DeviceTier tier = manaInjectorTileEntity.getDeviceTier();
        long time = manaInjectorTileEntity.getLevel().getLevelData().getGameTime();
        int rot = 2 * (int)(time % 360);
        final float tilt = 45.0F;
        final int coreColor = manaInjectorTileEntity.getCurrentColor(partialTicks);

        // Render the injector's frame rings
        poseStack.pushPose();
        poseStack.translate(0.5D, 0D, 0.5D);
        VertexConsumer topFrameBuilder = this.getTopFrameMaterial(tier).buffer(multiBufferSource, RenderType::entitySolid);
        VertexConsumer bottomFrameBuilder = BOTTOM_FRAME_MATERIAL.buffer(multiBufferSource, RenderType::entitySolid);

        poseStack.pushPose();
        poseStack.translate(0D, 1.875D + this.getDipAmount(time, partialTicks, 0), 0D);
        this.ringTopModel.renderToBuffer(poseStack, topFrameBuilder, combinedLight, combinedOverlay, -1);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0D, 1.375D + this.getDipAmount(time, partialTicks, 4), 0D);
        this.ringTopMiddleModel.renderToBuffer(poseStack, bottomFrameBuilder, combinedLight, combinedOverlay, -1);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0D, 0.875D + this.getDipAmount(time, partialTicks, 8), 0D);
        this.ringBottomMiddleModel.renderToBuffer(poseStack, bottomFrameBuilder, combinedLight, combinedOverlay, -1);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0D, 0.375D + this.getDipAmount(time, partialTicks, 12), 0D);
        this.ringBottomModel.renderToBuffer(poseStack, bottomFrameBuilder, combinedLight, combinedOverlay, -1);
        poseStack.popPose();

        poseStack.popPose();

        // Render the relay's core
        final float coreScale = 0.1875F;
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.75D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));    // Spin the core around its Y-axis
        poseStack.mulPose(Axis.ZP.rotationDegrees(tilt));   // Tilt the core onto its diagonal
        poseStack.mulPose(Axis.XP.rotationDegrees(tilt));   // Tilt the core onto its diagonal
        poseStack.scale(coreScale, coreScale, coreScale);
        VertexConsumer ringBuilder = CORE_MATERIAL.buffer(multiBufferSource, RenderType::entitySolid);
        this.manaCubeModel.renderToBuffer(poseStack, ringBuilder, combinedLight, combinedOverlay, coreColor);
        poseStack.popPose();

        // Draw a particle stream rising from the core
        FxDispatcher.INSTANCE.spellcraftingGlow(manaInjectorTileEntity.getBlockPos(), 0.75D, coreColor);
    }

    protected double getDipAmount(long gameTime, float partialTicks, int dipStartTime) {
        double cycleTime = (gameTime % CYCLE_DURATION) + (double)partialTicks;
        if (cycleTime >= dipStartTime && cycleTime <= (dipStartTime + DIP_DURATION)) {
            return -0.125D * Math.sin((cycleTime - dipStartTime) * (Math.PI / (double)DIP_DURATION));
        } else {
            return 0D;
        }
    }

    protected Material getTopFrameMaterial(DeviceTier tier) {
        return switch (tier) {
            case BASIC -> BASIC_FRAME_MATERIAL;
            case ENCHANTED -> ENCHANTED_FRAME_MATERIAL;
            case FORBIDDEN -> FORBIDDEN_FRAME_MATERIAL;
            case HEAVENLY, CREATIVE -> HEAVENLY_FRAME_MATERIAL;
        };
    }
}

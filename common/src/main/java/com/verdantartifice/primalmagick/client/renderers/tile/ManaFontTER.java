package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaCubeModel;
import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.tiles.mana.AbstractManaFontTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom tile entity renderer for mana font blocks.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.blocks.mana.AncientManaFontBlock
 */
public class ManaFontTER implements BlockEntityRenderer<AbstractManaFontTileEntity, ManaFontTER.RenderState> {
    public static final SpriteId CORE = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_cube"));

    protected final ManaCubeModel manaCubeModel;
    private final BlockEntityRendererProvider.Context context;
    
    public ManaFontTER(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.manaCubeModel = new ManaCubeModel(context.bakeLayer(ModelLayersPM.MANA_CUBE));
    }
    
    @Override
    @NotNull
    public ManaFontTER.RenderState createRenderState() {
        return new ManaFontTER.RenderState();
    }

    @Override
    public void extractRenderState(@NotNull AbstractManaFontTileEntity blockEntity, @NotNull ManaFontTER.RenderState state,
                                   float partialTicks, @NotNull Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.coreColor = blockEntity.getBlockState().getBlock() instanceof AbstractManaFontBlock fontBlock ? fontBlock.getSource().getColor() : 0;
        state.coreRotation = blockEntity.getLevel() == null ? 0 : (blockEntity.getLevel().getLevelData().getGameTime() % 360) + partialTicks;
        state.coreScale = (float)blockEntity.getMana() / Math.max(2000.0F, (float)blockEntity.getManaCapacity());   // Shrink the core as it holds less mana
    }

    @Override
    public void submit(@NotNull ManaFontTER.RenderState renderState, @NotNull PoseStack poseStack,
                       @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        final float baseScale = 0.375F;
        final float coreScale = renderState.coreScale;

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.coreRotation));    // Spin the core around its Y-axis
        poseStack.mulPose(Axis.ZP.rotationDegrees(45.0F));  // Tilt the core onto its diagonal
        poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));  // Tilt the core onto its diagonal
        poseStack.scale(baseScale, baseScale, baseScale);
        poseStack.scale(coreScale, coreScale, coreScale);
        submitNodeCollector.submitModelPart(this.manaCubeModel.root(), poseStack, CORE.renderType(RenderTypes::entitySolid),
                renderState.lightCoords, OverlayTexture.NO_OVERLAY, this.context.sprites().get(CORE), renderState.coreColor,
                renderState.breakProgress);
        poseStack.popPose();
    }

    public static class RenderState extends BlockEntityRenderState {
        public int coreColor;
        public float coreRotation;
        public float coreScale;
    }
}

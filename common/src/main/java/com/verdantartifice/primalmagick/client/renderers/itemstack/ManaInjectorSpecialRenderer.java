package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaCubeModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingBottomMiddleModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingBottomModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingTopMiddleModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingTopModel;
import com.verdantartifice.primalmagick.common.blocks.mana.ManaInjectorBlock;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * Custom item stack renderer for a mana injector.
 *
 * @author Daedalus4096
 */
public class ManaInjectorSpecialRenderer implements SpecialModelRenderer<DeviceTier> {
    private static final Identifier CORE_TEXTURE = ResourceUtils.loc("textures/entity/mana_cube.png");

    private static final Identifier BASIC_FRAME_TEXTURE = ResourceUtils.loc("textures/entity/mana_injector/basic_frame_top.png");
    private static final Identifier ENCHANTED_FRAME_TEXTURE = ResourceUtils.loc("textures/entity/mana_injector/enchanted_frame_top.png");
    private static final Identifier FORBIDDEN_FRAME_TEXTURE = ResourceUtils.loc("textures/entity/mana_injector/forbidden_frame_top.png");
    private static final Identifier HEAVENLY_FRAME_TEXTURE = ResourceUtils.loc("textures/entity/mana_injector/heavenly_frame_top.png");
    private static final Identifier BOTTOM_FRAME_TEXTURE = ResourceUtils.loc("textures/entity/mana_injector/frame_bottom.png");

    protected ManaInjectorFrameRingTopModel ringTopModel;
    protected ManaInjectorFrameRingTopMiddleModel ringTopMiddleModel;
    protected ManaInjectorFrameRingBottomMiddleModel ringBottomMiddleModel;
    protected ManaInjectorFrameRingBottomModel ringBottomModel;
    protected final ManaCubeModel cubeModel;
    protected final double cycleTime;

    public ManaInjectorSpecialRenderer(ManaInjectorFrameRingTopModel ringTopModel, ManaInjectorFrameRingTopMiddleModel ringTopMiddleModel,
                                       ManaInjectorFrameRingBottomMiddleModel ringBottomMiddleModel, ManaInjectorFrameRingBottomModel ringBottomModel,
                                       ManaCubeModel cubeModel, double cycleTime) {
        this.ringTopModel = ringTopModel;
        this.ringTopMiddleModel = ringTopMiddleModel;
        this.ringBottomMiddleModel = ringBottomMiddleModel;
        this.ringBottomModel = ringBottomModel;
        this.cubeModel = cubeModel;
        this.cycleTime = cycleTime;
    }

    private Identifier getTopFrameTexture(@Nullable DeviceTier tier) {
        return tier == null ? BASIC_FRAME_TEXTURE : switch (tier) {
            case BASIC -> BASIC_FRAME_TEXTURE;
            case ENCHANTED -> ENCHANTED_FRAME_TEXTURE;
            case FORBIDDEN -> FORBIDDEN_FRAME_TEXTURE;
            case HEAVENLY, CREATIVE -> HEAVENLY_FRAME_TEXTURE;
        };
    }

    @Override
    public void submit(
            @Nullable DeviceTier tier,
            @NotNull PoseStack poseStack,
            @NotNull SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            int overlayCoords,
            boolean hasFoil,
            int outlineColor) {
        // Render the injector's frame rings
        poseStack.pushPose();
        poseStack.translate(0D, 1.875D, 0D);
        submitNodeCollector.submitModelPart(this.ringTopModel.root(), poseStack, this.ringTopModel.renderType(this.getTopFrameTexture(tier)), lightCoords, overlayCoords, null, false, hasFoil, -1, null, outlineColor);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0D, 1.375D, 0D);
        submitNodeCollector.submitModelPart(this.ringTopMiddleModel.root(), poseStack, this.ringTopMiddleModel.renderType(BOTTOM_FRAME_TEXTURE), lightCoords, overlayCoords, null, false, hasFoil, -1, null, outlineColor);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0D, 0.875D, 0D);
        submitNodeCollector.submitModelPart(this.ringBottomMiddleModel.root(), poseStack, this.ringBottomMiddleModel.renderType(BOTTOM_FRAME_TEXTURE), lightCoords, overlayCoords, null, false, hasFoil, -1, null, outlineColor);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0D, 0.375D, 0D);
        submitNodeCollector.submitModelPart(this.ringBottomModel.root(), poseStack, this.ringBottomModel.renderType(BOTTOM_FRAME_TEXTURE), lightCoords, overlayCoords, null, false, hasFoil, -1, null, outlineColor);
        poseStack.popPose();

        // Draw the injector core
        final float tilt = 45.0F;
        final float coreScale = 0.1875F;
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.75D, 0.5D);
        poseStack.mulPose(Axis.ZP.rotationDegrees(tilt));   // Tilt the frame onto its diagonal
        poseStack.mulPose(Axis.XP.rotationDegrees(tilt));   // Tilt the frame onto its diagonal
        poseStack.scale(coreScale, coreScale, coreScale);
        submitNodeCollector.submitModelPart(this.cubeModel.root(), poseStack, this.cubeModel.renderType(CORE_TEXTURE), lightCoords, overlayCoords, null, false, hasFoil, -1, null, outlineColor);
        poseStack.popPose();
    }

    @Override
    public void getExtents(@NotNull Consumer<Vector3fc> consumer) {
        PoseStack poseStack = new PoseStack();
        this.ringTopModel.root().getExtentsForGui(poseStack, consumer);
        this.ringTopMiddleModel.root().getExtentsForGui(poseStack, consumer);
        this.ringBottomMiddleModel.root().getExtentsForGui(poseStack, consumer);
        this.ringBottomModel.root().getExtentsForGui(poseStack, consumer);
    }

    @Override
    public @Nullable DeviceTier extractArgument(@NotNull ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ManaInjectorBlock injectorBlock) {
            return injectorBlock.getDeviceTier();
        } else {
            return null;
        }
    }

    public record Unbaked(double cycleTime) implements SpecialModelRenderer.Unbaked<DeviceTier> {
        public static final MapCodec<ManaInjectorSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.DOUBLE.fieldOf("cycleTime").forGetter(ManaInjectorSpecialRenderer.Unbaked::cycleTime)
            ).apply(instance, ManaInjectorSpecialRenderer.Unbaked::new));

        @Override
        public ManaInjectorSpecialRenderer bake(@NotNull BakingContext bakingContext) {
            return new ManaInjectorSpecialRenderer(
                    new ManaInjectorFrameRingTopModel(bakingContext.entityModelSet().bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_TOP)),
                    new ManaInjectorFrameRingTopMiddleModel(bakingContext.entityModelSet().bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_TOP_MIDDLE)),
                    new ManaInjectorFrameRingBottomMiddleModel(bakingContext.entityModelSet().bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_BOTTOM_MIDDLE)),
                    new ManaInjectorFrameRingBottomModel(bakingContext.entityModelSet().bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_BOTTOM)),
                    new ManaCubeModel(bakingContext.entityModelSet().bakeLayer(ModelLayersPM.MANA_CUBE)),
                    this.cycleTime
            );
        }

        @Override
        @NotNull
        public MapCodec<ManaInjectorSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}

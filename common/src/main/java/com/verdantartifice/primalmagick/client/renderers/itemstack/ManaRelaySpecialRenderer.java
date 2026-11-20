package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaCubeModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaRelayFrameModel;
import com.verdantartifice.primalmagick.common.blocks.mana.ManaRelayBlock;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * Custom item stack renderer for a mana relay.
 */
public class ManaRelaySpecialRenderer implements SpecialModelRenderer<DeviceTier> {
    // These sprites are stitched into the blocks atlas under entity/...; the block entities mapper supplies that prefix
    private static final SpriteId CORE_TEXTURE = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_cube"));

    private static final SpriteId BASIC_FRAME_TEXTURE = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_relay/basic_frame"));
    private static final SpriteId ENCHANTED_FRAME_TEXTURE = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_relay/enchanted_frame"));
    private static final SpriteId FORBIDDEN_FRAME_TEXTURE = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_relay/forbidden_frame"));
    private static final SpriteId HEAVENLY_FRAME_TEXTURE = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_relay/heavenly_frame"));

    private final SpriteGetter sprites;
    protected final ManaRelayFrameModel frameModel;
    protected final ManaCubeModel cubeModel;

    public ManaRelaySpecialRenderer(SpriteGetter sprites, ManaRelayFrameModel frameModel, ManaCubeModel cubeModel) {
        this.sprites = sprites;
        this.frameModel = frameModel;
        this.cubeModel = cubeModel;
    }

    private static SpriteId getFrameTexture(@Nullable DeviceTier tier) {
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
        final float baseScale = 0.5F;
        final float tilt = 45.0F;

        // Draw the relay frame
        SpriteId frameTexture = getFrameTexture(tier);
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.ZP.rotationDegrees(tilt));   // Tilt the frame onto its diagonal
        poseStack.mulPose(Axis.XP.rotationDegrees(tilt));   // Tilt the frame onto its diagonal
        poseStack.scale(baseScale, baseScale, baseScale);
        submitNodeCollector.submitModelPart(this.frameModel.root(), poseStack, frameTexture.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords,
                this.sprites.get(frameTexture), false, hasFoil, -1, null, outlineColor);
        poseStack.popPose();

        // Draw the relay core
        final float coreScale = 0.375F;
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.ZP.rotationDegrees(tilt));   // Tilt the core onto its diagonal
        poseStack.mulPose(Axis.XP.rotationDegrees(tilt));   // Tilt the core onto its diagonal
        poseStack.scale(baseScale, baseScale, baseScale);
        poseStack.scale(coreScale, coreScale, coreScale);
        submitNodeCollector.submitModelPart(this.cubeModel.root(), poseStack, CORE_TEXTURE.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords,
                this.sprites.get(CORE_TEXTURE), false, hasFoil, ARGB.opaque(Sources.SKY.getColor()), null, outlineColor);
        poseStack.popPose();
    }

    @Override
    public void getExtents(@NotNull Consumer<Vector3fc> consumer) {
        PoseStack poseStack = new PoseStack();
        this.frameModel.root().getExtentsForGui(poseStack, consumer);
        this.cubeModel.root().getExtentsForGui(poseStack, consumer);
    }

    @Override
    public @Nullable DeviceTier extractArgument(@NotNull ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ManaRelayBlock relayBlock) {
            return relayBlock.getDeviceTier();
        } else {
            return null;
        }
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<DeviceTier> {
        public static final ManaRelaySpecialRenderer.Unbaked INSTANCE = new ManaRelaySpecialRenderer.Unbaked();
        public static final MapCodec<ManaRelaySpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public ManaRelaySpecialRenderer bake(@NotNull BakingContext bakingContext) {
            return new ManaRelaySpecialRenderer(
                    bakingContext.sprites(),
                    new ManaRelayFrameModel(bakingContext.entityModelSet().bakeLayer(ModelLayersPM.MANA_RELAY_FRAME)),
                    new ManaCubeModel(bakingContext.entityModelSet().bakeLayer(ModelLayersPM.MANA_CUBE))
            );
        }

        @Override
        @NotNull
        public MapCodec<ManaRelaySpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}

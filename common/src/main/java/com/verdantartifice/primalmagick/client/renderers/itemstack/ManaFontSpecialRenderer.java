package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

import java.awt.Color;
import java.util.function.Consumer;

/**
 * Custom item stack renderer for ancient mana fonts.
 *
 * @see com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock
 */
public class ManaFontSpecialRenderer implements SpecialModelRenderer<Source> {
    private static final SpriteId CORE_SPRITE = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_font_core"));

    private final SpriteGetter sprites;

    public ManaFontSpecialRenderer(SpriteGetter sprites) {
        this.sprites = sprites;
    }

    private static void addVertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float r, float g, float b, float u, float v, int lightCoords, int overlayCoords) {
        consumer.addVertex(pose.pose(), x, y, z)
                .setColor(r, g, b, 1.0F)
                .setUv(u, v)
                .setOverlay(overlayCoords)
                .setLight(LightCoordsUtil.FULL_BRIGHT)   // The core always glows, regardless of ambient light
                .setNormal(pose, 1, 0, 0);
    }

    @Override
    public void submit(
            @Nullable Source source,
            @NotNull PoseStack poseStack,
            @NotNull SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            int overlayCoords,
            boolean hasFoil,
            int outlineColor) {
        // Draw the font core; the font base is drawn by the sibling model in the item definition's composite
        Color sourceColor = new Color(source == null ? Sources.EARTH.getColor() : source.getColor());
        final float r = sourceColor.getRed() / 255.0F;
        final float g = sourceColor.getGreen() / 255.0F;
        final float b = sourceColor.getBlue() / 255.0F;
        final float ds = 0.1875F;
        final TextureAtlasSprite sprite = this.sprites.get(CORE_SPRITE);

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.ZP.rotationDegrees(45.0F)); // Tilt the core onto its diagonal
        poseStack.mulPose(Axis.XP.rotationDegrees(45.0F)); // Tilt the core onto its diagonal
        submitNodeCollector.submitCustomGeometry(poseStack, Sheets.cutoutBlockSheet(), (pose, consumer) -> {
            // Draw the south face of the core
            addVertex(consumer, pose, -ds, ds, ds, r, g, b, sprite.getU0(), sprite.getV1(), lightCoords, overlayCoords);
            addVertex(consumer, pose, -ds, -ds, ds, r, g, b, sprite.getU0(), sprite.getV0(), lightCoords, overlayCoords);
            addVertex(consumer, pose, ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV0(), lightCoords, overlayCoords);
            addVertex(consumer, pose, ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1(), lightCoords, overlayCoords);

            // Draw the north face of the core
            addVertex(consumer, pose, -ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV1(), lightCoords, overlayCoords);
            addVertex(consumer, pose, ds, ds, -ds, r, g, b, sprite.getU1(), sprite.getV1(), lightCoords, overlayCoords);
            addVertex(consumer, pose, ds, -ds, -ds, r, g, b, sprite.getU1(), sprite.getV0(), lightCoords, overlayCoords);
            addVertex(consumer, pose, -ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0(), lightCoords, overlayCoords);

            // Draw the east face of the core
            addVertex(consumer, pose, ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV1(), lightCoords, overlayCoords);
            addVertex(consumer, pose, ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1(), lightCoords, overlayCoords);
            addVertex(consumer, pose, ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV0(), lightCoords, overlayCoords);
            addVertex(consumer, pose, ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0(), lightCoords, overlayCoords);

            // Draw the west face of the core
            addVertex(consumer, pose, -ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV0(), lightCoords, overlayCoords);
            addVertex(consumer, pose, -ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1(), lightCoords, overlayCoords);
            addVertex(consumer, pose, -ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV1(), lightCoords, overlayCoords);
            addVertex(consumer, pose, -ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0(), lightCoords, overlayCoords);

            // Draw the top face of the core
            addVertex(consumer, pose, ds, ds, -ds, r, g, b, sprite.getU1(), sprite.getV0(), lightCoords, overlayCoords);
            addVertex(consumer, pose, -ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV0(), lightCoords, overlayCoords);
            addVertex(consumer, pose, -ds, ds, ds, r, g, b, sprite.getU0(), sprite.getV1(), lightCoords, overlayCoords);
            addVertex(consumer, pose, ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1(), lightCoords, overlayCoords);

            // Draw the bottom face of the core
            addVertex(consumer, pose, ds, -ds, -ds, r, g, b, sprite.getU1(), sprite.getV0(), lightCoords, overlayCoords);
            addVertex(consumer, pose, ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV1(), lightCoords, overlayCoords);
            addVertex(consumer, pose, -ds, -ds, ds, r, g, b, sprite.getU0(), sprite.getV1(), lightCoords, overlayCoords);
            addVertex(consumer, pose, -ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0(), lightCoords, overlayCoords);
        });
        poseStack.popPose();
    }

    @Override
    public void getExtents(@NotNull Consumer<Vector3fc> consumer) {
        SubModelRenderHelper.unitCubeExtents(consumer);
    }

    @Override
    public @Nullable Source extractArgument(@NotNull ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractManaFontBlock fontBlock) {
            return fontBlock.getSource();
        } else {
            return null;
        }
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<Source> {
        public static final MapCodec<ManaFontSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(new ManaFontSpecialRenderer.Unbaked());

        @Override
        public ManaFontSpecialRenderer bake(@NotNull BakingContext context) {
            return new ManaFontSpecialRenderer(context.sprites());
        }

        @Override
        @NotNull
        public MapCodec<ManaFontSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}

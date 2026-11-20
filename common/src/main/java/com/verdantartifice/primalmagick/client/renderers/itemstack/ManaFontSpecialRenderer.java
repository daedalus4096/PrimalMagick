package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaCubeModel;
import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
 * Custom item stack renderer for ancient mana fonts.
 *
 * @see com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock
 */
public class ManaFontSpecialRenderer implements SpecialModelRenderer<Source> {
    public static final SpriteId CORE = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_cube"));

    protected final ManaCubeModel manaCubeModel;
    private final SpriteGetter sprites;

    public ManaFontSpecialRenderer(SpriteGetter sprites, ManaCubeModel model) {
        this.sprites = sprites;
        this.manaCubeModel = model;
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
        final int coreColor =  source == null ? Sources.EARTH.getColor() : source.getColor();;
        final float baseScale = 0.375F;

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.ZP.rotationDegrees(45.0F)); // Tilt the core onto its diagonal
        poseStack.mulPose(Axis.XP.rotationDegrees(45.0F)); // Tilt the core onto its diagonal
        poseStack.scale(baseScale, baseScale, baseScale);
        submitNodeCollector.submitModelPart(this.manaCubeModel.root(), poseStack, CORE.renderType(RenderTypes::entitySolid),
                lightCoords, OverlayTexture.NO_OVERLAY, this.sprites.get(CORE), ARGB.opaque(coreColor),
                null);
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
        public static final ManaFontSpecialRenderer.Unbaked INSTANCE = new ManaFontSpecialRenderer.Unbaked();
        public static final MapCodec<ManaFontSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public ManaFontSpecialRenderer bake(@NotNull BakingContext context) {
            return new ManaFontSpecialRenderer(context.sprites(), new ManaCubeModel(context.entityModelSet().bakeLayer(ModelLayersPM.MANA_CUBE)));
        }

        @Override
        @NotNull
        public MapCodec<ManaFontSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}

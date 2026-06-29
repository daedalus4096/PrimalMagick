package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieHouseModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * Custom item stack renderer for a pixie house.
 *
 * @author Daedalus4096
 */
public class PixieHouseSpecialRenderer implements NoDataSpecialModelRenderer {
    public static final Transformation DEFAULT_TRANSFORMATION = new Transformation(
            new Vector3f(0.1F, 0.3F, 0.0F),
            null,
            new Vector3f(0.7F, -0.7F, -0.7F),
            null);
    public static final Identifier TEXTURE = ResourceUtils.loc("textures/entity/pixie_house.png");

    protected final PixieHouseModel model;

    public PixieHouseSpecialRenderer(PixieHouseModel model) {
        this.model = model;
    }

    @Override
    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        submitNodeCollector.submitModelPart(
                this.model.root(),
                poseStack,
                this.model.renderType(TEXTURE),
                lightCoords,
                overlayCoords,
                null,
                false,
                hasFoil,
                -1,
                null,
                outlineColor
        );
    }

    @Override
    public void getExtents(@NotNull Consumer<Vector3fc> consumer) {
        PoseStack poseStack = new PoseStack();
        this.model.root().getExtentsForGui(poseStack, consumer);
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
        public static final PixieHouseSpecialRenderer.Unbaked INSTANCE = new PixieHouseSpecialRenderer.Unbaked();
        public static final MapCodec<PixieHouseSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public PixieHouseSpecialRenderer bake(@NotNull BakingContext bakingContext) {
            return new PixieHouseSpecialRenderer(new PixieHouseModel(bakingContext.entityModelSet().bakeLayer(ModelLayersPM.PIXIE_HOUSE)));
        }

        @Override
        @NotNull
        public MapCodec<PixieHouseSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}

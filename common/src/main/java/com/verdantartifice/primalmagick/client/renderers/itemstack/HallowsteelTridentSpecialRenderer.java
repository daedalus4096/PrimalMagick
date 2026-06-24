package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.TridentModel;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Custom item stack renderer for a hallowsteel trident.
 * 
 * @author Daedalus4096
 */
public class HallowsteelTridentSpecialRenderer extends AbstractTieredTridentSpecialRenderer {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/entity/trident/hallowsteel_trident.png");

    public HallowsteelTridentSpecialRenderer(TridentModel model) {
        super(model);
    }

    @Override
    public Identifier getTextureLocation() {
        return TEXTURE;
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
        public static final HallowsteelTridentSpecialRenderer.Unbaked INSTANCE = new HallowsteelTridentSpecialRenderer.Unbaked();
        public static final MapCodec<HallowsteelTridentSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public HallowsteelTridentSpecialRenderer bake(@NotNull BakingContext context) {
            return new HallowsteelTridentSpecialRenderer(new TridentModel(context.entityModelSet().bakeLayer(ModelLayers.TRIDENT)));
        }

        @Override
        @NotNull
        public MapCodec<HallowsteelTridentSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}

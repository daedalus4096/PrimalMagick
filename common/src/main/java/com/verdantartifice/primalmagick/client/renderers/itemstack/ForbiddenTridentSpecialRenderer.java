package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.TridentModel;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Custom item stack renderer for a forbidden trident.
 * 
 * @author Daedalus4096
 */
public class ForbiddenTridentSpecialRenderer extends AbstractTieredTridentSpecialRenderer {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/entity/trident/forbidden_trident.png");

    public ForbiddenTridentSpecialRenderer(TridentModel model) {
        super(model);
    }

    @Override
    public Identifier getTextureLocation() {
        return TEXTURE;
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
        public static final ForbiddenTridentSpecialRenderer.Unbaked INSTANCE = new ForbiddenTridentSpecialRenderer.Unbaked();
        public static final MapCodec<ForbiddenTridentSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public ForbiddenTridentSpecialRenderer bake(@NotNull BakingContext context) {
            return new ForbiddenTridentSpecialRenderer(new TridentModel(context.entityModelSet().bakeLayer(ModelLayers.TRIDENT)));
        }

        @Override
        @NotNull
        public MapCodec<ForbiddenTridentSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}

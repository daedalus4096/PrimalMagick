package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.TridentModel;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Custom item stack renderer for a primalite trident.
 * 
 * @author Daedalus4096
 */
public class PrimaliteTridentSpecialRenderer extends AbstractTieredTridentSpecialRenderer {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/entity/trident/primalite_trident.png");

    public PrimaliteTridentSpecialRenderer(TridentModel model) {
        super(model);
    }

    @Override
    public Identifier getTextureLocation() {
        return TEXTURE;
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
        public static final PrimaliteTridentSpecialRenderer.Unbaked INSTANCE = new PrimaliteTridentSpecialRenderer.Unbaked();
        public static final MapCodec<PrimaliteTridentSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public PrimaliteTridentSpecialRenderer bake(@NotNull BakingContext context) {
            return new PrimaliteTridentSpecialRenderer(new TridentModel(context.entityModelSet().bakeLayer(ModelLayers.TRIDENT)));
        }

        @Override
        @NotNull
        public MapCodec<PrimaliteTridentSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}

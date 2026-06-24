package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.TridentModel;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Custom item stack renderer for a hexium trident.
 * 
 * @author Daedalus4096
 */
public class HexiumTridentSpecialRenderer extends AbstractTieredTridentSpecialRenderer {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/entity/trident/hexium_trident.png");

    public HexiumTridentSpecialRenderer(TridentModel model) {
        super(model);
    }

    @Override
    public Identifier getTextureLocation() {
        return TEXTURE;
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
        public static final HexiumTridentSpecialRenderer.Unbaked INSTANCE = new HexiumTridentSpecialRenderer.Unbaked();
        public static final MapCodec<HexiumTridentSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public HexiumTridentSpecialRenderer bake(@NotNull BakingContext context) {
            return new HexiumTridentSpecialRenderer(new TridentModel(context.entityModelSet().bakeLayer(ModelLayers.TRIDENT)));
        }

        @Override
        @NotNull
        public MapCodec<HexiumTridentSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}

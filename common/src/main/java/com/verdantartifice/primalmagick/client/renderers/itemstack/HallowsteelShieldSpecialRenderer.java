package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.renderers.SheetsPM;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.equipment.ShieldModel;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.component.DataComponentMap;
import org.jetbrains.annotations.NotNull;

/**
 * Custom item stack renderer for a hallowsteel shield.
 * 
 * @author Daedalus4096
 */
public class HallowsteelShieldSpecialRenderer extends AbstractTieredShieldSpecialRenderer {
    public HallowsteelShieldSpecialRenderer(SpriteGetter sprites, ShieldModel model) {
        super(sprites, model);
    }

    @Override
    protected @NotNull SpriteId getSpriteId(boolean hasPatterns) {
        return hasPatterns ? SheetsPM.HALLOWSTEEL_SHIELD_BASE : SheetsPM.HALLOWSTEEL_SHIELD_BASE_NO_PATTERN;
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<DataComponentMap> {
        public static final HallowsteelShieldSpecialRenderer.Unbaked INSTANCE = new HallowsteelShieldSpecialRenderer.Unbaked();
        public static final MapCodec<HallowsteelShieldSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        @NotNull
        public MapCodec<HallowsteelShieldSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        public HallowsteelShieldSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new HallowsteelShieldSpecialRenderer(context.sprites(), new ShieldModel(context.entityModelSet().bakeLayer(ModelLayers.SHIELD)));
        }
    }
}

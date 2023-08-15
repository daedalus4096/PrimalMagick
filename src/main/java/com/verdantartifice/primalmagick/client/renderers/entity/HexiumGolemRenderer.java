package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.renderers.entity.layers.HexiumGolemCracksLayer;
import com.verdantartifice.primalmagick.common.entities.companions.golems.HexiumGolemEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Entity renderer for a hexium golem.
 * 
 * @author Daedalus4096
 */
public class HexiumGolemRenderer extends AbstractEnchantedGolemRenderer<HexiumGolemEntity> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/entity/hexium_golem/hexium_golem.png");
    
    public HexiumGolemRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new HexiumGolemCracksLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(HexiumGolemEntity entity) {
        return TEXTURE;
    }
}

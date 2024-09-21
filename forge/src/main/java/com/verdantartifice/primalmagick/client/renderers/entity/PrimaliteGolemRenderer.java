package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.renderers.entity.layers.PrimaliteGolemCracksLayer;
import com.verdantartifice.primalmagick.common.entities.companions.golems.PrimaliteGolemEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Entity renderer for a primalite golem.
 * 
 * @author Daedalus4096
 */
public class PrimaliteGolemRenderer extends AbstractEnchantedGolemRenderer<PrimaliteGolemEntity> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/entity/primalite_golem/primalite_golem.png");
    
    public PrimaliteGolemRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new PrimaliteGolemCracksLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(PrimaliteGolemEntity entity) {
        return TEXTURE;
    }
}

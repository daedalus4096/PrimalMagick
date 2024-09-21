package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.companions.pixies.AbstractPixieEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Entity renderer for a grand pixie.
 * 
 * @author Daedalus4096
 */
public class GrandPixieRenderer extends AbstractPixieRenderer {
    private static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/entity/pixie/grand_pixie.png");
    
    public GrandPixieRenderer(EntityRendererProvider.Context context) {
        super(context, new PixieModel(context.bakeLayer(ModelLayersPM.PIXIE_BASIC)));
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractPixieEntity entity) {
        return TEXTURE;
    }
}

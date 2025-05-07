package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.companions.pixies.AbstractPixieEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Entity renderer for a basic pixie.
 * 
 * @author Daedalus4096
 */
public class BasicPixieRenderer extends AbstractPixieRenderer {
    public static final ResourceLocation TEXTURE = ResourceUtils.loc("textures/entity/pixie/basic_pixie.png");
    
    public BasicPixieRenderer(EntityRendererProvider.Context context) {
        super(context, new PixieModel(context.bakeLayer(ModelLayersPM.PIXIE_BASIC)));
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractPixieEntity entity) {
        return TEXTURE;
    }
}

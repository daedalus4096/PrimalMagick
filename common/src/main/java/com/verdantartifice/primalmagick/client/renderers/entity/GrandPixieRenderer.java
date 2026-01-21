package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieModel;
import com.verdantartifice.primalmagick.client.renderers.entity.state.PixieRenderState;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.AbstractPixieEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Entity renderer for a grand pixie.
 * 
 * @author Daedalus4096
 */
public class GrandPixieRenderer extends AbstractPixieRenderer<AbstractPixieEntity> {
    public static final Identifier TEXTURE = ResourceUtils.loc("textures/entity/pixie/grand_pixie.png");
    
    public GrandPixieRenderer(EntityRendererProvider.Context context) {
        super(context, new PixieModel(context.bakeLayer(ModelLayersPM.PIXIE_BASIC)));
    }

    @Override
    @NotNull
    public Identifier getTextureLocation(PixieRenderState renderState) {
        return TEXTURE;
    }
}

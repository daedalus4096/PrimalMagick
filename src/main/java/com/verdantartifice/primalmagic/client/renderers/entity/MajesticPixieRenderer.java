package com.verdantartifice.primalmagic.client.renderers.entity;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.entity.model.PixieModel;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.AbstractPixieEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

/**
 * Entity renderer for a majestic pixie.
 * 
 * @author Daedalus4096
 */
public class MajesticPixieRenderer extends AbstractPixieRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/pixie/majestic_pixie.png");
    
    public MajesticPixieRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new PixieModel(true));
    }

    @Override
    public ResourceLocation getEntityTexture(AbstractPixieEntity entity) {
        return TEXTURE;
    }
}

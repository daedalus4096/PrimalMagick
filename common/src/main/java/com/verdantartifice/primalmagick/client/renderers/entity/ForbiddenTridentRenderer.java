package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.entities.projectiles.AbstractTridentEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Renderer definition for a thrown forbidden trident.
 * 
 * @author Daedalus4096
 */
public class ForbiddenTridentRenderer extends AbstractTridentRenderer {
    public static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/entity/trident/forbidden_trident.png");

    public ForbiddenTridentRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractTridentEntity entity) {
        return TEXTURE;
    }
}

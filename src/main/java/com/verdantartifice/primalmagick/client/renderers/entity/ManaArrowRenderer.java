package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.entities.projectiles.ManaArrowEntity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Renderer for a mana-tinged arrow entity.
 * 
 * @author Daedalus4096
 */
public class ManaArrowRenderer extends ArrowRenderer<ManaArrowEntity> {
    public static final ResourceLocation MANA_ARROW_LOCATION = PrimalMagick.resource("textures/entity/mana_arrow.png");

    public ManaArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
     }

    @Override
    public ResourceLocation getTextureLocation(ManaArrowEntity p_114482_) {
        return MANA_ARROW_LOCATION;
    }
}

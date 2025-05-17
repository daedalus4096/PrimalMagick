package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.client.renderers.entity.model.GuardianPixieModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.pixies.guardians.AbstractGuardianPixieEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MajesticGuardianPixieRenderer extends AbstractGuardianPixieRenderer {
    public static final ResourceLocation TEXTURE = ResourceUtils.loc("textures/entity/pixie/majestic_pixie.png");

    public MajesticGuardianPixieRenderer(EntityRendererProvider.Context context) {
        super(context, new GuardianPixieModel(context.bakeLayer(ModelLayersPM.PIXIE_ROYAL)));
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractGuardianPixieEntity entity) {
        return TEXTURE;
    }
}

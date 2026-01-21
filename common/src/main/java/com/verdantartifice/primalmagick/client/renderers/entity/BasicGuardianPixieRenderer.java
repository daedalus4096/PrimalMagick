package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieModel;
import com.verdantartifice.primalmagick.client.renderers.entity.state.PixieRenderState;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.pixies.guardians.AbstractGuardianPixieEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class BasicGuardianPixieRenderer extends AbstractPixieRenderer<AbstractGuardianPixieEntity> {
    public static final Identifier TEXTURE = ResourceUtils.loc("textures/entity/pixie/basic_pixie.png");

    public BasicGuardianPixieRenderer(EntityRendererProvider.Context context) {
        super(context, new PixieModel(context.bakeLayer(ModelLayersPM.PIXIE_BASIC)));
    }

    @Override
    @NotNull
    public Identifier getTextureLocation(PixieRenderState renderState) {
        return TEXTURE;
    }
}

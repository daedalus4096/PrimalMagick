package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.common.entities.projectiles.ManaArrowEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Renderer for a mana-tinged arrow entity.
 * 
 * @author Daedalus4096
 */
public class ManaArrowRenderer extends ArrowRenderer<ManaArrowEntity, ArrowRenderState> {
    public static final Identifier MANA_ARROW_LOCATION = ResourceUtils.loc("textures/entity/mana_arrow.png");

    public ManaArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
     }

    @Override
    @NotNull
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    @NotNull
    public Identifier getTextureLocation(@NotNull ArrowRenderState renderState) {
        return MANA_ARROW_LOCATION;
    }
}

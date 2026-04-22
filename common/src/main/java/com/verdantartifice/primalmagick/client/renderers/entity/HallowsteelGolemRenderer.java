package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.client.renderers.entity.layers.HallowsteelGolemCracksLayer;
import com.verdantartifice.primalmagick.client.renderers.entity.state.EnchantedGolemRenderState;
import com.verdantartifice.primalmagick.common.entities.golems.HallowsteelGolemEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Entity renderer for a hallowsteel golem.
 * 
 * @author Daedalus4096
 */
public class HallowsteelGolemRenderer extends AbstractEnchantedGolemRenderer<HallowsteelGolemEntity> {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/entity/hallowsteel_golem/hallowsteel_golem.png");
    
    public HallowsteelGolemRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new HallowsteelGolemCracksLayer(this));
    }

    @Override
    @NotNull
    public Identifier getTextureLocation(EnchantedGolemRenderState state) {
        return TEXTURE;
    }
}

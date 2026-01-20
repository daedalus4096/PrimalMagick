package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.client.renderers.entity.layers.PrimaliteGolemCracksLayer;
import com.verdantartifice.primalmagick.client.renderers.entity.state.EnchantedGolemRenderState;
import com.verdantartifice.primalmagick.common.entities.golems.PrimaliteGolemEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Entity renderer for a primalite golem.
 * 
 * @author Daedalus4096
 */
public class PrimaliteGolemRenderer extends AbstractEnchantedGolemRenderer<PrimaliteGolemEntity> {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/entity/primalite_golem/primalite_golem.png");
    
    public PrimaliteGolemRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new PrimaliteGolemCracksLayer(this));
    }

    @Override
    @NotNull
    public Identifier getTextureLocation(EnchantedGolemRenderState state) {
        return TEXTURE;
    }
}

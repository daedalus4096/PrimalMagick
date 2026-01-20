package com.verdantartifice.primalmagick.client.renderers.entity;

import com.verdantartifice.primalmagick.client.renderers.entity.layers.HexiumGolemCracksLayer;
import com.verdantartifice.primalmagick.client.renderers.entity.state.EnchantedGolemRenderState;
import com.verdantartifice.primalmagick.common.entities.golems.HexiumGolemEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Entity renderer for a hexium golem.
 * 
 * @author Daedalus4096
 */
public class HexiumGolemRenderer extends AbstractEnchantedGolemRenderer<HexiumGolemEntity> {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/entity/hexium_golem/hexium_golem.png");
    
    public HexiumGolemRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new HexiumGolemCracksLayer(this));
    }

    @Override
    @NotNull
    public Identifier getTextureLocation(EnchantedGolemRenderState state) {
        return TEXTURE;
    }
}

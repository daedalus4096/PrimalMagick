package com.verdantartifice.primalmagick.client.renderers.entity.layers;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.client.renderers.entity.model.EnchantedGolemModel;
import com.verdantartifice.primalmagick.client.renderers.entity.state.EnchantedGolemRenderState;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Crackiness;

import java.util.Map;

/**
 * Layer renderer for damage cracks on a hexium golem.
 * 
 * @author Daedalus4096
 */
public class HexiumGolemCracksLayer extends AbstractEnchantedGolemCracksLayer {
    protected static final Map<Crackiness.Level, Identifier> TEXTURES = ImmutableMap.<Crackiness.Level, Identifier>builder()
            .put(Crackiness.Level.LOW, ResourceUtils.loc("textures/entity/hexium_golem/hexium_golem_crackiness_low.png"))
            .put(Crackiness.Level.MEDIUM, ResourceUtils.loc("textures/entity/hexium_golem/hexium_golem_crackiness_medium.png"))
            .put(Crackiness.Level.HIGH, ResourceUtils.loc("textures/entity/hexium_golem/hexium_golem_crackiness_high.png"))
            .build();
    
    public HexiumGolemCracksLayer(RenderLayerParent<EnchantedGolemRenderState, EnchantedGolemModel> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    protected Map<Crackiness.Level, Identifier> getTextureMap() {
        return TEXTURES;
    }
}

package com.verdantartifice.primalmagick.client.renderers.entity.layers;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.client.renderers.entity.model.EnchantedGolemModel;
import com.verdantartifice.primalmagick.common.entities.golems.AbstractEnchantedGolemEntity.Cracks;
import com.verdantartifice.primalmagick.common.entities.golems.HexiumGolemEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

/**
 * Layer renderer for damage cracks on a hexium golem.
 * 
 * @author Daedalus4096
 */
public class HexiumGolemCracksLayer extends AbstractEnchantedGolemCracksLayer<HexiumGolemEntity> {
    protected static final Map<Cracks, ResourceLocation> TEXTURES = ImmutableMap.<Cracks, ResourceLocation>builder()
            .put(Cracks.LOW, ResourceUtils.loc("textures/entity/hexium_golem/hexium_golem_crackiness_low.png"))
            .put(Cracks.MEDIUM, ResourceUtils.loc("textures/entity/hexium_golem/hexium_golem_crackiness_medium.png"))
            .put(Cracks.HIGH, ResourceUtils.loc("textures/entity/hexium_golem/hexium_golem_crackiness_high.png"))
            .build();
    
    public HexiumGolemCracksLayer(RenderLayerParent<HexiumGolemEntity, EnchantedGolemModel<HexiumGolemEntity>> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    protected Map<Cracks, ResourceLocation> getTextureMap() {
        return TEXTURES;
    }
}

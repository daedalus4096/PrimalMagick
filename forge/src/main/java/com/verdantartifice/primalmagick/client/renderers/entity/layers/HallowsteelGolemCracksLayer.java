package com.verdantartifice.primalmagick.client.renderers.entity.layers;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.renderers.entity.model.EnchantedGolemModel;
import com.verdantartifice.primalmagick.common.entities.companions.golems.AbstractEnchantedGolemEntity.Cracks;
import com.verdantartifice.primalmagick.common.entities.companions.golems.HallowsteelGolemEntity;

import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;

/**
 * Layer renderer for damage cracks on a hallowsteel golem.
 * 
 * @author Daedalus4096
 */
public class HallowsteelGolemCracksLayer extends AbstractEnchantedGolemCracksLayer<HallowsteelGolemEntity> {
    protected static final Map<Cracks, ResourceLocation> TEXTURES = ImmutableMap.<Cracks, ResourceLocation>builder()
            .put(Cracks.LOW, PrimalMagick.resource("textures/entity/hallowsteel_golem/hallowsteel_golem_crackiness_low.png"))
            .put(Cracks.MEDIUM, PrimalMagick.resource("textures/entity/hallowsteel_golem/hallowsteel_golem_crackiness_medium.png"))
            .put(Cracks.HIGH, PrimalMagick.resource("textures/entity/hallowsteel_golem/hallowsteel_golem_crackiness_high.png"))
            .build();
    
    public HallowsteelGolemCracksLayer(RenderLayerParent<HallowsteelGolemEntity, EnchantedGolemModel<HallowsteelGolemEntity>> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    protected Map<Cracks, ResourceLocation> getTextureMap() {
        return TEXTURES;
    }
}

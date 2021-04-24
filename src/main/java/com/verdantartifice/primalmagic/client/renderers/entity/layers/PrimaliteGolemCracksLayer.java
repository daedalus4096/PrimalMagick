package com.verdantartifice.primalmagic.client.renderers.entity.layers;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.entity.model.EnchantedGolemModel;
import com.verdantartifice.primalmagic.common.entities.companions.golems.AbstractEnchantedGolemEntity.Cracks;
import com.verdantartifice.primalmagic.common.entities.companions.golems.PrimaliteGolemEntity;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.util.ResourceLocation;

/**
 * Layer renderer for damage cracks on a primalite golem.
 * 
 * @author Daedalus4096
 */
public class PrimaliteGolemCracksLayer extends AbstractEnchantedGolemCracksLayer<PrimaliteGolemEntity> {
    protected static final Map<Cracks, ResourceLocation> TEXTURES = ImmutableMap.<Cracks, ResourceLocation>builder()
            .put(Cracks.LOW, new ResourceLocation(PrimalMagic.MODID, "textures/entity/primalite_golem/primalite_golem_crackiness_low.png"))
            .put(Cracks.MEDIUM, new ResourceLocation(PrimalMagic.MODID, "textures/entity/primalite_golem/primalite_golem_crackiness_medium.png"))
            .put(Cracks.HIGH, new ResourceLocation(PrimalMagic.MODID, "textures/entity/primalite_golem/primalite_golem_crackiness_high.png"))
            .build();
    
    public PrimaliteGolemCracksLayer(IEntityRenderer<PrimaliteGolemEntity, EnchantedGolemModel<PrimaliteGolemEntity>> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    protected Map<Cracks, ResourceLocation> getTextureMap() {
        return TEXTURES;
    }
}

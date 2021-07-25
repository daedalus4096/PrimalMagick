package com.verdantartifice.primalmagic.client.renderers.entity;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.entity.layers.PrimaliteGolemCracksLayer;
import com.verdantartifice.primalmagic.common.entities.companions.golems.PrimaliteGolemEntity;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Entity renderer for a primalite golem.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class PrimaliteGolemRenderer extends AbstractEnchantedGolemRenderer<PrimaliteGolemEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/primalite_golem/primalite_golem.png");
    
    public PrimaliteGolemRenderer(EntityRenderDispatcher rendererManager) {
        super(rendererManager);
        this.addLayer(new PrimaliteGolemCracksLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(PrimaliteGolemEntity entity) {
        return TEXTURE;
    }
}
